<%@ page language="java" contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.*" %>
<%!
	// SW접근관리(아우토) 서버 도메인, act.ncos.mil.kr, act = access control
    // 가시화 (링크나인) 서버 도메인, vst.ncos.mil.kr, vst = visualization
	String touchURI = "https://act.ncos.mil.kr/session";

	public static String getTouchText(HttpSession session, String touchURI) {

        long curTime = System.currentTimeMillis();
        String lastTouchedStr = (String)session.getAttribute("NCOS::last-touched");
/*
        if(lastTouchedStr != null) {
            long lastTouched = Long.parseLong(lastTouchedStr);

            // 예제는 10분 설정, 마지막 터치 후 10분이 지나야 터치 가능
            if(curTime - lastTouched < 10*60*1000L) return "";
        }
		*/
        session.setAttribute("NCOS::last-touched", curTime + "");
        String t = "<script type='text/javascript' src='" + touchURI + "'></script>\n";

        return t;
    }
%>
<center> [ <%= request.getServerName()+":"+request.getServerPort() %> ] </center>
<hr>
JSESSIONID : <%= session.getId() %>
<br>
<br>

User-Agent : <b><%= request.getHeader("User-Agent") %></b>
<hr>

<%-- NCOS 터치 스크립트 --%>
<%= getTouchText(session, touchURI) %>

