package com.mli.service.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;
import com.mli.constants.Constant;
import com.mli.constants.DocType;
import com.mli.constants.FileExtention;
import com.mli.constants.MLIMessageConstants;
import com.mli.dao.AdminProductConfigDao;
import com.mli.dao.AppointeeDAO;
import com.mli.dao.CamReportDetailsDao;
import com.mli.dao.CommonFileUploadDao;
import com.mli.dao.Covid19Dao;
import com.mli.dao.CovidReportDAO;
import com.mli.dao.CreditCardCustomerDAO;
import com.mli.dao.CustomerDetailsDAO;
import com.mli.dao.HealthDeclarationDAO;
import com.mli.dao.MandatoryDeclarationDAO;
import com.mli.dao.MasterOptionDao;
import com.mli.dao.MasterReflexiveQuestionDao;
import com.mli.dao.NomineeDetailsDAO;
import com.mli.dao.ProposalNumberDAO;
import com.mli.dao.ProposerDetailDao;
import com.mli.dao.ReflexiveAnswerDao;
import com.mli.dao.SFQCovidDAO;
import com.mli.dao.SFQHealthDeclarationDAO;
import com.mli.dao.SellerDAO;
import com.mli.entity.AdminProductConfigEntity;
import com.mli.entity.AppointeeDetailsEntity;
import com.mli.entity.CamReportDetailsEntity;
import com.mli.entity.CommonFileUploadEntity;
import com.mli.entity.Covid19Entity;
import com.mli.entity.CovidReportEntity;
import com.mli.entity.CreditCardCustomerEntity;
import com.mli.entity.CustomerDetailsEntity;
import com.mli.entity.HealthDeclarationEntity;
import com.mli.entity.MandatoryDeclarationEntity;
import com.mli.entity.MasterOptionEntity;
import com.mli.entity.MasterReflexiveQuestionEntity;
import com.mli.entity.NomineeDetailsEntity;
import com.mli.entity.ProposalNumberEntity;
import com.mli.entity.ProposerDetailEntity;
import com.mli.entity.ReflexiveAnswerEntity;
import com.mli.entity.SFQCovidEntity;
import com.mli.entity.SFQHealthDeclarationEntity;
import com.mli.entity.SelectOptionEntity;
import com.mli.entity.SellerDetailEntity;
import com.mli.enums.OTPType;
import com.mli.enums.OTPUserType;
import com.mli.enums.RiderType;
import com.mli.enums.Status;
import com.mli.exception.AgeException;
import com.mli.exception.StorageException;
import com.mli.filemaster.FileUtilityModel;
import com.mli.filemaster.SchemeDirDao;
import com.mli.filemaster.SchemeDirEntity;
import com.mli.filter.FileTypeTest;
import com.mli.helper.AppointeeDetailsHelper;
import com.mli.helper.CustomerDetailsHelper;
import com.mli.helper.FileUtilityConverter;
import com.mli.helper.HealthDeclarationHelper;
import com.mli.helper.MandatoryDeclarationHelper;
import com.mli.helper.NomineeDetailsHelper;
import com.mli.helper.SFQHealthDeclarationHelper;
import com.mli.helper.UserDetailsConverter;
import com.mli.modal.CamResponseModel;
import com.mli.modal.FileUploadModel;
import com.mli.modal.email.EmailModel;
import com.mli.modal.email.MliEmailServiceModel;
import com.mli.model.AnswerModel;
import com.mli.model.AppointeeModel;
import com.mli.model.CSModel;
import com.mli.model.CovidReportModel;
import com.mli.model.Covid_19Model;
import com.mli.model.CustomerDetailsModel;
import com.mli.model.HealthAnswerModel;
import com.mli.model.HealthDeclarationModel;
import com.mli.model.MandatoryDeclarationModel;
import com.mli.model.NationalityDetail;
import com.mli.model.NomineeDetailsModel;
import com.mli.model.PageInfoModel;
import com.mli.model.ProposerDetailModel;
import com.mli.model.SFQCovidModel;
import com.mli.model.SFQHealthDeclarationModel;
import com.mli.model.SelectedOptionModel;
import com.mli.model.UserDetailsModel;
import com.mli.model.response.ResponseModel;
import com.mli.model.response.SaveDetailsResponse;
import com.mli.model.response.SaveUpdateResponse;
import com.mli.model.sms.SMSResponseModel;
import com.mli.service.AdminProductConfigService;
import com.mli.service.CustomerDetailService;
import com.mli.service.MliEmailService;
import com.mli.service.ReflexiveQuestionService;
import com.mli.service.SSOManagementService;
import com.mli.service.SendMLISMSService;
import com.mli.utils.AES;
import com.mli.utils.DateUtil;
import com.mli.utils.NumberParser;
import com.mli.utils.ObjectsUtil;
import com.mli.utils.PageableUtil;
import com.mli.utils.aws.AwsFileUtility;
import com.mli.utils.excel.ExcelGeneratorUtility;
import com.mli.utils.pdf.PdfGenaratorUtil;

/**
 * @author Nikhilesh.Tiwari
 *
 */
@Service
public class CustomerDetailServiceImpl implements CustomerDetailService {

	private static final Logger logger = Logger.getLogger(CustomerDetailServiceImpl.class);

	@Autowired
	private CustomerDetailsDAO customerDetailsDAO;

	@Autowired
	private CamReportDetailsDao camReportDetailsDao;

	@Autowired
	private NomineeDetailsDAO nomineeDetailsDAO;

	@Autowired
	private AppointeeDAO appointeeDAO;

	@Autowired
	private HealthDeclarationDAO healthDeclarationDAO;
	
	@Autowired
	private SFQHealthDeclarationDAO sfqHealthDeclarationDAO;

	@Autowired
	private MandatoryDeclarationDAO mandatoryDeclarationDAO;

	@Autowired
	private ProposalNumberDAO proposalNumberDAO;

	@Autowired
	private CustomerDetailsHelper customerDetailsHelper;

	@Autowired
	private NomineeDetailsHelper nomineeDetailsHelper;

	@Autowired
	private AppointeeDetailsHelper appointeeDetailsHelper;

	@Autowired
	private HealthDeclarationHelper healthDeclarationHelper;
	
	@Autowired
	private SFQHealthDeclarationHelper sfqHealthDeclarationHelper;
	
	@Autowired
	private SFQCovidDAO sfqCovidDAO;

	@Autowired
	private MandatoryDeclarationHelper mandatoryDeclarationHelper;

	@Autowired
	private MliEmailService mliEmailService;

	@Autowired
	private SendMLISMSService sendMLISMSService;

	@Autowired
	private SellerDAO sellerDAO;

	@Autowired
	private CustomerDetailService customerDetailService;

	@Autowired
	private PdfGenaratorUtil pdfGenaratorUtil;

	@Value("#{'${upload.cdf.extension}'.split(',')}")
	private List<String> fileExtension;

	@Value("#{'${upload.cdf.extension}'}")
	private String fileExtensionString;

	@Value("#{'${doc.temp}'}")
	private String tempFilePath;

	@Autowired
	private AwsFileUtility awsFileUtility;

	@Autowired
	private MasterOptionDao masterOptionDao;

	@Autowired
	private MasterReflexiveQuestionDao masterReflexiveQuestionDao;

	@Autowired
	private ReflexiveAnswerDao answerDao;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private ProposerDetailDao proposerDetailDao;

	@Autowired
	private ReflexiveQuestionService reflexiveQuestionService;

	@Autowired
	private SchemeDirDao schemeDirDao;

	@Autowired
	private SessionFactory sessionFactory;

	@Value("${mli.aes.encryption.key}")
	private String aesSecratKey;

	@Autowired
	private ExcelGeneratorUtility excelUtility;

	@Value("${mli.download.customer.excel}")
	private String customerExcelPath;

	@Value("${mli.cam.report}")
	private String accessPath;
	
	
	@Value("${mli.physical.form}")
	private String physicalFormAcessPath;
	
	@Value("${mli.covid.report}")
	private String covidReportAccessPath;
	
	@Autowired
	private AdminProductConfigService adminProductConfigService;
	
	@Autowired
	private AdminProductConfigDao adminProductConfigDao;
	
	@Autowired
	private CommonFileUploadDao commonFileUploadDao;
	
	@Autowired
	private CovidReportDAO covidReportDAO;
	
	@Autowired
	private Covid19Dao covid19Dao;
	
	@Autowired
	private CreditCardCustomerDAO creditCardCustomerDAO;
	
	@Autowired
	private SSOManagementService  ssoManagementService;
	
	public Session getSession() {
		return sessionFactory.getCurrentSession();
	}

	/**
	 * Save Customer's all steps Details
	 */
	@Override
	@Transactional
	public ResponseModel<UserDetailsModel> saveCustomerDetails(UserDetailsModel userDetails, Integer steps,
			String proposalNumber) throws Exception {
		logger.info("::::::::::::::::::::  user details saving start step : " + steps + "      :::::::::::::");
		// CamResponseModel camResponseModel=new CamResponseModel();
		ResponseModel<UserDetailsModel> responseModel = new ResponseModel<UserDetailsModel>();
		Integer currentStep = steps;
		Long customerId = null;
		String newProposalNumber = null;
		CustomerDetailsEntity custDetails = new CustomerDetailsEntity();
		SellerDetailEntity sellerDetails = new SellerDetailEntity();
		try {
			logger.info("userDetails.getCustomerDetails().getCustomerDtlId() : " + customerId);
			if (!Status.STEP1.getValue().equals(currentStep)) {
				if (proposalNumber != null) {
					custDetails = customerDetailsDAO.findByProposalNumber(proposalNumber);
				}
				if (ObjectsUtil.isNull(custDetails)) {
					responseModel.setMessage(MLIMessageConstants.NO_USER_WRT_PROPOSALNO + proposalNumber);
					responseModel.setStatus(SaveUpdateResponse.FAILURE);
					return responseModel;
				} else if (!ObjectsUtil.isNull(custDetails.getCustomerDtlId())) {
					customerId = custDetails.getCustomerDtlId();
					proposalNumber = custDetails.getProposalNumber();
				}
			} else if (Status.STEP1.getValue().equals(currentStep) && ObjectsUtil.isNull(proposalNumber)) {
				boolean isloanAppo = checkLoanAppNumberValidation(userDetails.getCustomerDetails().getLoanAppNumber(),
						proposalNumber);
				if (isloanAppo) {
					responseModel.setMessage(MLIMessageConstants.DUPLICATE_LOAN_APP_NO);
					responseModel.setStatus(SaveDetailsResponse.FAILURE);
					return responseModel;
				} else {
					newProposalNumber = genearteProposalNumber();
					sellerDetails = sellerDAO.findByContactNo(Long.parseLong(userDetails.getSellerContNumber()));
					if (ObjectsUtil.isNull(sellerDetails)) {
						responseModel.setMessage(MLIMessageConstants.SELLER_CONTACT_NOT_FOUND);
						responseModel.setStatus(SaveDetailsResponse.FAILURE);
						return responseModel;
					}
				}

			} else if (Status.STEP1.getValue().equals(currentStep) && !ObjectsUtil.isNull(proposalNumber)) {
				boolean isloanAppo = checkLoanAppNumberValidation(userDetails.getCustomerDetails().getLoanAppNumber(),
						proposalNumber);
				if (isloanAppo) {
					responseModel.setMessage(MLIMessageConstants.DUPLICATE_LOAN_APP_NO);
					responseModel.setStatus(SaveDetailsResponse.FAILURE);
					return responseModel;
				} else {
					custDetails = customerDetailsDAO.findByProposalNumber(proposalNumber);
					if (ObjectsUtil.isNull(custDetails)) {
						responseModel.setMessage(MLIMessageConstants.PROPOSAL_NOT_FOUND);
						responseModel.setStatus(SaveDetailsResponse.FAILURE);
						return responseModel;
					} else {
						customerId = custDetails.getCustomerDtlId();
					}

				}
				/*
				 * if (custDetails != null) {
				 * logger.info("::::::Duplicate loan application number " +
				 * custDetails.getLoanAppNumber()); }
				 */
			} else if (currentStep.equals(Status.NRI.getValue())) {
				custDetails = customerDetailsDAO.findByProposalNumber(proposalNumber);
			}

			switch (currentStep) {
			case 1:
				boolean ageValidationCust = checkAgeGreaterThan18(userDetails.getCustomerDetails().getDob());
				if (!ObjectsUtil.isNull(newProposalNumber)) {
					userDetails.setProposalNumber(newProposalNumber);
					custDetails = customerDetailsHelper.convertToEntity(userDetails.getCustomerDetails());
					custDetails.setProposalNumber(newProposalNumber);
					custDetails.setSlrDtlId(sellerDetails);
				} else {
					custDetails = UserDetailsConverter.convertIntoCustomerDetailsEntity(
							userDetails.getCustomerDetails(), custDetails, proposalNumber);
				}
				// Changed by Devendra.Kumar
				boolean nationality = false; // from front end
				if (nationality) {
					custDetails.setFileUploadPath("");
				}
				logger.info("Code of LoanType : " + userDetails.getCustomerDetails().getLoanType());
				if (!ageValidationCust) {
					if (userDetails.getCustomerDetails().getProposerDetails() == null) {
						responseModel.setMessage(MLIMessageConstants.PROPOSER_VALIDATION);
						responseModel.setStatus(SaveDetailsResponse.FAILURE);
						return responseModel;
					}
				}
				// plz don't remove this commented code
				// Delete saved answer of Previous Occupation
				/*
				 * logger.info("Previous Occupation : "+custDetails.getOccupation()
				 * +" , new Occupation : "+ userDetails.getCustomerDetails().getOccupation());
				 * if (custDetails.getOccupation() != null ) {
				 * logger.info("Deleting answer of Previous Occupation : " +
				 * custDetails.getOccupation()); List<ReflexiveAnswerEntity> entity = answerDao
				 * .findByCustomerIdAndType(custDetails.getCustomerDtlId(),Constant.OCCUPATION);
				 * logger.info("Number of answer deleting for occupation : "+entity.size());
				 * deletePreviousAnswer(entity); }
				 */
				// if nationality changed then also delete reflexive question for previous
				// nationality
				logger.info("Previous Nationality : " + custDetails.getNationality() + " , new Occupation : "
						+ userDetails.getCustomerDetails().getNationality());
				if (!userDetails.getCustomerDetails().getNationality().equalsIgnoreCase(custDetails.getNationality())) {
					logger.info("Delete reflexive question for previous nationality at step 1");
					List<ReflexiveAnswerEntity> entity = answerDao
							.findByCustomerIdAndType(custDetails.getCustomerDtlId(), Constant.NATIONALITY);
					logger.info("Number of answer deleting for nationality : " + entity.size());
					deletePreviousAnswer(entity);

					// Delete passport from S3 if exist
					SchemeDirEntity schemeDirEntity = schemeDirDao.findByProposalNumberNdeDocType(proposalNumber,
							DocType.PASSPORT);
					if (schemeDirEntity != null) {
						logger.info("deleted file path of AWS " + schemeDirEntity.getAwsFilePath());
						awsFileUtility.deleteFileOnAWSS3(schemeDirEntity.getAwsFilePath());
						schemeDirDao.delete(schemeDirEntity);
					}
					// this key used by front end
					userDetails.getCustomerDetails().setFlushNationality(true);
				}
				// save nationality and occupation
				custDetails.setNationality(userDetails.getCustomerDetails().getNationality());

				// plz don't remove this line
//					custDetails.setOccupation(userDetails.getCustomerDetails().getOccupation());
				custDetails.setOtherPalce(userDetails.getCustomerDetails().getOtherPlace());
				Long custId = 0l;
				custDetails.setModifiedOn(DateUtil.toCurrentUTCTimeStamp());
				customerDetailsDAO.saveOrUpdate(custDetails);
				custId = custDetails.getCustomerDtlId();
				logger.info("::::::::At Step 1 Update, CustomerId is " + custId);
				logger.info("ageValidationCust :" + ageValidationCust);
				if (!ageValidationCust) {
					CustomerDetailsEntity entity = customerDetailsDAO.getEntity(CustomerDetailsEntity.class, custId);
					ProposerDetailEntity proposerDetails = entity.getProposerEntity();
					if (proposerDetails == null) {
						proposerDetails = modelMapper.map(userDetails.getCustomerDetails().getProposerDetails(),
								ProposerDetailEntity.class);
						proposerDetails.setCreatedOn(DateUtil.toCurrentUTCTimeStamp());
						logger.info("::::::::::Saved Proposer::::::::::::");
					} else {
						entity.setModifiedOn(DateUtil.toCurrentUTCTimeStamp());
						ProposerDetailModel proDetailModel = userDetails.getCustomerDetails().getProposerDetails();
						proposerDetails.setDateOfBirth(proDetailModel.getDateOfBirth());
						proposerDetails.setGender(proDetailModel.getGender());
						proposerDetails.setModifiedOn(DateUtil.toCurrentUTCTimeStamp());
						proposerDetails.setProposerFirstName(proDetailModel.getProposerFirstName());
						proposerDetails.setProposerLastName(proDetailModel.getProposerLastName());
						proposerDetails.setRelationWithAssured(proDetailModel.getRelationWithAssured());
						proposerDetails.setRelationWithProposer(proDetailModel.getRelationWithProposer());
						logger.info("::::::::::Updated Proposer::::::::::::");
					}
					proposerDetails.setCustomerDetailsEntity(entity);
					proposerDetailDao.saveOrUpdate(proposerDetails);
					logger.info(":::::::::::::Proposer Saved successfully:::::::::::::::::");
				}
				Object customerSavedAnswer = null;
				List<ReflexiveAnswerEntity> savedAnswer = null;
				// plz don't remove this code
				/*
				 * if (userDetails.getCustomerDetails().getOccupationAnswerModel() != null) {
				 * savedAnswer = saveRefQuestionAnswer(userDetails.getCustomerDetails().
				 * getOccupationAnswerModel(), custId); customerSavedAnswer =
				 * reflexiveQuestionService.convertToModelSavedAnswer(savedAnswer);
				 * userDetails.getCustomerDetails().setSavedAnswers(customerSavedAnswer); }
				 */

				logger.info(":::::::::::::::::: custId::::::::" + custId);
				userDetails.getCustomerDetails().setFinancePremium(custDetails.getIsFinancePremium());
				userDetails.getCustomerDetails().setBaseOrBaseWithCI(custDetails.getBaseOrBaseWithCI());
				userDetails.getCustomerDetails().setPercentageOfSumAssured(ObjectsUtil.isNotNull(custDetails.getPercentageOfSumAssured()) ?  custDetails.getPercentageOfSumAssured() : null);
				userDetails.getCustomerDetails().setCiOption(ObjectsUtil.isNotNull(custDetails.getCiOption()) ?  custDetails.getCiOption() : null);
				userDetails.getCustomerDetails().setCiTenureYears(ObjectsUtil.isNotNull(custDetails.getCiTenureYears()) ?  custDetails.getCiTenureYears() : null);
				userDetails.getCustomerDetails().setCiRiderSumAssured(custDetails.getCiRiderSumAssured());
				userDetails.getCustomerDetails().setCiRiderPermium(custDetails.getCiRiderPermium());
				userDetails.getCustomerDetails().setBaseWithCIPremium(custDetails.getBaseWithCIPremium());
				userDetails.getCustomerDetails().setMedicalUWCI(custDetails.getMedicalUWCI());
				userDetails.getCustomerDetails().setFinancialUWCI(custDetails.getFinancialUWCI());
				userDetails.getCustomerDetails().setOverallUWFinancial(custDetails.getOverallUWFinancial());
				userDetails.getCustomerDetails().setOverallUWMedical(custDetails.getOverallUWMedical());
				userDetails.getCustomerDetails().setTentativeEMIBaseWithCI(custDetails.getTentativeEMIBaseWithCI());
				userDetails.getCustomerDetails().setIncrementalEMIBaseWithCI(custDetails.getIncrementalEMIBaseWithCI());
				userDetails.getCustomerDetails().setGender(custDetails.getGender());
				userDetails.getCustomerDetails().setHealthType(custDetails.getHealthType() == null ? Constant.HEALTH_TYPE_DOGH:Constant.HEALTH_TYPE_SFQ);
				userDetails.setSteps(Status.STEP1.getValue());
				userDetails.getCustomerDetails().setFormStatus(custDetails.getFormStatus());
				responseModel.setData(userDetails);
				responseModel.setMessage(MLIMessageConstants.SAVE_STEP_1);
				responseModel.setStatus(SaveDetailsResponse.SUCCESS);
				return responseModel;
			case 2:
				NomineeDetailsEntity nomineeDtls = new NomineeDetailsEntity();
				AppointeeDetailsEntity appointeeDtls = new AppointeeDetailsEntity();
				nomineeDtls = nomineeDetailsDAO.findByCustomerDtlId(customerId);
				if (ObjectsUtil.isNull(nomineeDtls)) {
					nomineeDtls = nomineeDetailsHelper.convertToEntity(userDetails.getNomineeDetails());
					nomineeDtls.setCustomerDtlId(custDetails);
					nomineeDetailsDAO.saveOrUpdate(nomineeDtls);
					if (userDetails.getNomineeDetails().getIsAppointee()
							&& !ObjectsUtil.isNull(userDetails.getNomineeDetails().getAppointeeDetails())) {
						nomineeDtls = nomineeDetailsDAO.findByCustomerDtlId(customerId);
						boolean ageValidationNominee = checkAgeGreaterThan18(
								userDetails.getNomineeDetails().getAppointeeDetails().getDateOfBirth());
						if (!ageValidationNominee) {
							responseModel.setMessage(MLIMessageConstants.AGE_VALIDATION);
							responseModel.setStatus(SaveDetailsResponse.FAILURE);
							return responseModel;
						} else {
							appointeeDtls = appointeeDetailsHelper
									.convertToEntity(userDetails.getNomineeDetails().getAppointeeDetails());
							appointeeDtls.setNomineeDtlId(nomineeDtls);
							appointeeDAO.saveOrUpdate(appointeeDtls);
						}
					}
				} else if (!ObjectsUtil.isNull(nomineeDtls)) {
					nomineeDtls = UserDetailsConverter.toUpdateNomineeDetails(nomineeDtls,
							userDetails.getNomineeDetails());
					nomineeDetailsDAO.saveOrUpdate(nomineeDtls);
					if (userDetails.getNomineeDetails().getIsAppointee()
							&& !ObjectsUtil.isNull(userDetails.getNomineeDetails().getAppointeeDetails())) {
						appointeeDtls = appointeeDAO.findByNomineeDtlId(nomineeDtls.getNomineeDtlId());
						boolean ageValidationNominee = checkAgeGreaterThan18(
								userDetails.getNomineeDetails().getAppointeeDetails().getDateOfBirth());
						if (!ageValidationNominee) {
							responseModel.setMessage(MLIMessageConstants.AGE_VALIDATION);
							responseModel.setStatus(SaveDetailsResponse.FAILURE);
							return responseModel;
						} else {
							if (!ObjectsUtil.isNull(appointeeDtls)) {
								appointeeDtls = UserDetailsConverter.toUpdateAppointeedetails(appointeeDtls,
										userDetails.getNomineeDetails().getAppointeeDetails());
							} else {
								appointeeDtls = appointeeDetailsHelper
										.convertToEntity(userDetails.getNomineeDetails().getAppointeeDetails());
								appointeeDtls.setNomineeDtlId(nomineeDtls);
							}
							appointeeDAO.saveOrUpdate(appointeeDtls);
						}
					}
				}

				updateCustomerDetailsSteps(custDetails, currentStep);

				userDetails.setProposalNumber(proposalNumber);
				if(custDetails.getFormStatus()!=null && custDetails.getFormStatus().equals("Physical_Form_Verification")) {
					responseModel.setMessage(MLIMessageConstants.SENT_FOR_VERIFICATION);
					userDetails.setSteps(Status.STEP2.getValue());
					//custDetails.setStatus(Status.PHYSICAL_FORM_VERIFICATION);
				}else {
					responseModel.setMessage(MLIMessageConstants.SAVE_STEP_2);
					userDetails.setSteps(Status.STEP2.getValue());
				}
				
				responseModel.setData(userDetails);
				responseModel.setStatus(SaveDetailsResponse.SUCCESS);
				return responseModel;
			case 3:
				if (Constant.HEALTH_TYPE_SFQ.equals(custDetails.getHealthType())) {
					// SFQ health declaration
					SFQHealthDeclarationEntity sfqHealthDeclarationEntity = sfqHealthDeclarationDAO.findByCustomerDtlId(customerId);
					AdminProductConfigEntity adminProductConfigEntity = adminProductConfigService.getBySellerContactNumber(Long.parseLong(userDetails.getSellerContNumber()));
					SFQCovidEntity sfqCovidEntity = null;
					if (!ObjectUtils.isEmpty(sfqHealthDeclarationEntity)) {
						sfqHealthDeclarationEntity = sfqHealthDeclarationHelper.updateSFQHealthEntity(sfqHealthDeclarationEntity, userDetails.getSfqHealthDeclaration());
						sfqHealthDeclarationEntity.setModifiedBy(sellerDetails.getSellerName());
						sfqHealthDeclarationEntity.setModifiedOn(DateUtil.toCurrentUTCTimeStamp());
						if(!ObjectUtils.isEmpty(adminProductConfigEntity) && Constant.YES.equals(adminProductConfigEntity.getIsCovidEnable())) {
							sfqCovidEntity = sfqCovidDAO.findBySFQHealthDtlId(sfqHealthDeclarationEntity.getHealthDclId());
							if (!ObjectUtils.isEmpty(sfqCovidEntity) && userDetails.getSfqHealthDeclaration().getSfqCovidModel() != null) {
								sfqCovidEntity = sfqHealthDeclarationHelper.updateSFQCovidEntity(sfqCovidEntity,
										userDetails.getSfqHealthDeclaration().getSfqCovidModel());
							}
						}
					} else {
						sfqHealthDeclarationEntity = sfqHealthDeclarationHelper.convertToEntity(userDetails.getSfqHealthDeclaration());
						sfqHealthDeclarationEntity.setCreatedBy(sellerDetails.getSellerName());
						sfqHealthDeclarationEntity.setCreatedOn(DateUtil.toCurrentUTCTimeStamp());
						sfqHealthDeclarationEntity.setCustomerDtlId(custDetails);
						if(!ObjectUtils.isEmpty(userDetails.getSfqHealthDeclaration().getSfqCovidModel())) {
							sfqCovidEntity = sfqHealthDeclarationHelper.getSFQCovidEntity(userDetails.getSfqHealthDeclaration().getSfqCovidModel());
						}
					}
					sfqHealthDeclarationDAO.saveOrUpdate(sfqHealthDeclarationEntity);
					if(!ObjectUtils.isEmpty(adminProductConfigEntity) && Constant.YES.equals(adminProductConfigEntity.getIsCovidEnable())) {
						if(!ObjectUtils.isEmpty(sfqCovidEntity)) {
							sfqHealthDeclarationEntity = sfqHealthDeclarationDAO.findByCustomerDtlId(customerId);
							sfqCovidEntity.setSfqHealthDeclarationEntity(sfqHealthDeclarationEntity);
						    sfqCovidDAO.saveOrUpdate(sfqCovidEntity);
						}
					}
				}else {
					// DOGH health declaration
					HealthDeclarationEntity healthDetails = new HealthDeclarationEntity();
					Covid19Entity covid19Entity = new Covid19Entity();
					healthDetails = healthDeclarationDAO.findByCustomerDtlId(customerId);
					AdminProductConfigEntity adminProductConfigEntity = adminProductConfigService.getBySellerContactNumber(Long.parseLong(userDetails.getSellerContNumber()));
					if (ObjectsUtil.isNull(healthDetails)) {
						healthDetails = healthDeclarationHelper.convertToEntity(userDetails.getHealthDeclaration());
						healthDetails.setCustomerDtlId(custDetails);
						if(!ObjectUtils.isEmpty(userDetails.getHealthDeclaration().getCovid_19Details())) {
							covid19Entity = healthDeclarationHelper.getCovid19Entity(userDetails.getHealthDeclaration().getCovid_19Details());
						}
					} else if (!ObjectsUtil.isNull(healthDetails)) {
						healthDetails = UserDetailsConverter.toUpdateHealthDetails(healthDetails,
								userDetails.getHealthDeclaration());
						if(!ObjectUtils.isEmpty(adminProductConfigEntity) && Constant.YES.equals(adminProductConfigEntity.getIsCovidEnable())) {
							covid19Entity = covid19Dao.findByHealthDtlId(healthDetails.getHealthDclId());
							if (!ObjectUtils.isEmpty(covid19Entity)) {
								covid19Entity = healthDeclarationHelper.updateCovid19Entity(covid19Entity,
										userDetails.getHealthDeclaration().getCovid_19Details());
							}
						}
					}
	
					if (userDetails.getHealthDeclaration().getHealthAnswerModel() != null) {
						setTriggerMsg(healthDetails, userDetails.getHealthDeclaration().getHealthAnswerModel());
					}
					healthDeclarationDAO.saveOrUpdate(healthDetails);
					if(!ObjectUtils.isEmpty(adminProductConfigEntity) && Constant.YES.equals(adminProductConfigEntity.getIsCovidEnable())) {
						if(!ObjectUtils.isEmpty(covid19Entity)) {
							healthDetails = healthDeclarationDAO.findByCustomerDtlId(customerId);
							covid19Entity.setHealthDeclarationEntity(healthDetails);
						    covid19Dao.saveOrUpdate(covid19Entity);
						}
					}
					// if health declaration is true then reflexive question is saved
					savedAnswer = new ArrayList<>();
					customerSavedAnswer = new ArrayList<>();
					if (userDetails.getHealthDeclaration() != null) {
						List<ReflexiveAnswerEntity> entity = answerDao
								.findByCustomerIdAndType(custDetails.getCustomerDtlId(), Constant.DISEASE);
						deletePreviousAnswer(entity);
						if (userDetails.getHealthDeclaration().getHealthAnswerModel() != null
								&& !userDetails.getHealthDeclaration().getHealthAnswerModel().isEmpty()) {
							savedAnswer = saveRefQuestionAnswer(userDetails.getHealthDeclaration().getHealthAnswerModel(),
									custDetails.getCustomerDtlId());
							customerSavedAnswer = reflexiveQuestionService.convertToModelSavedAnswer(savedAnswer);
						}
					}
					userDetails.getHealthDeclaration().setSavedAnswers(customerSavedAnswer);
				}
				updateCustomerDetailsSteps(custDetails, currentStep);
				userDetails.setProposalNumber(proposalNumber);
				userDetails.setSteps(Status.STEP3.getValue());
				responseModel.setData(userDetails);
				responseModel.setMessage(MLIMessageConstants.SAVE_STEP_3);
				responseModel.setStatus(SaveDetailsResponse.SUCCESS);
				savedAnswer = null;
				customerSavedAnswer = null;
				return responseModel;
			case 4:
				MandatoryDeclarationEntity mandatoryDetails = new MandatoryDeclarationEntity();
				mandatoryDetails = mandatoryDeclarationDAO.findbyCustomerDtlId(customerId);
				if (ObjectsUtil.isNull(mandatoryDetails)) {
					mandatoryDetails = mandatoryDeclarationHelper
							.convertToEntity(userDetails.getMandatoryDeclaration());
					mandatoryDetails.setCustomerDtlId(custDetails);

				} else if (!ObjectsUtil.isNull(mandatoryDetails)) {
					mandatoryDetails = UserDetailsConverter.toUpdateMandatoryDetails(mandatoryDetails,
							userDetails.getMandatoryDeclaration());
				}
				mandatoryDeclarationDAO.saveOrUpdate(mandatoryDetails);
				// Generate PDF and send in email.
//				this.generateAndEmailPDF(custDetails);
				updateCustomerDetailsSteps(custDetails, currentStep);
				String status = "";
				if (custDetails.getStatus().equals(Status.APP_PENDING)) {
					status = Constant.WIP;
				}
				userDetails.setStatus(status);
				userDetails.setProposalNumber(proposalNumber);
				userDetails.setSteps(Status.STEP4.getValue());
				responseModel.setData(userDetails);
				responseModel.setMessage(MLIMessageConstants.SAVE_STEP_4);
				responseModel.setStatus(SaveDetailsResponse.SUCCESS);
				return responseModel;
			case 5:
				// Sent for verification
				logger.info("*****At Step 5*************");
				custDetails = customerDetailsDAO.findByProposalNumber(userDetails.getProposalNumber());

				if (custDetails != null && ObjectsUtil.isNotNull(custDetails.getBaseOrBaseWithCI())
						&& custDetails.getBaseOrBaseWithCI().equalsIgnoreCase(RiderType.BASE_CI_RIDER.getLabel())) {
					HealthDeclarationEntity healthDeclarationDetail = new HealthDeclarationEntity();
					healthDeclarationDetail = healthDeclarationDAO.findByCustomerDtlId(customerId);
					if (!ObjectsUtil.isNull(healthDeclarationDetail)
							&& ObjectsUtil.isNull(healthDeclarationDetail.getHealthDeclarationForCIRider())) {
						responseModel.setData(userDetails);
						responseModel.setMessage(MLIMessageConstants.CI_RIDER_VALIDATION);
						responseModel.setStatus(SaveDetailsResponse.FAILURE);
						return responseModel;
					}
				}
				if (custDetails.getCustEmailId() != null && !custDetails.getCustEmailId().trim().isEmpty()) {
					// send email(sent for verification) without attachement
					this.generateAndEmailPDF(custDetails, Constant.STEP_5, "", "");
				}
				// send SMS(sent for verification)
				SMSResponseModel smsResponse = sendMLISMSService.sendMLISMS(null, custDetails.getCustMobileNo(),
						OTPType.APP_SENT, custDetails);
				if (!("OK").equalsIgnoreCase(
						smsResponse.getMliSmsServiceResponse().getResponseHeader().getGeneralResponse().getStatus())) {
					logger.error("::::::::::::::::: SMS not Send to Customer :::::::::::::::Mob="
							+ custDetails.getCustMobileNo() + " at time : " + (new Date()));
				} else {
					logger.info("::::::::::::::::: SMS Send to Customer :::::::::::::::Mob="
							+ custDetails.getCustMobileNo() + " at time : " + (new Date()));
				}
				if (userDetails.getCustOTPVerificationModel().getStatus()
						.equalsIgnoreCase(SaveUpdateResponse.SUCCESS)) {
				}
				custDetails.setStatus(Status.APP_SENT);
				CustomerDetailsModel customerDetailsModel = userDetails.getCustomerDetails();
				if (customerDetailsModel == null) {
					customerDetailsModel = new CustomerDetailsModel();
				}
				custDetails.setSfvTimeStamp(DateUtil.toCurrentUTCTimeStamp());
				customerDetailsModel.setStatus(Status.APP_SENT.getLabel());
				userDetails.setCustomerDetails(customerDetailsModel);
				customerDetailsDAO.saveOrUpdate(custDetails);
				status = "";
				if (custDetails.getStatus().equals(Status.APP_SENT)) {
					status = Constant.SFV;
				}
				userDetails.setStatus(status);
				userDetails.setSfvTimeStamp(DateUtil.toCurrentUTCTimeStamp());
				responseModel.setData(userDetails);
				responseModel.setMessage(MLIMessageConstants.SENT_FOR_VERIFICATION);
				responseModel.setStatus(SaveDetailsResponse.SUCCESS);
				return responseModel;
			case 14:
				// this step for save NRI/OTHER reflexive question answer
				logger.info("userDetails.getNationalityAnswerModel() : "
						+ userDetails.getNationalityDetails().getNationalityAnswerModel());
				if (userDetails.getNationalityDetails() != null) {
					// delete Previous answer
					List<ReflexiveAnswerEntity> entity = answerDao
							.findByCustomerIdAndType(custDetails.getCustomerDtlId(), Constant.NATIONALITY);
					logger.info("Number of answer deleting for nationality : " + entity.size());
					deletePreviousAnswer(entity);

					// save answer for nationality
					logger.info("at step 14 custDetails.getCustomerDtlId() : " + custDetails.getCustomerDtlId());
					if (userDetails.getNationalityDetails() != null) {
						savedAnswer = saveRefQuestionAnswer(
								userDetails.getNationalityDetails().getNationalityAnswerModel(),
								custDetails.getCustomerDtlId());
						customerSavedAnswer = reflexiveQuestionService.convertToModelSavedAnswer(savedAnswer);
						userDetails.getNationalityDetails().setSavedAnswers(customerSavedAnswer);
						SchemeDirEntity schemeDirEntity = schemeDirDao.findByProposalNumberNdeDocType(proposalNumber,
								DocType.PASSPORT);
						if (schemeDirEntity != null) {
							userDetails.getNationalityDetails().setImageExist(true);
						}
					}
					userDetails.setSteps(Status.NRI.getValue());
					updateCustomerDetailsSteps(custDetails, currentStep);
					responseModel.setData(userDetails);
					responseModel.setMessage(MLIMessageConstants.NRI_SAVED);
					responseModel.setStatus(SaveDetailsResponse.SUCCESS);
					return responseModel;
				}
			}
			logger.info(
					"::::::::::::::::::::  user details saved successfully at step : " + steps + "      :::::::::::::");
		} catch (Exception e) {
			logger.info("::::::::::::::::: Error in Saving customer details  ::::::::::::::: at step : " + steps + e);
			e.printStackTrace();
			responseModel.setData(null);
			responseModel.setMessage(Constant.FAILURE_MSG);
			responseModel.setStatus(Constant.FAILURE);
			return responseModel;
		}
		return responseModel;
	}

	/**
	 * method is used to trigger health messagess
	 * @param healthDeclarationEntity
	 * @param answerModels
	 * @return
	 * @author rajkumar
	 */
	@Transactional
	public HealthDeclarationEntity setTriggerMsg(HealthDeclarationEntity healthDeclarationEntity,
			List<AnswerModel> answerModels) {
		Long hyperRemarkQuestionId = 0l;
		StringBuilder hyperRemarkTriggerMsg = new StringBuilder();
		StringBuilder hyperListingTriggerMsg = new StringBuilder();
		MasterReflexiveQuestionEntity masterReflexiveQuestionEntity = masterReflexiveQuestionDao
				.findByQuestion(Constant.HYPR_REMARKTYPE_QUEST_FOR_TRIGGER);
		if (masterReflexiveQuestionEntity != null) {
			hyperRemarkQuestionId = masterReflexiveQuestionEntity.getId();
		}
		StringBuilder diabetesTriggerMsg = new StringBuilder();
		for (AnswerModel answerModel : answerModels) {
			if (answerModel.getQuestionId().equals(hyperRemarkQuestionId)) {
				if (answerModel.getFlag().equalsIgnoreCase("Yes")) {
					hyperRemarkTriggerMsg.append("All investigation report");
					break;
				}
			}
		}
		for (AnswerModel answerModel : answerModels) {
			if (answerModel.getSelectedOptions() != null && !answerModel.getSelectedOptions().isEmpty()) {
				answerModel.getSelectedOptions().parallelStream().forEach(selectedOption -> {
					MasterOptionEntity masterOptionEntity = masterOptionDao.getEntity(MasterOptionEntity.class,
							selectedOption.getOptionId());
					if (masterOptionEntity != null) {
						String optionValue = masterOptionEntity.getValue();
						if (Constant.LESS_THEN_5_YRS.trim().equalsIgnoreCase(optionValue.trim())
								|| Constant.UPTO_10_YRS.trim().equalsIgnoreCase(optionValue.trim())) {
							diabetesTriggerMsg.append("HbA1C");
						} else if (Constant.GRETER_THEN_10_YRS.trim().equalsIgnoreCase(optionValue.trim())) {
							diabetesTriggerMsg.append("HbA1C and ECG");
						} else if (Constant.MORE_THEN_ONE_TYPE_OF_MEDICINE.trim()
								.equalsIgnoreCase(optionValue.trim())) {
							hyperListingTriggerMsg.append("MER");
						}
					}

				});
			}
		}
		StringBuilder hypertenstionFinalTriggerMsg = new StringBuilder();
		if (!hyperRemarkTriggerMsg.toString().isEmpty() && hyperListingTriggerMsg.toString().isEmpty()) {
			hypertenstionFinalTriggerMsg.append(hyperRemarkTriggerMsg);
		} else if (hyperRemarkTriggerMsg.toString().isEmpty() && !hyperListingTriggerMsg.toString().isEmpty()) {
			hypertenstionFinalTriggerMsg.append(hyperListingTriggerMsg);
		} else if (!hyperRemarkTriggerMsg.toString().isEmpty() && !hyperListingTriggerMsg.toString().isEmpty()) {
			hypertenstionFinalTriggerMsg.append(hyperRemarkTriggerMsg);
			hypertenstionFinalTriggerMsg.append(" ");
			hypertenstionFinalTriggerMsg.append(hyperListingTriggerMsg);
		}
		healthDeclarationEntity.setDiabetesTriggerMsg(diabetesTriggerMsg.toString());
		healthDeclarationEntity.setHypertensionTriggerMsg(hypertenstionFinalTriggerMsg.toString());

		logger.info("Trigger Msgs : " + healthDeclarationEntity.getDiabetesTriggerMsg() + " "
				+ healthDeclarationEntity.getHypertensionTriggerMsg());
		return healthDeclarationEntity;
	}

	/**
	 * method is used to delete previous reflexive answer
	 * @param entity
	 * @author rajkumar
	 */
	@Transactional
	private void deletePreviousAnswer(List<ReflexiveAnswerEntity> entity) {
		try {
			/*
			 * session.delete() is not working, so I using HQL for deleting
			 */
			if (entity != null && !entity.isEmpty()) {
				for (ReflexiveAnswerEntity temp : entity) {
					List<SelectOptionEntity> selectOptionEntities = temp.getSelectOptionEntities();
					for (SelectOptionEntity selectOp : selectOptionEntities) {
						Query q = getSession().createQuery("delete from SelectOptionEntity where id = :ID");
						q.setParameter("ID", selectOp.getId());
						q.executeUpdate();
					}
					Query q = getSession().createQuery("delete from ReflexiveAnswerEntity where id = :ID");
					q.setParameter("ID", temp.getId());
					q.executeUpdate();
					logger.info("Deleted occupation answer Id : " + temp.getId());
				}
				logger.info("::::::::::::::Answer Deleted successfully::::::::::");
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	/**
	 * To save reflexive Answer
	 * 
	 * @author Devendra.Kumar
	 * @param answerModel
	 * @param customerId
	 * @param nationality
	 */
	private List<ReflexiveAnswerEntity> saveRefQuestionAnswer(List<AnswerModel> answerModels, Long customerId) {
		Long questionId = 0l;
		try {
			CustomerDetailsEntity customerDetailsEntity = customerDetailsDAO.getEntity(CustomerDetailsEntity.class,
					customerId);
			List<ReflexiveAnswerEntity> answerEntities = customerDetailsEntity.getAnswerEntity();
			if (answerEntities == null) {
				answerEntities = new ArrayList<>();
			}
			for (AnswerModel answerModel : answerModels) {
				questionId = answerModel.getQuestionId();
				ReflexiveAnswerEntity answerEntity = new ReflexiveAnswerEntity();
				answerEntity.setCreatedOn(DateUtil.toCurrentUTCTimeStamp());
				answerEntity.setDescription(answerModel.getDescription());
				answerEntity.setFlag(answerModel.getFlag());
				MasterReflexiveQuestionEntity questionEntity = masterReflexiveQuestionDao
						.getEntity(MasterReflexiveQuestionEntity.class, answerModel.getQuestionId());
				answerEntity.setCustIdQuestType(
						customerId + "_" + questionEntity.getSubTypeEntity().getTypeEntity().getTypeValue());
				answerEntity.setCustIdQuestId(customerId + "_" + questionId);
				if (answerModel.getSelectedOptions() != null && !answerModel.getSelectedOptions().isEmpty()) {
					List<SelectOptionEntity> selectOptionEntities = new ArrayList<>();
					for (SelectedOptionModel optionModel : answerModel.getSelectedOptions()) {
						SelectOptionEntity selectOption = new SelectOptionEntity();
						MasterOptionEntity masterOption = masterOptionDao.getEntity(MasterOptionEntity.class,
								optionModel.getOptionId());
						selectOption.setAnswerEntity(answerEntity);
						selectOption.setCreatedOn(DateUtil.toCurrentUTCTimeStamp());
						selectOption.setCustIdMasterOpId(customerId + "_" + masterOption.getId());
						selectOptionEntities.add(selectOption);
					}
					answerEntity.setSelectOptionEntities(selectOptionEntities);
				}
				answerEntities.add(answerEntity);
				answerDao.saveOrUpdate(answerEntity);
				logger.info(":::::::::::::::Answer saved successfully:::::::::::::::::");
			}
			customerDetailsEntity.setAnswerEntity(answerEntities);
			customerDetailsDAO.saveOrUpdate(customerDetailsEntity);
			return answerEntities;
		} catch (Exception exception) {
			logger.error("Exception occured while saving answer of QuestionId " + questionId);
			exception.printStackTrace();
		}
		return null;
	}

	/**
	 * Age validation , age should be greater than 18 years for customer & appointee
	 */
	@Override
	public boolean checkAgeGreaterThan18(String age) {
		Integer ageTillDate;
		try {
			ageTillDate = DateUtil.ageValidation18Year(age);
			if (ageTillDate >= 18) {
				return true;
			} else {
				return false;
			}
		} catch (ParseException e) {
			e.printStackTrace();
			throw new AgeException(MLIMessageConstants.AGE_FORMATE);
		}

	}

	/**
	 * Loan App number validation
	 */

	@Transactional
	private boolean checkLoanAppNumberValidation(String loanAppNumber, String proposalNumber) {
		List<CustomerDetailsEntity> custDtls = customerDetailsDAO.findByLoanAppNumber(loanAppNumber, null);
		logger.info(":::::Loan App Number::::::" + loanAppNumber + " Proposal Number : " + proposalNumber);

		boolean proposerNoflag = true;
		for (CustomerDetailsEntity custDtl : custDtls) {
			if (!ObjectsUtil.isNull(custDtl) && custDtl.getProposalNumber() != null
					&& custDtl.getProposalNumber().equalsIgnoreCase(proposalNumber)) {
				proposerNoflag = false;
				break;
			} else if (!ObjectsUtil.isNull(custDtl) && custDtl.getProposalNumber() != null
					&& !custDtl.getProposalNumber().equalsIgnoreCase(proposalNumber)) {
				proposerNoflag = true;
			}
		}
		return proposerNoflag;
	}

	/**
	 * update CustomerDetailsEntity status
	 * @param custDetails
	 * @param currentStep
	 * @author rajkumar
	 */
	private void updateCustomerDetailsSteps(CustomerDetailsEntity custDetails, Integer currentStep) {
		custDetails.setAppStep(Status.getStatus(currentStep));
		custDetails.setModifiedOn(DateUtil.toCurrentUTCTimeStamp());
		if(Status.AFL_PHYSICAL_FORM_VERIFICATION !=custDetails.getStatus())
		  custDetails.setStatus(Status.APP_PENDING);
		if (Status.STEP5.getValue().equals(currentStep)) {
			custDetails.setCustOtpVerifiedDate(
					DateUtil.dateFormater(DateUtil.extractDateAsString(DateUtil.toCurrentUTCTimeStamp())));
		}
		customerDetailsDAO.saveOrUpdate(custDetails);
	}

	/**
	 * Generating unique proposal number
	 */
	@Override
	public String genearteProposalNumber() throws Exception {
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
				// some problem here
			}
			if ((null != time_in_db && nowTime.equals(time_in_db)) || (null != proposalDtl.getDateInDB())) {
				String formatedString = NumberParser.getFormatedNumber(proposalDtl.getUseNumber(), "xxxxx", 'x', '0');
				String prefix = "P";
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
	 * Find out All proposal by date
	 */

	@Transactional
	@Override
	public List<UserDetailsModel> getProposalsByDate(Long Date) throws Exception {
		try {
			List<UserDetailsModel> userDetailsModels = new ArrayList<UserDetailsModel>();
			List<CustomerDetailsEntity> customerDetailsEntities = customerDetailsDAO.findByDate(Date);
			for (CustomerDetailsEntity customerDetailsEntity : customerDetailsEntities) {
				UserDetailsModel userDetailsModel = getUserModel(customerDetailsEntity);
				userDetailsModels.add(userDetailsModel);
			}
			return userDetailsModels;
		} catch (Exception e) {
			logger.info("::::::::::::::::: Error in getting proposal number By date :::::::::::::::" + e);
			throw new Exception(e.getMessage());
		}

	}

	/**
	 * Find out proposal's details by bank name & date
	 */

	@Transactional
	@Override
	public List<UserDetailsModel> getSubmitedProposalsByBankNdDate(Long date, String masterPolicyHolderName,
			Status appstatus) throws Exception {
		logger.info("::::::::::::::::: getSubmitedProposalsByBankNdDate :::::::::::::::date=" + date
				+ ":::::masterPolicyHolderName=" + masterPolicyHolderName + ":::::::appstatus=" + appstatus);
		try {
			List<UserDetailsModel> userDetailsModels = new ArrayList<UserDetailsModel>();
			List<CustomerDetailsEntity> customerDetailsEntities = customerDetailsDAO.findByDateBankNameNdAppStatus(date,
					masterPolicyHolderName, appstatus);
			for (CustomerDetailsEntity customerDetailsEntity : customerDetailsEntities) {
				UserDetailsModel userDetailsModel = getUserModel(customerDetailsEntity);
				userDetailsModels.add(userDetailsModel);
			}
			return userDetailsModels;
		} catch (Exception e) {
			logger.error("::::::::::::::::: Error in get Submited ProposalsByBankNdDate() :::::::::::::::" + e);
			throw new Exception(e.getMessage());
		}
	}

	/**
	 * 
	 * Find out all pending proposal by seller's contact number
	 */
	@Transactional
	@Override
	public List<UserDetailsModel> getPendingProposalsBySellerContNo(String contNumber) throws Exception {
		try {
			List<UserDetailsModel> userDetailsModels = new ArrayList<UserDetailsModel>();
			SellerDetailEntity sellerDetailEntity = sellerDAO.findByContactNo(Long.parseLong(contNumber));
			List<CustomerDetailsEntity> customerDetailsEntities = customerDetailsDAO
					.findPendingProposalsBySellerId(sellerDetailEntity.getSellerDtlId(), Status.APP_PENDING);
			for (CustomerDetailsEntity customerDetailsEntity : customerDetailsEntities) {
				UserDetailsModel userDetailsModel = getUserModel(customerDetailsEntity);
				userDetailsModels.add(userDetailsModel);
			}
			return userDetailsModels;
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("::::::::::::::::: Error in getting getPendingProposalsBySellerContNo() :::::::::::::::" + e);
			throw new Exception(e.getMessage());
		}
	}

	/**
	 * Find out customer's details by proposal number
	 */

	@Override
	@Transactional
	public UserDetailsModel getByProposalNumber(String proposalNumber) {

		CustomerDetailsEntity customerDetailsEntity = customerDetailsDAO.findByProposalNumber(proposalNumber);
		UserDetailsModel userDetailsModel = getUserModel(customerDetailsEntity);
		return userDetailsModel;
	}

	/**
	 * Find out customer's details by loan app number
	 */

	@Override
	@Transactional
	public UserDetailsModel getUSerDetailModelByCustomerDetail(CustomerDetailsEntity customerDetailsEntity) {
		UserDetailsModel userDetailsModel = getUserModel(customerDetailsEntity);
		return userDetailsModel;
	}

	/**
	 * {@code param} is loanAppNumber or firstName or lastName
	 * 
	 */
	@Override
	@Transactional
	public List<Map<String, Object>> getByLoanAppNo(String param) {
		List<CustomerDetailsEntity> customerDetailsEntity = customerDetailsDAO.findByLoanAppNumber(param,
				Constant.VERIFIED);
		List<Map<String, Object>> result = new ArrayList<>(customerDetailsEntity.size());
		for (CustomerDetailsEntity entity : customerDetailsEntity) {
			Map<String, Object> tempMap = new HashMap<>();
			tempMap.put("customerFirstName", entity.getCustomerFirstName());
			tempMap.put("customerLastName", entity.getCustomerLastName());
			tempMap.put("dob", DateUtil.dateInFormatddmmyyyy(entity.getDob()));
			tempMap.put("masterPolicyholderName", entity.getMasterPolicyHolderName());
			tempMap.put("loanAppNumber", entity.getLoanAppNumber());
			tempMap.put("proposalNumber", entity.getProposalNumber());
			if (entity.getNationality() != null && (entity.getNationality().equalsIgnoreCase("NRI")
					|| entity.getNationality().equalsIgnoreCase("Other"))) {
				tempMap.put("passportExists", true);
			} else {
				tempMap.put("passportExists", false);
			}

			
			List<CamReportDetailsEntity> camReportList = camReportDetailsDao.getProposals(entity.getProposalNumber());
			if (camReportList != null && camReportList.size() > 0) {
				tempMap.put("camExists", true);
			} else {
				tempMap.put("camExists", false);
			}
			
			List<CommonFileUploadEntity> commonFileUploadList = commonFileUploadDao.getProposals(entity.getProposalNumber());
			
			if(!commonFileUploadList.isEmpty() && commonFileUploadList.size() > 0) {
				tempMap.put("physicalExists", true);
			} else {
				tempMap.put("physicalExists", false);
			}
			
			List<CovidReportEntity> covidReportList = covidReportDAO.findAllReport(entity.getProposalNumber());	
			if(!covidReportList.isEmpty() && covidReportList.size() > 0) {
				tempMap.put("covidReportExists", true);
			} else {
				tempMap.put("covidReportExists", false);
			}
			
			result.add(tempMap);
			/*
			 * CustomerDetailsModel customerDetailsModel =
			 * customerDetailsHelper.convertToModel(entity);
			 * customerDetailsModels.add(customerDetailsModel);
			 */
		}
		return result;
	}
	
	/**
	 * return list of CreditCardCustomerEntity map based on param
	 * @param param
	 * @return
	 * @author rajkumar
	 */
	@Override
	public List<Map<String, Object>> searchYBLCCCustomer(String param){
		List<CreditCardCustomerEntity> creditCardCustomerEntity = creditCardCustomerDAO.findByCustomerIdAndName(param,Constant.VERIFIED);
		List<Map<String, Object>> result = new ArrayList<>(creditCardCustomerEntity.size());
		for (CreditCardCustomerEntity entity : creditCardCustomerEntity) {
			Map<String, Object> tempMap = new HashMap<>();
			tempMap.put("customerFirstName", entity.getFirstName());
			tempMap.put("customerLastName", entity.getLastName());
			tempMap.put("dob", entity.getDob());
			tempMap.put("masterPolicyholderName", entity.getMasterPolicyHolderName());
			tempMap.put("customerId", entity.getCustomerId());
			tempMap.put("proposalNumber", entity.getProposalNumber());
			tempMap.put("passportExists", false);
			tempMap.put("camExists", false);
			tempMap.put("physicalExists", false);
			result.add(tempMap);
		}
		return result;
	
	}

	/**
	 * convert CustomerDetailsEntity into UserDetailsModel
	 * @param customerDetailsEntity
	 * @return
	 * @author rajkumar
	 */
	private UserDetailsModel getUserModel(CustomerDetailsEntity customerDetailsEntity) {
		try {
			UserDetailsModel userDetailsModel = new UserDetailsModel();
			AppointeeModel appointeeDetails = null;
			userDetailsModel.setCustomerDetails(customerDetailsHelper.convertToModel(customerDetailsEntity));
			String proposalNumber = userDetailsModel.getCustomerDetails().getProposalNumber();
			userDetailsModel.setSfvTimeStamp(customerDetailsEntity.getSfvTimeStamp());
			/*
			 * String status = ""; if
			 * (customerDetailsEntity.getStatus().equals(Status.APP_PENDING)) { status =
			 * Constant.WIP; } else if
			 * (customerDetailsEntity.getStatus().equals(Status.APP_SENT)) { status =
			 * Constant.SFV; } else if
			 * (customerDetailsEntity.getStatus().equals(Status.APP_VERIFIED)) { status =
			 * Constant.V; } else if
			 * (customerDetailsEntity.getStatus().equals(Status.APP_COMPLETE)) { status =
			 * "Completed"; }else
			 * if(customerDetailsEntity.getStatus().equals(Status.APP_NOT_INTERESTED)) {
			 * status = Constant.NI; }
			 */
			userDetailsModel.setStatus(customerDetailsHelper.getPrintedStatus(customerDetailsEntity.getStatus()));
			NomineeDetailsEntity nomineeDetailsEntity = nomineeDetailsDAO
					.findByCustomerDtlId(customerDetailsEntity.getCustomerDtlId());
			NomineeDetailsModel nomineeDetails = nomineeDetailsHelper.convertToModel(nomineeDetailsEntity);
			if (nomineeDetails != null && nomineeDetailsEntity != null) {
				nomineeDetails.setIsAppointee(nomineeDetailsEntity.isAppointee());
			}
			if (nomineeDetailsEntity != null)
				appointeeDetails = appointeeDetailsHelper
						.convertToModel(appointeeDAO.findByNomineeDtlId(nomineeDetailsEntity.getNomineeDtlId()));
			if (appointeeDetails != null)
				nomineeDetails.setAppointeeDetails(appointeeDetails);
			userDetailsModel.setNomineeDetails(nomineeDetails);
			// mandatory details
			MandatoryDeclarationModel mandatoryDeclaration = mandatoryDeclarationHelper.convertToModel(
					mandatoryDeclarationDAO.findbyCustomerDtlId(customerDetailsEntity.getCustomerDtlId()));
			userDetailsModel.setMandatoryDeclaration(mandatoryDeclaration);
			
			if(Constant.HEALTH_TYPE_SFQ.equals(customerDetailsEntity.getHealthType())) {
				// SFQ health declaration
				SFQHealthDeclarationEntity  sfqHealthDeclarationEntity = sfqHealthDeclarationDAO.findByCustomerDtlId(customerDetailsEntity.getCustomerDtlId());
				if (!ObjectUtils.isEmpty(sfqHealthDeclarationEntity)) {
					 SFQHealthDeclarationModel sfqHealthDeclarationModel = sfqHealthDeclarationHelper.convertToModel(sfqHealthDeclarationEntity);
					    SFQCovidEntity sfqCovidEntity = sfqCovidDAO.findBySFQHealthDtlId(sfqHealthDeclarationEntity.getHealthDclId());
					    if(!ObjectUtils.isEmpty(sfqCovidEntity)) {
					    	SFQCovidModel sfqCovidModel = sfqHealthDeclarationHelper.convertToModel(sfqCovidEntity);
					    	if(sfqCovidModel != null)
					    		sfqHealthDeclarationModel.setSfqCovidModel(sfqCovidModel);
					    }
					    userDetailsModel.setSfqHealthDeclaration(sfqHealthDeclarationModel);
				}
			}else {
				// DOGH health declaration
			    HealthDeclarationEntity  healthDeclarationEntity= healthDeclarationDAO.findByCustomerDtlId(customerDetailsEntity.getCustomerDtlId());
				HealthDeclarationModel healthDeclaration = healthDeclarationHelper
						.convertToModel(healthDeclarationEntity);
				if (healthDeclaration != null) {
					healthDeclaration.setSavedAnswers(reflexiveQuestionService.getAllAnswer(Constant.DISEASE,
							customerDetailsEntity.getCustomerDtlId(), null));
					Covid19Entity covid19Entity = covid19Dao.findByHealthDtlId(healthDeclarationEntity.getHealthDclId());
					Covid_19Model covid_19Model = healthDeclarationHelper.getCovid_19Model(covid19Entity);
					if(!ObjectUtils.isEmpty(covid_19Model)) {
						healthDeclaration.setCovid_19Details(covid_19Model);
					}
				}
				userDetailsModel.setHealthDeclaration(healthDeclaration);
			}
			if (customerDetailsEntity.getSlrDtlId() != null) {
				SellerDetailEntity sellerDetailEntity = sellerDAO.getEntity(SellerDetailEntity.class,
						customerDetailsEntity.getSlrDtlId().getSellerDtlId());
				if (sellerDetailEntity != null) {
					userDetailsModel.setHomeLoanGroupPolicyNo(sellerDetailEntity.getGroupPolicyNumber());
					userDetailsModel.setSellerContNumber(sellerDetailEntity.getContactNo().toString());
				}
			}
			userDetailsModel.setProposalNumber(customerDetailsEntity.getProposalNumber());
			if (customerDetailsEntity.getAppStep() != null) {
				userDetailsModel.setSteps(customerDetailsEntity.getAppStep().getValue());
			}

			NationalityDetail nationalityDetail = new NationalityDetail();
			nationalityDetail.setSavedAnswers(reflexiveQuestionService.getAllAnswer(Constant.NATIONALITY,
					customerDetailsEntity.getCustomerDtlId(), null));
			SchemeDirEntity schemeDirEntity = schemeDirDao.findByProposalNumberNdeDocType(proposalNumber,
					DocType.PASSPORT);
			if (schemeDirEntity != null) {
				nationalityDetail.setImageExist(true);
			}
			userDetailsModel.setNationalityDetails(nationalityDetail);
			return userDetailsModel;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * Save user's CDF file in PDF format
	 */

	@Override
	@Transactional
	public ResponseModel<UserDetailsModel> saveUploadFile(MultipartFile file, String proposalNumber) throws Exception {
		try {
			ResponseModel<UserDetailsModel> responseModel = new ResponseModel<UserDetailsModel>();
			CustomerDetailsEntity customerDetails = new CustomerDetailsEntity();

			customerDetails = customerDetailsDAO.findByProposalNumber(proposalNumber);
			if (ObjectsUtil.isNull(customerDetails)) {
				responseModel.setMessage(MLIMessageConstants.WRONG_PROPOSAL_NUMBER);
				responseModel.setStatus(SaveDetailsResponse.FAILURE);
				return responseModel;
			}
			customerDetails.setAppCompletionDate(
					DateUtil.dateFormater(DateUtil.extractDateAsString(DateUtil.toCurrentUTCTimeStamp())));

			String getFileExtension = getFileExtension(file);
			if (!ObjectsUtil.isNull(getFileExtension) && fileExtension.contains(getFileExtension)) {
				Document document = new Document();
				byte[] bytes = file.getBytes();

				String serverPath = awsFileUtility.generateFilePath(
						FileUtilityConverter.getFileUtilityModel(FileExtention.PDF, DocType.CDF, customerDetails));
				if (!("pdf".equalsIgnoreCase(getFileExtension))) {

					Path imageTempPath = Paths.get(tempFilePath + file.getOriginalFilename());
					Files.write(imageTempPath, bytes);
					PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(serverPath));
					writer.open();
					document.open();
					Image image = Image.getInstance(imageTempPath.toString());
					// image.scaleAbsolute(500f, 650f);
					image.scaleToFit(PageSize.A4.getWidth(), 300);
					document.add(image);
					document.close();
					writer.close();
					Files.delete(imageTempPath);
				} else {
					Path filePath = Paths.get(serverPath);
					Files.write(filePath, bytes);
				}

				responseModel.setMessage(MLIMessageConstants.CDF_UPLOAD_SUCC);
				responseModel.setStatus(SaveDetailsResponse.SUCCESS);
				try {
					logger.info(":::::START:::::::saveFinalSubmit:::::CDF uploading to aws s3::::::::");
					FileUtilityModel fileUtilityModel = new FileUtilityModel();
					fileUtilityModel.setLoanAppNo(customerDetails.getLoanAppNumber());
					fileUtilityModel.setDocType(DocType.CDF);
					fileUtilityModel.setProposalNumber(customerDetails.getProposalNumber());
					fileUtilityModel = awsFileUtility.getPDFFileNdDirectoryDetails(fileUtilityModel);
					awsFileUtility.createFileOnAWSS3(serverPath, fileUtilityModel.getAwsFilePath());
					awsFileUtility.updateAwsPDFNdCDFFilePathInDB(fileUtilityModel);
					logger.info(
							":::::END:::::::saveFinalSubmit:::::CDF uploading to aws s3::::::::serverPath=" + serverPath
									+ "::::fileUtilityModel.getAwsFilePath()=" + fileUtilityModel.getAwsFilePath());
				} catch (Exception e) {
					logger.error(":::::ERROR:::::::saveFinalSubmit:::::CDF uploading to aws s3::::::::" + e);
				}
				return responseModel;
			} else {
				responseModel.setMessage(MLIMessageConstants.UPLOAD_FILE_FORMAT + fileExtensionString);
				responseModel.setStatus(SaveDetailsResponse.FAILURE);
				return responseModel;
			}

		} catch (Exception e) {
			e.printStackTrace();
			throw new StorageException(e.getMessage());
		}
	}

	/**
	 * find file extension
	 * @param file
	 * @return
	 * @author rajkumar
	 */
	private static String getFileExtension(MultipartFile file) {
		String fileName = file.getOriginalFilename();
		if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
			return fileName.substring(fileName.lastIndexOf(".") + 1);
		else
			return null;
	}

	/**
	 * Final submit application , sending message & email to customer and a message
	 * to seller
	 */

	@Override
	@Transactional
	public ResponseModel<UserDetailsModel> saveFinalSubmit(String proposalNumber) throws Exception {
		try {
			ResponseModel<UserDetailsModel> responseModel = new ResponseModel<UserDetailsModel>();
			CustomerDetailsEntity customerDetails = null;

			SMSResponseModel smsResponse = new SMSResponseModel();

			customerDetails = customerDetailsDAO.findByProposalNumber(proposalNumber);
			if (ObjectsUtil.isNull(customerDetails)) {
				responseModel.setMessage(MLIMessageConstants.WRONG_PROPOSAL_NUMBER);
				responseModel.setStatus(SaveDetailsResponse.FAILURE);
				return responseModel;
			}
			customerDetails.setAppCompletionDate(
					DateUtil.dateFormater(DateUtil.extractDateAsString(DateUtil.toCurrentUTCTimeStamp())));

//			this.generateAndEmailPDF(customerDetails);

			smsResponse = sendMLISMSService.sendMLISMS(null, customerDetails.getSlrDtlId().getContactNo(),
					OTPType.SELLER_APP_SUBMIT, customerDetails);
			if (!("OK").equalsIgnoreCase(
					smsResponse.getMliSmsServiceResponse().getResponseHeader().getGeneralResponse().getStatus())) {
				logger.error("::::::::::::::::: SMS not Send to SELLER :::::::::::::::Mob="
						+ customerDetails.getSlrDtlId().getContactNo() + " at time : " + (new Date()));
			} else {
				logger.info("::::::::::::::::: SMS Send to SELLER :::::::::::::::Mob="
						+ customerDetails.getSlrDtlId().getContactNo() + " at time : " + (new Date()));
			}

			smsResponse = sendMLISMSService.sendMLISMS(null, customerDetails.getCustMobileNo(), OTPType.CUST_APP_SUBMIT,
					customerDetails);
			if (!("OK").equalsIgnoreCase(
					smsResponse.getMliSmsServiceResponse().getResponseHeader().getGeneralResponse().getStatus())) {
				logger.error("::::::::::::::::: SMS not Send to Customer :::::::::::::::Mob="
						+ customerDetails.getCustMobileNo() + " at time : " + (new Date()));
			} else {
				logger.info("::::::::::::::::: SMS Send to Customer :::::::::::::::Mob="
						+ customerDetails.getCustMobileNo() + " at time : " + (new Date()));
			}

			customerDetails.setStatus(Status.APP_COMPLETE);
			responseModel.setMessage(String.format(MLIMessageConstants.FINAL_SUBMIT_CUST_MSG_ALL_SUCCESS,
					customerDetails.getProposalNumber()));
			customerDetailsDAO.saveOrUpdate(customerDetails);
			responseModel.setStatus(SaveDetailsResponse.SUCCESS);
			return responseModel;

		} catch (IOException e) {
			System.out.println("e.getMessage()  ::::::::::::::::::   " + e.getMessage());
			throw new StorageException(e.getMessage());
		}
	}

	/**
	 * generate email and send to customer with attached application pdf file
	 * @param customerDetails
	 * @param step
	 * @param otp
	 * @param modifyTS
	 * @author rajkumar
	 */
	public void generateAndEmailPDF(CustomerDetailsEntity customerDetails, String step, String otp, String modifyTS) {
		try {
			ResponseModel<MliEmailServiceModel> emailResponse = new ResponseModel<MliEmailServiceModel>();
			FileUtilityModel fileDetails = new FileUtilityModel();
			String pdfFilepath = null;
			// START: Generating PDF file
			try {
				if (!Constant.STEP_5.equalsIgnoreCase(step)) {
					UserDetailsModel userDetailsModel = customerDetailService
							.getUSerDetailModelByCustomerDetail(customerDetails);
					pdfFilepath = awsFileUtility.generateFilePath(FileUtilityConverter
							.getFileUtilityModel(FileExtention.PDF, DocType.PROPOSAL, customerDetails));
					if(Constant.HEALTH_TYPE_SFQ.equalsIgnoreCase(customerDetails.getHealthType())) {
						Map<String, Object> data = pdfGenaratorUtil.getSFQPdfModel(userDetailsModel, otp, modifyTS);
						logger.info("SFQ Pdf file path : " + pdfFilepath);
						pdfGenaratorUtil.createPdf("sfqProposalPdf", data, pdfFilepath);
					}else {
					   Map<String, Object> data = pdfGenaratorUtil.getPdfModel(userDetailsModel, otp, modifyTS);
					   logger.info("Pdf file path : " + pdfFilepath);
					   pdfGenaratorUtil.createPdf("proposalPdf", data, pdfFilepath);
					}
					logger.info(
							"::::::START::::::saveFinalSubmit:::Uploadning DPF to AWS S3:::::::::customerDetails.getLoanAppNumber()="
									+ customerDetails.getLoanAppNumber());
					FileUtilityModel fileUtilityModel = new FileUtilityModel();
					fileUtilityModel.setLoanAppNo(customerDetails.getLoanAppNumber());
					fileUtilityModel.setDocType(DocType.PROPOSAL);
					fileUtilityModel.setProposalNumber(customerDetails.getProposalNumber());
					fileUtilityModel.setCreatedOn(customerDetails.getCreatedOn());
					fileUtilityModel.setCustFirstName(customerDetails.getCustomerFirstName());
					fileUtilityModel = awsFileUtility.getPDFFileNdDirectoryDetails(fileUtilityModel);

					awsFileUtility.createFileOnAWSS3(pdfFilepath, fileUtilityModel.getAwsFilePath());
					fileUtilityModel.setDocType(DocType.PROPOSAL);
					fileUtilityModel.setProposalNumber(customerDetails.getProposalNumber());
					awsFileUtility.updateAwsPDFNdCDFFilePathInDB(fileUtilityModel);
					logger.info(":::::END:::::::saveFinalSubmit:::::::::Uploadning DPF to AWS S3:::::local pdfFilepath="
							+ pdfFilepath + "::::::fileUtilityModel.getAwsFilePath()="
							+ fileUtilityModel.getAwsFilePath());
				}
			} catch (Exception e) {
				logger.error(
						"::::::::::::saveFinalSubmit:::::Error in generating PDF or uploading to aws s3::::::::" + e);
				e.printStackTrace();
			}

			try {
				fileDetails = awsFileUtility.getPDFFileNdDirectoryDetails(
						FileUtilityConverter.getFileUtilityModel(FileExtention.PDF, DocType.PROPOSAL, customerDetails));
			} catch (Exception e) {
				logger.error(":::::::::::::::::Error in getting PDF file details ::::::::" + e);
			}

			if (step != null) {
				EmailModel mliEmailModel = new EmailModel();
				mliEmailModel.setMailUserType(OTPUserType.CUSTOMER.getLabel());
				mliEmailModel.setLoanAppNumber(customerDetails.getLoanAppNumber());
				mliEmailModel.setProposalNumber(customerDetails.getProposalNumber());
				mliEmailModel.setCustMobNumber(customerDetails.getCustMobileNo());
				StringBuilder fullName = new StringBuilder();
				if (customerDetails.getCustomerFirstName() != null) {
					fullName.append(customerDetails.getCustomerFirstName());
				}
				if (customerDetails.getCustomerLastName() != null) {
					fullName.append(" ");
					fullName.append(customerDetails.getCustomerLastName());
				}
				mliEmailModel.setCustomerName(fullName.toString());
				mliEmailModel.setMailIdTo(customerDetails.getCustEmailId());
				mliEmailModel.setStep(Constant.STEP_5);

				if (!Constant.STEP_5.equalsIgnoreCase(step)) {
					
					  mliEmailModel.setName(!ObjectsUtil.isNull(fileDetails.getFileName()) ?
					  fileDetails.getFileName() : customerDetails.getLoanAppNumber());
					 
					/*
					 * if(fileDetails!=null && fileDetails.getFileName()!=null) {
					 * mliEmailModel.setName(fileDetails.getFileName()); }else {
					 * mliEmailModel.setName(customerDetails.getLoanAppNumber()); }
					 */
					mliEmailModel.setType(FileExtention.PDF);
					mliEmailModel.setStep(Constant.FINAL_STEP);
					mliEmailModel.setMailUserType(OTPUserType.VERIFIED.getLabel());
					File readPdfFilePath = new File(pdfFilepath);

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
				}
				try {
					emailResponse = mliEmailService.sendEmail(mliEmailModel);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (!("200".equalsIgnoreCase(emailResponse.getCode())
						&& ("Ok").equalsIgnoreCase(emailResponse.getStatus()))) {
					logger.error("::::::::::::::::: Mail Not Send to customer :::::::::::::::"
							+ customerDetails.getProposalNumber() + " at time : " + (new Date()));

				} else {
					logger.info("::::::::::::::::: Mail  Send to customer :::::::::::::::"
							+ customerDetails.getProposalNumber() + " at time : " + (new Date()));
				}
			}
		} catch (Exception exception) {
			logger.error("::::::::::: Something went wrong :generateAndEmailPDF :::::::::::::::");
			exception.printStackTrace();
		}

	}

	/**
	 * Upload passport
	 */
	@Override
	@Transactional
	public Map<String, Object> uploadPasport(String proposalNumber, MultipartFile front, MultipartFile back) {
		Map<String, Object> responseModel = new HashMap<>();
		try {
			String frontSidePath = null;
			String backSidePath = null;
			responseModel = uploadPassport(proposalNumber, front, Constant.FRONT_SIDE);
			frontSidePath = (String) responseModel.get(Constant.PASSPORT_PATH);
			logger.info("frontSidePath : " + frontSidePath);
			if (front != null && back != null) {
				responseModel = uploadPassport(proposalNumber, back, Constant.BACK_SIDE);
				backSidePath = (String) responseModel.get(Constant.PASSPORT_PATH);

				List<InputStream> list = new ArrayList<>(2);
				list.add(new FileInputStream(new File(frontSidePath)));
				list.add(new FileInputStream(new File(backSidePath)));

				frontSidePath = frontSidePath.replaceAll("-F-", "-FB-");
				FileOutputStream outputStream = new FileOutputStream(new File(frontSidePath));
				boolean isMerge = pdfGenaratorUtil.doMerge(list, outputStream);
				if (!isMerge) {
					responseModel.remove("customerData");
					responseModel.remove(Constant.PASSPORT_PATH);
					responseModel.put(Constant.STATUS, SaveDetailsResponse.FAILURE);
					responseModel.put(Constant.MESSAGE, "Invalid PDF Content");
					return responseModel;
				}
			}
			CustomerDetailsEntity customerDetails = (CustomerDetailsEntity) responseModel.get("customerData");
			try {
				logger.info(":::::START:::::::saveFinalSubmit:::::Passport uploading to aws s3::::::::");
				FileUtilityModel fileUtilityModel = new FileUtilityModel();
				fileUtilityModel.setLoanAppNo(customerDetails.getLoanAppNumber());
				fileUtilityModel.setCustFirstName(customerDetails.getCustomerFirstName());
				fileUtilityModel.setDocType(DocType.PASSPORT);
				fileUtilityModel.setCreatedOn(customerDetails.getCreatedOn());
				fileUtilityModel.setProposalNumber(customerDetails.getProposalNumber());
				String awsFilePath = frontSidePath.replaceAll("/tmp", "GCL");
				fileUtilityModel.setAwsFilePath(awsFilePath);
				awsFileUtility.createFileOnAWSS3(frontSidePath, awsFilePath);
				awsFileUtility.updateAwsPassportFilePathInDB(fileUtilityModel);
				logger.info(":::::END:::::::saveFinalSubmit:::::Passport uploading to aws s3::::::::serverPath="
						+ frontSidePath + "::::fileUtilityModel.getAwsFilePath()=" + awsFilePath);
			} catch (Exception e) {
				responseModel.remove("customerData");
				responseModel.remove(Constant.PASSPORT_PATH);
				logger.error(":::::ERROR:::::::saveFinalSubmit:::::Passport uploading to aws s3::::::::" + e);
				e.printStackTrace();
				responseModel.put(Constant.STATUS, SaveDetailsResponse.FAILURE);
				responseModel.put(Constant.MESSAGE, Constant.FAILURE);
				return responseModel;
			}
			responseModel.remove("customerData");
			responseModel.remove(Constant.PASSPORT_PATH);
			return responseModel;
		} catch (Exception e) {
			e.printStackTrace();
			responseModel.remove("customerData");
			responseModel.remove(Constant.PASSPORT_PATH);
			responseModel.put(Constant.MESSAGE, Constant.FAILURE);
			responseModel.put(Constant.STATUS, SaveDetailsResponse.FAILURE);
			return responseModel;
		}
	}

	/**
	 * service method to upload passport
	 * @param proposalNumber
	 * @param file
	 * @param passportSide
	 * @return
	 * @author rajkumar
	 */
	private Map<String, Object> uploadPassport(String proposalNumber, MultipartFile file, String passportSide) {
		Map<String, Object> responseModel = new HashMap<>();
		try {
			CustomerDetailsEntity customerDetails = new CustomerDetailsEntity();
			customerDetails = customerDetailsDAO.findByProposalNumber(proposalNumber);
			if (ObjectsUtil.isNull(customerDetails)) {
				responseModel.put(Constant.MESSAGE, MLIMessageConstants.WRONG_PROPOSAL_NUMBER);
				responseModel.put(Constant.STATUS, SaveDetailsResponse.FAILURE);
				return responseModel;
			}
			customerDetails.setPassportUploadDate(
					DateUtil.dateFormater(DateUtil.extractDateAsString(DateUtil.toCurrentUTCTimeStamp())));

			String getFileExtension = getFileExtension(file);
			if (!ObjectsUtil.isNull(getFileExtension) && fileExtension.contains(getFileExtension)) {
				Document document = new Document();
				byte[] bytes = file.getBytes();
				String serverPath = awsFileUtility.generateFilePathForUploadPassport(
						FileUtilityConverter.getFileUtilityModel(FileExtention.PDF, DocType.PASSPORT, customerDetails),
						passportSide);
				logger.error("serverPath :::::::::: " + serverPath);
				responseModel.put(Constant.PASSPORT_PATH, serverPath);
				if (!("pdf".equalsIgnoreCase(getFileExtension))) {
					Path imageTempPath = Paths.get(tempFilePath + file.getOriginalFilename());
					Files.write(imageTempPath, bytes);
					PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(serverPath));
					writer.open();
					document.open();
					Image image = Image.getInstance(imageTempPath.toString());
					// image.scaleAbsolute(500f, 650f);
					image.scaleToFit(PageSize.A4.getWidth(), 300);
					document.add(image);
					document.close();
					writer.close();
					Files.delete(imageTempPath);
				} else {
					Path filePath = Paths.get(serverPath);
					Files.write(filePath, bytes);
				}
				responseModel.put(Constant.MESSAGE, MLIMessageConstants.PASSPORT_UPLOAD_SUCC);
				responseModel.put(Constant.STATUS, SaveDetailsResponse.SUCCESS);
				responseModel.put("customerData", customerDetails);
				/*
				 * try { logger.
				 * info(":::::START:::::::saveFinalSubmit:::::Passport uploading to aws s3::::::::"
				 * ); FileUtilityModel fileUtilityModel = new FileUtilityModel();
				 * fileUtilityModel.setLoanAppNo(customerDetails.getLoanAppNumber());
				 * fileUtilityModel.setCustFirstName(customerDetails.getCustomerFirstName());
				 * fileUtilityModel.setDocType(DocType.PASSPORT);
				 * fileUtilityModel.setCreatedOn(customerDetails.getCreatedOn());
				 * fileUtilityModel.setProposalNumber(customerDetails.getProposalNumber());
				 * fileUtilityModel =
				 * awsFileUtility.getPDFFileNdDirectoryForPassport(fileUtilityModel,passportSide
				 * ); awsFileUtility.createFileOnAWSS3(serverPath,
				 * fileUtilityModel.getAwsFilePath());
				 * awsFileUtility.updateAwsPassportFilePathInDB(fileUtilityModel); logger.
				 * info(":::::END:::::::saveFinalSubmit:::::Passport uploading to aws s3::::::::serverPath="
				 * + serverPath + "::::fileUtilityModel.getAwsFilePath()=" +
				 * fileUtilityModel.getAwsFilePath()); } catch (Exception e) { logger.
				 * error(":::::ERROR:::::::saveFinalSubmit:::::Passport uploading to aws s3::::::::"
				 * + e); e.printStackTrace(); }
				 */
				return responseModel;
			} else {
				responseModel.put(Constant.MESSAGE, MLIMessageConstants.UPLOAD_FILE_FORMAT + fileExtensionString);
				responseModel.put(Constant.STATUS, SaveDetailsResponse.FAILURE);
				return responseModel;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
			responseModel.put(Constant.STATUS, SaveDetailsResponse.FAILURE);
			responseModel.put(Constant.MESSAGE, Constant.FAILURE);
			return responseModel;
		}
	}

	/**
	 * Search and filter on Draft screen
	 * 
	 * @param param  (firatName , lastName , loanAppNumber)
	 * @param status (PENDING , SFV , V) SVF : Send for Verification V : Verified
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public Map<String, Object> search(Long slrContNo, String param, String status, Pageable pageable,String isFinance) {
		try {
			SellerDetailEntity sellerDetailEntity = sellerDAO.findByContactNo(slrContNo);
			Map<String, Object> countResultMap = customerDetailsDAO.search(sellerDetailEntity.getSellerDtlId(), param,
					status, pageable,isFinance);
			List<CustomerDetailsEntity> userEntity = (List<CustomerDetailsEntity>) countResultMap
					.get(Constant.QUERY_RESULT);
			Long totalCount = (Long) countResultMap.get(Constant.TOTAL_COUNT);
			/*
			 * List<UserDetailsModel> userDetailsModels = new
			 * ArrayList<>(userEntity.size()); for (CustomerDetailsEntity
			 * customerDetailsEntity : userEntity) { UserDetailsModel userDetailsModel =
			 * getUserModel(customerDetailsEntity); userDetailsModels.add(userDetailsModel);
			 * }
			 */
			List<UserDetailsModel> userDetailsModels = getCustomerModel(userEntity, null);
			PageInfoModel pageInfoModel = PageableUtil.getPageMetaInfo(pageable, userDetailsModels.size(), totalCount);
			Map<String, Object> response = new HashMap<>(2);
			response.put(Constant.PAGE_META_INFO, pageInfoModel);
			response.put(Constant.RESULT, userDetailsModels);
			return response;
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return null;
	}

	/**
	 * get status based on data on customer verification screen
	 * @param data
	 * @return
	 * @author rajkumar
	 */
	@Transactional
	@Override
	public Map<String, Object> getStatus(String data) {
		Map<String, Object> resultMap = new HashMap<>(1);
		try {
			String decryptData = AES.decrypt(data, aesSecratKey);
			if (decryptData == null) {
				resultMap.put(Constant.STATUS, Constant.FAILURE);
				resultMap.put(Constant.MESSAGE, "Invalid Access Key");
				logger.info("decryptData : " + decryptData);
				return resultMap;
			}
			String values[] = decryptData.split("_");
			String mobileNumber = values[0];
			String proposalNumber = values[1];
			resultMap.put("proposalNumber", proposalNumber);
			resultMap.put("mobileNumber", mobileNumber);
			CustomerDetailsEntity customerDetailsEntity = customerDetailsDAO.findByProposalNumber(proposalNumber);
			if (customerDetailsEntity != null) {
				logger.info(
						"Proposal number : " + proposalNumber + ", Id : " + customerDetailsEntity.getCustomerDtlId());
				resultMap.put(Constant.STATUS,
						customerDetailsHelper.getPrintedStatus(customerDetailsEntity.getStatus()));
				if (customerDetailsEntity.getCustMobileNo() != null
						&& customerDetailsEntity.getCustMobileNo().equals(Long.parseLong(mobileNumber))) {
					resultMap.put(Constant.MOBILE_VERIFIED, true);
				} else {
					resultMap.put(Constant.MOBILE_VERIFIED, false);
				}
				UserDetailsModel userDetailsModel = getUserModel(customerDetailsEntity);
				resultMap.put("userDetails", userDetailsModel);
			} else {
				resultMap.put(Constant.STATUS, Constant.FAILURE);
				resultMap.put(Constant.MESSAGE, "Customer not found for given Proposal Number");
			}
		} catch (Exception exception) {
			resultMap.put(Constant.STATUS, Constant.FAILURE);
			resultMap.put(Constant.MESSAGE, Constant.FAILURE_MSG);
			exception.printStackTrace();
		}
		return resultMap;
	}

	/*
	 * private String getPrintedStatus(Status status) { String printedStatus = "";
	 * if (status.equals(Status.APP_PENDING)) { printedStatus = Constant.WIP; } else
	 * if (status.equals(Status.APP_SENT)) { printedStatus = Constant.SFV; } else if
	 * (status.equals(Status.APP_VERIFIED)) { printedStatus = Constant.V; } else if
	 * (status.equals(Status.APP_COMPLETE)) { printedStatus = "Completed"; } else if
	 * (status.equals(Status.APP_NOT_INTERESTED)) { printedStatus = Constant.NI; }
	 * return printedStatus; }
	 */

	/**
	 * get all customer based on given parameter in excel format
	 * @param from
	 * @param to
	 * @param masterPlcy
	 * @param appStatus
	 * @return
	 * @author rajkumar
	 */
	@Override
	public List<UserDetailsModel> getAllCustomerForExcel(Long from, Long to, String masterPlcy, Status appStatus) {
		logger.info("from : " + DateUtil.extractDateWithTSAsStringSlashFormate(from) + ", to : "
				+ DateUtil.extractDateWithTSAsStringSlashFormate(to));
		try {
			List<CustomerDetailsEntity> customerDetailsEntities = customerDetailsDAO.getAllCustomerForExcel(from, to,
					masterPlcy, appStatus);
			List<UserDetailsModel> userDetailsModels = new ArrayList<UserDetailsModel>(customerDetailsEntities.size());
			for (CustomerDetailsEntity customerDetailsEntity : customerDetailsEntities) {
				UserDetailsModel userDetailsModel = getUserModel(customerDetailsEntity);
				userDetailsModels.add(userDetailsModel);
			}
			return userDetailsModels;
		} catch (Exception e) {
			logger.error("::::::::::::::::: Error in get Submited ProposalsByBankNdDate() :::::::::::::::" + e);
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * method is used to find UserDetailsModel based on encrypted data
	 * @param data
	 * @return
	 * @author rajkumar
	 */
	@Override
	@Transactional
	public ResponseModel<UserDetailsModel> getCustomerDetails(String data) {
		ResponseModel<UserDetailsModel> responseModel = new ResponseModel<UserDetailsModel>();
		try {
			String decryptData = AES.decrypt(data, aesSecratKey);
			String values[] = decryptData.split("_");
			String proposalNumber = values[1];
			CustomerDetailsEntity customerDetailsEntity = customerDetailsDAO.findByProposalNumber(proposalNumber);
			if (customerDetailsEntity != null) {
				logger.info(
						"Proposal number : " + proposalNumber + ", Id : " + customerDetailsEntity.getCustomerDtlId());
				UserDetailsModel userDetailsModel = getUserModel(customerDetailsEntity);
				responseModel.setData(userDetailsModel);
				responseModel.setStatus(Constant.SUCCESS);
				responseModel.setMessage(MLIMessageConstants.CUSTOMER_DATA_FETCHED);
			} else {
				responseModel.setStatus(Constant.FAILURE);
				responseModel.setMessage(MLIMessageConstants.CUSTOMER_NOT_FOUND);
			}
		} catch (Exception exception) {
			logger.error(":::::::::::::::: Error in get Customer Details :::::::::::::" + exception.getMessage());
			exception.printStackTrace();
			responseModel.setStatus(Constant.FAILURE);
			responseModel.setStatus(Constant.FAILURE_MSG);
		}
		return responseModel;
	}

	/**
	 * edit reflextive questions
	 * @param csModel
	 * @return
	 * @author rajkumar
	 */
	@Override
	@Transactional
	public ResponseModel<?> editReflexiveQuestion(CSModel csModel) {
		ResponseModel<?> responseModel = new ResponseModel<UserDetailsModel>();
		try {
			String decryptData = AES.decrypt(csModel.getData(), aesSecratKey);
			String values[] = decryptData.split("_");
			String proposalNumber = values[1];
			CustomerDetailsEntity customerDetailsEntity = customerDetailsDAO.findByProposalNumber(proposalNumber);
			if (customerDetailsEntity != null) {
				if (customerDetailsEntity.getStatus().getLabel().equals(Status.APP_VERIFIED.getLabel())) {
					responseModel.setStatus(Constant.FAILURE);
					responseModel.setMessage("Customer Already Verified");
					return responseModel;
				}
				logger.info(
						"Proposal number : " + proposalNumber + ", Id : " + customerDetailsEntity.getCustomerDtlId());
				if(Constant.HEALTH_TYPE_SFQ.equals(customerDetailsEntity.getHealthType())) {
					// SFQ health declaration
					SFQHealthDeclarationEntity sfqHealthDeclarationEntity = sfqHealthDeclarationDAO
							.findByCustomerDtlId(customerDetailsEntity.getCustomerDtlId());
					if(!ObjectUtils.isEmpty(sfqHealthDeclarationEntity) && csModel.getSfqHealthDeclarationModel() != null) {
						sfqHealthDeclarationEntity = sfqHealthDeclarationHelper.updateSFQHealthEntity(sfqHealthDeclarationEntity,csModel.getSfqHealthDeclarationModel());
						sfqHealthDeclarationDAO.saveOrUpdate(sfqHealthDeclarationEntity);
						if(!StringUtils.isEmpty(customerDetailsEntity.getMasterPolicyHolderName())) {
							String pHolderName = customerDetailsEntity.getMasterPolicyHolderName();
							String bankName = pHolderName.contains(" ") ? pHolderName.split(" ")[0] : pHolderName;
							AdminProductConfigEntity adminProductConfigEntity = adminProductConfigService.getAdminConfigDetails(bankName);
							if(!ObjectUtils.isEmpty(adminProductConfigEntity) && Constant.YES.equals(adminProductConfigEntity.getIsCovidEnable())) {
								SFQCovidEntity sfqCovidEntity = sfqCovidDAO.findBySFQHealthDtlId(sfqHealthDeclarationEntity.getHealthDclId());
								if (!ObjectUtils.isEmpty(sfqCovidEntity) && csModel.getSfqHealthDeclarationModel().getSfqCovidModel() != null) {
									sfqCovidEntity = sfqHealthDeclarationHelper.updateSFQCovidEntity(sfqCovidEntity,
											csModel.getSfqHealthDeclarationModel().getSfqCovidModel());
									sfqCovidDAO.saveOrUpdate(sfqCovidEntity);
								}
							}
						}
					} else {
						responseModel.setStatus(Constant.FAILURE);
						responseModel.setMessage(MLIMessageConstants.HEALTH_INFO_NOT_FOUND);
						return responseModel;
					}
					logger.info("Saved all SQF health updated answer");
					responseModel.setMessage(MLIMessageConstants.RQ_SAVED);
					responseModel.setStatus(SaveDetailsResponse.SUCCESS);
					return responseModel;
				} else {
					// DOGH health declaration
					HealthDeclarationEntity healthDeclarationEntity = healthDeclarationDAO
							.findByCustomerDtlId(customerDetailsEntity.getCustomerDtlId());
					if (healthDeclarationEntity != null) {
						healthDeclarationEntity.setApplicationNumber(csModel.getApplicationNumber());
						healthDeclarationEntity.setIsApplication(csModel.getIsApplication());
						healthDeclarationEntity.setIsHealthDeclaration(csModel.getIsHealthDeclaration());
						healthDeclarationEntity.setOtherHealthInfo(csModel.getOtherHealthInfo());
						if (!CollectionUtils.isEmpty(csModel.getCiRiderQuestionAns())) {
							healthDeclarationEntity = updateCiRiderQuestionsAnswer(healthDeclarationEntity, csModel);
						}
						healthDeclarationDAO.saveOrUpdate(healthDeclarationEntity);
						if(!StringUtils.isEmpty(customerDetailsEntity.getMasterPolicyHolderName())) {
							String pHolderName = customerDetailsEntity.getMasterPolicyHolderName();
							String bankName = pHolderName.contains(" ") ? pHolderName.split(" ")[0] : pHolderName;
							AdminProductConfigEntity adminProductConfigEntity = adminProductConfigService.getAdminConfigDetails(bankName);
								if(!ObjectUtils.isEmpty(adminProductConfigEntity) && Constant.YES.equals(adminProductConfigEntity.getIsCovidEnable()) && !ObjectUtils.isEmpty(csModel.getCovid_19Details())) {
									Covid19Entity covid19Entity = covid19Dao.findByHealthDtlId(healthDeclarationEntity.getHealthDclId());
								    if(!ObjectUtils.isEmpty(covid19Entity)) {
								    	covid19Entity = healthDeclarationHelper.updateCovid19Entity(covid19Entity, csModel.getCovid_19Details());
								    	covid19Dao.saveOrUpdate(covid19Entity);
								    }
							     }
						}
						
					} else {
						responseModel.setStatus(Constant.FAILURE);
						responseModel.setMessage(MLIMessageConstants.HEALTH_INFO_NOT_FOUND);
						return responseModel;
					}
					List<ReflexiveAnswerEntity> entity = answerDao
							.findByCustomerIdAndType(customerDetailsEntity.getCustomerDtlId(), Constant.DISEASE);
					deletePreviousAnswer(entity);
					logger.info("Deleted all previous answer of DISEASE");
					saveRefQuestionAnswer(csModel.getHealthAnswerModel(), customerDetailsEntity.getCustomerDtlId());
					logger.info("Saved all new answer");
					responseModel.setMessage(MLIMessageConstants.RQ_SAVED);
					responseModel.setStatus(SaveDetailsResponse.SUCCESS);
					return responseModel;
					}
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
	 * update  HealthDeclarationEntity object 
	 * @param healthDeclarationEntity
	 * @param csModel
	 * @return
	 * @author rajkumar
	 */
	private HealthDeclarationEntity updateCiRiderQuestionsAnswer(HealthDeclarationEntity healthDeclarationEntity,
			CSModel csModel) {
		String anslist = "";
		for (HealthAnswerModel healthAnswerModel : csModel.getCiRiderQuestionAns()) {
			if ("yes".equalsIgnoreCase(healthAnswerModel.getQuestionAns())) {
				anslist = anslist + healthAnswerModel.getQuestionId() + ",";
			}
		}
		if (csModel.getCiHealthDecsAns() != null && csModel.getCiHealthDecsAns() != "") {
			healthDeclarationEntity.setCiHealthDecsAns(csModel.getCiHealthDecsAns());
		}
		healthDeclarationEntity.setHealthDeclarationForCIRider(anslist);
		return healthDeclarationEntity;
	}

	/**
	 * Get Customers in excel
	 */
	@Transactional
	@Override
	public boolean getCustomerInBulk(String fromDate, String toDate) {
		boolean customerDownloadFlag = false;
		try {
			List<CustomerDetailsEntity> customerDetailsEntities = null;
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
			customerDetailsEntities = customerDetailsDAO.getAllCustomerForExcel(from, to);
			/*
			 * List<UserDetailsModel> userDetailsModels = new
			 * ArrayList<UserDetailsModel>(customerDetailsEntities.size()); for
			 * (CustomerDetailsEntity customerDetailsEntity : customerDetailsEntities) {
			 * UserDetailsModel userDetailsModel = getUserModel(customerDetailsEntity);
			 * userDetailsModels.add(userDetailsModel); }
			 */
			List<UserDetailsModel> userDetailsModels = getCustomerModel(customerDetailsEntities, "");
			excelUtility.getExcelData(userDetailsModels, customerExcelPath, Constant.AXIS,
					Constant.CUSTOMER_SIMPLE_DUMP);
			customerDownloadFlag = true;
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return customerDownloadFlag;
	}

	/**
	 * convert list of CustomerDetailsEntity into list of UserDetailsModel based on given action
	 * @param customerDetailsEntities
	 * @param action
	 * @return
	 * @author rajkumar
	 */
	public List<UserDetailsModel> getCustomerModel(List<CustomerDetailsEntity> customerDetailsEntities, String action) {
		List<Long> customerIds = new ArrayList<>(customerDetailsEntities.size());
		List<Long> sellerIds = new ArrayList<>(customerDetailsEntities.size());

		customerDetailsEntities.parallelStream().forEach(c -> {
			sellerIds.add(c.getSlrDtlId().getSellerDtlId());
			customerIds.add(c.getCustomerDtlId());
		});

		List<NomineeDetailsEntity> nominees = nomineeDetailsDAO.findByCustomerDtlIds(customerIds);
		List<Long> nomineeIds = new ArrayList<>(nominees.size());
		// get map of custDtlId -> nominee
		Map<Long, Object> nomnieeMap = new HashMap<>(nominees.size());
		nominees.parallelStream().forEach(n -> {
			nomineeIds.add(n.getNomineeDtlId());
			nomnieeMap.put(n.getCustomerDtlId().getCustomerDtlId(), n);
		});
		nominees = null;

		List<AppointeeDetailsEntity> appointeeDetailsEntities = appointeeDAO.findByNomineeDtlIds(nomineeIds);
		// get map of nomineeDtlId -> appointee
		Map<Long, Object> appointeeMap = new HashMap<>(appointeeDetailsEntities.size());
		appointeeDetailsEntities.parallelStream().forEach(a -> {
			appointeeMap.put(a.getNomineeDtlId().getNomineeDtlId(), a);
		});
		appointeeDetailsEntities = null;
		
		List<SFQHealthDeclarationEntity> sfqHealthDeclarationEntities = sfqHealthDeclarationDAO
				.findByCustomerDtlIds(customerIds);
		//for SFQ health
		Map<Long, Object> sfqHealthMap = new HashMap<>(sfqHealthDeclarationEntities.size());
		sfqHealthDeclarationEntities.parallelStream().forEach(health -> {
			sfqHealthMap.put(health.getCustomerDtlId().getCustomerDtlId(), health);
		});
		sfqHealthDeclarationEntities = null;

		
		//for DOGH health
		List<HealthDeclarationEntity> healthDeclarationEntities = healthDeclarationDAO
				.findByCustomerDtlIds(customerIds);
		// get map of custDtlId -> healthDeclaration
		Map<Long, Object> healthMap = new HashMap<>(healthDeclarationEntities.size());
		healthDeclarationEntities.parallelStream().forEach(h -> {
			healthMap.put(h.getCustomerDtlId().getCustomerDtlId(), h);
		});
		healthDeclarationEntities = null;

		List<MandatoryDeclarationEntity> mandatoryDeclarationEntities = mandatoryDeclarationDAO
				.findbyCustomerDtlIds(customerIds);
		// get map of custDtlId -> mandatoryDeclaration
		Map<Long, Object> mandatoryMap = new HashMap<>(mandatoryDeclarationEntities.size());
		mandatoryDeclarationEntities.parallelStream().forEach(m -> {
			mandatoryMap.put(m.getCustomerDtlId().getCustomerDtlId(), m);
		});
		mandatoryDeclarationEntities = null;

		List<SellerDetailEntity> sellerDetailEntities = sellerDAO.findBySellerDtlIds(sellerIds);
		// get map of sellerId -> sellerDetail
		Map<Long, Object> sellerMap = new HashMap<>(sellerDetailEntities.size());
		sellerDetailEntities.parallelStream().forEach(s -> {
			sellerMap.put(s.getSellerDtlId(), s);
		});
		sellerDetailEntities = null;

		List<UserDetailsModel> userDetailsModels = new ArrayList<>(customerDetailsEntities.size());
		customerDetailsEntities.stream().forEach(c -> {
			UserDetailsModel userDetailsModel = new UserDetailsModel();
			AppointeeModel appointeeDetails = null;
			if (action == null) {
				userDetailsModel.setCustomerDetails(customerDetailsHelper.convertToModel(c));
			} else {
				userDetailsModel.setCustomerDetails(getCustomerModel(c));
			}
			String proposalNumber = userDetailsModel.getCustomerDetails().getProposalNumber();
			userDetailsModel.setSfvTimeStamp(c.getSfvTimeStamp());
			userDetailsModel.setStatus(customerDetailsHelper.getPrintedStatus(c.getStatus()));
//			NomineeDetailsEntity nomineeDetailsEntity = nomineeDetailsDAO
//					.findByCustomerDtlId(customerDetailsEntity.getCustomerDtlId());
			NomineeDetailsEntity nomineeDetailsEntity = (NomineeDetailsEntity) nomnieeMap.get(c.getCustomerDtlId());
			NomineeDetailsModel nomineeDetails = nomineeDetailsHelper.convertToModel(nomineeDetailsEntity);
			if (nomineeDetails != null && nomineeDetailsEntity != null) {
				nomineeDetails.setIsAppointee(nomineeDetailsEntity.isAppointee());
			}
			if (nomineeDetailsEntity != null)
				/*
				 * appointeeDetails = appointeeDetailsHelper
				 * .convertToModel(appointeeDAO.findByNomineeDtlId(nomineeDetailsEntity.
				 * getNomineeDtlId()));
				 */
				appointeeDetails = appointeeDetailsHelper.convertToModel(
						(AppointeeDetailsEntity) appointeeMap.get(nomineeDetailsEntity.getNomineeDtlId()));

			if (appointeeDetails != null)
				nomineeDetails.setAppointeeDetails(appointeeDetails);
			userDetailsModel.setNomineeDetails(nomineeDetails);
			// mandatory details
//			MandatoryDeclarationModel mandatoryDeclaration = mandatoryDeclarationHelper
//					.convertToModel(mandatoryDeclarationDAO.findbyCustomerDtlId(customerDetailsEntity.getCustomerDtlId()));
			MandatoryDeclarationModel mandatoryDeclaration = mandatoryDeclarationHelper
					.convertToModel((MandatoryDeclarationEntity) mandatoryMap.get(c.getCustomerDtlId()));
			userDetailsModel.setMandatoryDeclaration(mandatoryDeclaration);

			if(Constant.HEALTH_TYPE_SFQ.equals(c.getHealthType())){
				// SFQ health declaration
				SFQHealthDeclarationEntity sfqHealthDeclarationEntity = (SFQHealthDeclarationEntity) sfqHealthMap.get(c.getCustomerDtlId());
				if(!ObjectUtils.isEmpty(sfqHealthDeclarationEntity)) {
				    SFQHealthDeclarationModel sfqHealthDeclarationModel = sfqHealthDeclarationHelper.convertToModel(sfqHealthDeclarationEntity);
				    SFQCovidEntity sfqCovidEntity = sfqCovidDAO.findBySFQHealthDtlId(sfqHealthDeclarationEntity.getHealthDclId());
				    if(!ObjectUtils.isEmpty(sfqCovidEntity)) {
				    	SFQCovidModel sfqCovidModel = sfqHealthDeclarationHelper.convertToModel(sfqCovidEntity);
				    	if(sfqCovidModel != null)
				    		sfqHealthDeclarationModel.setSfqCovidModel(sfqCovidModel);
				    }
				    userDetailsModel.setSfqHealthDeclaration(sfqHealthDeclarationModel);
				}
		    }else {
				// DOGH health declaration
				if (action == null) {
					HealthDeclarationEntity healthDeclarationEntity = (HealthDeclarationEntity) healthMap.get(c.getCustomerDtlId());
					HealthDeclarationModel healthDeclaration = healthDeclarationHelper
							.convertToModel(healthDeclarationEntity);
					if (healthDeclaration != null) {
						Covid19Entity covid19Entity = covid19Dao.findByHealthDtlId(healthDeclarationEntity.getHealthDclId());
						healthDeclaration.setCovid_19Details(healthDeclarationHelper.getCovid_19Model(covid19Entity));
						healthDeclaration.setSavedAnswers(
								reflexiveQuestionService.getAllAnswer(Constant.DISEASE, c.getCustomerDtlId(), null));
					}
					userDetailsModel.setHealthDeclaration(healthDeclaration);
				} else {
					HealthDeclarationEntity declarationEntity = (HealthDeclarationEntity) healthMap.get(c.getCustomerDtlId());
					HealthDeclarationModel healthDeclaration = getHealthModel(declarationEntity);
					if(declarationEntity != null) {
						Covid19Entity covid19Entity = covid19Dao.findByHealthDtlId(declarationEntity.getHealthDclId());
						healthDeclaration.setCovid_19Details(healthDeclarationHelper.getCovid_19Model(covid19Entity));
					}
					userDetailsModel.setHealthDeclaration(healthDeclaration);
				}
			}
			if (c.getSlrDtlId() != null) {
//				SellerDetailEntity sellerDetailEntity = sellerDAO.getEntity(SellerDetailEntity.class,
//						customerDetailsEntity.getSlrDtlId().getSellerDtlId());
				SellerDetailEntity sellerDetailEntity = (SellerDetailEntity) sellerMap
						.get(c.getSlrDtlId().getSellerDtlId());
				if (sellerDetailEntity != null) {
					userDetailsModel.setHomeLoanGroupPolicyNo(sellerDetailEntity.getGroupPolicyNumber());
					userDetailsModel.setSellerContNumber(sellerDetailEntity.getContactNo().toString());
					userDetailsModel.setMliRMCode(
							ObjectsUtil.isNotNull(sellerDetailEntity.getMliRM()) ? sellerDetailEntity.getMliRM()
									: "NA");
					userDetailsModel.setMliSMCode(
							ObjectsUtil.isNotNull(sellerDetailEntity.getMliSMCode()) ? sellerDetailEntity.getMliSMCode()
									: "NA");
				}
			}
			userDetailsModel.setProposalNumber(c.getProposalNumber());
			if (c.getAppStep() != null) {
				userDetailsModel.setSteps(c.getAppStep().getValue());
			}


			NationalityDetail nationalityDetail = new NationalityDetail();
			if (action == null) {
				nationalityDetail.setSavedAnswers(
						reflexiveQuestionService.getAllAnswer(Constant.NATIONALITY, c.getCustomerDtlId(), null));
			}
			if (action == null) {
				SchemeDirEntity schemeDirEntity = schemeDirDao.findByProposalNumberNdeDocType(proposalNumber,
						DocType.PASSPORT);
				if (schemeDirEntity != null) {
					nationalityDetail.setImageExist(true);
				}
			}
			userDetailsModel.setNationalityDetails(nationalityDetail);
			
		  List<CamReportDetailsEntity> camReportList =camReportDetailsDao.getProposals(proposalNumber);
		  if(camReportList!=null) {
			  userDetailsModel.getCustomerDetails().setCamReportUrls(camReportList);
		  }
		  
		  List<FileUploadModel>commonFileUpload=commonFileUploadDao.getProposalsNumber(proposalNumber);
		  if(!commonFileUpload.isEmpty())
			  userDetailsModel.getCustomerDetails().setPhysicalFileList(commonFileUpload);
		  
		  List<CovidReportEntity> covidReportList = covidReportDAO.findAllReport(proposalNumber);
		  if(!covidReportList.isEmpty())
			  userDetailsModel.getCustomerDetails().setCovidReportList(sfqHealthDeclarationHelper.convertToCovidReportModelList(covidReportList));
		
		  
		  userDetailsModel.getCustomerDetails().setStreet(c.getStreet());
		  userDetailsModel.getCustomerDetails().setAddress1(c.getAddress1());
		  userDetailsModel.getCustomerDetails().setAddress2(c.getAddress2());
		  userDetailsModel.getCustomerDetails().setAddress3(c.getAddress3());
		  userDetailsModel.getCustomerDetails().setPinCode(c.getPinCode());
		  userDetailsModel.getCustomerDetails().setTown(c.getTown());
		  userDetailsModel.getCustomerDetails().setState(c.getState());
		  userDetailsModel.getCustomerDetails().setCountry(c.getCountry());
		  
		  
		  userDetailsModel.getCustomerDetails().setGender(c.getGender());
		  
		  
		  userDetailsModel.getCustomerDetails().setBaseOrBaseWithCI(ObjectsUtil.isNotNull(c.getBaseOrBaseWithCI()) ?  c.getBaseOrBaseWithCI() : null);
		  userDetailsModel.getCustomerDetails().setPercentageOfSumAssured(ObjectsUtil.isNotNull(c.getPercentageOfSumAssured()) ?  c.getPercentageOfSumAssured() : null);
		  userDetailsModel.getCustomerDetails().setCiOption(ObjectsUtil.isNotNull(c.getCiOption()) ?  c.getCiOption() : null);
		  userDetailsModel.getCustomerDetails().setCiTenureYears(ObjectsUtil.isNotNull(c.getCiTenureYears()) ?  c.getCiTenureYears() : null);
		  userDetailsModel.getCustomerDetails().setCiRiderSumAssured(c.getCiRiderSumAssured());
		  userDetailsModel.getCustomerDetails().setCiRiderPermium(c.getCiRiderPermium());
		  userDetailsModel.getCustomerDetails().setBaseWithCIPremium(c.getBaseWithCIPremium());
		  userDetailsModel.getCustomerDetails().setMedicalUWCI(c.getMedicalUWCI());
		  userDetailsModel.getCustomerDetails().setFinancialUWCI(c.getFinancialUWCI());
		  userDetailsModel.getCustomerDetails().setOverallUWFinancial(c.getOverallUWFinancial());
		  userDetailsModel.getCustomerDetails().setOverallUWMedical(c.getOverallUWMedical());
		  userDetailsModel.getCustomerDetails().setTentativeEMIBaseWithCI(c.getTentativeEMIBaseWithCI());
		  userDetailsModel.getCustomerDetails().setIncrementalEMIBaseWithCI(c.getIncrementalEMIBaseWithCI());
		  userDetailsModel.getCustomerDetails().setFormStatus(c.getFormStatus());
		  userDetailsModel.getCustomerDetails().setHealthType(c.getHealthType() == null ? Constant.HEALTH_TYPE_DOGH:Constant.HEALTH_TYPE_SFQ);
		  if(ObjectsUtil.isNotNull(c.getIsFinancePremium()) && c.getIsFinancePremium().equals("No") && ObjectsUtil.isNotNull(c.getStatus()) && c.getStatus().equals(Status.APP_VERIFIED)) {
			  userDetailsModel.getCustomerDetails().setPaymentLink(Constant.PAYMENT_LINK);
			  
		  }
		  userDetailsModel.getCustomerDetails().setRoId(c.getRoId());
		  userDetailsModel.getCustomerDetails().setSmId(c.getSmId());
		  userDetailsModels.add(userDetailsModel);
		});
		
		customerDetailsEntities = null;
		return userDetailsModels;
	}

	/**
	 * convert CustomerDetailsEntity into CustomerDetailsModel
	 * @param source
	 * @return
	 * @author rajkumar
	 */
	public CustomerDetailsModel getCustomerModel(CustomerDetailsEntity source) {
		if (source == null)
			return null;

		CustomerDetailsModel customerDetailsModel = new CustomerDetailsModel();
		customerDetailsModel.setCustomerDtlId(source.getCustomerDtlId());
		if (source.getLoanType() != null) {
			customerDetailsModel.setLoanType(source.getLoanType().getCode());
			customerDetailsModel.setLoanTypeDesc(source.getLoanType().getText());
		}
		if (source.getSchemeType() != null) {
			customerDetailsModel.setSchemeType(source.getSchemeType().getLabel());
		}
		if (source.getMasterPolicyHolderName() != null) {
			customerDetailsModel.setMasterPolicyholderName(source.getMasterPolicyHolderName());
		}
		customerDetailsModel.setLoanAmount(source.getLoanAmount());
		customerDetailsModel.setGender(source.getGender());
		customerDetailsModel.setCustomerFirstName(source.getCustomerFirstName());
		customerDetailsModel.setCustomerLastName(source.getCustomerLastName());
		customerDetailsModel.setLoanAppNumber(source.getLoanAppNumber());
		customerDetailsModel.setCustMobileNo(source.getCustMobileNo());
		customerDetailsModel.setCustEmailId(source.getCustEmailId());
		customerDetailsModel.setSumAssured(source.getSumAssured());
		if (source.getRelationshipGpPolicyHolder() != null) {
			customerDetailsModel.setRelationshipGpPolicyHolder(source.getRelationshipGpPolicyHolder().getLabel());
		}
		customerDetailsModel
				.setProposalNumber(!ObjectsUtil.isNull(source.getProposalNumber()) ? source.getProposalNumber() : null);
		customerDetailsModel.setLoanTenure(source.getLoanTenure());
		customerDetailsModel.setAppCompletionDate(source.getAppCompletionDate());
		customerDetailsModel.setDob(DateUtil.dateInFormatddmmyyyy(source.getDob()));
		customerDetailsModel.setAdhaarNumber(source.getAdhaarNumber());
		customerDetailsModel.setVerifiedOtp(source.getVerifiedOtp());
		if (source.getProposerEntity() != null) {
			customerDetailsModel
					.setProposerDetails(modelMapper.map(source.getProposerEntity(), ProposerDetailModel.class));
		}
		/*
		 * ProposerDetailEntity proposerDetailEntity =
		 * proposerDetailDao.findByCustomerDtlId(source.getCustomerDtlId()); if
		 * (proposerDetailEntity != null) {
		 * customerDetailsModel.setIsProposer(Boolean.TRUE); } else {
		 * customerDetailsModel.setIsProposer(Boolean.FALSE); }
		 */

		customerDetailsModel.setStatus(customerDetailsHelper.getPrintedStatus(source.getStatus()));
		customerDetailsModel.setOtherPlace(source.getOtherPalce());
		customerDetailsModel.setNationality(source.getNationality());
		customerDetailsModel.setOccupation(source.getOccupation());
//		customerDetailsModel.setSavedAnswers(
//				reflexiveQuestionService.getAllAnswer(Constant.OCCUPATION, source.getCustomerDtlId(), null));
		customerDetailsModel
				.setSavedAnswers(reflexiveQuestionService.getAllAnswer(null, source.getCustomerDtlId(), null));

		customerDetailsModel.setSellerMobileNumber(source.getSlrDtlId().getContactNo());
		customerDetailsModel.setSelletName(source.getSlrDtlId().getSellerName());
		customerDetailsModel.setInterested(source.getIsInterested());
		customerDetailsModel.setIncrementalEMI(source.getIncrementalEMI());
		customerDetailsModel.setInterestRate(source.getInterestRate());
		customerDetailsModel.setReasonForNotInterested(source.getReseanForInterested());
		customerDetailsModel.setFinancePremium(source.getIsFinancePremium());
		customerDetailsModel.setTenureEligible("" + source.getTenureEligible());
		customerDetailsModel.setTentativeEMI(source.getTentativeEMI());
		customerDetailsModel.setMedicalRequired(source.getIsMedicalUnderWritingRequired());
		customerDetailsModel.setFinancialUWRequired(source.getIsFinancialUnderWritingRequired());
		customerDetailsModel.setVerifiedDate(source.getAppCompletionDate());
		customerDetailsModel.setCreatedOn(source.getCreatedOn());
		customerDetailsModel.setTotalPremium("" + source.getTotalPremium());
		customerDetailsModel.setModifiedOn(source.getModifiedOn());
		if (source.getSlrDtlId() != null) {
			customerDetailsModel.setSourceEmpCode(source.getSlrDtlId().getSourceEmpCode());
			customerDetailsModel.setMliSalesManager(source.getSlrDtlId().getMliSalesManager());
			customerDetailsModel.setMliRM(source.getSlrDtlId().getMliRM());
			customerDetailsModel.setRacLocationMapping(source.getSlrDtlId().getRacLocationMapping());
		}
		  List<CamReportDetailsEntity> camReportList =camReportDetailsDao.getProposals(source.getProposalNumber());
		  if(camReportList!=null) {
			  customerDetailsModel.setCamReportUrls(camReportList);
		  }
		  
		  List<CovidReportEntity> covidReportList = covidReportDAO.findAllReport(source.getProposalNumber());
		  if(!covidReportList.isEmpty())
			customerDetailsModel.setCovidReportList(sfqHealthDeclarationHelper.convertToCovidReportModelList(covidReportList));
	
		  
		  customerDetailsModel.setStreet(source.getStreet());
		  customerDetailsModel.setAddress1(source.getAddress1());
		  customerDetailsModel.setAddress2(source.getAddress2());
		  customerDetailsModel.setAddress3(source.getAddress3());
		  customerDetailsModel.setPinCode(source.getPinCode());
		  customerDetailsModel.setTown(source.getTown());
		  customerDetailsModel.setState(source.getState());
		  customerDetailsModel.setCountry(source.getCountry());
		  customerDetailsModel.setHealthType(source.getHealthType() == null ? Constant.HEALTH_TYPE_DOGH:Constant.HEALTH_TYPE_SFQ);
		  
		/*
		 * if (source.getCamReportUrls() != null && source.getCamReportUrls() != "") {
		 * String[] camUrlList = source.getCamReportUrls().split(","); List<String>
		 * urlsList = new ArrayList<>(); for (String singleUrl : camUrlList) { if
		 * (singleUrl != null && singleUrl != "") { urlsList.add(singleUrl); } }
		 * customerDetailsModel.setCamReportUrls(urlsList); }
		 */
		source = null;
		return customerDetailsModel;
	}

	/**
	 * convert HealthDeclarationEntity into HealthDeclarationModel
	 * @param source
	 * @return
	 * @author rajkumar
	 */
	public HealthDeclarationModel getHealthModel(HealthDeclarationEntity source) {
		if (source == null)
			return null;
		HealthDeclarationModel healthDeclarationModel = new HealthDeclarationModel();
		healthDeclarationModel.setNegativeDeclaration(source.getNegativeDeclaration());
		healthDeclarationModel.setIsHealthDeclaration(source.isHealthDeclaration());
		healthDeclarationModel.setNegativeDeclaration(source.getNegativeDeclaration());
		healthDeclarationModel.setOtherInsurance(source.getOtherInsurance());
		healthDeclarationModel.setCreatedOn(source.getCreatedOn());
		healthDeclarationModel.setIsApplication(source.getIsApplication());
		healthDeclarationModel.setApplicationNumber(source.getApplicationNumber());
		String healthDesc=source.getCiHealthDecsAns();
		//healthDeclarationModel.setCiHealthDecsAns(source.getCiHealthDecsAns());
//		healthDeclarationModel.setSavedAnswers(
//				reflexiveQuestionService.getAllAnswer(null, source.getCustomerDtlId().getCustomerDtlId(), null));
		healthDeclarationModel.setOtherHealthInfo(source.getOtherHealthInfo());
		StringBuilder triggerMsg = new StringBuilder();
		if (source.getDiabetesTriggerMsg() != null && source.getHypertensionTriggerMsg() == null) {
			triggerMsg.append(source.getDiabetesTriggerMsg());
		} else if (source.getDiabetesTriggerMsg() == null && source.getHypertensionTriggerMsg() != null) {
			triggerMsg.append(source.getHypertensionTriggerMsg());
		} else if (source.getDiabetesTriggerMsg() != null && source.getHypertensionTriggerMsg() != null) {
			triggerMsg.append(source.getDiabetesTriggerMsg());
			triggerMsg.append(", ");
			triggerMsg.append(source.getHypertensionTriggerMsg());
		}
		healthDeclarationModel.setTriggerMsg(triggerMsg.toString());
		if (source.getHealthDeclarationForCIRider() != null && source.getHealthDeclarationForCIRider() != "") {
			logger.info("==============start MandatoryDeclarationModel convertToModel=============");
			List<String> savedAns = new ArrayList<>();
			List<HealthAnswerModel> healthAnswerModelList = new ArrayList<>();
			logger.info("===source.getHealthDeclarationForCIRider()=======" + source.getHealthDeclarationForCIRider());
			String[] getList = source.getHealthDeclarationForCIRider().split(",");
			logger.info("=====getList  ======" + getList);
			for (int i = 0; i < getList.length; i++) {
				HealthAnswerModel healthAnswerModel = new HealthAnswerModel();
				healthAnswerModel.setQuestionAns("Yes");
				if(!getList[i].equals("")) {
					if(getList[i].equals("7")) {
						healthAnswerModel.setCiRiderDsc(healthDesc);
					}
				healthAnswerModel.setQuestionId(Integer.parseInt(getList[i]));
				healthAnswerModelList.add(healthAnswerModel);
				savedAns.add(getList[i]);}
			}
			for (int i = 1; i < 8; i++) {
				if (!savedAns.contains(String.valueOf(i))) {
					HealthAnswerModel healthAnswerModel = new HealthAnswerModel();
					healthAnswerModel.setQuestionAns("No");
					healthAnswerModel.setQuestionId(i);
					healthAnswerModelList.add(healthAnswerModel);
				}
			}
			healthDeclarationModel.setCiRiderQuestionAns(healthAnswerModelList);
		}
		source = null;

		return healthDeclarationModel;
	}

	/**
	 * method is used to save cam report into s3
	 * @param files
	 * @param proposalNumber
	 * @param sellerId
	 * @return
	 * @author rajkumar
	 */
	@Override
	@Transactional
	public ResponseModel<List<CamResponseModel>> saveCamFiles(List<MultipartFile> files, String proposalNumber,String sellerId) {
		ResponseModel<List<CamResponseModel>> response = new ResponseModel<>();
		FileTypeTest fileTypeTest=new FileTypeTest();
		
		try {
			List<CamResponseModel> camResponseModelList = new ArrayList<>();
			List<File> filesList = new ArrayList<>();
			File serverFile = null;
			for (MultipartFile multipartFile : files) {
				File dir = new File(accessPath + File.separator + proposalNumber);
				if (!dir.exists())
					dir.mkdirs();

				byte[] bytes = multipartFile.getBytes();

				serverFile = new File(dir.getAbsolutePath() + File.separator + multipartFile.getOriginalFilename());

				BufferedOutputStream stream = null;
				try {
					try {
						stream = new BufferedOutputStream(new FileOutputStream(serverFile));
						stream.write(bytes);
					} catch (Exception e) {

					} finally {
						if (stream != null)
							try {
								stream.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
					}
					filesList.add(serverFile);
					logger.info("Doc upload completed, Location: {}");
				} catch (Exception e) {
					logger.error("Exception in upload Multiple File : {}");
					logger.debug("{}", e);
					// throw new UploadFileException(e.getMessage(), e);
				}
			}
			boolean validate=isFileValide(filesList);
			if (validate == true) {
				boolean value = fileTypeTest.getFileType(filesList);

				if (value == true) {
					CustomerDetailsEntity customerDetailsEntity = customerDetailsDAO.findByProposalNumber(proposalNumber);
					String docType = Constant.AXIS_BANK_LTD.equalsIgnoreCase(customerDetailsEntity.getMasterPolicyHolderName()) ? DocType.COVID_REPORT : DocType.AFL_CAM_REPORT;
					camResponseModelList = awsFileUtility.uploadFileS3BucketByMultipart(proposalNumber, filesList,docType);
					try {
						String urls = "";
						for (CamResponseModel camResponseModel : camResponseModelList) {
							CamReportDetailsEntity camReportDetailsEntity = new CamReportDetailsEntity();
							urls = urls + "," + camResponseModel.getCamReportUrls();
							camReportDetailsEntity.setProposalNumber(proposalNumber);
							camReportDetailsEntity.setCamReportUrls(camResponseModel.getCamReportUrls());
							camReportDetailsEntity.setCamReportUrlsName(camResponseModel.getCamReportUrlsName());
							camReportDetailsEntity.setCamFolderPath(camResponseModel.getCamFolderPath());
							camReportDetailsEntity.setCreatedBy(sellerId);
							camReportDetailsEntity.setCreatedOn(customerDetailsEntity.getCreatedOn());
							camReportDetailsDao.save(camReportDetailsEntity);
						}
						customerDetailsEntity.setModifiedOn(DateUtil.toCurrentUTCTimeStamp());
						customerDetailsDAO.saveOrUpdate(customerDetailsEntity);
						response.setData(camResponseModelList);
						response.setStatus(Constant.SUCCESS);
						
						for(File tmpFile:filesList) {
							if (tmpFile != null) {
								tmpFile.delete();
							}
						}
						
						return response;

					} catch (Exception e) {
						e.printStackTrace();
						logger.info("============== Getting error during saving cam report =============");

					}
					for(File tmpFile:filesList) {
						if (tmpFile != null) {
							tmpFile.delete();
						}
					}

				} else {
					response.setMessage("Malicious File Detected : Cannot upload file ");
					response.setStatus(Constant.FAILURE);
				}
			}else {
				response.setMessage("Malicious File Detected : Cannot upload file ");
				response.setStatus(Constant.FAILURE);
			}
			
			
		} catch (Exception e) {
			return null;
		}
		return response;

	}
	
	/**
	 * delete cam file for given url and proposal number
	 * @param url
	 * @param propsalNo
	 * @return
	 * @author rajkumar
	 */
	public List<CamResponseModel> deleteAdditionalFile(String url, String propsalNo) {
		 awsFileUtility.deleteCamFileOnAWSS3(url);
		List<CamResponseModel>listCamReport=null;
		try {
			camReportDetailsDao.deleteAdditonalDocument(propsalNo,url);
			listCamReport=camReportDetailsDao.getProposalsNumber(propsalNo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return listCamReport;
	}
	
	/**
	 * list of valid file extenstion
	 * @param files
	 * @return
	 * @author rajkumar
	 */
	public boolean isFileValide(List<File> files) {
		List<String> extensionList = new LinkedList<String>();
		extensionList.add(".pdf");
		extensionList.add(".gif");
		extensionList.add(".png");
		extensionList.add(".jpg");
		extensionList.add(".jpeg");
		for (File file : files) {
			String fileExtension = getFileExtension(file);
			if (extensionList.contains(fileExtension)) {

			} else {
				return false;
			}
		}
		return true;
	}

	/**
	 * find file extension
	 * @param file
	 * @return
	 * @author rajkumar
	 */
	private static String getFileExtension(File file) {
		String fileName = file.getName();
		if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
			return fileName.substring(fileName.lastIndexOf("."));
		else
			return "";
	}
	
	
	
	/**
	 * save list of physical file into s3 bucket
	 * @param files
	 * @param proposalNumber
	 * @param sellerId
	 * @param roId
	 * @param smId
	 * @return
	 * @author rajkumar
	 */
	@Override
	@Transactional
	public ResponseModel<List<FileUploadModel>> savePhysicalFormFile(List<MultipartFile> files, String proposalNumber,String sellerId,String roId,String smId) {
		ResponseModel<List<FileUploadModel>> response = new ResponseModel<>();
		FileTypeTest fileTypeTest=new FileTypeTest();
		
		try {
			
			if(!StringUtils.isEmpty(roId) && !ssoManagementService.findByRoId(roId)) {
				response.setMessage("Given RO SSO ID does not exist.");
				response.setStatus(Constant.FAILURE);
				return response;
			}
            if(!StringUtils.isEmpty(smId) && !ssoManagementService.findBySmId(smId)) {
            	response.setMessage("Given SM SSO ID does not exist.");
				response.setStatus(Constant.FAILURE);
				return response;
			}
			
			List<FileUploadModel> fileUploadModelList = new ArrayList<>();
			List<File> filesList = new ArrayList<>();
			File serverFile = null;
			for (MultipartFile multipartFile : files) {
				File dir = new File(physicalFormAcessPath + File.separator + proposalNumber);
				if (!dir.exists())
					dir.mkdirs();

				byte[] bytes = multipartFile.getBytes();

				serverFile = new File(dir.getAbsolutePath() + File.separator + multipartFile.getOriginalFilename());

				BufferedOutputStream stream = null;
				try {
					try {
						stream = new BufferedOutputStream(new FileOutputStream(serverFile));
						stream.write(bytes);
					} catch (Exception e) {

					} finally {
						if (stream != null)
							try {
								stream.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
					}
					filesList.add(serverFile);
					logger.info("Doc upload completed, Location: {}");
				} catch (Exception e) {
					logger.error("Exception in upload Multiple File : {}");
					logger.debug("{}", e);
					// throw new UploadFileException(e.getMessage(), e);
				}
			}
			boolean validate=isFileValide(filesList);
			if (validate == true) {
				boolean value = fileTypeTest.getFileType(filesList);

				if (value == true) {
					CustomerDetailsEntity customerDetailsEntity = customerDetailsDAO.findByProposalNumber(proposalNumber);
					String docType = Status.PHYSICAL_FORM_VERIFICATION == customerDetailsEntity.getStatus() ? DocType.Physical_Form_Journey : DocType.AFL_PHYSICAL_FROM;
					fileUploadModelList = awsFileUtility.uploadFileS3BucketphysicalForm(proposalNumber, filesList,docType);
					try {
						String urls = "";
						for (FileUploadModel fileUploadModel : fileUploadModelList) {
							CommonFileUploadEntity commonFileUploadEntity = new CommonFileUploadEntity();
							urls = urls + "," + fileUploadModel.getFileUrl();
							commonFileUploadEntity.setProposalNumber(proposalNumber);
							commonFileUploadEntity.setFileName(fileUploadModel.getFileName());
							commonFileUploadEntity.setFileUrl(fileUploadModel.getFileUrl());
							commonFileUploadEntity.setFileFolderPath(fileUploadModel.getFileFolderPath());
							commonFileUploadEntity.setFileType("Physical_Form_Journey");
							commonFileUploadDao.save(commonFileUploadEntity);
						}
						customerDetailsEntity.setModifiedOn(DateUtil.toCurrentUTCTimeStamp());
						customerDetailsEntity.setRoId(roId);
						customerDetailsEntity.setSmId(smId);
						customerDetailsDAO.saveOrUpdate(customerDetailsEntity);
						response.setData(fileUploadModelList);
						response.setStatus(Constant.SUCCESS);
						copyPhysicalForm(proposalNumber);
						for(File tmpFile:filesList) {
							if (tmpFile != null) {
								tmpFile.delete();
							}
						}
						
						return response;

					} catch (Exception e) {
						e.printStackTrace();
						logger.info("============== Getting error during saving Physical_Form_Journey report =============");

					}
					for(File tmpFile:filesList) {
						if (tmpFile != null) {
							tmpFile.delete();
						}
					}

				} else {
					response.setMessage("Malicious File Detected : Cannot upload file ");
					response.setStatus(Constant.FAILURE);
				}
			}else {
				response.setMessage("Malicious File Detected : Cannot upload file ");
				response.setStatus(Constant.FAILURE);
			}
			
			
		} catch (Exception e) {
			return null;
		}
		return response;

	}

	/**
	 * delete physical file from given s3 url based on given proposal number
	 * @param fileUrl
	 * @param propsalNo
	 * @return
	 * @author rajkumar
	 */
	@Override
	public List<FileUploadModel> deletePhysicalFormFile(String fileUrl, String propsalNo) {
		 awsFileUtility.deleteCommonFileOnAWSS3(fileUrl);
			List<FileUploadModel>fileUpload=null;
			try {
				commonFileUploadDao.deleteFileDocument(propsalNo,fileUrl);
				fileUpload=commonFileUploadDao.getProposalsNumber(propsalNo);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return fileUpload;
	}
	
    /**
     * copy one folder to another
     * @param proposalNumber
     * @return
     * @author rajkumar
     */
	public boolean copyPhysicalForm(String proposalNumber) {
		logger.info("----Enter into pgysical form copy folder service implementation-----");
		try {
			if (!StringUtils.isEmpty(proposalNumber)) {
				List<CommonFileUploadEntity> commonFileUpload = commonFileUploadDao.getProposals(proposalNumber);
				if(!CollectionUtils.isEmpty(commonFileUpload)) {
					String sourcePath = commonFileUpload.get(0).getFileFolderPath();
					StringBuilder destPath = new StringBuilder();
					destPath.append(Constant.VERIFIED+"_");
					destPath.append(DateUtil.extractDateAsStringDashFormate(DateUtil.toCurrentUTCTimeStamp())+"/");
					awsFileUtility.copyFolder(sourcePath, destPath.toString());
				}
				return true;
			}
		} catch (Exception e) {
			logger.error("---------Execption occurred while copy folder--------");
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * upload list of covid report into s3 bucket
	 * @param files
	 * @param proposalNumber
	 * @param fileType
	 * @return
	 * @author rajkumar
	 */
	@Override
	@Transactional
	public ResponseModel<List<CovidReportModel>> uploadCovidReport(List<MultipartFile> files, String proposalNumber, String fileType) {
		ResponseModel<List<CovidReportModel>> response = new ResponseModel<>();
		FileTypeTest fileTypeTest=new FileTypeTest();
		try {
			List<CovidReportModel> covidReportModelList = new ArrayList<>();
			List<File> filesList = new ArrayList<>();
			File serverFile = null;
			for (MultipartFile multipartFile : files) {
				File dir = new File(covidReportAccessPath + File.separator + proposalNumber);
				if (!dir.exists())
					dir.mkdirs();

				byte[] bytes = multipartFile.getBytes();

				serverFile = new File(dir.getAbsolutePath() + File.separator + multipartFile.getOriginalFilename());

				BufferedOutputStream stream = null;
				try {
					try {
						stream = new BufferedOutputStream(new FileOutputStream(serverFile));
						stream.write(bytes);
					} catch (Exception e) {

					} finally {
						if (stream != null)
							try {
								stream.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
					}
					filesList.add(serverFile);
					logger.info("Doc upload completed, Location: {}");
				} catch (Exception e) {
					logger.error("Exception in upload Multiple File : {}");
					logger.debug("{}", e);
				}
			}
			boolean validate=isFileValide(filesList);
			if (validate == true) {
				boolean value = fileTypeTest.getFileType(filesList);

				if (value == true) {
					CustomerDetailsEntity customerDetailsEntity = customerDetailsDAO.findByProposalNumber(proposalNumber);
					String docType = Constant.AXIS_BANK_LTD.equalsIgnoreCase(customerDetailsEntity.getMasterPolicyHolderName()) ? DocType.COVID_REPORT: DocType.AFL_COVID_REPORT;
					covidReportModelList = awsFileUtility.uploadCovidReportOnS3Bucket(proposalNumber,fileType, filesList,docType);
					try {
						String urls = "";
						for (CovidReportModel covidReportModel : covidReportModelList) {
							CovidReportEntity covidReportEntity = new CovidReportEntity();
							urls = urls + "," + covidReportModel.getFileUrl();
							covidReportEntity.setProposalNumber(proposalNumber);
							covidReportEntity.setFileName(covidReportModel.getFileName());
							covidReportEntity.setFileUrl(covidReportModel.getFileUrl());
							covidReportEntity.setFileFolderPath(covidReportModel.getFileFolderPath());
							covidReportEntity.setFileType(fileType);
							covidReportDAO.save(covidReportEntity);
						}
						customerDetailsEntity.setModifiedOn(DateUtil.toCurrentUTCTimeStamp());
						customerDetailsDAO.saveOrUpdate(customerDetailsEntity);
						response.setData(covidReportModelList);
						response.setStatus(Constant.SUCCESS);
						copyAllCovidReport(proposalNumber);
						for(File tmpFile:filesList) {
							if (tmpFile != null) {
								tmpFile.delete();
							}
						}
						
						return response;

					} catch (Exception e) {
						e.printStackTrace();
						logger.info("============== Getting error during saving covid report =============");

					}
					for(File tmpFile:filesList) {
						if (tmpFile != null) {
							tmpFile.delete();
						}
					}

				} else {
					response.setMessage("Malicious File Detected : Cannot upload file ");
					response.setStatus(Constant.FAILURE);
				}
			}else {
				response.setMessage("Malicious File Detected : Cannot upload file ");
				response.setStatus(Constant.FAILURE);
			}
			
			
		} catch (Exception e) {
			return null;
		}
		return response;
	}

	/**
	 * service method to copy all covid file from source to given destination path
	 * @param proposalNumber
	 * @return
	 * @author rajkumar
	 */
	public boolean copyAllCovidReport(String proposalNumber) {
		logger.info("----Enter into COVID REPORT copy folder service implementation-----");
		try {
			if (!StringUtils.isEmpty(proposalNumber)) {
				List<CovidReportEntity> covidReportEntity = covidReportDAO.findAllReport(proposalNumber);
				if(!CollectionUtils.isEmpty(covidReportEntity)) {
					String sourcePath = covidReportEntity.get(0).getFileFolderPath();
					StringBuilder destPath = new StringBuilder();
					destPath.append(Constant.VERIFIED+"_");
					destPath.append(DateUtil.extractDateAsStringDashFormate(DateUtil.toCurrentUTCTimeStamp())+"/");
					awsFileUtility.copyFolder(sourcePath, destPath.toString());
				}
				return true;
			}
		} catch (Exception e) {
			logger.error("---------Execption occurred while copy folder--------");
			e.printStackTrace();
		}
		return false;
	}


}