package cn.sinobest.framework.web;

import cn.sinobest.framework.comm.exception.AppException;
import cn.sinobest.framework.comm.iface.IExportService;
import cn.sinobest.framework.comm.transcation.DataSourceCallBack;
import cn.sinobest.framework.comm.transcation.IDataSourceCallBack;
import cn.sinobest.framework.util.ConfUtil;
import cn.sinobest.framework.util.Util;
import cn.sinobest.framework.web.interceptor.Cleanable;
import java.io.File;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DownFileAction
  extends BaseActionSupport
  implements Cleanable
{
  private static final Logger LOG = LoggerFactory.getLogger(DownFileAction.class);
  private static final long serialVersionUID = 1L;
  private static final String KEY_DSID = "_ds";
  private static final String KEY_SERVICEID = "serviceId";
  private static final String KEY_EXPTYPE = "_expType";
  private static final String KEY_EXPORT = "export";
  private boolean isNoDelete;
  File inputFile;
  
  public File getInputFile()
  {
    return this.inputFile;
  }
  
  public void cleanDo()
    throws Exception
  {
    if ((!this.isNoDelete) && (this.inputFile != null)) {
      this.inputFile.delete();
    }
  }
  
  public String execute()
    throws Exception
  {
    Map<String, Object> map = getData();
    String dsId = (String)map.get("_ds");
    final String serviceId = (String)map.get("serviceId");
    final String expType = (String)getValue("_expType");
    
    this.inputFile = ((File)DataSourceCallBack.execute(dsId, 
      new IDataSourceCallBack()
      {
        public File doAction()
          throws AppException
        {
          long startTime = System.currentTimeMillis();
          String expService = serviceId;
          if (!Util.isEmpty(expType)) {
            expService = ConfUtil.getSysParam("export." + expType, "");
          }
          IExportService service = (IExportService)Util.getBean(expService);
          if (service == null) {
            throw new AppException("EFW0213", null, new Object[] { serviceId });
          }
          try
          {
            return service.exportFile(DownFileAction.this.dto);
          }
          finally
          {
            DownFileAction.LOG.debug("文件导出总共花了" + (System.currentTimeMillis() - startTime) + 
              "毫秒");
          }
        }
      }));
    return "success";
  }
  
  protected boolean isNoDelete()
  {
    return this.isNoDelete;
  }
  
  protected void setNoDelete(boolean isNoDelete)
  {
    this.isNoDelete = isNoDelete;
  }
}
