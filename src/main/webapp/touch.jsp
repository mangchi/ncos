<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<%
    boolean touchCallAlert = false;

    if(touchCallAlert) {   	
    	
    	response.setHeader("Cache-Control", "no-cache");  

        String msg = "alert('ncos_touch.. ok');\r\n";

        java.io.PrintWriter _out = response.getWriter();
        _out.print(msg);       
        _out.flush();
    }
%>