package cn.sinobest.framework.service.tags;

import cn.sinobest.framework.comm.exception.AppException;
import cn.sinobest.framework.comm.iface.IDAO;
import cn.sinobest.framework.util.ConfUtil;
import cn.sinobest.framework.util.Util;
import com.opensymphony.xwork2.util.ValueStack;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GtService {
	@Autowired
	IDAO commDAO;
	@Autowired
	GltService gltService;

	public static class DictComparator implements Comparator<Map<String, String>> {
		private StringComparator stringComparator = new StringComparator();
		public static final String KEY_ITEM_VALUE = "AAA102";

		public int compare(Map<String, String> o1, Map<String, String> o2) {
			return this.stringComparator.compare((String) o1.get("AAA102"), (String) o2.get("AAA102"));
		}
	}

	private static final DictComparator DICTCOMPARATOR = new DictComparator();
	private static final String EMPTY_STR = "&nbsp;";
	private static final String LABEL = "label";
	private static final String TYPE = "type";
	private static final String COLSPAN = "colspan";
	private static final String ROWSPAN = "rowspan";
	private static final String PNAME = "pname";
	private static final String DNAME = "dname";
	private static final String GT = "gt";

	public static List<String> parseHiddenStr(String source) {
		if ((source == null) || (source.trim().length() == 0)) {
			return null;
		}
		return Arrays.asList(source.split("\\|"));
	}

	private static Map<String, Object> extractLabel8Composite(Map<String, Object> source) {
		Map<String, Object> labelCompoment = new HashMap(source);

		labelCompoment.put("type", Gt.GtType.LABEL);
		labelCompoment.remove("pname");
		int i_colspan = ((Integer) source.get("colspan")).intValue();
		switch (i_colspan) {
		case 0:
			break;
		default:
			labelCompoment.put("colspan", Integer.valueOf(1));
		}
		return labelCompoment;
	}

	public List<List<List<Map<String, Object>>>> parseRenderStr(String source) throws AppException {
		List<List<List<Map<String, Object>>>> row_list = null;
		if ((source == null) || (source.trim().length() == 0)) {
			return null;
		}
		source = source.replaceAll("\n", "");
		String[] rows = source.split("\\|\\|");
		row_list = new ArrayList(rows.length);
		for (int i = 0; i < rows.length; i++) {
			String[] cols = rows[i].split("\\|");
			List<List<Map<String, Object>>> column_list = new ArrayList(cols.length);

			List<Map<String, Object>> compoment_list = new ArrayList(1);
			for (int j = 0; j < cols.length; j++) {
				Map<String, Object> col_rst = new HashMap();
				String[] attrs = cols[j].split("\\*");
				for (int k = 0; k < attrs.length; k++) {
					String attr = attrs[k];
					int pos = attr.indexOf('=');
					if (pos == -1) {
						throw new AppException("EFW0107", null, new Object[] { attr });
					}
					col_rst.put(attr.substring(0, pos).trim(), attr.substring(pos + 1).trim());
				}
				if (!col_rst.containsKey("type")) {
					throw new AppException("EFW0108", null, new Object[] { cols[j] });
				}
				String type = col_rst.get("type").toString();
				if (type.length() > 2) {
					col_rst.put("subtype", type.substring(2));
				} else if (type.length() != 2) {
					throw new AppException("EFW0109", null, new Object[] { type });
				}
				Gt.GtType t = Gt.GtType.valueOfCode(type.substring(0, 2));
				col_rst.put("type", t);

				t.evalExt(col_rst);
				if (Gt.GtType.NESTTABLE == t) {
					Gt nestGt = getGt((String) col_rst.get("gt"));
					col_rst.put("gt", nestGt);
				}
				if (!col_rst.containsKey("rowspan")) {
					col_rst.put("rowspan", Integer.valueOf(1));
				} else {
					try {
						int i_rowspan = Integer.parseInt((String) col_rst.get("rowspan"));
						if (i_rowspan == 0) {
							throw new AppException("EFW0110", null, new Object[] { cols[j] });
						}
						col_rst.put("rowspan", Integer.valueOf(i_rowspan));
					} catch (NumberFormatException e) {
						throw new AppException("EFW0111", null, new Object[] { cols[j] });
					}
				}
				if (!col_rst.containsKey("colspan")) {
					col_rst.put("colspan", Integer.valueOf(1));
				} else {
					try {
						int i_colspan = Integer.parseInt((String) col_rst.get("colspan"));
						if ((i_colspan == 0) && (j == 0)) {
							throw new AppException("EFW0112", null, new Object[] { cols[j] });
						}
						col_rst.put("colspan", Integer.valueOf(i_colspan));
					} catch (NumberFormatException e) {
						throw new AppException("EFW0113", null, new Object[] { cols[j] });
					}
				}
				String vld = (String) col_rst.get("vld");
				col_rst.put("nClass", "");
				if (!Util.isEmpty(vld)) {
					if (vld.indexOf("nn") > -1) {
						col_rst.put("nClass", "tdprompt_n");
					}
					if (vld.indexOf("sn") > -1) {
						col_rst.put("nClass", "tdprompt_sn");
					}
				}
				if (((Integer) col_rst.get("colspan")).intValue() != 0) {
					if ((t.isComposite()) && (col_rst.containsKey("label"))) {
						compoment_list = new ArrayList(1);
						Map<String, Object> labelCompoment = extractLabel8Composite(col_rst);
						compoment_list.add(labelCompoment);
						column_list.add(compoment_list);
					}
					compoment_list = new ArrayList(1);
					compoment_list.add(col_rst);
					column_list.add(compoment_list);
				} else {
					if ((t.isComposite()) && (col_rst.containsKey("label"))) {
						Map<String, Object> labelCompoment = extractLabel8Composite(col_rst);
						compoment_list.add(labelCompoment);
					}
					compoment_list.add(col_rst);
				}
			}
			row_list.add(column_list);
		}
		return row_list;
	}

	private static int compomentColSpan(Map<String, Object> compoment) {
		return ((Integer) compoment.get("colspan")).intValue();
	}

	private static int compomentRowSpan(Map<String, Object> compoment) {
		return ((Integer) compoment.get("rowspan")).intValue();
	}

	public static void fillCollumn(Gt gt)
   {
     int rowIndex = 0;
     List<List<List<Map<String, Object>>>> renders = gt.getRenders();
     
     int[] colNums = new int[renders.size()];
     int colSpan;
     for (List<List<Map<String, Object>>> row : renders)
     {
       int rowSpan;
       int tmpColNum;
       for (Iterator localIterator2 = row.iterator(); localIterator2.hasNext(); tmpColNum < rowSpan)
       {
         col = (List)localIterator2.next();
         colSpan = compomentColSpan((Map)col.get(0));
         rowSpan = compomentRowSpan((Map)col.get(0));
         if (rowSpan + rowIndex > colNums.length) {
           throw new AppException("EFW0114", null, new Object[] { Integer.valueOf(rowSpan) });
         }
         tmpColNum = 0; continue;
         colNums[(rowIndex + tmpColNum)] += colSpan;tmpColNum++;
       }
       rowIndex++;
     }
     int maxColNum = 0;
     List<Map<String, Object>> localList1 = (colSpan = colNums).length;
     for (List<Map<String, Object>> col = 0; col < localList1; col++)
     {
       int iColNum = colSpan[col];
       maxColNum = Math.max(maxColNum, iColNum);
     }
     gt.setColNum(maxColNum);
     for (rowIndex = 0; rowIndex < colNums.length; rowIndex++) {
       if (colNums[rowIndex] != maxColNum)
       {
         Object empty_td_list = new ArrayList(
           1);
         Map<String, Object> empty_td = new HashMap();
         empty_td.put("label", "&nbsp;");
         empty_td.put("colspan", Integer.valueOf(maxColNum - colNums[rowIndex]));
         empty_td.put("type", Gt.GtType.LABEL);
         empty_td.put("rowspan", Integer.valueOf(1));
         ((List)empty_td_list).add(empty_td);
         ((List)renders.get(rowIndex)).add(empty_td_list);
       }
     }
   }

	public static String parseWhereCls(String whereCls, String query) {
		return whereCls.replace("?", query.replaceAll("'", "''"));
	}

	public Map<String, Object[]> prepareGlt(Gt gt, Map<String, String> whereClsMap, Map<String, Object> valueMap,
			Map<String, Object[]> gltMap) throws AppException {
		if (gltMap == null) {
			gltMap = new HashMap();
		}
		if (whereClsMap == null) {
			whereClsMap = Collections.EMPTY_MAP;
		}
		Iterator localIterator2;
		for (Iterator localIterator1 = gt.getRenders().iterator(); localIterator1.hasNext(); localIterator2.hasNext()) {
			List<List<Map<String, Object>>> row = (List) localIterator1.next();

			localIterator2 = row.iterator();
			continue;
			List<Map<String, Object>> col = (List) localIterator2.next();
			for (Map<String, Object> comp : col) {
				if (Gt.GtType.NESTTABLE == comp.get("type")) {
					gltMap.putAll(prepareGlt((Gt) comp.get("gt"), whereClsMap, valueMap, gltMap));
				} else if (((Gt.GtType.COMBOBOX == comp.get("type")) || (Gt.GtType.INPUT == comp.get("type"))
						|| (Gt.GtType.TEXTAREA == comp.get("type"))) && ("q".equals(comp.get("subtype")))) {
					String glt_id = (String) comp.get("glt");
					if (!gltMap.containsKey(glt_id)) {
						Glt glt_conf = null;
						Object[] comp_glt = new Object[2];
						gltMap.put(glt_id, comp_glt);

						glt_conf = this.gltService.getGlt(glt_id);
						comp_glt[0] = glt_conf;
						if (valueMap.get(comp.get("pname")) != null) {
							Object tmp_code = valueMap.get(comp.get("pname"));
							if (tmp_code != null) {
								String code = null;
								if (tmp_code.getClass().isArray()) {
									if ((Array.getLength(tmp_code) == 0) || (Array.get(tmp_code, 0) == null)) {
										continue;
									}
									code = Array.get(tmp_code, 0).toString();
								} else {
									code = tmp_code.toString();
								}
								if (!"u".equals(glt_conf.getType())) {
									String whereCls = (String) whereClsMap.get(glt_id);
									if (whereCls == null) {
										whereCls = "1=1";
									} else {
										whereCls = parseWhereCls((String) whereClsMap.get(glt_id), code);
									}
									List<Map<String, String>> rows = this.gltService.getRowData(glt_conf, whereCls,
											whereClsMap, 1, 1);
									if ((row != null) && (!row.isEmpty())) {
										comp_glt[1] = rows.get(0);
									}
								}
							}
						}
					}
				}
			}
		}
		return gltMap;
	}

	public Map<String, Map<String, Map<String, String>>> getDicts(Gt gt, Map<String, Object> datas,
			Map<String, String> whereClsMap, Map<String, Map<String, Map<String, String>>> dictsMap)
					throws AppException {
		if (dictsMap == null) {
			dictsMap = new HashMap();
		}
		if (whereClsMap == null) {
			whereClsMap = Collections.EMPTY_MAP;
		}
		Iterator localIterator2;
		for (Iterator localIterator1 = gt.getRenders().iterator(); localIterator1.hasNext(); localIterator2.hasNext()) {
			List<List<Map<String, Object>>> row = (List) localIterator1.next();

			localIterator2 = row.iterator();
			continue;
			List<Map<String, Object>> col = (List) localIterator2.next();
			for (Map<String, Object> comp : col) {
				if (Gt.GtType.NESTTABLE == comp.get("type")) {
					getDicts((Gt) comp.get("gt"), datas, whereClsMap, dictsMap);
				} else if (comp.containsKey("dname")) {
					String value = (String) comp.get("dname");
					if (!dictsMap.containsKey(value)) {
						Map<String, Map<String, String>> dictMap = null;
						if (value.startsWith("dyndict_")) {
							if ((datas != null) && (datas.containsKey(value))) {
								dictMap = (Map) datas.get(value);
							}
							if (dictMap == null) {
								dictMap = ConfUtil.getDynDict(value, (String) whereClsMap.get(value));
							}
						} else if (value.indexOf('@') == -1) {
							dictMap = ConfUtil.getDict(value);
						} else {
							String[] subs = value.split("\\@");
							dictMap = ConfUtil.getSubDict(subs[0], subs[1]);
						}
						if (dictMap != null) {
							dictsMap.put(value, dictSort(dictMap));
						}
					}
				}
			}
		}
		return dictsMap;
	}

	public static Map<String, Map<String, String>> dictSort(Map<String, Map<String, String>> dictMap) {
		List<Map<String, String>> dictList = new ArrayList(dictMap.values());
		Collections.sort(dictList, DICTCOMPARATOR);

		dictMap = new LinkedHashMap();
		for (Map<String, String> dict : dictList) {
			dictMap.put((String) dict.get("AAA102"), dict);
		}
		return dictMap;
	}

	public Map<String, Object> getData(Gt gt, Map<String, Object> datas, ValueStack stack, Map<String, Object> defv) {
		Collection<String> pnames = requiredPnames4Value(gt, null);
		Map<String, Object> rst = new HashMap(pnames.size(), 1.0F);
		for (String key : pnames) {
			if ((datas != null) && (datas.containsKey(key))) {
				rst.put(key, datas.get(key));
			} else {
				Object tmp = null;
				if (stack != null) {
					tmp = stack.findValue("#parameters['" + key + "']");
				}
				if (tmp != null) {
					rst.put(key, tmp);
				} else if (defv != null) {
					rst.put(key, defv.get(key));
				}
			}
		}
		return rst;
	}

	public static Collection<String> requiredPnames4Value(Gt gt, Collection<String> collectedPnames) {
		if (collectedPnames == null) {
			collectedPnames = new ArrayList();
		}
		if (gt.getHiddens() != null) {
			collectedPnames.addAll(gt.getHiddens());
		}
		Iterator localIterator2;
		for (Iterator localIterator1 = gt.getRenders().iterator(); localIterator1.hasNext(); localIterator2.hasNext()) {
			List<List<Map<String, Object>>> row = (List) localIterator1.next();
			localIterator2 = row.iterator();
			continue;
			List<Map<String, Object>> col = (List) localIterator2.next();
			for (Map<String, Object> comp : col) {
				if (Gt.GtType.NESTTABLE == comp.get("type")) {
					requiredPnames4Value((Gt) comp.get("gt"), collectedPnames);
				} else if ((comp.containsKey("pname")) && (!collectedPnames.contains(comp.get("pname")))) {
					collectedPnames.add((String) comp.get("pname"));
					if ((Gt.GtType.COMBOBOX == comp.get("type")) && ("q".equals(comp.get("subtype")))) {
						collectedPnames.add("_DIC_" + (String) comp.get("pname"));
					}
				}
			}
		}
		return collectedPnames;
	}

	public Map<String, Object> getInitData(String id, Map<String, Object> datas, Map<String, String> whereClsMap,
			Map<String, Map<String, Map<String, String>>> dictsMap, ValueStack stack, Map<String, Object> defv) {
		Gt gt = getGt(id);
		Map<String, Map<String, Map<String, String>>> dicts = getDicts(gt, datas, whereClsMap, dictsMap);
		Map<String, Object> data = getData(gt, datas, stack, defv);
		Map<String, Object[]> gtlMap = prepareGlt(gt, whereClsMap, data, null);
		Map<String, Object> args = new HashMap(3);
		args.put("gt", gt);
		args.put("dicts", dicts);
		args.put("data", data);
		args.put("gtlMap", gtlMap);
		return args;
	}

	public Gt getGt(String id) throws AppException {
		if (ConfUtil.getGenTblConf(id) != null) {
			return (Gt) ConfUtil.getGenTblConf(id);
		}
		Map<String, String> paramsMap = new HashMap();
		paramsMap.put("ID", id);
		Gt gt = new Gt();
		try {
			List<Map<String, Object>> rst = this.commDAO.select("FW_CONFIG.FW_GENTBL_CONF_Q", paramsMap);
			if (rst.isEmpty()) {
				throw new AppException("EFW0115", null, new Object[] { id });
			}
			Map<String, Object> gt_conf = (Map) rst.get(0);
			gt.setId((String) gt_conf.get("ID"));
			gt.setDescription((String) gt_conf.get("DESCRIPTION"));
			gt.setRenders(parseRenderStr((String) gt_conf.get("RENDERSTR")));
			gt.setHiddens(parseHiddenStr((String) gt_conf.get("HIDDENSTR")));
			gt.setBae001((String) gt_conf.get("BAE001"));
			gt.setBae002((String) gt_conf.get("BAE002"));
			gt.setBae003((String) gt_conf.get("BAE003"));
		} catch (AppException e) {
			throw e;
		} catch (Exception e) {
			throw new AppException("EFW0116", null, new Object[] { id });
		}
		fillCollumn(gt);
		ConfUtil.setGenTblConf(id, gt);
		return gt;
	}

	public boolean isSysPermitArchive() {
		String permitArchive = ConfUtil.getSysParamOnly("permitArchive", "");
		if ((permitArchive.equals("")) || (permitArchive.equalsIgnoreCase("true"))) {
			return true;
		}
		return false;
	}

	public String getArchiveUrl() {
		return ConfUtil.getParam("ARCHIVEURL", "");
	}

	public boolean getHasArchive(String id) {
		Map<String, Map<String, String>> dict = null;
		try {
			dict = ConfUtil.getDictKeyItemCode("GT_ARCHIVE");
		} catch (Exception e) {
			dict = null;
			return false;
		}
		if ((dict != null) && (dict.get(id) != null)) {
			return true;
		}
		return false;
	}
}