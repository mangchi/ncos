<%@page import="java.io.File"%><%
    //clipreport5.properties 서버환경에 따라 파일 위치를 지정합니다.
    String propertyPath  = request.getSession().getServletContext().getRealPath("/") + File.separator +  "WEB-INF" + File.separator + "clipreport5" + File.separator + "clipreport5.properties";

%>