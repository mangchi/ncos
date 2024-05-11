<%@page language="java" contentType="text/html; charset=UTF-8"%>
<%-- <%@page import="org.apache.http.client.methods.CloseableHttpResponse"%>
<%@page import="org.apache.http.impl.client.CloseableHttpClient"%>
<%@page import="org.apache.http.client.methods.HttpGet"%>
<%@page import="org.apache.http.client.config.RequestConfig"%>
<%@page import="org.apache.http.impl.client.HttpClientBuilder"%>
<%@page import="org.apache.http.impl.client.HttpClients"%>
<%@page import="org.apache.http.conn.ConnectTimeoutException"%> --%>
<%@page import="java.io.BufferedReader"%>
<%@page import="org.json.simple.JSONObject"%>
<%@page import="org.json.simple.parser.JSONParser"%>
<%@page import="java.util.Map"%>
<%@page import="java.io.InputStreamReader"%>
<%-- <%@page import="org.apache.http.conn.HttpHostConnectException"%> --%>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>
<%@page import="java.io.IOException" %>
<%@page import="java.text.ParseException" %>

<%@page import="java.net.URI" %>
<%@page import="java.security.cert.CertificateException" %>
<%@page import="java.security.cert.X509Certificate" %>

<%@page import="javax.net.ssl.SSLContext" %>

<%@page import="org.apache.http.HttpResponse" %>
<%@page import="org.apache.http.client.HttpClient" %>
<%@page import="org.apache.http.client.methods.HttpGet" %>
<%@page import="org.apache.http.conn.ssl.NoopHostnameVerifier" %>
<%@page import="org.apache.http.entity.StringEntity" %>
<%@page import="org.apache.http.impl.client.HttpClientBuilder" %>
<%@page import="org.apache.http.impl.client.HttpClients" %>
<%@page import="org.apache.http.protocol.HTTP" %>
<%@page import="org.apache.http.ssl.SSLContexts" %>
<%@page import="org.apache.http.ssl.TrustStrategy" %>
<%@page import="org.apache.http.util.EntityUtils" %>

<%@include file="agentInfo.jsp"%>
<%
 
    /**
     * Business - 최초로 호출되는 페이지
     *    인증서버 통신체크 한 후 이상이 없을 경우 인증서버의 로그인 페이지(SSO_LOGIN_PAGE)로 리다이렉션 처리
     */
    System.out.println("Session ID : " + session.getId());

     // 인증서버 통신 체크
    HttpClient httpclient = null;
    BufferedReader rd = null;
    try{
	    HttpClientBuilder httpClientBuilder = HttpClients.custom();
		SSLContext sslcontext = SSLContexts.custom().useProtocol("SSL")
		    .loadTrustMaterial(null, new TrustStrategy() {
		        @Override
		        public boolean isTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString) throws CertificateException {
		            return true;
		        }
		    }).build();
	
		httpClientBuilder.setSSLHostnameVerifier(new NoopHostnameVerifier()).setSSLContext(sslcontext);
	
		httpclient = httpClientBuilder.build();
		
		URI uri = new URI(CHECK_SERVER_URL);
		HttpGet httpGet = new HttpGet(uri);
		httpGet.setURI(uri);	
		
		HttpResponse postResponse = httpclient.execute(httpGet);
		//String responseString = EntityUtils.toString(httpRes.getEntity(), HTTP.UTF_8);
		rd = new BufferedReader(new InputStreamReader(postResponse.getEntity().getContent(), "UTF-8"));
		String httpResponse = "";
		httpResponse = rd.readLine();
		JSONParser parser = new JSONParser();
		Object object = parser.parse(httpResponse);
		JSONObject jsonObject = (JSONObject)object;

		String resultCode = (String)jsonObject.get("resultCode");
		
		if (resultCode == null || resultCode.equals("000000") == false) {
			throw new Exception("resultCode : " + resultCode);
		}
		rd.close();
		///System.out.println(responseString);
    }catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch(Exception e) {
		e.printStackTrace();
	} finally {
		if(httpclient != null)httpclient.getConnectionManager().shutdown(); 
		if(rd != null)rd.close(); 
	}
	
	
	/*
			
    CloseableHttpClient httpClient =  getHttpClient(connectionTimeout, connectionTimeout);
    HttpGet httpGet = new HttpGet(CHECK_SERVER_URL);
    if(activeProfile.equals("land")){
    	httpGet = new HttpGet(LAND_CHECK_SERVER_URL);
    }
   
    CloseableHttpResponse postResponse = null;

    try {
    	 postResponse = httpClient.execute(httpGet);
    	  
    } catch(ConnectTimeoutException timeOutEx) {
        // SSO 인증서버와 통신이 되지 않을 경우 네트워크 에러 페이지로 리다이렉션
    	System.out.println("[Connection Time Out Exception] : " + timeOutEx.toString());
        response.sendRedirect(NETWORK_ERROR_PAGE);
        return;
    } catch (HttpHostConnectException httpException) {
        // SSO 인증서버와 통신이 되지 않을 경우 네트워크 에러 페이지로 리다이렉션
    	System.out.println("[Business Exception Of Network ] : " + httpException.toString());
        response.sendRedirect(NETWORK_ERROR_PAGE);
        return;
    }
    
	BufferedReader rd = new BufferedReader(new InputStreamReader(postResponse.getEntity().getContent(), "UTF-8"));
       
    try {

		String httpResponse = "";
		httpResponse = rd.readLine();

		// debug print
		 System.out.println("[httpResponse] : " + httpResponse);

		JSONParser parser = new JSONParser();
		Object object = parser.parse(httpResponse);
		JSONObject jsonObject = (JSONObject)object;

		String resultCode = (String)jsonObject.get("resultCode");
		
		if (resultCode == null || resultCode.equals("000000") == false) {
			throw new Exception("resultCode : " + resultCode);
		}
    } catch (Exception e) {
		// TODO - 인증서버와 통신 도중 예외 상황 발생 시 개별 업무로 로그인 할 수 있도록 처리 해야 합니다.
		System.out.println("[Business Exception] : " + e.toString());
		response.sendRedirect(EXCEPTION_PAGE);
		return;
    } finally {
        rd.close();
    }
    */
%>
<%!
/*
private CloseableHttpClient getHttpClient(int connectTimeout, int reqTimeout) {
	
	RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(connectTimeout).setConnectionRequestTimeout(reqTimeout).build();
	
	HttpClientBuilder httpClientBuilder = HttpClients.custom().setDefaultRequestConfig(requestConfig);
	
	return httpClientBuilder.build();
}
*/
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
</head>

<body>
    <form name="sendForm" method="post">
        <input type="hidden" name="agentId" value="<%=agentId%>" />
    </form>

    <script>
        var sendUrl = "<%=AUTH_LOGIN_PAGE%>";  
        var sendForm = document.sendForm;
        sendForm.action = sendUrl;
        sendForm.submit();
    </script>
    
</body>
</html>
