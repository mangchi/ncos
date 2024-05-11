<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="org.apache.http.NameValuePair"%>
<%@ page import="org.apache.http.client.ClientProtocolException"%>
<%@page import="org.apache.http.client.methods.CloseableHttpResponse"%>
<%@page import="org.apache.http.impl.client.CloseableHttpClient"%>
<%@page import="org.apache.http.client.methods.HttpGet"%>
<%@page import="org.apache.http.client.config.RequestConfig"%>
<%@page import="org.apache.http.impl.client.HttpClientBuilder"%>
<%@page import="org.apache.http.impl.client.HttpClients"%>
<%@page import="org.apache.http.conn.ConnectTimeoutException"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="org.json.simple.JSONObject"%>
<%@page import="org.json.simple.parser.JSONParser"%>
<%@page import="org.apache.http.message.BasicNameValuePair"%>
<%@page import="java.util.ArrayList"%>
<%@page import="org.apache.http.client.entity.UrlEncodedFormEntity"%>
<%@page import="java.io.InputStreamReader"%>
<%@page import="org.apache.http.client.methods.HttpPost"%>
<%@page import="java.util.List"%>
<%@page import="org.slf4j.Logger" %>
<%@page import="org.slf4j.LoggerFactory" %>
<%@page import="java.io.IOException" %>
<%@page import="org.apache.http.client.HttpClient" %>
<%@page import="org.apache.http.ssl.SSLContexts" %>
<%@page import="javax.net.ssl.SSLContext" %>
<%@page import="org.apache.http.ssl.TrustStrategy" %>
<%@page import="java.security.cert.CertificateException" %>
<%@page import="java.security.cert.X509Certificate" %>
<%@page import="org.apache.http.conn.ssl.NoopHostnameVerifier" %>
<%@page import="org.apache.http.HttpResponse" %>
<%@page import="java.net.URI" %>
<%@ include file="agentInfo.jsp"%>

<%

    /**
     * checkauth - SSO로부터 호출 되는 페이지
     *      토큰 검증 및 업데이트 진행
     */
     //logger.debug("[[[ checkauth page ]]]");

     String resultCode = request.getParameter("resultCode") == null ? "" : request.getParameter("resultCode");
     String secureToken = request.getParameter("secureToken") == null ? "" : request.getParameter("secureToken");
     String secureSessionId = request.getParameter("secureSessionId") == null ? "" : request.getParameter("secureSessionId");
     String clientIp = request.getRemoteAddr();

     String resultMessage = "";
     String returnUrl = "";
     
     HttpClient httpClient = null;
     // debug print
     /*
     logger.debug("[resultCode] : " + resultCode);
     logger.debug("[secureToken]: " + secureToken);
     logger.debug("[secureSessionId] : " + secureSessionId);
     logger.debug("[clientIp] : " + clientIp);
     */
     BufferedReader rd = null;
     if (resultCode.equals("000000") && "".equals(secureToken) == false && "".equals(resultCode) == false) {
         
     	 HttpPost httpPost = null;
         httpClient = HttpClients.createDefault();
         try {
     	    HttpClientBuilder httpClientBuilder = HttpClients.custom();
    		SSLContext sslcontext = SSLContexts.custom().useProtocol("SSL")
    		    .loadTrustMaterial(null, new TrustStrategy() {
    		        @Override
    		        public boolean isTrusted(X509Certificate[] paramArrayOfX509Certificate, String paramString) throws CertificateException {
    		            return true;
    		        }
    		    }).build();
    	
    		httpClientBuilder.setSSLHostnameVerifier(new NoopHostnameVerifier()).setSSLContext(sslcontext);        	 
    		httpClient = httpClientBuilder.build();
             // 인증서버에 토큰 검증 및 사용자 정보를 요청하기 위해 httpclient를 사용하여 전달
          URI uri = new URI(TOKEN_AUTHORIZATION_URL);
  		  httpPost = new HttpPost();
  		  httpPost.setURI(uri);	
  		  
  	      List<NameValuePair> params = new ArrayList<NameValuePair>();
 	      params.add(new BasicNameValuePair("secureToken", secureToken));
 	      params.add(new BasicNameValuePair("secureSessionId", secureSessionId));
 	      params.add(new BasicNameValuePair("requestData", requestData));
 	      params.add(new BasicNameValuePair("agentId", agentId));
 	      params.add(new BasicNameValuePair("clientIP", clientIp));
 	      httpPost.setEntity(new UrlEncodedFormEntity(params));
 	      
 	     HttpResponse postResponse = httpClient.execute(httpPost);

 	      String httpResponse = "";
 	      try {
 	          rd = new BufferedReader(new InputStreamReader(postResponse.getEntity().getContent(), "UTF-8"));  
 	          httpResponse = rd.readLine();
 	      }catch (IllegalArgumentException e  ){
 	          System.out.println("failed to Response buffer reader");
 	      }catch (IOException e ){
 	    	 System.out.println("failed to Response buffer reader");
 	      } finally {  
 	    	  //postResponse.close();
 	    	  if(rd != null){
 	    	 	rd.close();
 	    	  }
 	      }

 	      System.out.println("[httpResponse] : " + httpResponse);
          JSONParser jsonParser = new JSONParser();
          JSONObject jsonObject = (JSONObject) jsonParser.parse(httpResponse);

          // 사용자 요청 정보
          JSONObject dataObject = (JSONObject) jsonObject.get("user");
          // 결과 코드와 메시지
          resultCode = (String) jsonObject.get("resultCode");
          resultMessage = (String) jsonObject.get("resultMessage");
          // Return URL(인증서버에서 리다이렉션될 주소를 전달)
          returnUrl = (String) jsonObject.get("returnUrl");
          // check cs mode(토큰저장소에 토큰을 저장하기 위해 사용되며 CS모드일 경우는 SAVE_TOKEN_URL로 리다이렉션 됨)
          boolean useCSMode  = jsonObject.get("useCSMode") == null ? false:Boolean.valueOf(jsonObject.get("useCSMode").toString());
          boolean useNotesMode = jsonObject.get("useNotesMode") == null 
                  ? false: Boolean.valueOf(jsonObject.get("useNotesMode").toString());

           HttpSession httpsession = request.getSession();
           // 요청 데이터 정보 추출
           if ("000000".equals(resultCode)) {
               // 검증 성공
               String[] keys = requestData.split(",");
               System.out.println("####################");
               for (int i = 0; i < keys.length; i++) {
                   String value = (String) dataObject.get(keys[i]);
                   if (value == null) {
                       continue;
                   }
                   System.out.println("KEY ["+keys[i]+"] value= "+value);
                   httpsession.setAttribute(keys[i], value);
               }
               System.out.println("####################");
               // 무조건 saveToken.html 호출
               returnUrl = SAVE_TOKEN_URL;

           } else if ("310017".equals(resultCode) || "310012".equals(resultCode)) {
               // 서비스 접근 권한 실패(다른 서비스에 영향을 주어서는 안됨으로 로그아웃은 하지 않음)
               returnUrl = SERVICE_ERR_PAGE;
           } else {
               // SSO 검증 실패(로그아웃 필요)
               returnUrl = ERROR_PAGE;
           }

           // 결과 코드와 메시지, 사용자 요청 데이터를 세션에 저장
           httpsession.setAttribute("resultCode", resultCode);
           httpsession.setAttribute("resultMessage", resultMessage);
       } catch(ConnectTimeoutException timeOutEx) {
    	   System.out.println("[Connection Time Out Exception] : " + timeOutEx.toString());  
    	   // TODO - 인증서버와 통신 실패 시 개별 업무로 로그인 할 수 있도록 처리 해야 합니다.
           returnUrl = EXCEPTION_PAGE;
       } catch (ClientProtocolException clientEx) {
    	   System.out.println("[checkauth HttpException] : " + clientEx.toString());
           // TODO - 인증서버와 통신 실패 시 개별 업무로 로그인 할 수 있도록 처리 해야 합니다.
           returnUrl = EXCEPTION_PAGE;
       } catch (Exception e) {
    	   System.out.println("[checkauth Exception] : " + e.toString());
           returnUrl = ERROR_PAGE;
       } finally {
       		httpPost.releaseConnection();
       }
     } else {
         // 비정상 호출 할 경우 Business 페이지로 리다이렉션 처리
         System.out.println("unknown call");
         returnUrl = LOGOUT_PAGE;
     }
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
    <input type="hidden" name="resultCode" value="<%=resultCode%>" />
    <input type="hidden" name="secureSessionId" value="<%=secureSessionId%>" />
</form>

<script>
    var sendUrl = "<%=returnUrl%>";
    var sendForm = document.sendForm;
    sendForm.action = sendUrl;
    sendForm.submit();
</script>
</body>
</html>