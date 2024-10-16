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

	@Value("${crypto.key}")
	private String cryptoModeKey;

	private final FuncService funcService;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
	public void receive(Message msg) throws ParseException {
		// log.debug("msg:{}",);
		// byte[] byteArray =
		// msg.getPayload().toString().getBytes(StandardCharsets.UTF_8);
		// String message = new String(byteArray,StandardCharsets.UTF_8);
		/*
		 * String message = new String((byte[]) msg.getPayload());
		 * byte[] byteArray = message.getBytes(StandardCharsets.UTF_8);
		 * message = new String(byteArray,StandardCharsets.UTF_8);
		 */

		BufferedWriter writer = null;
		String receiveType = "1";
		try {
			String message = new String((byte[]) msg.getPayload());
			byte[] byteArray = message.getBytes(StandardCharsets.UTF_8);
			message = new String(byteArray, StandardCharsets.UTF_8);

			JSONObject jsonObj = ConvertUtil.convertToJson((byte[]) msg.getPayload());
			// byte[] msgid = Arrays.copyOfRange((byte[]) msg.getPayload(), 0, 1);
			int nMsgid = ConvertUtil.UnsignedByteParse(
					Arrays.copyOfRange(Arrays.copyOfRange((byte[]) msg.getPayload(), 0, 1), 0, 1), 0);

			log.debug("UdpInboundService data:{}", message);
			Map<String, Object> param = new HashMap<String, Object>();
			if (nMsgid == 1) {
				log.debug("heartbeat message start");
				param.put("unitId",
						ConvertUtil.UnsignedByteParse(Arrays.copyOfRange((byte[]) msg.getPayload(), 1, 2), 0));
				param.put("shipId",
						ConvertUtil.UnsignedShortParse(Arrays.copyOfRange((byte[]) msg.getPayload(), 2, 4), 0));
				param.put("asset_status", jsonObj.get("asset_status"));

				// 수신데이터 처리 필요
				saveAssertStatusInfo(param);
				NcosApplication.transStatus.put("heartbeatStatus", "Y");
				log.debug("heartbeat message end");
			}
			// else if(messageContent.startsWith("2")) {
			else if (nMsgid == 2) {
				receiveType = "2";
				log.debug("threatInfo message start");

				param.put("unitId",
						ConvertUtil.UnsignedByteParse(Arrays.copyOfRange((byte[]) msg.getPayload(), 1, 2), 0));
				param.put("shipId",
						ConvertUtil.UnsignedShortParse(Arrays.copyOfRange((byte[]) msg.getPayload(), 2, 4), 0));
				param.put("detectionTime", jsonObj.get("detection_time"));
				if (cryptoMode.equals("Y")) {
					param.put("srcIp", ScpDbUtil.scpEnc("" + jsonObj.get("src_ip"), cryptoModeKey));
					param.put("dstIp", ScpDbUtil.scpEnc("" + jsonObj.get("dst_ip"), cryptoModeKey));

				} else {
					param.put("srcIp", jsonObj.get("src_ip"));
					param.put("dstIp", jsonObj.get("dst_ip"));
				}
				param.put("srcPort", jsonObj.get("src_port"));
				param.put("dstPort", jsonObj.get("dst_port"));
				param.put("protocol", jsonObj.get("protocol"));
				param.put("payloadSize", jsonObj.get("payload_size"));
				param.put("fragmentation", jsonObj.get("fragmentation"));
				param.put("fragmentId", jsonObj.get("fragment_id"));
				param.put("detectionThreatName", jsonObj.get("detection_threat_name"));
				param.put("threatDetectionMethod", jsonObj.get("threat_detection_method"));
				param.put("threatImportance", jsonObj.get("threat_importance"));
				param.put("transSpeed", jsonObj.get("trans_speed"));
				param.put("payload", "");
				param.put("srcBytes", ((byte[]) msg.getPayload()).length);

				saveThreatInfo(param);
				saveTransmitData(param);
				NcosApplication.transStatus.put("threatStatus", "Y");
				log.debug("threatInfo message end");

			}

			// else if(messageContent.startsWith("3")) {
			else if (nMsgid == 3) {
				receiveType = "3";
				log.debug("assetInfo message start");
				param.put("unitId",
						ConvertUtil.UnsignedByteParse(Arrays.copyOfRange((byte[]) msg.getPayload(), 1, 2), 0));
				param.put("shipId",
						ConvertUtil.UnsignedShortParse(Arrays.copyOfRange((byte[]) msg.getPayload(), 2, 4), 0));

				if ("2".equals(String.valueOf(jsonObj.get("equip_status")))
						&& "2".equals(String.valueOf(jsonObj.get("func_status")))) {
					param.put("shipStatusId", 2);
				} else {
					param.put("shipStatusId", 1);
				}

				// 수신데이터 처리 필요
				saveShipStatus(param);
				NcosApplication.transStatus.put("assetStatus", "Y");
				log.debug("assetInfo message end");
			} else {
				log.error("msgId is not valid....");
			}

		} catch (Exception e) {
			Map<String, Object> param = new HashMap();
			param.put("status", "1");
			param.put("cscName", "4");
			param.put("workType", "8");
			if (receiveType.equals("1")) {
				NcosApplication.transStatus.put("heartbeatStatus", "N");
				param.put("reason", "데이터 전송(heartbeat) 실패");
			} else if (receiveType.equals("2")) {
				NcosApplication.transStatus.put("threatStatus", "N");
				param.put("reason", "데이터 전송(위협 정보) 실패");
			} else {
				NcosApplication.transStatus.put("assetStatus", "N");
				param.put("reason", "데이터 전송(자산 정보) 실패");
			}
			try {
				funcService.saveFuncOperation(param);
			} catch (Exception ex) {
				// ex.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			try {
				if (null != writer)
					writer.close();
			} catch (Exception e) {
				log.error(e.getMessage());
			}
		}
	}

	private int saveShipStatus(Map<String, Object> param) throws Exception {
		return dao.updateByJob("Server.saveShipStatus", param);
	}

	private int saveAssertStatusInfo(Map<String, Object> param) throws Exception {
		return dao.updateByJob("Server.saveAssertStatusInfo", param);
	}

	private int saveThreatInfo(Map<String, Object> param) throws Exception {
		return dao.updateByJob("Server.insertThreatInfo", param);
	}

	private int saveTransmitData(Map<String, Object> param) throws Exception {
		return dao.updateByJob("Server.insertTransmitData", param);
	}

}
