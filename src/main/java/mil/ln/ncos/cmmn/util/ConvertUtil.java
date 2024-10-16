package mil.ln.ncos.cmmn.util;

import java.math.BigInteger;
import java.text.SimpleDateFormat;

import java.util.Arrays;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;

import java.util.Map;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.boot.configurationprocessor.json.JSONException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConvertUtil {

	private static String[] msgStruct = {
			"detection_time",
			"src_ip",
			"dst_ip",
			"src_port",
			"dst_port",
			"protocol",
			"fragmentation",
			"fragment_id",
			"threat_detection_method",
			"threat_importance",
			"payload_size",
			"detection_threat_name"
	};

	private static int[] bodyLen = { 8, 4, 4, 2, 2, 1, 1, 2, 1, 1, 2, 1 };

	private static String msgStructure = "{"
			+ "\"detection_time\":{\"len\":8,\"index\":0, \"flag\":false},"
			+ "\"src_ip\":{\"len\":4,\"index\":1, \"flag\":false},"
			+ "\"dst_ip\":{\"len\":4,\"index\":2, \"flag\":false},"
			+ "\"src_port\":{\"len\":2,\"index\":3, \"flag\":false},"
			+ "\"dst_port\":{\"len\":2,\"index\":4, \"flag\":false},"
			+ "\"protocol\":{\"len\":1,\"index\":5, \"flag\":false},"
			+ "\"fragmentation\":{\"len\":1,\"index\":6, \"flag\":false},"
			+ "\"fragment_id\":{\"len\":2,\"index\":7, \"flag\":false},"
			+ "\"threat_detection_method\":{\"len\":1,\"index\":8, \"flag\":false},"
			+ "\"threat_importance\":{\"len\":1,\"index\":9, \"flag\":false},"
			+ "\"payload_size\":{\"len\":2,\"index\":10, \"flag\":false},"
			+ "\"detection_threat_name\":{\"len\":1,\"index\":11, \"flag\":false},"
			+ "}";

	private static JSONObject jsonObject = null;

	@SuppressWarnings("unchecked")
	public static void initJson(short msg_format) throws ParseException {
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(msgStructure);
		jsonObject = (JSONObject) obj;

		((JSONObject) jsonObject.get("detection_time")).put("flag", ((msg_format & 0x0001) != 0));
		((JSONObject) jsonObject.get("src_ip")).put("flag", ((msg_format & 0x0002) != 0));
		((JSONObject) jsonObject.get("dst_ip")).put("flag", ((msg_format & 0x0004) != 0));
		((JSONObject) jsonObject.get("src_port")).put("flag", ((msg_format & 0x0008) != 0));
		((JSONObject) jsonObject.get("dst_port")).put("flag", ((msg_format & 0x0010) != 0));
		((JSONObject) jsonObject.get("protocol")).put("flag", ((msg_format & 0x0020) != 0));
		((JSONObject) jsonObject.get("fragmentation")).put("flag", ((msg_format & 0x0040) != 0));
		((JSONObject) jsonObject.get("fragment_id")).put("flag", ((msg_format & 0x0080) != 0));
		((JSONObject) jsonObject.get("threat_detection_method")).put("flag", ((msg_format & 0x0100) != 0));
		((JSONObject) jsonObject.get("threat_importance")).put("flag", ((msg_format & 0x0200) != 0));
		((JSONObject) jsonObject.get("payload_size")).put("flag", ((msg_format & 0x0400) != 0));
		((JSONObject) jsonObject.get("detection_threat_name")).put("flag", ((msg_format & 0x0800) != 0));
	}

	public static void test() throws ParseException {
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(msgStructure);
		JSONObject jsonObject = (JSONObject) obj;
		log.debug("jsonObject:{}", jsonObject);

	}

	public static long LongToByte(byte[] data) {
		if (data == null || data.length != 8)
			return 0;

		return (long) ((long) (0xff & data[0]) << 56 |
				(long) (0xff & data[1]) << 48 |
				(long) (0xff & data[2]) << 40 |
				(long) (0xff & data[3]) << 32 |
				(long) (0xff & data[4]) << 24 |
				(long) (0xff & data[5]) << 16 |
				(long) (0xff & data[6]) << 8 |
				(long) (0xff & data[7]) << 0);
	}

	public static byte[] LongToByte(long number) {

		return new byte[] {
				(byte) ((number >> 56) & 0xff),
				(byte) ((number >> 48) & 0xff),
				(byte) ((number >> 40) & 0xff),
				(byte) ((number >> 32) & 0xff),
				(byte) ((number >> 24) & 0xff),
				(byte) ((number >> 16) & 0xff),
				(byte) ((number >> 8) & 0xff),
				(byte) ((number >> 0) & 0xff),
		};
	}

	public static long uLongTOLong(long number) {
		BigInteger bigInteger = BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.valueOf(number));

		return bigInteger.longValue();
	}

	public static long UnsignedIntParse(byte[] data, int offset) {
		if (data == null || data.length != 4)
			return 0;

		return (((long) data[offset] & 0xffL) << 24)
				| (((long) data[offset + 1] & 0xffL) << 16)
				| (((long) data[offset + 2] & 0xffL) << 8)
				| ((long) data[offset + 3] & 0xffL);
	}

	public static byte[] UnsignedIntParse(long number) {
		byte[] data = new byte[4];

		data[0] = (byte) ((number >> 24) & 0xff);
		data[1] = (byte) ((number >> 16) & 0xff);
		data[2] = (byte) ((number >> 8) & 0xff);
		data[3] = (byte) (number & 0xff);

		return data;
	}

	public static byte[] SignedIntParse(int value) {
		byte[] byteArray = new byte[4];
		byteArray[0] = (byte) (value >> 24);
		byteArray[1] = (byte) (value >> 16);
		byteArray[2] = (byte) (value >> 8);
		byteArray[3] = (byte) (value);
		return byteArray;
	}

	public static int SignedIntParse(byte bytes[]) {
		return ((((int) bytes[0] & 0xff) << 24) |
				(((int) bytes[1] & 0xff) << 16) |
				(((int) bytes[2] & 0xff) << 8) |
				(((int) bytes[3] & 0xff)));
	}

	public static int UnsignedShortParse(byte[] data, int offset) {
		if (data == null || data.length != 2)
			return 0;

		return ((data[offset] & 0xff) << 8) | (data[offset + 1] & 0xff);
	}

	public static byte[] UnsignedShortParse(int number) {
		byte[] data = new byte[2];

		data[0] = (byte) ((number >> 8) & 0xff);
		data[1] = (byte) (number & 0xff);

		return data;
	}

	public static short UnsignedByteParse(byte[] data, int offset) {
		if (data == null || data.length != 1)
			return 0;
		return (short) (data[offset] & 0xff);
	}

	public static byte[] UnsignedByteParse(short number) {
		return new byte[] { (byte) (number & 0xff) };
	}

	public static byte[] convertToByte(String str, Map<String, Object> headerParam)
			throws java.text.ParseException, ParseException {
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(str);
		JSONObject jsonObj = (JSONObject) obj;

		boolean[] boolArr = PrintMsgFormatBitField(Short.parseShort(headerParam.get("msg_format").toString()));

		int totalLen = 0;
		for (int i = 0; i < 12; i++) {
			if (boolArr[i]) {
				if (i == 11) {
					// length와 명
					totalLen += ((String) jsonObj.get("detection_threat_name")).getBytes().length + 1;
				} else {
					totalLen += bodyLen[i];
				}
			}
		}

		/****************************************************************************************************************
		 * header 구성
		 ****************************************************************************************************************/
		int idx = 0;

		// msgid
		String strMsgid = (String) headerParam.get("msgid");

		byte[] convertByte = new byte[totalLen + ("2".equals(strMsgid) ? 8 + 2 : 6)]; // +2 trans_speed

		byte[] msgid = UnsignedByteParse(Short.parseShort(strMsgid));
		convertByte[idx++] = msgid[0];

		byte[] unit_id = UnsignedByteParse(Short.parseShort(headerParam.get("unit_id").toString()));
		convertByte[idx++] = unit_id[0];

		byte[] ship_id = UnsignedShortParse(Integer.parseInt(headerParam.get("ship_id").toString()));
		convertByte[idx++] = ship_id[0];
		convertByte[idx++] = ship_id[1];

		byte[] msg_len = UnsignedShortParse(totalLen + 6);
		convertByte[idx++] = msg_len[0];
		convertByte[idx++] = msg_len[1];

		/****************************************************************************************************************
		 * body 구성
		 ****************************************************************************************************************/
		if ("2".equals(strMsgid)) {
			byte[] msg_format = UnsignedShortParse(Integer.parseInt(headerParam.get("msg_format").toString()));
			convertByte[idx++] = msg_format[0];
			convertByte[idx++] = msg_format[1];
			idx = 8;

			for (int i = 0; i < 12; i++) {
				if (boolArr[i]) {
					if (i == 0) // detection_time
					{
						SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						Date time = (Date) formatter.parse((String) jsonObj.get("detection_time"));
						// byte[] data = UnsignedIntParse(time.getTime() & 0xFFFFFFFFL);
						byte[] data = LongToByte(time.getTime());
						for (int j = 0; j < bodyLen[i]; j++) {
							convertByte[idx++] = data[j];
						}
					} else if (i == 1) // src_ip
					{
						String strValue = (String) jsonObj.get("src_ip");
						if (!StringUtil.isEmpty(strValue)) {
							byte[] data = UnsignedIntParse(Integer.parseUnsignedInt(strValue) & 0xFFFFFFFFL);
							for (int j = 0; j < bodyLen[i]; j++) {
								convertByte[idx++] = data[j];
							}
						} else {
							for (int j = 0; j < bodyLen[i]; j++) {
								convertByte[idx++] = 0;
							}
						}
					} else if (i == 2) // dst_ip
					{
						String strValue = (String) jsonObj.get("dst_ip");
						if (!StringUtil.isEmpty(strValue)) {
							byte[] data = UnsignedIntParse(Integer.parseUnsignedInt(strValue) & 0xFFFFFFFFL);
							for (int j = 0; j < bodyLen[i]; j++) {
								convertByte[idx++] = data[j];
							}
						} else {
							for (int j = 0; j < bodyLen[i]; j++) {
								convertByte[idx++] = 0;
							}
						}
					} else if (i == 3) {
						String strValue = (String) jsonObj.get("src_port");
						if (!StringUtil.isEmpty(strValue)) {
							byte[] data = UnsignedShortParse(Integer.parseInt(strValue));
							for (int j = 0; j < bodyLen[i]; j++) {
								convertByte[idx++] = data[j];
							}
						} else {
							for (int j = 0; j < bodyLen[i]; j++) {
								convertByte[idx++] = 0;
							}
						}
					} else if (i == 4) {
						String strValue = (String) jsonObj.get("dst_port");
						if (!StringUtil.isEmpty(strValue)) {
							byte[] data = UnsignedShortParse(Integer.parseInt(strValue));
							for (int j = 0; j < bodyLen[i]; j++) {
								convertByte[idx++] = data[j];
							}
						} else {
							for (int j = 0; j < bodyLen[i]; j++) {
								convertByte[idx++] = 0;
							}
						}
					} else if (i == 5) // protocol unsigned char
					{
						String strValue = (String) jsonObj.get("protocol");
						if (!StringUtil.isEmpty(strValue)) {
							byte[] data = UnsignedByteParse(Short.parseShort(strValue));
							for (int j = 0; j < bodyLen[i]; j++) {
								convertByte[idx++] = data[j];
							}
						} else {
							for (int j = 0; j < bodyLen[i]; j++) {
								convertByte[idx++] = 0;
							}
						}
					} else if (i == 6) // fragmentation unsigned char
					{
						String strValue = (String) jsonObj.get("fragmentation");
						if (!StringUtil.isEmpty(strValue)) {
							byte[] data = UnsignedByteParse(Short.parseShort(strValue));
							for (int j = 0; j < bodyLen[i]; j++) {
								convertByte[idx++] = data[j];
							}
						} else {
							for (int j = 0; j < bodyLen[i]; j++) {
								convertByte[idx++] = 0;
							}
						}
					} else if (i == 7) // fragment_id unsigned short
					{
						String strValue = (String) jsonObj.get("fragment_id");
						if (!StringUtil.isEmpty(strValue)) {
							byte[] data = UnsignedShortParse(Integer.parseInt(strValue));
							for (int j = 0; j < bodyLen[i]; j++) {
								convertByte[idx++] = data[j];
							}
						} else {
							for (int j = 0; j < bodyLen[i]; j++) {
								convertByte[idx++] = 0;
							}
						}
					} else if (i == 8) // threat detection method unsigned char
					{
						String strValue = (String) jsonObj.get("threat_detection_method");
						if (!StringUtil.isEmpty(strValue)) {
							byte[] data = UnsignedByteParse(Short.parseShort(strValue));
							for (int j = 0; j < bodyLen[i]; j++) {
								convertByte[idx++] = data[j];
							}
						} else {
							for (int j = 0; j < bodyLen[i]; j++) {
								convertByte[idx++] = 0;
							}
						}
					} else if (i == 9) // threat importance unsigned char [위협 중요도]
					{
						String strValue = (String) jsonObj.get("threat_importance");
						if (!StringUtil.isEmpty(strValue)) {
							byte[] data = UnsignedByteParse(Short.parseShort(strValue));
							for (int j = 0; j < bodyLen[i]; j++) {
								convertByte[idx++] = data[j];
							}
						} else {
							for (int j = 0; j < bodyLen[i]; j++) {
								convertByte[idx++] = 0;
							}
						}
					} else if (i == 10) // payload size unsigned char [페이로드 크기] -> short로 변경
					{
						String strValue = (String) jsonObj.get("payload_size");
						if (!StringUtil.isEmpty(strValue)) {
							// byte[] data = UnsignedByteParse(Short.parseShort((String)
							// jsonObj.get("payload_size")));
							byte[] data = UnsignedShortParse(Integer.parseInt(strValue));
							for (int j = 0; j < bodyLen[i]; j++) {
								convertByte[idx++] = data[j];
							}
						} else {
							for (int j = 0; j < bodyLen[i]; j++) {
								convertByte[idx++] = 0;
							}
						}
					} else if (i == 11) // detection threat name len 실제 name을 조합해야하는지? [탐지 위협명]
					{
						String strValue = (String) jsonObj.get("detection_threat_name");
						if (!StringUtil.isEmpty(strValue)) {
							// Short.parseShort(((String)
							// jsonObj.get("detection_threat_name")).getBytes().length, 0))
							byte[] data = UnsignedByteParse((short) (strValue).getBytes().length);
							for (int j = 0; j < bodyLen[i]; j++) {
								convertByte[idx++] = data[j];
							}
							data = ((String) jsonObj.get("detection_threat_name")).getBytes();
							for (int j = 0; j < data.length; j++) {
								convertByte[idx++] = data[j];
							}
						}
					}
				}
			}
		}
		/****************************************************************************************************************
		 * footer 구성
		 ****************************************************************************************************************/
		byte[] trans_speed = UnsignedShortParse(Integer.parseInt(headerParam.get("trans_speed").toString()));
		convertByte[idx++] = trans_speed[0];
		convertByte[idx++] = trans_speed[1];

		return convertByte;
	}

	public static byte[] convertToByte1(String str, Map<String, Object> headerParam)
			throws java.text.ParseException, ParseException, JSONException {
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(str);
		JSONObject jsonObj = (JSONObject) obj;

		int nMsgFormat = (int) GetMsgFormat(str);
		initJson(Short.parseShort(String.valueOf(nMsgFormat)));

		int totalLen = 0;
		@SuppressWarnings("rawtypes")
		Iterator it = jsonObject.keySet().iterator();

		while (it.hasNext()) {
			String strKey = it.next().toString();
			if ((boolean) ((JSONObject) jsonObject.get(strKey)).get("flag")) {
				if ("detection_threat_name".equals(strKey)) {
					totalLen += ((String) jsonObj.get("detection_threat_name")).getBytes().length + 1;
				} else {
					totalLen += Integer.parseInt(String.valueOf(((JSONObject) jsonObject.get(strKey)).get("len")));
				}
			}
		}

		/****************************************************************************************************************
		 * header 구성
		 ****************************************************************************************************************/
		int idx = 0; // nLen = 0;

		String strMsgid = (String) headerParam.get("msgid");

		byte[] convertByte = new byte[("2".equals(strMsgid) ? totalLen + 8 + 2 : ("1".equals(strMsgid) ? 12 : 10))]; // +2
																														// trans_speed

		byte[] msgid = UnsignedByteParse(Short.parseShort(strMsgid));
		convertByte[idx++] = msgid[0];

		byte[] unit_id = UnsignedByteParse(Short.parseShort(headerParam.get("unit_id").toString()));
		convertByte[idx++] = unit_id[0];

		byte[] ship_id = UnsignedShortParse(Integer.parseInt(headerParam.get("ship_id").toString()));
		convertByte[idx++] = ship_id[0];
		convertByte[idx++] = ship_id[1];

		/****************************************************************************************************************
		 * body 구성
		 ****************************************************************************************************************/
		if ("1".equals(strMsgid)) {
			// 헤더(6) + body(4) + trans_speed(2)
			byte[] msg_len = UnsignedShortParse(12);

			convertByte[idx++] = msg_len[0];
			convertByte[idx++] = msg_len[1];

			// body 시작
			String strValue = String.valueOf(jsonObj.get("asset_status"));
			if (!StringUtil.isEmpty(strValue)) {
				byte[] data = SignedIntParse(Integer.parseUnsignedInt(strValue));
				System.arraycopy(data, 0, convertByte, idx, data.length);
				idx += data.length;
			}
		} else if ("3".equals(strMsgid)) {
			// 헤더(6) + body(2) + trans_speed(2)
			byte[] msg_len = UnsignedShortParse(10);

			convertByte[idx++] = msg_len[0];
			convertByte[idx++] = msg_len[1];

			idx = 6;
			// body 시작
			String strValue = String.valueOf(jsonObj.get("equip_status"));
			if (!StringUtil.isEmpty(strValue)) {
				byte[] data = UnsignedByteParse(Short.parseShort(strValue));
				System.arraycopy(data, 0, convertByte, idx, data.length);
				idx += data.length;
			}

			strValue = String.valueOf(jsonObj.get("function_status"));
			log.debug("strValue:::{}", strValue);
			if (!StringUtil.isEmpty(strValue)) {
				log.debug("strValue:{}", strValue);
				byte[] data = UnsignedByteParse(Short.parseShort(strValue));
				System.arraycopy(data, 0, convertByte, idx, data.length);
				idx += data.length;
			}
		} else if ("2".equals(strMsgid)) {
			byte[] msg_len = UnsignedShortParse(totalLen + 6);

			convertByte[idx++] = msg_len[0];
			convertByte[idx++] = msg_len[1];

			// body 시작
			byte[] msg_format = UnsignedShortParse(nMsgFormat);
			convertByte[idx++] = msg_format[0];
			convertByte[idx++] = msg_format[1];
			idx = 8;

			for (int i = 0; i < jsonObject.size(); i++) {
				Map<String, JSONObject> mapJson = (Map<String, JSONObject>) getSelectJSONObject(i);
				if (mapJson != null) {
					if (i == 0) // long
					{
						SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

						Date time = (Date) formatter
								.parse((String) jsonObj.get(mapJson.keySet().toArray()[0].toString()));
						byte[] data = LongToByte(time.getTime());
						System.arraycopy(data, 0, convertByte, idx, data.length);
						idx += data.length;
					} else if (i == 1 || i == 2) // unsinged int
					{
						String strValue = (String) jsonObj.get(mapJson.keySet().toArray()[0]).toString();
						log.debug("strValue:{}", strValue);
						if (!StringUtil.isEmpty(strValue)) {
							strValue = strValue.trim();
							byte[] data = UnsignedIntParse(Integer.parseUnsignedInt(strValue) & 0xFFFFFFFFL);
							System.arraycopy(data, 0, convertByte, idx, data.length);
							idx += data.length;
						}
					} else if (i == 3 || i == 4 || i == 7 || i == 10) // unsigned short
					{
						String strValue = (String) jsonObj.get(mapJson.keySet().toArray()[0]).toString();
						log.debug("strValue:{}", strValue);
						if (!StringUtil.isEmpty(strValue)) {
							strValue = strValue.trim();
							byte[] data = UnsignedShortParse(Integer.parseInt(strValue));
							System.arraycopy(data, 0, convertByte, idx, data.length);
							idx += data.length;
						}
					} else if (i == 5 || i == 6 || i == 7 || i == 8 || i == 9) // unsigned char
					{
						String strValue = (String) jsonObj.get(mapJson.keySet().toArray()[0]).toString();
						log.debug("strValue:{}", strValue);
						if (!StringUtil.isEmpty(strValue)) {
							strValue = strValue.trim();
							byte[] data = UnsignedByteParse(Short.parseShort(strValue));
							System.arraycopy(data, 0, convertByte, idx, data.length);
							idx += data.length;
						}
					} else if (i == 11) // unsigned char 이면서 가변배열(탐지위협명)
					{
						String strValue = (String) jsonObj.get(mapJson.keySet().toArray()[0]).toString();
						log.debug("strValue:{}", strValue);
						if (!StringUtil.isEmpty(strValue)) {
							strValue = strValue.trim();
							byte[] data = UnsignedByteParse((short) (strValue).getBytes().length);
							System.arraycopy(data, 0, convertByte, idx, data.length);
							idx += data.length;
							data = ((String) jsonObj.get("detection_threat_name")).getBytes();
							System.arraycopy(data, 0, convertByte, idx, data.length);
							// idx += data.length;
						}
					}
				}
			}
		}
		/****************************************************************************************************************
		 * footer 구성 (trans_speed)
		 ****************************************************************************************************************/
		/*
		 * byte[] trans_speed =
		 * UnsignedShortParse(Integer.parseInt(headerParam.get("trans_speed").toString()
		 * ));
		 * convertByte[idx++] = trans_speed[0];
		 * convertByte[idx++] = trans_speed[1];
		 */

		return convertByte;
	}

	@SuppressWarnings("rawtypes")
	public static Map<String, JSONObject> getSelectJSONObject(int idx) {
		Map<String, JSONObject> map = new HashMap<String, JSONObject>();
		Iterator it = jsonObject.keySet().iterator();

		while (it.hasNext()) {
			String strKey = it.next().toString();
			if ((boolean) ((JSONObject) jsonObject.get(strKey)).get("flag")
					&& Integer.parseInt(String.valueOf(((JSONObject) jsonObject.get(strKey)).get("index"))) == idx) {
				map.put(strKey, (JSONObject) jsonObject.get(strKey));
				return map;
			}
		}

		return null;
	}

	// byte를 JSONObject로 변환
	@SuppressWarnings("unchecked")
	public static JSONObject convertToJson(byte[] byteArr) throws ParseException {
		JSONObject jsonObj = new JSONObject();
		byte[] msgFormat = Arrays.copyOfRange(byteArr, 6, 8);
		initJson((short) UnsignedShortParse(msgFormat, 0));

		int nMsgid = UnsignedByteParse(Arrays.copyOfRange(byteArr, 0, 1), 0);
		int idx = 0;

		int nLen = 0;

		if (nMsgid == 1) {
			idx = 6;

			byte[] bValue = Arrays.copyOfRange(byteArr, idx, idx + 4);
			jsonObj.put("asset_status", SignedIntParse(bValue));
			idx += 4;
		} else if (nMsgid == 3) {
			idx = 6;

			byte[] bValue = Arrays.copyOfRange(byteArr, idx, idx + 1);
			jsonObj.put("equip_status", UnsignedByteParse(bValue, 0));
			idx += 1;

			bValue = Arrays.copyOfRange(byteArr, idx, idx + 1);
			jsonObj.put("func_status", UnsignedByteParse(bValue, 0));
			idx += 1;
		} else if (nMsgid == 2) {
			idx = 8;

			boolean isYn = (boolean) ((JSONObject) jsonObject.get("detection_time")).get("flag");
			if (isYn) {
				nLen = Integer.parseInt(String.valueOf(((JSONObject) jsonObject.get("detection_time")).get("len")));
				byte[] bValue = Arrays.copyOfRange(byteArr, idx, idx + nLen);
				SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date dValue = new Date(LongToByte(bValue));
				jsonObj.put("detection_time", fmt.format(dValue));
			}
			idx += nLen;

			isYn = (boolean) ((JSONObject) jsonObject.get("src_ip")).get("flag");
			if (isYn) {
				nLen = Integer.parseInt(String.valueOf(((JSONObject) jsonObject.get("src_ip")).get("len")));
				byte[] bValue = Arrays.copyOfRange(byteArr, idx, idx + nLen);
				jsonObj.put("src_ip", UnsignedIntParse(bValue, 0));
			}
			idx += nLen;

			isYn = (boolean) ((JSONObject) jsonObject.get("dst_ip")).get("flag");
			if (isYn) {
				nLen = Integer.parseInt(String.valueOf(((JSONObject) jsonObject.get("dst_ip")).get("len")));
				byte[] bValue = Arrays.copyOfRange(byteArr, idx, idx + nLen);
				jsonObj.put("dst_ip", UnsignedIntParse(bValue, 0));
			}
			idx += nLen;

			isYn = (boolean) ((JSONObject) jsonObject.get("src_port")).get("flag");
			if (isYn) {
				nLen = Integer.parseInt(String.valueOf(((JSONObject) jsonObject.get("src_port")).get("len")));
				byte[] bValue = Arrays.copyOfRange(byteArr, idx, idx + nLen);
				jsonObj.put("src_port", UnsignedShortParse(bValue, 0));
			}
			idx += nLen;

			isYn = (boolean) ((JSONObject) jsonObject.get("dst_port")).get("flag");
			if (isYn) {
				nLen = Integer.parseInt(String.valueOf(((JSONObject) jsonObject.get("dst_port")).get("len")));
				byte[] bValue = Arrays.copyOfRange(byteArr, idx, idx + nLen);
				jsonObj.put("dst_port", UnsignedShortParse(bValue, 0));
			}
			idx += nLen;

			isYn = (boolean) ((JSONObject) jsonObject.get("protocol")).get("flag");
			if (isYn) {
				nLen = Integer.parseInt(String.valueOf(((JSONObject) jsonObject.get("protocol")).get("len")));
				byte[] bValue = Arrays.copyOfRange(byteArr, idx, idx + nLen);
				jsonObj.put("protocol", UnsignedByteParse(bValue, 0));
			}
			idx += nLen;

			isYn = (boolean) ((JSONObject) jsonObject.get("fragmentation")).get("flag");
			if (isYn) {
				nLen = Integer.parseInt(String.valueOf(((JSONObject) jsonObject.get("fragmentation")).get("len")));
				byte[] bValue = Arrays.copyOfRange(byteArr, idx, idx + nLen);
				jsonObj.put("fragmentation", UnsignedByteParse(bValue, 0));
			}
			idx += nLen;

			isYn = (boolean) ((JSONObject) jsonObject.get("fragment_id")).get("flag");
			if (isYn) {
				nLen = Integer.parseInt(String.valueOf(((JSONObject) jsonObject.get("fragment_id")).get("len")));
				byte[] bValue = Arrays.copyOfRange(byteArr, idx, idx + nLen);
				jsonObj.put("fragment_id", UnsignedShortParse(bValue, 0));
			}
			idx += nLen;

			isYn = (boolean) ((JSONObject) jsonObject.get("threat_detection_method")).get("flag");
			if (isYn) {
				nLen = Integer
						.parseInt(String.valueOf(((JSONObject) jsonObject.get("threat_detection_method")).get("len")));
				byte[] bValue = Arrays.copyOfRange(byteArr, idx, idx + nLen);
				jsonObj.put("threat_detection_method", UnsignedByteParse(bValue, 0));
			}
			idx += nLen;

			isYn = (boolean) ((JSONObject) jsonObject.get("threat_importance")).get("flag");
			if (isYn) {
				nLen = Integer.parseInt(String.valueOf(((JSONObject) jsonObject.get("threat_importance")).get("len")));
				byte[] bValue = Arrays.copyOfRange(byteArr, idx, idx + nLen);
				jsonObj.put("threat_importance", UnsignedByteParse(bValue, 0));
			}
			idx += nLen;

			isYn = (boolean) ((JSONObject) jsonObject.get("payload_size")).get("flag");
			if (isYn) {
				nLen = Integer.parseInt(String.valueOf(((JSONObject) jsonObject.get("payload_size")).get("len")));
				byte[] bValue = Arrays.copyOfRange(byteArr, idx, idx + nLen);
				jsonObj.put("payload_size", UnsignedShortParse(bValue, 0));
			}
			idx += nLen;

			isYn = (boolean) ((JSONObject) jsonObject.get("detection_threat_name")).get("flag");
			if (isYn) {
				nLen = Integer
						.parseInt(String.valueOf(((JSONObject) jsonObject.get("detection_threat_name")).get("len")));
				byte[] bValue = Arrays.copyOfRange(byteArr, idx, idx + nLen);
				idx += nLen;
				nLen = (int) UnsignedByteParse(bValue, 0);
				bValue = Arrays.copyOfRange(byteArr, idx, idx + nLen);
				jsonObj.put("detection_threat_name", new String(bValue));
				// idx += nLen;
			}
		}

		/****************************************************************************************************************
		 * footer 구성 (trans_speed)
		 ****************************************************************************************************************/
		/*
		 * byte[] bValue = Arrays.copyOfRange(byteArr, idx, idx+2);
		 * jsonObj.put("trans_speed", UnsignedShortParse(bValue, 0));
		 */

		return jsonObj;
	}

	public static boolean[] PrintMsgFormatBitField(short msg_format) {
		/*
		 * System.out.println("bit 0: " + ((msg_format & 0x0001) != 0));
		 * System.out.println("bit 1: " + ((msg_format & 0x0002) != 0));
		 * System.out.println("bit 2: " + ((msg_format & 0x0004) != 0));
		 * System.out.println("bit 3: " + ((msg_format & 0x0008) != 0));
		 * System.out.println("bit 4: " + ((msg_format & 0x0010) != 0));
		 * System.out.println("bit 5: " + ((msg_format & 0x0020) != 0));
		 * System.out.println("bit 6: " + ((msg_format & 0x0040) != 0));
		 * System.out.println("bit 7: " + ((msg_format & 0x0080) != 0));
		 * System.out.println("bit 8: " + ((msg_format & 0x0100) != 0));
		 * System.out.println("bit 9: " + ((msg_format & 0x0200) != 0));
		 * System.out.println("bit 10: " + ((msg_format & 0x0400) != 0));
		 * System.out.println("bit 11: " + ((msg_format & 0x0800) != 0));
		 * System.out.println("bit 12: " + ((msg_format & 0x1000) != 0));
		 * System.out.println("bit 13: " + ((msg_format & 0x2000) != 0));
		 * System.out.println("bit 14: " + ((msg_format & 0x4000) != 0));
		 * System.out.println("bit 15: " + ((msg_format & 0x8000) != 0));
		 */

		boolean[] boolArray = new boolean[12];
		boolArray[0] = ((msg_format & 0x0001) != 0);
		boolArray[1] = ((msg_format & 0x0002) != 0);
		boolArray[2] = ((msg_format & 0x0004) != 0);
		boolArray[3] = ((msg_format & 0x0008) != 0);
		boolArray[4] = ((msg_format & 0x0010) != 0);
		boolArray[5] = ((msg_format & 0x0020) != 0);
		boolArray[6] = ((msg_format & 0x0040) != 0);
		boolArray[7] = ((msg_format & 0x0080) != 0);
		boolArray[8] = ((msg_format & 0x0100) != 0);
		boolArray[9] = ((msg_format & 0x0200) != 0);
		boolArray[10] = ((msg_format & 0x0400) != 0);
		boolArray[11] = ((msg_format & 0x0800) != 0);

		return boolArray;
	}

	public static double GetMsgFormat(String str) throws ParseException {
		// double result = Math.pow(5, 2);
		double result = 0;

		JSONParser parser = new JSONParser();
		Object obj = parser.parse(str);
		JSONObject jsonObj = (JSONObject) obj;

		// msgStructure json 키의 정렬 보장 하지 않아서 기존 순서배열 사용
		for (int i = 0; i < 12; i++) {
			String strValue = (String) jsonObj.get(msgStruct[i]);
			if (!StringUtil.isEmpty(strValue)) {
				// result += Math.pow(2,(jsonObj.containsKey(msgStruct[i]) ? i : 0));
				result += Math.pow(2, i);
			}
		}
		return result;
	}
}
