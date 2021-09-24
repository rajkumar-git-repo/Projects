package com.mli.service.impl;


import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.mli.constants.SMSConstant;
import com.mli.entity.CreditCardCustomerEntity;
import com.mli.entity.CustomerDetailsEntity;
import com.mli.enums.OTPType;
import com.mli.model.sms.SMSGeneralConsumerInformationModel;
import com.mli.model.sms.SMSMliSmsServiceModel;
import com.mli.model.sms.SMSRequestBodyModel;
import com.mli.model.sms.SMSRequestHeaderModel;
import com.mli.model.sms.SMSResponseModel;
import com.mli.service.SendMLISMSService;
import com.mli.utils.AES;
import com.mli.utils.MliApiUtils;

/**
 * @author Nikhilesh.Tiwari
 *
 */
@Service
public class SendMLISMSServiceImpl implements SendMLISMSService {
	
	
	private static final Logger logger = LoggerFactory.getLogger(SendMLISMSServiceImpl.class);

	@Value("${mli.sms.messageversion}")
	private String SmsMessageversion;

	@Value("${mli.sms.consumerid}")
	private String SmsConsumerId;

//	@Value("${mli.sms.correlationid}")
//	private String SmsCorrelationid;

	@Value("${mli.sms.appaccid}")
	private String SmsAppAccid;

	@Value("${mli.sms.appaccpass}")
	private String SmsAppAccpass;
	
	@Value("${mli.sms.bitly.appaccid}")
	private String smsAppBitlyAccid;

	@Value("${mli.sms.bitly.appaccpass}")
	private String smsAppBitlyAccpass;

	@Value("${mli.sms.appid}")
	private String SmsAppid;
	
	@Autowired
	private MliApiUtils mliApiUtilService;
	
	@Value("${mli.customerscreen.redirect.url}")
	private String custScreenRdirectUrl;
	
	@Value("${mli.aes.encryption.key}")
	private String aesSecratKey;
	
	@Value("${mli.sms.bitly.consumerid}")
	private String bitlyConsumerId;
	
	/**
	 * Send a sms to customers & seller for OTP and at final submit
	 */
	
	@Override
	public SMSResponseModel sendMLISMS(Integer otp, Long contNo, OTPType otpPurpose, CustomerDetailsEntity customerDetails) throws Exception {

		SMSRequestHeaderModel requestHeader = new SMSRequestHeaderModel();
		SMSGeneralConsumerInformationModel generalConsumerInformation = new SMSGeneralConsumerInformationModel();
		SMSRequestBodyModel requestBody = new SMSRequestBodyModel();
		SMSMliSmsServiceModel smsMliSmsServiceModel = new SMSMliSmsServiceModel();
		SMSResponseModel smsResponse = new SMSResponseModel();

		generalConsumerInformation.setMessageVersion(SmsMessageversion);
		generalConsumerInformation.setConsumerId(SmsConsumerId);
//		generalConsumerInformation.setCorrelationId(SmsCorrelationid);
		generalConsumerInformation.setCorrelationId(UUID.randomUUID().toString());
		logger.info("SMS CorrelationId : "+generalConsumerInformation.getCorrelationId());
		requestHeader.setGeneralConsumerInformation(generalConsumerInformation);

		requestBody.setAppAccId(SmsAppAccid);
		requestBody.setAppAccPass(SmsAppAccpass);
		requestBody.setAppId(SmsAppid);
		requestBody.setMsgTo(contNo.toString());
		StringBuilder fullName = new StringBuilder();
		if (customerDetails != null) {
			if (customerDetails.getCustomerFirstName() != null) {
				fullName.append(customerDetails.getCustomerFirstName());
			}
			if (customerDetails.getCustomerLastName() != null) {
				fullName.append(" ");
				fullName.append(customerDetails.getCustomerLastName());
			}
		}
		switch (otpPurpose) {
		case SELLER_OTP_VERIFY_SMS:
			requestBody.setMsgText(String.format(SMSConstant.SELLER_OTP_MSG_TEXT, otp));
			break;
		case CUST_OTP_VERIFY_SMS:
			requestBody.setMsgText(String.format(SMSConstant.CUST_OTP_VERIFY_MSG_TEXT, otp,customerDetails.getLoanAppNumber()));
			break;
		case CUST_APP_SUBMIT :
			generalConsumerInformation.setConsumerId(bitlyConsumerId);
			requestHeader.setGeneralConsumerInformation(generalConsumerInformation);
			requestBody.setAppAccId(smsAppBitlyAccid);
			requestBody.setAppAccPass(smsAppBitlyAccpass);
			requestBody.setMsgText(String.format(SMSConstant.CUST_APP_FINAL_SUBMIT_TEXT,customerDetails.getLoanAppNumber()));
			break;
		case SELLER_APP_SUBMIT :
			generalConsumerInformation.setConsumerId(bitlyConsumerId);
			requestHeader.setGeneralConsumerInformation(generalConsumerInformation);
			requestBody.setAppAccId(smsAppBitlyAccid);
			requestBody.setAppAccPass(smsAppBitlyAccpass);
			requestBody.setMsgText(String.format(SMSConstant.SELLER_APP_FINAL_SUBMIT_TEXT,customerDetails.getLoanAppNumber()));
			break;
		case APP_SENT:
			generalConsumerInformation.setConsumerId(bitlyConsumerId);
			requestHeader.setGeneralConsumerInformation(generalConsumerInformation);
			String encryptedStr = AES.encrypt(customerDetails.getCustMobileNo()+"_"+customerDetails.getProposalNumber(), aesSecratKey);
			requestBody.setAppAccId(smsAppBitlyAccid);
			requestBody.setAppAccPass(smsAppBitlyAccpass);
			String redirectURL = ""+custScreenRdirectUrl+"/cs/"+encryptedStr;
			logger.info("SMS redirectURL : "+redirectURL);
			requestBody.setMsgText(String.format(SMSConstant.APP_SENT,customerDetails.getLoanAppNumber(),redirectURL));
			break;
		}
		smsMliSmsServiceModel.setRequestBody(requestBody);
		smsMliSmsServiceModel.setRequestHeader(requestHeader);
		smsResponse = mliApiUtilService.callAPISmsPost(smsMliSmsServiceModel);
		return smsResponse;
	}
	
	/**
	 * Send a sms to yblecc customers & seller for OTP and at final submit
	 */
	@Override
	public SMSResponseModel sendSmsToYBLCCCustomer(Integer otp, Long contNo, OTPType otpPurpose, CreditCardCustomerEntity creditCardCustomerEntity) throws Exception {
		SMSRequestHeaderModel requestHeader = new SMSRequestHeaderModel();
		SMSGeneralConsumerInformationModel generalConsumerInformation = new SMSGeneralConsumerInformationModel();
		SMSRequestBodyModel requestBody = new SMSRequestBodyModel();
		SMSMliSmsServiceModel smsMliSmsServiceModel = new SMSMliSmsServiceModel();
		SMSResponseModel smsResponse = new SMSResponseModel();

		generalConsumerInformation.setMessageVersion(SmsMessageversion);
		generalConsumerInformation.setConsumerId(SmsConsumerId);
		generalConsumerInformation.setCorrelationId(UUID.randomUUID().toString());
		logger.info("SMS CorrelationId : "+generalConsumerInformation.getCorrelationId());
		requestHeader.setGeneralConsumerInformation(generalConsumerInformation);

		requestBody.setAppAccId(SmsAppAccid);
		requestBody.setAppAccPass(SmsAppAccpass);
		requestBody.setAppId(SmsAppid);
		requestBody.setMsgTo(contNo.toString().trim());
		StringBuilder fullName = new StringBuilder();
		if (!ObjectUtils.isEmpty(creditCardCustomerEntity)) {
			if (!StringUtils.isEmpty(creditCardCustomerEntity.getFirstName())) {
				fullName.append(creditCardCustomerEntity.getFirstName());
			}
			if (!StringUtils.isEmpty(creditCardCustomerEntity.getLastName())) {
				fullName.append(" ");
				fullName.append(creditCardCustomerEntity.getLastName());
			}
		}
		switch (otpPurpose) {
			case YBLCC_APP_SENT:
				generalConsumerInformation.setConsumerId(bitlyConsumerId);
				requestHeader.setGeneralConsumerInformation(generalConsumerInformation);
				String encryptedStr = AES.encrypt(creditCardCustomerEntity.getPhone() + "_" + creditCardCustomerEntity.getProposalNumber(),aesSecratKey);
				String redirectURL = "" + custScreenRdirectUrl + "/cs/yblcc/" + encryptedStr;
				logger.info("SMS redirectURL : " + redirectURL);
				requestBody.setMsgText(String.format(SMSConstant.YBLCC_APP_SENT, redirectURL));
				break;
			case YBLCC_OTP_SENT:
				requestHeader.setGeneralConsumerInformation(generalConsumerInformation);
				requestBody.setMsgText(String.format(SMSConstant.YBLCC_CUST_OTP_VERIFY_MSG_TEXT, otp,creditCardCustomerEntity.getCoverage()));
		        break;
			case YBLCC_CUST_APP_SUBMIT:
				generalConsumerInformation.setConsumerId(bitlyConsumerId);
				requestHeader.setGeneralConsumerInformation(generalConsumerInformation);
				requestBody.setAppAccId(smsAppBitlyAccid);
				requestBody.setAppAccPass(smsAppBitlyAccpass);
				requestBody.setMsgText(String.format(SMSConstant.YBLCC_CUST_FINAL_SUBMIT_TEXT, fullName,creditCardCustomerEntity.getPremium(), creditCardCustomerEntity.getCoverage()));
		        break;
			case YBLCC_PAYMENT_FAIL:
				generalConsumerInformation.setConsumerId(bitlyConsumerId);
				requestHeader.setGeneralConsumerInformation(generalConsumerInformation);
				requestBody.setAppAccId(smsAppBitlyAccid);
				requestBody.setAppAccPass(smsAppBitlyAccpass);
				String encryptedString = AES.encrypt(creditCardCustomerEntity.getPhone() + "_" + creditCardCustomerEntity.getProposalNumber(),aesSecratKey);
				String redirectPaymentURL = "" + custScreenRdirectUrl + "/cs/yblcc/" + encryptedString;
				logger.info("SMS redirectURL : " + redirectPaymentURL);
				requestBody.setMsgText(String.format(SMSConstant.YBLCC_PAYMENT_FAILED,fullName, redirectPaymentURL));
				break;
			case YBL_VERIFICATION_PENDING1:
				generalConsumerInformation.setConsumerId(bitlyConsumerId);
				requestHeader.setGeneralConsumerInformation(generalConsumerInformation);
				requestBody.setAppAccId(smsAppBitlyAccid);
				requestBody.setAppAccPass(smsAppBitlyAccpass);
				String encryptedString1 = AES.encrypt(creditCardCustomerEntity.getPhone() + "_" + creditCardCustomerEntity.getProposalNumber(),aesSecratKey);
				String redirectPaymentURL1 = "" + custScreenRdirectUrl + "/cs/yblcc/" + encryptedString1;
				logger.info("SMS redirectURL : " + redirectPaymentURL1);
				requestBody.setMsgText(String.format(SMSConstant.YBLCC_VERIFICATION_REMINDER1, fullName, creditCardCustomerEntity.getProposalNumber(), redirectPaymentURL1));
		        break;
			case YBL_VERIFICATION_PENDING2:
				generalConsumerInformation.setConsumerId(bitlyConsumerId);
				requestHeader.setGeneralConsumerInformation(generalConsumerInformation);
				requestBody.setAppAccId(smsAppBitlyAccid);
				requestBody.setAppAccPass(smsAppBitlyAccpass);
				String encryptedString2 = AES.encrypt(creditCardCustomerEntity.getPhone() + "_" + creditCardCustomerEntity.getProposalNumber(),aesSecratKey);
				String redirectPaymentURL2 = "" + custScreenRdirectUrl + "/cs/yblcc/" + encryptedString2;
				logger.info("SMS redirectURL : " + redirectPaymentURL2);
				requestBody.setMsgText(String.format(SMSConstant.YBLCC_VERIFICATION__REMINDER2, fullName,creditCardCustomerEntity.getProposalNumber(), redirectPaymentURL2));
		        break;
			case YBL_VERIFICATION_PENDING3:
				generalConsumerInformation.setConsumerId(bitlyConsumerId);
				requestHeader.setGeneralConsumerInformation(generalConsumerInformation);
				requestBody.setAppAccId(smsAppBitlyAccid);
				requestBody.setAppAccPass(smsAppBitlyAccpass);
				String encryptedString3 = AES.encrypt(creditCardCustomerEntity.getPhone() + "_" + creditCardCustomerEntity.getProposalNumber(),aesSecratKey);
				String redirectPaymentURL3 = "" + custScreenRdirectUrl + "/cs/yblcc/" + encryptedString3;
				logger.info("SMS redirectURL : " + redirectPaymentURL3);
				requestBody.setMsgText(String.format(SMSConstant.YBLCC_VERIFICATION__REMINDER3, fullName,creditCardCustomerEntity.getProposalNumber(), redirectPaymentURL3));
		        break;
			case YBL_PAYMENT_PENDING1:
				String encryptedString4 = AES.encrypt(creditCardCustomerEntity.getPhone() + "_" + creditCardCustomerEntity.getProposalNumber(),aesSecratKey);
				String redirectPaymentURL4 = "" + custScreenRdirectUrl + "/cs/yblcc/" + encryptedString4;
				logger.info("SMS redirectURL : " + redirectPaymentURL4);
				requestBody.setMsgText(String.format(SMSConstant.YBLCC_PAYMENT_REMINDER1, fullName,creditCardCustomerEntity.getProposalNumber(), redirectPaymentURL4));
		        break;
			case YBL_PAYMENT_PENDING2:
				String encryptedString5 = AES.encrypt(creditCardCustomerEntity.getPhone() + "_" + creditCardCustomerEntity.getProposalNumber(),aesSecratKey);
				String redirectPaymentURL5 = "" + custScreenRdirectUrl + "/cs/yblcc/" + encryptedString5;
				logger.info("SMS redirectURL : " + redirectPaymentURL5);
				requestBody.setMsgText(String.format(SMSConstant.YBLCC_PAYMENT_REMINDER2, fullName,creditCardCustomerEntity.getProposalNumber(), redirectPaymentURL5));
		        break;
			case YBL_PAYMENT_PENDING3:
				String encryptedString6 = AES.encrypt(creditCardCustomerEntity.getPhone() + "_" + creditCardCustomerEntity.getProposalNumber(),aesSecratKey);
				String redirectPaymentURL6 = "" + custScreenRdirectUrl + "/cs/yblcc/" + encryptedString6;
				logger.info("SMS redirectURL : " + redirectPaymentURL6);
				requestBody.setMsgText(String.format(SMSConstant.YBLCC_PAYMENT_REMINDER3, fullName,creditCardCustomerEntity.getProposalNumber(), redirectPaymentURL6));
		        break;
		}
		smsMliSmsServiceModel.setRequestBody(requestBody);
		smsMliSmsServiceModel.setRequestHeader(requestHeader);
		smsResponse = mliApiUtilService.callAPISmsPost(smsMliSmsServiceModel);
		return smsResponse;
	}

}
