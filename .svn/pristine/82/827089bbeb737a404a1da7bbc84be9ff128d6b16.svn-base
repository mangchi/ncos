package mil.ln.ncos.cmmn.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
//import java.security.SecureRandom;
import java.security.SecureRandom;
import java.util.Base64;

import javax.xml.bind.DatatypeConverter;

import mil.ln.ncos.cmmn.error.ErrorCode;
import mil.ln.ncos.exception.BizException;

public class CryptoUtil {
    private static final SecureRandom random = new SecureRandom();
	public static String encode(String planeStr, byte[] salt) {
		String encodingStr = "";
		try {

			MessageDigest md = MessageDigest.getInstance("SHA-512");
			md.update(salt);
			md.update(planeStr.getBytes(StandardCharsets.UTF_8));
			encodingStr = DatatypeConverter.printBase64Binary(md.digest());
		} catch (NoSuchAlgorithmException e) {
			throw new BizException(e.getMessage(), ErrorCode.UNDEFINED_ERROR);
		}
		return encodingStr;
	}

	public static String getSalt() throws NoSuchAlgorithmException {
		
		byte[] bytes = new byte[16];
		random.nextBytes(bytes);
		String salt = new String(Base64.getEncoder().encode(bytes));
		return salt;
	}

}
