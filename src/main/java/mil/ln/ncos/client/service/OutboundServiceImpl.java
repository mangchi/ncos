package mil.ln.ncos.client.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.ip.udp.UnicastSendingMessageHandler;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeType;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mil.ln.ncos.NcosApplication;
import mil.ln.ncos.client.gateway.TcpGateway;
import mil.ln.ncos.cmmn.service.CmmnService;
import mil.ln.ncos.cmmn.util.ConvertUtil;
import mil.ln.ncos.cmmn.util.JsonUtil;
import mil.ln.ncos.cmmn.util.ScpDbUtil;
import mil.ln.ncos.func.service.FuncService;

@Slf4j
@RequiredArgsConstructor
@Service
public class OutboundServiceImpl implements OutboudService {

	private final TcpGateway tcpGateway;

	private final CmmnService cmmnService;

	private final FuncService funcService;

	@Value("${spring.profiles.active}")
	private String activeProfile;

	@Value("${udp.server.port}")
	private int port;

	@Value("${udp.server.host}")
	private String host;

	@Value("${prodMode}")
	private String prodMode;

	@Value("${cryptoMode}")
	private String cryptoMode;

	@Value("${crypto.key1}")
	private String cryptoModeKey1;

	@Value("${crypto.key2}")
	private String cryptoModeKey2;

	@Value("${tcpClient}")
	private String tcpClient;

	@Value("${udpClient}")
	private String udpClient;

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void saveTransmitData() throws Exception {

		if (prodMode.equals("Y") && activeProfile.indexOf("land") == -1 && activeProfile.indexOf("Land") == -1
				&& activeProfile.indexOf("hmm") == -1 || activeProfile.indexOf("Hmm") == -1) {
			try {
				Map<String, Object> param = new HashMap<String, Object>();
				Map<String, Object> transmitData = cmmnService.getTransmitDataByJob(param);
				log.debug("transmitData:{}", transmitData);
				if (null != transmitData) {
					param.put("threatId", transmitData.get("threatId"));
					cmmnService.saveTransmitData(param);
				}
				NcosApplication.transStatus.put("threatStatus", "Y");
			} catch (Exception e) {
				NcosApplication.transStatus.put("threatStatus", "N");
				Map<String, Object> param = new HashMap();
				param.put("status", "1");
				param.put("cscName", "4");
				param.put("workType", "8");
				param.put("reason", "데이터 전송 데이터 생 실패");
				funcService.saveFuncOperation(param);
				throw e;
			}
		}

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void sendHeartBeat() throws Exception {
		if (prodMode.equals("Y") && activeProfile.indexOf("land") == -1 && activeProfile.indexOf("Land") == -1) {
			log.debug("sendHeartBeat start...........");
			String message = null;
			String dataId = "";
			try {
				if (activeProfile.indexOf("hmm") > -1 || activeProfile.indexOf("Hmm") > -1) {

					Map<String, Object> satelliteInfo = cmmnService.getSatelliteTransByJob(null);
					long cscCnt = cmmnService.getCscStatusByJob();
					StringBuffer sb = new StringBuffer();
					sb.append("1").append("|");
					sb.append(satelliteInfo.get("shipId")).append("|");
					sb.append(satelliteInfo.get("unitId")).append("|");
					if (cscCnt > 0) {
						sb.append("1");
					} else {
						sb.append("2");
					}
					message = sb.toString();
					log.debug("sendHeartBeat:{}", message);
					int delay = Integer.parseInt(String.valueOf(satelliteInfo.get("systemOpStatusDelay") == null ? "5"
							: satelliteInfo.get("systemOpStatusDelay")));
					if (delay > 1) {
						delay = delay - 1;
					} else {
						delay = 1;
					}
					try {
						Thread.sleep(delay * 1000);
					} catch (InterruptedException ex) {
						ex.printStackTrace();
					}
					tcpGateway.send(message.getBytes());
				} else {
					log.debug("ncos heartbeat..................");
					Map<String, Object> param = new HashMap();
					param.put("messageType", "1");

					Map<String, Object> transmitData = cmmnService.getNavyTransmitDataByJob(param);
					if (null != transmitData && transmitData.containsKey("srcData")
							&& null != transmitData.get("srcData")) {
						dataId = String.valueOf(transmitData.get("dataId"));
						if (dataId.equals(NcosApplication.transStatus.get("heartbeatDataId"))) {
							log.error("이전 heartbeat send data........");
							return;
						}

						Map<String, Object> satelliteInfo = cmmnService.getSatelliteTransByJob(null);
						/*
						 * StringBuffer sb = new StringBuffer();
						 * sb.append("1").append("|");
						 * sb.append(satelliteInfo.get("shipId")).append("|");
						 * sb.append(satelliteInfo.get("unitId")).append("|");
						 * String srcData = String.valueOf(transmitData.get("srcData"));
						 * Map<String,Object> srcMap = JsonUtil.fromJsonStrToMap(srcData);
						 * sb.append(srcMap.get("asset_status"));
						 * message = sb.toString();
						 * log.debug("sendHeartBeat:{}", message);
						 */
						String srcData = String.valueOf(transmitData.get("srcData"));

						Map<String, Object> headerParam = new HashMap();
						headerParam.put("msgid", "1");
						headerParam.put("unit_id", satelliteInfo.get("unitId"));
						headerParam.put("ship_id", satelliteInfo.get("shipId"));
						headerParam.put("trans_speed", satelliteInfo.get("transSpeed"));
						// headerParam.put("msg_format",
						// Integer.toString((int)ConvertUtil.GetMsgFormat(srcData)));

						byte[] sendMsg = ConvertUtil.convertToByte1(srcData, headerParam);
						// JSONObject jsonObj = ConvertUtil.convertToJson(sendMsg); //수신 임시 테스트

						// transmitData.put("srcBytes", message.getBytes().length);
						transmitData.put("srcBytes", sendMsg.length);
						cmmnService.saveTransmitData(transmitData);
						if (Boolean.parseBoolean(tcpClient)) {
							log.debug("Tcp Server Host:{}", host);
							log.debug("Tcp Server port:{}", port);
							// byte[] sendData = tcpGateway.send(message.getBytes());
							// log.debug("ncos heartBeat send Data size:{}",sendData.length);
							tcpGateway.send(sendMsg);
							log.debug("ncos threatInfo sendData size:{}", sendMsg.length);
						}
						if (Boolean.parseBoolean(udpClient)) {
							log.debug("Udp Server Host:{}", host);
							log.debug("Udp Server port:{}", port);
							UnicastSendingMessageHandler handler = new UnicastSendingMessageHandler(host, port);
							// handler.handleMessage(MessageBuilder.withPayload(message).build());
							handler.handleMessage(MessageBuilder.withPayload(sendMsg)
									.setHeader(MessageHeaders.CONTENT_TYPE, MimeType.valueOf("text/plain"))
									.build());
						}

						// byte[] responseBytes = tcpGateway.send(message.getBytes());

					}

				}
				NcosApplication.transStatus.put("heartbeatDataId", dataId);
				NcosApplication.transStatus.put("heartbeatStatus", "Y");
			} catch (Exception e) {
				NcosApplication.transStatus.put("heartbeatStatus", "N");
				e.printStackTrace();
				Map<String, Object> param = new HashMap();
				param.put("status", "1");
				param.put("cscName", "4");
				param.put("workType", "8");
				param.put("reason", "데이터 전송(Heartbeat) 실패");
				funcService.saveFuncOperation(param);
				throw e;
			}
			log.debug("sendHeartBeat finish...........");
		}

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void sendThreatInfo() throws Exception {
		if (prodMode.equals("Y") && activeProfile.indexOf("land") == -1 && activeProfile.indexOf("Land") == -1) {
			log.debug("sendThreatInfo start...........");
			String message = null;
			String dataId = "";
			try {

				if (activeProfile.indexOf("hmm") > -1 || activeProfile.indexOf("Hmm") > -1) { // 민수
					Map<String, Object> param = new HashMap();
					Map<String, Object> transmitData = cmmnService.getTransmitDataByJob(param);
					if (transmitData != null) {
						StringBuffer sb = new StringBuffer();
						Map<String, Object> satelliteInfo = cmmnService.getSatelliteTransByJob(null);

						sb.append("2").append("|");
						sb.append(satelliteInfo.get("shipId")).append("|");
						sb.append(satelliteInfo.get("unitId")).append("|");
						sb.append(transmitData.get("detectionTime")).append("|");

						boolean srcIp = Boolean.parseBoolean(String.valueOf(satelliteInfo.get("srcIp")));
						sb.append(srcIp ? transmitData.get("srcIp") : "").append("|");
						boolean dstIp = Boolean.parseBoolean(String.valueOf(satelliteInfo.get("dstIp")));
						sb.append(dstIp ? transmitData.get("dstIp") : "").append("|");
						boolean srcPort = Boolean.parseBoolean(String.valueOf(satelliteInfo.get("srcPort")));
						sb.append(srcPort ? transmitData.get("srcPort") : "").append("|");
						boolean dstPort = Boolean.parseBoolean(String.valueOf(satelliteInfo.get("dstPort")));
						sb.append(dstPort ? transmitData.get("dstPort") : "").append("|");
						boolean protocol = Boolean.parseBoolean(String.valueOf(satelliteInfo.get("protocol")));
						sb.append(protocol ? transmitData.get("protocol") : "").append("|");
						boolean payloadSize = Boolean.parseBoolean(String.valueOf(satelliteInfo.get("payloadSize")));
						sb.append(payloadSize ? transmitData.get("payloadSize") : "").append("|");
						if (transmitData.get("fragmentation") instanceof Boolean) {
							boolean fragmentationSet = Boolean
									.parseBoolean(String.valueOf(satelliteInfo.get("fragmentation")));
							boolean fragmentation = Boolean
									.parseBoolean(String.valueOf(transmitData.get("fragmentation")));
							sb.append(fragmentationSet ? (fragmentation ? "1" : "0") : "").append("|");

						} else {
							sb.append(transmitData.get("fragmentation")).append("|");
						}
						boolean fragmentId = Boolean.parseBoolean(String.valueOf(satelliteInfo.get("fragmentId")));
						sb.append(fragmentId ? "0" : "").append("|");
						// sb.append(transmitData.get("fragmentId")).append("|");
						boolean detectionThreatName = Boolean
								.parseBoolean(String.valueOf(satelliteInfo.get("detectionThreatName")));
						sb.append(detectionThreatName ? transmitData.get("detectionThreatName") : "").append("|");
						boolean threatDetectionMethod = Boolean
								.parseBoolean(String.valueOf(satelliteInfo.get("threatDetectionMethod")));
						sb.append(threatDetectionMethod ? transmitData.get("threatDetectionMethod") : "").append("|");
						boolean threatImportance = Boolean
								.parseBoolean(String.valueOf(satelliteInfo.get("threatImportance")));
						sb.append(threatImportance ? transmitData.get("threatImportance") : "").append("|");
						boolean payload = Boolean.parseBoolean(String.valueOf(satelliteInfo.get("payload")));
						String payloadContent = "0";
						if (payload) {
							StringBuffer sbf = new StringBuffer();
							BufferedReader reader = null;
							try {
								if (transmitData.containsKey("payload") && transmitData.get("payload") != null
										&& !transmitData.get("payload").equals("")) {

									String payloadPath = String.valueOf(transmitData.get("payload"));
									File payloadFile = new File(payloadPath);
									if (payloadFile.exists() && payloadFile.isFile()) {
										reader = new BufferedReader(new FileReader(payloadPath));
										String str;
										while ((str = reader.readLine()) != null) {
											sbf.append(str);
										}
										payloadContent = sbf.toString();
										reader.close();
									} else {
										log.error(String.format("payload file: \"%s\" is not found", payloadPath));
									}

								}
							} catch (IOException ie) {
								log.error(ie.getMessage());
							} finally {
								if (reader != null)
									reader.close();
							}
						}
						sb.append(payload ? payloadContent : "0");

						message = sb.toString();
						log.debug("sendThreatInfo:{}", message);
						byte[] responseBytes = tcpGateway.send(message.getBytes());
						log.debug("message byte:{}", responseBytes);
						// Map<String, Object> param = new HashMap();
						param.put("detectionTime", transmitData.get("detectionTime"));
						param.put("threatId", transmitData.get("threatId"));
						// param.put("dataId", transmitData.get("dataId"));
						param.put("srcBytes", message.getBytes().length);
						cmmnService.saveTransmitData(param);
					}
				} else {
					// 적용
					log.debug("ncos send message Type2");
					//
					Map<String, Object> param = new HashMap();
					param.put("messageType", "2");

					Map<String, Object> transmitData = cmmnService.getNavyTransmitDataByJob(param);
					;
					if (null != transmitData && transmitData.containsKey("srcData")
							&& null != transmitData.get("srcData")) {
						dataId = String.valueOf(transmitData.get("dataId"));
						if (dataId.equals(NcosApplication.transStatus.get("threatDataId"))) {
							log.error("이전 threat send data........");
							return;
						}
						Map<String, Object> satelliteInfo = cmmnService.getSatelliteTransByJob(null);
						int transSpeed = Integer.parseInt(String.valueOf(
								satelliteInfo.get("transSpeed") == null ? "5000" : satelliteInfo.get("transSpeed")));
						/*
						 * /*String srcData =
						 * "{\"detection_time\": \"2023-06-26 20:16:42\", \"src_ip\": \"3421657623\", \"dst_ip\": \"3232286721\""
						 * +
						 * ", \"src_port\": \"120\", \"dst_port\": \"100\", \"protocol\": \"1\", \"payload_size\": \"0\", \"fragmentation\": \"0\""
						 * +
						 * ", \"threat_detection_method\": \"2\", \"threat_importance\": \"5\", \"payload\": \"0\", \"fragment_id\": \"1\", \"detection_threat_name\": \"방화벽 위협 탐지\"}"
						 * ;
						 * 
						 * // 오류 데이터
						 * String srcData =
						 * "{\"detection_time\": \"2023-07-02 20:48:08\", \"src_ip\": \"3421657623\", \"dst_ip\": \"3232286721\""
						 * +
						 * ", \"src_port\": \"120\", \"dst_port\": \"100\", \"protocol\": \"1\", \"payload_size\": \"0\", \"fragmentation\": \"0\", \"threat_detection_method\": \"2\""
						 * +
						 * ", \"threat_importance\": \"3\", \"payload\": \"\", \"fragment_id\": \"1\", \"detection_threat_name\": \"방화벽 위협 탐지\"}"
						 * ;
						 */
						String srcData = String.valueOf(transmitData.get("srcData"));
						Map<String, Object> srDataMap = JsonUtil.fromJsonStrToMap(srcData);
						if (cryptoMode.equals("Y")) {
							if (srDataMap.containsKey("src_ip")) {
								log.debug("enc src_ip:{}", srDataMap.get("src_ip"));
								if (activeProfile.equals("navy")) {
									srDataMap.put("src_ip",
											ScpDbUtil.scpDec(String.valueOf(srDataMap.get("src_ip")), cryptoModeKey1));
								} else {
									srDataMap.put("src_ip",
											ScpDbUtil.scpDec(String.valueOf(srDataMap.get("src_ip")), cryptoModeKey2));
								}

								log.debug("dec src_ip:{}", srDataMap.get("src_ip"));
							}
							if (srDataMap.containsKey("dst_ip")) {
								log.debug("enc dst_ip:{}", srDataMap.get("dst_ip"));
								if (activeProfile.equals("navy")) {
									srDataMap.put("dst_ip",
											ScpDbUtil.scpDec(String.valueOf(srDataMap.get("dst_ip")), cryptoModeKey1));
								} else {
									srDataMap.put("dst_ip",
											ScpDbUtil.scpDec(String.valueOf(srDataMap.get("dst_ip")), cryptoModeKey2));
								}

								log.debug("dec dst_ip:{}", srDataMap.get("dst_ip"));
							}
						}
						srcData = JsonUtil.toGsonStr(srDataMap);

						Map<String, Object> headerParam = new HashMap();
						headerParam.put("msgid", "2");
						headerParam.put("unit_id", satelliteInfo.get("unitId"));
						headerParam.put("ship_id", satelliteInfo.get("shipId"));
						headerParam.put("trans_speed", satelliteInfo.get("transSpeed"));
						// headerParam.put("msg_format",
						// Integer.toString((int)ConvertUtil.GetMsgFormat(srcData)));

						// byte[] sendMsg = ConvertUtil.convertToByte(srcData, headerParam);
						byte[] sendMsg = ConvertUtil.convertToByte1(srcData, headerParam);
						log.debug("sendMsg:{}", sendMsg);
						// JSONObject jsonObj = ConvertUtil.convertToJson(sendMsg); //수신 임시 테스트

						param.put("detectionTime", srDataMap.get("detection_time"));
						// param.put("threatId", transmitData.get("threatId"));
						param.put("dataId", transmitData.get("dataId"));
						param.put("srcBytes", sendMsg.length);
						param.put("transSpeed", satelliteInfo.get("transSpeed"));
						cmmnService.saveTransmitData(param);
						if (Boolean.parseBoolean(tcpClient)) {
							// byte[] sendData = tcpGateway.send(message.getBytes());
							// log.debug("ncos threatInfo sendData size:{}",sendData.length);
							tcpGateway.send(sendMsg);
							log.debug("ncos threatInfo sendData size:{}", sendMsg.length);
						}
						if (Boolean.parseBoolean(udpClient)) {
							UnicastSendingMessageHandler handler = new UnicastSendingMessageHandler(host, port);
							// handler.handleMessage(MessageBuilder.withPayload(sb.toString()).build());
							handler.handleMessage(MessageBuilder.withPayload(sendMsg)
									.setHeader(MessageHeaders.CONTENT_TYPE, MimeType.valueOf("text/plain"))
									.build());
						}
						int bitLength = sendMsg.length * 8;
						log.debug("bitLength:{}", bitLength);
						log.debug("transSpeed:{}", transSpeed);
						if (bitLength > transSpeed) {
							int delayLength = bitLength - transSpeed;
							long delay = delayLength / transSpeed;
							log.debug("delay:{}", delay);
							TimeUnit.SECONDS.sleep(delay * 1000);
							/*
							 * try {
							 * Thread.sleep(delay*1000);
							 * }
							 * catch(InterruptedException ex) {
							 * ex.printStackTrace();
							 * }
							 */
						}
						NcosApplication.transStatus.put("threatDataId", dataId);
						NcosApplication.transStatus.put("threatStatus", "Y");
					}
				}

			} catch (Exception e) {
				Map<String, Object> param = new HashMap();
				param.put("status", "1");
				param.put("cscName", "4");
				param.put("workType", "8");
				NcosApplication.transStatus.put("threatStatus", "N");
				param.put("reason", "데이터 전송(위협 정보) 실패");
				funcService.saveFuncOperation(param);
				throw e;
			}
			log.debug("sendThreatInfo finish...........");
		}

	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void sendAssertInfo() throws Exception {
		if (prodMode.equals("Y") && activeProfile.indexOf("land") == -1 && activeProfile.indexOf("Land") == -1) {
			log.debug("sendAssertInfo start...........");
			try {

				if (activeProfile.indexOf("hmm") == -1 && activeProfile.indexOf("Hmm") == -1) { // 해군
					log.debug("ncos send message type3");
					String dataId = "";
					Map<String, Object> param = new HashMap();
					param.put("messageType", "3");
					/*
					 * param.put("psTime", psTime);
					 * int cnt = cmmnService.setNavyTransmitDataSetStartTimeByJob(param);
					 * Map<String,Object> transmitData = null;
					 * if(cnt > 0) {
					 * transmitData = cmmnService.getNavyTransmitDataByJob(param);
					 * }
					 */
					Map<String, Object> transmitData = cmmnService.getNavyTransmitDataByJob(param);
					if (null != transmitData && transmitData.containsKey("srcData")
							&& null != transmitData.get("srcData")) {
						dataId = String.valueOf(transmitData.get("dataId"));
						if (dataId.equals(NcosApplication.transStatus.get("assetDataId"))) {
							log.error("이전 asset send data........");
							return;
						}
						Map<String, Object> satelliteInfo = cmmnService.getSatelliteTransByJob(null);
						/*
						 * StringBuffer sb = new StringBuffer();
						 * sb.append("3").append("|");
						 * sb.append(satelliteInfo.get("shipId")).append("|");
						 * sb.append(satelliteInfo.get("unitId")).append("|");
						 * String srcData = String.valueOf(transmitData.get("srcData"));
						 * Map<String,Object> srcMap = JsonUtil.fromJsonStrToMap(srcData);
						 * if(srcMap.get("equip_status").equals("1")) {
						 * sb.append("1");
						 * }
						 * else {
						 * sb.append("2");
						 * }
						 * sb.append("|");
						 * if(srcMap.get("func_status").equals("1")) {
						 * sb.append("1");
						 * }
						 * else {
						 * sb.append("2");
						 * }
						 * message = sb.toString();
						 * log.debug("sendHeartBeat:{}", message);
						 * transmitData.put("srcBytes", message.getBytes().length);
						 * cmmnService.saveTransmitData(transmitData);
						 * if(Boolean.parseBoolean(tcpClient)) {
						 * byte[] sendData = tcpGateway.send(message.getBytes());
						 * log.debug("ncos assetInfo sendData size:{}",sendData.length);
						 * }
						 * if(Boolean.parseBoolean(udpClient)) {
						 * UnicastSendingMessageHandler handler = new UnicastSendingMessageHandler(host,
						 * port);
						 * handler.handleMessage(MessageBuilder.withPayload(message).build());
						 * }
						 */

						String srcData = String.valueOf(transmitData.get("srcData"));

						Map<String, Object> headerParam = new HashMap();
						headerParam.put("msgid", "3");
						headerParam.put("unit_id", satelliteInfo.get("unitId"));
						headerParam.put("ship_id", satelliteInfo.get("shipId"));
						headerParam.put("trans_speed", satelliteInfo.get("transSpeed"));
						// headerParam.put("msg_format",
						// Integer.toString((int)ConvertUtil.GetMsgFormat(srcData)));

						byte[] sendMsg = ConvertUtil.convertToByte1(srcData, headerParam);
						// JSONObject jsonObj = ConvertUtil.convertToJson(sendMsg); //수신 임시 테스트

						// transmitData.put("srcBytes", message.getBytes().length);
						transmitData.put("srcBytes", sendMsg.length);
						cmmnService.saveTransmitData(transmitData);
						if (Boolean.parseBoolean(tcpClient)) {
							tcpGateway.send(sendMsg);
							log.debug("ncos threatInfo sendData size:{}", sendMsg.length);
						}
						if (Boolean.parseBoolean(udpClient)) {
							UnicastSendingMessageHandler handler = new UnicastSendingMessageHandler(host, port);
							handler.handleMessage(MessageBuilder.withPayload(sendMsg)
									.setHeader(MessageHeaders.CONTENT_TYPE, MimeType.valueOf("text/plain"))
									.build());
						}
						NcosApplication.transStatus.put("assetDataId", dataId);
						NcosApplication.transStatus.put("assetStatus", "Y");

					}
				}
			} catch (Exception e) {
				NcosApplication.transStatus.put("assetStatus", "N");
				Map<String, Object> param = new HashMap();
				param.put("status", "1");
				param.put("cscName", "4");
				param.put("workType", "8");
				param.put("reason", "데이터 전송(자산 정보) 실패");
				funcService.saveFuncOperation(param);
				throw e;
			}
			log.debug("sendAssertInfo finish...........");
		}

	}

}
