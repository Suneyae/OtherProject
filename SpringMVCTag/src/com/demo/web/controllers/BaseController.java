package com.demo.web.controllers;

import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ExceptionHandler;
/**
 * 
 * @author Wei
 * @time  2017年4月20日 下午3:56:51
 */
public abstract class BaseController {  

	@ExceptionHandler
    public String exception(HttpServletRequest request, Exception e) {  
          
        request.setAttribute("exceptionMessage", e.getMessage());  
          
        // 根据不同的异常类型进行不同处理
        if(e instanceof SQLException) 
            return "testerror";   
        else
            return "error";  
    }  
	
}  
