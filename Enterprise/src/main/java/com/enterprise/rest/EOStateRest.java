package com.enterprise.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import com.enterprise.model.EOState;
import com.enterprise.service.EOStateService;

@Path("/state")
public class EOStateRest {

	final EOStateService eoStateService = new EOStateService();
	
	@POST
	@Consumes({MediaType.APPLICATION_JSON})
	public void save(String json) {
		eoStateService.saveState(json);
	}
	
	@PUT
	@Consumes({MediaType.APPLICATION_JSON})
	public void update(String json) {
		eoStateService.updateState(json);
	}
	
	@DELETE
	@Consumes({MediaType.APPLICATION_JSON})
	public String delete(String json) {
		EOState eoState = (EOState) eoStateService.deleteState(json);
		return eoState.toString();
	}
	
}
