package com.mli.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.mli.constants.MLIMessageConstants;
/**
 * 
 * @author Nikhilesh.Tiwari
 * Email validation.
 *
 */
@Component
public class EmailValidationUtil {

	private static final Logger logger = Logger.getLogger(OTPGenerateUtil.class);

	public static Boolean isEmailValidation(String stringColumn) throws Exception {
		try {
			String regex = "^([_a-zA-Z0-9-]+(\\.[_a-zA-Z0-9-]+)*@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*(\\.[a-zA-Z]{1,6}))?$";
			Pattern pattern = Pattern.compile(regex);
			Matcher matcher = pattern.matcher(stringColumn);
			if (matcher.find()) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			logger.error("::::::::::::          Getting error matching in isEmailValidation  ::::::::::::::::::");
			throw new Exception(MLIMessageConstants.EMAIL_VALIDATION_EXCEPTION);
		}
	}

}
