package com.mli.utils;

import java.util.Random;

import org.apache.log4j.Logger;

/**
 * @author Nikhilesh.Tiwari
 *
 */
public class OTPGenerateUtil {

	private static final Logger logger = Logger.getLogger(OTPGenerateUtil.class);

	public static String generateOTPCode(final Integer noOfRandomChars) throws Exception {
		try {
			final char[] chars = "0123456789".toCharArray();
			final Random random = new Random();
			StringBuilder randomOTP = new StringBuilder();
			for (int i = 0; i < noOfRandomChars; i++) {
				randomOTP.append(chars[random.nextInt(chars.length)]);
			}
			return randomOTP.toString();
		} catch (final Exception exception) {
			logger.error(exception.getMessage(), exception);
			throw exception;
		}
	}
}
