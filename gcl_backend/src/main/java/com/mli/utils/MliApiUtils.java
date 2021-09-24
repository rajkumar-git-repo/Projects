package com.mli.utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.google.gson.Gson;
import com.mli.entity.OTPHistoryEntity;
import com.mli.helper.OTPHelper;
import com.mli.modal.email.MliEmailServiceModel;
import com.mli.model.otp.MLIGenerateOTPResponseModel;
import com.mli.model.otp.MLIValidateResponseModel;
import com.mli.model.otp.OTPModel;
import com.mli.model.sms.SMSMliSmsServiceModel;
import com.mli.model.sms.SMSResponseModel;
/**
 * 
 * @author Nikhilesh.Tiwari
 *  
 * OTP generation/verification and email util.
 * API are provided by MLI(third party) that is being call for OTP generation/verification and email sending.
 *
 */
@Component
public class MliApiUtils {

	@Value("${mli.otpGenerateApiPath}")
	private  String otpGenerateApiPath;

	@Value("${mli.otpVerificationApiPath}")
	private  String otpVerificationApiPath;

	@Value("${mli.emailApiPath}")
	private  String emailApiPath;

	@Value("${mli.otpSmsApiPath}")
	private  String otpSmsApiPath;
	
	@Autowired
	private OTPHelper otpHelper;

	private static final Logger logger = Logger.getLogger(MliApiUtils.class);
	private static final RestTemplate restTemplate = new RestTemplate();

	private static String contentType = "application/json";
    /**
     * 
     * @return
     * @throws RuntimeException
     * @throws JsonParseException
     * @throws JsonMappingException
     * @throws IOException
     * OTP generation.
     */
	public  MLIGenerateOTPResponseModel callOTPGenerationAPI()
			throws RuntimeException, JsonParseException, JsonMappingException, IOException {
		logger.info(":::::::::: call MLI OTP Generation API:::::::::::");

		MLIGenerateOTPResponseModel mliotpResponseModel = null;

		OTPModel otpModel = new OTPModel();
		otpModel = otpHelper.getGenerateOTPRequestModel();

		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", "application/json");

		HttpEntity<OTPModel> entity = new HttpEntity<OTPModel>(otpModel, headers);

		try {
			ResponseEntity<String> response = restTemplate.exchange(otpGenerateApiPath, HttpMethod.POST, entity,
					String.class);
			Gson gson = new Gson();
			mliotpResponseModel = gson.fromJson(response.getBody(), MLIGenerateOTPResponseModel.class);
		} catch (HttpStatusCodeException e1) {
			logger.error(":::::::::::::::::: call MLI OTP Generation API :::::::::::::::");
			logger.error(":::::::::::OTP Generation API Response::::::"+e1.getResponseBodyAsString());
			e1.printStackTrace();
		}
		return mliotpResponseModel;
	}

 /**
  * 
  * @param otpDetails
  * @return
  * @throws RuntimeException
  * OTP verification.
  */
	public  MLIValidateResponseModel callOTPVerificationAPI(OTPHistoryEntity otpDetails) throws RuntimeException {
		logger.info(":::::::::: call MLI OTP Verification API :::::::::::");

		MLIValidateResponseModel mliValidateResponseModel = new MLIValidateResponseModel();

		OTPModel otpModel = new OTPModel();
		otpModel = otpHelper.getValidateRequestData(otpDetails);

		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", contentType);

		HttpEntity<OTPModel> entity = new HttpEntity<OTPModel>(otpModel, headers);

		try {
			ResponseEntity<String> response = restTemplate.exchange(otpVerificationApiPath, HttpMethod.POST, entity,
					String.class);
			Gson gson = new Gson();
			mliValidateResponseModel = gson.fromJson(response.getBody(), MLIValidateResponseModel.class);
		} catch (HttpStatusCodeException e1) {
			logger.error(":::::::::: Error MLI OTP Verification API :::::::::::");
			logger.error(":::::::::::OTP Verification API Response::::::"+e1.getResponseBodyAsString());
			e1.printStackTrace();
		}
		return mliValidateResponseModel;
	}

	public  ResponseEntity<String> callAPIPost(MliEmailServiceModel MliEmailService) throws Exception {
		logger.info("::::::::::   call MLI API For Email  :::::::::::");
		logger.info("::::::::::   Mail Api Service Data   :::::::::::"+MliEmailService);
		Map<String, MliEmailServiceModel> map = new HashMap<>();
		map.put("MliEmailService", MliEmailService);
		
		logger.info("::::::::::  Map Data On Request and Response   :::::::::::"+map);
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.set("Content-Type", contentType);
			HttpEntity<Object> entity = new HttpEntity<Object>(map, headers);
			ResponseEntity<String> response = restTemplate.exchange(emailApiPath, HttpMethod.POST, entity,
					String.class);
			return response;
		} catch (HttpStatusCodeException e1) {
			logger.error(":::::::::: Error MLI API For Email :::::::::::");
			logger.error("e1.getResponseBodyAsString() : "+e1.getResponseBodyAsString());
			e1.printStackTrace();
			throw new Exception();
		}

	}
  /**
   * 
   * @param smsMliSmsServiceModel
   * @return
   * @throws Exception
   * Send an email by calling MLI email API.
   */
	public  SMSResponseModel callAPISmsPost(SMSMliSmsServiceModel smsMliSmsServiceModel) throws Exception {
		logger.info("::::::::::   call MLI API Sms For SMS  :::::::::::");
		SMSResponseModel smsResponse = new SMSResponseModel();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", contentType);
		Map<String, SMSMliSmsServiceModel> map = new HashMap<>();
		map.put("MliSmsService", smsMliSmsServiceModel);

		HttpEntity<Object> entity = new HttpEntity<Object>(map, headers);

		try {
			ResponseEntity<String> response = restTemplate.exchange(otpSmsApiPath, HttpMethod.POST, entity,
					String.class);
			Gson gson = new Gson();
			smsResponse = gson.fromJson(response.getBody(), SMSResponseModel.class);
			return smsResponse;
		} catch (HttpStatusCodeException e1) {
			logger.error(":::::::::: call MLI API Sms For SMS  :::::::::::"+e1);
			e1.printStackTrace();
			throw new Exception();
		}
	}

}
