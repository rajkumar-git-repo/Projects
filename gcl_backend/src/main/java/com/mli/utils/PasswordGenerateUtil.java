package com.mli.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.util.StringUtils;

import com.mli.constants.Constant;
import com.mli.model.UserDetailsModel;
/**
 * 
 * @author Nikhilesh.Tiwari
 * Password pattern and logic for PDF and Excel file. 
 *
 */
public class PasswordGenerateUtil {

	private static final Logger logger = Logger.getLogger(PasswordGenerateUtil.class);

	/**
	 * 
	 * @param userDetailsModel
	 * Password for PDF file.
	 * Pattern: <Customer FirstName in CAPS><Date of birth in ddmm>
	 */
	public static String getPDFPassword(UserDetailsModel userDetailsModel) {
		try {
			String password = null;
			String custName = null;
			Integer custNameLength = userDetailsModel.getCustomerDetails().getCustomerFirstName().length();
			if (custNameLength >= 3) {
				custName = userDetailsModel.getCustomerDetails().getCustomerFirstName().substring(0, 3).toUpperCase();
			} else {
				custName = userDetailsModel.getCustomerDetails().getCustomerFirstName().toUpperCase();
			}

			String[] dob = userDetailsModel.getCustomerDetails().getDob().split("/");
			password = custName + dob[0] + dob[1];
			return password;
		} catch (Exception exception) {
			logger.error(exception.getMessage(), exception);
			throw exception;
		}
	}

	/**
	 * 
	 * @param label
	 * Password logic for Excel file.
	 * Pattern: <Bank name in CAPS><Today date in ddmmyyyy>
	 */
	public static String getExcelPassword(String label) {
		try {
			if(!ObjectsUtil.isNull(label)){
				String password = null;
				password = label.toUpperCase() + DateUtil.todayDateDDMMYYYY();
				logger.info("password : "+password);
				return password;
			}else{
				logger.error(":::::::::::::: Master Policy Holder name not getting during excel password  ::::::::::::::::::::::::");
				return null;
			}
		} catch (Exception exception) {
			logger.error(exception.getMessage(), exception);
			throw exception;
		}
	}
	
	/**
	 * 
	 * @param password
	 * @return
	 * @author rajkumar
	 */
	public static boolean getMatchedPassword(String password) {
		try {
			if(!StringUtils.isEmpty(password)) {
				Pattern p = Pattern.compile(Constant.PASSWORD_REGEX);
				Matcher m = p.matcher(password);  
				return m.matches();  
			}else {
				return false;
			}
		}catch (Exception exception) {
			logger.error(exception.getMessage(), exception);
			throw exception;
		}
	}
}
