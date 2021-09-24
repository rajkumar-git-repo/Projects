package com.enterprise.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import com.enterprise.model.EOAddress;
import com.enterprise.service.EOAddressService;

@Path("/address")
public class EOAddressRest {


    final EOAddressService eoAddressService = new EOAddressService();
	
	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	public void save(String json) {
		eoAddressService.saveAddress(json);
	}
	
	@PUT
	@Consumes({MediaType.APPLICATION_JSON})
	public void update(String json) {
		eoAddressService.updateAddress(json);
	}
	
	@DELETE
	@Consumes({MediaType.APPLICATION_JSON})
	public String delete(String json) {
		EOAddress eoAddress = (EOAddress) eoAddressService.deleteAddress(json);
		return eoAddress.toString();
	}
}
