/**
 * 
 */
package com.enterprise.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

/**
 * @author rajkumar
 *
 */
public class AppExceptionMapper implements ExceptionMapper<Throwable> {

	/*
	 * public Response toResponse(AppException ex) { return
	 * Response.status(ex.getStatus()) .entity(new ErrorMessage(ex))
	 * .type(MediaType.APPLICATION_JSON). build(); }
	 */

	
	@Override
	public Response toResponse(Throwable ex) {

		ErrorMessage errorMessage = new ErrorMessage();		
		setHttpStatus(ex, errorMessage);
		errorMessage.setCode(222);
		errorMessage.setMessage(ex.getMessage());
		StringWriter errorStackTrace = new StringWriter();
		ex.printStackTrace(new PrintWriter(errorStackTrace));
		errorMessage.setDeveloperMessage(errorStackTrace.toString());
		errorMessage.setLink("BLOG_POST_URL");

		return Response.status(errorMessage.getStatus())
				.entity(errorMessage)
				.type(MediaType.APPLICATION_JSON)
				.build();
	}

	private void setHttpStatus(Throwable ex, ErrorMessage errorMessage) {
		if(ex instanceof WebApplicationException ) {
			errorMessage.setStatus(((WebApplicationException)ex).getResponse().getStatus());
		} else {
			errorMessage.setStatus(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode()); //defaults to internal server error 500
		}
	}
}