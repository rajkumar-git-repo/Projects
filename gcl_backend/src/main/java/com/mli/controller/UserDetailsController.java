package com.mli.controller;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mli.constants.Constant;
import com.mli.constants.MLIEmailSmsConstants;
import com.mli.constants.MLIMessageConstants;
import com.mli.entity.CamReportDetailsEntity;
import com.mli.enums.RelationshipWithAssured;
import com.mli.enums.Status;
import com.mli.exception.StorageException;
import com.mli.modal.CamResponseModel;
import com.mli.modal.DocumentDeleteModel;
import com.mli.model.CSModel;
import com.mli.model.CovidReportModel;
import com.mli.model.UserDetailsModel;
import com.mli.model.response.ResponseModel;
import com.mli.model.response.SaveUpdateResponse;
import com.mli.security.JwtTokenUtil;
import com.mli.service.CustomerDetailService;
import com.mli.service.ReportEmailToBankService;
import com.mli.utils.CustomResponse;
import com.mli.utils.ObjectsUtil;
import com.mli.utils.aes.AESService;

/**
 * @author Nikhilesh.Tiwari
 * Save, update, get proposal details API. 
 */
/**
 * @author baisakhi
 *
 */
@RestController
public class UserDetailsController {

	private static final Logger logger = Logger.getLogger(UserDetailsController.class);

	@Autowired
	private CustomerDetailService customerDetailService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private ReportEmailToBankService bankService;
	
	@Autowired
	private AESService aesService;
	
    /**
     * 
     * @param token
     * @param userDetails
     * Save update customer(proposal details). 
     * AT STEP1: customer details, STEP2: nominee and Appointee details, STEP3: Health declaration, STEP4: Mandatory  declaration, STEP5: Customer OTP verification.
     */
	@RequestMapping(value = "/save/userdetails", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> saveUSerDetails(@RequestHeader("Authorization") String token, @RequestBody UserDetailsModel userDetails) throws Exception {
		SaveUpdateResponse response = new SaveUpdateResponse();
		
		
		try {
			String proposalNumber = userDetails.getProposalNumber();
			Integer step = userDetails.getSteps();
			if (!step.equals(Status.STEP1.getValue()) && ObjectsUtil.isNull(proposalNumber)) {
				response.setStatus(SaveUpdateResponse.FAILURE);
				response.setMessage(MLIMessageConstants.PROPOSAL_NOT_FOUND);
				return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
			}
			if(step.equals(Status.STEP1.getValue()) && !ObjectsUtil.isNull(userDetails)){
				if(ObjectsUtil.isNull(userDetails.getCustomerDetails().getCustomerFirstName()) || userDetails.getCustomerDetails().getCustomerFirstName().trim().length() == 0){
					response.setStatus(SaveUpdateResponse.FAILURE);
					response.setMessage(MLIMessageConstants.NAME_VALIDATION);
					return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
				}
			}
			if(step.equals(Status.STEP2.getValue())  && !ObjectsUtil.isNull(userDetails)){
				if(ObjectsUtil.isNull(userDetails.getNomineeDetails().getNomineeFirstName()) || userDetails.getNomineeDetails().getNomineeFirstName().trim().length() == 0){
					response.setStatus(SaveUpdateResponse.FAILURE);
					response.setMessage(MLIMessageConstants.NAME_VALIDATION);
					return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
				}
				if(!ObjectsUtil.isNull(userDetails.getNomineeDetails().getAppointeeDetails()) && (ObjectsUtil.isNull(userDetails.getNomineeDetails().getAppointeeDetails().getAppointeeFirstName()) || userDetails.getNomineeDetails().getAppointeeDetails().getAppointeeFirstName().trim().length()==0)){
					response.setStatus(SaveUpdateResponse.FAILURE);
					response.setMessage(MLIMessageConstants.NAME_VALIDATION);
					return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
				}
				if(userDetails.getNomineeDetails().getRelationWitHAssured().equals(RelationshipWithAssured.OTHERS.getLabel()) && (( ObjectsUtil.isNull(userDetails.getNomineeDetails().getRelationWithNominee())) || userDetails.getNomineeDetails().getRelationWithNominee().trim().length() == 0 || userDetails.getNomineeDetails().getRelationWithNominee().length() > 55)){
					response.setStatus(SaveUpdateResponse.FAILURE);
					response.setMessage(MLIMessageConstants.OTHERS_NAME_VALIDATION);
					return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
				}
				if((!ObjectsUtil.isNull(userDetails.getNomineeDetails().getAppointeeDetails()) && userDetails.getNomineeDetails().getAppointeeDetails().getRelationWithAssured().equals(RelationshipWithAssured.OTHERS.getLabel())) && (ObjectsUtil.isNull(userDetails.getNomineeDetails().getAppointeeDetails().getRelationWithAppointee()) || (userDetails.getNomineeDetails().getAppointeeDetails().getRelationWithAppointee().trim().length()==0) || (userDetails.getNomineeDetails().getAppointeeDetails().getRelationWithAppointee().length() > 55) )){
					response.setStatus(SaveUpdateResponse.FAILURE);
					response.setMessage(MLIMessageConstants.OTHERS_NAME_VALIDATION);
					return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
				}
			}
			if(userDetails.getSteps()==1  && userDetails.getSellerContNumber() != null && Long.parseLong(userDetails.getSellerContNumber()) == (userDetails.getCustomerDetails().getCustMobileNo())){
				response.setStatus(SaveUpdateResponse.FAILURE);
				response.setMessage(MLIMessageConstants.CUST_SELLER_SAME_NO);
				return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
			}

			ResponseModel<UserDetailsModel> userDetail = customerDetailService.saveCustomerDetails(userDetails, step, proposalNumber);
			return ResponseEntity.ok(userDetail);
		} catch (Exception e) {
			logger.error("::::::/save/userdetails:::::"+ e);
			response.setStatus(SaveUpdateResponse.FAILURE);
			response.setMessage(e.getMessage());
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
		}
	}
   /**
    * 
    * @param token
    * @param contNumber 
    * Listing pending proposals for seller by his/her contact number.
 * @throws JsonProcessingException 
    */
	@RequestMapping(value = "/pendingproposal/{contNumber}", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> getPendingProposals(@RequestHeader("Authorization") String token, @PathVariable("contNumber") String contNumber) throws JsonProcessingException {
		ResponseModel<List<UserDetailsModel>> response = new ResponseModel<List<UserDetailsModel>>();
		try {
			response.setData(customerDetailService.getPendingProposalsBySellerContNo(contNumber));
			response.setStatus(SaveUpdateResponse.SUCCESS);
			response.setMessage("Fetched successfully");
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			logger.error("::::::::::Error in getting pending proposals:::::" + e.getMessage());
			response.setStatus(SaveUpdateResponse.FAILURE);
			response.setMessage(MLIMessageConstants.EXCEPTION_MSG);	
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
		}
	}

/**
 * 
 * @param file
 * @param proposalNumber
 * @param token
 * Uploading CDF file after customer OTP verification.
 */
	@RequestMapping(value = "/save/uploadfile", method = RequestMethod.POST)
	public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file,
			@RequestParam(name = "proposalNumber") String proposalNumber, @RequestHeader(value = "Authorization", required = true) String token) throws Exception {
		SaveUpdateResponse response = new SaveUpdateResponse();
		try {
			try{
				if(jwtTokenUtil.isTokenExpired(token)){
					response.setStatus(SaveUpdateResponse.FAILURE);
					response.setMessage(MLIMessageConstants.UNAUTHORIZED_REQUEST);
					return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
				}
			}
			catch(Exception e){
				response.setStatus(SaveUpdateResponse.FAILURE);
				response.setMessage(MLIMessageConstants.SESSION_EXPIRED);
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
			}
			if (file.isEmpty()) {
				throw new StorageException(MLIEmailSmsConstants.EMPTY_IMPORT_FILE + file.getOriginalFilename());
			} 
			if (ObjectsUtil.isNull(proposalNumber)) {
				response.setStatus(SaveUpdateResponse.FAILURE);
				response.setMessage(MLIMessageConstants.NO_PROPOSAL_NO);
				return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
			} else {
				ResponseModel<UserDetailsModel> userDetail = customerDetailService.saveUploadFile(file, proposalNumber);
				return ResponseEntity.ok(userDetail);
			}
		} catch (Exception e) {
			logger.error("::::::::::Error in save upload file:::::" + e);
			response.setStatus(SaveUpdateResponse.FAILURE);
			response.setMessage(MLIMessageConstants.EXCEPTION_MSG);
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
		}
	}

/**
 * 
 * @param token
 * @param proposalNumber
 * Final submit, sending proposal SMS, sending email to customer with attached proposal PDF. 
 */
	@RequestMapping(value = "/save/finalsubmit", method = RequestMethod.POST,consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> finalSubmit(@RequestHeader("Authorization") String token, @RequestParam(name = "proposalNumber") String proposalNumber) throws Exception {
		SaveUpdateResponse response = new SaveUpdateResponse();
		try {
			if(ObjectsUtil.isNull(proposalNumber)){
				response.setStatus(SaveUpdateResponse.FAILURE);
				response.setMessage(MLIMessageConstants.NO_PROPOSAL_NO);
				return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
			}
			ResponseModel<UserDetailsModel> userDetail = customerDetailService.saveFinalSubmit(proposalNumber);
			return ResponseEntity.ok(userDetail);

		} catch (Exception e) {
			logger.error("::::::::::Error in save final submit:::::" + e);
			response.setStatus(SaveUpdateResponse.FAILURE);
			response.setMessage(MLIMessageConstants.EXCEPTION_MSG);
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
		}
	}

	/**
	 * {@code param} is loanAppNumber or firstName or lastName
	 * 
	 * @param token
	 * @param param
	 * Getting Proposal/customer details by loan app number.
	 */
	@RequestMapping(value = "/customer/details/{param}", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<?> getCustoemrDetailsByLoanAppNo(@RequestHeader("Authorization") String token, @PathVariable("param") String param,
			 @RequestParam(value = "mphType", required = true) String mphType) {
		ResponseModel<List<Map<String, Object>>> response = new ResponseModel<List<Map<String, Object>>>();
		try {
			List<Map<String, Object>> customerDetailsModel = null;
			if(!StringUtils.isEmpty(mphType)) {
				if(Constant.AXIS.equalsIgnoreCase(mphType)) {
			       customerDetailsModel = customerDetailService.getByLoanAppNo(param);
				}else if(Constant.YESBANKCC.equalsIgnoreCase(mphType)) {
					customerDetailsModel = customerDetailService.searchYBLCCCustomer(param);
				}
			}
			if(customerDetailsModel != null){
				response.setData(customerDetailsModel);
				response.setStatus(SaveUpdateResponse.SUCCESS);
				response.setMessage("Fetched successfully");
				return ResponseEntity.ok(response);	
			}
			else{
				response.setStatus(SaveUpdateResponse.FAILURE);
				response.setMessage("No record found!");			
				return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
			}

		} catch (Exception e) {
			logger.error("::::::::::Error in getting customer details on  admin portal:::::" + e.getMessage());
			response.setStatus(SaveUpdateResponse.FAILURE);
			response.setMessage(MLIMessageConstants.EXCEPTION_MSG);			
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
		}
	}


	/**
	 * Upload passport for NRI/Other 
	 * 
	 * @author Devendra.Kumar
	 * @param token
	 * @param contactNo
	 */
	@PostMapping("/upload-passport")
	public ResponseEntity<?> uploadPasport(@RequestHeader("Authorization") String token,
			@RequestParam("proposalNumber") String proposalNumber,@RequestParam(value = "file1",required=true) MultipartFile front,
			@RequestParam(value = "file2",required=false) MultipartFile back) {
		return (!jwtTokenUtil.isTokenExpired(token))
				? ResponseEntity.status(HttpStatus.OK)
						.body(customerDetailService.uploadPasport(proposalNumber,front,back))
				: ResponseEntity.status(HttpStatus.UNAUTHORIZED)
						.body(new CustomResponse(Constant.FAILURE, Constant.UNAUTHORIZED_REQUEST));
	}
	
	/**
	 * @author Devendra.Kumar
	 * search and filter on draft screen
	 * 
	 * Seller Home Page
	 * 
	 */
//	@PreAuthorize("hasRole('ROLE_SELLER')")
	@GetMapping("/customer/search")
	public ResponseEntity<?> search(@RequestHeader("Authorization") String token,
			Pageable pageable, @RequestParam(value = "param", required = false) String param,
			@RequestParam(value = "status", required = false) String status,
			@RequestParam("slr-contno") String slrContNo,@RequestParam(value = "isFinance", required = false)String isFinance) {
		    Long contact=null;
		    String result=null;
		    String parmEnc=null;
		    String statusEnc=null;
		    String decrptParam=null;
		    String depIsFinaceParm=null;
		  
            try {
		    	
		    	if(!StringUtils.isEmpty(isFinance)) {
		    		depIsFinaceParm = aesService.decryptData(java.net.URLDecoder.decode(isFinance, StandardCharsets.UTF_8.name()));
		    	}
				 result = java.net.URLDecoder.decode(slrContNo, StandardCharsets.UTF_8.name());
				 if (!ObjectsUtil.isNull(param)) {
				 parmEnc = java.net.URLDecoder.decode(param, StandardCharsets.UTF_8.name()); 
				 decrptParam= aesService.decryptData(parmEnc);
				 }
				 statusEnc = java.net.URLDecoder.decode(status, StandardCharsets.UTF_8.name());
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		    String decrptContact=aesService.decryptData(result);
		    String decrptStatus=aesService.decryptData(statusEnc);
		   
		    if(decrptContact!=null) {
		    	 contact=Long.parseLong(decrptContact);
		    }
		    
		return (!jwtTokenUtil.isTokenExpired(token))
				? ResponseEntity.status(HttpStatus.OK)
						.body(new CustomResponse(Constant.SUCCESS, "Search Successfully",
								customerDetailService.search(contact, decrptParam, decrptStatus, pageable, depIsFinaceParm)))
				: ResponseEntity.status(HttpStatus.UNAUTHORIZED)
						.body(new CustomResponse(Constant.FAILURE, Constant.UNAUTHORIZED_REQUEST));
	}
	
	/**
	 * Using for delete all saved answer
	 */
	@GetMapping("/exceldump")
	@Transactional
	public void manuallyExclDump() {
/*		Session session = sessionFactory.getCurrentSession();
		Transaction transaction = session.beginTransaction();
		sessionFactory.getCurrentSession().createQuery("delete from SelectOptionEntity").executeUpdate();
		sessionFactory.getCurrentSession().createQuery("delete from ReflexiveAnswerEntity").executeUpdate();
		session.flush();
		transaction.commit();*/
		bankService.sendExcelToBank();
		logger.info("::::::::::::::::exceldump::::::::::::::::");
	}
	
	/**
	 * @author Devendra.Kumar
	 * @param data : <checksum>
	 * api to get customer status
	 */
	@GetMapping("/customer/status")
	public ResponseEntity<?> getStatus(@RequestParam("data") String data) {
		return ResponseEntity.status(HttpStatus.OK).body(
				new CustomResponse(Constant.SUCCESS, "Get status Successfully", customerDetailService.getStatus(data)));
	}

	/**
	 * Fetched Complete customer detail
	 * @param data : <checksum>
	 * @return
	 */
	@GetMapping("/customer")
	public ResponseEntity<?> getCustomerDetails(@RequestParam("data") String data) {
		return ResponseEntity.status(HttpStatus.OK).body(customerDetailService.getCustomerDetails(data));
	}
	
	/**
	 * Edit answer
	 * @return
	 */
	@PostMapping("/customer/answer")
	public ResponseEntity<?> saveReflexiveQuestion(@Valid @RequestBody CSModel csModel) {
		return ResponseEntity.status(HttpStatus.OK).body(customerDetailService.editReflexiveQuestion(csModel));
	}
	
	/**
	 * @exception Uploade Additional Document
	 * @param proposalNumber
	 * @param files
	 * @return ResponseModel<List<CamResponseModel>>
	 */
	@PostMapping("/customer/cam")
	public ResponseEntity<ResponseModel<List<CamResponseModel>>> uploadFile(
			@RequestHeader("Authorization") String token, @RequestParam("file") List<MultipartFile> files,
			@RequestParam("proposalNumber") String proposalNumber, @RequestParam("sellerName") String sellerId) {
		ResponseModel<List<CamResponseModel>> response = customerDetailService.saveCamFiles(files, proposalNumber,
				sellerId);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	/**
	 * @exception remove Additional Document
	 * @param proposalNumber
	 * @param fileUrl
	 * @return ResponseModel<String>
	 * @DeleteMapping forntend are not mapping with this thats why it will changes deleteMapping to PostMapping
	 */
	@PostMapping("/customer/remove-document")
	public ResponseEntity<?> removeCamUploadDocument(@RequestHeader("Authorization") String token,
			@RequestBody DocumentDeleteModel documentDeleteModel) {
		List<CamResponseModel> listCam = customerDetailService.deleteAdditionalFile(documentDeleteModel.getUrl(),
				documentDeleteModel.getProposalNumber());
		logger.info("===================File Path Url ==================="+documentDeleteModel.getUrl());
		return ResponseEntity.status(HttpStatus.OK)
				.body(new CustomResponse(Constant.SUCCESS, "Get status Successfully", listCam));
	}
	
	/**
	 * api to upload covid report
	 * @param covidReportUploadModel
	 * @return
	 * @author rajkumar
	 */
	@PostMapping("/customer/upload-covid-report")
	public ResponseEntity<ResponseModel<List<CovidReportModel>>> uploadCovidReport(@RequestParam(name="file",required = true) List<MultipartFile> files, 
			@RequestParam(name="proposalNumber",required=true) String proposalNumber,@RequestParam(name="fileType",required = true) String fileType) {
		ResponseModel<List<CovidReportModel>> response = customerDetailService.uploadCovidReport(files,proposalNumber,fileType);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}

}






