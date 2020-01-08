<%@page contentType="text/html;charset=UTF-8"%>
<% request.setCharacterEncoding("UTF-8"); %>
<HTML>
<HEAD>
<TITLE>Result</TITLE>
</HEAD>
<BODY>
<H1>Result</H1>

<jsp:useBean id="sampleCalculateServiceProxyid" scope="session" class="edu.sjtu.webservice.CalculateServiceProxy" />
<%
if (request.getParameter("endpoint") != null && request.getParameter("endpoint").length() > 0)
sampleCalculateServiceProxyid.setEndpoint(request.getParameter("endpoint"));
%>

<%
String method = request.getParameter("method");
int methodID = 0;
if (method == null) methodID = -1;

if(methodID != -1) methodID = Integer.parseInt(method);
boolean gotMethod = false;

try {
switch (methodID){ 
case 2:
        gotMethod = true;
        java.lang.String getEndpoint2mtemp = sampleCalculateServiceProxyid.getEndpoint();
if(getEndpoint2mtemp == null){
%>
<%=getEndpoint2mtemp %>
<%
}else{
        String tempResultreturnp3 = org.eclipse.jst.ws.util.JspUtils.markup(String.valueOf(getEndpoint2mtemp));
        %>
        <%= tempResultreturnp3 %>
        <%
}
break;
case 5:
        gotMethod = true;
        String endpoint_0id=  request.getParameter("endpoint8");
            java.lang.String endpoint_0idTemp = null;
        if(!endpoint_0id.equals("")){
         endpoint_0idTemp  = endpoint_0id;
        }
        sampleCalculateServiceProxyid.setEndpoint(endpoint_0idTemp);
break;
case 10:
        gotMethod = true;
        edu.sjtu.webservice.CalculateService getCalculateService10mtemp = sampleCalculateServiceProxyid.getCalculateService();
if(getCalculateService10mtemp == null){
%>
<%=getCalculateService10mtemp %>
<%
}else{
        if(getCalculateService10mtemp!= null){
        String tempreturnp11 = getCalculateService10mtemp.toString();
        %>
        <%=tempreturnp11%>
        <%
        }}
break;
case 13:
        gotMethod = true;
        String x_1id=  request.getParameter("x16");
        float x_1idTemp  = Float.parseFloat(x_1id);
        String y_2id=  request.getParameter("y18");
        float y_2idTemp  = Float.parseFloat(y_2id);
        float divide13mtemp = sampleCalculateServiceProxyid.divide(x_1idTemp,y_2idTemp);
        String tempResultreturnp14 = org.eclipse.jst.ws.util.JspUtils.markup(String.valueOf(divide13mtemp));
        %>
        <%= tempResultreturnp14 %>
        <%
break;
case 20:
        gotMethod = true;
        String x_3id=  request.getParameter("x23");
        float x_3idTemp  = Float.parseFloat(x_3id);
        String y_4id=  request.getParameter("y25");
        float y_4idTemp  = Float.parseFloat(y_4id);
        float multiply20mtemp = sampleCalculateServiceProxyid.multiply(x_3idTemp,y_4idTemp);
        String tempResultreturnp21 = org.eclipse.jst.ws.util.JspUtils.markup(String.valueOf(multiply20mtemp));
        %>
        <%= tempResultreturnp21 %>
        <%
break;
case 27:
        gotMethod = true;
        String x_5id=  request.getParameter("x30");
        float x_5idTemp  = Float.parseFloat(x_5id);
        String y_6id=  request.getParameter("y32");
        float y_6idTemp  = Float.parseFloat(y_6id);
        float minus27mtemp = sampleCalculateServiceProxyid.minus(x_5idTemp,y_6idTemp);
        String tempResultreturnp28 = org.eclipse.jst.ws.util.JspUtils.markup(String.valueOf(minus27mtemp));
        %>
        <%= tempResultreturnp28 %>
        <%
break;
case 34:
        gotMethod = true;
        String x_7id=  request.getParameter("x37");
        float x_7idTemp  = Float.parseFloat(x_7id);
        String y_8id=  request.getParameter("y39");
        float y_8idTemp  = Float.parseFloat(y_8id);
        float plus34mtemp = sampleCalculateServiceProxyid.plus(x_7idTemp,y_8idTemp);
        String tempResultreturnp35 = org.eclipse.jst.ws.util.JspUtils.markup(String.valueOf(plus34mtemp));
        %>
        <%= tempResultreturnp35 %>
        <%
break;
}
} catch (Exception e) { 
%>
Exception: <%= org.eclipse.jst.ws.util.JspUtils.markup(e.toString()) %>
Message: <%= org.eclipse.jst.ws.util.JspUtils.markup(e.getMessage()) %>
<%
return;
}
if(!gotMethod){
%>
result: N/A
<%
}
%>
</BODY>
</HTML>