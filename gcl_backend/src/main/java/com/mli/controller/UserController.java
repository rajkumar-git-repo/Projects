package com.mli.controller;

import java.io.IOException;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mli.constants.MLIMessageConstants;
import com.mli.model.response.SaveUpdateResponse;
import com.mli.security.JwtTokenUtil;
import com.mli.service.UserService;
import com.mli.utils.PasswordGenerateUtil;
import com.mli.utils.aes.AESService;

/**
 * 
 * @author Haripal.Chauhan Change password and user related API.
 */
@RestController
public class UserController {
	private static final Logger logger = Logger.getLogger(UserController.class);

	@Value("${jwt.header}")
	private String tokenHeader;

	@Autowired
	private UserService userService;

	@Autowired
	private AESService aesService;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;

// This code for production Plz don't delete 

	/*
	 * @RequestMapping(path = "/user/changepassword", method = RequestMethod.POST,
	 * produces = {
	 * 
	 * @RequestMapping(path = "/user/changepassword", method = RequestMethod.POST,
	 * produces = { MediaType.APPLICATION_JSON_VALUE,
	 * MediaType.APPLICATION_XML_VALUE }) public ResponseEntity<?>
	 * changePswd(@RequestHeader("Authorization") String token, @RequestBody String
	 * body) throws IOException { ObjectMapper mapper = new ObjectMapper();
	 * SaveUpdateResponse errorModel = new
	 * SaveUpdateResponse(SaveUpdateResponse.FAILURE); HashMap<String, String> node;
	 * try { node = mapper.readValue(body, HashMap.class); } catch
	 * (JsonParseException e) { errorModel.setMessage("Invalid Json Data."); return
	 * ResponseEntity.status(HttpStatus.ACCEPTED).body(errorModel); } catch
	 * (JsonMappingException e) {
	 * errorModel.setMessage("Invalid Json Mapping Data."); return
	 * ResponseEntity.status(HttpStatus.ACCEPTED).body(errorModel); } catch
	 * (IOException e) { errorModel.setMessage(e.getMessage()); return
	 * ResponseEntity.status(HttpStatus.ACCEPTED).body(errorModel); } String
	 * userName = jwtTokenUtil.getUsernameFromToken(token); String newPswd =
	 * node.get("newPassword").toString(); SaveUpdateResponse response = null; try {
	 * response = userService.setPassword(userName, newPswd); } catch
	 * (IllegalArgumentException ie) { logger.
	 * error("::::::::::::::::::::   Error in user changepassword    ::::::::::::::::::::::::::"
	 * ); errorModel.setMessage(ie.getMessage()); return
	 * ResponseEntity.status(HttpStatus.ACCEPTED).body(errorModel); } return
	 * ResponseEntity.ok(response); }
	 */
	/**
	 * 
	 * @param token
	 * @param body
	 * @throws IOException USer password change method.
	 */
	@RequestMapping(path = "/user/changepassword", method = RequestMethod.POST, consumes = {
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> changePswdTest(@RequestHeader("Authorization") String token, @RequestBody String body)
			throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		SaveUpdateResponse errorModel = new SaveUpdateResponse(SaveUpdateResponse.FAILURE);
		HashMap<String, String> node;
		try {
			node = mapper.readValue(body, HashMap.class);
		} catch (JsonParseException e) {
			errorModel.setMessage("Invalid Json Data.");
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(errorModel);
		} catch (JsonMappingException e) {
			errorModel.setMessage("Invalid Json Mapping Data.");
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(errorModel);
		} catch (IOException e) {
			errorModel.setMessage(e.getMessage());
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(errorModel);
		}
		String userName = node.get("userName").toString();
		String newPswd = node.get("newPassword").toString();
		SaveUpdateResponse response = null;
		try {
			if (newPswd != null) {
				boolean isValid = PasswordGenerateUtil.getMatchedPassword(newPswd);
				if (isValid) {
					response = userService.setPassword(userName, newPswd);
				} else {
					logger.error("::::::Error in Password validation::::::::");
					errorModel.setMessage(MLIMessageConstants.PASSWORD_VALIDATION);
					return ResponseEntity.status(HttpStatus.ACCEPTED).body(errorModel);
				}
			}
		} catch (IllegalArgumentException ie) {
			logger.error("::::::Error in Change Password::::::::" + ie);
			errorModel.setMessage(ie.getMessage());
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(errorModel);
		}
		return ResponseEntity.ok(response);
	}
	
	/**
	 * api used to change admin password
	 * @param token
	 * @param body
	 * @return
	 * @throws IOException
	 * @author rajkumar
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(path = "/user/admin/changepassword", method = RequestMethod.POST, consumes = {
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> changeAdminPassowrd(@RequestHeader("Authorization") String token, @RequestBody String body)
			throws IOException {
		if (jwtTokenUtil.isTokenExpired(token) || (!jwtTokenUtil.getUserRoleFromToken(token).contains("ROLE_ADMIN"))) {
			return ResponseEntity.badRequest().body(MLIMessageConstants.UNAUTHORIZED_REQUEST);
		}
		ObjectMapper mapper = new ObjectMapper();
		SaveUpdateResponse errorModel = new SaveUpdateResponse(SaveUpdateResponse.FAILURE);
		HashMap<String, String> jsonMap;
		try {
			jsonMap = mapper.readValue(body, HashMap.class);
		} catch (JsonParseException e) {
			errorModel.setMessage("Invalid Json Data.");
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(errorModel);
		} catch (JsonMappingException e) {
			errorModel.setMessage("Invalid Json Mapping Data.");
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(errorModel);
		} catch (IOException e) {
			errorModel.setMessage(e.getMessage());
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(errorModel);
		}
		String userName = jsonMap.get("userName").toString();
		String oldPswd = jsonMap.get("oldPassword").toString();
		String newPswd = jsonMap.get("newPassword").toString();
		SaveUpdateResponse response = null;
		try {
			if (newPswd != null) {
				boolean isValid = PasswordGenerateUtil.getMatchedPassword(newPswd);
				if (isValid) {
					response = userService.changePswd(userName, oldPswd, newPswd);
				} else {
					logger.error("::::::Error in Password validation::::::::");
					errorModel.setMessage(MLIMessageConstants.PASSWORD_VALIDATION);
					return ResponseEntity.status(HttpStatus.ACCEPTED).body(errorModel);
				}
			}
		} catch (IllegalArgumentException ie) {
			logger.error("::::::Error in Change Password::::::::" + ie);
			errorModel.setMessage(ie.getMessage());
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(errorModel);
		}
		return ResponseEntity.ok(response);
	}
}
