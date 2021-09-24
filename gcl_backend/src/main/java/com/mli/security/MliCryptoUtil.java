package com.mli.security;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Component;

/**
 * 
 * @author Haripal.Chauhan
 * 
 * Encryption and Decryption utility method.
 *
 */
@Component
public class MliCryptoUtil {

	private static final String ALGO = "AES";
	private static final byte[] keyValue = 
			new byte[] { 'T', 'h', 'e', 'B', 'e', 's', 't',
					'S', 'e', 'c', 'r','e', 't', 'K', 'e', 'y' };



	private static String ENCRYPTIONALGO = "AES";

	@SuppressWarnings("unused")
	private static SecretKey generateSecretKey() throws NoSuchAlgorithmException {
		KeyGenerator keyGenerator = KeyGenerator.getInstance(ENCRYPTIONALGO);
		keyGenerator.init(128);
		SecretKey secretKey = keyGenerator.generateKey();
		return secretKey;
	}

	@SuppressWarnings("unused")
	private static String KeyToString(SecretKey sk) {
		String keyStr = Base64.getEncoder().encodeToString(sk.getEncoded());
		return keyStr;
	}

	private static SecretKey StringToKey(String sk) {
		byte[] decodedKey = Base64.getDecoder().decode(sk);
		SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, ENCRYPTIONALGO);
		return originalKey;
	}

	public static String generateSecureToken(String userInfo, String claimSecret) {
		String token = userInfo;
		/*
		 * try { Cipher cipher = Cipher.getInstance(ENCRYPTIONALGO);
		 * cipher.init(Cipher.ENCRYPT_MODE, StringToKey(claimSecret)); byte[] uByte =
		 * userInfo.getBytes(); byte[] encryptedByte = cipher.doFinal(uByte); Base64.Encoder encoder
		 * = Base64.getEncoder(); token = encoder.encodeToString(encryptedByte); } catch
		 * (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException |
		 * IllegalBlockSizeException | BadPaddingException e) { e.printStackTrace(); }
		 */

		return token;
	}

	public static String parseSecureToken(String token, String claimSecret) {
		String valueFromToken = token;
		/*
		 * try { Base64.Decoder decoder = Base64.getDecoder(); byte[] tokenByte =
		 * decoder.decode(token); byte[] encoded = StringToKey(claimSecret).getEncoded(); SecretKey
		 * sk = new SecretKeySpec(encoded, ENCRYPTIONALGO);
		 * 
		 * Cipher cipher = Cipher.getInstance(ENCRYPTIONALGO); cipher.init(Cipher.DECRYPT_MODE, sk);
		 * byte[] decryptedByte = cipher.doFinal(tokenByte); valueFromToken = new
		 * String(decryptedByte); } catch (NoSuchAlgorithmException | NoSuchPaddingException |
		 * InvalidKeyException | IllegalBlockSizeException | BadPaddingException |
		 * IllegalArgumentException e) { throw new IllegalArgumentException(
		 * "Please provide a valid info."); }
		 */

		return valueFromToken;
	}
	public static void testEncoding(){

		try {
			String password = "mypassword";
			String passwordEnc = encrypt(password);
			String passwordDec = decrypt(passwordEnc);

			System.out.println("Plain Text : " + password);
			System.out.println("Encrypted Text : " + passwordEnc);
			System.out.println("Decrypted Text : " + passwordDec);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}






	public static String encrypt(String Data) throws Exception {
		Key key = generateKey();
		Cipher c = Cipher.getInstance(ALGO);
		c.init(Cipher.ENCRYPT_MODE, key);
		byte[] encVal = c.doFinal(Data.getBytes());
		Base64.Encoder encoder = Base64.getEncoder();         
		String encryptedValue =  encoder.encodeToString(encVal);
		return encryptedValue;
	}

	public static String decrypt(String encryptedData) throws Exception {
		Key key = generateKey();
		Cipher c = Cipher.getInstance(ALGO);
		c.init(Cipher.DECRYPT_MODE, key);
		Base64.Decoder decoder = Base64.getDecoder();  
		byte[] decordedValue = decoder.decode(encryptedData);
		byte[] decValue = c.doFinal(decordedValue);
		String decryptedValue = new String(decValue);
		return decryptedValue;
	}
	private static Key generateKey() throws Exception {
		Key key = new SecretKeySpec(keyValue, ALGO);
		return key;
	}

}
