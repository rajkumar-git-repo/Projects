package com.mli.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.mli.constants.Constant;
import com.mli.constants.DocType;
import com.mli.constants.FileExtention;
import com.mli.constants.MLIMessageConstants;
import com.mli.dao.CreditCardCovidDAO;
import com.mli.dao.CreditCardCustomerDAO;
import com.mli.dao.CreditCardHealthDAO;
import com.mli.dao.CreditCardMandatoryDAO;
import com.mli.dao.CreditCardNomineeDAO;
import com.mli.dao.PaymentDAO;
import com.mli.dao.ProposalNumberDAO;
import com.mli.dao.SellerDAO;
import com.mli.entity.AdminProductConfigEntity;
import com.mli.entity.Covid19Entity;
import com.mli.entity.CreditCardCovidEntity;
import com.mli.entity.CreditCardCustomerEntity;
import com.mli.entity.CreditCardHealthEntity;
import com.mli.entity.CreditCardMandatoryEntity;
import com.mli.entity.CreditCardNomineeEntity;
import com.mli.entity.CronJobEntity;
import com.mli.entity.CustomerDetailsEntity;
import com.mli.entity.HealthDeclarationEntity;
import com.mli.entity.PaymentEntity;
import com.mli.entity.ProposalNumberEntity;
import com.mli.entity.ReflexiveAnswerEntity;
import com.mli.entity.SellerDetailEntity;
import com.mli.enums.CardType;
import com.mli.enums.CronJobType;
import com.mli.enums.MasterPolicyHolderName;
import com.mli.enums.OTPType;
import com.mli.enums.OTPUserType;
import com.mli.enums.RelationshipWithAssured;
import com.mli.enums.Status;
import com.mli.exception.AgeException;
import com.mli.filemaster.FileUtilityModel;
import com.mli.filemaster.SchemeDirDao;
import com.mli.filemaster.SchemeDirEntity;
import com.mli.helper.CustomerDetailsHelper;
import com.mli.helper.FileUtilityConverter;
import com.mli.modal.email.EmailModel;
import com.mli.modal.email.MliEmailServiceModel;
import com.mli.model.CardTypeModel;
import com.mli.model.CreditCardCovidModel;
import com.mli.model.CreditCardCustomerModel;
import com.mli.model.CreditCardHealthModel;
import com.mli.model.CreditCardJourneyModel;
import com.mli.model.CreditCardMandatoryModel;
import com.mli.model.CreditCardNomineeModel;
import com.mli.model.CustomerDeclarationModel;
import com.mli.model.CustomerDetailsModel;
import com.mli.model.CustomerUpdateModel;
import com.mli.model.PageInfoModel;
import com.mli.model.UserDetailsModel;
import com.mli.model.response.ResponseModel;
import com.mli.model.response.SaveDetailsResponse;
import com.mli.model.response.SaveUpdateResponse;
import com.mli.model.sms.SMSResponseModel;
import com.mli.service.AdminProductConfigService;
import com.mli.service.CreditCardJourneyService;
import com.mli.service.CustomerDetailService;
import com.mli.service.MliEmailService;
import com.mli.service.SendMLISMSService;
import com.mli.utils.AES;
import com.mli.utils.CustomParameterizedException;
import com.mli.utils.DateUtil;
import com.mli.utils.NumberParser;
import com.mli.utils.ObjectsUtil;
import com.mli.utils.PageableUtil;
import com.mli.utils.ZipUtils;
import com.mli.utils.aws.AwsFileUtility;
import com.mli.utils.excel.ExcelGeneratorUtility;
import com.mli.utils.pdf.PdfGenaratorUtil;

@Service
public class CreditCardJourneyServiceImpl implements CreditCardJourneyService{

	private static final Logger logger = Logger.getLogger(CreditCardJourneyServiceImpl.class);
	
	@Autowired
	private SellerDAO sellerDAO;
	
	@Autowired
	private CustomerDetailService customerDetailService;
	
	@Autowired
	private CreditCardCustomerDAO creditCardCustomerDAO;
	
	@Autowired
    private CreditCardNomineeDAO creditCardNomineeDAO;
	
	@Autowired
	private CreditCardHealthDAO creditCardHealthDAO;
	
	@Autowired
	private CreditCardCovidDAO creditCardCovidDAO;
	
	@Autowired
	private AdminProductConfigService adminProductConfigService;
	
	@Autowired
	private ProposalNumberDAO proposalNumberDAO;
	
	@Autowired
	private CreditCardMandatoryDAO creditCardMandatoryDAO;
	
	@Autowired
	private SendMLISMSService sendMLISMSService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private MliEmailService mliEmailService;
	
	@Value("${mli.aes.encryption.key}")
	private String aesSecratKey;
	
	@Autowired
	private PdfGenaratorUtil pdfGenaratorUtil;
	
	@Value("#{'${doc.temp}'}")
	private String tempFilePath;

	@Autowired
	private AwsFileUtility awsFileUtility;
	
	@Autowired
	private TaskExecutor taskExecutor;
	
	@Autowired
	private SchemeDirDao schemeDirDao;
	
	@Autowired
	private PaymentDAO paymentDAO;
	
	@Autowired
	private ExcelGeneratorUtility excelUtility;

	@Value("${mli.download.customer.excel}")
	private String customerExcelPath;
	
	/**
	 * save creditCard customer
	 * @param creditCardJourneyModel
	 * @return
	 * @author rajkumar
	 */
	@Override
	@Transactional
	public ResponseModel<CreditCardJourneyModel> saveCreditCardJourneyDetails(CreditCardJourneyModel creditCardJourneyModel){
		logger.info("Enter into CreditCardJourneyServiceImpl to save customer credit card journey....");
		ResponseModel<CreditCardJourneyModel> responseModel = new ResponseModel<CreditCardJourneyModel>();
		try {
			boolean isUpdate = false;
			String proposalNumber = "";
			CreditCardCustomerEntity creditCardCustomerEntity = null;
			if(!StringUtils.isEmpty(creditCardJourneyModel.getProposalNumber())) {
				creditCardCustomerEntity = creditCardCustomerDAO.findByProposalNumber(creditCardJourneyModel.getProposalNumber());
			    if(ObjectUtils.isEmpty(creditCardCustomerEntity)) {
			    	responseModel.setMessage(MLIMessageConstants.INVALID_PROPOSAL);
					responseModel.setStatus(Constant.FAILURE);
					return responseModel;
			    }
			    isUpdate = true;
			}else {
				creditCardCustomerEntity = new CreditCardCustomerEntity();
				proposalNumber = genearteYBLCCNumber();
			}
			SellerDetailEntity sellerDetailEntity = sellerDAO.findByContactNo(creditCardJourneyModel.getSellerContNumber());
			boolean isCorrectData = Boolean.FALSE;
			if (!ObjectUtils.isEmpty(sellerDetailEntity)) {
				if (sellerDetailEntity.getStatus() != null && sellerDetailEntity.getStatus().equalsIgnoreCase(Constant.SELLER_DEACTIVE_STATUS)) {
					responseModel.setMessage(MLIMessageConstants.INACTIVE_SELLER);
					responseModel.setStatus(Constant.FAILURE);
					return responseModel;
				} else {
					isCorrectData = Boolean.TRUE;
				}
			} else {
				responseModel.setMessage(MLIMessageConstants.SELLER_NOT_FOUND);
				responseModel.setStatus(Constant.FAILURE);
				return responseModel;
			}
			if(isCorrectData) {
				Integer currentStep = creditCardJourneyModel.getSteps();
				switch (currentStep) {
				case 1:
					CreditCardCustomerModel creditCardCustomerModel = creditCardJourneyModel.getCreditCardCustomerModel();
					boolean ageValidationCust = customerDetailService.checkAgeGreaterThan18(creditCardCustomerModel.getDob());
					if(!ageValidationCust) {
						responseModel.setMessage(MLIMessageConstants.AGE_VALIDATION);
						responseModel.setStatus(SaveDetailsResponse.FAILURE);
						return responseModel;
					}
					creditCardCustomerEntity.setSlrDtlId(sellerDetailEntity);
					creditCardCustomerEntity.setFirstName(creditCardCustomerModel.getFirstName());
					creditCardCustomerEntity.setLastName(creditCardCustomerModel.getLastName());
					creditCardCustomerEntity.setEmail(creditCardCustomerModel.getEmail());
					creditCardCustomerEntity.setDob(creditCardCustomerModel.getDob());
					creditCardCustomerEntity.setGender(creditCardCustomerModel.getGender());
					creditCardCustomerEntity.setCustomerId(creditCardCustomerModel.getCustomerId());
					creditCardCustomerEntity.setMasterPolicyHolderName(MasterPolicyHolderName.YESBANKCC.getDescription());
					creditCardCustomerEntity.setPhone(creditCardCustomerModel.getPhone());
					creditCardCustomerEntity.setCardSegment(creditCardCustomerModel.getCardSegment());
					creditCardCustomerEntity.setCoverage(creditCardCustomerModel.getCoverage());
					creditCardCustomerEntity.setPremium(creditCardCustomerModel.getPremium());
					creditCardCustomerEntity.setPdfSent("No");
					creditCardCustomerEntity.setPaymentStatus(Status.PAYMENT_PENDING);
					creditCardCustomerEntity.setStep(Status.STEP1);
					creditCardCustomerEntity.setAppStatus(Status.APP_PENDING);
					if(isUpdate) {
						creditCardJourneyModel.setProposalNumber(creditCardCustomerEntity.getProposalNumber());
						creditCardCustomerEntity.setModifiedBy(sellerDetailEntity.getSellerName());
						creditCardCustomerEntity.setModifiedOn(DateUtil.toCurrentUTCTimeStamp());
						creditCardCustomerDAO.saveOrUpdate(creditCardCustomerEntity);
					}else {
						creditCardJourneyModel.setProposalNumber(proposalNumber);
						creditCardCustomerEntity.setCreatedBy(sellerDetailEntity.getSellerName());
						creditCardCustomerEntity.setCreatedOn(DateUtil.toCurrentUTCTimeStamp());
					    creditCardCustomerEntity.setProposalNumber(proposalNumber);
					    creditCardCustomerDAO.save(creditCardCustomerEntity);
					}
					responseModel.setData(creditCardJourneyModel);
					responseModel.setMessage(MLIMessageConstants.CUSTOMER_SAVE_SUCCESS);
					responseModel.setStatus(SaveDetailsResponse.SUCCESS);
					return responseModel;
				case 2 :
					 CreditCardNomineeModel creditCardNomineeModel = creditCardJourneyModel.getCreditCardNomineeModel();
					 CreditCardNomineeEntity creditCardNomineeEntity = creditCardNomineeDAO.findByCustomerDtlId(creditCardCustomerEntity.getCreditCardCustomerId());;
                     if(ObjectUtils.isEmpty(creditCardNomineeEntity)) {
                    	 creditCardNomineeEntity = new CreditCardNomineeEntity();
                    	 creditCardNomineeEntity.setCreatedBy(sellerDetailEntity.getSellerName());
                    	 creditCardNomineeEntity.setCreatedOn(DateUtil.toCurrentUTCTimeStamp());
                     }else {
                    	 creditCardNomineeEntity.setModifiedBy(sellerDetailEntity.getSellerName());
                    	 creditCardNomineeEntity.setModifiedOn(DateUtil.toCurrentUTCTimeStamp());
                     }
					 creditCardNomineeEntity.setNomineeFirstName(creditCardNomineeModel.getNomineeFirstName());
					 creditCardNomineeEntity.setNomineeLastName(creditCardNomineeModel.getNomineeLastName());
					 creditCardNomineeEntity.setNomineeDob(creditCardNomineeModel.getNomineeDob());
					 creditCardNomineeEntity.setNomineeGender(creditCardNomineeModel.getNomineeGender());
					 creditCardNomineeEntity.setRelationshipWithAssured(RelationshipWithAssured.getRelationshipWithAssured(creditCardNomineeModel.getRelationshipWithAssured()));
					 creditCardNomineeEntity.setAppointeeFirstName(creditCardNomineeModel.getAppointeeFirstName());
			    	 creditCardNomineeEntity.setAppointeeLastName(creditCardNomineeModel.getAppointeeLastName());
			    	 creditCardNomineeEntity.setAppointeeDob(creditCardNomineeModel.getAppointeeDob());
			    	 creditCardNomineeEntity.setAppointeeGender(creditCardNomineeModel.getAppointeeGender());
			    	 creditCardNomineeEntity.setRelationWithNominee(RelationshipWithAssured.getRelationshipWithAssured(creditCardNomineeModel.getRelationWithNominee()));
					 boolean isValidAge = customerDetailService.checkAgeGreaterThan18(creditCardNomineeModel.getNomineeDob());
				     if(!isValidAge) {
				    	 creditCardNomineeEntity.setAppointee(Boolean.TRUE);
				     }else {
				    	 creditCardNomineeEntity.setAppointee(Boolean.FALSE);
				     }
				     creditCardNomineeEntity.setCreditCardCustomerEntity(creditCardCustomerEntity);
				     creditCardNomineeDAO.saveOrUpdate(creditCardNomineeEntity);
				     updateCustomerDetailsSteps(creditCardCustomerEntity,currentStep);
				     responseModel.setData(creditCardJourneyModel);
				     responseModel.setMessage(MLIMessageConstants.NOMINEE_SAVE_SUCCESS);
					 responseModel.setStatus(SaveDetailsResponse.SUCCESS);
					 return responseModel;
				case 3:
					 CreditCardCovidEntity creditCardCovidEntity = null;
					 CreditCardHealthModel creditCardHealthModel = creditCardJourneyModel.getCreditCardHealthModel();
					 CreditCardHealthEntity creditCardHealthEntity = creditCardHealthDAO.findByCustomerDtlId(creditCardCustomerEntity.getCreditCardCustomerId());;
					 AdminProductConfigEntity adminProductConfigEntity = adminProductConfigService.getBySellerContactNumber(creditCardJourneyModel.getSellerContNumber());
					 CreditCardCovidModel creditCardCovidModel =  creditCardHealthModel.getCreditCardCovidModel(); 
					 if(ObjectUtils.isEmpty(creditCardHealthEntity)) {
                    	 creditCardHealthEntity = new CreditCardHealthEntity();
                    	 creditCardHealthEntity.setCreatedBy(sellerDetailEntity.getSellerName());
                    	 creditCardHealthEntity.setCreatedOn(DateUtil.toCurrentUTCTimeStamp());
                     }else {
                    	 creditCardCovidEntity = creditCardCovidDAO.findByCreditCardHealthId(creditCardHealthEntity.getCreditCardHealthId());
                    	 creditCardHealthEntity.setModifiedBy(sellerDetailEntity.getSellerName());
                    	 creditCardHealthEntity.setModifiedOn(DateUtil.toCurrentUTCTimeStamp());
                     }
                     creditCardHealthEntity.setHealthFirstAnswer(creditCardHealthModel.getHealthFirstAnswer());
                     creditCardHealthEntity.setHealthSecondAnswer(creditCardHealthModel.getHealthSecondAnswer());
                     creditCardHealthEntity.setHealthThirdAnswer(creditCardHealthModel.getHealthThirdAnswer());
                     creditCardHealthEntity.setIsSmoker(creditCardHealthModel.getIsSmoker());
                     if("Smoker".equalsIgnoreCase(creditCardHealthModel.getIsSmoker())) {
                        creditCardHealthEntity.setSmokePerDay(creditCardHealthModel.getSmokePerDay());
                     }else {
                    	 creditCardHealthEntity.setSmokePerDay(null);
                     }
                     creditCardHealthEntity.setDeclaration(creditCardHealthModel.getDeclaration());
                     creditCardHealthEntity.setAppNumber(creditCardHealthModel.getAppNumber());
                     creditCardHealthEntity.setCreditCardCustomerId(creditCardCustomerEntity);
                     creditCardHealthDAO.saveOrUpdate(creditCardHealthEntity);
                     updateCustomerDetailsSteps(creditCardCustomerEntity,currentStep);
					if (!ObjectUtils.isEmpty(creditCardCovidModel) && !ObjectUtils.isEmpty(adminProductConfigEntity) && Constant.YES.equals(adminProductConfigEntity.getIsCovidEnable())) {
						if (ObjectUtils.isEmpty(creditCardCovidEntity)) {
							creditCardCovidEntity = new CreditCardCovidEntity();
							creditCardCovidEntity.setCreatedBy(sellerDetailEntity.getSellerName());
							creditCardCovidEntity.setCreatedOn(DateUtil.toCurrentUTCTimeStamp());
						}else {
							creditCardCovidEntity.setModifiedBy(sellerDetailEntity.getSellerName());
							creditCardCovidEntity.setModifiedOn(DateUtil.toCurrentUTCTimeStamp());
						}
						creditCardCovidEntity.setCovidDeclaration(creditCardCovidModel.getCovidDeclaration());
						if("No".equalsIgnoreCase(creditCardCovidModel.getCovidDeclaration())) {
							creditCardCovidEntity.setComment(creditCardCovidModel.getComment());
						}else {
							creditCardCovidEntity.setComment(null);
						}
						creditCardCovidEntity.setCreditCardHealthId(creditCardHealthEntity);
						creditCardCovidDAO.saveOrUpdate(creditCardCovidEntity);

					}
				     responseModel.setData(creditCardJourneyModel);
				     responseModel.setMessage(MLIMessageConstants.HEALTH_SAVE_SUCCESS);
					 responseModel.setStatus(SaveDetailsResponse.SUCCESS);
					 return responseModel;
			   case 4:
				     CreditCardMandatoryModel creditCardMandatoryModel = creditCardJourneyModel.getCreditCardMandatoryModel();
				     CreditCardMandatoryEntity creditCardMandatoryEntity = creditCardMandatoryDAO.findbyCreditCardCustomerId(creditCardCustomerEntity.getCreditCardCustomerId());
				     if(ObjectUtils.isEmpty(creditCardMandatoryEntity)) {
				    	 creditCardMandatoryEntity = new CreditCardMandatoryEntity();
				    	 creditCardMandatoryEntity.setCreatedBy(sellerDetailEntity.getSellerName());
				    	 creditCardMandatoryEntity.setCreatedOn(DateUtil.toCurrentUTCTimeStamp());
				     }else {
				    	 creditCardMandatoryEntity.setModifiedBy(sellerDetailEntity.getSellerName());
				    	 creditCardMandatoryEntity.setModifiedOn(DateUtil.toCurrentUTCTimeStamp());
				     }
				     creditCardMandatoryEntity.setIsMandatoryDeclaration(creditCardMandatoryModel.getIsMandatoryDeclaration());
				     creditCardMandatoryEntity.setPlace(creditCardMandatoryModel.getPlace());
				     creditCardMandatoryEntity.setPolicyHolderName(MasterPolicyHolderName.YESBANKCC);
				     creditCardMandatoryEntity.setCreditCardCustomerId(creditCardCustomerEntity);
				     creditCardMandatoryDAO.saveOrUpdate(creditCardMandatoryEntity);
				     updateCustomerDetailsSteps(creditCardCustomerEntity,currentStep);
				     responseModel.setData(creditCardJourneyModel);
				     responseModel.setMessage(MLIMessageConstants.MANDATORY_SAVE_SUCCESS);
					 responseModel.setStatus(SaveDetailsResponse.SUCCESS);
					 return responseModel;
			   case 5:
				   if (creditCardJourneyModel.getCustOTPVerificationModel().getStatus().equalsIgnoreCase(SaveUpdateResponse.SUCCESS)) {
					   if (creditCardCustomerEntity.getEmail() != null && !creditCardCustomerEntity.getEmail().trim().isEmpty()) {
							generateEmail(creditCardCustomerEntity, Constant.STEP_5);
						}
						SMSResponseModel smsResponse = sendMLISMSService.sendSmsToYBLCCCustomer(null,creditCardCustomerEntity.getPhone(),OTPType.YBLCC_APP_SENT,creditCardCustomerEntity);
						if (!("OK").equalsIgnoreCase(smsResponse.getMliSmsServiceResponse().getResponseHeader().getGeneralResponse().getStatus())) {
						   logger.error("::::::::::::::::: SMS not Send to Customer :::::::::::::::Mob="+ creditCardCustomerEntity.getPhone() + " at time : " + (new Date())); 
						} else{ 
						   logger.info("::::::::::::::::: SMS Send to Customer :::::::::::::::Mob=" +creditCardCustomerEntity.getPhone() + " at time : " + (new Date())); 
						}
						 
						updateCustomerDetailsSteps(creditCardCustomerEntity,currentStep);
						CreditCardCustomerModel creditCardCustomerModelObj = creditCardJourneyModel.getCreditCardCustomerModel();
						if (ObjectUtils.isEmpty(creditCardCustomerModelObj)) {
							creditCardCustomerModelObj = new CreditCardCustomerModel();
						}
						creditCardCustomerModelObj.setAppStatus(Status.APP_SENT.getLabel());
						creditCardJourneyModel.setCreditCardCustomerModel(creditCardCustomerModelObj);
						creditCardJourneyModel.setSfvTimeStamp(DateUtil.toCurrentUTCTimeStamp());
						responseModel.setData(creditCardJourneyModel);
						responseModel.setMessage(MLIMessageConstants.CC_SENT_VERIFICATION_SUCCESS);
						responseModel.setStatus(SaveDetailsResponse.SUCCESS);
						return responseModel;
				   }else {
					    responseModel.setData(creditCardJourneyModel);
						responseModel.setMessage(MLIMessageConstants.CC_SENT_VERIFICATION_FAILED);
						responseModel.setStatus(SaveDetailsResponse.FAILURE);
						return responseModel;
				   }
				};
			}
		} catch (Exception e) {
			logger.error("Exception occurred while saving customer credit card journey : {}");
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * generate email for credit card cusotmer verification
	 * @param creditCardCustomerEntity
	 * @param step5
	 * @author rajkumar
	 */
	private void generateEmail(CreditCardCustomerEntity creditCardCustomerEntity, String step5) {
		logger.info("Enter into CreditCardJourneyServiceImpl to generate Email....");
		ResponseModel<MliEmailServiceModel> emailResponse = new ResponseModel<MliEmailServiceModel>();
		try {
			EmailModel mliEmailModel = new EmailModel();
			mliEmailModel.setMailUserType(OTPUserType.YBLCCCUSTOMER.getLabel());
			mliEmailModel.setProposalNumber(creditCardCustomerEntity.getProposalNumber());
			mliEmailModel.setCustMobNumber(creditCardCustomerEntity.getPhone());
			StringBuilder fullName = new StringBuilder();
			if (!StringUtils.isEmpty(creditCardCustomerEntity.getFirstName())) {
				fullName.append(creditCardCustomerEntity.getFirstName());
			}
			if (!StringUtils.isEmpty(creditCardCustomerEntity.getLastName())) {
				fullName.append(" ").append(creditCardCustomerEntity.getLastName());
			}
			mliEmailModel.setCustomerName(fullName.toString());
			mliEmailModel.setMailIdTo(creditCardCustomerEntity.getEmail());
			mliEmailModel.setStep(Constant.STEP_5);
			try {
				emailResponse = mliEmailService.sendEmail(mliEmailModel);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (!("200".equalsIgnoreCase(emailResponse.getCode()) && ("Ok").equalsIgnoreCase(emailResponse.getStatus()))) {
				logger.error("::::::::::::::::: Mail Not Send to customer :::::::::::::::"+ creditCardCustomerEntity.getProposalNumber() + " at time : " + (new Date()));

			} else {
				logger.info("::::::::::::::::: Mail  Send to customer :::::::::::::::"+ creditCardCustomerEntity.getProposalNumber() + " at time : " + (new Date()));
			}
	} catch (Exception exception) {
		logger.error("::::::::::: Something went wrong :generateAndEmailPDF :::::::::::::::");
		exception.printStackTrace();
	}

	}

	/**
	 * update  CreditCardCustomerEntity  app status
	 * @param creditCardCustomerEntity
	 * @param currentStep 
	 * @author rajkumar
	 */
	private void updateCustomerDetailsSteps(CreditCardCustomerEntity creditCardCustomerEntity, Integer currentStep) {
		creditCardCustomerEntity.setStep(Status.getStatus(currentStep));
		creditCardCustomerEntity.setModifiedOn(DateUtil.toCurrentUTCTimeStamp());
		creditCardCustomerEntity.setAppStatus(Status.APP_PENDING);
		if (Status.STEP5.getValue().equals(currentStep)) {
			creditCardCustomerEntity.setAppStatus(Status.APP_SENT);
			creditCardCustomerEntity.setCustOtpVerifiedDate(DateUtil.dateFormater(DateUtil.extractDateAsString(DateUtil.toCurrentUTCTimeStamp())));
			creditCardCustomerEntity.setSfvTimeStamp(DateUtil.toCurrentUTCTimeStamp());
		}
		creditCardCustomerDAO.saveOrUpdate(creditCardCustomerEntity);
	}
	
	/**
	 * get card type lookup
	 */
	@Override
	public List<CardTypeModel> getLookup() {
		logger.info("Enter into CreditCardJourneyServiceImpl to fetch card lookup....");
		try {
			return CardType.lookup;
		} catch (Exception e) {
			logger.error("Exception occurred while fetching card lookup : {}");
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * @param age
	 * @param coverage
	 * @return health form type based on age and coverage
	 * @author rajkumar
	 */
	public String getHealthFormType(String age, String coverage) {
		Integer ageTillDate;
		Integer sumAssured;
		try {
			sumAssured = Integer.parseInt(coverage.replace("L", "00000"));
			ageTillDate = DateUtil.ageValidation18Year(age);
			if(18 <= ageTillDate &&  ageTillDate <= 45) {
				if(1000000 <=sumAssured && sumAssured <= 3000000) {
					return "DOGH";
				}else if(3000001 <=sumAssured && sumAssured <= 5000000){
					return "SFQ";
				}
			}else if(46 <= ageTillDate && ageTillDate <= 55) {
				return "SFQ";
			}
		}catch (NumberFormatException exception) {
			exception.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
			throw new AgeException(MLIMessageConstants.AGE_FORMATE);
		}
		return "SFQ";
	}
	
	/**
	 * generate unique proposal number for yblcc
	 * @return
	 * @throws Exception
	 * @author rajkumar
	 */
	private String genearteYBLCCNumber() throws Exception {
		logger.info("Enter into CreditCardJourneyServiceImpl to generate YBLCC proposal number....");
		try {
			ProposalNumberEntity proposalDtl = new ProposalNumberEntity();
			Long proposalId = 1l;
			String proposalNumber = null;
			proposalDtl = proposalNumberDAO.findByProposalNumberId(proposalId);

			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyy");
			Date nowTime = sdf.parse(sdf.format(new Date(System.currentTimeMillis())));
			Date time_in_db = sdf.parse(sdf.format(proposalDtl.getDateInDB()));

			if ((null != time_in_db && time_in_db.before(nowTime))) {
				proposalDtl.setDateInDB(DateUtil.toMilliSecond());
				proposalDtl.setModifiedOn(DateUtil.toCurrentUTCTimeStamp());
				proposalDtl.setUseNumber(proposalDtl.getStartNumber());
				proposalNumberDAO.saveOrUpdate(proposalDtl);
			}
			if ((null != time_in_db && nowTime.equals(time_in_db)) || (null != proposalDtl.getDateInDB())) {
				String formatedString = NumberParser.getFormatedNumber(proposalDtl.getUseNumber(), "xxxxx", 'x', '0');
				String prefix = "YBLCC";
				SimpleDateFormat format = new SimpleDateFormat("ddMMyyyy");
				String dateFormat = format.format(proposalDtl.getDateInDB());
				proposalNumber = prefix + dateFormat + formatedString;
				proposalDtl = proposalNumberDAO.findByProposalNumberId(proposalId);
				proposalDtl.setUseNumber(proposalDtl.getUseNumber() + 1);
				proposalDtl.setModifiedOn(DateUtil.toCurrentUTCTimeStamp());
				proposalNumberDAO.saveOrUpdate(proposalDtl);
			}
			return proposalNumber;
		} catch (Exception e) {
			logger.info("::::::::::::::::: Error in generating proposal number :::::::::::::::" + e);
			throw new Exception(e.getMessage());
		}
	}

	/**
	 * return map with search and filter based on param and status 
	 */
	@Override
	@Transactional
	public Map<String, Object> search(Pageable pageable, Long slrContNo, String param, String status, String paymentStatus) {
		logger.info("Enter into CreditCardJourneyServiceImpl to search and filter customer....");
		try {
			SellerDetailEntity sellerDetailEntity = sellerDAO.findByContactNo(slrContNo);
			if(ObjectUtils.isEmpty(sellerDetailEntity)) {
				throw new CustomParameterizedException("SellerNotFoundException",MLIMessageConstants.SELLER_NOT_FOUND);
			}
			Map<String, Object> countResultMap = creditCardCustomerDAO.search(pageable, sellerDetailEntity.getSellerDtlId(), param, status, paymentStatus);
			List<CreditCardCustomerEntity> creditCardCustomerEntity = (List<CreditCardCustomerEntity>) countResultMap.get(Constant.QUERY_RESULT);
			Long totalCount = (Long) countResultMap.get(Constant.TOTAL_COUNT);
			List<CreditCardJourneyModel> creditCardJourneyModel = getCreditCardJourneyModel(creditCardCustomerEntity,sellerDetailEntity.getContactNo());
			PageInfoModel pageInfoModel = PageableUtil.getPageMetaInfo(pageable, creditCardJourneyModel.size(), totalCount);
			Map<String, Object> response = new HashMap<>(2);
			response.put(Constant.PAGE_META_INFO, pageInfoModel);
			response.put(Constant.RESULT, creditCardJourneyModel);
			return response;
		} catch (Exception e) {
			logger.info("::::::::::::::::: Error while searching customer :::::::::::::::" + e);
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * convert list of {@link CreditCardCustomerEntity} into list of {@link CreditCardJourneyModel}
	 * @param creditCardCustomerList
	 * @param object
	 * @return
	 * @author rajkumar
	 */
	private List<CreditCardJourneyModel> getCreditCardJourneyModel(List<CreditCardCustomerEntity> creditCardCustomerList, Long selletContact) {
		logger.info("Enter into CreditCardJourneyServiceImpl to credit card journey model list....");
		try {
			List<CreditCardJourneyModel> creditCardJourneyModelList = new ArrayList<>(creditCardCustomerList.size());
			if(!CollectionUtils.isEmpty(creditCardCustomerList)) {
				for(CreditCardCustomerEntity creditCardCustomerEntity : creditCardCustomerList) {
					CreditCardJourneyModel creditCardJourneyModel = new CreditCardJourneyModel();
					creditCardJourneyModel.setSteps(creditCardCustomerEntity.getStep().getValue());
					creditCardJourneyModel.setProposalNumber(creditCardCustomerEntity.getProposalNumber());
					creditCardJourneyModel.setSellerContNumber(selletContact);
					creditCardJourneyModel.setSfvTimeStamp(creditCardCustomerEntity.getSfvTimeStamp());
					CreditCardCustomerModel creditCardCustomerModel = modelMapper.map(creditCardCustomerEntity, CreditCardCustomerModel.class);
					creditCardCustomerModel.setAppStatus(getPrintedStatus(creditCardCustomerEntity.getAppStatus()));
					creditCardCustomerModel.setPaymentStatus(creditCardCustomerEntity.getPaymentStatus().getLabel());
					creditCardJourneyModel.setCreditCardCustomerModel(creditCardCustomerModel);
					CreditCardNomineeEntity creditCardNomineeEntity =  creditCardNomineeDAO.findByCustomerDtlId(creditCardCustomerEntity.getCreditCardCustomerId());
					if (!ObjectUtils.isEmpty(creditCardNomineeEntity)) {
						CreditCardNomineeModel creditCardNomineeModel = modelMapper.map(creditCardNomineeEntity,CreditCardNomineeModel.class);
						if(creditCardNomineeEntity.getRelationshipWithAssured() != null && !"".equals(creditCardNomineeEntity.getRelationshipWithAssured())) {
						    creditCardNomineeModel.setRelationshipWithAssured(creditCardNomineeEntity.getRelationshipWithAssured().getLabel());
						}
						if(creditCardNomineeEntity.getRelationWithNominee() != null && !"".equals(creditCardNomineeEntity.getRelationWithNominee())) {
						    creditCardNomineeModel.setRelationWithNominee(creditCardNomineeEntity.getRelationWithNominee().getLabel());
						}
						creditCardJourneyModel.setCreditCardNomineeModel(creditCardNomineeModel);
					}
					CreditCardHealthEntity creditCardHealthEntity =  creditCardHealthDAO.findByCustomerDtlId(creditCardCustomerEntity.getCreditCardCustomerId());
					if(!ObjectUtils.isEmpty(creditCardHealthEntity)) {
						CreditCardHealthModel creditCardHealthModel = modelMapper.map(creditCardHealthEntity,CreditCardHealthModel.class);
						CreditCardCovidEntity creditCardCovidEntity = creditCardCovidDAO.findByCreditCardHealthId(creditCardHealthEntity.getCreditCardHealthId());
					    if(!ObjectUtils.isEmpty(creditCardCovidEntity)) {
							CreditCardCovidModel creditCardCovidModel = modelMapper.map(creditCardCovidEntity,CreditCardCovidModel.class);
							creditCardHealthModel.setCreditCardCovidModel(creditCardCovidModel);
					    }
					    creditCardJourneyModel.setCreditCardHealthModel(creditCardHealthModel);
					}
					CreditCardMandatoryEntity creditCardMandatoryEntity = creditCardMandatoryDAO.findbyCreditCardCustomerId(creditCardCustomerEntity.getCreditCardCustomerId());
					if(!ObjectUtils.isEmpty(creditCardMandatoryEntity)) {
						CreditCardMandatoryModel creditCardMandatoryModel = modelMapper.map(creditCardMandatoryEntity,CreditCardMandatoryModel.class);
						creditCardJourneyModel.setCreditCardMandatoryModel(creditCardMandatoryModel);
					}
					creditCardJourneyModelList.add(creditCardJourneyModel);
				}
			}
			return creditCardJourneyModelList;
		} catch (Exception exception) {
			logger.error(":::::::::::::::: Error in get credit card journey model list  :::::::::::::" + exception.getMessage());
			exception.printStackTrace();
		}
		return null;
	}
	
	/**
	 * get constant status based on enum status
	 * @param status
	 * @return
	 * @author rajkumar
	 */
	public String getPrintedStatus(Status status) {
		String printedStatus = "";
		if (status.equals(Status.APP_PENDING)) {
			printedStatus = Constant.WIP;
		} else if (status.equals(Status.APP_SENT)) {
			printedStatus = Constant.SFV;
		} else if (status.equals(Status.APP_VERIFIED)) {
			printedStatus = Constant.V;
		} else if (status.equals(Status.APP_COMPLETE)) {
			printedStatus = Constant.COMPLETED;
		}
		return printedStatus;
	}
	
	
    /**
     * method to get status of customer into map
     */
	@Transactional
	@Override
	public Map<String, Object> getStatus(String data) {
		logger.info("Enter into CreditCardJourneyServiceImpl to get status of customer ....");
		Map<String, Object> resultMap = new HashMap<>(1);
		try {
			String decryptData = AES.decrypt(data, aesSecratKey);
			if (StringUtils.isEmpty(decryptData)) {
				resultMap.put(Constant.STATUS, Constant.FAILURE);
				resultMap.put(Constant.MESSAGE, MLIMessageConstants.INVALID_KEY);
				logger.info("decryptData : " + decryptData);
				return resultMap;
			}
			String values[] = decryptData.split("_");
			String mobileNumber = values[0];
			String proposalNumber = values[1];
			resultMap.put("proposalNumber", proposalNumber);
			resultMap.put("mobileNumber", mobileNumber);
			CreditCardCustomerEntity creditCardCustomerEntity = creditCardCustomerDAO.findByProposalNumber(proposalNumber);
			if (!ObjectUtils.isEmpty(creditCardCustomerEntity)) {
				logger.info("Proposal number : " + proposalNumber + ", Id : " + creditCardCustomerEntity.getCreditCardCustomerId());
				resultMap.put(Constant.STATUS,getPrintedStatus(creditCardCustomerEntity.getAppStatus()));
				if (creditCardCustomerEntity.getPhone() != null && creditCardCustomerEntity.getPhone().equals(Long.parseLong(mobileNumber))) {
					resultMap.put(Constant.MOBILE_VERIFIED, true);
				} else {
					resultMap.put(Constant.MOBILE_VERIFIED, false);
				}
				SellerDetailEntity sellerDetailEntity = sellerDAO.getEntity(SellerDetailEntity.class,creditCardCustomerEntity.getSlrDtlId().getSellerDtlId());
				CreditCardJourneyModel creditCardJourneyModel = null;
				if(!ObjectUtils.isEmpty(sellerDetailEntity)) {
					resultMap.put("racLocationMapping", sellerDetailEntity.getRacLocationMapping());
					resultMap.put("sourceEmpCode", sellerDetailEntity.getSourceEmpCode());
					resultMap.put("mliRM", sellerDetailEntity.getMliRM());
					creditCardJourneyModel =	getCreditCardJourneyModel(Arrays.asList(creditCardCustomerEntity), sellerDetailEntity.getContactNo()).get(0);
				}
				resultMap.put("creditCardJourneyModel", creditCardJourneyModel);
			} else {
				resultMap.put(Constant.STATUS, Constant.FAILURE);
				resultMap.put(Constant.MESSAGE, MLIMessageConstants.CUSTOMER_PROPOSAL_NOT_FOUND);
			}
		} catch (Exception exception) {
			resultMap.put(Constant.STATUS, Constant.FAILURE);
			resultMap.put(Constant.MESSAGE, Constant.FAILURE_MSG);
			exception.printStackTrace();
		}
		return resultMap;
	}

	/**
	 * method to update customer email and health details
	 */
	@Transactional
	@Override
	public ResponseModel<?> updateCustomer(CustomerUpdateModel customerUpdateModel) {
		logger.info("Enter into CreditCardJourneyServiceImpl to update customer email and health declaration details");
		ResponseModel<?> responseModel = new ResponseModel<UserDetailsModel>();
		try {
			String decryptData = AES.decrypt(customerUpdateModel.getData(), aesSecratKey);
			String values[] = decryptData.split("_");
			String proposalNumber = values[1];
			CreditCardCustomerEntity creditCardCustomerEntity = creditCardCustomerDAO.findByProposalNumber(proposalNumber);
			if (!ObjectUtils.isEmpty(creditCardCustomerEntity)) {
				String name = creditCardCustomerEntity.getFirstName() +" "+creditCardCustomerEntity.getLastName();
				if (creditCardCustomerEntity.getAppStatus().getLabel().equals(Status.APP_VERIFIED.getLabel())) {
					responseModel.setStatus(Constant.FAILURE);
					responseModel.setMessage(MLIMessageConstants.CUST_ALREADY_VERIFIED);
					return responseModel;
				}
				if(!StringUtils.isEmpty(customerUpdateModel.getCustEmail())) {
					creditCardCustomerEntity.setEmail(customerUpdateModel.getCustEmail());
					creditCardCustomerDAO.save(creditCardCustomerEntity);
					responseModel.setMessage(MLIMessageConstants.CUST_EMAIL_UPDATE_SUCCESS);
					responseModel.setStatus(SaveDetailsResponse.SUCCESS);
					return responseModel;
				}
				logger.info("Proposal number : " + proposalNumber + ", Id : " + creditCardCustomerEntity.getCreditCardCustomerId());
				CreditCardHealthEntity creditCardHealthEntity = creditCardHealthDAO.findByCustomerDtlId(creditCardCustomerEntity.getCreditCardCustomerId());
				if (!ObjectUtils.isEmpty(creditCardHealthEntity) && !ObjectUtils.isEmpty(customerUpdateModel.getCreditCardHealthModel())) {
					CreditCardHealthModel creditCardHealthModel = customerUpdateModel.getCreditCardHealthModel();
					creditCardHealthEntity.setAppNumber(creditCardHealthModel.getAppNumber());
					creditCardHealthEntity.setHealthFirstAnswer(creditCardHealthModel.getHealthFirstAnswer());
					creditCardHealthEntity.setHealthSecondAnswer(creditCardHealthModel.getHealthSecondAnswer());
					creditCardHealthEntity.setHealthThirdAnswer(creditCardHealthModel.getHealthThirdAnswer());
					creditCardHealthEntity.setIsSmoker(creditCardHealthModel.getIsSmoker());
					if("Smoker".equalsIgnoreCase(creditCardHealthModel.getIsSmoker())) {
	                    creditCardHealthEntity.setSmokePerDay(creditCardHealthModel.getSmokePerDay());
	                }else {
	                	creditCardHealthEntity.setSmokePerDay(null);
	                }
					creditCardHealthEntity.setDeclaration(creditCardHealthModel.getDeclaration());
					creditCardHealthEntity.setModifiedBy(name);
					creditCardHealthEntity.setModifiedOn(DateUtil.toCurrentUTCTimeStamp());
					CreditCardCovidEntity creditCardCovidEntity = creditCardCovidDAO.findByCreditCardHealthId(creditCardHealthEntity.getCreditCardHealthId());
					if(!ObjectUtils.isEmpty(creditCardCovidEntity) && !ObjectUtils.isEmpty(creditCardHealthModel.getCreditCardCovidModel())) {
						CreditCardCovidModel creditCardCovidModel = creditCardHealthModel.getCreditCardCovidModel();
						creditCardCovidEntity.setCovidDeclaration(creditCardCovidModel.getCovidDeclaration());
						if("No".equalsIgnoreCase(creditCardCovidModel.getCovidDeclaration())) {
						   creditCardCovidEntity.setComment(creditCardCovidModel.getComment());
						}else {
							creditCardCovidEntity.setComment(null);
						}
						creditCardCovidEntity.setModifiedBy(name);
						creditCardCovidEntity.setModifiedOn(DateUtil.toCurrentUTCTimeStamp());
						creditCardCovidDAO.saveOrUpdate(creditCardCovidEntity);
					}
					creditCardHealthDAO.saveOrUpdate(creditCardHealthEntity);
				} else {
					responseModel.setStatus(Constant.FAILURE);
					responseModel.setMessage(MLIMessageConstants.HEALTH_INFO_NOT_FOUND);
					return responseModel;
				}
				responseModel.setMessage(MLIMessageConstants.HEALTH_UPDATE_SUCCESS);
				responseModel.setStatus(SaveDetailsResponse.SUCCESS);
				return responseModel;
			} else {
				responseModel.setStatus(Constant.FAILURE);
				responseModel.setMessage(MLIMessageConstants.CUSTOMER_NOT_FOUND);
			}
		} catch (Exception exception) {
			logger.error(":::::::::::::::: Error in save reflexive question  :::::::::::::" + exception.getMessage());
			exception.printStackTrace();
			responseModel.setStatus(Constant.FAILURE);
			responseModel.setStatus(Constant.FAILURE_MSG);
		}
		return responseModel;
	}
	
	
	/**
	 * method is used to generate pdf and send email and sms to customer 
	 * @param creditCardCustomerEntity
	 * @author rajkumar
	 */
	@Override
	public void generatePDF(CreditCardCustomerEntity creditCardCustomerEntity) {
		logger.info("Enter into CreditCardJourneyServiceImpl to generate PDF and send email and sms");
		try {
			if (!ObjectUtils.isEmpty(creditCardCustomerEntity)) {
				String modifyTS = DateUtil.extractDateWithTSAsAM_PMStringSlashFormate(DateUtil.toCurrentUTCTimeStamp());
				String filePath = generateAndEmailPDF(creditCardCustomerEntity, modifyTS);
				List<SchemeDirEntity> schemeDr = schemeDirDao.findByProposalNumber(creditCardCustomerEntity.getProposalNumber());
				if (schemeDr != null && !schemeDr.isEmpty()) {
					try {
						for (SchemeDirEntity temp : schemeDr) {
							StringBuilder copiedPath = new StringBuilder();
							if (DocType.PROPOSAL.equals(temp.getDocType())) {
								copiedPath.append(
										ZipUtils.getDirNameOfVerified(creditCardCustomerEntity.getAppCompletionDate(),
												DocType.YBLCCPROPOSAL, creditCardCustomerEntity.getProposalNumber()));
								awsFileUtility.copyObject(temp.getAwsFilePath(), copiedPath.toString());
							} 
							logger.info("copiedPath : " + copiedPath + " ,Proposal number : "
									+ creditCardCustomerEntity.getProposalNumber() + " ,DocType : " + temp.getDocType()
									+ " ,Current date : " + new Date());
						}
					} catch (Exception exception) {
						logger.error("::::::::::::Error in copy PDF or uploading to aws s3::::::::" + exception);
						exception.printStackTrace();
					}
				}
				logger.info("PDF File path *************** :"+filePath);
				if(!StringUtils.isEmpty(creditCardCustomerEntity.getEmail().trim()) && !StringUtils.isEmpty(filePath)) {
			    	sendPDFNdEmailAsynchronously(creditCardCustomerEntity, filePath);
				}
				if(!ObjectUtils.isEmpty(creditCardCustomerEntity.getPhone())) {
					sendSMSAsynchronously(creditCardCustomerEntity);
				}
			}
		} catch (Exception exception) {
			logger.error("::::::::::::Error while generating pdf::::::::" + exception);
			exception.printStackTrace();
		}
	}
	
	/**
	 * generate Yes bank credit card PDF 
	 */
	@Override
	public String generateAndEmailPDF(CreditCardCustomerEntity creditCardCustomerEntity,String modifyTS) {
		String pdfFilepath = null;
		try {
				SellerDetailEntity sellerDetailEntity = sellerDAO.getEntity(SellerDetailEntity.class,creditCardCustomerEntity.getSlrDtlId().getSellerDtlId());
				CreditCardJourneyModel creditCardJourneyModel = null;
				if (!ObjectUtils.isEmpty(sellerDetailEntity)) {
					creditCardJourneyModel = getCreditCardJourneyModel(Arrays.asList(creditCardCustomerEntity),sellerDetailEntity.getContactNo()).get(0);
				}
				Map<String, Object> data = pdfGenaratorUtil.getYBLCCPdfModel(creditCardJourneyModel, modifyTS);
				pdfFilepath = awsFileUtility.generateFilePath(FileUtilityConverter.getYBLCCFileUtilityModel(FileExtention.PDF, DocType.PROPOSAL, creditCardCustomerEntity));
				logger.info("Pdf file path : " + pdfFilepath);
				pdfGenaratorUtil.createPdf("yblcc_proposalPdf", data, pdfFilepath);
				logger.info("::::::START::::::saveFinalSubmit:::Uploadning DPF to AWS S3:::::::::customerDetails.getProposalNumber()="+ creditCardCustomerEntity.getProposalNumber());
				FileUtilityModel fileUtilityModel = new FileUtilityModel();
				fileUtilityModel.setDocType(DocType.PROPOSAL);
				fileUtilityModel.setProposalNumber(creditCardCustomerEntity.getProposalNumber());
				fileUtilityModel.setCreatedOn(creditCardCustomerEntity.getCreatedOn());
				fileUtilityModel.setCustFirstName(creditCardCustomerEntity.getFirstName());
				fileUtilityModel = awsFileUtility.getPDFFileNdDirectoryDetails(fileUtilityModel);

				awsFileUtility.createFileOnAWSS3(pdfFilepath, fileUtilityModel.getAwsFilePath());
				fileUtilityModel.setDocType(DocType.PROPOSAL);
				fileUtilityModel.setProposalNumber(creditCardCustomerEntity.getProposalNumber());
				awsFileUtility.updateAwsPDFNdCDFFilePathInDB(fileUtilityModel);
				logger.info(":::::END:::::::saveFinalSubmit:::::::::Uploadning DPF to AWS S3:::::local pdfFilepath="+ pdfFilepath + "::::::fileUtilityModel.getAwsFilePath()=" + fileUtilityModel.getAwsFilePath());
			return pdfFilepath;
		} catch (Exception e) {
			logger.error("::::::::::::saveFinalSubmit:::::Error in generating PDF or uploading to aws s3::::::::" + e);
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * send email to credit card customer with pdf
	 */
	@Override
	public void sendEmailWithPDF(CreditCardCustomerEntity creditCardCustomerEntity,String filePath) {
		ResponseModel<MliEmailServiceModel> emailResponse = new ResponseModel<MliEmailServiceModel>();
		FileUtilityModel fileDetails = new FileUtilityModel();
		try {
			fileDetails = awsFileUtility.getPDFFileNdDirectoryDetails(FileUtilityConverter
					.getYBLCCFileUtilityModel(FileExtention.PDF, DocType.PROPOSAL, creditCardCustomerEntity));
		} catch (Exception e) {
			logger.error(":::::::::::::::::Error in getting PDF file details ::::::::" + e);
		}
		if (!StringUtils.isEmpty(filePath) && !ObjectUtils.isEmpty(fileDetails)) {
			EmailModel mliEmailModel = new EmailModel();
			mliEmailModel.setProposalNumber(creditCardCustomerEntity.getProposalNumber());
			mliEmailModel.setCustMobNumber(creditCardCustomerEntity.getPhone());
			StringBuilder fullName = new StringBuilder();
			if (creditCardCustomerEntity.getFirstName() != null) {
				fullName.append(creditCardCustomerEntity.getFirstName());
			}
			if (creditCardCustomerEntity.getLastName() != null) {
				fullName.append(" ").append(creditCardCustomerEntity.getLastName());
			}
			mliEmailModel.setCustomerName(fullName.toString());
			mliEmailModel.setMailIdTo(creditCardCustomerEntity.getEmail());
			mliEmailModel.setName(!ObjectsUtil.isNull(fileDetails.getFileName()) ? fileDetails.getFileName(): creditCardCustomerEntity.getProposalNumber());
			mliEmailModel.setType(FileExtention.PDF);
			mliEmailModel.setStep(Constant.FINAL_STEP);
			mliEmailModel.setPremium(creditCardCustomerEntity.getPremium());
			mliEmailModel.setCoverage(creditCardCustomerEntity.getCoverage());
			mliEmailModel.setMailUserType(OTPUserType.YBLCCVERIFIED.getLabel());
			mliEmailModel.setProposalNumber(creditCardCustomerEntity.getProposalNumber());
			mliEmailModel.setCoverageStartDate(DateUtil.extractDateAsStringSlashFormate(creditCardCustomerEntity.getCreatedOn()));
			PaymentEntity paymentEntity = paymentDAO.findByCreditCardCustomerIdAndStatus(creditCardCustomerEntity.getCreditCardCustomerId(), "SUCCESS");
			if(!ObjectUtils.isEmpty(paymentEntity)) {
				mliEmailModel.setAmount(paymentEntity.getAmount());
				mliEmailModel.setPaymentDate(paymentEntity.getTxnDate());
				mliEmailModel.setTransactionNumber(paymentEntity.getTxnId());
				mliEmailModel.setCoverageEndDate(DateUtil.extractDateAsStringSlashFormate(paymentEntity.getPaymentOn()));
				if(!StringUtils.isEmpty(paymentEntity.getBankCode())){
					if("1340".equalsIgnoreCase(paymentEntity.getBankCode())) {
						mliEmailModel.setPaymentMode("Yes Bank Credit Card");
					} else if("1350".equalsIgnoreCase(paymentEntity.getBankCode())) {
						mliEmailModel.setPaymentMode("Yes Bank Debit Card");
					} else {
						mliEmailModel.setPaymentMode("--");
					}
				}else {
					mliEmailModel.setPaymentMode("--");
				}
			}
			File readPdfFilePath = new File(filePath);

			byte[] byteArray = new byte[(int) readPdfFilePath.length()];
			FileInputStream fileInputStream;
			try {
				fileInputStream = new FileInputStream(readPdfFilePath);
				fileInputStream.read(byteArray);
			} catch (IOException e) {
				e.printStackTrace();
			}
			String byteArrayStr = new String(Base64.encodeBase64(byteArray));
			mliEmailModel.setBytes(byteArrayStr);
			try {
				emailResponse = mliEmailService.sendEmail(mliEmailModel);
				File dir = new File(new File(filePath).getParent());
				if (!dir.isDirectory()) {
					dir.mkdirs();
				}
				Path path = Paths.get(filePath);
				Files.deleteIfExists(path);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (!("200".equalsIgnoreCase(emailResponse.getCode())
					&& ("Ok").equalsIgnoreCase(emailResponse.getStatus()))) {
				logger.error("::::::::::::::::: Mail Not Send to customer :::::::::::::::"
						+ creditCardCustomerEntity.getProposalNumber() + " at time : " + (new Date()));

			} else {
				logger.info("::::::::::::::::: Mail  Send to customer :::::::::::::::"
						+ creditCardCustomerEntity.getProposalNumber() + " at time : " + (new Date()));
			}
		}else {
			logger.info("::::::::::::::::: Error occured while sending mail :::::::::::::::");
        }
	}
	
	/**
	 * trigger sms to customer and seller
	 * @param creditCardCustomerEntity
	 * @author rajkumar
	 */
	private void sendSMSAsynchronously(CreditCardCustomerEntity creditCardCustomerEntity) {
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					try {
						triggerSMS(creditCardCustomerEntity.getPhone(), creditCardCustomerEntity, OTPType.YBLCC_CUST_APP_SUBMIT);
						logger.info("SMS trigger (Customer) successfully : " + creditCardCustomerEntity.getPhone());
						//triggerSMS(creditCardCustomerEntity.getSlrDtlId().getContactNo(), creditCardCustomerEntity,OTPType.SELLER_APP_SUBMIT);
						logger.info("SMS trigger (Seller) successfully : "+ creditCardCustomerEntity.getSlrDtlId().getContactNo());
					} catch (Exception exception) {
						exception.printStackTrace();
					}
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}
		});
		
	}
	
	/**
	 * 
	 * @param mobileNumber
	 * @param creditCardCustomerEntity
	 * @param otpType
	 * @throws Exception
	 * @author rajkumar
	 */
	public void triggerSMS(Long mobileNumber, CreditCardCustomerEntity creditCardCustomerEntity, OTPType otpType) throws Exception {
		SMSResponseModel smsResponse = sendMLISMSService.sendSmsToYBLCCCustomer(null, mobileNumber, otpType, creditCardCustomerEntity);
		if (!("OK").equalsIgnoreCase(
				smsResponse.getMliSmsServiceResponse().getResponseHeader().getGeneralResponse().getStatus())) {
			logger.error("::::::::::::::::: SMS not Send to Customer :::::::::::::::Mob=" + mobileNumber + " at time : "
					+ (new Date()));
		} else {
			logger.info("::::::::::::::::: SMS Send to Customer :::::::::::::::Mob=" + mobileNumber + " at time : "
					+ (new Date()));
		}
	}

	/**
	 * send email to customer
	 */
	private void sendPDFNdEmailAsynchronously(CreditCardCustomerEntity creditCardCustomerEntity,String filePath) {
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				sendEmailWithPDF(creditCardCustomerEntity,filePath);
				logger.info("PDF generate and Email sent successfully");
			}
		});
		
	}
	
	/**
	 * get list of yblcc customer between given date 
	 * @param fromDate
	 * @param toDate
	 * @return
	 * @author rajkumar
	 */
	@Transactional
	@Override
	public boolean getYBLCCCustomerInBulk(String fromDate, String toDate) {
		boolean customerDownloadFlag = false;
		try {
			List<CreditCardCustomerEntity> creditCardCustomerEntityList = null;
			Long to = null;
			Long from = null;
			if (fromDate != null && !fromDate.trim().isEmpty()) {
				if (toDate == null) {
					to = DateUtil.toCurrentUTCTimeStamp();
				} else {
					to = DateUtil.dateFormaterInIST(toDate + " " + Constant.END_TS, Constant.DATE_WITH_TS_FORMAT);
				}
				from = DateUtil.dateFormaterInIST(fromDate + " " + Constant.START_TS, Constant.DATE_WITH_TS_FORMAT);

			} else {
				// Get all customer which is created in last day
				to = DateUtil.addDaysWithUTCLastTS(-1);
				from = DateUtil.addDaysWithUTCLastTS(-2);
			}
			creditCardCustomerEntityList = creditCardCustomerDAO.getAllCustomerForExcel(from, to);
			excelUtility.getYBLCCExcelData(creditCardCustomerEntityList, customerExcelPath, Constant.YBLCC,
					Constant.CUSTOMER_SIMPLE_DUMP);
			customerDownloadFlag = true;
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return customerDownloadFlag;
	}

	/**
	 * save customer verification page declaration value in db
	 */
	@Transactional
	@Override
	public ResponseModel<?> saveCustDeclaration(CustomerDeclarationModel customerDeclarationModel) {
		logger.info("Enter into CreditCardJourneyServiceImpl to save customer verification declaration");
		ResponseModel<?> responseModel = new ResponseModel<UserDetailsModel>();
		try {
			if (!ObjectUtils.isEmpty(customerDeclarationModel)) {
				String proposalNumber = customerDeclarationModel.getProposalNumber();
				if (!StringUtils.isEmpty(proposalNumber)) {
					List<String> declarationValue = Arrays.asList("Agree", "Disagree");
					if (declarationValue.contains(customerDeclarationModel.getCustFirstDeclarationValue()) && declarationValue.contains(customerDeclarationModel.getCustSecondDeclarationValue())) {
						CreditCardCustomerEntity creditCardCustomerEntity = creditCardCustomerDAO.findByProposalNumber(proposalNumber);
						if (!ObjectUtils.isEmpty(creditCardCustomerEntity)) {
							creditCardCustomerEntity.setCustFirstDeclaration(customerDeclarationModel.getCustFirstDeclaration());
							creditCardCustomerEntity.setCustSecondDeclaration(customerDeclarationModel.getCustSecondDeclaration());
							creditCardCustomerEntity.setCustFirstDeclarationValue(customerDeclarationModel.getCustFirstDeclarationValue());
							creditCardCustomerEntity.setCustSecondDeclarationValue(customerDeclarationModel.getCustSecondDeclarationValue());
							creditCardCustomerDAO.saveOrUpdate(creditCardCustomerEntity);
							responseModel.setMessage(MLIMessageConstants.DECLARATION_SAVE_SUCCESS);
							responseModel.setStatus(SaveDetailsResponse.SUCCESS);
							return responseModel;
						} else {
							responseModel.setMessage(MLIMessageConstants.CUSTOMER_PROPOSAL_NOT_FOUND);
							responseModel.setStatus(SaveDetailsResponse.FAILURE);
							return responseModel;
						}
					} else {
						responseModel.setMessage(MLIMessageConstants.DECLARATION_FAILURE);
						responseModel.setStatus(SaveDetailsResponse.FAILURE);
						return responseModel;
					}
				}
			}
		} catch (Exception exception) {
			logger.error(":::::::::::::::: Error in save customer declaration value  :::::::::::::" + exception.getMessage());
			exception.printStackTrace();
		}
		return null;
	}

	/**
	 * method is used to execute cron job for sending email to Payment pending customers
	 */
	@Override
	@Transactional
	public List<CreditCardCustomerModel> executeCron() {
		logger.info("execute cron job to send email to customer......");
		List<CreditCardCustomerModel> creditCardCustomerModel = new ArrayList<CreditCardCustomerModel>();
		try {
			List<CreditCardCustomerEntity> creditCardCustomerList = creditCardCustomerDAO.findByCustAppAndPaymentStatus();
			if(!CollectionUtils.isEmpty(creditCardCustomerList)) {
				creditCardCustomerModel = Arrays.asList(modelMapper.map(creditCardCustomerList, CreditCardCustomerModel[].class));
				List<CreditCardCustomerEntity> appSentCustomerList = creditCardCustomerList.stream().filter(creditCardCustomer -> Status.APP_SENT.equals(creditCardCustomer.getAppStatus())).collect(Collectors.toList());
				List<CreditCardCustomerEntity> appVerifiedCustomerList = creditCardCustomerList.stream().filter(creditCardCustomer -> Status.APP_VERIFIED.equals(creditCardCustomer.getAppStatus())).collect(Collectors.toList());
				if (!CollectionUtils.isEmpty(appSentCustomerList)) {
					appSentCustomerList.forEach(creditCardCustomerEntity -> {
						if (!"THIRD".equals(creditCardCustomerEntity.getVerificationReminderCount())) {
							sendEmail(creditCardCustomerEntity, Status.APP_SENT.toString());
						}
					});
				}
				if (!CollectionUtils.isEmpty(appVerifiedCustomerList)) {
					appVerifiedCustomerList.forEach(creditCardCustomerEntity -> {
						if (!"THIRD".equals(creditCardCustomerEntity.getPaymentReminderCount())) {
							sendEmail(creditCardCustomerEntity, Status.APP_VERIFIED.toString());
						}
					});
				}
			}
		} catch (Exception exception) {
			logger.error(":::::::::::::::: Error in save customer declaration value  :::::::::::::" + exception.getMessage());
			exception.printStackTrace();
		}
		return creditCardCustomerModel;
	}
	
	/**
	 * method used to send email
	 * @param creditCardCustomerEntity
	 * @author rajkumar
	 */
	public void sendEmail(CreditCardCustomerEntity creditCardCustomerEntity, String appStatus) {
		ResponseModel<MliEmailServiceModel> emailResponse = new ResponseModel<MliEmailServiceModel>();
		EmailModel mliEmailModel = new EmailModel();
		mliEmailModel.setProposalNumber(creditCardCustomerEntity.getProposalNumber());
		mliEmailModel.setCustMobNumber(creditCardCustomerEntity.getPhone());
		StringBuilder fullName = new StringBuilder();
		if (creditCardCustomerEntity.getFirstName() != null) {
			fullName.append(creditCardCustomerEntity.getFirstName());
		}
		if (creditCardCustomerEntity.getLastName() != null) {
			fullName.append(" ").append(creditCardCustomerEntity.getLastName());
		}
		mliEmailModel.setCustomerName(fullName.toString());
		mliEmailModel.setMailIdTo(creditCardCustomerEntity.getEmail());
		String mailType = "";
		OTPType msgType = null;
		String verificationReminderCount = null;
		String paymentReminderCount = null;
		Long from = creditCardCustomerEntity.getModifiedOn();
		Long to = DateUtil.toCurrentUTCTimeStamp();
		Date preDate = new Date(from * 1000);
		Date nextDate = new Date(to * 1000);
		Date startPreDate = DateUtil.getStartTSDate(preDate);
		Date endPreDate = DateUtil.getEndTSDate(nextDate);
		Long preTime = startPreDate.getTime() / 1000;
		Long nextTime = endPreDate.getTime() / 1000;
		List<Long> dates = DateUtil.getAllDatesInLong(preTime, nextTime);
		logger.info("Date range size :::::::::"+dates.size());
		if (Status.APP_SENT.toString().equalsIgnoreCase(appStatus)) {
			if (!StringUtils.isEmpty(creditCardCustomerEntity.getVerificationReminderCount())) {
				if ("FIRST".equals(creditCardCustomerEntity.getVerificationReminderCount()) && dates.size() == 3) {
					mailType = Constant.YBL_VERIFICATION_PENDING2;
					msgType = OTPType.YBL_VERIFICATION_PENDING2;
					verificationReminderCount = "SECOND";
				}
				if ("SECOND".equals(creditCardCustomerEntity.getVerificationReminderCount()) && dates.size() == 4) {
					mailType = Constant.YBL_VERIFICATION_PENDING3;
					msgType = OTPType.YBL_VERIFICATION_PENDING3;
					verificationReminderCount = "THIRD";
				}
			} else {
				mailType = Constant.YBL_VERIFICATION_PENDING1;
				msgType = OTPType.YBL_VERIFICATION_PENDING1;
				verificationReminderCount = "FIRST";
			}
		} else if (Status.APP_VERIFIED.toString().equalsIgnoreCase(appStatus)) {
			if (!StringUtils.isEmpty(creditCardCustomerEntity.getPaymentReminderCount())) {
				if ("FIRST".equals(creditCardCustomerEntity.getPaymentReminderCount()) && dates.size() == 3) {
					mailType = Constant.YBL_PAYMENT_PENDING2;
					msgType = OTPType.YBL_PAYMENT_PENDING2;
					paymentReminderCount = "SECOND";
				}
				if ("SECOND".equals(creditCardCustomerEntity.getPaymentReminderCount()) && dates.size() == 4) {
					mailType = Constant.YBL_PAYMENT_PENDING3;
					msgType = OTPType.YBL_PAYMENT_PENDING3;
					paymentReminderCount = "THIRD";
				}
			} else {
				mailType = Constant.YBL_PAYMENT_PENDING1;
				msgType = OTPType.YBL_PAYMENT_PENDING1;
				paymentReminderCount = "FIRST";
			}
		}
		mliEmailModel.setMailUserType(mailType);
		try {
			if(!StringUtils.isEmpty(mailType)) {
			   emailResponse = mliEmailService.sendEmail(mliEmailModel);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!("200".equalsIgnoreCase(emailResponse.getCode())
				&& ("Ok").equalsIgnoreCase(emailResponse.getStatus()))) {
			logger.error("::::::::::::::::: Mail Not Send to customer :::::::::::::::"
					+ creditCardCustomerEntity.getProposalNumber() + " at time : " + (new Date()));

		} else {
			logger.info("::::::::::::::::: Mail  Send to customer :::::::::::::::"
					+ creditCardCustomerEntity.getProposalNumber() + " at time : " + (new Date()));
		}
		SMSResponseModel smsResponse = new SMSResponseModel();
		try {
			if (!ObjectUtils.isEmpty(msgType)) {
				smsResponse = sendMLISMSService.sendSmsToYBLCCCustomer(null, creditCardCustomerEntity.getPhone(),msgType, creditCardCustomerEntity);
				if (!("OK").equalsIgnoreCase(
						smsResponse.getMliSmsServiceResponse().getResponseHeader().getGeneralResponse().getStatus())) {
					logger.error("::::::::::::::::: SMS not Send to Customer :::::::::::::::Mob="
							+ creditCardCustomerEntity.getPhone() + " at time : " + (new Date()));
				} else {
					logger.info("::::::::::::::::: SMS Send to Customer :::::::::::::::Mob="
							+ creditCardCustomerEntity.getPhone() + " at time : " + (new Date()));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if("200".equalsIgnoreCase(emailResponse.getCode())
				&& ("Ok").equalsIgnoreCase(emailResponse.getStatus()) && smsResponse.getMliSmsServiceResponse() != null &&
				("OK").equalsIgnoreCase(
						smsResponse.getMliSmsServiceResponse().getResponseHeader().getGeneralResponse().getStatus())) {
			if (!StringUtils.isEmpty(verificationReminderCount)) {
				creditCardCustomerEntity.setVerificationReminderCount(verificationReminderCount);
				creditCardCustomerEntity.setModifiedOn(DateUtil.toCurrentUTCTimeStamp());
				creditCardCustomerDAO.saveOrUpdate(creditCardCustomerEntity);
			}
			if (!StringUtils.isEmpty(paymentReminderCount)) {
				creditCardCustomerEntity.setPaymentReminderCount(paymentReminderCount);
				creditCardCustomerEntity.setModifiedOn(DateUtil.toCurrentUTCTimeStamp());
				creditCardCustomerDAO.saveOrUpdate(creditCardCustomerEntity);
			}
		}
		
	}
	
	
	/**
	 * cron is used for send application status ,currently cron job is disabled
	 * 
	 * @author rajkumar
	 */
	@Override
	//@Scheduled(cron = "${mli.cron.customer}")
	@Transactional
	public void sendEmailToBank() {
		try {
			executeCron();
			logger.info("::::::: Excel sent from SERVER1 ::::::::::::  ");
		} catch (Exception e) {
			logger.error("::::::: Error in send excel from SERVER1  ");
		}
	}
}
