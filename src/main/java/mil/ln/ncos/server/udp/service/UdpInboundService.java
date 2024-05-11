package mil.ln.ncos.server.udp.service;

import java.io.BufferedWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mil.ln.ncos.NcosApplication;
import mil.ln.ncos.cmmn.util.ConvertUtil;
import mil.ln.ncos.cmmn.util.ScpDbUtil;
import mil.ln.ncos.dao.DAO;
import mil.ln.ncos.func.service.FuncService;



@Slf4j
@RequiredArgsConstructor
@Service
public class UdpInboundService {

	private final DAO dao;

	@Value("${cryptoMode}")
	private String cryptoMode;

	@Value("${spring.profiles.active}")
	private String activeProfile;
	
	@Value("${crypto.key1}")
	private String cryptoModeKey1;
	
	private final FuncService funcService;

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
	public void receive(Message msg) throws ParseException {

        BufferedWriter writer = null;
        String receiveType = "1";
        try {
        	String message = new String((byte[]) msg.getPayload());
        	byte[] byteArray = message.getBytes(StandardCharsets.UTF_8);
        	message = new String(byteArray,StandardCharsets.UTF_8);

        	JSONObject jsonObj = ConvertUtil.convertToJson((byte[]) msg.getPayload());
            
        	int nMsgid = ConvertUtil.UnsignedByteParse(Arrays.copyOfRange(Arrays.copyOfRange((byte[]) msg.getPayload(), 0, 1), 0, 1), 0);

            log.debug("UdpInboundService data:{}",message);
	        Map<String,Object> param = new HashMap();
	        if( nMsgid == 1 ) {
	        	log.debug("heartbeat message start");
	        	param.put("unitId", ConvertUtil.UnsignedByteParse(Arrays.copyOfRange((byte[]) msg.getPayload(), 1, 2), 0));
	        	param.put("shipId", ConvertUtil.UnsignedShortParse(Arrays.copyOfRange((byte[]) msg.getPayload(), 2, 4), 0));
	        	param.put("asset_status", jsonObj.get("asset_status"));

	        	// 수신데이터 처리 필요
	        	//saveAssertStatusInfo(param);
	        	NcosApplication.transStatus.put("heartbeatStatus", "Y");
	        	log.debug("heartbeat message end");
	        }
	        //else if(messageContent.startsWith("2")) {
        	else if( nMsgid == 2 ) {
        		receiveType = "2";
	        	log.debug("threatInfo message start.............");
	        	
	        	//log.debug("unItId===================="+ConvertUtil.UnsignedShortParse(Arrays.copyOfRange((byte[]) msg.getPayload(), 1, 2), 0));

	        	param.put("unitId", ConvertUtil.UnsignedByteParse(Arrays.copyOfRange((byte[]) msg.getPayload(), 1, 2), 0));
	        	param.put("shipId", ConvertUtil.UnsignedShortParse(Arrays.copyOfRange((byte[]) msg.getPayload(), 2, 4), 0));
	        	param.put("detectionTime", jsonObj.get("detection_time"));
	
	        	param.put("srcIp", jsonObj.get("src_ip"));
		        param.put("dstIp", jsonObj.get("dst_ip"));
	        	param.put("srcPort", jsonObj.get("src_port"));
	        	param.put("dstPort", jsonObj.get("dst_port"));
	        	param.put("protocol", jsonObj.get("protocol"));
	        	param.put("payloadSize", jsonObj.get("payload_size"));
	        	//log.debug("####################################### payload_size: ",jsonObj.get("payload_size"));
	        	param.put("fragmentation", jsonObj.get("fragmentation"));
	        	//log.debug("####################################### fragmentation: ",jsonObj.get("fragmentation"));
	        	param.put("fragmentId", jsonObj.get("fragment_id"));
	        	log.debug("####################################### fragment_id: ",jsonObj.get("fragment_id"));
	        	param.put("detectionThreatName", jsonObj.get("detection_threat_name"));
	        	param.put("threatDetectionMethod", jsonObj.get("threat_detection_method"));
	        	param.put("threatImportance", jsonObj.get("threat_importance"));
	        	param.put("transSpeed", jsonObj.get("trans_speed"));
	        	param.put("payload", "");
	        	param.put("srcBytes", ((byte[]) msg.getPayload()).length);
	        	

	        	if(cryptoMode.equals("Y")) {

	        		Map<String, Object> salt = dao.selectMapByJob("Cmmn.selectIntegritySalt", null).orElseGet(() -> new HashMap<String, Object>());
	                //log.debug("salt:{}",salt);
	    			final String saltKey="threatInfo";
	    			String integrityKey = (param.get("unitId") == null?"":param.get("unitId").toString())
	    						+(param.get("shipId")== null?"":param.get("shipId").toString())
	    						+(param.get("detectionTime")== null?"":param.get("detectionTime").toString().substring(0,10))
	    						+(param.get("srcIp")== null?"":param.get("srcIp").toString())
	    						+(param.get("dstIp")== null?"":param.get("dstIp").toString())
	    						+(param.get("srcPort")== null?"":param.get("srcPort").toString())
	    						+(param.get("dstPort")== null?"":param.get("dstPort").toString())
	    						+(param.get("protocol")== null?"":param.get("protocol").toString())
	    						+(param.get("payloadSize")== null?"":param.get("payloadSize").toString())
	    						+(param.get("fragmentation")== null?"":param.get("fragmentation").toString())
	    						//+(param.get("fragmentId")== null?"":param.get("fragmentId").toString());
	    			           
	    						+(param.get("detectionThreatName")== null?"":param.get("detectionThreatName").toString())
	    						+(param.get("threatDetectionMethod")== null?"":param.get("threatDetectionMethod").toString())
	    						+(param.get("threatImportance")== null?"":param.get("threatImportance").toString());
	    				
                    
	    						//+ScpDbUtil.scpDec(String.valueOf(param.get("payload")),cryptoModeKey1);
	    					
	                //log.debug("before integrityKey:{}",integrityKey);
	    			//String saltEnc = (String)salt.get(saltKey);
	    			//log.debug("before saltEnc:{}",saltEnc );
	    			String saltDec = ScpDbUtil.scpDec((String)salt.get(saltKey),cryptoModeKey1);
	    			//log.debug("after saltDec:{}",saltDec );
	    			integrityKey = integrityKey + saltDec;
	    			//log.debug("saveThreatInfo integrityKey:{}",integrityKey);
	    			String integrity = ScpDbUtil.AgentCipherHashStringB64(integrityKey);
	    			param.put("integrity", integrity);
	    			param.put("srcIp", ScpDbUtil.scpEnc(""+param.get("srcIp"), cryptoModeKey1));
					param.put("dstIp", ScpDbUtil.scpEnc(""+param.get("dstIp"), cryptoModeKey1));
					//param.put("fragmentId", ScpDbUtil.scpEnc(""+param.get("fragmentId"), cryptoModeKey1));
	        	}

	        	saveThreatInfo(param);
	        	saveTransmitData(param);
	        	NcosApplication.transStatus.put("threatStatus", "Y");
	        	log.debug("threatInfo message end");

	        }
        	else if( nMsgid == 3 ) {
        		receiveType = "3";
	        	log.debug("assetInfo message start");
	        	log.debug("jsonObj:{}",jsonObj);
	        	param.put("unitId", ConvertUtil.UnsignedByteParse(Arrays.copyOfRange((byte[]) msg.getPayload(), 1, 2), 0));
	        	param.put("shipId", ConvertUtil.UnsignedShortParse(Arrays.copyOfRange((byte[]) msg.getPayload(), 2, 4), 0));

	        	if( "2".equals(String.valueOf(jsonObj.get("equip_status"))) && "2".equals(String.valueOf(jsonObj.get("function_status"))) )
    			{
	        		param.put("shipStatusId", 2);
    			}
	        	else
	        	{
	        		param.put("shipStatusId", 1);
	        	}

	        	// 수신데이터 처리 필요
	        	saveShipStatus(param);
	        	NcosApplication.transStatus.put("assetStatus", "Y");
	        	log.debug("assetInfo message end");
	        }
	        else {
	        	log.error("msgId is not valid....");
	        }

        }
        catch(Exception e) {
        	e.printStackTrace();
        	Map<String,Object> param = new HashMap();
        	param.put("status", "1");
			param.put("cscName", "4");
			param.put("workType", "8");
        	if(receiveType.equals("1")) {
        		NcosApplication.transStatus.put("heartbeatStatus", "N");
        		 param.put("reason", "데이터 전송(heartbeat) 실패");
        	}
        	else if(receiveType.equals("2")){
        		NcosApplication.transStatus.put("threatStatus", "N");
        		 param.put("reason", "데이터 전송(위협 정보) 실패");
        	}
        	else {
        		NcosApplication.transStatus.put("assetStatus", "N");
        		 param.put("reason", "데이터 전송(자산 정보) 실패");
        	}
        	try {
				funcService.saveFuncOperation(param);
			}
			catch(Exception ex) {
				ex.printStackTrace();
			}
        	e.printStackTrace();
        }
        finally {
        	try {
        		if(null != writer) writer.close();
        	}
        	catch(Exception e) {
        		log.error(e.getMessage());
        	}
        }
    }

    private int saveShipStatus(Map<String,Object> param) throws Exception {
    	return dao.updateByJob("Server.saveShipStatus", param);
    }

    @SuppressWarnings("unused")
	private int saveAssertStatusInfo(Map<String,Object> param) throws Exception {
    	return dao.updateByJob("Server.saveAssertStatusInfo", param);
    }

    private int saveThreatInfo(Map<String,Object> param) throws Exception {
    	return dao.updateByJob("Server.insertThreatInfo", param);
    }

    private int saveTransmitData(Map<String,Object> param) throws Exception {
    	return dao.updateByJob("Server.insertTransmitData", param);
    }


}

