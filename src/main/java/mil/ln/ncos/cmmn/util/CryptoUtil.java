package mil.ln.ncos.cmmn.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
//import java.security.SecureRandom;

import javax.xml.bind.DatatypeConverter;

import mil.ln.ncos.cmmn.error.ErrorCode;
import mil.ln.ncos.exception.BizException;

public class CryptoUtil {

	public static String encode(String planeStr) {
		String encodingStr = "";
		try {
			/*
			SecureRandom random = new SecureRandom();
			byte[] salt = new byte[16];
			random.nextBytes(salt);
			*/
			MessageDigest md = MessageDigest.getInstance("SHA-512");
			md.update(planeStr.getBytes(StandardCharsets.UTF_8));
			encodingStr = DatatypeConverter.printBase64Binary(md.digest());
		} catch (NoSuchAlgorithmException e) {
			throw new BizException(e.getMessage(), ErrorCode.UNDEFINED_ERROR);
		}
		return encodingStr;
	}

	/*
	 * public static void main(String args[]) {
	 * System.out.println(encode("ncos!1245")); }
	 */

}
