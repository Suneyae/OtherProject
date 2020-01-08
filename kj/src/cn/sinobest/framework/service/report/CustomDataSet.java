 package cn.sinobest.framework.service.report;
 
 import cn.sinobest.framework.comm.iface.IReportService;
 import cn.sinobest.framework.util.Util;
 import com.runqian.report4.dataset.DataSet;
 import com.runqian.report4.dataset.IDataSetFactory;
 import com.runqian.report4.usermodel.Context;
 import com.runqian.report4.usermodel.DataSetConfig;
 
 public class CustomDataSet
   implements IDataSetFactory
 {
   public DataSet createDataSet(Context ctx, DataSetConfig dsc, boolean retrieve)
   {
     IReportService service = (IReportService)Util.getBean("reportService");
     return service.createDataSet(ctx, dsc, retrieve);
   }
 }