package cn.sinobest.framework.web;

import cn.sinobest.framework.comm.iface.IDTO;
import cn.sinobest.framework.comm.iface.IOperator;
import cn.sinobest.framework.service.CommService;
import cn.sinobest.framework.service.json.JSONUtilities;
import cn.sinobest.framework.service.tags.TreeService;
import cn.sinobest.framework.util.DTOUtil;
import cn.sinobest.framework.util.Util;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

@Scope("prototype")
public class AjaxAction extends BaseActionSupport {
	private static Logger log = Logger.getLogger(AjaxAction.class);
	private static final long serialVersionUID = 1L;
	private InputStream inputStream;
	@Autowired
	TreeService treeService;
	@Autowired
	CommService commService;

	public InputStream getInputStream() {
		return this.inputStream;
	}

	public String execute() throws Exception {
			/*url:contextPath+'/ajax.do'
			,data:{parameters:JSON.stringify(this.services),shareArguments:JSON.stringify(this.shareParameters)}
			,dataType :'json'
			,cache:false
			,async:t_async
			,type:'post'
			,error:function (request, textStatus, errorThrown) {
				if(!t_error){
					FWalert('数据请求错误！');	
				}else{
					t_error(request, textStatus, errorThrown);
				}
				}
			,success:function (data, textStatus) {
				//校验业务处理是否正确执行
				if("1"!=data.FHZ){//出错了，弹出错误提醒
					if ("loginTimeout" == data.FHZ) {
						if(window.confirm(data.MSG||'')){
							window.top.location.href=_selfUrl;
						}
					} else {
						if(t_error){
							t_error(data.MSG||'', 'serviceErr', data.MSG);
						}else{
							FWalert(data.MSG||'');
						}
					}
				}else if(!t_success){
				}else{
					t_success(data.RTN);
				}
			}
			,beforeSend:function( ){
				$.data(thisrequest,'msg',showMsg(t_message,-1));
				//createProgressBar();
			}
			,complete:function( ){
				hideMsg($.data(thisrequest,'msg'));
			}
		 });*/
//		var proParams ={
//				PI_AAC002:aac002,
//				PI_AAC002_NEW:aac002n,
//				PI_AAE013:aae013,
//				PI_AAE011:aae011,
//				PI_BAE001:bae001
//				};
////		var mm = new WeiAjax('hha').hint();
////		alert(typeof mm);
//		
//				new Service().appendProc({
//					procName:'pkg_weiyl.updateIDCard',
//					parameters:proParams
//				}).sentAjax('正在进行数据处理',function(datass){
//					if(datass[0]['PO_FHZ'] !='1'){
//						alert(datass[0]['PO_FHZ']+"  "+ datass[0]['PO_MSG']);
//					}else{
//						closeWindow();
//						getListData('glt_sfzxg',' 1=0 ');
//						alert('身份证修改成功');
////						setTimeout(alert('身份证修改成功'),1000);
//					}
//				},function(data){
//					docx();
//					getListData('glt_sfzxg',' 1=0 ');
//					closeWindow();
//					alert("修改失败");},false);
		String inParameters = (String) getValue("parameters");
		String inShareParameters = (String) getValue("shareParameters");
		Map<String, Object> actionResult = new HashMap();
		try {
			Object[] inParams = (Object[]) JSONUtilities.parseJSON(inParameters);
			Map[] params = (Map[]) null;
			if (inParams != null) {
				params = (Map[]) Util.toTypedArray(inParams, Map.class);
			}
			Map<String, Object> shareArguments = null;
			if (inShareParameters.length() > 0) {
				shareArguments = (Map) JSONUtilities.parseJSON(inShareParameters);
			} else {
				shareArguments = new HashMap();
			}
			this.dto.setValue("parameters", params);
			this.dto.setValue("shareParameters", shareArguments);
			Object rtn = this.commService.doAjaxService(this.dto);

			actionResult.put("RTN", rtn);
			actionResult.put("FHZ", "1");
		} catch (Exception e) {
			log.warn(null, e);
			actionResult.put("FHZ", e.getLocalizedMessage());
			actionResult.put("MSG", e.getLocalizedMessage());
			String url = this.request.getRequestURI();
			String operId = "";
			if (DTOUtil.getUserInfo() != null) {
				operId = DTOUtil.getUserInfo().getOperID();
			}
			this.commService.storeException(url, DTOUtil.getDTO(), operId, e);
		}
		StringBuffer rstString = new JSONUtilities(1).parseObject(actionResult);
		this.inputStream = new ByteArrayInputStream(rstString.toString().getBytes());
		return "success";
	}
}
