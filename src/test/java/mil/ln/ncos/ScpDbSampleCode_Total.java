package mil.ln.ncos;

import org.junit.Test;

/******** DAMO API **********/
import com.penta.scpdb.*;

/******** DAMO API **********/

public class ScpDbSampleCode_Total {

  public String AgentCipherHashStringB64(String str) {
    if (str == null || "".equals(str.trim())) {
      return str;
    }
    String hash = "";
    try {

      ScpDbAgent agt = new ScpDbAgent();
      String iniFilePath = "C:\\WISELY\\ba_scp\\scpdb_agent.ini"; /* scpdb_agent.ini fullpath */
      agt.AgentInit(iniFilePath);
      hash = agt.AgentCipherHashStringB64(71, str);// 70(SHA1), 71(SHA256), 72(SHA384), 73(SHA512), 74(HAS160), 75(MD5)
      return hash;
    } catch (ScpDbAgentException e1) {
      System.out.println(e1.toString());
      e1.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return hash;
  }

  @Test
  public void main(String[] args) {
    try {
      byte[] byteInput = { (byte) 0xED, (byte) 0x99, (byte) 0x8D, (byte) 0xEA, (byte) 0xB8, (byte) 0xB8, (byte) 0xEB,
          (byte) 0x8F, (byte) 0x99, 0x2D, 0x31, 0x32, 0x33, 0x34, 0x35, 0x36, 0x37 };
      String strInputPlain = new String(byteInput, "UTF-8"); /* Hong Gil Dong ( U+D640 U+AE38 U+B3D9 ) "-1234567" */

      String strEnc = "";
      String strDec = "";

      int ret;

      /* DAMO SCP : config file full path */
      String iniFilePath = "C:\\WISELY\\ba_scp\\scpdb_agent.ini"; /* scpdb_agent.ini fullpath */

      System.out.println("JAVA CLASS PATH : " + System.getProperty("java.class.path"));
      System.out.println("JAVA LIBRARY PATH : " + System.getProperty("java.library.path"));

      /* DAMO SCP : Create ScpDbAgent object */
      ScpDbAgent agt = new ScpDbAgent();

      /*****************************************************************************/

      strEnc = agt.ScpEncStr(iniFilePath, "KEY1", strInputPlain);
      System.out.println("[java] ScpEncStr : " + strEnc);
      strDec = agt.ScpDecStr(iniFilePath, "KEY1", strEnc);
      System.out.println("[java] ScpDecStr : " + strDec);
      strEnc = agt.ScpEncB64(iniFilePath, "KEY1", strInputPlain);
      System.out.println("[java] ScpEncB64 : " + strEnc);
      strDec = agt.ScpDecB64(iniFilePath, "KEY1", strEnc);
      System.out.println("[java] ScpDecB64 : " + strDec);
      /*
       * strEnc = agt.ScpEncRRNB64( iniFilePath, "KEY1", strInputPlain );
       * System.out.println("[java] ScpEncRRNB64 : " + strEnc);
       * strDec = agt.ScpDecB64( iniFilePath, "KEY1", strEnc);
       * System.out.println("[java] ScpDecB64 : " + strDec);
       */

      /*
       * DAMO SCP API
       * HASH function
       * HASH Algorithm ID :
       * SHA1 = 70
       * SHA256 = 71
       * SHA384 = 72
       * SHA512 = 73
       * HAS160 = 74
       * MD5 = 75
       */

      strEnc = agt.ScpHashStr(iniFilePath, 71, strInputPlain);
      System.out.println("[java] ScpHashStr String : " + strEnc);
      strEnc = agt.ScpHashB64(iniFilePath, 71, strInputPlain);
      System.out.println("[java] ScpHashB64 String : " + strEnc);

      strEnc = agt.ScpKeyHashStr(iniFilePath, "KEY2", strInputPlain);
      System.out.println("[java] ScpKeyHashStr String : " + strEnc);
      strEnc = agt.ScpKeyHashB64(iniFilePath, "KEY2", strInputPlain);
      System.out.println("[java] ScpKeyHashB64 String : " + strEnc);

      /*****************************************************************************/

      strEnc = agt.ScpEncStr(iniFilePath, "KEY1", strInputPlain, "EUC-KR");
      System.out.println("[java] ScpEncStr EUC-KR : " + strEnc);
      strDec = agt.ScpDecStr(iniFilePath, "KEY1", strEnc, "EUC-KR");
      System.out.println("[java] ScpDecStr EUC-KR : " + strDec);
      strEnc = agt.ScpEncB64(iniFilePath, "KEY1", strInputPlain, "EUC-KR");
      System.out.println("[java] ScpEncB64 EUC-KR : " + strEnc);
      strDec = agt.ScpDecB64(iniFilePath, "KEY1", strEnc, "EUC-KR");
      System.out.println("[java] ScpDecB64 EUC-KR : " + strDec);
      /*
       * strEnc = agt.ScpEncRRNB64( iniFilePath, "KEY1", strInputPlain, "EUC-KR");
       * System.out.println("[java] ScpEncRRNB64 EUC-KR : " + strEnc);
       * strDec = agt.ScpDecB64( iniFilePath, "KEY1", strEnc, "EUC-KR");
       * System.out.println("[java] ScpDecB64 EUC-KR : " + strDec);
       */

      strEnc = agt.ScpHashStr(iniFilePath, 71, strInputPlain, "EUC-KR");
      System.out.println("[java] ScpHashStr EUC-KR : " + strEnc);
      strEnc = agt.ScpHashB64(iniFilePath, 71, strInputPlain, "EUC-KR");
      System.out.println("[java] ScpHashB64 EUC-KR : " + strEnc);

      strEnc = agt.ScpKeyHashStr(iniFilePath, "KEY2", strInputPlain, "EUC-KR");
      System.out.println("[java] ScpKeyHashStr EUC-KR : " + strEnc);
      strEnc = agt.ScpKeyHashB64(iniFilePath, "KEY2", strInputPlain, "EUC-KR");
      System.out.println("[java] ScpKeyHashB64 EUC-KR : " + strEnc);

      /*****************************************************************************/

      strEnc = agt.ScpEncStr(iniFilePath, "KEY1", strInputPlain, "UTF-8");
      System.out.println("[java] ScpEncStr UTF-8 : " + strEnc);
      strDec = agt.ScpDecStr(iniFilePath, "KEY1", strEnc, "UTF-8");
      System.out.println("[java] ScpDecStr UTF-8 : " + strDec);
      strEnc = agt.ScpEncB64(iniFilePath, "KEY1", strInputPlain, "UTF-8");
      System.out.println("[java] ScpEncB64 UTF-8 : " + strEnc);
      strDec = agt.ScpDecB64(iniFilePath, "KEY1", strEnc, "UTF-8");
      System.out.println("[java] ScpDecB64 UTF-8 : " + strDec);
      /*
       * strEnc = agt.ScpEncRRNB64( iniFilePath, "KEY1", strInputPlain, "UTF-8");
       * System.out.println("[java] ScpEncRRNB64 UTF-8 : " + strEnc);
       * strDec = agt.ScpDecB64( iniFilePath, "KEY1", strEnc, "UTF-8");
       * System.out.println("[java] ScpDecB64 UTF-8 : " + strDec);
       */

      strEnc = agt.ScpHashStr(iniFilePath, 71, strInputPlain, "UTF-8");
      System.out.println("[java] ScpHashStr UTF-8 : " + strEnc);
      strEnc = agt.ScpHashB64(iniFilePath, 71, strInputPlain, "UTF-8");
      System.out.println("[java] ScpHashB64 UTF-8 : " + strEnc);

      strEnc = agt.ScpKeyHashStr(iniFilePath, "KEY2", strInputPlain, "UTF-8");
      System.out.println("[java] ScpKeyHashStr UTF-8 : " + strEnc);
      strEnc = agt.ScpKeyHashB64(iniFilePath, "KEY2", strInputPlain, "UTF-8");
      System.out.println("[java] ScpKeyHashB64 UTF-8 : " + strEnc);

      /*****************************************************************************/

      byte[] enc = null;
      byte[] dec = null;
      enc = agt.ScpEncStr(iniFilePath, "KEY1", strInputPlain.getBytes("UTF-8"));
      strEnc = new String(enc);
      System.out.println("[Java] ScpEncStr Byte UTF-8 : " + strEnc);
      dec = agt.ScpDecStr(iniFilePath, "KEY1", enc);
      strDec = new String(dec, "UTF-8");
      System.out.println("[Java] ScpDecStr Byte UTF-8 : " + strDec);
      enc = agt.ScpEncB64(iniFilePath, "KEY1", strInputPlain.getBytes("UTF-8"));
      strEnc = new String(enc);
      System.out.println("[Java] ScpEncB64 Byte UTF-8 : " + strEnc);
      dec = agt.ScpDecB64(iniFilePath, "KEY1", enc);
      strDec = new String(dec, "UTF-8");
      System.out.println("[Java] ScpDecB64 Byte UTF-8 : " + strDec);

      enc = agt.ScpHashStr(iniFilePath, 71, strInputPlain.getBytes("UTF-8"));
      strEnc = new String(enc);
      System.out.println("[Java] ScpHashStr Byte UTF-8 : " + strEnc);
      enc = agt.ScpHashB64(iniFilePath, 71, strInputPlain.getBytes("UTF-8"));
      strEnc = new String(enc);
      System.out.println("[Java] ScpHashB64 Byte UTF-8 : " + strEnc);

      enc = agt.ScpKeyHashStr(iniFilePath, "KEY2", strInputPlain.getBytes("UTF-8"));
      strEnc = new String(enc);
      System.out.println("[Java] ScpKeyHashStr Byte UTF-8 : " + strEnc);
      enc = agt.ScpKeyHashB64(iniFilePath, "KEY2", strInputPlain.getBytes("UTF-8"));
      strEnc = new String(enc);
      System.out.println("[Java] ScpKeyHashB64 Byte UTF-8 : " + strEnc);

      /*****************************************************************************/

      enc = agt.ScpEncStr(iniFilePath, "KEY1", strInputPlain.getBytes("MS949"));
      strEnc = new String(enc);
      System.out.println("[Java] ScpEncStr Byte MS949 : " + strEnc);
      dec = agt.ScpDecStr(iniFilePath, "KEY1", enc);
      strDec = new String(dec, "MS949");
      System.out.println("[Java] ScpDecStr Byte MS949 : " + strDec);
      enc = agt.ScpEncB64(iniFilePath, "KEY1", strInputPlain.getBytes("MS949"));
      strEnc = new String(enc);
      System.out.println("[Java] ScpEncB64 Byte MS949 : " + strEnc);
      dec = agt.ScpDecB64(iniFilePath, "KEY1", enc);
      strDec = new String(dec, "MS949");
      System.out.println("[Java] ScpDecB64 Byte MS949 : " + strDec);

      enc = agt.ScpHashStr(iniFilePath, 71, strInputPlain.getBytes("MS949"));
      strEnc = new String(enc);
      System.out.println("[Java] ScpHashStr Byte MS949 : " + strEnc);
      enc = agt.ScpHashB64(iniFilePath, 71, strInputPlain.getBytes("MS949"));
      strEnc = new String(enc);
      System.out.println("[Java] ScpHashB64 Byte MS949 : " + strEnc);

      enc = agt.ScpKeyHashStr(iniFilePath, "KEY2", strInputPlain.getBytes("MS949"));
      strEnc = new String(enc);
      System.out.println("[Java] ScpKeyHashStr Byte MS949 : " + strEnc);
      enc = agt.ScpKeyHashB64(iniFilePath, "KEY2", strInputPlain.getBytes("MS949"));
      strEnc = new String(enc);
      System.out.println("[Java] ScpKeyHashB64 Byte MS949 : " + strEnc);

      /*****************************************************************************/

      ret = agt.ScpEncFile(iniFilePath, "KEY1", "scp_test.txt", "scp_test.enc");
      System.out.println("[java] ScpEncFile : " + ret);
      ret = agt.ScpDecFile(iniFilePath, "KEY1", "scp_test.enc", "scp_test.dec");
      System.out.println("[java] ScpDecFile : " + ret);

    } catch (ScpDbAgentException e1) {
      System.out.println(e1.toString());
      e1.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
