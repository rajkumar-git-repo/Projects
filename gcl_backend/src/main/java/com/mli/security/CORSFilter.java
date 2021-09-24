package com.mli.security;
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.MDC;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * 
 * @author Haripal.Chauhan
 * CROSS filter for filtering headers information.
 *
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CORSFilter implements Filter {
	
	@Value("${url.server}")
	private  String serverUrl;

	private final Logger LOG = LoggerFactory.getLogger(CORSFilter.class);
	

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {
			HttpServletResponse response = (HttpServletResponse) res;
			HttpServletRequest request = (HttpServletRequest) req;
			MDC.put("ipAddress", request.getRemoteAddr());
			response.addHeader("Access-Control-Allow-Origin", serverUrl);
			response.addHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS");
			response.addHeader("Access-Control-Max-Age", "3600");
			response.addHeader("Access-Control-Allow-Headers",
					"Origin, X-Requested-With, Content-Range, Content-Disposition, Content-Type, Authorization, X-CSRF-TOKEN, X-XSRF-TOKEN, X-FRAME-OPTIONS");
			response.addHeader("access-control-expose-headers", "Authorization");
			response.addHeader("Content-Security-Policy", "default-src 'self' https://*.maxlifeinsurance.com;script-src 'none'; object-src 'none'");
			response.addHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains");
			response.addHeader("Cache-Control", "no-cache, no-store, max-age=0, must-revalidate");
			response.addHeader("Pragma", "no-cache, no-store, must-revalidate");
			response.addHeader("X-XSS-Protection", "1; mode=block");
			if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
				response.setStatus(HttpServletResponse.SC_OK);
			} else {
				if (request.getHeader("Origin") != null && !request.getHeader("Origin").equals(serverUrl)) {
					response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
				}else {
				    chain.doFilter(req, res);
				}
			}
	}

	@Override
	public void destroy() {
	}

}