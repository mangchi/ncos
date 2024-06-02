<%@page import="com.clipsoft.clipreport.common.logging.log.ServiceLogger"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*, javax.naming.*, javax.sql.*" %>
<%
	Connection conn = null;
	Statement stmt = null;
	String jndiName = "";
	try {
		InitialContext ctx = new InitialContext();
		DataSource ds = (DataSource) ctx.lookup(jndiName);
		conn = ds.getConnection();
		out.println("<h5>JNDI Connection Success</h5><br><br>");
		out.println("<h5>- Product Name : " + conn.getMetaData().getDatabaseProductName() + "</h5>");
		out.println("<h5>- Product Version : " + conn.getMetaData().getDatabaseProductVersion() + "</h5>");
		out.println("<h5>- Driver Name : " + conn.getMetaData().getDriverName()+"</h5>");
		out.println("<h5>- Driver Version : " + conn.getMetaData().getDriverVersion()+"</h5>");
	} catch (NamingException e) {
		response.getWriter().append("<h5>Exception Message<h5><br>");
		ServiceLogger.getInstance().error(e);
	}
%>