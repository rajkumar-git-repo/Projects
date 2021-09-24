package com.enterprise.rest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.enterprise.exception.AppException;
import com.enterprise.exception.EnterpriseException;
import com.enterprise.model.EOAddress;
import com.enterprise.model.EOPerson;
import com.enterprise.service.EOPersonService;

@Path("/person")
public class EOPersonRest {


    final EOPersonService eoPersonService = new EOPersonService();
	
	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	public void save(String json) {
		eoPersonService.savePerson(json);
	}
	
	@PUT
	@Consumes({MediaType.APPLICATION_JSON})
	public void update(String json) {
		eoPersonService.updatePerson(json);
	}
	
	@DELETE
	@Consumes({MediaType.APPLICATION_JSON})
	public String delete(String json) {
		EOPerson eoPerson = (EOPerson) eoPersonService.deletePerson(json);
		return eoPerson.toString();
	}
	
	@Path("/address")
	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	public String saveAddress(String json) throws AppException {
		EOAddress eoAddress = eoPersonService.saveAddress(json);
		return "Person saved successfully.";
	}
}
