package cn.sinobest.framework.service;

import cn.sinobest.framework.comm.exception.AppException;
import cn.sinobest.framework.util.ConfUtil;
import cn.sinobest.framework.util.Util;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;

@Service
public class JdbcCallService {
	@Autowired
	private JdbcTemplate jdbcTemplate;
	public static final char PARAM_SEPERATOR = '\032';
	public static final char FIELD_SEPERATOR = '\005';
	private static final Logger LOGGER = LoggerFactory.getLogger(JdbcCallService.class);

	public Map<String, Object> doProcedure(String procedureName, Map<String, ?> values, String dataSource)
			throws AppException {
		String procedureFace = ConfUtil.getParam("PROCEDURE_FACE", "2");
		if (!"1".equals(procedureFace)) {
			Map<String, Object> out = null;
			try {
				SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(this.jdbcTemplate.getDataSource());

				String[] store = procedureName.split("\\.");
				if (store.length > 1) {
					simpleJdbcCall.withCatalogName(store[0]).withProcedureName(store[1]);
				} else {
					simpleJdbcCall.withProcedureName(store[0]);
				}
				SqlParameterSource parameterSource = new MapSqlParameterSource().addValues(values);

				LOGGER.info("Procedure in parameters:" + Util.getMapLogString(values));
				long s = System.currentTimeMillis();
				out = simpleJdbcCall.execute(parameterSource);
				if (LOGGER.isDebugEnabled()) {
					LOGGER.debug("执行过程" + procedureName + "耗时:" + (System.currentTimeMillis() - s) + "(ms)");
				}
				LOGGER.info("Procedure out parameters:" + Util.getMapLogString(out));
			} catch (Exception ex) {
				throw new AppException("调用存储过程{0}出错!,详细:" + ex.getLocalizedMessage(), ex,
						new Object[] { procedureName });
			}
			return out == null ? new HashMap(1) : out;
		}
		return doProcedureFace(procedureName, values, false);
	}

	private String commonDatabaseName(String source) {
		String name = source;
		if ((source != null) && (source.startsWith("DB2"))) {
			name = "DB2";
		} else if (("Sybase SQL Server".equals(source)) || ("Adaptive Server Enterprise".equals(source))
				|| ("ASE".equals(source)) || ("sql server".equalsIgnoreCase(source))) {
			name = "Sybase";
		}
		return name;
	}

	public Map<String, Object> doProcedureFace(String procedureName, Map<String, ?> values, boolean throwFlag)
			throws AppException {
		DataSource dataSource = this.jdbcTemplate.getDataSource();
		Connection con = DataSourceUtils.getConnection(dataSource);
		try {
			String sql = "{call P_FW_PROCEDURE(?,?,?,?,?)}";

			String params = joinParams(values);
			long s = System.currentTimeMillis();
			LOGGER.info("Procedure in parameters:" + Util.getMapLogString(values) + " \n In parametersStr:" + params);
			CallableStatement call = con.prepareCall(sql);
			call.setString(1, procedureName);
			call.setString(2, params);
			call.registerOutParameter(3, 12);
			call.registerOutParameter(4, 12);
			call.registerOutParameter(5, 12);
			call.execute();
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("执行过程" + procedureName + "耗时:" + (System.currentTimeMillis() - s) + "(ms)");
			}
			String outArgs = call.getString(3);
			String fhz = call.getString(4);
			String msg = call.getString(5);
			if ((throwFlag) && (!"1".equals(fhz))) {
				throw new AppException("fhz:" + fhz + ",msg:" + msg);
			}
			Map<String, Object> respMap = splitParams(outArgs);
			respMap.put("PO_FHZ", fhz);
			respMap.put("PO_MSG", msg);
			LOGGER.info(
					"Procedure out parameters:" + Util.getMapLogString(respMap) + " \n Out parametersStr:" + outArgs);

			return respMap;
		} catch (SQLException e) {
			throw new AppException("调用过程出错，详细:" + e.getLocalizedMessage(), e);
		} finally {
			DataSourceUtils.releaseConnection(con, dataSource);
		}
	}

	private Map<String, Object> splitParams(String outParams) {
		if (outParams == null) {
			return new HashMap(1);
		}
		String[] outs = outParams.split(Character.toString('\005'));
		Map<String, Object> outMap = new HashMap(outs.length);
		for (String field : outs) {
			String[] attr = field.split(Character.toString('\032'));
			if (Integer.valueOf(attr[2]).intValue() == 2) {
				if ((attr[1] == null) || ("".equals(attr[1].trim()))) {
					outMap.put(attr[0], null);
				} else {
					outMap.put(attr[0], new BigDecimal(attr[1]));
				}
			} else {
				outMap.put(attr[0], attr[1]);
			}
		}
		return outMap;
	}

	private String joinParams(Map<String, ?> args) {
		StringBuffer paramsBuf = new StringBuffer("");
		String name = "";
		Object value = null;
		String sValue = "";
		for (Map.Entry<String, ?> arg : args.entrySet()) {
			name = (String) arg.getKey();
			value = arg.getValue();
			if (value == null) {
				sValue = "";
			} else {
				sValue = String.valueOf(value);
			}
			paramsBuf.append(name.toUpperCase()).append('\032').append(sValue).append('\005');
		}
		return paramsBuf.toString();
	}

	public <T> T doFunction(String functionName, Map<String, ?> values, String dataSource) throws AppException {
		T t = null;
		try {
			this.jdbcTemplate.setResultsMapCaseInsensitive(true);
			SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(this.jdbcTemplate.getDataSource());
			String[] fun = functionName.split("\\.");
			if (fun.length > 1) {
				simpleJdbcCall.withCatalogName(fun[0]).withFunctionName(fun[1]);
			} else {
				simpleJdbcCall.withFunctionName(fun[0]);
			}
			SqlParameterSource parameterSource = new MapSqlParameterSource().addValues(values);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Function in parameters:" + Util.getMapLogString(values));
			}
			t = (T) simpleJdbcCall.executeFunction(String.class, parameterSource);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Function out parameters:" + t);
			}
		} catch (Exception ex) {
			throw new AppException("调用函数{0}出错!", ex, new Object[] { functionName });
		}
		return t;
	}
}
