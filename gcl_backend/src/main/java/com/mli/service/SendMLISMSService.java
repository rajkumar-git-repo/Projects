package com.mli.service;

import com.mli.entity.CreditCardCustomerEntity;
import com.mli.entity.CustomerDetailsEntity;
import com.mli.enums.OTPType;
import com.mli.model.sms.SMSResponseModel;

public interface SendMLISMSService {

	SMSResponseModel sendMLISMS(Integer otp, Long contNo, OTPType otpPurpose, CustomerDetailsEntity customerDetails) throws Exception;

	SMSResponseModel sendSmsToYBLCCCustomer(Integer otp, Long contNo, OTPType otpPurpose,CreditCardCustomerEntity creditCardCustomerEntity) throws Exception;

	
}
