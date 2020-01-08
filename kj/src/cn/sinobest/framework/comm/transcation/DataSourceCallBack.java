package cn.sinobest.framework.comm.transcation;

import cn.sinobest.framework.comm.exception.AppException;
import cn.sinobest.framework.service.CommService;
import cn.sinobest.framework.util.Util;

public class DataSourceCallBack {
	public static <T> T execute(String datasouce, IDataSourceCallBack<T> action) throws AppException {
		try {
			AppContextHolder.setCustomerType(Util.isEmpty(datasouce) ? "ds" : datasouce);
			CommService commService = (CommService) Util.getBean("commService", CommService.class);
			T t = (T) commService.autoActoin(new IDataSourceCallBack()
		       {
		         public T doAction()
		           throws AppException
		         {
		           T t = DataSourceCallBack.this.doAction();
		           return t;
		         }
		       });
			return t;
		} finally {
			AppContextHolder.clearCustomerType();
		}
	}
}