package com.mli.filter;

import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.mli.utils.aes.AESService;

/**
 * @author baisakhi
 * 
 *         this class main purpose to filter all the responce a encrypt the
 *         responce
 *
 */
@Component
public class CustomURLFilter implements Filter {
	private static final Logger logger = Logger.getLogger(CustomURLFilter.class);
	@Autowired
	private AESService aesService;


	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) servletRequest;
		String path = ((HttpServletRequest) request).getRequestURI();
		HttpServletResponse response = (HttpServletResponse) servletResponse;

		BufferedServletResponseWrapper bufferedResponse = new BufferedServletResponseWrapper(response);

		// call next filter in the filter chain

		List<String> excludeList = new LinkedList<String>();
		excludeList.add("/mli/download/admin");
		excludeList.add("/mli/pdf/download");
		excludeList.add("/mli/cdf/download");
		excludeList.add("/mli/third-party");
		excludeList.add("/mli/downloads/cam-report");
	//	excludeList.add("/mli/customer/search");
		excludeList.add("/mli/seller/premium-calc");
		excludeList.add("/mli/download/seller");
		excludeList.add("/mli/checkAutoMailer");
		excludeList.add("/mli/downloads/physical-form");
		excludeList.add("/mli/seller/loan-list");
		excludeList.add("/mli/payment/logs");
		excludeList.add("/mli/payment/servertoserver");
		excludeList.add("/mli/downloads/physical-file");
		excludeList.add("/mli/card-journey/cron");
		excludeList.add("/mli/downloads/covidReport-zip");
		
		
		if (excludeList.contains(path)) {
			filterChain.doFilter(request, response);
		} else {
			filterChain.doFilter(request, bufferedResponse); // Just continue chain.
			String responseData = bufferedResponse.getResponseData();
			ObjectMapper mapper = new ObjectMapper();
			mapper.enable(SerializationFeature.INDENT_OUTPUT);
			String json = mapper.writeValueAsString(responseData);
			String encryptedResponce = aesService.encryptData(json);
			String des = aesService.decryptData(encryptedResponce);

		//	logger.info("DECRYPTED DATA ======================" + des);
			OutputStream outputStream = response.getOutputStream();
			outputStream.write(encryptedResponce.getBytes());
			outputStream.flush();
			outputStream.close();

		}

	}

	@Override
	public void destroy() {

	}

}
