package com.mli.controller;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
import com.mli.entity.CustomerDetailsEntity;
import com.mli.helper.CustomerDetailsHelper;
import com.mli.modal.DocumentDeleteModel;
import com.mli.modal.FileUploadModel;
import com.mli.model.CustomerDetailsModel;
import com.mli.model.CustomerPhysicalFormModel;
import com.mli.model.response.ResponseModel;
import com.mli.service.CustomerDetailService;
import com.mli.service.SellerService;
import com.mli.utils.CustomResponse;

@RestController
public class PhysicalCustomerJourneyController {

	private static final Logger logger = Logger.getLogger(PhysicalCustomerJourneyController.class);

	@Autowired
	private SellerService sellerService;

	@Autowired
	private CustomerDetailService customerDetailService;
	
	@Autowired
	private CustomerDetailsHelper customerDetailsHelper;

	/**
	 * @author
	 * @throws JsonProcessingException
	 * @description saving the data from customer physical form
	 */
	@RequestMapping(value = "/physical-customer/create", method = RequestMethod.POST, consumes = {
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> savePhysicallCustomerForm(@RequestHeader("Authorization") String token,
			@RequestBody CustomerPhysicalFormModel customerPhysicalFormModel) {

		Map<String, Object> responseMap = sellerService.savePhysicalForm(customerPhysicalFormModel);
		String responseStatus = (String) responseMap.get("responseStatus");
		String message = (String) responseMap.get("response");
		CustomerDetailsEntity customerDetails=(CustomerDetailsEntity) responseMap.get("data");
		
		CustomerDetailsModel customerModel=customerDetailsHelper.convertToModel(customerDetails);
		
		
		try {
			if (responseStatus != null && responseStatus.equals(Constant.SUCCESS)) {
				responseStatus = Constant.SUCCESS;
			} else if (responseStatus != null) {
				responseStatus = Constant.FAILURE;
				customerPhysicalFormModel = null;
			}
		} catch (Exception e) {
			logger.error("Error getting to save customer physical form  data");
			e.printStackTrace();
		}

		return ResponseEntity.status(HttpStatus.OK)
				.body(new CustomResponse(responseStatus, message, customerModel));
	}

	/**
	 * @exception Uploade physical form journey customer
	 * @param proposalNumber
	 * @param files
	 * @return ResponseModel<List<FileUploadModel>>
	 */
	@PostMapping("/customer/physical-form")
	public ResponseEntity<ResponseModel<List<FileUploadModel>>> uploadFile(@RequestHeader("Authorization") String token,
			@RequestParam("file") List<MultipartFile> files, @RequestParam("proposalNumber") String proposalNumber,
			@RequestParam("sellerName") String sellerId,@RequestParam(name="roId",required = false) String roId,
			@RequestParam(name="smId",required = false) String smId) {
		ResponseModel<List<FileUploadModel>> response = customerDetailService.savePhysicalFormFile(files,
				proposalNumber, sellerId, roId,smId);
		return ResponseEntity.status(HttpStatus.OK).body(response);
	}
	
	
	
	
	
	
	/**
	 * api to delete physical form document
	 * @exception remove Additional Document
	 * @param proposalNumber
	 * @param fileUrl
	 * @return ResponseModel<String>
	 * @DeleteMapping forntend are not mapping with this thats why it will changes deleteMapping to PostMapping
	 */
	@PostMapping("/customer/remove-physical-form")
	public ResponseEntity<?> removePhysicalDocument(@RequestHeader("Authorization") String token,
			@RequestBody DocumentDeleteModel documentDeleteModel) {
		List<FileUploadModel> listCam = customerDetailService.deletePhysicalFormFile(documentDeleteModel.getUrl(),
				documentDeleteModel.getProposalNumber());
		logger.info("===================File Path Url ==================="+documentDeleteModel.getUrl());
		return ResponseEntity.status(HttpStatus.OK)
				.body(new CustomResponse(Constant.SUCCESS, "Get status Successfully", listCam));
	}

}
