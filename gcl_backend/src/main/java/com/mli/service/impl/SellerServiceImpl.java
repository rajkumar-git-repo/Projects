package com.mli.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.log4j.Logger;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.mli.constants.Constant;
import com.mli.constants.DocType;
import com.mli.constants.MLIMessageConstants;
import com.mli.dao.CamReportDetailsDao;
import com.mli.dao.CreditCardCustomerDAO;
import com.mli.dao.CustomerDetailsDAO;
import com.mli.dao.LoanTypeSellerDao;
import com.mli.dao.MasterLoanTypeBankDAO;
import com.mli.dao.OtpDDAO;
import com.mli.dao.SellerDAO;
import com.mli.dao.UserDAO;
import com.mli.entity.CamReportDetailsEntity;
import com.mli.entity.CreditCardCustomerEntity;
import com.mli.entity.CustomerDetailsEntity;
import com.mli.entity.LoanTypeSellerEntity;
import com.mli.entity.MasterLoanTypeBankEntity;
import com.mli.entity.OTPHistoryEntity;
import com.mli.entity.SellerDetailEntity;
import com.mli.entity.UserEntity;
import com.mli.enums.CIRiderOption;
import com.mli.enums.LoanType;
import com.mli.enums.LoginType;
import com.mli.enums.MasterPolicyHolderName;
import com.mli.enums.OTPType;
import com.mli.enums.OTPUserType;
import com.mli.enums.RiderType;
import com.mli.enums.SchemeType;
import com.mli.enums.Status;
import com.mli.enums.UserType;
import com.mli.exception.StorageException;
import com.mli.filemaster.SchemeDirDao;
import com.mli.filemaster.SchemeDirEntity;
import com.mli.helper.CustomerDetailsHelper;
import com.mli.helper.SellerHelper;
import com.mli.modal.email.EmailModel;
import com.mli.modal.email.MliEmailServiceModel;
import com.mli.model.CustomerDetailsModel;
import com.mli.model.CustomerPhysicalFormModel;
import com.mli.model.LoginModel;
import com.mli.model.PageInfoModel;
import com.mli.model.PremiumCalculator;
import com.mli.model.SellerBankModel;
import com.mli.model.SellerDetailModel;
import com.mli.model.UserLoginModel;
import com.mli.model.otp.MLIGenerateOTPResponseModel;
import com.mli.model.otp.MLIValidateResponseModel;
import com.mli.model.response.ResponseModel;
import com.mli.model.response.SaveDetailsResponse;
import com.mli.model.response.SaveUpdateResponse;
import com.mli.model.sms.SMSResponseModel;
import com.mli.security.JwtTokenUtil;
import com.mli.service.CreditCardJourneyService;
import com.mli.service.MliEmailService;
import com.mli.service.SSOManagementService;
import com.mli.service.SellerService;
import com.mli.service.SendMLISMSService;
import com.mli.utils.DateUtil;
import com.mli.utils.MliApiUtils;
import com.mli.utils.ObjectsUtil;
import com.mli.utils.PageableUtil;
import com.mli.utils.ZipUtils;
import com.mli.utils.aws.AwsFileUtility;
import com.mli.utils.excel.ExcelGeneratorUtility;

/**
 * @author Nikhilesh.Tiwari
 *
 */
@CrossOrigin
@Service
public class SellerServiceImpl implements SellerService {

	private static final Logger logger = Logger.getLogger(SellerServiceImpl.class);

	@Autowired
	private SellerDAO sellerDAO;

	@Autowired
	private OtpDDAO otpDAO;

	@Autowired
	SellerHelper sellerHelper;

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private SendMLISMSService sendMLISMSService;

	@Autowired
	private CustomerDetailsDAO customerDetailsDAO;

	@Autowired
	private MliApiUtils mliApiUtilService;

	@Value("${mli.noofseller.records}")
	private Integer noOfRecordPerpage;

	@Autowired
	private CustomerDetailsHelper customerHelper;

	@Autowired
	private CustomerDetailServiceImpl customerDetailsImpl;

	@Autowired
	private MliEmailService mliEmailService;

	@Autowired
	private SchemeDirDao schemeDirDao;

	@Autowired
	private CustomerDetailServiceImpl customerDetailServiceImpl;

	@Autowired
	private AwsFileUtility awsFileUtility;

	@Value(value = "${symbol.forwardslash}")
	private String symbolForwardslash;

	@Autowired
	private ExcelGeneratorUtility excelGeneratorUtility;

	@Value("${mli.download.seller.excel}")
	private String sellerExcelPath;

	@Autowired
	private TaskExecutor taskExecutor;

	private CustomerDetailsEntity customerDetailsEntity;

	private String modifyTS;
	
	@Autowired
	private CamReportDetailsDao camReportDetailsDao;
	
	@Autowired
	private MasterLoanTypeBankDAO masterLoanTypeBankDAO;
	
	@Autowired
	private LoanTypeSellerDao loanTypeSellerDao;
	
	@Autowired
	private CreditCardCustomerDAO creditCardCustomerDAO;
	
	@Autowired
	private SSOManagementService  ssoManagementService;

	/**
	 * Seller login with contact number Successful login if contact number will
	 * exist in database
	 */
	@Override
	@Transactional
	public ResponseModel<UserLoginModel> sellerLogin(Long contNo) throws Exception {
		try {
			ResponseModel<UserLoginModel> responseModel = new ResponseModel<UserLoginModel>();
			SellerDetailEntity sellerDtl = sellerDAO.findByContactNo(contNo);
			if (!ObjectsUtil.isNull(sellerDtl)) {
				if (sellerDtl.getIsPasswordSet()) {
					responseModel.setLoginType(LoginType.PASSWORD_AND_OTP.getLabel());
				} else {
					responseModel.setLoginType(LoginType.OTP.getLabel());
				}
				responseModel.setMessage(MLIMessageConstants.SELLER_EXIT);
				responseModel.setStatus(SaveDetailsResponse.SUCCESS);
				return responseModel;
			} else {
				responseModel.setStatus(SaveDetailsResponse.FAILURE);
				responseModel.setMessage(MLIMessageConstants.SELLER_NOT_EXIST);
				return responseModel;
			}
		} catch (Exception e) {
			logger.info("::::::::::::::::: Error in Login Verification :::::::::::::::" + e);
			throw new Exception(e.getMessage());
		}

	}

	/**
	 * Generate 4 digit OTP with the help of MLI generate OTP API , for seller &
	 * customer
	 */

	@Override
	@Transactional
	public ResponseModel<UserLoginModel> generateOTP(Long contNo, String userType, Boolean regenerateOtp,
			String proposalNumber, String triggerScreen) throws Exception {
		try {
			CustomerDetailsEntity custDetails = null;
			ResponseModel<UserLoginModel> responseModel = new ResponseModel<UserLoginModel>();
			if ("customer-screen".equalsIgnoreCase(triggerScreen)) {
				custDetails = customerDetailsDAO.findByProposalNumber(proposalNumber);
				if (!custDetails.getStatus().equals(Status.APP_SENT)) {
					responseModel.setMessage(MLIMessageConstants.OTP_CANNOT_BE_TRIGGER);
					responseModel.setStatus(Constant.ABORTED);
					return responseModel;
				}
			}
			CreditCardCustomerEntity creditCardCustomerEntity = null;
			if ("yblcc-customer-screen".equalsIgnoreCase(triggerScreen)) {
				creditCardCustomerEntity = creditCardCustomerDAO.findByProposalNumber(proposalNumber);
				if (!creditCardCustomerEntity.getAppStatus().equals(Status.APP_SENT)) {
					responseModel.setMessage(MLIMessageConstants.OTP_CANNOT_BE_TRIGGER);
					responseModel.setStatus(Constant.ABORTED);
					return responseModel;
				}
			}

			OTPHistoryEntity otpDetails = otpDAO.findByContNo(contNo);
			MLIGenerateOTPResponseModel mliotpResponseModel = new MLIGenerateOTPResponseModel();
			SMSResponseModel smsResponseModel = new SMSResponseModel();
			boolean isValidUserType = false;

			if (userType.equals(OTPUserType.SELLER.getLabel())) {
				responseModel = sellerLogin(contNo);
				if (!ObjectsUtil.isNull(responseModel)
						&& SaveUpdateResponse.FAILURE.equalsIgnoreCase(responseModel.getStatus())) {
					responseModel.setMessage(MLIMessageConstants.INVALID_USERNAME_PASSWORD);
					responseModel.setStatus(SaveDetailsResponse.FAILURE);
					return responseModel;
				} else if (!ObjectsUtil.isNull(responseModel)
						&& SaveUpdateResponse.SUCCESS.equalsIgnoreCase(responseModel.getStatus())) {
					isValidUserType = true;
				}
			} else if (userType.equals(OTPUserType.CUSTOMER.getLabel())) {
				if (!ObjectsUtil.isNull(proposalNumber)) {
					custDetails = customerDetailsDAO.findByProposalNumber(proposalNumber);
					if (ObjectsUtil.isNull(custDetails)) {
						responseModel.setMessage(MLIMessageConstants.INVALID_PROPOSAL_NUMBER);
						responseModel.setStatus(SaveDetailsResponse.FAILURE);
						return responseModel;
					}
				} else {
					responseModel.setMessage(MLIMessageConstants.PROPOSAL_NOT_FOUND);
					responseModel.setStatus(SaveDetailsResponse.FAILURE);
					return responseModel;
				}
				isValidUserType = true;
			}else if (userType.equals(OTPUserType.YBLCCCUSTOMER.getLabel())) {
				if (!ObjectsUtil.isNull(proposalNumber)) {
					creditCardCustomerEntity = creditCardCustomerDAO.findByProposalNumber(proposalNumber);
					if (ObjectsUtil.isNull(creditCardCustomerEntity)) {
						responseModel.setMessage(MLIMessageConstants.INVALID_PROPOSAL_NUMBER);
						responseModel.setStatus(SaveDetailsResponse.FAILURE);
						return responseModel;
					}
				} else {
					responseModel.setMessage(MLIMessageConstants.PROPOSAL_NOT_FOUND);
					responseModel.setStatus(SaveDetailsResponse.FAILURE);
					return responseModel;
				}
				isValidUserType = true;
			}
			if (isValidUserType) {
				mliotpResponseModel = mliApiUtilService.callOTPGenerationAPI();
				Integer otp = Integer.parseInt(!ObjectsUtil
						.isNull(mliotpResponseModel.getResponse().getResponseData().getPayload().getOtpCode())
								? mliotpResponseModel.getResponse().getResponseData().getPayload().getOtpCode()
								: null);
				// send otp at mail when otp is trigger from customer screen.
				logger.info("Trigger screen : " + triggerScreen);
				if (custDetails != null && custDetails.getCustEmailId() != null
						&& !custDetails.getCustEmailId().trim().isEmpty()
						&& "customer-screen".equalsIgnoreCase(triggerScreen)) {
					sendOtpInEmail(otp, custDetails);
				}
				if (!ObjectUtils.isEmpty(creditCardCustomerEntity) && !StringUtils.isEmpty(creditCardCustomerEntity.getEmail().trim())
						&& "yblcc-customer-screen".equalsIgnoreCase(triggerScreen)) {
					sendYBLCCOtpInEmail(otp, creditCardCustomerEntity);
				}

				/*
				 * if (regenerateOtp) { OTPHistoryEntity OldOtpDetails = new OTPHistoryEntity();
				 * OldOtpDetails = otpDAO.findByContNo(contNo);
				 * OldOtpDetails.setOtpStatus(Status.NOT_USED_OTP);
				 * OldOtpDetails.setModifiedBy(contNo.toString());
				 * OldOtpDetails.setModifiedOn(DateUtil.toCurrentUTCTimeStamp());
				 * otpDAO.saveOrUpdate(OldOtpDetails); }
				 */
				if (otpDetails != null) {
					otpDetails.setOtpStatus(Status.CURRENT_OTP);
					otpDetails.setOtp(otp);
					otpDetails.setModifiedOn(DateUtil.toCurrentUTCTimeStamp());
					otpDetails.setModifiedBy(contNo.toString());
					otpDetails.setUniqueToken(
							mliotpResponseModel.getResponse().getResponseData().getPayload().getUnqTokenNo());
					otpDAO.saveOrUpdate(otpDetails);
				} else {
					otpDetails = new OTPHistoryEntity();
					otpDetails.setContNo(contNo);
					otpDetails.setOtpStatus(Status.CURRENT_OTP);
					otpDetails.setOtp(otp);
					otpDetails.setCreatedBy(contNo.toString());
					otpDetails.setOtpUserType(OTPUserType.getUserType(userType));
					otpDetails.setCreatedOn(DateUtil.toCurrentUTCTimeStamp());
					otpDetails.setModifiedOn(DateUtil.toCurrentUTCTimeStamp());
					otpDetails.setUniqueToken(
							mliotpResponseModel.getResponse().getResponseData().getPayload().getUnqTokenNo());
					otpDAO.saveOrUpdate(otpDetails);
				}

				if (OTPUserType.SELLER.getLabel().equalsIgnoreCase(userType)) {
					SellerDetailEntity sellerDtl = sellerDAO.findByContactNo(contNo);
					if(!ObjectUtils.isEmpty(sellerDtl) && !StringUtils.isEmpty(sellerDtl.getSellerEmailId())) {
						sendLoginOtpInEmail(otp, sellerDtl);
					}
					smsResponseModel = sendMLISMSService.sendMLISMS(otp, contNo, OTPType.SELLER_OTP_VERIFY_SMS, custDetails);
				    
				} else if(OTPUserType.CUSTOMER.getLabel().equalsIgnoreCase(userType)){
					smsResponseModel = sendMLISMSService.sendMLISMS(otp, contNo, OTPType.CUST_OTP_VERIFY_SMS, custDetails);
				} else if(OTPUserType.YBLCCCUSTOMER.getLabel().equalsIgnoreCase(userType)) {
				    smsResponseModel = sendMLISMSService.sendSmsToYBLCCCustomer(otp, contNo, OTPType.YBLCC_OTP_SENT, creditCardCustomerEntity);
				}

				if (!ObjectsUtil.isNull(smsResponseModel) && smsResponseModel.getMliSmsServiceResponse()
						.getResponseHeader().getGeneralResponse().getStatus().equalsIgnoreCase("ok")) {
					responseModel.setStatus(SaveDetailsResponse.SUCCESS);
					if (OTPUserType.SELLER.getLabel().equalsIgnoreCase(userType)) {
						responseModel.setMessage(MLIMessageConstants.SELLER_OTP_MESSAGE);
					} else {
						responseModel.setMessage(MLIMessageConstants.CUST_OTP_MESSAGE);
					}
				} else {
					responseModel.setStatus(SaveDetailsResponse.FAILURE);
					responseModel.setMessage(!ObjectsUtil.isNotNull(smsResponseModel.getMliSmsServiceResponse()
							.getResponseHeader().getGeneralResponse().getDescription())
									? smsResponseModel.getMliSmsServiceResponse().getResponseHeader()
											.getGeneralResponse().getDescription()
									: null);
				}
			} else {
				responseModel.setMessage(MLIMessageConstants.INVALID_USER_TYPE);
				responseModel.setStatus(SaveDetailsResponse.FAILURE);
			}
			return responseModel;
		} catch (Exception e) {
			logger.info("::::::::::::::::: Error in Generate OTP :::::::::::::::" + e);
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}

	/**
	 * service method to find full name
	 * @param customerDetails
	 * @return
	 * @author rajkumar
	 */
	private String getCustFullName(CustomerDetailsEntity customerDetails) {
		StringBuilder fullName = new StringBuilder();
		if (customerDetails.getCustomerFirstName() != null) {
			fullName.append(customerDetails.getCustomerFirstName());
		}
		if (customerDetails.getCustomerLastName() != null) {
			fullName.append(" ");
			fullName.append(customerDetails.getCustomerLastName());
		}
		return fullName.toString();
	}
	
	/**
	 * get ybl credit card customer fullname
	 * @param creditCardCustomerEntity
	 * @return
	 * @author rajkumar
	 */
	private String getYBLCCCustFullName(CreditCardCustomerEntity creditCardCustomerEntity) {
		StringBuilder fullName = new StringBuilder();
		if (!StringUtils.isEmpty(creditCardCustomerEntity.getFirstName())) {
			fullName.append(creditCardCustomerEntity.getFirstName());
		}
		if (!StringUtils.isEmpty(creditCardCustomerEntity.getLastName())) {
			fullName.append(" ").append(creditCardCustomerEntity.getLastName());
		}
		return fullName.toString();
	}

	/**
	 * send otp to customer email
	 * @param otp
	 * @param customerDetails
	 * @author rajkumar
	 */
	private void sendOtpInEmail(Integer otp, CustomerDetailsEntity customerDetails) {
		EmailModel mliEmailModel = new EmailModel();
		mliEmailModel.setMailUserType(Constant.OTP_AT_EMAIL);
		mliEmailModel.setOtp(otp);
		mliEmailModel.setCustomerName(getCustFullName(customerDetails));
		mliEmailModel.setMailIdTo(customerDetails.getCustEmailId());
		mliEmailModel.setLoanAppNumber(customerDetails.getLoanAppNumber());
		ResponseModel<MliEmailServiceModel> emailResponse = null;
		try {
			emailResponse = mliEmailService.sendEmail(mliEmailModel);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!("200".equalsIgnoreCase(emailResponse.getCode()) && ("Ok").equalsIgnoreCase(emailResponse.getStatus()))) {
			logger.error("::::::::::::::::: Mail Not Send to customer ::::::::::::::: at time : " + (new Date()));

		} else {
			logger.info("::::::::::::::::: Mail Send to customer ::::::::::::::: at time : " + (new Date()));
		}
	}

	/**
	 * Validate 4 digit OTP with the help of MLI validate OTP API , for seller &
	 * customer
	 */

	@Override
	@Transactional
	public ResponseModel<UserLoginModel> verifyLoginOPT(Long contNo, Integer otp, String userType,
			String proposalNumber, String triggerScreen) throws Exception {
		try {
			OTPHistoryEntity otpDetails = new OTPHistoryEntity();
			ResponseModel<UserLoginModel> responseModel = new ResponseModel<UserLoginModel>();
			MLIValidateResponseModel mliValidateResponse = new MLIValidateResponseModel();
			UserLoginModel userLoginModel = new UserLoginModel();

			UserEntity userEntity = userDAO.findByUserName(String.valueOf(contNo));
			boolean isValidUserType = false;

			if (OTPUserType.SELLER.getLabel().equalsIgnoreCase(userType)) {
				responseModel = sellerLogin(contNo);
				if (!ObjectsUtil.isNull(responseModel)
						&& SaveUpdateResponse.FAILURE.equalsIgnoreCase(responseModel.getStatus())) {
					responseModel.setMessage(MLIMessageConstants.INVALID_USERNAME_PASSWORD);
					return responseModel;
				} else if (!ObjectsUtil.isNull(responseModel)
						&& SaveUpdateResponse.SUCCESS.equalsIgnoreCase(responseModel.getStatus())) {
					isValidUserType = true;
				}
			} else if (OTPUserType.CUSTOMER.getLabel().equalsIgnoreCase(userType)) {
				isValidUserType = true;
			} else if (OTPUserType.YBLCCCUSTOMER.getLabel().equalsIgnoreCase(userType)) {
				isValidUserType = true;
			}
			if (isValidUserType) {
				if(OTPUserType.SELLER.getLabel().equalsIgnoreCase(userType) && !ObjectUtils.isEmpty(userEntity) && userEntity.getLockTimestamp() != null && DateUtil.getDiffHourMinSecond(userEntity.getLockTimestamp()).get("hour") >=1) {
					userEntity.setLockTimestamp(null);
					userEntity.setLocked(false);
				    userEntity.setLockCount(0);
				    userEntity.setModifiedOn(DateUtil.toCurrentUTCTimeStamp());
				    userDAO.saveOrUpdate(userEntity);
				    logger.info("ACCOUNT IS UNLOCKED::"+contNo);
				}
				if (OTPUserType.SELLER.getLabel().equalsIgnoreCase(userType) && !ObjectUtils.isEmpty(userEntity) && userEntity.isLocked()) {
					logger.info("ACCOUNT IS LOCK MESSAGE::"+contNo);
					HashMap<String, Long> map = new HashMap<String, Long>();
					if(!ObjectUtils.isEmpty(userEntity)) {
						map = DateUtil.getDiffHourMinSecond(userEntity.getLockTimestamp());
					}
					responseModel.setMessage("Your account is locked : Try after "+(new Long(59)-map.get("minute"))+" min."+(new Long(59)-map.get("second"))+" sec.");
					responseModel.setStatus(SaveUpdateResponse.FAILURE);
					return responseModel;
				}
				otpDetails = otpDAO.findByContNoAndOtp(contNo, otp);
				if (otpDetails != null) {
					logger.info("otpDetails.getOtp() : " + otpDetails.getOtp());
				} else {
					logger.info("CURRENT_OTP not found for given contact number : " + contNo);
				}
				if (!ObjectsUtil.isNull(otpDetails)) {
					mliValidateResponse = mliApiUtilService.callOTPVerificationAPI(otpDetails);
					logger.info("Contact Number (Verification): " + contNo);
					if (!ObjectsUtil.isNull(mliValidateResponse)
							&& !ObjectsUtil.isNull(mliValidateResponse.getResponse())) {
						logger.info("SoaMessage (Verification): "
								+ mliValidateResponse.getResponse().getResponseData().getSoaMessage());
						logger.info("SoaStatusCode (Verification): "
								+ mliValidateResponse.getResponse().getResponseData().getSoaStatusCode());
					}

					if (!ObjectsUtil.isNull(mliValidateResponse)
							&& !ObjectsUtil.isNull(mliValidateResponse.getResponse())
							&& mliValidateResponse.getResponse().getResponseData().getSoaMessage().equals("Success")
							&& mliValidateResponse.getResponse().getResponseData().getSoaStatusCode().equals("200")) {

						otpDetails.setOtpStatus(Status.USED_OTP);
						otpDetails.setModifiedBy(contNo.toString());
						otpDetails.setModifiedOn(DateUtil.toCurrentUTCTimeStamp());
						otpDAO.saveOrUpdate(otpDetails);

						// otp is verified , changed status to verified
						responseModel = customerStatusChange(proposalNumber, Status.APP_VERIFIED, triggerScreen,
								otpDetails.getOtp());
						if (responseModel != null && responseModel.getStatus() != null
								&& responseModel.getStatus().equals(Constant.FAILURE)) {
							return responseModel;
						}

						if (OTPUserType.SELLER.getLabel().equalsIgnoreCase(userType)
								&& !ObjectsUtil.isNull(mliValidateResponse.getResponse().getResponseData()
										.getSoaStatusCode().equals("200"))
								&& mliValidateResponse.getResponse().getResponseData().getSoaMessage()
										.equalsIgnoreCase("Success")) {
							responseModel.setMessage(MLIMessageConstants.LOGIN_SUCCESS);
							// setting JWT TOKEN
							LoginModel loginModel = new LoginModel();
							loginModel.setUsername(contNo.toString());
							userLoginModel.setTokenDetails(jwtTokenUtil.getTokenModelWhenLoginWithOTP(loginModel));
							responseModel.setData(userLoginModel);
						} else {
							responseModel.setMessage(MLIMessageConstants.OTP_AUTHENTHICATE);
						}
						if (OTPUserType.SELLER.getLabel().equalsIgnoreCase(userType)) {
							logger.info("ACCOUNT IS UNLOCKED::"+contNo);
							userEntity.setLockTimestamp(null);
							userEntity.setLocked(false);
							userEntity.setLockCount(0);
							userEntity.setModifiedOn(DateUtil.toCurrentUTCTimeStamp());
							userDAO.saveOrUpdate(userEntity);
						}
						responseModel.setStatus(SaveDetailsResponse.SUCCESS);
						return responseModel;
					} else {
						if (mliValidateResponse.getResponse().getResponseData().getSoaStatusCode().equals("500")) {
							if (OTPUserType.SELLER.getLabel().equalsIgnoreCase(userType)) {
								responseModel.setMessage(MLIMessageConstants.SELLER_INVALID_OTP);
							} else {
								responseModel.setMessage(MLIMessageConstants.CUST_INVALID_OTP);
							}
						}
						responseModel.setStatus(SaveDetailsResponse.FAILURE);
						return responseModel;
					}
				} else {
					String msg = "";
					try {
						if (!ObjectUtils.isEmpty(userEntity) && OTPUserType.SELLER.getLabel().equalsIgnoreCase(userType)) {
							if (userEntity.getLockCount() == 4) {
								userEntity.setLockTimestamp(DateUtil.toCurrentUTCTimeStamp());
								userEntity.setLocked(true);
								logger.info("ACCOUNT IS LOCKED::"+contNo);
							}
							userEntity.setLockCount(userEntity.getLockCount() + 1);
							userEntity.setModifiedOn(DateUtil.toCurrentUTCTimeStamp());
							userDAO.saveOrUpdate(userEntity);
							if(userEntity.isLocked()) {
								msg = "Your account is locked for 60 minutes";
							}else {
								msg = "You have "+(5-userEntity.getLockCount())+" attempts before your account is locked for 60 minutes";
							}
						}
					}catch (Exception exp) {
						logger.error("::BAD CREDENTIAL FOR USER::" + contNo + "::::::" + exp);
					} 
					if (OTPUserType.SELLER.getLabel().equalsIgnoreCase(userType)) {
						responseModel.setMessage("Invalid OTP. "+msg);
					} else {
						responseModel.setMessage(MLIMessageConstants.CUST_INVALID_OTP);
					}
					responseModel.setStatus(SaveDetailsResponse.FAILURE);
					return responseModel;
				}
			} else {
				responseModel.setMessage(MLIMessageConstants.INVALID_USER_TYPE);
				responseModel.setStatus(SaveDetailsResponse.FAILURE);
				return responseModel;
			}
		} catch (Exception e) {
			logger.error("::::::::::::::::: Error in OTP Varification :::::::::::::::" + e);
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}

	}

	/**
	 * After OTP verification 1) Generate PDF 2) Copy passport into verified folder
	 * 3) Copy PDF into verified folder 4) Status update 5) Send mail to customer
	 * with PDF attachement 6) Trigger SMS to customer 7) Trigger SMS to Seller
	 * 
	 * @param proposalNo
	 * @param status
	 * @param triggerScreen
	 */
	private ResponseModel<UserLoginModel> customerStatusChange(String proposalNo, Status status, String triggerScreen,
			Integer otp) {
		ResponseModel<UserLoginModel> responseModel = new ResponseModel<UserLoginModel>();
		try {
			if ("customer-screen".equalsIgnoreCase(triggerScreen)) {
				CustomerDetailsEntity customerDetailsEntity = customerDetailsDAO.findByProposalNumber(proposalNo);
				if (customerDetailsEntity != null) {
					customerDetailsEntity.setModifiedOn(DateUtil.toCurrentUTCTimeStamp());
					customerDetailsEntity.setAppCompletionDate(DateUtil.toCurrentUTCTimeStamp());
					customerDetailsEntity.setVerifiedOtp(otp + "");

					// Generate PDF
					try {
						modifyTS = DateUtil
								.extractDateWithTSAsAM_PMStringSlashFormate(DateUtil.toCurrentUTCTimeStamp());
						customerDetailServiceImpl.generateAndEmailPDF(customerDetailsEntity, null, otp + "", modifyTS);
					} catch (Exception exception) {
						exception.printStackTrace();
						responseModel.setStatus(Constant.FAILURE);
						responseModel.setMessage(MLIMessageConstants.PDF_GENRATION);
						return responseModel;
					}

					// Copy passport and PDF to verified folder
					List<SchemeDirEntity> schemeDr = schemeDirDao.findByProposalNumber(proposalNo);
					if (schemeDr != null && !schemeDr.isEmpty()) {
						try {
							for (SchemeDirEntity temp : schemeDr) {
								StringBuilder copiedPath = new StringBuilder();
								if (DocType.PASSPORT.equals(temp.getDocType())) {
									copiedPath.append(
											ZipUtils.getDirNameOfVerified(customerDetailsEntity.getAppCompletionDate(),
													Constant.AXIS_BANK_LTD.equalsIgnoreCase(customerDetailsEntity.getMasterPolicyHolderName()) ? DocType.PASSPORT : DocType.AFL_PASSPORT, customerDetailsEntity.getProposalNumber() + "_"
															+ customerDetailsEntity.getLoanAppNumber()));
									awsFileUtility.copyObject(temp.getAwsFilePath(), copiedPath.toString());
								} else if (DocType.PROPOSAL.equals(temp.getDocType())) {
									copiedPath.append(
											ZipUtils.getDirNameOfVerified(customerDetailsEntity.getAppCompletionDate(),
													Constant.AXIS_BANK_LTD.equalsIgnoreCase(customerDetailsEntity.getMasterPolicyHolderName()) ?DocType.PROPOSAL : DocType.AFL_PROPOSAL, customerDetailsEntity.getProposalNumber() + "_"
															+ customerDetailsEntity.getLoanAppNumber()));
									awsFileUtility.copyObject(temp.getAwsFilePath(), copiedPath.toString());
								} 
								
								
								logger.info("copiedPath : " + copiedPath + " ,Proposal number : "
										+ customerDetailsEntity.getProposalNumber() + " ,DocType : " + temp.getDocType()
										+ " ,Current date : " + new Date());
							}
						} catch (Exception exception) {
							exception.printStackTrace();
							responseModel.setStatus(Constant.FAILURE);
							responseModel.setMessage(MLIMessageConstants.COPY_DOCUMENT);
							return responseModel;
						}
					}
					
					// copy Additonal document in the verified folder 
					List<CamReportDetailsEntity> camReportList = camReportDetailsDao.getProposals(proposalNo);
					if (camReportList != null && !camReportList.isEmpty()) {
						StringBuilder copiedPath = new StringBuilder();
						//String awsFullPath="https://maxlifedev.s3.ap-south-1.amazonaws.com/";
						String awsPath =camReportList.get(0).getCamFolderPath();
						copiedPath.append(ZipUtils.getDirNameOfVerified(customerDetailsEntity.getAppCompletionDate(),
							DocType.CAM_REPORT));
						awsFileUtility.copyFolder(awsPath, copiedPath.toString());

					}
					

					// Status update
					customerDetailsEntity.setStatus(status);
					customerDetailsDAO.save(customerDetailsEntity);
					this.customerDetailsEntity = customerDetailsEntity;
					responseModel.setStatus(Constant.SUCCESS);

					// create new thread for send email and SMS
					if (customerDetailsEntity.getCustEmailId() != null
							&& !customerDetailsEntity.getCustEmailId().trim().isEmpty()) {
						sendPDFNdEmailAsynchronously(otp);
					}
					sendSMSAsynchronously();
					return responseModel;

					/*
					 * // Generate PDF and send mail to customer try {
					 * customerDetailServiceImpl.generateAndEmailPDF(customerDetailsEntity,
					 * Constant.FINAL_STEP); } catch (Exception exception) {
					 * exception.printStackTrace(); responseModel.setStatus(Constant.FAILURE);
					 * responseModel.setMessage(MLIMessageConstants.PDF_GENRATION_AND_SEND_EMAIL);
					 * return responseModel; }
					 * 
					 * // trigger SMS to customer and seller try {
					 * triggerSMS(customerDetailsEntity.getCustMobileNo(), customerDetailsEntity,
					 * OTPType.CUST_APP_SUBMIT);
					 * triggerSMS(customerDetailsEntity.getSlrDtlId().getContactNo(),
					 * customerDetailsEntity, OTPType.SELLER_APP_SUBMIT); } catch (Exception
					 * exception) { exception.printStackTrace();
					 * responseModel.setStatus(Constant.FAILURE);
					 * responseModel.setMessage(MLIMessageConstants.SMS_NOT_SENT); return
					 * responseModel; }
					 */
				} else {
					responseModel.setStatus(Constant.FAILURE);
					responseModel.setMessage(MLIMessageConstants.CUSTOMER_NOT_FOUND);
					return responseModel;
				}
			}
			
			/* ******************Credit card customer vrification Start*************** */
			if("yblcc-customer-screen".equalsIgnoreCase(triggerScreen)) {
				CreditCardCustomerEntity creditCardCustomerEntity = creditCardCustomerDAO.findByProposalNumber(proposalNo);
				if (!ObjectUtils.isEmpty(creditCardCustomerEntity)) {
					creditCardCustomerEntity.setModifiedOn(DateUtil.toCurrentUTCTimeStamp());
					creditCardCustomerEntity.setAppCompletionDate(DateUtil.toCurrentUTCTimeStamp());
					creditCardCustomerEntity.setVerifiedOtp(otp + "");
					// Status update
					creditCardCustomerEntity.setPaymentStatus(status.PAYMENT_PENDING);
					creditCardCustomerEntity.setAppStatus(status);
					creditCardCustomerDAO.saveOrUpdate(creditCardCustomerEntity);
					responseModel.setStatus(Constant.SUCCESS);
					return responseModel;
					
				} else {
					responseModel.setStatus(Constant.FAILURE);
					responseModel.setMessage(MLIMessageConstants.CUSTOMER_NOT_FOUND);
					return responseModel;
				}
			}
			/* ******************Credit card customer verification End*************** */
		} catch (Exception exception) {
			responseModel.setStatus(Constant.FAILURE);
			responseModel.setMessage(Constant.FAILURE_MSG);
			exception.printStackTrace();
			return responseModel;
		}
		return responseModel;
	}

	
	/**
	 * Generate PDF and send mail to customer
	 */
	public void sendPDFNdEmailAsynchronously(Integer otp) {
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					/*
					 * List<OTPHistoryEntity> otpDetails = otpDDAO
					 * .findByContNoAndStatus(customerDetailsEntity.getCustMobileNo(),
					 * Status.CURRENT_OTP); OTPHistoryEntity otpDetail = new OTPHistoryEntity(); //
					 * Get last object of otpDetails which is used to verify StringBuilder otp = new
					 * StringBuilder(); StringBuilder modifyTS = new StringBuilder(); if (otpDetails
					 * != null && !otpDetails.isEmpty()) { otpDetail =
					 * otpDetails.get(otpDetails.size() - 1); otp.append(otpDetail.getOtp()); if
					 * (otpDetail.getModifiedOn() != null) { //
					 * modifyTS.append(DateUtil.extractDateWithTSAsStringSlashFormate(otpDetail.
					 * getModifiedOn()));
					 * modifyTS.append(DateUtil.extractDateWithTSAsStringSlashFormate(DateUtil.
					 * toCurrentUTCTimeStamp())); } }
					 */
					customerDetailServiceImpl.generateAndEmailPDF(customerDetailsEntity, Constant.FINAL_STEP, otp + "",
							modifyTS);
					logger.info("PDF generate and Email sent successfully");
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}
		});
	}

	/**
	 * trigger SMS to customer and seller
	 */
	public void sendSMSAsynchronously() {
		taskExecutor.execute(new Runnable() {
			@Override
			public void run() {
				try {
					triggerSMS(customerDetailsEntity.getCustMobileNo(), customerDetailsEntity, OTPType.CUST_APP_SUBMIT);
					logger.info("SMS trigger (Customer) successfully : " + customerDetailsEntity.getCustMobileNo());
					triggerSMS(customerDetailsEntity.getSlrDtlId().getContactNo(), customerDetailsEntity,
							OTPType.SELLER_APP_SUBMIT);
					logger.info("SMS trigger (Seller) successfully : "
							+ customerDetailsEntity.getSlrDtlId().getContactNo());
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			}
		});
	}

	/**
	 * Trigger SMS
	 * 
	 */
	public void triggerSMS(Long mobileNumber, CustomerDetailsEntity custDetails, OTPType otpType) throws Exception {
		SMSResponseModel smsResponse = sendMLISMSService.sendMLISMS(null, mobileNumber, otpType, custDetails);
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
	 * Save or Update Seller Details
	 */

	@Override
	@Transactional
	public void saveOrUpdateSallerDetails(SellerDetailModel sellerDetailModel) throws Exception {
		try {
			// SellerDetailEntity sellerDetailEntity =
			// sellerDAO.findByContactNo(sellerDetailModel.getContactNo());
			LoanTypeSellerEntity  loanTypeSellerEntity=new LoanTypeSellerEntity();
			
			SellerDetailEntity sellerDetailEntity = null;
			Long currentTimeStamp = DateUtil.toCurrentUTCTimeStamp();
			if (sellerDetailModel.getSellerDtlId() != null) {
				sellerDetailEntity = sellerDAO.getEntity(SellerDetailEntity.class, sellerDetailModel.getSellerDtlId());
				if (sellerDetailEntity != null) {
					sellerDetailEntity = sellerHelper.convertToEntity(sellerDetailModel, sellerDetailEntity);
					sellerDetailEntity.setModifiedOn(currentTimeStamp);
				}
			} else {
				sellerDetailEntity = sellerHelper.convertToEntity(sellerDetailModel, sellerDetailEntity);
				sellerDetailEntity.setModifiedOn(currentTimeStamp);
				sellerDetailEntity.setCreatedOn(currentTimeStamp);
			}
			
			SellerBankModel sellerDetail = sellerDetailModel.getSellerBankDetails().iterator().next();
			/* this code snipet for yes bank only to store the information related to yes bank */
			if (Constant.YES.equalsIgnoreCase(sellerDetail.getBankName()) && !CollectionUtils.isEmpty(sellerDetailModel.getLoanTypes())) {
				List<MasterLoanTypeBankEntity> masterLoanTypeBankEntity = masterLoanTypeBankDAO.findByloanTypeIn(sellerDetailModel.getLoanTypes());
				if (!CollectionUtils.isEmpty(masterLoanTypeBankEntity)) {
					Set<LoanTypeSellerEntity> loanTypeList = new HashSet<LoanTypeSellerEntity>();
					List<LoanTypeSellerEntity> loanTypeSellerEntityList = loanTypeSellerDao.findBySellerId(sellerDetailModel.getSellerDtlId());
					if (!CollectionUtils.isEmpty(loanTypeSellerEntityList)) {
						List<String> masterLoanType = masterLoanTypeBankEntity.stream().map(e -> e.getLoanType()).collect(Collectors.toList());
						List<String> sellerLoanType = loanTypeSellerEntityList.stream().map(e -> e.getLoanType()).collect(Collectors.toList());
						if (masterLoanType.size() != sellerLoanType.size() || !masterLoanType.containsAll(sellerLoanType)) {
							for (LoanTypeSellerEntity loanTypeSeller : loanTypeSellerEntityList) {
								loanTypeSellerDao.delete(loanTypeSeller);
							}
							loanTypeSellerEntityList = null;
						}
					}
					if (CollectionUtils.isEmpty(loanTypeSellerEntityList)) {
						for (MasterLoanTypeBankEntity masterLoanObject : masterLoanTypeBankEntity) {
							loanTypeSellerEntity = new LoanTypeSellerEntity();
							loanTypeSellerEntity.setInterest(masterLoanObject.getInterest());
							loanTypeSellerEntity.setLoanType(masterLoanObject.getLoanType());
							loanTypeSellerEntity.setCiRider(masterLoanObject.getCiRider());
							loanTypeSellerEntity.setCiRiderType(masterLoanObject.getCiRiderType());
							loanTypeSellerEntity.setCoverType(masterLoanObject.getCoverType());
							loanTypeSellerEntity.setPercentage(masterLoanObject.getPercentage());
							loanTypeSellerEntity.setMph(masterLoanObject.getMph());
							loanTypeSellerEntity.setBalicPremimum(masterLoanObject.getBalicPremimum());
							loanTypeSellerEntity.setLevelRates(masterLoanObject.getLevelRates());
							loanTypeSellerEntity.setReducingCoverInterestRates(masterLoanObject.getReducingCoverInterestRates());
							loanTypeSellerEntity.setReducingMaxRates(masterLoanObject.getReducingMaxRates());
							loanTypeSellerEntity.setSellerDetailEntity(sellerDetailEntity);
							loanTypeList.add(loanTypeSellerEntity);
						}
					}
					sellerDetailEntity.setLoanTypeSellerEntity(loanTypeList);
				}
			}
			if(Constant.AXIS.equalsIgnoreCase(sellerDetail.getBankName())) {
				List<LoanTypeSellerEntity> loanTypeSellerEntityList = loanTypeSellerDao.findBySellerId(sellerDetailModel.getSellerDtlId());
                if(!CollectionUtils.isEmpty(loanTypeSellerEntityList)) {
                	for (LoanTypeSellerEntity loanTypeSeller : loanTypeSellerEntityList) {
						loanTypeSellerDao.delete(loanTypeSeller);
					}
                }
                sellerDetailEntity.setLoanTypeSellerEntity(null);
			}
	
			// If bankdo not present in model and it is present in DB we are making in
			// active
			sellerDetailEntity.setRole(Constant.YES.equalsIgnoreCase(sellerDetail.getBankName()) ? sellerDetailModel.getRole() : null);
			sellerDAO.saveOrUpdate(sellerDetailEntity);
			UserEntity userEntity = userDAO.findByUserName(sellerDetailEntity.getContactNo().toString());
			if (userEntity != null) {
				userEntity.setUserName(sellerDetailEntity.getContactNo().toString());
				userEntity.setModifiedOn(currentTimeStamp);
			} else {
				userEntity = new UserEntity();
				userEntity.setUserName(sellerDetailEntity.getContactNo().toString());
				userEntity.setUserType(UserType.ROLE_SELLER);
				userEntity.setStatus(true);
				userEntity.setCreatedOn(currentTimeStamp);
			}

			userDAO.saveOrUpdate(userEntity);
			sellerDetailEntity.setUserId(userEntity);
			sellerDAO.saveOrUpdate(sellerDetailEntity);
		} catch (Exception e) {
			logger.error("::::::::::::::::: Error in Seller Creation/Updatation :::::::::::::::" + e);
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}

	/**
	 * Find out seller details by Id
	 */
	@Override
	@Transactional
	public SellerDetailModel getSellerDetailsById(Long id) throws Exception {
		try {
			SellerDetailEntity sellerDetailEntity = sellerDAO.getEntity(SellerDetailEntity.class, id);
			return sellerHelper.convertToModel(sellerDetailEntity);
		} catch (Exception e) {
			logger.error("::::::::::::::::: Error in getSellerDetailsById() :::::::::::::::" + e);
			throw new StorageException(e.getMessage());
		}
	}

	/**
	 * get all seller details
	 * @return
	 * @throws Exception
	 * @author rajkumar
	 */
	@Override
	@Transactional
	public List<SellerDetailModel> getAllSellerDetails() throws Exception {
		try {
			List<SellerDetailEntity> sellerDetailEntities = sellerDAO.getAll();
			return (List<SellerDetailModel>) sellerHelper.convertToModelList(sellerDetailEntities);
		} catch (Exception e) {
			logger.error("::::::::::::::::: Error in getAllSellerDetails() :::::::::::::::");
			throw new Exception(e.getMessage());
		}
	}

	/**
	 * find seller detail and draft count for given contact number
	 * @param contNo
	 * @return
	 * @throws Exception
	 * @author rajkumar
	 */
	@Override
	@Transactional
	public ResponseModel<UserLoginModel> sellerDtlAndDraftCount(Long contNo) throws Exception {
		try {
			ResponseModel<UserLoginModel> responseModel = new ResponseModel<UserLoginModel>();
			UserLoginModel userLoginModel = new UserLoginModel();
			SellerDetailEntity sellerDetails = new SellerDetailEntity();
			sellerDetails = sellerDAO.findByContactNo(contNo);
			//responseModel.setSellerDetailModel(findByContantNumber(contNo));
			if (!ObjectsUtil.isNull(sellerDetails)) {	
				Integer draftCount = 0;
				userLoginModel.setSellerDetailModel(sellerHelper.convertToModel(sellerDetails));
				responseModel.setData(userLoginModel);
				if(Constant.YESBANKCC.equalsIgnoreCase((sellerDetails.getSellerBankEntity().iterator().next().getBankName().getLabel()))) {
					draftCount = creditCardCustomerDAO.findProposalsCountBySellerId(sellerDetails.getSellerDtlId());
				}else {
				    draftCount = customerDetailsDAO
						.findPendingProposalsCountBySellerId(sellerDetails.getSellerDtlId(), Status.APP_PENDING);
				}
				responseModel.setDraftCount(ObjectsUtil.isNotNull(draftCount) ? draftCount : null);
				responseModel.setMessage(MLIMessageConstants.SELLER_EXIT);
				responseModel.setStatus(SaveDetailsResponse.SUCCESS);
				return responseModel;
			} else {
				responseModel.setMessage(MLIMessageConstants.SELLER_NOT_EXIST);
				responseModel.setStatus(SaveDetailsResponse.FAILURE);
				return responseModel;
			}
		} catch (Exception e) {
			logger.error("::::::::::::::::: Error in Seller Getting draft data & seller details  :::::::::::::::");
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}

	}

	/**
	 * Find out all master policy holders details
	 */
	@Override
	public List<SellerBankModel> getAllMasterPolicyHolders() {

		List<SellerBankModel> sellerBankModels = new ArrayList<SellerBankModel>();
		for (MasterPolicyHolderName masterPolicyHolderName : MasterPolicyHolderName.masterPolicyHolders) {
		//	if (Constant.AXIS.equals(masterPolicyHolderName.getLabel())) 
			{
				SellerBankModel sellerBankModel = new SellerBankModel();
				sellerBankModel.setBankName(masterPolicyHolderName.getLabel());
				sellerBankModel.setBankNameDesc(masterPolicyHolderName.getDescription());
				sellerBankModels.add(sellerBankModel);
			}
		}

		return sellerBankModels;
	}

	/**
	 * Find out seller exist or not in database
	 */

	@Transactional
	@Override
	public boolean checkSellerIfExist(Long contactNo) {
		SellerDetailEntity sellerDetails = sellerDAO.findByContactNo(contactNo);
		if (sellerDetails == null)
			return false;
		else
			return true;
	}

	/**
	 * check seller is exist or not for given cintact no and seller id
	 * @param contactNo
	 * @param sellerId
	 * @return
	 * @author rajkumar
	 */
	@Transactional
	@Override
	public boolean checkOtherSellerIfExistForMobileUpdate(Long contactNo, Long sellerId) {
		SellerDetailEntity sellerDetails = sellerDAO.checkOtherSellerIfExistForMobileUpdate(contactNo, sellerId);
		if (sellerDetails == null)
			return false;
		else
			return true;
	}

	/**
	 * fetch list of SellerDetailEntity map based on pageable object
	 * @param pageable
	 * @param pattern
	 * @return
	 * @throws Exception
	 * @author rajkumar
	 */
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public Map<String, Object> getAllSellerDetailsByPageNumber(Pageable pageable, Long pattern) throws Exception {
		try {
			Map<String, Object> countResultMap = sellerDAO.getSellerSearch(pageable, pattern);
			List<SellerDetailEntity> sellerEntity = (List<SellerDetailEntity>) countResultMap
					.get(Constant.QUERY_RESULT);
			Long totalCount = (Long) countResultMap.get(Constant.TOTAL_COUNT);
			List<SellerDetailModel> sellerDetailsModels = new ArrayList<>(sellerEntity.size());
			for (SellerDetailEntity tempSlrEntity : sellerEntity) {
				SellerDetailModel tempSlrDetailModel = sellerHelper.convertToModel(tempSlrEntity);
				tempSlrDetailModel.setSellerEmailId("");
//				tempSlrDetailModel.setContactNo(sellerHelper.getMaskedContact(tempSlrDetailModel.getContactNo() + ""));
				sellerDetailsModels.add(tempSlrDetailModel);
			}
			PageInfoModel pageInfoModel = PageableUtil.getPageMetaInfo(pageable, sellerDetailsModels.size(),
					totalCount);
			Map<String, Object> response = new HashMap<>(2);
			response.put(Constant.PAGE_META_INFO, pageInfoModel);
			response.put(Constant.RESULT, sellerDetailsModels);
			return response;
		} catch (Exception e) {
			logger.error("::::::::::::::::: Error in getAllSellerDetails() Per page record :::::::::::::::");
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}

	/**
	 * Seller is saved customer data by using Premium Calculator.
	 * 
	 * @author Devendra.Kumar
	 * @param premiumCalculator
	 */
	@Override
	@Transactional
	public Map<String, Object> saveByPremiumCalc(PremiumCalculator premiumCalculator) {
		Map<String, Object> resultMap = new HashMap<>(2);
		String response = "";
		CustomerDetailsEntity customer = null;
		try {
			customer = new CustomerDetailsEntity();
			customer.setBaseOrBaseWithCI("Base Benefit");
			customer.setTenureEligible(premiumCalculator.getTenureEligible());
			customer.setLoanAppNumber(premiumCalculator.getLoanAppNumber());
			customer.setSumAssured(premiumCalculator.getSumAssured());
			customer.setDob(DateUtil.dateFormater(premiumCalculator.getDob()));
			customer.setLoanTenure(premiumCalculator.getTenureYear());
			customer.setLoanAmount(premiumCalculator.getLoanAmount());
			customer.setInterestRate(premiumCalculator.getInterestRate());
			customer.setIsFinancePremium(premiumCalculator.getIsFinancePremium());
			customer.setTentativeEMI(premiumCalculator.getTentativeEMI());
			customer.setAge(premiumCalculator.getAge());
			customer.setTotalPremium(premiumCalculator.getTotalPremium());
			customer.setIncrementalEMI(premiumCalculator.getIncrementalEMI());
			customer.setIsMedicalUnderWritingRequired(premiumCalculator.getIsMedicalUnderWritingRequired());
			customer.setIsFinancialUnderWritingRequired(premiumCalculator.getIsFinancialUnderWritingRequired());
			customer.setIsInterested(premiumCalculator.getIsInterested());
			if (!premiumCalculator.getReseanForNotInterested().equalsIgnoreCase("NA")) {
				customer.setStatus(Status.APP_NOT_INTERESTED);
			} else {
				customer.setStatus(Status.APP_PENDING);
			}
			customer.setReseanForInterested(premiumCalculator.getReseanForNotInterested());
			customer.setLoanType(LoanType.getLoanType(premiumCalculator.getLoanType()));
			customer.setSchemeType(SchemeType.getSchemeType(premiumCalculator.getSchemeType()));
			customer.setAppStep(Status.STEP0);
			customer.setCreatedOn(DateUtil.toCurrentUTCTimeStamp());
			customer.setModifiedOn(DateUtil.toCurrentUTCTimeStamp());

			// currently do not use channel name which is coming from premium calculator
//			customer.setMasterPolicyHolderName(premiumCalculator.getChannel());

			customer.setMasterPolicyHolderName(Constant.AXIS_BANK_LTD);
			String proposalNumber = customerDetailsImpl.genearteProposalNumber();
			logger.info("Saved by Premium calculator (Proposal Number) " + proposalNumber);
			customer.setProposalNumber(proposalNumber);

			SellerDetailEntity sellerDetailEntity = sellerDAO.findByContactNo(premiumCalculator.getSellerContactNo());
			if (sellerDetailEntity != null) {
				if (sellerDetailEntity.getStatus() != null
						&& sellerDetailEntity.getStatus().equalsIgnoreCase(Constant.SELLER_DEACTIVE_STATUS)) {
					response = "Seller is Inactive";
					customer = null;
				} else {
					customer.setSlrDtlId(sellerDetailEntity);
				}
			} else {
				response = "Invalid mobile number";
				customer = null;
			}
			if (customer != null) {
				response = "Saved !!";
			}
		} catch (Exception exception) {
			exception.printStackTrace();
			logger.error("Exception occured while saving customerDetail by using PremiumCalculator :{}", exception);
			response = "DB Error";
			customer = null;
		}
		resultMap.put("response", response);
		resultMap.put("data", customer);
		return resultMap;
	}

	/**
	 * Get list of saved customer data or draft which is stored by using Premium
	 * Calculator
	 * 
	 * @author Devendra.Kumar
	 * @param contactNo
	 */
	@Override
	@Transactional
	public List<CustomerDetailsModel> getDraftByContactNo(Long contactNo) {
		try {
			SellerDetailEntity sellerDetailEntity = sellerDAO.findByContactNo(contactNo);
			if (sellerDetailEntity != null) {
				List<CustomerDetailsEntity> customers = customerDetailsDAO
						.findBySellerContactAndAppStatus(sellerDetailEntity, Status.STEP0);
				if (!customers.isEmpty()) {
					return customerHelper.convertToModelList(customers);
				} else {
					return new ArrayList<>();
				}
			} else {
				throw new Exception("Seller not found for given contact-no");
			}

		} catch (Exception exception) {
			logger.error("Exception occured while getting draft by using SellerContactNo :{}", exception);
		}
		return null;
	}

	/**
	 * to check whether seller is active or not for given contact number
	 * @param contactPhone
	 * @return
	 * @author rajkumar
	 */
	@Transactional
	@Override
	public Boolean isSellerActive(Long contactPhone) {
		SellerDetailEntity sellerDtl = sellerDAO.findByContactNo(contactPhone);
		if (sellerDtl != null) {
			if (sellerDtl.getStatus() != null && sellerDtl.getStatus().equals(Constant.SELLER_DEACTIVE_STATUS)) {
				return Boolean.FALSE;
			} else {
				return Boolean.TRUE;
			}
		} else {
			return null;
		}
	}

	/**
	 * Download all seller in excel
	 */
	@Transactional
	@Override
	public boolean getSellerInBulk() {
		boolean sellerDownloadFlag = false;
		try {
			List<SellerDetailEntity> sellerEntity = sellerDAO.getAll();
			if (sellerEntity != null) {
				logger.info("Total number of seller : " + sellerEntity.size());
			}
			List<SellerDetailModel> sellerDetailsModels = new ArrayList<>(sellerEntity.size());
			for (SellerDetailEntity tempSlrEntity : sellerEntity) {
				SellerDetailModel tempSlrDetailModel = sellerHelper.convertToModel(tempSlrEntity);
				sellerDetailsModels.add(tempSlrDetailModel);
			}
			excelGeneratorUtility.getExcelDataForSeller(sellerDetailsModels, sellerExcelPath, Constant.AXIS);
			sellerDownloadFlag = true;
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return sellerDownloadFlag;
	}

	/**
	 * save premium calculate data into customer
	 * @author nikhilesh
	 */
	@Override
	@Transactional
	public Map<String, Object> savePremiumCalc(PremiumCalculator premiumCalculator) {
		Map<String, Object> resultMap = new HashMap<>(2);
		String response = "";
		String responseStatus = "";
		CustomerDetailsEntity customer = null;
		try {
			customer = customerDetailsDAO.findByLoanAppNumber(premiumCalculator.getLoanAppNumber());
			if(!ObjectUtils.isEmpty(customer)) {
				resultMap.put("response", "Given Loan Application Number already exists for Proposal number "+customer.getProposalNumber()+". Please re-check the App number, or visit the Application Forms list to upload additional documents.");
				resultMap.put("data", customer);
				resultMap.put("responseStatus", Constant.FAILURE);
				return resultMap;
			}
			if(!StringUtils.isEmpty(premiumCalculator.getRoId()) && !ssoManagementService.findByRoId(premiumCalculator.getRoId())) {
				resultMap.put("response", "Given RO SSO ID does not exist.");
				resultMap.put("data", customer);
				resultMap.put("responseStatus", Constant.FAILURE);
				return resultMap;
			}
            if(!StringUtils.isEmpty(premiumCalculator.getSmId()) && !ssoManagementService.findBySmId(premiumCalculator.getSmId())) {
            	resultMap.put("response", "Given SM SSO ID does not exist.");
				resultMap.put("data", customer);
				resultMap.put("responseStatus", Constant.FAILURE);
				return resultMap;
			}
			customer = new CustomerDetailsEntity();
			customer.setTenureEligible(premiumCalculator.getTenureEligible());
			customer.setLoanAppNumber(premiumCalculator.getLoanAppNumber());
			customer.setSumAssured(premiumCalculator.getSumAssured());
			customer.setDob(DateUtil.dateFormater(premiumCalculator.getDob()));
			customer.setLoanTenure(premiumCalculator.getTenureYear());
			customer.setLoanAmount(premiumCalculator.getLoanAmount());
			customer.setInterestRate(premiumCalculator.getInterestRate());
			customer.setIsFinancePremium(premiumCalculator.getIsFinancePremium());
			customer.setTentativeEMI(premiumCalculator.getTentativeEMI());
			customer.setAge(premiumCalculator.getAge());
			customer.setTotalPremium(premiumCalculator.getTotalPremium());
			customer.setIncrementalEMI(premiumCalculator.getIncrementalEMI());
			customer.setIsMedicalUnderWritingRequired(premiumCalculator.getIsMedicalUnderWritingRequired());
			customer.setIsFinancialUnderWritingRequired(premiumCalculator.getIsFinancialUnderWritingRequired());
			customer.setIsInterested(premiumCalculator.getIsInterested());
			customer.setGender(premiumCalculator.getGender());

			customer.setStreet(premiumCalculator.getStreet());
			customer.setAddress1(premiumCalculator.getAddress1());
			customer.setAddress2(premiumCalculator.getAddress2());
			customer.setAddress3(premiumCalculator.getAddress3());
			customer.setPinCode(premiumCalculator.getPinCode());
			customer.setTown(premiumCalculator.getTown());
			customer.setState(premiumCalculator.getState());
			customer.setCountry(premiumCalculator.getCountry());
            customer.setRoId(premiumCalculator.getRoId());
            customer.setSmId(premiumCalculator.getSmId());

			customer.setAppStep(Status.STEP0);
			customer.setLoanType(LoanType.getLoanType(premiumCalculator.getLoanType()));
			customer.setSchemeType(SchemeType.getSchemeType(premiumCalculator.getSchemeType()));
			customer.setCreatedOn(DateUtil.toCurrentUTCTimeStamp());
			customer.setModifiedOn(DateUtil.toCurrentUTCTimeStamp());
			if(Constant.AXIS.equals(premiumCalculator.getChannel())){
				if (premiumCalculator.getReseanForNotInterested() != null) {
					customer.setStatus(Status.APP_NOT_INTERESTED);
					customer.setReseanForInterested(premiumCalculator.getReseanForNotInterested());
				} else {
					customer.setStatus(Status.APP_PENDING);
				}
			    customer.setMasterPolicyHolderName(premiumCalculator.getMph());
			}else if(Constant.PHYSICAL_FORM.equals(premiumCalculator.getChannel())) {
				if (premiumCalculator.getReseanForNotInterested() != null) {
					customer.setStatus(Status.APP_NOT_INTERESTED);
					customer.setReseanForInterested(premiumCalculator.getReseanForNotInterested());
				} else {
					customer.setFormStatus("Physical_Form_Verification");
					customer.setStatus(Status.PHYSICAL_FORM_VERIFICATION);
				}
			}else if(Constant.AFL_PHYSICAL_FORM.equals(premiumCalculator.getChannel())) {
				if (premiumCalculator.getReseanForNotInterested() != null) {
					customer.setStatus(Status.APP_NOT_INTERESTED);
					customer.setReseanForInterested(premiumCalculator.getReseanForNotInterested());
				} else {
					customer.setFormStatus("Physical_Form_Verification");
					customer.setStatus(Status.AFL_PHYSICAL_FORM_VERIFICATION);
				}
			}
			// currently do not use channel name which is coming from premium calculator
			// customer.setMasterPolicyHolderName(premiumCalculator.getChannel());

			// set value of CI Rider
			customer.setBaseOrBaseWithCI(RiderType.getRequestType(premiumCalculator.getBaseOrBaseWithCI()).getLabel());
			boolean isCiriderData = Boolean.FALSE;
			if (RiderType.BASE_CI_RIDER.getValue().equals(premiumCalculator.getBaseOrBaseWithCI())) {
				if (ObjectsUtil.isNotNull(premiumCalculator.getCiOption())
						&& ObjectsUtil.isNotNull(premiumCalculator.getPercentageSumAssured())
						&& ObjectsUtil.isNotNull(premiumCalculator.getCiTenureYears())
						&& ObjectsUtil.isNotNull(premiumCalculator.getCiRiderSumAssured())) {
					isCiriderData = Boolean.TRUE;
					customer.setCiOption(CIRiderOption.getRequestType(premiumCalculator.getCiOption()).getLabel());
					customer.setPercentageOfSumAssured(premiumCalculator.getPercentageSumAssured());
					customer.setCiTenureYears(premiumCalculator.getCiTenureYears());
					customer.setCiRiderSumAssured(premiumCalculator.getCiRiderSumAssured());
					customer.setCiRiderPermium(premiumCalculator.getCiRiderPermium());
					customer.setBaseWithCIPremium(premiumCalculator.getBaseWithCIPremium());
					customer.setMedicalUWCI(premiumCalculator.getMedicalUWCI());
					customer.setFinancialUWCI(premiumCalculator.getFinancialUWCI());
					customer.setOverallUWFinancial(premiumCalculator.getOverallUWFinancial());
					customer.setOverallUWMedical(premiumCalculator.getOverallUWMedical());
					customer.setTentativeEMIBaseWithCI(premiumCalculator.getTentativeEMIBaseWithCI());
					customer.setIncrementalEMIBaseWithCI(premiumCalculator.getIncrementalEMIBaseWithCI());

				} else {
					response = "Please provide ci rider's all fields";
					responseStatus = Constant.FAILURE;
				}
			} else {
				isCiriderData = Boolean.TRUE;
			}
           
			String proposalNumber = customerDetailsImpl.genearteProposalNumber();
			logger.info("Saved by Premium calculator (Proposal Number) " + proposalNumber);
			customer.setProposalNumber(proposalNumber);

			SellerDetailEntity sellerDetailEntity = sellerDAO.findByContactNo(premiumCalculator.getSellerContactNo());
			boolean isCorrectData = Boolean.FALSE;
			if (sellerDetailEntity != null) {
				if (sellerDetailEntity.getStatus() != null
						&& sellerDetailEntity.getStatus().equalsIgnoreCase(Constant.SELLER_DEACTIVE_STATUS)) {
					response = "Seller is Inactive";
					customer = null;
				} else {
					isCorrectData = Boolean.TRUE;
					customer.setSlrDtlId(sellerDetailEntity);
				}
			} else {
				response = "Seller does not exist !!!";
				customer = null;
				responseStatus = Constant.FAILURE;
			}
			if (isCorrectData && isCiriderData) {
				customerDetailsDAO.save(customer);
				response = "User details saved successfully !!!";
				responseStatus = Constant.SUCCESS;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
			logger.error("Exception occured while saving customerDetail by using PremiumCalculator :{}", exception);
			response = "DB Error";
			customer = null;
			responseStatus = Constant.FAILURE;
		}
		resultMap.put("response", response);
		resultMap.put("data", customer);
		resultMap.put("responseStatus", responseStatus);
		return resultMap;
	}

	
	/**
	 * @author 
	 * @param
	 * This method is for save the details of customer physical form
	 */
	
	@Override
	@Transactional
	public Map<String, Object> savePhysicalForm(CustomerPhysicalFormModel customerPhysicalFormModel) {
		Map<String, Object> resultMap = new HashMap<>(2);
		String response = "";
		String responseStatus = "";
		CustomerDetailsEntity customer = null;
		try {
			if(!StringUtils.isEmpty(customerPhysicalFormModel.getProposalNumber())) {
				customer = customerDetailsDAO.findByProposalNumber(customerPhysicalFormModel.getProposalNumber());
				customer.setCustomerFirstName(customerPhysicalFormModel.getCustomerFirstName());
				customer.setCustomerLastName(customerPhysicalFormModel.getCustomerLastName());
				customer.setSumAssured(customerPhysicalFormModel.getSumAssured());
				customer.setAppStep(Status.STEP1);
				customer.setDob(DateUtil.dateFormater(customerPhysicalFormModel.getDob()));
				customer.setLoanType(LoanType.getLoanType(customerPhysicalFormModel.getLoanType()));
				customer.setTotalPremium(customerPhysicalFormModel.getPremiumAmount());
				customer.setModifiedOn(DateUtil.toCurrentUTCTimeStamp());
				customerDetailsDAO.saveOrUpdate(customer);
				response = "Customer detail updated successfully !!!";
				responseStatus = Constant.SUCCESS;
			} else {
				CustomerDetailsEntity customerDetailsEntity = customerDetailsDAO.findByLoanAppNumber(customerPhysicalFormModel.getLoanAppNumber());
				if( customerDetailsEntity != null) {
					String msg = "Given Loan Application Number already exists for Proposal number "+customerDetailsEntity.getProposalNumber()+". Please re-check the App number, or visit the Application Forms list to upload additional documents.";
					resultMap.put("response", msg);
					resultMap.put("data", customerDetailsEntity);
					resultMap.put("responseStatus", Constant.FAILURE);
					return resultMap;
				}
				customer = new CustomerDetailsEntity();
				customer.setCustomerFirstName(customerPhysicalFormModel.getCustomerFirstName());
				customer.setCustomerLastName(customerPhysicalFormModel.getCustomerLastName());
				customer.setLoanAppNumber(customerPhysicalFormModel.getLoanAppNumber());
				customer.setAppStep(Status.STEP1);
				customer.setModifiedOn(DateUtil.toCurrentUTCTimeStamp());
				customer.setCreatedOn(DateUtil.toCurrentUTCTimeStamp());
				customer.setSumAssured(customerPhysicalFormModel.getSumAssured());
				customer.setDob(DateUtil.dateFormater(customerPhysicalFormModel.getDob()));
				customer.setFormStatus("Physical_Form_Verification");
				customer.setStatus(Constant.AXIS_BANK_LTD.equalsIgnoreCase(customerPhysicalFormModel.getMph()) ? Status.PHYSICAL_FORM_VERIFICATION : Status.AFL_PHYSICAL_FORM_VERIFICATION);
				customer.setLoanType(LoanType.getLoanType(customerPhysicalFormModel.getLoanType()));
				//customer.setMasterPolicyHolderName(customerPhysicalFormModel.getMph());
				customer.setTotalPremium(customerPhysicalFormModel.getPremiumAmount());
				String proposalNumber = customerDetailsImpl.genearteProposalNumber();
				logger.info("Saved by Physical_Form_Journey (Proposal Number) : " + proposalNumber);
				customer.setProposalNumber(proposalNumber);

				SellerDetailEntity sellerDetailEntity = sellerDAO
						.findByContactNo(customerPhysicalFormModel.getSellerContactNo());
				boolean isCorrectData = Boolean.FALSE;
				if (sellerDetailEntity != null) {
					if (sellerDetailEntity.getStatus() != null
							&& sellerDetailEntity.getStatus().equalsIgnoreCase(Constant.SELLER_DEACTIVE_STATUS)) {
						response = "Seller is Inactive";
						customer = null;
					} else {
						isCorrectData = Boolean.TRUE;
						customer.setSlrDtlId(sellerDetailEntity);
					}
				} else {
					response = "Seller does not exist !!!";
					customer = null;
					responseStatus = Constant.FAILURE;
				}
				if (isCorrectData) {
					customerDetailsDAO.save(customer);
					response = "Physical_Form_Journey details saved successfully !!!";
					responseStatus = Constant.SUCCESS;
				}
			}
		} catch (Exception exception) {
			exception.printStackTrace();
			logger.error("Exception occured while saving Physical_Form_Journey :{}", exception);
			response = "DB Error";
			customer = null;
			responseStatus = Constant.FAILURE;
		}
		resultMap.put("response", response);
		resultMap.put("data", customer);
		resultMap.put("responseStatus", responseStatus);
		return resultMap;
	}

	/**
	 * used to capture seller last login timestamp
	 * @param contactNo
	 * @author rajkumar
	 */
	@Override
	@Transactional
	public void updateSellerLastLogIn(Long contactNo) {
		try {
		SellerDetailEntity sellerDetailEntity = sellerDAO.findByContactNo(contactNo);
		sellerDetailEntity.setLastLogIn(sellerDetailEntity.getCurrentLogIn());
		sellerDetailEntity.setCurrentLogIn(DateUtil.toCurrentUTCTimeStamp());
		sellerDAO.save(sellerDetailEntity);
		logger.info("Seller last Log In Is Update");}
		catch (Exception e) {
			logger.info("Seller lastLogIn error while  Update "+e.getMessage());	
			}
		
	}
	
	/* this  code is give the all details of seller*/
	@Override
	@Transactional
	public SellerDetailModel findByContantNumber(Long contactNo) {
		SellerDetailEntity sellerDetails = sellerDAO.findByContactNo(contactNo);
		return sellerHelper.convertToModel(sellerDetails);
	}
	
	/**
	 * find list of seller loan type
	 * @return
	 * @author rajkumar
	 */
	@Override
	public Set<String> getAllSellerLoanType() {
		List<MasterLoanTypeBankEntity> masterLoanTypeBankEntity = masterLoanTypeBankDAO.getAllLoanType();
		Set<String> loanTypeList = new HashSet<String>();
		for (MasterLoanTypeBankEntity obj : masterLoanTypeBankEntity) {
			loanTypeList.add(obj.getLoanType());

		}
		return loanTypeList;

	}
	
	/**
	 * return loanType based on seller role
	 */
	@Override
	public Map<String, Object> getSellerRoles() {
		logger.info("Enter into seller service to get seller roles....");
		try {
			Map<String, Object> roleMap = new LinkedHashMap<String, Object>();
			Map<String, Object> roleMapFirst = new HashMap<String, Object>();
			Map<String, Object> roleMapSecond = new HashMap<String, Object>();
			Map<String, Object> roleMapThird = new HashMap<String, Object>();
			Map<String, Object> roleMapFourth = new HashMap<String, Object>();
			Map<String, Object> roleMapFifth = new LinkedHashMap<String, Object>();
			roleMapFirst.put(LoanType.MORTGAGE_LOAN.getText(),Arrays.asList(LoanType.AFFORDABLE_HOUSING_LOAN.getText(), LoanType.HOME_LOAN.getText(),LoanType.SMALL_BUSINESS_LOAN.getText(), LoanType.BUSINESS_BANKING_GCL.getText(),LoanType.HEALTH_CARE_FINANCE.getText(), LoanType.COMMERCIAL_EQUIPMENT_LOAN.getText()));
			roleMapFirst.put(LoanType.MORTGAGE_LOAN_GTL.getText(), Arrays.asList(LoanType.BUSINESS_BANKING_GTL.getText()));
			roleMapSecond.put(LoanType.GOLD_LOAN.getText(), Arrays.asList(LoanType.GOLD_LOAN_GCL.getText()));
			roleMapSecond.put(LoanType.RETAIL_LOAN.getText(), Arrays.asList(LoanType.GOLD_LOAN_GTL.getText()));
			roleMapThird.put(LoanType.VEHICLE_LOAN.getText(), Arrays.asList(LoanType.VEHICLE_LOAN.getText()));
			roleMapFourth.put(LoanType.PERSONAL_LOAN.getText(), Arrays.asList(LoanType.PERSONAL_LOAN.getText()));
			roleMapFifth.putAll(roleMapFirst);
			roleMapFifth.putAll(roleMapSecond);
			roleMapFifth.putAll(roleMapThird);
			roleMapFifth.putAll(roleMapFourth);
			roleMap.put(MLIMessageConstants.ROLE1, roleMapFirst);
			roleMap.put(MLIMessageConstants.ROLE2, roleMapSecond);
			roleMap.put(MLIMessageConstants.ROLE3, roleMapThird);
			roleMap.put(MLIMessageConstants.ROLE4, roleMapFourth);
			roleMap.put(MLIMessageConstants.ROLE5, roleMapFifth);
			return roleMap;
		} catch (Exception exception) {
			logger.error("Exception occured while getting all seller roles :{}", exception);
			exception.printStackTrace();
		}
		return null;
	}
	
	/**
	 * find loan type based on role id
	 * @param roleId
	 * @return
	 * @author rajkumar
	 */
	@Override
	public Set<String> findLonaTypeById(String roleId){
		try {
			Set<String> loanTypes = new HashSet<String>();
			Map<String, Object>  roleMap = getSellerRoles();
			if(roleMap.containsKey(roleId)) {
				Map<String, Object> loanTypeMap = (Map<String, Object>) roleMap.get(roleId);
				for(Map.Entry<String, Object> map : loanTypeMap.entrySet()) {
					loanTypes.add(map.getKey());
				}
			}
			return loanTypes;
		}catch (Exception exception) {
			logger.error("Exception occured while getting loan type based on role id :{}", exception);
			exception.printStackTrace();
		}
		return null;
	}
	
	
	/**
	 * get all master loan type list
	 */
	@Override
	public List<MasterLoanTypeBankEntity> getAllMasterLoanTypeList() {
		logger.info("Enter into seller service to get all master type loan list....");
		try {
		List<MasterLoanTypeBankEntity> masterLoanTypeBankEntity = masterLoanTypeBankDAO.getAllLoanType();
		return masterLoanTypeBankEntity;
		}catch (Exception exception) {
			logger.error("Exception occured while getting loan type based on role id :{}", exception);
			exception.printStackTrace();
		}
        return null;
	}
	
	/**
	 * 
	 * @param otp
	 * @param creditCardCustomerEntity
	 * @author rajkumar
	 */
	private void sendYBLCCOtpInEmail(Integer otp, CreditCardCustomerEntity creditCardCustomerEntity) {
		EmailModel mliEmailModel = new EmailModel();
		mliEmailModel.setMailUserType(Constant.YBLCC_OTP_AT_EMAIL);
		mliEmailModel.setOtp(otp);
		mliEmailModel.setCustomerName(getYBLCCCustFullName(creditCardCustomerEntity));
		mliEmailModel.setMailIdTo(creditCardCustomerEntity.getEmail().trim());
		mliEmailModel.setCoverage(creditCardCustomerEntity.getCoverage());
		ResponseModel<MliEmailServiceModel> emailResponse = null;
		try {
			emailResponse = mliEmailService.sendEmail(mliEmailModel);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!("200".equalsIgnoreCase(emailResponse.getCode()) && ("Ok").equalsIgnoreCase(emailResponse.getStatus()))) {
			logger.error("::::::::::::::::: Mail Not Send to customer ::::::::::::::: at time : " + (new Date()));

		} else {
			logger.info("::::::::::::::::: Mail Send to customer ::::::::::::::: at time : " + (new Date()));
		}
	}
	
	/**
	 * sent otp email for seller login
	 * @param otp
	 * @param sellerDetailEntity
	 * @author rajkumar
	 */
	private void sendLoginOtpInEmail(Integer otp, SellerDetailEntity sellerDetailEntity) {
		EmailModel mliEmailModel = new EmailModel();
		mliEmailModel.setMailUserType(Constant.LOGIN_OTP_AT_EMAIL);
		mliEmailModel.setOtp(otp);
		mliEmailModel.setCustomerName(sellerDetailEntity.getSellerName());
		mliEmailModel.setMailIdTo(sellerDetailEntity.getSellerEmailId());
		ResponseModel<MliEmailServiceModel> emailResponse = null;
		try {
			emailResponse = mliEmailService.sendEmail(mliEmailModel);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (!("200".equalsIgnoreCase(emailResponse.getCode()) && ("Ok").equalsIgnoreCase(emailResponse.getStatus()))) {
			logger.error("::::::::::::::::: Mail Not Send to seller ::::::::::::::: at time : " + (new Date()));

		} else {
			logger.info("::::::::::::::::: Mail Send to seller ::::::::::::::: at time : " + (new Date()));
		}
	}
}
