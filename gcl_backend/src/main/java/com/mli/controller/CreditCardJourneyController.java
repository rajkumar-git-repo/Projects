package com.mli.controller;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mli.constants.Constant;
import com.mli.constants.MLIMessageConstants;
import com.mli.enums.Status;
import com.mli.model.CreditCardJourneyModel;
import com.mli.model.CustomerDeclarationModel;
import com.mli.model.CustomerUpdateModel;
import com.mli.model.response.ResponseModel;
import com.mli.model.response.SaveUpdateResponse;
import com.mli.security.JwtTokenUtil;
import com.mli.service.CreditCardJourneyService;
import com.mli.service.CustomerDetailService;
import com.mli.service.ReportEmailToBankService;
import com.mli.utils.CustomResponse;
import com.mli.utils.ObjectsUtil;
import com.mli.utils.aes.AESService;

/**
 * 
 * @author rajkumar
 * this controller is specially for credit card journey
 */
@RestController
public class CreditCardJourneyController {
	
	private static final Logger logger = Logger.getLogger(CreditCardJourneyController.class);

	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private CreditCardJourneyService creditCardJourneyService;
	
	@Autowired
	private CustomerDetailService customerDetailService;
	
	@Autowired
	private AESService aesService;
	
	@Autowired
	private ReportEmailToBankService reportEmailToBankService;
	/**
     * 
     * @param token
     * @param userDetails
     * Save update credit card journey
     * AT STEP1: customer details, STEP2: nominee and Appointee details, STEP3: Health declaration, STEP4: Mandatory  declaration, STEP5: Customer OTP verification.
     */
	@PreAuthorize("hasRole('ROLE_SELLER')")
	@RequestMapping(value = "/card-journey/save")
	public ResponseEntity<?> saveUSerDetails(@Valid @RequestBody CreditCardJourneyModel creditCardJourneyModel) throws Exception {
		SaveUpdateResponse response = new SaveUpdateResponse();
		try {
			Integer step = creditCardJourneyModel.getSteps();
			if(ObjectUtils.isEmpty(step)) {
				response.setStatus(SaveUpdateResponse.FAILURE);
				response.setMessage(MLIMessageConstants.NO_STEP_NUMBER);
				return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
			}
			if(step.equals(Status.STEP1.getValue()) && !ObjectsUtil.isNull(creditCardJourneyModel.getCreditCardCustomerModel())){
				if(ObjectsUtil.isNull(creditCardJourneyModel.getCreditCardCustomerModel().getFirstName()) || creditCardJourneyModel.getCreditCardCustomerModel().getLastName().trim().length() == 0){
					response.setStatus(SaveUpdateResponse.FAILURE);
					response.setMessage(MLIMessageConstants.NAME_VALIDATION);
					return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
				}
			}
			if(step.equals(Status.STEP2.getValue())  && !ObjectsUtil.isNull(creditCardJourneyModel.getCreditCardNomineeModel())){
				if(StringUtils.isEmpty(creditCardJourneyModel.getCreditCardNomineeModel().getNomineeFirstName())){
					response.setStatus(SaveUpdateResponse.FAILURE);
					response.setMessage(MLIMessageConstants.NAME_VALIDATION);
					return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
				}
				if(StringUtils.isEmpty(creditCardJourneyModel.getCreditCardNomineeModel().getRelationshipWithAssured())){
					response.setStatus(SaveUpdateResponse.FAILURE);
					response.setMessage(MLIMessageConstants.OTHERS_NAME_VALIDATION);
					return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
				}
				boolean isValidAge = customerDetailService.checkAgeGreaterThan18(creditCardJourneyModel.getCreditCardNomineeModel().getNomineeDob());
				if (!isValidAge) {
					if (StringUtils.isEmpty(creditCardJourneyModel.getCreditCardNomineeModel().getAppointeeFirstName())) {
						response.setStatus(SaveUpdateResponse.FAILURE);
						response.setMessage(MLIMessageConstants.NAME_VALIDATION);
						return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
					}
					if (StringUtils.isEmpty(creditCardJourneyModel.getCreditCardNomineeModel().getRelationWithNominee())) {
						response.setStatus(SaveUpdateResponse.FAILURE);
						response.setMessage(MLIMessageConstants.OTHERS_NAME_VALIDATION);
						return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
					}
				}
			}
			if(creditCardJourneyModel.getSteps() == 1  && creditCardJourneyModel.getSellerContNumber() != null && creditCardJourneyModel.getSellerContNumber().toString().trim().equals(creditCardJourneyModel.getCreditCardCustomerModel().getPhone().toString().trim())){
				response.setStatus(SaveUpdateResponse.FAILURE);
				response.setMessage(MLIMessageConstants.CUST_SELLER_SAME_NO);
				return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
			}
			ResponseModel<CreditCardJourneyModel> userDetail = creditCardJourneyService.saveCreditCardJourneyDetails(creditCardJourneyModel);
			return ResponseEntity.ok(userDetail);
		} catch (Exception e) {
			logger.error("::::::Error occured while saving credit card journey:::::"+ e);
			response.setStatus(SaveUpdateResponse.FAILURE);
			response.setMessage(e.getMessage());
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
		}
	}
	
	/**
	 * get lookup for credit card premium
	 * @param token
	 * @return
	 * @author rajkumar
	 */
	@PreAuthorize("hasRole('ROLE_SELLER')")
	@GetMapping(value = "/card-journey/lookup")
	public ResponseEntity<?> getAdminConfig() {
		return ResponseEntity.status(HttpStatus.OK).body(creditCardJourneyService.getLookup());
	}
	
	/**
	 * 
	 * @param pageable
	 * @param slrContNo
	 * @param param
	 * @param status
	 * @return list of credit card customer based on search param
	 * @author rajkumar
	 */
	@PreAuthorize("hasRole('ROLE_SELLER')")
	@GetMapping(value = "/card-journey/search")
	public ResponseEntity<?> searchCustomer(Pageable pageable,@RequestParam("slr-contno") String slrContNo, @RequestParam(value = "param", required = false) String param,
			@RequestParam(value = "status", required = false) String status,@RequestParam(value = "paymentStatus", required = false) String paymentStatus) {
		String decSlrContNo = "";
		String decParam = "";
		String decStatus = "";
		String decPaymentStatus = "";
		try {
			if (!StringUtils.isEmpty(slrContNo)) {
				decSlrContNo = aesService.decryptData(java.net.URLDecoder.decode(slrContNo, StandardCharsets.UTF_8.name()));
			}
			if (!StringUtils.isEmpty(param)) {
				decParam = aesService.decryptData(java.net.URLDecoder.decode(param, StandardCharsets.UTF_8.name()));
			}
			if (!StringUtils.isEmpty(status)) {
				decStatus = aesService.decryptData(java.net.URLDecoder.decode(status, StandardCharsets.UTF_8.name()));
			}
			if (!StringUtils.isEmpty(paymentStatus)) {
				decPaymentStatus = aesService.decryptData(java.net.URLDecoder.decode(paymentStatus, StandardCharsets.UTF_8.name()));
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(Constant.SUCCESS, MLIMessageConstants.SEARCH_SUCCESS,
				creditCardJourneyService.search(pageable, Long.parseLong(decSlrContNo), decParam, decStatus, decPaymentStatus)));
	}
	
	/**
	 * get status from customer
	 * @param data
	 * @return
	 * @author rajkumar
	 */
	@GetMapping("/card-journey/status")
	public ResponseEntity<?> getStatus(@RequestParam("data") String data) {
		return ResponseEntity.status(HttpStatus.OK).body(
				new CustomResponse(Constant.SUCCESS, MLIMessageConstants.STATUS_SUCCESS, creditCardJourneyService.getStatus(data)));
	}

	/**
	 * update customer health
	 * @param csModel
	 * @return
	 * @author rajkumar
	 */
	@PostMapping("/card-journey/answer")
	public ResponseEntity<?> saveReflexiveQuestion(@Valid @RequestBody CustomerUpdateModel customerUpdateModel) {
		return ResponseEntity.status(HttpStatus.OK).body(creditCardJourneyService.updateCustomer(customerUpdateModel));
	}
	
	/**
	 * save customer declaration
	 * @param customerDeclarationModel
	 * @return
	 * @author rajkumar
	 */
	@PostMapping("/card-journey/declaration")
	public ResponseEntity<?> saveCustDeclaration(@Valid @RequestBody CustomerDeclarationModel customerDeclarationModel) {
		return ResponseEntity.status(HttpStatus.OK).body(creditCardJourneyService.saveCustDeclaration(customerDeclarationModel));
	}
	
	
	/**
	 * cron test api for payment reminder
	 * @return
	 * @author rajkumar
	 */
	@GetMapping("/card-journey/cron")
	public ResponseEntity<?> executeCron() {
		return ResponseEntity.status(HttpStatus.OK).body(
				new CustomResponse(Constant.SUCCESS, MLIMessageConstants.STATUS_SUCCESS, creditCardJourneyService.executeCron()));
	}
}
