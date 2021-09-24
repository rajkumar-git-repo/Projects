package com.mli.utils;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Formatter;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mli.model.PremiumCalculator;

/**
 * 
 * <p>
 * 		Class name ,Property name ,Package name, setter getter method should be same
 * 		Must be implement Serializable interface with same serial version Id
 * </p>
 *
 */
public class HmacSha256Signature {

	public static final String SECRETE_KEYS = "m|19c!@d02k*#";
	
	private static final String HMAC_SHA256_ALGORITHM = "HmacSHA256";
	
	
	private static final Logger logger = LoggerFactory.getLogger(HmacSha256Signature.class);

	
	/**
	 * Convert byte[] to hexaString
	 * 
	 */
	@SuppressWarnings("resource")
	private static String toHexString(byte[] bytes) {
		Formatter formatter = new Formatter();
		for (byte b : bytes) {
			formatter.format("%02x", b);
		}
		return formatter.toString();
	}
		
	private static String calculateRFC2104HMAC(String data, String key)
			throws SignatureException, NoSuchAlgorithmException, InvalidKeyException, IllegalStateException, IOException {
		SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), HMAC_SHA256_ALGORITHM);
		Mac mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
		mac.init(signingKey);
		return toHexString(mac.doFinal(data.getBytes()));
	}
	
	public static boolean validateCheckSum(PremiumCalculator requestData, String checksum) throws InvalidKeyException,
			SignatureException, NoSuchAlgorithmException, IllegalStateException, IOException {
		// got checksum which is coming in request
		ObjectMapper responseObjMapper = new ObjectMapper();
		requestData.setChecksum(null);
		responseObjMapper.setSerializationInclusion(Include.NON_NULL);
		// convert json string excluding checksum property
		String jsonStr = responseObjMapper.writeValueAsString(requestData);
		logger.info("Json String  : "+jsonStr);
		// genreated checksum at our end
		String hmac = calculateRFC2104HMAC(jsonStr, SECRETE_KEYS);
		logger.info("Checksum : "+hmac);
		if (hmac.equals(checksum)) {
			return true;
		} else {
			return false;
		}
	}
}
	
