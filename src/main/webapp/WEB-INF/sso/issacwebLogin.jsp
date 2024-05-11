<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@page import="org.apache.http.client.methods.CloseableHttpResponse"%>
<%@page import="org.apache.http.impl.client.CloseableHttpClient"%>
<%@page import="org.apache.http.client.methods.HttpGet"%>
<%@page import="org.apache.http.client.config.RequestConfig"%>
<%@page import="org.apache.http.impl.client.HttpClientBuilder"%>
<%@page import="org.apache.http.impl.client.HttpClients"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="org.json.simple.JSONObject"%>
<%@page import="org.json.simple.parser.JSONParser"%>
<%@page import="java.util.Map"%>
<%@page import="java.io.InputStreamReader"%>
<%@ page import="org.slf4j.Logger" %>
<%@ page import="org.slf4j.LoggerFactory" %>
<%@page import="java.io.IOException" %>
<%@page import="java.text.ParseException" %>

<%@include file="agentInfo.jsp"%>
<%
    Logger logger = LoggerFactory.getLogger("issacwebLogin");

    String clientIp = request.getRemoteAddr();
    String userAgent = request.getHeader("User-Agent");

    logger.debug("[clientIp] : " + clientIp);
    logger.debug("[userAgent] : " + userAgent);

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <!-- js 로딩 순서 바꾸면 안됨 -->
    <script type="text/javascript" src="webcrypto/js/forge/forge.js?v=<%=ISIGN_PLUS_WA_VER%>" charset="UTF-8"></script>
    <script type="text/javascript" src="webcrypto/js/forge/jsbn.js?v=<%=ISIGN_PLUS_WA_VER%>" charset="UTF-8"></script>
    <script type="text/javascript" src="webcrypto/js/forge/util.js?v=<%=ISIGN_PLUS_WA_VER%>" charset="UTF-8"></script>
    <script type="text/javascript" src="webcrypto/js/forge/sha1.js?v=<%=ISIGN_PLUS_WA_VER%>" charset="UTF-8"></script>
    <script type="text/javascript" src="webcrypto/js/forge/sha256.js?v=<%=ISIGN_PLUS_WA_VER%>" charset="UTF-8"></script>
    <script type="text/javascript" src="webcrypto/js/forge/sha512.js?v=<%=ISIGN_PLUS_WA_VER%>" charset="UTF-8"></script>
    <script type="text/javascript" src="webcrypto/js/forge/asn1.js?v=<%=ISIGN_PLUS_WA_VER%>" charset="UTF-8"></script>
    <script type="text/javascript" src="webcrypto/js/forge/cipher.js?v=<%=ISIGN_PLUS_WA_VER%>" charset="UTF-8"></script>
    <script type="text/javascript" src="webcrypto/js/forge/cipherModes.js?v=<%=ISIGN_PLUS_WA_VER%>" charset="UTF-8"></script>
    <script type="text/javascript" src="webcrypto/js/forge/seed.js?v=<%=ISIGN_PLUS_WA_VER%>" charset="UTF-8"></script>
    <script type="text/javascript" src="webcrypto/js/forge/aes.js?v=<%=ISIGN_PLUS_WA_VER%>" charset="UTF-8"></script>
    <script type="text/javascript" src="webcrypto/js/forge/prng.js?v=<%=ISIGN_PLUS_WA_VER%>" charset="UTF-8"></script>
    <script type="text/javascript" src="webcrypto/js/forge/random.js?v=<%=ISIGN_PLUS_WA_VER%>" charset="UTF-8"></script>
    <script type="text/javascript" src="webcrypto/js/forge/rsa.js?v=<%=ISIGN_PLUS_WA_VER%>" charset="UTF-8"></script>
    <script type="text/javascript" src="webcrypto/js/forge/pkcs1.js?v=<%=ISIGN_PLUS_WA_VER%>" charset="UTF-8"></script>

    <script type="text/javascript" src="webcrypto/js/webcrypto/common/webcrypto.js?v=<%=ISIGN_PLUS_WA_VER%>" charset="UTF-8"></script>
    <script type="text/javascript" src="webcrypto/js/webcrypto/common/webcrypto_msg.js?v=<%=ISIGN_PLUS_WA_VER%>" charset="UTF-8"></script>

    <script type="text/javascript" src="webcrypto/js/webcrypto/e2e/webcrypto_e2e.js?v=<%=ISIGN_PLUS_WA_VER%>" charset="UTF-8"></script>
    
    <script type="text/javascript" src="jquery-3.1.1.min.js"></script>
    <script type="text/javascript">
    $(function() {
        	
            var keyname1 = 'Sample1';
            function issacweb_escape(msg) {
                var i;
                var ch;
                var encMsg = '';
                var tmp_msg = String(msg);

                for (i = 0; i < tmp_msg.length; i++) {
                    ch = tmp_msg.charAt(i);

                    if (ch == ' ')
                        encMsg += '%20';
                    else if (ch == '%')
                        encMsg += '%25';
                    else if (ch == '&')
                        encMsg += '%26';
                    else if (ch == '+')
                        encMsg += '%2B';
                    else if (ch == '=')
                        encMsg += '%3D';
                    else if (ch == '?')
                        encMsg += '%3F';
                    else if (ch == '|')
                        encMsg += '%7C';
                    else
                        encMsg += ch;
                }
                return encMsg;
            }
            
            var props = {
                constants : {},
                elements:{
                    $formlogin : $('#form-login'),
                    $btnLogin : $('#btn-login')
                }
            };
            
            $("#btn-login").click(function(){
                var id = $('#id').val();
                var pw = $('#pw').val();

                if (id == undefined || id.length == 0) {
                    alert("id를 입력하세요.");
                    return;
                }
                if (pw == undefined || pw.length == 0) {
                    alert("비밀번호를 입력하세요.");
                    return;
                }
                
                var message = issacweb_escape('id') + "=" + issacweb_escape(id);
                message += "&" + issacweb_escape('pw') + "=" + issacweb_escape(pw);

                // public key 얻기
                $.ajax({
                    url : '<%=GET_PUBLICKEY_URL%>',
                    type : 'GET',
                    dataType : 'json',
                    success : function(data) {
                    	console.log(data);
                        var resultCode = data.resultCode
                        var resultMessage = data.resultMessage
                        
                        if (resultCode == "000000") {
                        	
                        	var publicKey = data.resultData.publicKey;
                        	var timeStamp = data.resultData.timeStamp;

                            //공개키 획득 성공 시 추가적으로 타임스탬프 값을
                            //issacwebdata 에 구성한다.
                        	message += "&" + issacweb_escape('timeStamp') + "=" + issacweb_escape(timeStamp);
                        	
                            try{
                            	var reqHybridEnc = webcrypto.e2e.hybridEncrypt(keyname1, message, 
                                        'UTF-8', 'SEED', publicKey, 'RSAES-OAEP', 'RSA-SHA1');
                                
                                reqHybridEnc.onerror = function(errMsg) { alert(errMsg); };
                                reqHybridEnc.oncomplete = function(result) {
                                    
                                    if(result === "") {
                                    	alertErrorMessage("issacweb_data is null");
                                        return;
                                    }
                                    
                                    var agentId = $('#agentId').val();
                                    var userId = $('#userId').val();
                                    var issacwebData = result;
                                    
                                    var action = "<%=ISSACWEB_LOGIN_PROCESS%>";
                                    var form = $('<form action=' + action + ' method="post">'
                                        + '<input type="hidden" name="agentId" value="' + agentId + '" />'
                                        + '<input type="hidden" name="userId" value="' + userId + '" />'
                                        + '<input type="hidden" name="issacwebData" value="' + issacwebData + '" />'
                                        + '</form>');
                                    $('body').append(form);
                                    form.submit();
                                };
                            }catch(e){
                                if (e.message) {
                                    alert(e.message);
                                } else {
                                    alert(e);
                                }
                            }
                            
                        } else {
                        	alertErrorMessage(resultMessage);
                            return;
                        }
                    }
                });
            });
            
            $("#pw").keydown(function(key) {
                if(key.keyCode == 13) {
                    $("#btn-login").click();
                }
                
            });
        });
    </script>
    <meta charset="UTF-8">
    <title>login page</title>
<body>

<h1>issacweb login page</h1>
<form id="form-login">
    <input type="hidden" id="agentId" name="agentId" value="<%=agentId%>">
    <input type="hidden" id="issacwebData" name="issacwebData">
    <input type="text" id="id"name="id" placeholder="loginId">
    <input type="password" id="pw" name="pw" placeholder="loginPw">
    <button type="button" id="btn-login">로그인</button>
</form>
</body>
</html>
