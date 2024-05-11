package mil.ln.ncos;

import org.assertj.core.api.Assertions;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;

import mil.ln.ncos.config.JasyptConfig;

//@SpringBootTest
public class JasyptConfigTest extends JasyptConfig {
	
	@Test
    public void jasypt(){
		String a = "2023-10-25 17:19:46";
		System.out.println(a.substring(0,10));
	}
		/*
		System.out.println("jasypt test...............");
        String url = "jdbc:mariadb://lnsystem.iptime.org:4036/ncos_ship";
        String encUrl = "0KNEv4jLgVL9SfXm0Wvy3zI316OKE7m1hF1UdEaSpaMRPTxiYgsIaVh7+mDJ3hZ2";
        String username = "lnsystem";
        String password = "lnsystem420";

        String encryptUrl = jasyptEncrypt(url);
        String decrypttUrl = jasyptDecryt(encUrl);
        String encryptUsername = jasyptEncrypt(username);
        String encryptPassword = jasyptEncrypt(password);

        System.out.println("encryptUrl : " + encryptUrl);
        System.out.println("decrypttUrl : " + decrypttUrl);
        System.out.println("encryptUsername : " + encryptUsername);
        System.out.println("encryptPassword :" + encryptPassword);

        Assertions.assertThat(url).isEqualTo(jasyptDecryt(encryptUrl));
        
        
        System.out.println("decryptUrl : " + jasyptDecryt(encryptUrl));
        
		Integer a = 200;
		Integer b = 200;
		System.out.println(a.hashCode());
		System.out.print(a.hashCode() == b.hashCode()); // true
    }
    */

    private String jasyptEncrypt(String input) {
        String key = "1245";
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setAlgorithm("PBEWithMD5AndDES");
        encryptor.setPassword(key);
        return encryptor.encrypt(input);
    }

    private String jasyptDecryt(String input){
        String key = "1245";
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        encryptor.setAlgorithm("PBEWithMD5AndDES");
        encryptor.setPassword(key);
        return encryptor.decrypt(input);
    }
}