package com.mli.service.impl;


import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.svenson.JSONParser;

import com.mli.constants.Constant;
import com.mli.constants.DocType;
import com.mli.enums.OTPUserType;
import com.mli.helper.EmailDetailHelper;
import com.mli.modal.email.EmailModel;
import com.mli.modal.email.MLIEmailResponse;
import com.mli.modal.email.MliEmailServiceModel;
import com.mli.model.AttchDetailsModel;
import com.mli.model.response.ResponseModel;
import com.mli.service.MliEmailService;
import com.mli.utils.AES;
import com.mli.utils.DateUtil;
import com.mli.utils.MliApiUtils;
import com.mli.utils.ObjectsUtil;

/**
 * @author Nikhilesh.Tiwari
 *
 */
@Service
public class MliEmailServiceImpl implements MliEmailService {

	private static Logger logger = Logger.getLogger(SellerServiceImpl.class);

	@Autowired
	private EmailDetailHelper emailDetailHelper = new EmailDetailHelper();

	@Value("${mli.mailFrom}")
	private String mailFrom;
	
	@Value("${mli.mailfrom.name}")
	private String mailFromName;
	
	@Value("${mli.customerscreen.redirect.url}")
	private String custScreenRdirectUrl;
	
	@Autowired
	private MliApiUtils mliApiUtilService;
	
	@Value("${mli.aes.encryption.key}")
	private String aesSecratKey;
	
	@Value("${mli.zip.download.redirect}")
	private String serverRedirectUrl;
	
	@Value("${mli.server.identified}")
	private String serverIdentified;
	
	/**
	 * Send a mail to customers  automatically in PDF format ,with user's current  data 
	 */
	@Override
	public ResponseModel<MliEmailServiceModel> sendEmail(EmailModel mliEmailModel) throws Exception {
		try {

			MliEmailServiceModel MliEmailService = new MliEmailServiceModel();
			MLIEmailResponse EmailResponse = new MLIEmailResponse();
			List<AttchDetailsModel> attchDetails = new ArrayList<AttchDetailsModel>();
			ResponseModel<MliEmailServiceModel> responseModel = new ResponseModel<MliEmailServiceModel>();
			
			AttchDetailsModel attchDetail = new AttchDetailsModel();			
			if(!mliEmailModel.getMailUserType().equals(Constant.OTP_AT_EMAIL) && !mliEmailModel.getMailUserType().equals(Constant.YBLCC_OTP_AT_EMAIL) && !mliEmailModel.getMailUserType().equals(Constant.LOGIN_OTP_AT_EMAIL)) {
				attchDetail.setName(mliEmailModel.getName());
				attchDetail.setType(mliEmailModel.getType());
				attchDetail.setBytes(mliEmailModel.getBytes());
				attchDetails.add(attchDetail);
				mliEmailModel.setAttchDetails(attchDetails);
			}
			
			//mliEmailModel.setMailIdTo(mliEmailModel.getMailIdTo());
			mliEmailModel.setFromEmail(mailFrom);
			mliEmailModel.setFromName(mailFromName);
			
			String custMailBody = "";
			if (mliEmailModel.getMailUserType().equals(Constant.YBLCC_OTP_AT_EMAIL)) {
				custMailBody = "<html><head></head><body><div style='width: 600px;'><div style=''>"
						+ "</div><div style='width: 95%; text-align: justify;'><p>Dear "
						+ mliEmailModel.getCustomerName()
						+ ",</p><p><b>"
						+ mliEmailModel.getOtp()
						+ "</b> is your OTP to complete the verification for Max Life Group Term Life Insurance"	
						+ " Product with coverage of <b>"+mliEmailModel.getCoverage()+".</b></p>"
						+ "<p>We assure you that we are continuously working on creating a financially secure future for you and your family.</p>"
						+ "<p>For any further assistance, e-mail us at </p>"
						+ "<p>group.servicehelpdesk@maxlifeinsurance.com</p>"
						+ "<p>We value your association with us and assure you the best of our services always.</p>"
						+ "<p>Warm Regards,<br><b>Max Life Insurance Company Limited</b></p>"
						+ "<div><span style='FONT-WEIGHT: bold;'>Note:</span><div style='padding-left: 28px;'><ul><li>This is an electronically generated mail. Please do not reply to this email.</li></ul></div></div>"
						+ "</div></div></body></html>";
				mliEmailModel.setMailSubject("OTP for application verification");
				mliEmailModel.setMailBody(custMailBody);
			}
			
			if (mliEmailModel.getMailUserType().equals(Constant.OTP_AT_EMAIL)) {
				custMailBody = "<html><head></head><body><div style='width: 600px;'><div style=''>"
						+ "</div><div style='width: 95%; text-align: justify;'><p>Dear "
						+ mliEmailModel.getCustomerName()
						+ ",</p><p><b>"
						+ mliEmailModel.getOtp()
						+ "</b> is your One Time Verification Code for your Application No. <b>"+mliEmailModel.getLoanAppNumber()+"</b>"	
						+ ". Please use this Verification Code  to authorize your application processing.</p>"
						+ "<p><b>For further assistance</b><br>Please write to us at Helpdesk.Homeoffice@maxlifeinsurance.com or call on our helpline number 18002665433 if you need more information or help with your Application.</p>"
						+ "<p><b>Regards,<br>Max Life Insurance (Group Customer Services Team)</b></p>"
						+ "<div><span style='FONT-WEIGHT: bold;'>Note:</span><div style='padding-left: 28px;'><ul><li>* This is system generated email. Please do not reply on this mail.</li></ul></div></div>"
						+ "</div></div></body></html>";
				mliEmailModel.setMailSubject(
						"Max Life GCL Secure Proposal (OTP)");
				mliEmailModel.setMailBody(custMailBody);
			}
			if (OTPUserType.CUSTOMER.getLabel().equals(mliEmailModel.getMailUserType())) {
			/*String custMailBody ="<html><head></head><body><div style='width: 600px;'><div style=''><img alt='Max Life Insurance' src='https://sitminte.maxlifeinsurance.com/mli-platform-ep/email_img/logo.jpg' style='border: 0px; position: relative;'>"+ 
					"</div><div style='width: 95%; text-align: justify;'><p>Dear "+mliEmailModel.getCustomerName()+",</p><p>Thank you for submitting the e-application for Max Life Group Credit Life Secure insurance against loan application no. "+mliEmailModel.getLoanAppNumber()+" to Max Life Insurance.</p>"+
					"<p>Please find the details you provided in the application form in PDF attached herewith this mail.</p><p>We are processing your application and will contact you if we need more details.</><p>Password- First 3 characters of your name in caps+DDMM of DOB (Ex- SHI2107)</p><p>Warm Regards,</p><p>Max Life Insurance Group Business Team</p><div><span style='FONT-WEIGHT: bold;'>Note:</span><div style='padding-left: 28px;'><ul><li>* This is system generated email. Please do not reply on this mail.</li></ul></div></div>"+
					"</div><div><img alt='Max Life Insurance' src='https://sitminte.maxlifeinsurance.com/mli-platform-ep/email_img/footer_agency.jpg' style='border: 0px; position: relative; width: 100%;'></div></div></body></html>";
			*/
				String encryptedStr = AES.encrypt(mliEmailModel.getCustMobNumber()+"_"+mliEmailModel.getProposalNumber(), aesSecratKey);
				String redirectURL = ""+custScreenRdirectUrl+"/cs/"+encryptedStr;
				logger.info("redirec url : "+redirectURL);
				custMailBody = "<html><head></head><body><div style='width: 600px;'><div style=''>"
						+ "</div><div style='width: 95%; text-align: justify;'><p>Dear "
						+ mliEmailModel.getCustomerName()
						+ ",</p><p>Thank you for showing interest in the Max Life Group Credit Life scheme. Your Application No. is <b>"
						+ mliEmailModel.getLoanAppNumber()+"</b>"
						+"<p>To view and verify your filled Application-cum-Health Declaration Form <a href="+redirectURL+">click here</a></p>"
						+ "<p><b>For further assistance</b><br>Please write to us at Helpdesk.Homeoffice@maxlifeinsurance.com or call on our helpline number 18002665433 if you need more information or help with your Application.</p><p><b>Regards,<br>Max Life Insurance (Group Customer Services Team)</b></p>"
						+ "<div><span style='FONT-WEIGHT: bold;'>Note:</span><div style='padding-left: 28px;'><ul><li>* This is system generated email. Please do not reply on this mail.</li></ul></div></div>"
						+ "</div></div></body></html>";
			
           
              mliEmailModel.setMailSubject("Max Life GCL Secure Proposal against app. no.- " +mliEmailModel.getLoanAppNumber());
              mliEmailModel.setMailBody(custMailBody);
				if (Constant.STEP_5.equals(mliEmailModel.getStep())) {
					mliEmailModel.setAttchDetails(null);
				}
			}
			if (OTPUserType.BANK.getLabel().equals(mliEmailModel.getMailUserType())) {
				String encodedDate = AES.encrypt(DateUtil.dateFormat(mliEmailModel.getVerifiedDate()), aesSecratKey);
				String redirectUrl = serverRedirectUrl+"/mli/download/zip?d="+encodedDate+"&f="+mliEmailModel.getZipFileName()+"&t="+AES.encrypt(DocType.PROPOSAL, aesSecratKey);
				logger.info("redirect URL (BANK) : "+redirectUrl);
				String bankBody ="<html><head></head><body><div style='width: 600px;'><div style=''>"+
		            		  "</div><div style='width: 95%; text-align: justify;'><p>Dear Sir/Madam,</p><p>Please find attached the Max Life Group Credit Life MIS for the Date "+DateUtil.extractDateAsStringSlashFormate(DateUtil.toCurrentUTCTimeStamp())+"</p>"+
		            		  "<p>Thanks and Regards,<br>Max Life Insurance Group Business Team</p><div><span style='FONT-WEIGHT: bold;'>Note:</span><div style='padding-left: 28px;'><ul><li>* This is system generated email. Please do not reply on this mail.</li></ul></div></div>"+ 
		            		  "</div></div></body></html>";
			
			 mliEmailModel.setMailBody(bankBody);
			 mliEmailModel.setMailSubject("List of Max Life GCL product applications - " +DateUtil.extractDateAsStringSlashFormate(DateUtil.toCurrentUTCTimeStamp())+" "+serverIdentified);
			}
			if(OTPUserType.UWT.getLabel().equals(mliEmailModel.getMailUserType())) {
				String encodedDate = AES.encrypt(DateUtil.dateFormat(mliEmailModel.getVerifiedDate()), aesSecratKey);
				String proposalDownloadUrl = serverRedirectUrl+"/mli/download/zip?d="+encodedDate+"&f="+Constant.PROPOSAL_ZIP+"&t="+AES.encrypt(DocType.PROPOSAL, aesSecratKey);
				String passportDownloadUrl = serverRedirectUrl+"/mli/download/zip?d="+encodedDate+"&f="+Constant.PASSPORT_ZIP+"&t="+AES.encrypt(DocType.PASSPORT, aesSecratKey);
				String bankBody ="<html><head></head><body><div style='width: 600px;'><div style=''>"+
		            		  "</div><div style='width: 95%; text-align: justify;'><p>Dear Sir/Madam,</p><p>Please find attached the Max Life Group Credit Life MIS for the Date "+DateUtil.extractDateAsStringSlashFormate(DateUtil.toCurrentUTCTimeStamp())+"</p>"+
		            		  "<p>Thanks and Regards,<br>Max Life Insurance Group Business Team</p><div><span style='FONT-WEIGHT: bold;'>Note:</span><div style='padding-left: 28px;'><ul><li>* This is system generated email. Please do not reply on this mail.</li></ul></div></div>"+ 
		            		  "</div></div></body></html>";
				
				mliEmailModel.setMailBody(bankBody);
				 mliEmailModel.setMailSubject("List of Max Life GCL product applications - " +DateUtil.extractDateAsStringSlashFormate(DateUtil.toCurrentUTCTimeStamp())+" "+serverIdentified);
			}
			
			if(OTPUserType.VERIFIED.getLabel().equals(mliEmailModel.getMailUserType())) {
				custMailBody = "<html><head></head><body><div style='width: 600px;'><div style=''>"
						+ "</div><div style='width: 95%; text-align: justify;'><p>Dear "
						+ mliEmailModel.getCustomerName()
						+ ",</p><p>Thank you for verifying your details for your application to the Max Life Group Credit Life scheme. Please note your Application No. <b>"
						+ mliEmailModel.getLoanAppNumber()+"</b>"
						+" & please find attached the filled Application-cum-Health Declaration Form copy for your reference."  
						+ "<p>For any technical assistance please write to us at Helpdesk.Homeoffice@maxlifeinsurance.com or call on our helpline number 18002665433</p><p><b>Regards,<br>Max Life Insurance (Group Customer Services Team)</b></p>"
						+ "<div><span style='FONT-WEIGHT: bold;'>Note:</span><div style='padding-left: 28px;'><ul><li>* This is system generated email. Please do not reply on this mail.</li></ul></div></div>"
						+ "</div></div></body></html>";
              mliEmailModel.setMailSubject("Max Life GCL Secure Proposal against app. no.- " +mliEmailModel.getLoanAppNumber());
              mliEmailModel.setMailBody(custMailBody);
			}
			
			if(OTPUserType.YBLCCVERIFIED.getLabel().equals(mliEmailModel.getMailUserType())) {
				custMailBody = "<html><head><style>\n" + 
						"table, th, td {\n" + 
						"  border: 1px solid black;\n" + 
						"  border-collapse: collapse;\n" + 
						"}\n" + 
						"th, td {\n" + 
						"  padding: 5px;\n" + 
						"  text-align: center;    \n" + 
						"}\n" + 
						"</style></head><body><div style='width: 600px;'><div style=''>"
						+ "</div><div style='width: 95%; text-align: justify;'><p>Dear "
						+ mliEmailModel.getCustomerName()
						+ "</div><div style='width: 95%; text-align: justify;'><p>Greetings from Max Life Insurance!"
						+ ",</p><p>Thank you for paying the premium of Rs.<b>"+mliEmailModel.getPremium()+"</b> towards Max Life Group Term Life Insurance Plan for <b>"+mliEmailModel.getCoverage()+"</b>"
						+ "<p>Presenting further details on the application.</p>"
						+"<table style=\"width:95%\">\n" + 
						"  <tr>\n" + 
						"    <th align=\"center\" colspan=\"12\">Coverage details</th>\n" + 
						"  </tr>\n" + 
						"  <tr>\n" + 
						"    <th>Application No.</th>\n" + 
						"    <td>"+mliEmailModel.getProposalNumber()+"</td>\n" + 
						"    <th>Sum Assured(Rs.)</th>\n" + 
						"    <td>"+mliEmailModel.getCoverage()+"</td>\n" + 
						"    <th>Premium(Incl. of GST)(Rs.)</th>\n" + 
						"    <td>"+mliEmailModel.getPremium()+"</td>	 \n" + 
						"  </tr>\n"+ 
						"</table><br></br>"
						+ "<table style=\"width:95%\">\n" + 
						"  <tr>\n" + 
						"    <th align=\"center\" colspan=\"12\">Payment details</th>\n" + 
						"  </tr>\n" + 
						"  <tr>\n" + 
						"    <th>Premium Payment</th>\n" + 
						"    <td>"+mliEmailModel.getAmount()+"</td>\n" + 
						"	  <th>Date of Payment</th>\n" + 
						"    <td>"+mliEmailModel.getPaymentDate()+"</td>\n" + 
						"	  <th>Application No.</th>\n" + 
						"    <td>"+mliEmailModel.getProposalNumber()+"</td>\n" + 
						"	 \n" + 
						"  </tr>\n" + 
						"   <tr>\n" + 
						"    <th>MODE(YBL Credit/Debit Card)</th>\n" + 
						"    <td>"+mliEmailModel.getPaymentMode()+"</td>\n" + 
						"	  <th>Transaction Ref No.</th>\n" + 
						"    <td>"+mliEmailModel.getTransactionNumber()+"</td>\n" + 
						"	  <th></th>\n" + 
						"    <td></td>\n" + 
						"	 \n" + 
						"  </tr>\n" + 
						"</table>"
						+ "<p>We assure you that we are continuously working on creating a financially secure future for you and \n" + 
						  "your family.</p>"
						+ "<p>For any further assistance, e-mail us at<br>group.servicehelpdesk@maxlifeinsurance.com</p>"
						+ "<p>We value your association with us and assure you the best of our services always.</p>"
						+ "<p>Warm Regards,<br><b>Max Life Insurance Company Limited</b></p>"
						+ "<div><span style='FONT-WEIGHT: bold;'>Note:</span><div style='padding-left: 28px;'><ul><li>* This is an electronically generated mail. Please do not reply to this email.</li></ul></div></div>"
						+ "</div></div></body></html>";
              mliEmailModel.setMailSubject("Payment Confirmation- Max Life Group Credit Life Policy");
              mliEmailModel.setMailBody(custMailBody);
			}
			
			if(OTPUserType.YBLCCCUSTOMER.getLabel().equals(mliEmailModel.getMailUserType())) {
				String encryptedStr = AES.encrypt(mliEmailModel.getCustMobNumber()+"_"+mliEmailModel.getProposalNumber(), aesSecratKey);
				String redirectURL = ""+custScreenRdirectUrl+"/cs/yblcc/"+encryptedStr;
				logger.info("redirec url : "+redirectURL);
				custMailBody = "<html><head></head><body><div style='width: 650px;'><div style=''>"
						+ "</div><div style='width: 95%; text-align: justify;'><p>Dear "
						+ mliEmailModel.getCustomerName()
						+ "</div><div style='width: 95%; text-align: justify;'><p>Greetings from Max Life Insurance!"
						+ "</p><p>Thank you for showing interest in Max Life Group Term Life Platinum Assurance plan. Please \n"
						+ "click on <a href="+redirectURL+">click here</a> to complete the journey.</p>"
						+"<p>We assure you that we are continuously working on creating a financially secure future for you and \n" + 
						"your family.</p>"
						+ "<p>For any further assistance, e-mail us at<br>group.servicehelpdesk@maxlifeinsurance.com</p>"
						+"<p>Warm Regards,<br><b>Max Life Insurance Company Limited</b></p>"
						+ "<div><span style='FONT-WEIGHT: bold;'>Note:</span><div style='padding-left: 28px;'><ul><li>* This is an electronically generated mail. Please do not reply to this email.</li></ul></div></div>"
						+ "</div></div></body></html>";
			
           
              mliEmailModel.setMailSubject("Thank you for interest in Max Life Group Term Life Product. Kindly complete the journey");
              mliEmailModel.setMailBody(custMailBody);
				if (Constant.STEP_5.equals(mliEmailModel.getStep())) {
					mliEmailModel.setAttchDetails(null);
				}
			}
			if (OTPUserType.YBLCCBANK.getLabel().equals(mliEmailModel.getMailUserType())) {
				String encodedDate = AES.encrypt(DateUtil.dateFormat(mliEmailModel.getVerifiedDate()), aesSecratKey);
				String redirectUrl = serverRedirectUrl+"/mli/download/zip?d="+encodedDate+"&f="+mliEmailModel.getZipFileName()+"&t="+AES.encrypt(DocType.YBLCCPROPOSAL, aesSecratKey);
				String bankBody ="<html><head></head><body><div style='width: 600px;'><div style=''>"+
		            		  "</div><div style='width: 95%; text-align: justify;'><p>Dear Sir/Madam,</p><p>Please find attached the YES Bank Credit Card Insurance Applications MIS for the Date "+DateUtil.extractDateAsStringSlashFormate(DateUtil.toCurrentUTCTimeStamp())+"</p>"+
		            		  "<p>Thanks and Regards,<br>Max Life Insurance Group Business Team</p><div><span style='FONT-WEIGHT: bold;'>Note:</span><div style='padding-left: 28px;'><ul><li>* This is system generated email. Please do not reply on this mail.</li></ul></div></div>"+ 
		            		  "</div></div></body></html>";
			
			 mliEmailModel.setMailBody(bankBody);
			 mliEmailModel.setMailSubject("YES Bank Credit Card Insurance Applications - " +DateUtil.extractDateAsStringSlashFormate(DateUtil.toCurrentUTCTimeStamp())+" "+serverIdentified);
			}
			
			if (mliEmailModel.getMailUserType().equals(Constant.YBL_VERIFICATION_PENDING1)) {
				String encryptedStr = AES.encrypt(mliEmailModel.getCustMobNumber()+"_"+mliEmailModel.getProposalNumber(), aesSecratKey);
				String redirectURL = ""+custScreenRdirectUrl+"/cs/yblcc/"+encryptedStr;
				logger.info("redirec url : "+redirectURL);
				custMailBody = "<html><head></head><body><div style='width: 650px;'><div style=''>"
						+ "</div><div style='width: 95%; text-align: justify;'><p>Dear "
						+ mliEmailModel.getCustomerName()
						+ ",</p><p>please verify the Max Life Group Term Life Platinum Assurance application "+ mliEmailModel.getProposalNumber()+"\n"
						+ "and pay the Premium to activate your policy.Click on <a href="+redirectURL+">click here</a> to verify.</p>"
						+"<p>We assure you that we are continuously working on creating a financially secure future for you and \n" + 
						"your family.</p>"
						+ "<p>For any further assistance, e-mail us at<br>group.servicehelpdesk@maxlifeinsurance.com</p>"
						+"<p>Warm Regards,<br><b>Max Life Insurance Company Limited</b></p>"
						+ "<div><span style='FONT-WEIGHT: bold;'>Note:</span><div style='padding-left: 28px;'><ul><li>* This is an electronically generated mail. Please do not reply to this email.</li></ul></div></div>"
						+ "</div></div></body></html>";
				mliEmailModel.setMailSubject("Reminder to complete your application "+mliEmailModel.getProposalNumber()+" for Max Life Group Platinum Assurance plan");
				mliEmailModel.setMailBody(custMailBody);
				mliEmailModel.setAttchDetails(null);
			}
			if (mliEmailModel.getMailUserType().equals(Constant.YBL_VERIFICATION_PENDING2)) {
				String encryptedStr = AES.encrypt(mliEmailModel.getCustMobNumber()+"_"+mliEmailModel.getProposalNumber(), aesSecratKey);
				String redirectURL = ""+custScreenRdirectUrl+"/cs/yblcc/"+encryptedStr;
				logger.info("redirec url : "+redirectURL);
				custMailBody = "<html><head></head><body><div style='width: 650px;'><div style=''>"
						+ "</div><div style='width: 95%; text-align: justify;'><p>Dear "
						+ mliEmailModel.getCustomerName()
						+ ",</p><p>you can still verify the Max Life Group Term Life Platinum Assurance application "+ mliEmailModel.getProposalNumber()+"\n"
						+ "and pay the Premium to activate your policy.Click on <a href="+redirectURL+">click here</a> to verify.</p>"
						+"<p>We assure you that we are continuously working on creating a financially secure future for you and \n" + 
						"your family.</p>"
						+ "<p>For any further assistance, e-mail us at<br>group.servicehelpdesk@maxlifeinsurance.com</p>"
						+"<p>Warm Regards,<br><b>Max Life Insurance Company Limited</b></p>"
						+ "<div><span style='FONT-WEIGHT: bold;'>Note:</span><div style='padding-left: 28px;'><ul><li>* This is an electronically generated mail. Please do not reply to this email.</li></ul></div></div>"
						+ "</div></div></body></html>";
				mliEmailModel.setMailSubject("You are a few moments away from completing your application "+mliEmailModel.getProposalNumber()+" for Max Life Group Platinum Assurance plan");
				mliEmailModel.setMailBody(custMailBody);
				mliEmailModel.setAttchDetails(null);
			}
			if (mliEmailModel.getMailUserType().equals(Constant.YBL_VERIFICATION_PENDING3)) {
				String encryptedStr = AES.encrypt(mliEmailModel.getCustMobNumber()+"_"+mliEmailModel.getProposalNumber(), aesSecratKey);
				String redirectURL = ""+custScreenRdirectUrl+"/cs/yblcc/"+encryptedStr;
				logger.info("redirec url : "+redirectURL);
				custMailBody = "<html><head></head><body><div style='width: 650px;'><div style=''>"
						+ "</div><div style='width: 95%; text-align: justify;'><p>Dear "
						+ mliEmailModel.getCustomerName()
						+ ",</p><p>last chance to verify the Max Life Group Term Life Platinum Assurance application "+ mliEmailModel.getProposalNumber()+"\n"
						+ "and pay the Premium to activate your policy.Click on <a href="+redirectURL+">click here</a> to verify.</p>"
						+"<p>We assure you that we are continuously working on creating a financially secure future for you and \n" + 
						"your family.</p>"
						+ "<p>For any further assistance, e-mail us at<br>group.servicehelpdesk@maxlifeinsurance.com</p>"
						+"<p>Warm Regards,<br><b>Max Life Insurance Company Limited</b></p>"
						+ "<div><span style='FONT-WEIGHT: bold;'>Note:</span><div style='padding-left: 28px;'><ul><li>* This is an electronically generated mail. Please do not reply to this email.</li></ul></div></div>"
						+ "</div></div></body></html>";
				mliEmailModel.setMailSubject("Complete your application "+mliEmailModel.getProposalNumber()+" for Max Life Group Platinum Assurance plan and secure your family");
				mliEmailModel.setMailBody(custMailBody);
				mliEmailModel.setAttchDetails(null);
			}
			if (mliEmailModel.getMailUserType().equals(Constant.YBL_PAYMENT_PENDING1)) {
				String encryptedStr = AES.encrypt(mliEmailModel.getCustMobNumber()+"_"+mliEmailModel.getProposalNumber(), aesSecratKey);
				String redirectURL = ""+custScreenRdirectUrl+"/cs/yblcc/"+encryptedStr;
				logger.info("redirec url : "+redirectURL);
				custMailBody = "<html><head></head><body><div style='width: 650px;'><div style=''>"
						+ "</div><div style='width: 95%; text-align: justify;'><p>Dear "
						+ mliEmailModel.getCustomerName()
						+ ",</p><p> please pay the initial Premium towards the Max Life Group Term Life Platinum Assurance application "+ mliEmailModel.getProposalNumber()+"\n"
						+ "to activate your policy.Click on <a href="+redirectURL+">click here</a> to pay.</p>"
						+"<p>We assure you that we are continuously working on creating a financially secure future for you and \n" + 
						"your family.</p>"
						+ "<p>For any further assistance, e-mail us at<br>group.servicehelpdesk@maxlifeinsurance.com</p>"
						+"<p>Warm Regards,<br><b>Max Life Insurance Company Limited</b></p>"
						+ "<div><span style='FONT-WEIGHT: bold;'>Note:</span><div style='padding-left: 28px;'><ul><li>* This is an electronically generated mail. Please do not reply to this email.</li></ul></div></div>"
						+ "</div></div></body></html>";
				
				mliEmailModel.setMailSubject("Reminder to complete your application "+mliEmailModel.getProposalNumber()+" for Max Life Group Platinum Assurance plan");
				mliEmailModel.setMailBody(custMailBody);
				mliEmailModel.setAttchDetails(null);
			}
			if (mliEmailModel.getMailUserType().equals(Constant.YBL_PAYMENT_PENDING2)) {
				String encryptedStr = AES.encrypt(mliEmailModel.getCustMobNumber()+"_"+mliEmailModel.getProposalNumber(), aesSecratKey);
				String redirectURL = ""+custScreenRdirectUrl+"/cs/yblcc/"+encryptedStr;
				logger.info("redirec url : "+redirectURL);
				custMailBody = "<html><head></head><body><div style='width: 650px;'><div style=''>"
						+ "</div><div style='width: 95%; text-align: justify;'><p>Dear "
						+ mliEmailModel.getCustomerName()
						+ ",</p><p>we request you to pay the initial Premium towards the Max Life Group Term Life Platinum Assurance application "+ mliEmailModel.getProposalNumber()+"\n"
						+ "to activate your policy.Click on <a href="+redirectURL+">click here</a> to pay.</p>"
						+"<p>We assure you that we are continuously working on creating a financially secure future for you and \n" + 
						"your family.</p>"
						+ "<p>For any further assistance, e-mail us at<br>group.servicehelpdesk@maxlifeinsurance.com</p>"
						+"<p>Warm Regards,<br><b>Max Life Insurance Company Limited</b></p>"
						+ "<div><span style='FONT-WEIGHT: bold;'>Note:</span><div style='padding-left: 28px;'><ul><li>* This is an electronically generated mail. Please do not reply to this email.</li></ul></div></div>"
						+ "</div></div></body></html>";
				
				mliEmailModel.setMailSubject("You are a few moments away from completing your application "+mliEmailModel.getProposalNumber()+" for Max Life Group Platinum Assurance plan");
				mliEmailModel.setMailBody(custMailBody);
				mliEmailModel.setAttchDetails(null);
			}
			if (mliEmailModel.getMailUserType().equals(Constant.YBL_PAYMENT_PENDING3)) {
				String encryptedStr = AES.encrypt(mliEmailModel.getCustMobNumber()+"_"+mliEmailModel.getProposalNumber(), aesSecratKey);
				String redirectURL = ""+custScreenRdirectUrl+"/cs/yblcc/"+encryptedStr;
				logger.info("redirec url : "+redirectURL);
				custMailBody = "<html><head></head><body><div style='width: 650px;'><div style=''>"
						+ "</div><div style='width: 95%; text-align: justify;'><p>Dear "
						+ mliEmailModel.getCustomerName()
						+ ",</p><p>last chance to pay the initial Premium towards the Max Life Group Term Life Platinum Assurance application "+ mliEmailModel.getProposalNumber()+"\n"
						+ "to activate your policy.Click on <a href="+redirectURL+">click here</a> to pay.</p>"
						+"<p>We assure you that we are continuously working on creating a financially secure future for you and \n" + 
						"your family.</p>"
						+ "<p>For any further assistance, e-mail us at<br>group.servicehelpdesk@maxlifeinsurance.com</p>"
						+"<p>Warm Regards,<br><b>Max Life Insurance Company Limited</b></p>"
						+ "<div><span style='FONT-WEIGHT: bold;'>Note:</span><div style='padding-left: 28px;'><ul><li>* This is an electronically generated mail. Please do not reply to this email.</li></ul></div></div>"
						+ "</div></div></body></html>";
				
				mliEmailModel.setMailSubject("Complete your application "+mliEmailModel.getProposalNumber()+" for Max Life Group Platinum Assurance plan and secure your family");
				mliEmailModel.setMailBody(custMailBody);
				mliEmailModel.setAttchDetails(null);
			}
			
			if (mliEmailModel.getMailUserType().equals(Constant.LOGIN_OTP_AT_EMAIL)) {
				custMailBody = "<html><head></head><body><div style='width: 600px;'><div style=''>"
						+ "</div><div style='width: 95%; text-align: justify;'><p>Dear "
						+ mliEmailModel.getCustomerName()
						+ ",</p><p><b>"
						+ mliEmailModel.getOtp()
						+ "</b> is your One Time Verification Code for Login.</b>"
						+ "<p>Warm Regards,<br><b>Max Life Insurance Company Limited</b></p>"
						+ "<div><span style='FONT-WEIGHT: bold;'>Note:</span><div style='padding-left: 28px;'><ul><li>* This is an electronically generated mail. Please do not reply to this email.</li></ul></div></div>"
						+ "</div></div></body></html>";
				mliEmailModel.setMailSubject(
						"Max Life GCL Secure Login (OTP)");
				mliEmailModel.setMailBody(custMailBody);
			}

			MliEmailService = emailDetailHelper.convertToModel(mliEmailModel);
			 
			ResponseEntity<String> entity = mliApiUtilService.callAPIPost(MliEmailService);
			
			String jsonObject = entity.getBody().toString();
			JSONObject providersBodyJsonObj = new JSONObject(jsonObject);
			String getData = (String) providersBodyJsonObj.getString("EmailResponse");
			JSONParser parser = new JSONParser();
			
			EmailResponse = parser.parse(MLIEmailResponse.class, getData);
			
			responseModel.setStatus(!ObjectsUtil.isNull(EmailResponse.getResponseHeader().getGeneralResponse().getStatus())? EmailResponse.getResponseHeader().getGeneralResponse().getStatus() : null);
			responseModel.setMessage(!ObjectsUtil.isNull(EmailResponse.getResponseHeader().getGeneralResponse().getDescription())? EmailResponse.getResponseHeader().getGeneralResponse().getDescription() : null);
			responseModel.setCode(!ObjectsUtil.isNull(EmailResponse.getResponseHeader().getGeneralResponse().getCode()) ? EmailResponse.getResponseHeader().getGeneralResponse().getCode() : null);
			return responseModel;

		} catch (Exception e) {
			e.printStackTrace();
			logger.info("::::::::::::::::: Error in Sending Email :::::::::::::::");
			throw new Exception(e.getMessage());
		}
	}
}