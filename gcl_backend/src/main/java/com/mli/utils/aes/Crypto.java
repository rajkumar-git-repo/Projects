package com.mli.utils.aes;

import java.security.MessageDigest;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;

public class Crypto {
	@Autowired
	private static AESService aesService;
	private static SecretKeySpec generateSecretKey(String password) throws Exception {
		MessageDigest shahash = MessageDigest.getInstance("SHA-1");
		byte[] key = shahash.digest(password.getBytes());
		key = Arrays.copyOf(key, 16);
		return new SecretKeySpec(key, "AES");
	}

	public static String encrypt(String text, String password) throws Exception {
		SecretKeySpec secretkey = generateSecretKey(password);
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.ENCRYPT_MODE, secretkey);
		return Base64.encodeBase64String(cipher.doFinal(text.getBytes()));
	}

	public static String decrypt(String encryptedtext, String password) throws Exception {
		SecretKeySpec secretkey = generateSecretKey(password);
		Cipher cipher = Cipher.getInstance("AES");
		cipher.init(Cipher.DECRYPT_MODE, secretkey);
		return new String(cipher.doFinal(Base64.decodeBase64(encryptedtext)), "UTF-8");
	}

	public static void main(String ag[]) {
		try {
			Main();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void Main() throws Exception {

		String text = "U2FsdGVkX18t1bFL4yiWP2L9Ptb6bcVvgsKyz3VyFSyrlofNDhr8GFJLD8EAcQTCMCx87Z2rHyWIFpwGdTyi4Mo+AlXlFUG44eMm2+k2zPW8SQViioHEysY1uTqCSIXs7eHXZTTj7HPaSrYXokPd3Tl581qYavGzMDjwxV/mG1Z8dal/cBNg+myxAMbzN1AWBP4y+9jh8ztTBWie9eGaRQO45Rl+RKCIhoqfXwN3pI5EiC9yjbDR3EBn1SQo0yhvoVnkm/9FRYJcTLIwJuAzWCdNLKEAqlY3PhvN9pXD3KJgKfYeeok0ayybnbWJu1S1f5+HYp4/xK3OZtyVRlT98wa2SLXB/A7jaUhZD/wn//b/Tjyl1VIeDDaQOxT0V+j4V9vFM9+WKR8q4HRWLSSyWxLvMsce8P4RKT9sZgTpGOomtz8KAbyd59QasMZADYvkHkgafX1sSV8cpwT7tGmAKHuqAvxFH/togdoHaHi0dZ/JcFvyLHOaCyfAaE2jKhvzOCE2JNkzvD71LGSmxU0dujzMkvEr73jBya1dcJKzzY4C+jyq8y0atskyCkHb2DreaJI7P2XoAfEwSUR1771JU0nkf13g7AjolZiL5ReedlN83spuL+UXgl4HIYTIx2FbClXhLvEfVfkYbkT8IUq9gphRNQAljI8MvU0eoTFILrJX9uM+QnzFjfTLuw+74xfntSDTyTtPAP3xgB23p/BsvtzL0Oj4HYkvv9/v/Y5/8A6dFwSPlqyNUicQ9ORXO7H8";
		System.out.println(aesService.decryptData(text));
		//System.out.println("Original text before encryption: " + text);
		
		//String str = aesService.decryptData(text);
	//	System.out.println(str);

		// User A verschlüsselt und speichert ab
		/*
		 * Crypto crypto = new Crypto(); String encryptedtext = crypto.encrypt(text,
		 * "encryptionkeyforSHAAES256"); System.out.println("encryption1=" +
		 * encryptedtext);
		 */

		// User B lädt Datei und kennt das Passwort
		//Crypto crypto2 = new Crypto();
		//String decryptedtext = crypto2.decrypt(text, "encryptionkeyforSHAAES256");
		//System.out.println("Original text after encryption: " + decryptedtext);

	}
}
