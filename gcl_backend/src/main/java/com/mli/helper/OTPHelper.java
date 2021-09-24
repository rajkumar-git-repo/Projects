package com.mli.helper;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.mli.entity.OTPHistoryEntity;
import com.mli.model.otp.GenerateOTPPayload;
import com.mli.model.otp.Header;
import com.mli.model.otp.OTPModel;

@Component
public  class OTPHelper {

	// OTP Generation
	@Value("${mli.genotp.soaappId}")
	private  String GenSoaAppId;
	
//	@Value("${mli.genotp.soacorrelationId}")
//	private  String GenSoaCorrelationId;
	
	@Value("${mli.genotp.soamsgversion}")
	private  String GenSoaMsgVersion;
	
//	@Value("${mli.genotp.stepId}")
//	private  String GenStepId;
	
//	@Value("${mli.genotp.unqTokenNo}")
//	private  String GenUnqTokenNo;
	
	// OYP Verify
	@Value("${mli.verifyotp.soaappId}")
	private  String VerSoaAppId;
	
//	@Value("${mli.verifyotp.soacorrelationId}")
//	private  String VerSoaCorrelationId;
	
	@Value("${mli.verifyotp.soamsgversion}")
	private  String VerSoaMsgVersion;
	
//	@Value("${mli.verifyotp.stepId}")
//	private  String VerStepId;
	
	private static Logger logger = LoggerFactory.getLogger(UserDetailsConverter.class);

	public  OTPModel getGenerateOTPRequestModel() {
		logger.info("::::::::::::::   set OTP generation credentials :::::::::::::::::");
		OTPModel otpModel = new OTPModel();
		Header header = new Header();
		GenerateOTPPayload payload = new GenerateOTPPayload();

		header.setSoaAppId(GenSoaAppId);
//		header.setSoaCorrelationId(GenSoaCorrelationId);
		header.setSoaCorrelationId(UUID.randomUUID().toString());
		header.setSoaMsgVersion(GenSoaMsgVersion);

		payload.setOtpCode(null);
//		payload.setStepId(GenStepId);
		payload.setStepId("");
//		payload.setUnqTokenNo(GenUnqTokenNo);
		payload.setUnqTokenNo(UUID.randomUUID().toString());

		otpModel.setHeader(header);
		otpModel.setPayload(payload);
		
		logger.info("::::::::::::::Otp Generation Request:::::::::::::::");
		logger.info("OTP (Generaton) soaCorrelationId : "+header.getSoaCorrelationId());
		logger.info("OTP (Generaton) unqTokenNo : "+payload.getUnqTokenNo());
		logger.info("OTP (Generaton) stepId : "+payload.getStepId());

		return otpModel;
	}

	public  OTPModel getValidateRequestData(OTPHistoryEntity otpDetails) {
		logger.info("::::::::::::::   set OTP verification credentials :::::::::::::::::");
		OTPModel otpModel = new OTPModel();
		Header header = new Header();
		GenerateOTPPayload payload = new GenerateOTPPayload();

//		header.setSoaCorrelationId(VerSoaCorrelationId);
		header.setSoaCorrelationId(UUID.randomUUID().toString());
		header.setSoaMsgVersion(VerSoaMsgVersion);
		header.setSoaAppId(VerSoaAppId);

		payload.setUnqTokenNo(otpDetails.getUniqueToken());
		payload.setOtpCode(otpDetails.getOtp().toString());
//		payload.setStepId(VerStepId);
		payload.setStepId("");

		otpModel.setHeader(header);
		otpModel.setPayload(payload);
		
		logger.info("::::::::::::::Otp Verify Request:::::::::::::::");
		logger.info("OTP (Verify) soaCorrelationId : "+header.getSoaCorrelationId());
		logger.info("OTP (Verify) unqTokenNo : "+payload.getUnqTokenNo());
		logger.info("OTP (Verify) stepId : "+payload.getStepId());
		logger.info("OTP (Verify) Contact No : "+otpDetails.getContNo());
		return otpModel;
	}
}
