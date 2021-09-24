package com.mli.controller;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.Valid;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mli.constants.Constant;
import com.mli.constants.MLIMessageConstants;
import com.mli.dao.CustomerDetailsDAO;
import com.mli.entity.CustomerDetailsEntity;
import com.mli.enums.LoanType;
import com.mli.exception.MobileNumberAlreadyExist;
import com.mli.helper.CustomerDetailsHelper;
import com.mli.model.CustomerDetailsModel;
import com.mli.model.LoginModel;
import com.mli.model.PremiumCalculator;
import com.mli.model.SellerBankModel;
import com.mli.model.SellerDetailModel;
import com.mli.model.UserDetailsModel;
import com.mli.model.UserLoginModel;
import com.mli.model.response.ResponseModel;
import com.mli.model.response.SaveUpdateResponse;
import com.mli.security.JwtTokenUtil;
import com.mli.service.SellerService;
import com.mli.utils.CustomResponse;
import com.mli.utils.ObjectsUtil;
import com.mli.utils.aes.AESService;

/**
 * 
 * @author Haripal.Chauhan Seller save/update, getting seller by id or getting
 *         all seller with pagination. Master policy holder list.
 *
 */
@RestController
public class SellerController {
	private static final Logger logger = Logger.getLogger(SellerController.class);

	@Autowired
	private SellerService sellerService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private CustomerDetailsDAO customerDetailsDAO;

	@Autowired
	private AESService aesService;
	
	@Autowired
	private CustomerDetailsHelper customerDetailsHelper; 

	/**
	 * 
	 * @param token
	 * @param sellerDetailModel Save update seller method, If seller id is already
	 *                          created, updating seller otherwise creating new one.
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "seller/savedetails", method = RequestMethod.POST, consumes = {
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> saveOrUpdateSeller(@RequestHeader("Authorization") String token,
			@RequestBody SellerDetailModel sellerDetailModel) {
		SaveUpdateResponse response = new SaveUpdateResponse();

		try {
			if ((sellerDetailModel.getSellerDtlId() == null
					&& sellerService.checkSellerIfExist(sellerDetailModel.getContactNo()))
					|| (sellerDetailModel.getSellerDtlId() != null
							&& sellerService.checkOtherSellerIfExistForMobileUpdate(sellerDetailModel.getContactNo(),
									sellerDetailModel.getSellerDtlId()))) {
				response.setStatus(SaveUpdateResponse.FAILURE);
				response.setMessage("Seller already exists with this mobile number!");
				return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
			}
			sellerService.saveOrUpdateSallerDetails(sellerDetailModel);
			response.setStatus(SaveUpdateResponse.SUCCESS);
			response.setMessage("Saved successfully");
			return ResponseEntity.ok(response);
		} catch (MobileNumberAlreadyExist e) {
			e.printStackTrace();
			response.setStatus(SaveUpdateResponse.FAILURE);
			response.setMessage("Seller already exists with this mobile number!");
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
		} catch (Exception e) {
			logger.error("::::::::::::::::: Error in Seller Creation/Updatation :::::::::::::::" + e);
			e.printStackTrace();
			response.setStatus(SaveUpdateResponse.FAILURE);
			response.setMessage(MLIMessageConstants.EXCEPTION_MSG);
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
		}
	}

	/**
	 * 
	 * @param token
	 * @param id
	 * 
	 *              Getting seller details by id.
	 */
	@RequestMapping(value = "seller/details/{id}", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> getSellerById(@RequestHeader("Authorization") String token, @PathVariable("id") Long id) {
		ResponseModel<SellerDetailModel> response = new ResponseModel<SellerDetailModel>();
		try {
			response.setData(sellerService.getSellerDetailsById(id));
			response.setStatus(SaveUpdateResponse.SUCCESS);
			response.setMessage("Fetched successfully");
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("::::::::::::::::: Error while getting seller detail by id :::::::::::::::" + e.getMessage());
			response.setStatus(SaveUpdateResponse.FAILURE);
			response.setMessage(MLIMessageConstants.EXCEPTION_MSG);
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
		}
	}

	/**
	 * 
	 * @param token
	 * @param perPageRecords
	 * @param pageNumber
	 * @param pattern        Getting all seller details by page wise.
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "seller/details", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> getAllSellers(@RequestHeader("Authorization") String token,
			@RequestParam(name = "per_page_records", required = false) Integer perPageRecords,
			@RequestParam(name = "page_number", required = false) Integer pageNumber,
			@RequestParam(name = "pattern", required = false) Long pattern) {
		try {
			Pageable pageable = new PageRequest(pageNumber, perPageRecords);
			return (!jwtTokenUtil.isTokenExpired(token))
					? ResponseEntity.status(HttpStatus.OK)
							.body(new CustomResponse(Constant.SUCCESS, "Search Successfully",
									sellerService.getAllSellerDetailsByPageNumber(pageable, pattern)))
					: ResponseEntity.status(HttpStatus.UNAUTHORIZED)
							.body(new CustomResponse(Constant.FAILURE, Constant.UNAUTHORIZED_REQUEST));
		} catch (Exception e) {
			e.printStackTrace();
			SaveUpdateResponse exception = new SaveUpdateResponse();
			logger.error("::::::::::::::::: Error while getting seller list :::::::::::::::" + e.getMessage());
			exception.setStatus(SaveUpdateResponse.FAILURE);
			exception.setMessage(e.getMessage());
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(exception);
		}
	}

	/**
	 * 
	 * @param token List of master policy holders.
	 */
	@RequestMapping(value = "seller/masterplicyholders", method = RequestMethod.GET, produces = {
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> getALlMasterPolicyHolderName(@RequestHeader("Authorization") String token) {
		ResponseModel<List<SellerBankModel>> response = new ResponseModel<List<SellerBankModel>>();
		try {
			response.setData(sellerService.getAllMasterPolicyHolders());
			response.setStatus(SaveUpdateResponse.SUCCESS);
			response.setMessage("Fetched successfully");
			return ResponseEntity.ok(response);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(
					"::::::::::::::::: Error while getting master policy holder list :::::::::::::::" + e.getMessage());
			response.setStatus(SaveUpdateResponse.FAILURE);
			response.setMessage(MLIMessageConstants.EXCEPTION_MSG);
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
		}
	}

	/**
	 * Seller is saved customer data by using Premium Calculator.
	 * 
	 */
	@Transactional
	@PostMapping("seller/premium-calc")
	public ResponseEntity<?> saveByPremiumCalc(@Valid @RequestBody PremiumCalculator premiumCalculator,
			BindingResult bindingResult) throws InvalidKeyException, SignatureException, NoSuchAlgorithmException,
			IllegalStateException, IOException {
		Map<String, Object> resultMap = sellerService.saveByPremiumCalc(premiumCalculator);
		String response = (String) resultMap.get("response");
		CustomerDetailsEntity customer = (CustomerDetailsEntity) resultMap.get("data");
		String status = Constant.SUCCESS;

		/*
		 * boolean valid = HmacSha256Signature.validateCheckSum(premiumCalculator,
		 * premiumCalculator.getChecksum()); if(!valid) { response = "Access Denied";
		 * status = Constant.FAILURE; }
		 */
		if (bindingResult.hasErrors()) {
			response = "Field should not be null or empty";
			status = Constant.FAILURE;
		}
		if ("Seller is Inactive".equals(response)) {
			status = Constant.FAILURE;
		} else if ("Invalid mobile number".equals(response)) {
			status = Constant.FAILURE;
		} else if ("Saved !!".equals(response)) {
			if (customer != null) {
				status = Constant.SUCCESS;
				customerDetailsDAO.save(customer);
			} else {
				response = Constant.FAILURE_MSG;
				status = Constant.FAILURE;
			}
		} else if ("DB Error".equals(response)) {
			status = Constant.FAILURE;
		}
		return ResponseEntity.status(HttpStatus.OK)
				.body(new CustomResponse(status, "Premium calculator save Service", response));
	}

	/**
	 * Get list of saved customer data or draft which is stored by using Premium
	 * Calculator
	 * 
	 * @author Devendra.Kumar
	 * @param token
	 * @param contactNo
	 */
	@GetMapping("/seller/draft")
	public ResponseEntity<?> getDraftByContactNo(@RequestHeader("Authorization") String token,
			@RequestParam("contact-no") Long contactNo) {
		return (!jwtTokenUtil.isTokenExpired(token))
				? ResponseEntity.status(HttpStatus.OK)
						.body(new CustomResponse(Constant.SUCCESS, "Get Draft list Successfully",
								sellerService.getDraftByContactNo(contactNo)))
				: ResponseEntity.status(HttpStatus.UNAUTHORIZED)
						.body(new CustomResponse(Constant.FAILURE, Constant.UNAUTHORIZED_REQUEST));
	}

	/**
	 * @author nikhilesh tiwari
	 * @throws JsonProcessingException
	 * @description saving premimum calculator data with base and CI rider (cr) for
	 *              web-app
	 */
	@RequestMapping(value = "seller/premium-calc/cr", method = RequestMethod.POST, consumes = {
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> savePremiumCalculation(@Valid @RequestBody PremiumCalculator premiumCalculator)
			throws JsonProcessingException {
		Map<String, Object> responseMap = sellerService.savePremiumCalc(premiumCalculator);
		String responseStatus = (String) responseMap.get("responseStatus");
		String message = (String) responseMap.get("response");
		CustomerDetailsEntity customerDetailsEntity = (CustomerDetailsEntity)responseMap.get("data");
		if (!ObjectUtils.isEmpty(customerDetailsEntity)) {
			customerDetailsEntity.getSlrDtlId().setSellerBankEntity(null);
			customerDetailsEntity.getSlrDtlId().setUserId(null);
			customerDetailsEntity.getSlrDtlId().setLoanTypeSellerEntity(null);
			;
			CustomerDetailsModel customerDetailsModel = customerDetailsHelper.convertToModel(customerDetailsEntity);
			customerDetailsModel.setLoanType(LoanType.getLoanType(premiumCalculator.getLoanType()).getText());
			try {
				if (responseStatus != null && responseStatus.equals(Constant.SUCCESS)) {
					responseStatus = Constant.SUCCESS;
				} else if (responseStatus != null) {
					responseStatus = Constant.FAILURE;
					premiumCalculator = null;
				}
			} catch (Exception e) {
				logger.error("Error getting to save calculator data");
				e.printStackTrace();
			}

			return ResponseEntity.status(HttpStatus.OK)
					.body(new CustomResponse(responseStatus, message, customerDetailsModel));
		}else {
			return ResponseEntity.status(HttpStatus.OK)
					.body(new CustomResponse(responseStatus, message, null));
		}
	}

	/**
	 * Calculating draft count for particular seller logged in.
	 */
	@RequestMapping(value = "/sellerdtl/draftcount", method = RequestMethod.POST, consumes = {
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> sellerDtlAndDraftCount(@RequestHeader("Authorization") String token,
			@RequestBody LoginModel login) throws Exception {

		try {
			if (!ObjectsUtil.isNull(login.getContactNo())) {
				Long contNo = Long.valueOf(login.getContactNo());
				ResponseModel<UserLoginModel> verifiedOTPResponse = sellerService.sellerDtlAndDraftCount(contNo);
				return ResponseEntity.ok(verifiedOTPResponse);
			} else {
				SaveUpdateResponse response = new SaveUpdateResponse();
				response.setStatus(SaveUpdateResponse.FAILURE);
				response.setMessage(MLIMessageConstants.CON_NO);
				return ResponseEntity.ok(response);
			}
		} catch (Exception e) {
			logger.error("::::::::::::::::::  Error in /sellerdtl/draftcount API :::::::::::::::" + e);
			SaveUpdateResponse response = new SaveUpdateResponse();
			response.setStatus(SaveUpdateResponse.FAILURE);
			response.setMessage(MLIMessageConstants.EXCEPTION_MSG);
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
		}
	}
	
	/**
	 * Get list of saved seller All Loan
	 */
	@GetMapping("/seller/loan")
	public ResponseEntity<?> getAllLoanType(@RequestHeader("Authorization") String token) {
		Set<String> loanTypeSet=sellerService.getAllSellerLoanType();
		return ResponseEntity.status(HttpStatus.OK).body(loanTypeSet);
	}

	/**
	 * api to get all seller roles
	 * @return
	 * @author rajkumar
	 */
	@GetMapping("/seller/role")
	public ResponseEntity<?> getAllLoanType() {
		return ResponseEntity.status(HttpStatus.OK).body(sellerService.getSellerRoles());
	}
	
	/**
	 * Get list of saved  master Loan list
	 */
	@GetMapping("/seller/loan-list")
	public ResponseEntity<?> getAllLoanTypeList(@RequestHeader("Authorization") String token) {
		return ResponseEntity.status(HttpStatus.OK).body(sellerService.getAllMasterLoanTypeList());
	}
}
