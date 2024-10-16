package mil.ln.ncos.server.tcp.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mil.ln.ncos.NcosApplication;
import mil.ln.ncos.cmmn.util.DateUtil;
import mil.ln.ncos.dao.DAO;
import mil.ln.ncos.func.service.FuncService;

@Slf4j
@RequiredArgsConstructor
@Service
public class MessageServiceImpl implements MessageService {

	private final DAO dao;
	private final FuncService funcService;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	// @Transactional(rollbackFor = Exception.class, propagation =
	// Propagation.REQUIRED, isolation = Isolation.SERIALIZABLE)
	public byte[] processMessage(byte[] message) {
		String messageContent = new String(message);
		String responseContent = null;
		BufferedWriter writer = null;
		String receiveType = "1";
		try {
			log.info("Receive message: {}", messageContent);
			responseContent = String.format("Message \"%s\" is processed", messageContent);
			String[] messageContents = messageContent.split("[|]");
			Map<String, Object> param = new HashMap();
			if (messageContent.startsWith("1")) { // heartbeat
				log.debug("heartbeat message start");
				param.put("shipId", messageContents[1]);
				param.put("unitId", messageContents[2]);
				param.put("shipStatusId", messageContents[3]);
				log.debug("param:{}", param);
				saveShipStatus(param);
				log.debug("heartbeat message end");
				NcosApplication.transStatus.put("heartbeatStatus", "Y");
			} else if (messageContent.startsWith("2")) {
				log.debug("threatInfo message start");
				receiveType = "2";
				param.put("shipId", messageContents[1]);
				param.put("unitId", messageContents[2]);
				param.put("detectionTime", messageContents[3]);
				param.put("srcIp", messageContents[4]);
				param.put("dstIp", messageContents[5]);
				param.put("srcPort", messageContents[6]);
				param.put("dstPort", messageContents[7]);
				param.put("protocol", messageContents[8]);
				param.put("payloadSize", messageContents[9]);
				param.put("fragmentation", messageContents[10]);
				param.put("fragmentId", messageContents[11]);
				param.put("detectionThreatName", messageContents[12]);
				param.put("threatDetectionMethod", messageContents[13]);
				param.put("threatImportance", messageContents[14]);
				param.put("payload", messageContents[15]);
				if (!messageContents[15].equals("0")) {
					File dir = new File("/home/user/logs/"
							+ DateUtil.getFrmtDate(Timestamp.valueOf(LocalDateTime.now()), "yyyyMMdd"));
					if (!dir.exists()) {
						dir.mkdir();
					}
					File file = new File("/home/user/logs/"
							+ DateUtil.getFrmtDate(Timestamp.valueOf(LocalDateTime.now()), "yyyyMMdd") + "/"
							+ DateUtil.getFrmtDate(Timestamp.valueOf(LocalDateTime.now()), "HHmmss") + ".log");
					if (!file.exists()) {
						log.debug("path:{}", file.getAbsolutePath());
						param.put("payload", file.getAbsolutePath());
						file.createNewFile();
					}
					writer = new BufferedWriter(new FileWriter(file, true));
					writer.flush();
					writer.write(messageContents[15]);
					writer.close();
				}
				param.put("srcBytes", messageContent.length());
				saveThreatInfo(param);
				saveTransmitData(param);
				log.debug("threatInfo message end");
				NcosApplication.transStatus.put("threatStatus", "Y");

			}

			else if (messageContent.startsWith("3")) {
				log.debug("assetInfo message start");
			} else {
				log.error("msgId is not valid....");
			}

		} catch (IOException ie) {
			Map<String, Object> param = new HashMap();
			param.put("status", "1");
			param.put("cscName", "4");
			param.put("workType", "8");
			if (receiveType.equals("1")) {
				NcosApplication.transStatus.put("heartbeatStatus", "N");
				param.put("reason", "데이터 전송(heartbeat) 실패");
			} else {
				NcosApplication.transStatus.put("threatStatus", "N");
				param.put("reason", "데이터 전송(위협 정보) 실패");
			}
			try {
				funcService.saveFuncOperation(param);
			} catch (Exception e) {
				// e.printStackTrace();
			}
			ie.printStackTrace();
		} catch (Exception e) {
			Map<String, Object> param = new HashMap();
			param.put("status", "1");
			param.put("cscName", "4");
			param.put("workType", "8");
			if (receiveType.equals("1")) {
				NcosApplication.transStatus.put("heartbeatStatus", "N");
				param.put("reason", "데이터 전송(heartbeat) 실패");
			} else {
				NcosApplication.transStatus.put("threatStatus", "N");
				param.put("reason", "데이터 전송(위협 정보) 실패");
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
		if (responseContent != null) {
			return responseContent.getBytes();
		}
		return null;

	}

	private int saveShipStatus(Map<String, Object> param) throws Exception {
		return dao.updateByJob("Server.saveShipStatus", param);
	}

	private int saveThreatInfo(Map<String, Object> param) throws Exception {
		return dao.updateByJob("Server.insertThreatInfo", param);
	}

	private int saveTransmitData(Map<String, Object> param) throws Exception {
		return dao.updateByJob("Server.insertTransmitData", param);
	}

}
