package mil.ln.ncos.cmmn.util;

import com.penta.scpdb.ScpDbAgent;
import com.penta.scpdb.ScpDbAgentException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ScpDbUtil {
	static final String SCP_INI_FILE_PATH = "/home/user/scp/scpdb_agent.ini";
	//static final String SCP_INI_FILE_PATH = "C:\\WISELY\\ba_scp\\scpdb_agent.ini";
    /*
	public static void main(String[] args) {
		try {

			String strEnc = "";
			String strDec = "";

			int ret;

			int inputNumber = -2147483648;
			int outputNumber;

			//DAMO SCP : config file full path
			String iniFilePath = "/home/scp_4.0.202.4/scpdb_agent.ini"; // scpdb_agent.ini fullpath

			// DAMO SCP : Create ScpDbAgent object
			ScpDbAgent agt = new ScpDbAgent();

			strEnc = agt.ScpEncB64(iniFilePath, "KEY2", Integer.toString(inputNumber));
			System.out.println("[java] ScpEncB64 : " + strEnc);
			strDec = agt.ScpDecB64(iniFilePath, "KEY2", strEnc);
			System.out.println("[java] ScpDecB64 : " + strDec);
			outputNumber = Integer.parseInt(strDec);
			System.out.println("[java] ScpDecB64(Convert String to Int) : " + outputNumber);

		} catch (ScpDbAgentException e1) {
			System.out.println(e1.toString());
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    */
	public static String scpEnc(String str,String key) {
		if(str == null || "".equals(str.trim())) {
			return str;
		}
		//KEY1
		String strEnc = "";
		try {

			ScpDbAgent agt = new ScpDbAgent();
			strEnc = agt.ScpEncB64(SCP_INI_FILE_PATH, key, str);
			//strEnc = agt.ScpEncB64(SCP_INI_FILE_PATH, "KEY1", str);
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
		if(str == null || "".equals(str.trim())) {
			return "";
		}
		String strDec = "";
		try {

			ScpDbAgent agt = new ScpDbAgent();
			strDec = agt.ScpDecB64(SCP_INI_FILE_PATH, key, str);
			//strDec = agt.ScpDecB64(SCP_INI_FILE_PATH, "KEY1", str);
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
		if(str == null || "".equals(str.trim())) {
			return "";
		}
		String hash = "";
		try {

			ScpDbAgent agt = new ScpDbAgent();
			agt.AgentInit(SCP_INI_FILE_PATH);
			hash = agt.AgentCipherHashStringB64(71, str);//70(SHA1), 71(SHA256), 72(SHA384), 73(SHA512), 74(HAS160), 75(MD5)
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
