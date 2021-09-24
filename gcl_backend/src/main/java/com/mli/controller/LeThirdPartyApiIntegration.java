package com.mli.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mli.constants.MLIMessageConstants;
import com.mli.model.response.ResponseModel;
import com.mli.model.response.SaveUpdateResponse;
import com.mli.third.Application;
import com.mli.utils.aes.AESService;

@RestController
public class LeThirdPartyApiIntegration {
	
	@Value("${mli.leThirtPartyApiPath}")
	private  String leThirdPartyApiPathUrl;
	
	@Autowired
	private AESService aesService;
	

	private static final Logger logger = Logger.getLogger(LoginController.class);

	/**
	 * api to calculate premium using LE api
	 * @param token
	 * @param mypojo
	 * @return
	 * @author rajkumar
	 */
	@RequestMapping(value = "/third-party", method = RequestMethod.POST, consumes = {
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> apiCalling(@RequestHeader("Authorization") String token, @RequestBody Application mypojo) {
		String result = "";
		ResponseModel<?> response = new ResponseModel();
		try {
			ObjectMapper mapper = new ObjectMapper();
			mapper.enable(SerializationFeature.INDENT_OUTPUT);
			String json = mapper.writeValueAsString(mypojo);
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> entity = new HttpEntity<String>(json, headers);

			result = restTemplate.postForObject(leThirdPartyApiPathUrl, entity, String.class);

			if (result == null) {
				logger.error("-----------Result is not getting from Third party api(Le)" + result);
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
			}
			if(result.contains(MLIMessageConstants.LE_ERROR)) {
				logger.error("----------Result is not getting from Third party api(Le)" + result);
				response.setStatus(SaveUpdateResponse.FAILURE);
				response.setMessage(MLIMessageConstants.LE_ERROR);
				return ResponseEntity.status(HttpStatus.OK).body(aesService.encryptData(mapper.writeValueAsString(response)));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("-----------Result getting from Third party api(Le)" + result);
		return ResponseEntity.status(HttpStatus.OK).body(aesService.encryptData(result));
	}
}
