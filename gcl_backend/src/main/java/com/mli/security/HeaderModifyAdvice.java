package com.mli.security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.mli.dao.ValidateTokenDAO;
import com.mli.entity.ValidateTokenEntity;
import com.mli.service.ValidateTokenService;

/***
 * @author haripal
 * Refreshing token on API call for session expire.
 * @description Modify response header by refresh token.
 *  
 *
 */

@ControllerAdvice
public class HeaderModifyAdvice implements ResponseBodyAdvice<Object> {
	@Autowired
	JwtTokenUtil jwtTokenUtil;

	@Autowired
	private ValidateTokenDAO validateTokenDAO;
	
	@Autowired
	private ValidateTokenService validateTokenService;
	
	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return true;
	}

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
			Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
			ServerHttpResponse response) {
		if (request.getHeaders().containsKey("Authorization")) {
			String oldToken = request.getHeaders().get("Authorization").get(0);
			String newToken = jwtTokenUtil.refreshToken(oldToken);
			response.getHeaders().add("Authorization", newToken);
			ValidateTokenEntity validateTokenEntity = validateTokenDAO.findByUserName(jwtTokenUtil.getUsernameFromToken(newToken));
			if(!ObjectUtils.isEmpty(validateTokenEntity) && !StringUtils.isEmpty(validateTokenEntity.getAuthToken())) {
			    validateTokenService.saveToken(jwtTokenUtil.getUsernameFromToken(newToken), newToken, request.getRemoteAddress().getAddress().getHostAddress());
			}//response.getHeaders().add("Access-Control-Expose-Headers", "Authorization");
		}
		response.getHeaders().add("Access-Control-Expose-Headers", "fileName");
		return body;
	}

}