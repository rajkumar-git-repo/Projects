package com.mli.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mli.constants.MLIEmailSmsConstants;
import com.mli.constants.MLIMessageConstants;
import com.mli.exception.MobileNumberAlreadyExist;
import com.mli.exception.StorageException;
import com.mli.filter.FileTypeTest;
import com.mli.model.response.ResponseModel;
import com.mli.model.response.SaveUpdateResponse;
import com.mli.security.JwtTokenUtil;
import com.mli.service.UploadCSVDataService;

/**
 * @author Nikhilesh.Tiwari
 * Seller creation by bulk upload in excel sheet.
 */
@RestController
public class UploadCSVDataController {

	private static final Logger logger = Logger.getLogger(UploadCSVDataController.class);

	@Autowired
	private UploadCSVDataService uploadCSVDataService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;


	/**
	 * @param sellerDetails
	 * @throws Exception
	 * Seller creation by bulk upload in excel sheet.
	 */
	@RequestMapping(value = "/upload/excel", method = RequestMethod.POST)
	public ResponseEntity<?> uploadExcelFile(@RequestHeader(value = "Authorization", required = true) String token, @RequestParam("file") MultipartFile file) throws Exception {
		SaveUpdateResponse response = new SaveUpdateResponse();
		try {
			try{
				if(jwtTokenUtil.isTokenExpired(token) || (!jwtTokenUtil.getUserRoleFromToken(token).contains("ROLE_ADMIN"))){
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
				response.setStatus(SaveUpdateResponse.FAILURE);
				response.setMessage(MLIEmailSmsConstants.EMPTY_IMPORT_FILE + file.getOriginalFilename());
				return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
			}
			
			
			
			  FileTypeTest fileTypeTest=new FileTypeTest();
			  if(!fileTypeTest.checkFileExtension(file)) {
			  response.setStatus(SaveUpdateResponse.FAILURE);
			  response.setMessage("Invalid File Uploaded"); return
			  ResponseEntity.status(HttpStatus.OK).body(response); }
			 
			 
			
			ResponseModel<?> userDetail = uploadCSVDataService.uploadSellerInBulk(file);
			return ResponseEntity.ok(userDetail);
		}catch (MobileNumberAlreadyExist e) {
			logger.error("::::::::::Duplicate data in excel file :::::" + e);
			response.setStatus(SaveUpdateResponse.FAILURE);
			response.setMessage(e.getMessage());
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
		}catch (StorageException e) {
			logger.error("::::::::::Error in save upload file:::::" + e);
			response.setStatus(SaveUpdateResponse.FAILURE);
			response.setMessage(MLIMessageConstants.FILE_FORMAT);
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
		}catch (Exception e) {
			logger.error("::::::::::Error in save upload file:::::" + e);
			response.setStatus(SaveUpdateResponse.FAILURE);
			response.setMessage(MLIMessageConstants.EXCEPTION_MSG);
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
		}
	}
	
	/**
	 * Seller status to be update (by Excel)
	 * 
	 * @param token
	 * @param file
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/upload/excel/status", method = RequestMethod.POST)
	public ResponseEntity<?> uploadExcelFileToUpdateSeller(@RequestHeader(value = "Authorization", required = true) String token, @RequestParam("file") MultipartFile file) throws Exception {
		SaveUpdateResponse response = new SaveUpdateResponse();
		logger.info("In uploadExcelFileToUpdateSeller() ");
		try {
			try{
				if(jwtTokenUtil.isTokenExpired(token) || (!jwtTokenUtil.getUserRoleFromToken(token).contains("ROLE_ADMIN"))){
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
				response.setStatus(SaveUpdateResponse.FAILURE);
				response.setMessage(MLIEmailSmsConstants.EMPTY_IMPORT_FILE + file.getOriginalFilename());
				return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
			}
			FileTypeTest fileTypeTest=new FileTypeTest();
			if(!fileTypeTest.checkFileExtension(file)) {
			  response.setStatus(SaveUpdateResponse.FAILURE);
			  response.setMessage("Invalid File Uploaded"); return
			  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response); 
			}
			ResponseModel<?> userDetail = uploadCSVDataService.updateSellerInBulk(file);
			return ResponseEntity.ok(userDetail);
		} catch (MobileNumberAlreadyExist e) {
			logger.error("::::::::::Duplicate data in excel file :::::" + e);
			response.setStatus(SaveUpdateResponse.FAILURE);
			response.setMessage(e.getMessage());
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
		} catch (StorageException e) {
			logger.error("::::::::::Error in save upload file:::::" + e);
			response.setStatus(SaveUpdateResponse.FAILURE);
			response.setMessage(MLIMessageConstants.FILE_FORMAT);
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
		} catch (Exception e) {
			logger.error("::::::::::Error in save upload file:::::" + e);
			response.setStatus(SaveUpdateResponse.FAILURE);
			response.setMessage(MLIMessageConstants.EXCEPTION_MSG);
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
		}
	}
	
	
	/**
	 * api to update sellet in bulk using excel
	 * @param token
	 * @param file
	 * @return
	 * @throws Exception
	 * @author rajkumar
	 */
	@RequestMapping(value = "/update/excel", method = RequestMethod.POST)
	public ResponseEntity<?> updateExcelFile(@RequestHeader(value = "Authorization", required = true) String token, @RequestParam("file") MultipartFile file) throws Exception {
		SaveUpdateResponse response = new SaveUpdateResponse();
		try {
			try{
				if(jwtTokenUtil.isTokenExpired(token) || (!jwtTokenUtil.getUserRoleFromToken(token).contains("ROLE_ADMIN"))){
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
				response.setStatus(SaveUpdateResponse.FAILURE);
				response.setMessage(MLIEmailSmsConstants.EMPTY_IMPORT_FILE + file.getOriginalFilename());
				return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
			}
			
			
			
			  FileTypeTest fileTypeTest=new FileTypeTest();
			  if(!fileTypeTest.checkFileExtension(file)) {
			  response.setStatus(SaveUpdateResponse.FAILURE);
			  response.setMessage("Invalid File Uploaded"); return
			  ResponseEntity.status(HttpStatus.OK).body(response); }
			 
			 
			
			ResponseModel<?> userDetail = uploadCSVDataService.editSellerInBulk(file);
			return ResponseEntity.ok(userDetail);
		}catch (StorageException e) {
			logger.error("::::::::::Error in save upload file:::::" + e);
			response.setStatus(SaveUpdateResponse.FAILURE);
			response.setMessage(MLIMessageConstants.FILE_FORMAT);
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
		}catch (Exception e) {
			logger.error("::::::::::Error in save upload file:::::" + e);
			response.setStatus(SaveUpdateResponse.FAILURE);
			response.setMessage(MLIMessageConstants.EXCEPTION_MSG);
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
		}
	}
}
