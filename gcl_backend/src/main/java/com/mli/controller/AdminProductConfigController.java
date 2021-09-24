package com.mli.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mli.constants.MLIMessageConstants;
import com.mli.dao.AdminProductConfigDao;
import com.mli.entity.AdminProductConfigEntity;
import com.mli.model.AdminProductConfigModel;
import com.mli.model.MISRecipientModel;
import com.mli.model.response.ResponseModel;
import com.mli.security.JwtTokenUtil;
import com.mli.service.AdminProductConfigService;
import com.mli.service.CustomerDetailService;

/**
 * @author baisakhi
 * 
 *         this controller is specially for admin confirguation like ci rider
 *         add or remove
 *
 */
@RestController
public class AdminProductConfigController {
	@Autowired
	private AdminProductConfigService adminProductConfigService;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	/**
	 * api is used to create {@link AdminProductConfigEntity} object
	 * @param token
	 * @param adminProductConfigModelList
	 * @return
	 * @throws Exception
	 * @author rajkumar
	 */
	@RequestMapping(value = "/Admin-config/create", method = RequestMethod.POST, consumes = {
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> createConfigProduct(@RequestHeader("Authorization") String token,
			@RequestBody List<AdminProductConfigModel> adminProductConfigModelList) throws Exception {
		if (jwtTokenUtil.isTokenExpired(token)
				|| (!jwtTokenUtil.getUserRoleFromToken(token).contains("ROLE_ADMIN"))) {
			return ResponseEntity.badRequest().body(MLIMessageConstants.UNAUTHORIZED_REQUEST);
		}
		ResponseModel<?> responseModel = adminProductConfigService
				.createAdminConfig(adminProductConfigModelList);
		return ResponseEntity.status(HttpStatus.OK).body(responseModel);

	}

	/**
	 * Fetched Complete Details of Config Product
	 * 
	 * @param data : <checksum>
	 * @return
	 */
	@GetMapping(value = "Admin-config/get")
	public ResponseEntity<?> getAdminConfigDetails(@RequestHeader("Authorization") String token,@RequestParam("bankName") String bankName) {
		return ResponseEntity.status(HttpStatus.OK).body(adminProductConfigService.getAdminConfigDetails(bankName));
	}
	
	/**
	 * 
	 * @param token
	 * @return api to get all {@link AdminProductConfigEntity} details
	 * @author rajkumar
	 */
	@GetMapping(value = "Admin-config/getAll")
	public ResponseEntity<?> getAllAdminConfigDetails() {
		return ResponseEntity.status(HttpStatus.OK).body(adminProductConfigService.getAllAdminConfig());
	}
	
	/**
	 * api to get {@link AdminProductConfigEntity} based on bankName
	 * @param token
	 * @return 
	 * @author rajkumar
	 */
	@GetMapping(value = "Admin-config")
	public ResponseEntity<?> getAdminConfig(@RequestHeader("Authorization") String token) {
		return ResponseEntity.status(HttpStatus.OK).body(adminProductConfigService.getByCurrentSellerBankName(token));
	}

	/**
	 * api to add {@link MISRecipientModel} mis recipient
	 * @param token
	 * @param misRecipientModel
	 * @return
	 * @throws Exception
	 * @author rajkumar
	 */
	@RequestMapping(value = "/Admin-config/add-mail", method = RequestMethod.POST, consumes = {
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> saveMisRecipient(@RequestHeader("Authorization") String token,
			@Valid @RequestBody List<MISRecipientModel> misRecipientModel) throws Exception {
		if (jwtTokenUtil.isTokenExpired(token) || (!jwtTokenUtil.getUserRoleFromToken(token).contains("ROLE_ADMIN"))) {
			return ResponseEntity.badRequest().body(MLIMessageConstants.UNAUTHORIZED_REQUEST);
		}
		ResponseModel<?> responseModel = adminProductConfigService.saveMisRecipients(misRecipientModel);
		return ResponseEntity.status(HttpStatus.OK).body(responseModel);
	}
	
	/**
	 * api to get all Recipient based on mph type
	 * @param token
	 * @param mphType
	 * @return
	 * @throws Exception
	 * @author rajkumar
	 */
	@GetMapping(value = "Admin-config/fetch-mail")
	public ResponseEntity<?> getAllMisRecipient(@RequestHeader("Authorization") String token,
			@RequestParam(name = "mphType", required = false) String mphType) throws Exception {
		if (jwtTokenUtil.isTokenExpired(token) || (!jwtTokenUtil.getUserRoleFromToken(token).contains("ROLE_ADMIN"))) {
			return ResponseEntity.badRequest().body(MLIMessageConstants.UNAUTHORIZED_REQUEST);
		}
		ResponseModel<?> responseModel = adminProductConfigService.getAllRecipients(mphType);
		return ResponseEntity.status(HttpStatus.OK).body(responseModel);

	}
}
