/**
 * 
 */
package com.enterprise.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.enterprise.auth.AuthenticationService;

/**
 * @author rajkumar
 *
 */
@WebFilter("/rest/*")
public class AuthFilter implements Filter{

	public static final String AUTHENTICATION_HEADER = "Authorization";
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
            System.out.println("***************Auth filter Inialized***************");		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)throws IOException, ServletException {
		if (request instanceof HttpServletRequest) {
			HttpServletRequest httpServletRequest = (HttpServletRequest) request;
			String authCredentials = httpServletRequest.getHeader(AUTHENTICATION_HEADER);

			AuthenticationService authenticationService = new AuthenticationService();

			boolean authenticationStatus = authenticationService.authenticate(authCredentials);

			if (authenticationStatus) {
				chain.doFilter(request, response);
			} else {
				if (response instanceof HttpServletResponse) {
					HttpServletResponse httpServletResponse = (HttpServletResponse) response;
					httpServletResponse
							.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				}
			}
		}
	}

	@Override
	public void destroy() {
		 System.out.println("***************Auth filter Destroyed***************");				
	}

}
