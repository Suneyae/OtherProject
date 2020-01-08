package cn.sinobest.framework.service;

import cn.sinobest.framework.comm.exception.AppException;
import cn.sinobest.framework.comm.iface.IDTO;
import cn.sinobest.framework.dao.CommDAO;
import cn.sinobest.framework.service.entities.ProcedureInfo;
import cn.sinobest.framework.service.entities.ProcedureInfo.Param;
import cn.sinobest.framework.util.Util;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class DirectJdbcService {
	@Autowired
	private CommDAO commDAO;
	static Logger log = Logger.getLogger(DirectJdbcService.class);

	static enum ProcedureColumnsName {
		NULL, PROCEDURE_CAT, PROCEDURE_SCHEM, PROCEDURE_NAME, COLUMN_NAME, COLUMN_TYPE, DATA_TYPE, TYPE_NAME, PRECISION, LENGTH, SCALE, RADIX, NULLABLE, REMARKS, SEQUENCE, OVERLOAD, DEFAULT_VALUE;
	}

	public Map<String, Object> savePointProcedure(IDTO dto) throws Exception {
		Map<String, Object> parameters = dto.getData();
		return Util.doProcedure((String) parameters.get(""), parameters, null);
	}

	public Map<String, Object> doProcedure(String procedureName, final Map<String, Object> parameters) {
		final ProcedureInfo pi = getProcedureInfo(procedureName.toUpperCase());
		log.debug("存储过程" + procedureName + "的调用语句是" + pi.getCallSql());
		JdbcTemplate jdbcTemplate = new JdbcTemplate(this.commDAO.getDataSource());
		Map<String, Object> result = (Map) jdbcTemplate.execute(pi.getCallSql(), new CallableStatementCallback() {
			public Map<String, Object> doInCallableStatement(CallableStatement callableStatement)
					throws SQLException, DataAccessException {
				DirectJdbcService.this.bindParameters(pi, callableStatement, parameters);
				callableStatement.execute();

				int index = 1;
				Map<String, Object> map = new HashMap();
				for (ProcedureInfo.Param param : pi.getParams()) {
					switch (param.getColumnType()) {
					case 2:
					case 4:
					case 5:
						Object rtn = callableStatement.getObject(index);
						map.put(param.getName(), rtn);
					}
					index++;
				}
				return map;
			}
		});
		return result;
	}

	public void bindParameters(ProcedureInfo procedureInfo, CallableStatement callableStatement, Map<String, ?> values)
			throws SQLException {
		int index = 1;
		for (ProcedureInfo.Param param : procedureInfo.getParams()) {
			if (1 != param.getColumnType()) {
				callableStatement.registerOutParameter(index, param.getDataType());
			}
			if (4 != param.getColumnType()) {
				callableStatement.setObject(index, values.get(param.getName()), param.getDataType());
			}
			index++;
		}
	}

	public String extractCallable(ProcedureInfo procedureInfo) {
		StringBuilder sql = new StringBuilder(20).append('{');

		int i = 0;
		if ((!procedureInfo.getParams().isEmpty())
				&& (5 == ((ProcedureInfo.Param) procedureInfo.getParams().get(0)).getColumnType())) {
			sql.append(" ? = ");
			i = 1;
		}
		sql.append("call ");

		sql.append(procedureInfo.getName()).append(' ');
		if (!procedureInfo.getParams().isEmpty()) {
			sql.append('(');
			for (; i < procedureInfo.getParams().size(); i++) {
				sql.append("?,");
			}
			sql.replace(sql.length() - 1, sql.length(), ")");
		}
		sql.append('}');
		return sql.toString();
	}

	public ProcedureInfo getProcedureInfo(String procedureName) {
		Connection conn = null;
		ResultSet rs = null;
		try {
			conn = this.commDAO.getDataSource().getConnection();
			DatabaseMetaData databaseMetaData = conn.getMetaData();
			rs = databaseMetaData.getProcedures(null, databaseMetaData.getUserName(), procedureName);
			if (!rs.next()) {
				throw new AppException("DirectJdbcService.getProcedureInfo.notfound", null,
						new Object[] { procedureName });
			}
			rs = databaseMetaData.getProcedureColumns(null, databaseMetaData.getUserName(), procedureName, null);
			ProcedureInfo pi = new ProcedureInfo();
			pi.setName(procedureName);
			List<ProcedureInfo.Param> params = new ArrayList();
			pi.setParams(params);
			while (rs.next()) {
				ProcedureInfo.Param param = new ProcedureInfo.Param();
				param.setColumnType(rs.getInt(ProcedureColumnsName.COLUMN_TYPE.ordinal()));
				param.setDataType(rs.getInt(ProcedureColumnsName.DATA_TYPE.ordinal()));
				param.setName(rs.getString(ProcedureColumnsName.COLUMN_NAME.ordinal()));
				param.setTypeName(rs.getString(ProcedureColumnsName.TYPE_NAME.ordinal()));
				pi.getParams().add(param);
			}
			pi.setCallSql(extractCallable(pi));
			return pi;
		} catch (AppException e) {
			throw e;
		} catch (SQLException e) {
			throw new AppException("DirectJdbcService.getProcedureInfo.error", e, new Object[] { procedureName });
		} finally {
			if (rs != null) {
				try {
					rs.close();
					rs = null;
				} catch (SQLException localSQLException3) {
				}
			}
			if (conn != null) {
				try {
					conn.close();
					conn = null;
				} catch (SQLException localSQLException4) {
				}
			}
		}
	}
}
