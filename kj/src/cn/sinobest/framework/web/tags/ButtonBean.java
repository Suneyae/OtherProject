package cn.sinobest.framework.web.tags;

import cn.sinobest.framework.comm.exception.AppException;
import cn.sinobest.framework.comm.transcation.DataSourceCallBack;
import cn.sinobest.framework.comm.transcation.IDataSourceCallBack;
import cn.sinobest.framework.service.tags.Glt;
import cn.sinobest.framework.service.tags.GltService;
import cn.sinobest.framework.service.tags.StringComparator;
import cn.sinobest.framework.util.Util;
import com.opensymphony.xwork2.util.ValueStack;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.components.UIBean;
import org.apache.struts2.views.annotations.StrutsTag;
import org.apache.struts2.views.annotations.StrutsTagAttribute;

@StrutsTag(name = "glt", tldTagClass = "cn.sinobest.framework.web.tags.GltTag", description = "多记录表标签")
public class GltBean extends UIBean {
	public static final String TEMPLATE_GLT = "glt";
	public static final String TEMPLATE_EDITGLT = "editglt";
	public static final String TEMPLATE_NUPGLT = "nupglt";
	public static final String TEMPLATE_SIMPLE = "s_glt";

	static enum PageAlign {
		TOP, BOTTOM, BOTH;
	}

	static enum SubTotalAlign {
		TOP, BOTTOM;
	}

	String editable = "false";
	String showDelete = "true";
	String whereCls = "1=2";
	String width = "95%";
	String colorfun;
	String dsId;
	boolean hasTitle = true;
	String expBtns = "EXCEL,PDF";
	boolean hasPage = true;
	boolean showContent = true;
	String data;
	PageAlign pageAlign = PageAlign.BOTTOM;
	SubTotalAlign subTotalAlign = SubTotalAlign.BOTTOM;
	int pageSize = 20;
	String height = "220";
	String layout;
	GltService gltService;
	Map<String, String> dynDictWhereClsMap;
	private Integer nupRow;

	public GltBean(ValueStack stack, HttpServletRequest request, HttpServletResponse response) {
		super(stack, request, response);
	}

	protected String getDefaultTemplate() {
		if ("true".equalsIgnoreCase(this.editable)) {
			if ((this.nupRow != null) && (this.nupRow.intValue() > 1)) {
				return "nupglt";
			}
			if ("NOFIXCOL".equals(this.layout)) {
				return "editglt_nf";
			}
			if ("MERGEHEADER".equals(this.layout)) {
				return "editglt_mh";
			}
			return "editglt";
		}
		if ("NOFIXCOL".equals(this.layout)) {
			return "glt_nf";
		}
		if ("MERGEHEADER".equals(this.layout)) {
			return "glt_mh";
		}
		return "glt";
	}

	protected void evaluateExtraParams() {
		super.evaluateExtraParams();

		addParameter("width", findString(this.width));
		String dsIdString = null;
		addParameter("hasTitle", Boolean.valueOf(this.hasTitle));
		addParameter("pageAlign", this.pageAlign.toString());
		addParameter("pageSize", Integer.valueOf(this.pageSize));
		addParameter("showContent", Boolean.valueOf(this.showContent));
		addParameter("showDelete", this.showDelete);

		Map<String, Object> gltOptions = new HashMap();

		gltOptions.put("confid", this.id);

		gltOptions.put("nupRow", this.nupRow);
		gltOptions.put("layout", this.layout);
		gltOptions.put("hasPage", Boolean.valueOf(this.hasPage));
		gltOptions.put("url", this.request.getContextPath() + "/gltPage.do");
		gltOptions.put("whereCls", findString(this.whereCls));
		gltOptions.put("dynDictWhereCls", this.dynDictWhereClsMap);
		gltOptions.put("pageSize", Integer.valueOf(this.pageSize));
		gltOptions.put("height", this.height);
		gltOptions.put("width", this.width);

		gltOptions.put("subTotalAlign", this.subTotalAlign.toString().toLowerCase());
		if (this.colorfun != null) {
			gltOptions.put("colorfun", findString(this.colorfun));
		} else {
			gltOptions.put("colorfun", null);
		}
		gltOptions.put("expbtn", findString(this.expBtns));
		if (this.dsId != null) {
			dsIdString = findString(this.dsId);
			gltOptions.put("dsId", dsIdString);
		} else {
			gltOptions.put("dsId", "");
		}
		final List<Glt> gtlSet = new ArrayList(1);
		if ((!Util.isEmpty(dsIdString)) && (!"ds".equalsIgnoreCase(dsIdString))) {
			gtlSet.add(this.gltService.getGlt(this.id));
		}
		Map<String, Object> datas = (Map) DataSourceCallBack.execute(dsIdString, new IDataSourceCallBack() {
			public Map<String, Object> doAction() throws AppException {
				List<Map<String, Object>> listData = null;
				boolean isCusData = false;
				if (GltBean.this.data != null) {
					isCusData = true;
					listData = (List) GltBean.this.findValue(GltBean.this.data, List.class);
				}
				return GltBean.this.gltService.getInitData(gtlSet, GltBean.this.id,
						GltBean.this.findString(GltBean.this.whereCls), GltBean.this.dynDictWhereClsMap, 1,
						GltBean.this.pageSize, isCusData, listData);
			}
		});
		Glt glt = (Glt) gtlSet.remove(0);
		if (datas != null) {
			if ("true".equalsIgnoreCase(this.editable)) {
				datas.put("dicts", getDicsForEdit(glt));
			}
		}
		gltOptions.put("data", datas);
		if ("true".equalsIgnoreCase(this.editable)) {
			addParameter("lastHeaderRow", getLastRowForEdit(glt));
		} else {
			addParameter("lastHeaderRow", GltService.lastRow(glt));
		}
		if (!"FULL".equals(this.layout)) {
			addParameter("mergeHeader", GltService.mergeHeader(glt));
		}
		gltOptions.put("frozenColumns", Integer.valueOf(glt.getFixcolumns()));
		gltOptions.put("columns", Integer.valueOf(glt.getColumns()));

		addParameter("glt", glt);
		addParameter("options", gltOptions);
	}

	private Map<String, Object>[] getLastRowForEdit(Glt glt) {
		Map[] lastRowPrev = GltService.lastRow(glt);

		Map[] lastRowNew = new Map[lastRowPrev.length];
		String TYPE = "type";
		String TYPE_DEFAULT = "02";
		String SUBTYPE = "subtype";
		String SUBTYPE_DEFAULT = " ";
		String READONLY = "readOnly";
		String VLD = "vld";
		String TIP = "tip";
		String CUS = "cus";
		String V_EMPTY = "";
		String CHK_FIELD = "chk";
		for (int i = 0; i < lastRowPrev.length; i++) {
			Map<String, Object> col = new HashMap(lastRowPrev[i]);

			String type = (String) col.get("type");
			String readOnly = (String) col.get("readOnly");
			col.put("subtype", " ");
			if (type == null) {
				col.put("type", "02");
			} else {
				col.put("type", type.substring(0, 2));
				if (type.length() > 2) {
					col.put("subtype", type.substring(2, 3));
				}
			}
			if ("true".equals(readOnly)) {
				col.put("type", "01");
			}
			if (col.get("chk") == null) {
				col.put("chk", "");
			}
			if (col.get("tip") == null) {
				col.put("tip", "");
			}
			if (col.get("cus") == null) {
				col.put("cus", "");
			}
			if (col.get("vld") == null) {
				col.put("vld", "");
			}
			lastRowNew[i] = col;
		}
		return lastRowNew;
	}

	public Map<String, Map<String, String>> getDicsForEdit(Glt glt) {
		Map<String, Map<String, Map<String, String>>> dicts = GltService.prepareDictData(glt, this.dynDictWhereClsMap);

		Map<String, String> dt = glt.getDict();
		if (dt != null) {
			Map<String, List<String>> revertMap = new HashMap();
			for (Map.Entry<String, String> entry : dt.entrySet()) {
				if (revertMap.get(entry.getValue()) == null) {
					revertMap.put((String) entry.getValue(), new ArrayList());
				}
				((List) revertMap.get(entry.getValue())).add((String) entry.getKey());
			}
			Map<String, Map<String, String>> aliasMaps = new HashMap();
			String key;
			for (Iterator localIterator2 = revertMap.entrySet().iterator(); localIterator2.hasNext(); key.hasNext()) {
				Object entry = (Map.Entry) localIterator2.next();

				Map<String, Map<String, String>> flDict = (Map) dicts
						.get(((List) ((Map.Entry) entry).getValue()).get(0));

				Map<String, String> oneDictMap = null;
				if (flDict != null) {
					List<String> keyList = new ArrayList(flDict.keySet());
					Collections.sort(keyList, new StringComparator());

					oneDictMap = new LinkedHashMap();
					for (Iterator localIterator3 = keyList.iterator(); localIterator3.hasNext();) {
						key = (String) localIterator3.next();
						oneDictMap.put(key, (String) ((Map) flDict.get(key)).get("AAA103"));
					}
				}
				key = ((List) ((Map.Entry) entry).getValue()).iterator();
				continue;
				String alias = (String) key.next();
				aliasMaps.put(alias, oneDictMap);
			}
			return aliasMaps;
		}
		return null;
	}

	public void setEditable(String editable) {
		this.editable = editable;
	}

	public void setShowDelete(String showDelete) {
		this.showDelete = showDelete;
	}

	@StrutsTagAttribute(description = "设置多记录表查询条件", type = "String", defaultValue = "1=2")
	public void setWhereCls(String whereCls) {
		this.whereCls = whereCls;
	}

	@StrutsTagAttribute(description = "设置多记录表宽度", type = "String", defaultValue = "98%")
	public void setWidth(String width) {
		this.width = width;
	}

	@StrutsTagAttribute(description = "设置多记录表宽度", type = "String", defaultValue = "98%")
	public void setColorfun(String colorfun) {
		this.colorfun = colorfun;
	}

	@StrutsTagAttribute(description = "外部数据源ID", type = "String")
	public void setDsId(String dsId) {
		this.dsId = dsId;
	}

	@StrutsTagAttribute(description = "是否显示标题", type = "boolean", defaultValue = "true")
	public void setHasTitle(boolean hasTitle) {
		this.hasTitle = hasTitle;
	}

	@StrutsTagAttribute(description = "是否显示分页件", type = "boolean", defaultValue = "true")
	public void setHasPage(boolean hasPage) {
		this.hasPage = hasPage;
	}

	@StrutsTagAttribute(description = "外部自定义数据源", type = "String[][]")
	public void setData(String data) {
		this.data = data;
	}

	@StrutsTagAttribute(description = "分页控件显示位置", type = "PageAlign", defaultValue = "PageAlign.BUTTOM")
	public void setPageAlign(PageAlign pageAlign) {
		this.pageAlign = pageAlign;
	}

	@StrutsTagAttribute(description = "分页大小", type = "int", defaultValue = "20")
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setShowContent(boolean showContent) {
		this.showContent = showContent;
	}

	@StrutsTagAttribute(description = "导出按钮串", type = "String", defaultValue = "EXCEL,CSV,TEXT,PDF")
	public void setExpBtns(String expBtns) {
		String[] acceptableValues = { "CSV", "EXCEL", "PDF", "TEXT", "MUTL", "NEW", "MUTLDB" };
		expBtns = expBtns.trim().toUpperCase();
		if (expBtns.length() == 0) {
			this.expBtns = expBtns;
			return;
		}
		boolean b = false;
		String[] test = expBtns.trim().split(",");
		for (String one : test) {
			b = false;
			for (String btn : acceptableValues) {
				if (btn.equals(one)) {
					b = true;
					break;
				}
			}
			if (!b) {
				throw new AppException("EFW0121", null, new Object[] { expBtns });
			}
		}
		this.expBtns = expBtns.toUpperCase();
	}

	public void setGltService(GltService gltService) {
		this.gltService = gltService;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public void setSubTotalAlign(SubTotalAlign subTotalAlign) {
		this.subTotalAlign = subTotalAlign;
	}

	public void addWhereCls(String id, String whereCls) {
		if (this.dynDictWhereClsMap == null) {
			this.dynDictWhereClsMap = new HashMap();
		}
		this.dynDictWhereClsMap.put(id, whereCls);
	}

	public static void main(String[] args) {
		String[] array = { "CSV", "EXCEL", "PDF", "TEXT" };
		Arrays.sort(array);
		System.out.println(Arrays.asList(array));
	}

	public void setNupRows(Integer nupRow) {
		this.nupRow = nupRow;
	}

	public void setLayout(String layout) {
		this.layout = layout;
	}
}
