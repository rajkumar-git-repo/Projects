package com.mli.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mli.model.SSOManagementModel;
import com.mli.model.response.ResponseModel;
import com.mli.service.SSOManagementService;

@RestController
public class SSOManagementController {
	
	@Autowired
	private SSOManagementService ssoManagementService;

	/**
	 * api to create {@link SSOManagementEntity} 
	 * @param ssoManagementModel
	 * @return
	 * @author rajkumar
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/sso/create",method = RequestMethod.POST,consumes = {
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> create(@Valid @RequestBody SSOManagementModel ssoManagementModel){
		ResponseModel<?> responseModel = ssoManagementService.create(ssoManagementModel);
		return ResponseEntity.status(HttpStatus.OK).body(responseModel);
	}
	
	/**
	 * api to update {@link SSOManagementEntity} 
	 * @param ssoManagementModel
	 * @return
	 * @author rajkumar
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/sso/update",method = RequestMethod.PUT,consumes = {
			MediaType.APPLICATION_JSON_VALUE }, produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> update(@Valid @RequestBody SSOManagementModel ssoManagementModel){
		ResponseModel<?> responseModel = ssoManagementService.update(ssoManagementModel);
		return ResponseEntity.status(HttpStatus.OK).body(responseModel);
	}
	
	/**
	 * api to get {@link SSOManagementEntity} based on primary key
	 * @param id
	 * @return
	 * @author rajkumar
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/sso/delete/{id}",method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable("id") Long id){
		ResponseModel<?> responseModel = ssoManagementService.delete(id);
		return ResponseEntity.status(HttpStatus.OK).body(responseModel);
	}
	
	/**
	 * api to get all {@link SSOManagementEntity}  object
	 * @return
	 * @author rajkumar
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/sso/getAll",method = RequestMethod.GET)
	public ResponseEntity<?> getAll(){
		return ResponseEntity.status(HttpStatus.OK).body(ssoManagementService.getAll());
	}
	
	/**
	 * api to get {@link SSOManagementEntity} based on roId
	 * @param roId
	 * @return
	 * @author rajkumar
	 */
	@RequestMapping(value = "/sso/roId",method = RequestMethod.GET)
	public ResponseEntity<?> findByRoId(@RequestParam(value = "roId",required = true) String roId){
		return ResponseEntity.status(HttpStatus.OK).body(ssoManagementService.findByRoId(roId));
	}
	
	/**
	 * api to get {@link SSOManagementEntity} based on smId
	 * @param smId
	 * @return
	 * @author rajkumar
	 */
	@RequestMapping(value = "/sso/smId",method = RequestMethod.GET)
	public ResponseEntity<?> findBySmId(@RequestParam(value = "smId",required = true) String smId){
		return ResponseEntity.status(HttpStatus.OK).body(ssoManagementService.findBySmId(smId));
	}
}

