package com.mli.utils.pdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;
import org.xhtmlrenderer.pdf.PDFEncryption;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfWriter;
import com.mli.constants.Constant;
import com.mli.controller.LoginController;
import com.mli.dao.CreditCardCustomerDAO;
import com.mli.dao.CustomerDetailsDAO;
import com.mli.dao.OtpDDAO;
import com.mli.dao.PaymentDAO;
import com.mli.entity.CreditCardCustomerEntity;
import com.mli.entity.CustomerDetailsEntity;
import com.mli.entity.OTPHistoryEntity;
import com.mli.entity.PaymentEntity;
import com.mli.entity.ReflexiveAnswerEntity;
import com.mli.enums.CIRiderOption;
import com.mli.enums.LoanType;
import com.mli.enums.RelationshipWithAssured;
import com.mli.enums.RiderType;
import com.mli.enums.Status;
import com.mli.model.Covid_19Model;
import com.mli.model.CreditCardCovidModel;
import com.mli.model.CreditCardCustomerModel;
import com.mli.model.CreditCardHealthModel;
import com.mli.model.CreditCardJourneyModel;
import com.mli.model.CreditCardMandatoryModel;
import com.mli.model.CreditCardNomineeModel;
import com.mli.model.HealthAnswerModel;
import com.mli.model.ProposerDetailModel;
import com.mli.model.SFQCovidModel;
import com.mli.model.SFQCovidTestModel;
import com.mli.model.SFQCovidVaccineModel;
import com.mli.model.SFQHealthDeclarationModel;
import com.mli.model.UserDetailsModel;
import com.mli.service.ReflexiveQuestionService;
import com.mli.utils.DateUtil;
import com.mli.utils.ObjectsUtil;

/**
 * 
 * @author Haripal.Chauhan
 *PDF utility class.
 */
@Component
public class PdfGenaratorUtil {
	@Autowired
	private TemplateEngine templateEngine;

	@Value(value = "${logo.path}")
	private String logoPath;

	@Autowired
	private CustomerDetailsDAO customerDetailsDAO;

	@Autowired
	private ReflexiveQuestionService questionService;

	@Autowired
	private OtpDDAO otpDDAO;
	
	@Autowired
	private CreditCardCustomerDAO creditCardCustomerDAO;
	
	@Autowired
	private PaymentDAO paymentDAO;
	
	private static final Logger logger = Logger.getLogger(LoginController.class);
/**
 * 
 * @param templateName
 * @param map
 * @param filePath
 * @throws Exception
 * 
 */
	public void createPdf(String templateName, Map map, String filePath) throws Exception {
		Assert.notNull(templateName, "The PDF templateName can not be null");
		Context ctx = new Context();
		if (map != null) {
			Iterator itMap = map.entrySet().iterator();
			while (itMap.hasNext()) {
				Map.Entry pair = (Map.Entry) itMap.next();
				ctx.setVariable(pair.getKey().toString(), pair.getValue());
			}
		}
		PDFEncryption pdfEncryption  = new PDFEncryption();
		byte[] passInByte = null;
		try {
			 passInByte = ((String) map.get("password")).getBytes();
		} catch (Exception e) {
			logger.error("::::::::::::    Getting Error in password in createPdf  :::::::::::::::::::");
		}
		pdfEncryption.setUserPassword(passInByte);
		String processedHtml = templateEngine.process(templateName, ctx);
		FileOutputStream os = null;
		try {  
     		final File outputFile = new File(filePath);
			os = new FileOutputStream(outputFile);
			ITextRenderer renderer = new ITextRenderer();
			renderer.setPDFEncryption(pdfEncryption);
			renderer.setDocumentFromString(processedHtml);
			renderer.layout();
			renderer.createPDF(os, false);
			renderer.finishPDF();
			logger.info("PDF created successfully");
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
				/* ignore */ }
			}
		}
	}
	

/**
 * 
 * @param userDetailsModel
 * @return
 */
	@SuppressWarnings("unchecked")
	@Transactional
	public Map<String, Object> getPdfModel(UserDetailsModel userDetailsModel,String otp,String modifyTS) {
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			if (userDetailsModel == null)
				return data;
			// customer details
			logger.info("Get Customer Answer (Proposal Number)"+userDetailsModel.getProposalNumber());
			data.put("homeLoanGroupPlicyNo", userDetailsModel.getHomeLoanGroupPolicyNo());
			if (userDetailsModel.getCustomerDetails() != null) {
				data.put("sfvTimestamp", DateUtil.extractDateAsStringSlashFormate(DateUtil.toCurrentUTCTimeStamp()));
				data.put("customerFullName", (userDetailsModel.getCustomerDetails().getCustomerFirstName() + " "
						+ userDetailsModel.getCustomerDetails().getCustomerLastName()).trim());
				String loanAppNumber = userDetailsModel.getCustomerDetails().getLoanAppNumber();
				data.put("loanAppNumber",loanAppNumber);
				/*for(int count = 0; count < loanAppNumber.length(); count++){
				data.put("loanAppD" + (count + 1), String.valueOf(loanAppNumber.charAt(count)));
				}*/
				data.put("groupPolicyHolderName", userDetailsModel.getCustomerDetails().getMasterPolicyholderName());
				data.put("relationshipGpPolicyHolder",
						userDetailsModel.getCustomerDetails().getRelationshipGpPolicyHolder());
				data.put("nameOfTheLifeToBeInsured", userDetailsModel.getCustomerDetails().getCustomerFirstName()+" "+userDetailsModel.getCustomerDetails().getCustomerLastName());
				LoanType loanType = LoanType.getLoanType(userDetailsModel.getCustomerDetails().getLoanType());
//				data.put("loanType", loanType.getCode()+"-"+loanType.getText());
				if (loanType != null) {
					data.put("loanType", loanType.getText());
				}
				data.put("loanTenure", String.valueOf(userDetailsModel.getCustomerDetails().getLoanTenure().intValue()));
				data.put("schemeType", userDetailsModel.getCustomerDetails().getSchemeType());
				DecimalFormat decimalFormat = new DecimalFormat("0.00");
		        String sumAssuredAsString = null;
		        if(userDetailsModel.getCustomerDetails().getSumAssured() != null){
		        	sumAssuredAsString = decimalFormat.format(userDetailsModel.getCustomerDetails().getSumAssured());
		        }
				data.put("sumAssured", sumAssuredAsString);
				data.put("mobileNo", userDetailsModel.getCustomerDetails().getCustMobileNo().toString());
				data.put("customerDob", userDetailsModel.getCustomerDetails().getDob());
				data.put("customerEmail", userDetailsModel.getCustomerDetails().getCustEmailId());
				data.put("casID", "");
				data.put("gender", userDetailsModel.getCustomerDetails().getGender());
				data.put("status", userDetailsModel.getCustomerDetails().getStatus());
//				data.put("adhaarNumber", userDetailsModel.getCustomerDetails().getAdhaarNumber().toString());
//				data.put("password", PasswordGenerateUtil.getPDFPassword(userDetailsModel));
				
				logger.info("status : "+userDetailsModel.getCustomerDetails().getStatus());
				ProposerDetailModel proposerDetail = userDetailsModel.getCustomerDetails().getProposerDetails();
				if (proposerDetail != null) {
					data.put("proposerExit", "true");
					data.put("proDob", proposerDetail.getDateOfBirth());
					data.put("proGender", proposerDetail.getGender());
					data.put("proFullname",
							(proposerDetail.getProposerFirstName() + " " + proposerDetail.getProposerLastName())
									.trim());
					data.put("proRelationWithAssured", proposerDetail.getRelationWithAssured());
					data.put("proRelationWithProposer", proposerDetail.getRelationWithProposer());
				} else {
					data.put("proposerExit", "false");
				}

			}
			// health declaration
			if (userDetailsModel.getHealthDeclaration() != null) {
				data.put("negativeDeclaration", userDetailsModel.getHealthDeclaration().getNegativeDeclaration());
				data.put("otherInsurance", userDetailsModel.getHealthDeclaration().getOtherInsurance());
			}
			// mandatory info
			if (userDetailsModel.getMandatoryDeclaration() != null) {
				data.put("signedDate",
						DateUtil.extractDateAsString(userDetailsModel.getMandatoryDeclaration().getSignedDate()));
				data.put("place", userDetailsModel.getMandatoryDeclaration().getPlace());

				if (!CollectionUtils.isEmpty(userDetailsModel.getHealthDeclaration().getCiRiderQuestionAns())) {
					List<String> savedAns = new ArrayList<>();
					List<HealthAnswerModel> healthAnswerModelList = userDetailsModel.getHealthDeclaration()
							.getCiRiderQuestionAns();
					String questionKey = "question";
					Collections.sort(healthAnswerModelList, HealthAnswerModel.healthAnswere);
					
					for (HealthAnswerModel healthModel : healthAnswerModelList) {
						String keyOfQuestion = questionKey + healthModel.getQuestionId();
						if (healthModel.getQuestionAns().equals("Yes")) {
							data.put(keyOfQuestion, "Yes");
							savedAns.add(String.valueOf(healthModel.getQuestionId()));
						}

					}
					for (int i = 1; i < 8; i++) {
						if (!savedAns.contains(String.valueOf(i))) {
							String keyOfQuestion = questionKey + i;
							data.put(keyOfQuestion, "No");
						}
					}
				}
			}
			
			// Beneficiary details
			if (userDetailsModel.getNomineeDetails() != null) {
				StringBuilder fullName = new StringBuilder();
				if (userDetailsModel.getNomineeDetails().getNomineeFirstName() != null) {
					fullName.append(userDetailsModel.getNomineeDetails().getNomineeFirstName());
				}
				if (userDetailsModel.getNomineeDetails().getNomineeLastName() != null) {
					fullName.append(" ");
					fullName.append(userDetailsModel.getNomineeDetails().getNomineeLastName());
				}
				data.put("beneficiaryName", fullName.toString());
				data.put("beneficiaryDob", userDetailsModel.getNomineeDetails().getDateOfBirth());
				if(userDetailsModel.getNomineeDetails().getRelationWitHAssured().equalsIgnoreCase(RelationshipWithAssured.OTHERS.getLabel())){
					data.put("beneficiaryRelationWithAssured",
							userDetailsModel.getNomineeDetails().getRelationWithNominee());
				}else{
					data.put("beneficiaryRelationWithAssured",
							userDetailsModel.getNomineeDetails().getRelationWitHAssured());
				}
			
				data.put("beneficiaryGender", userDetailsModel.getNomineeDetails().getGender());
			}
			// Appointee details
			if (userDetailsModel.getNomineeDetails() != null
					&& userDetailsModel.getNomineeDetails().getAppointeeDetails() != null) {
				data.put("appointeeExist", "true");
				data.put("appointeeName", !ObjectsUtil.isNull(userDetailsModel.getNomineeDetails().getAppointeeDetails().getAppointeeFirstName()) ? userDetailsModel.getNomineeDetails().getAppointeeDetails().getAppointeeFirstName()+" "+userDetailsModel.getNomineeDetails().getAppointeeDetails().getAppointeeLastName() : "--");
				if(userDetailsModel.getNomineeDetails().getAppointeeDetails().getRelationWithAssured().equalsIgnoreCase(RelationshipWithAssured.OTHERS.getLabel())){
					data.put("appointeeRelationWithBeneficiary",
							!ObjectsUtil.isNull(userDetailsModel.getNomineeDetails().getAppointeeDetails().getRelationWithAppointee()) ? userDetailsModel.getNomineeDetails().getAppointeeDetails().getRelationWithAppointee() : "--");
				}else{
					data.put("appointeeRelationWithBeneficiary",
							!ObjectsUtil.isNull(userDetailsModel.getNomineeDetails().getAppointeeDetails().getRelationWithAssured())? userDetailsModel.getNomineeDetails().getAppointeeDetails().getRelationWithAssured() : "--");	
				}
				data.put("appointeeGender", !ObjectsUtil.isNull(userDetailsModel.getNomineeDetails().getAppointeeDetails().getGender()) ? userDetailsModel.getNomineeDetails().getAppointeeDetails().getGender() : "--");
				data.put("appointeeDob", !ObjectsUtil.isNull(userDetailsModel.getNomineeDetails().getAppointeeDetails().getDateOfBirth()) ? userDetailsModel.getNomineeDetails().getAppointeeDetails().getDateOfBirth() : "--");
			} else {
				data.put("appointeeExist", "false");
			}
			if(userDetailsModel.getHealthDeclaration() != null) {
				data.put("applicationNumber", userDetailsModel.getHealthDeclaration().getApplicationNumber());
			}
			CustomerDetailsEntity customerDetailsEntity = customerDetailsDAO
					.findByProposalNumber(userDetailsModel.getProposalNumber());
			List<Map<String, Object>> savedAnswers = new ArrayList<>();
			if (customerDetailsEntity != null) {
				List<ReflexiveAnswerEntity> answerEntities = customerDetailsEntity.getAnswerEntity();
				if (answerEntities != null) {
					savedAnswers = (List<Map<String, Object>>) questionService
							.convertToModelSavedAnswer(answerEntities);
				}
			}
			logger.info("savedAnswers.size() : "+savedAnswers.size());
			data.put("savedAnswer", savedAnswers);
			// plz don't remove this line
//			data.put("occupation", customerDetailsEntity.getOccupation());
			data.put("nationality", customerDetailsEntity.getNationality());
			data.put("otherPlace", customerDetailsEntity.getOtherPalce());
			
			String tempHypertenstion = "";
			String tempDiabetes = "";
			String tempAsthama = "";
			for (Map<String, Object> answer : savedAnswers) {
				if (Constant.DISEASE.equals((String) answer.get("type"))) {
					String diseaseType = (String) answer.get("subType");
					if (diseaseType.equals(Constant.HYPERTENSION)) {
						tempHypertenstion = Constant.HYPERTENSION;
					}
					if (diseaseType.equals(Constant.DIABETES)) {
						tempDiabetes = Constant.DIABETES;
					}
					if (diseaseType.equals(Constant.ASTHMA)) {
						tempAsthama = Constant.ASTHMA;
					}
				}
			}
			data.put("tempHypertenstion", tempHypertenstion);
			data.put("tempAsthama", tempAsthama);
			data.put("tempDiabetes", tempDiabetes);
			
			if (userDetailsModel.getCustomerDetails() != null) {
				List<OTPHistoryEntity> otpDetails = otpDDAO.findByContNoAndStatus(
						userDetailsModel.getCustomerDetails().getCustMobileNo(), Status.CURRENT_OTP);
				OTPHistoryEntity otpDetail = new OTPHistoryEntity();
				// Get last object of otpDetails which is used to verify
				if (otpDetails != null && !otpDetails.isEmpty()) {
					otpDetail = otpDetails.get(otpDetails.size()-1);
					if (otpDetail != null) {
						data.put("otp", otpDetail.getOtp());
						data.put("otpTimestamp",
								otpDetail.getModifiedOn() != null
										? DateUtil.extractDateWithTSAsStringSlashFormate(otpDetail.getModifiedOn())
										: "");
					}
				}
			}
			data.put("otp", otp);
			data.put("otpTimestamp",modifyTS);
			data.put("ciriderType", userDetailsModel.getCustomerDetails().getBaseOrBaseWithCI());
			
			if(RiderType.BASE_CI_RIDER.getLabel().equalsIgnoreCase(userDetailsModel.getCustomerDetails().getBaseOrBaseWithCI())) {
				data.put("percentageOfSumAssured", userDetailsModel.getCustomerDetails().getPercentageOfSumAssured());
				data.put("ciRiderSumAssured", userDetailsModel.getCustomerDetails().getCiRiderSumAssured());
				data.put("ciOption", userDetailsModel.getCustomerDetails().getCiOption());
				data.put("ciTenureYears", userDetailsModel.getCustomerDetails().getCiTenureYears());
				data.put("ciHealthDecsAns",userDetailsModel.getHealthDeclaration().getCiHealthDecsAns());
			}
			
			
				data.put("financePrimum", userDetailsModel.getCustomerDetails().getFinancePremium());
				data.put("street", userDetailsModel.getCustomerDetails().getStreet());
				data.put("address1", userDetailsModel.getCustomerDetails().getAddress1());
				data.put("address2", userDetailsModel.getCustomerDetails().getAddress2());
				data.put("address3", userDetailsModel.getCustomerDetails().getAddress3());
				data.put("pincode", userDetailsModel.getCustomerDetails().getPinCode());
				data.put("town", userDetailsModel.getCustomerDetails().getTown());
				data.put("state", userDetailsModel.getCustomerDetails().getState());
				data.put("country", userDetailsModel.getCustomerDetails().getCountry());
				if(userDetailsModel.getHealthDeclaration().getIsHealthDeclaration()) {
					data.put("otherHealth", userDetailsModel.getHealthDeclaration().getOtherHealthInfo());
				}
				
		    if(!ObjectUtils.isEmpty(userDetailsModel.getHealthDeclaration().getCovid_19Details())) {
		    	Covid_19Model covid_19Model = userDetailsModel.getHealthDeclaration().getCovid_19Details();
		    	if(!ObjectUtils.isEmpty(covid_19Model)) {
		    		data.put("isCovidActive", "Yes");
		    		Map<String,String> covidMap = new HashMap<String, String>();
		    		covidMap.put("firstAnswer", covid_19Model.getCovidAnsOne());
		    		covidMap.put("secondAnswer", covid_19Model.getCovidAnsTwo());
		    		covidMap.put("thirdAnswer_a", covid_19Model.getCovidAnsThree_a());
		    		covidMap.put("thirdAnswer_b", covid_19Model.getCovidAnsThree_b());
		    		covidMap.put("fourthAnswer", covid_19Model.getCovidAnsFour());
		    		covidMap.put("fifthAnswer", covid_19Model.getCovidAnsFive());
		    		covidMap.put("covidDeclaration", covid_19Model.getCovidDeclaration());
		    		data.put("covid", covidMap);
		    	}else {
		    		data.put("isCovidActive", "No");
		    	}
		    	
		    }else {
		    	data.put("isCovidActive", "No");
		    }
			logger.info("LOGO PATH:::::::::"+logoPath);
			data.put("logoPath", logoPath);
		} catch (Exception e) {
			logger.error("Error while creating PDF :::::" + e.getMessage());
			e.printStackTrace();
		}

		return data;
	}
	
	/**
	 * Merge list of InputStream into one PDF
	 * 
	 * @param list
	 * @param outputStream
	 * @throws DocumentException
	 * @throws IOException
	 */
	public boolean doMerge(List<InputStream> list,FileOutputStream outputStream)
			throws DocumentException, IOException {
		try {
			Document document = new Document();
			PdfWriter writer = PdfWriter.getInstance(document, outputStream);
			document.open();
			PdfContentByte cb = writer.getDirectContent();
			for (InputStream in : list) {
				PdfReader reader = new PdfReader(in);
				reader.unethicalreading = true;
				for (int i = 1; i <= reader.getNumberOfPages(); i++) {
					document.newPage();
					// import the page from source pdf
					PdfImportedPage page = writer.getImportedPage(reader, i);
					// add the page to the destination pdf
					cb.addTemplate(page, 0, 0);
				}
			}
			outputStream.flush();
			document.close();
			outputStream.close();
			return true;
		} catch (Exception exception) {
			exception.printStackTrace();
			return false;
		}
	}

	/**
	 * build pdf data for ybl credit card customer into map
	 * @param creditCardJourneyModel
	 * @param otp
	 * @param modifyTS
	 * @return
	 * @author rajkumar
	 */
	@Transactional
	public Map<String, Object> getYBLCCPdfModel(CreditCardJourneyModel creditCardJourneyModel, String modifyTS) {
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			if (ObjectUtils.isEmpty(creditCardJourneyModel))
				return data;
			
			logger.info("Get Customer Answer (Proposal Number)"+creditCardJourneyModel.getProposalNumber());
			CreditCardCustomerModel creditCardCustomerModel = creditCardJourneyModel.getCreditCardCustomerModel();
			CreditCardNomineeModel creditCardNomineeModel = creditCardJourneyModel.getCreditCardNomineeModel();
			CreditCardHealthModel creditCardHealthModel = creditCardJourneyModel.getCreditCardHealthModel();
			CreditCardMandatoryModel creditCardMandatoryModel = creditCardJourneyModel.getCreditCardMandatoryModel();
			if (!ObjectUtils.isEmpty(creditCardCustomerModel)) {
				CreditCardCustomerEntity creditCardCustomerEntity = creditCardCustomerDAO.findByProposalNumber(creditCardCustomerModel.getProposalNumber());
				if(!ObjectUtils.isEmpty(creditCardCustomerEntity)) {
					PaymentEntity paymentEntity = paymentDAO.findByCreditCardCustomerIdAndStatus(creditCardCustomerEntity.getCreditCardCustomerId(), "SUCCESS");
				    if(!ObjectUtils.isEmpty(paymentEntity)) {
				    	data.put("premiumPaid", paymentEntity.getAmount());
				    	data.put("transactionRefNo", paymentEntity.getTxnId());
				    	data.put("applicationNo", creditCardCustomerModel.getProposalNumber());
				    	data.put("dateofPayment", paymentEntity.getTxnDate());
				    	if(!StringUtils.isEmpty(paymentEntity.getBankCode())){
							if("1340".equalsIgnoreCase(paymentEntity.getBankCode())) {
								data.put("paymentMode", "Yes Bank Credit Card");
							} else if("1350".equalsIgnoreCase(paymentEntity.getBankCode())) {
								data.put("paymentMode", "Yes Bank Debit Card");
							} else {
								data.put("paymentMode", "--");
							}
						}else {
							data.put("paymentMode", "--");
						}
				    }
				    if(!StringUtils.isEmpty(creditCardCustomerEntity.getCustFirstDeclaration())){
				    	data.put("ccDeclaration", "Yes");
				    	data.put("custFirstDeclaration", creditCardCustomerEntity.getCustFirstDeclaration());
				    }
				    if(!StringUtils.isEmpty(creditCardCustomerEntity.getCustSecondDeclaration())){
				    	data.put("voluntaryDeclaration", "Yes");
				    	data.put("custSecondDeclaration", creditCardCustomerEntity.getCustSecondDeclaration());
				    }
				    if(!StringUtils.isEmpty(creditCardCustomerEntity.getCustFirstDeclarationValue())){
				    	data.put("custFirstDeclarationValue", creditCardCustomerEntity.getCustFirstDeclarationValue());
				    }
				    if(!StringUtils.isEmpty(creditCardCustomerEntity.getCustSecondDeclarationValue())){
				    	data.put("custSecondDeclarationValue", creditCardCustomerEntity.getCustSecondDeclarationValue());
				    }
				    data.put("otp",creditCardCustomerEntity.getVerifiedOtp());
				    data.put("otpTimestamp", modifyTS);
				}
				data.put("sfvTimestamp", DateUtil.extractDateAsStringSlashFormate(DateUtil.toCurrentUTCTimeStamp()));
				data.put("customerFullName", (creditCardCustomerModel.getFirstName() + " "+ creditCardCustomerModel.getLastName()).trim());
				data.put("customerId",creditCardCustomerModel.getCustomerId());
				data.put("groupPolicyHolderName", creditCardCustomerModel.getMasterPolicyHolderName());
				data.put("mobileNo", creditCardCustomerModel.getPhone().toString());
				data.put("customerDob", creditCardCustomerModel.getDob());
				data.put("customerEmail", creditCardCustomerModel.getEmail());
				data.put("gender", creditCardCustomerModel.getGender());
				data.put("appStatus", creditCardCustomerModel.getAppStatus());
				data.put("cardSegment", creditCardCustomerModel.getCardSegment());
				data.put("coverage", creditCardCustomerModel.getCoverage());
				data.put("premium", creditCardCustomerModel.getPremium());
				data.put("proposalNumber", creditCardCustomerModel.getProposalNumber());
				data.put("paymentStatus", creditCardCustomerModel.getPaymentStatus());
			}
			if(!ObjectUtils.isEmpty(creditCardNomineeModel)) {
				data.put("nomineeName",creditCardNomineeModel.getNomineeFirstName()+" "+creditCardNomineeModel.getNomineeLastName());
				data.put("nomineeDob",creditCardNomineeModel.getNomineeDob());
				data.put("nomineeGender",creditCardNomineeModel.getNomineeGender());
				data.put("relationshipWithAssured",creditCardNomineeModel.getRelationshipWithAssured());
				data.put("appointeetName", !StringUtils.isEmpty(creditCardNomineeModel.getAppointeeFirstName()) ? creditCardNomineeModel.getAppointeeFirstName()+" "+creditCardNomineeModel.getAppointeeLastName() : "--");
				data.put("appointeeDob", !StringUtils.isEmpty(creditCardNomineeModel.getAppointeeDob()) ? creditCardNomineeModel.getAppointeeDob() : "--" );
				data.put("appointeeGender",!StringUtils.isEmpty(creditCardNomineeModel.getAppointeeGender()) ? creditCardNomineeModel.getAppointeeGender() : "--");
				data.put("relationWithNominee",!StringUtils.isEmpty(creditCardNomineeModel.getRelationWithNominee()) ? creditCardNomineeModel.getRelationWithNominee() : "--");
				data.put("isAppointee", creditCardNomineeModel.isAppointee());
			}
			if(!ObjectUtils.isEmpty(creditCardHealthModel)) {
				data.put("healthFirstAnswer", creditCardHealthModel.getHealthFirstAnswer());
				data.put("healthSecondAnswer", creditCardHealthModel.getHealthSecondAnswer());
				data.put("healthThirdAnswer", creditCardHealthModel.getHealthThirdAnswer());
				data.put("isSmoker", creditCardHealthModel.getIsSmoker());
				data.put("smokePerDay", creditCardHealthModel.getSmokePerDay());
				data.put("declaration", creditCardHealthModel.getDeclaration());
				data.put("appNumber", creditCardHealthModel.getAppNumber());
				CreditCardCovidModel creditCardCovidModel = creditCardHealthModel.getCreditCardCovidModel();
				if(!ObjectUtils.isEmpty(creditCardCovidModel)) {
					data.put("isCovidActive", "Yes");
		    		Map<String,String> covidMap = new HashMap<String, String>();
		    		covidMap.put("covidDeclaration", "Yes".equalsIgnoreCase(creditCardCovidModel.getCovidDeclaration()) ? "Agree" : "Disagree");
		    		covidMap.put("comment", creditCardCovidModel.getComment());
		    		data.put("covid", covidMap);
		    	}else {
		    		data.put("isCovidActive", "No");
		    	}
			}
			if(!ObjectUtils.isEmpty(creditCardMandatoryModel)) {
				data.put("isMandatoryDeclaration", creditCardMandatoryModel.getIsMandatoryDeclaration());
				data.put("place", creditCardMandatoryModel.getPlace());
				data.put("signedDate", DateUtil.extractDateAsString(DateUtil.toCurrentUTCTimeStamp()));
			}
			logger.info("LOGO PATH:::::::::"+logoPath);
			data.put("logoPath", logoPath);
		} catch (Exception e) {
			logger.error("Error while creating PDF :::::" + e.getMessage());
			e.printStackTrace();
		}
		return data;
	}
	
	
	@SuppressWarnings("unchecked")
	@Transactional
	public Map<String, Object> getSFQPdfModel(UserDetailsModel userDetailsModel, String otp, String modifyTS) {
		Map<String, Object> data = new HashMap<String, Object>();
		try {
			if (userDetailsModel == null)
				return data;
			// customer details
			logger.info("Get Customer Answer (Proposal Number)" + userDetailsModel.getProposalNumber());
			data.put("homeLoanGroupPlicyNo", userDetailsModel.getHomeLoanGroupPolicyNo());
			if (userDetailsModel.getCustomerDetails() != null) {
				data.put("sfvTimestamp", DateUtil.extractDateAsStringSlashFormate(DateUtil.toCurrentUTCTimeStamp()));
				data.put("customerFullName", (userDetailsModel.getCustomerDetails().getCustomerFirstName() + " "
						+ userDetailsModel.getCustomerDetails().getCustomerLastName()).trim());
				String loanAppNumber = userDetailsModel.getCustomerDetails().getLoanAppNumber();
				data.put("loanAppNumber", loanAppNumber);
				data.put("groupPolicyHolderName", userDetailsModel.getCustomerDetails().getMasterPolicyholderName());
				data.put("relationshipGpPolicyHolder",
						userDetailsModel.getCustomerDetails().getRelationshipGpPolicyHolder());
				data.put("nameOfTheLifeToBeInsured", userDetailsModel.getCustomerDetails().getCustomerFirstName() + " "
						+ userDetailsModel.getCustomerDetails().getCustomerLastName());
				LoanType loanType = LoanType.getLoanType(userDetailsModel.getCustomerDetails().getLoanType());
				if (loanType != null) {
					data.put("loanType", loanType.getText());
				}
				data.put("loanTenure",
						String.valueOf(userDetailsModel.getCustomerDetails().getLoanTenure().intValue()));
				data.put("schemeType", userDetailsModel.getCustomerDetails().getSchemeType());
				DecimalFormat decimalFormat = new DecimalFormat("0.00");
				String sumAssuredAsString = null;
				if (userDetailsModel.getCustomerDetails().getSumAssured() != null) {
					sumAssuredAsString = decimalFormat.format(userDetailsModel.getCustomerDetails().getSumAssured());
				}
				data.put("sumAssured", sumAssuredAsString);
				data.put("mobileNo", userDetailsModel.getCustomerDetails().getCustMobileNo().toString());
				data.put("customerDob", userDetailsModel.getCustomerDetails().getDob());
				data.put("customerEmail", userDetailsModel.getCustomerDetails().getCustEmailId());
				data.put("casID", "");
				data.put("gender", userDetailsModel.getCustomerDetails().getGender());
				data.put("status", userDetailsModel.getCustomerDetails().getStatus());

				logger.info("status : " + userDetailsModel.getCustomerDetails().getStatus());
				ProposerDetailModel proposerDetail = userDetailsModel.getCustomerDetails().getProposerDetails();
				if (proposerDetail != null) {
					data.put("proposerExit", "true");
					data.put("proDob", proposerDetail.getDateOfBirth());
					data.put("proGender", proposerDetail.getGender());
					data.put("proFullname",
							(proposerDetail.getProposerFirstName() + " " + proposerDetail.getProposerLastName())
									.trim());
					data.put("proRelationWithAssured", proposerDetail.getRelationWithAssured());
					data.put("proRelationWithProposer", proposerDetail.getRelationWithProposer());
				} else {
					data.put("proposerExit", "false");
				}

			}

			// mandatory info
			if (userDetailsModel.getMandatoryDeclaration() != null) {
				data.put("signedDate",
						DateUtil.extractDateAsString(userDetailsModel.getMandatoryDeclaration().getSignedDate()));
				data.put("place", userDetailsModel.getMandatoryDeclaration().getPlace());
			}

			// Beneficiary details
			if (userDetailsModel.getNomineeDetails() != null) {
				StringBuilder fullName = new StringBuilder();
				if (userDetailsModel.getNomineeDetails().getNomineeFirstName() != null) {
					fullName.append(userDetailsModel.getNomineeDetails().getNomineeFirstName());
				}
				if (userDetailsModel.getNomineeDetails().getNomineeLastName() != null) {
					fullName.append(" ");
					fullName.append(userDetailsModel.getNomineeDetails().getNomineeLastName());
				}
				data.put("beneficiaryName", fullName.toString());
				data.put("beneficiaryDob", userDetailsModel.getNomineeDetails().getDateOfBirth());
				if (userDetailsModel.getNomineeDetails().getRelationWitHAssured()
						.equalsIgnoreCase(RelationshipWithAssured.OTHERS.getLabel())) {
					data.put("beneficiaryRelationWithAssured",
							userDetailsModel.getNomineeDetails().getRelationWithNominee());
				} else {
					data.put("beneficiaryRelationWithAssured",
							userDetailsModel.getNomineeDetails().getRelationWitHAssured());
				}

				data.put("beneficiaryGender", userDetailsModel.getNomineeDetails().getGender());
			}
			// Appointee details
			if (userDetailsModel.getNomineeDetails() != null
					&& userDetailsModel.getNomineeDetails().getAppointeeDetails() != null) {
				data.put("appointeeExist", "true");
				data.put("appointeeName", !ObjectsUtil
						.isNull(userDetailsModel.getNomineeDetails().getAppointeeDetails().getAppointeeFirstName())
								? userDetailsModel.getNomineeDetails().getAppointeeDetails().getAppointeeFirstName()
										+ " "
										+ userDetailsModel.getNomineeDetails().getAppointeeDetails()
												.getAppointeeLastName()
								: "--");
				if (userDetailsModel.getNomineeDetails().getAppointeeDetails().getRelationWithAssured()
						.equalsIgnoreCase(RelationshipWithAssured.OTHERS.getLabel())) {
					data.put("appointeeRelationWithBeneficiary",
							!ObjectsUtil.isNull(userDetailsModel.getNomineeDetails().getAppointeeDetails()
									.getRelationWithAppointee())
											? userDetailsModel.getNomineeDetails().getAppointeeDetails()
													.getRelationWithAppointee()
											: "--");
				} else {
					data.put("appointeeRelationWithBeneficiary",
							!ObjectsUtil.isNull(
									userDetailsModel.getNomineeDetails().getAppointeeDetails().getRelationWithAssured())
											? userDetailsModel.getNomineeDetails().getAppointeeDetails()
													.getRelationWithAssured()
											: "--");
				}
				data.put("appointeeGender",
						!ObjectsUtil.isNull(userDetailsModel.getNomineeDetails().getAppointeeDetails().getGender())
								? userDetailsModel.getNomineeDetails().getAppointeeDetails().getGender()
								: "--");
				data.put("appointeeDob",
						!ObjectsUtil.isNull(userDetailsModel.getNomineeDetails().getAppointeeDetails().getDateOfBirth())
								? userDetailsModel.getNomineeDetails().getAppointeeDetails().getDateOfBirth()
								: "--");
			} else {
				data.put("appointeeExist", "false");
			}
			if (userDetailsModel.getHealthDeclaration() != null) {
				data.put("applicationNumber", userDetailsModel.getHealthDeclaration().getApplicationNumber());
			}

			if (userDetailsModel.getCustomerDetails() != null) {
				List<OTPHistoryEntity> otpDetails = otpDDAO.findByContNoAndStatus(
						userDetailsModel.getCustomerDetails().getCustMobileNo(), Status.CURRENT_OTP);
				OTPHistoryEntity otpDetail = new OTPHistoryEntity();
				// Get last object of otpDetails which is used to verify
				if (otpDetails != null && !otpDetails.isEmpty()) {
					otpDetail = otpDetails.get(otpDetails.size() - 1);
					if (otpDetail != null) {
						data.put("otp", otpDetail.getOtp());
						data.put("otpTimestamp",
								otpDetail.getModifiedOn() != null
										? DateUtil.extractDateWithTSAsStringSlashFormate(otpDetail.getModifiedOn())
										: "");
					}
				}
			}
			data.put("otp", otp);
			data.put("otpTimestamp", modifyTS);
			data.put("ciriderType", userDetailsModel.getCustomerDetails().getBaseOrBaseWithCI());

			if (RiderType.BASE_CI_RIDER.getLabel()
					.equalsIgnoreCase(userDetailsModel.getCustomerDetails().getBaseOrBaseWithCI())) {
				data.put("percentageOfSumAssured", userDetailsModel.getCustomerDetails().getPercentageOfSumAssured());
				data.put("ciRiderSumAssured", userDetailsModel.getCustomerDetails().getCiRiderSumAssured());
				data.put("ciOption", userDetailsModel.getCustomerDetails().getCiOption());
				data.put("ciTenureYears", userDetailsModel.getCustomerDetails().getCiTenureYears());
			}

			data.put("financePrimum", userDetailsModel.getCustomerDetails().getFinancePremium());
			data.put("street", userDetailsModel.getCustomerDetails().getStreet());
			data.put("address1", userDetailsModel.getCustomerDetails().getAddress1());
			data.put("address2", userDetailsModel.getCustomerDetails().getAddress2());
			data.put("address3", userDetailsModel.getCustomerDetails().getAddress3());
			data.put("pincode", userDetailsModel.getCustomerDetails().getPinCode());
			data.put("town", userDetailsModel.getCustomerDetails().getTown());
			data.put("state", userDetailsModel.getCustomerDetails().getState());
			data.put("country", userDetailsModel.getCustomerDetails().getCountry());
			CustomerDetailsEntity customerDetailsEntity = customerDetailsDAO.findByProposalNumber(userDetailsModel.getProposalNumber());
			data.put("nationality", customerDetailsEntity.getNationality());
			data.put("otherPlace", customerDetailsEntity.getOtherPalce());
			
			if(!ObjectUtils.isEmpty(userDetailsModel.getSfqHealthDeclaration())){
				SFQHealthDeclarationModel sfqHealthDeclarationModel = userDetailsModel.getSfqHealthDeclaration();
				data.put("height", sfqHealthDeclarationModel.getHeight());
				data.put("weight", sfqHealthDeclarationModel.getWeight());
				data.put("sfqHealthFirstAnswer", sfqHealthDeclarationModel.getHealthFirstAnswer());
				data.put("sfqHealthSecondAnswer", sfqHealthDeclarationModel.getHealthSecondAnswer());
				data.put("sfqHealthThirdAnswer", sfqHealthDeclarationModel.getHealthThirdAnswer());
				data.put("sfqHealthFourthAnswer", sfqHealthDeclarationModel.getHealthFourthAnswer());
				data.put("sfqHealthFifthAnswer", sfqHealthDeclarationModel.getHealthFifthAnswer());
				data.put("sfqHealthSixthAnswer", sfqHealthDeclarationModel.getHealthSixthAnswer());
				data.put("smoker", sfqHealthDeclarationModel.isSmoker());
				data.put("smokePerDay", sfqHealthDeclarationModel.getSmokePerDay());
				if(!ObjectUtils.isEmpty(sfqHealthDeclarationModel.getSfqCovidModel())) {
					SFQCovidModel sfqCovidModel = sfqHealthDeclarationModel.getSfqCovidModel();
					if(sfqCovidModel != null) {
						data.put("isCovidActive", "Yes");
						data.put("covidFirstAnswer",sfqCovidModel.getCovidFirstAnswer());
						data.put("covidSecondAnswer",sfqCovidModel.getCovidSecondAnswer());
						data.put("covidThirdAnswer_a",sfqCovidModel.getCovidThirdAnswer_a());
						data.put("covidThirdAnswer_b",sfqCovidModel.getCovidThirdAnswer_b());
						SFQCovidTestModel sfqCovidTestModel = sfqCovidModel.getSfqCovidTestModel();
						if(sfqCovidTestModel != null) {
							data.put("testCovidFirstAnswer", sfqCovidTestModel.getCovidFirstAnswer());
							data.put("testCovidSecondAnswer", sfqCovidTestModel.getCovidSecondAnswer());
							data.put("testCovidThirdAnswer", sfqCovidTestModel.getCovidThirdAnswer());
							data.put("testCovidFourthAnswer", sfqCovidTestModel.getCovidFourthAnswer());
						}
						SFQCovidVaccineModel sfqCovidVaccineModel = sfqCovidModel.getSfqCovidVaccineModel();
						if(sfqCovidVaccineModel != null) {
							data.put("isVaccinated", sfqCovidVaccineModel.isVaccinated());
							data.put("firstDoseDate", sfqCovidVaccineModel.getFirstDoseDate());
							data.put("secondDoseDate", sfqCovidVaccineModel.getSecondDoseDate());
							data.put("vaccineName", sfqCovidVaccineModel.getVaccineName());
							data.put("declaration", sfqCovidVaccineModel.getDeclaration());
						}
					}else {
						data.put("isCovidActive", "No");
					}
				}else {
					data.put("isCovidActive", "No");
				}
			}
			logger.info("LOGO PATH:::::::::" + logoPath);
			data.put("logoPath", logoPath);
		} catch (Exception e) {
			logger.error("Error while creating PDF :::::" + e.getMessage());
			e.printStackTrace();
		}

		return data;
	}
	
}
