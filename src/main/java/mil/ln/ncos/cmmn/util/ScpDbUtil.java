package mil.ln.ncos.cmmn.util;

import com.penta.scpdb.ScpDbAgent;
import com.penta.scpdb.ScpDbAgentException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ScpDbUtil {
	static final String SCP_INI_FILE_PATH = "/home/user/scp/scpdb_agent.ini";

	public static String scpEnc(String str,String key) {
		String strEnc = "";
		if(str == null || "".equals(str.trim()) || "null".equals(str.trim())) {
			return strEnc;
		}
		String inputStr = new String(str);
		//KEY1
		
		try {
			ScpDbAgent agt = new ScpDbAgent();
			strEnc = agt.ScpEncB64(SCP_INI_FILE_PATH, key, inputStr);
			return strEnc;
		} catch (ScpDbAgentException e1) {
			log.error(e1.toString());
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strEnc;

	}

	public static String scpDec(String str,String key){
		String strDec = "";
		if(str == null || "".equals(str.trim()) || "null".equals(str.trim())) {
			return strDec;
		}
		String inputStr = new String(str);
		try {
			ScpDbAgent agt = new ScpDbAgent();
			strDec = agt.ScpDecB64(SCP_INI_FILE_PATH, key, inputStr);
			return strDec;
		} catch (ScpDbAgentException e1) {
			log.error(e1.toString());
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return strDec;
	}

	public static String AgentCipherHashStringB64(String str){
		String hash = "";
		if(str == null || "".equals(str.trim())) {
			return hash;
		}
		
		String inputStr = new String(str);
		try {
			ScpDbAgent agt = new ScpDbAgent();
			agt.AgentInit(SCP_INI_FILE_PATH);
			hash = agt.AgentCipherHashStringB64(71, inputStr);//70(SHA1), 71(SHA256), 72(SHA384), 73(SHA512), 74(HAS160), 75(MD5)
			return hash;
		} catch (ScpDbAgentException e1) {
			log.error(e1.toString());
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return hash;
	}


}
