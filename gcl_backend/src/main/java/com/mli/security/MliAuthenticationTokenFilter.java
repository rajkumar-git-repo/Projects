package com.mli.security;


import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.mli.dao.ValidateTokenDAO;
import com.mli.entity.ValidateTokenEntity;
import com.mli.model.CustomUserDetail;
/**
 * 
 * @author Haripal.Chauhan
 * Setting authentication details in  SecurityContextHolder if  SecurityContextHolder.getContext().getAuthentication() is null.
 *
 */
public class MliAuthenticationTokenFilter extends UsernamePasswordAuthenticationFilter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    
	@Autowired
	private ValidateTokenDAO validateTokenDAO;

    @Value("${jwt.header}")
    private String tokenHeader;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String authToken = httpRequest.getHeader(this.tokenHeader);
        if(null != authToken) {
            String username = jwtTokenUtil.getUsernameFromToken(authToken);

			if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
				ValidateTokenEntity validateTokenEntity = validateTokenDAO.findByUserName(username);
				if (!ObjectUtils.isEmpty(validateTokenEntity)
						&& !StringUtils.isEmpty(validateTokenEntity.getAuthToken())) {
					if (username.equals(jwtTokenUtil.getUsernameFromToken(validateTokenEntity.getAuthToken()))) {
						CustomUserDetail userDetails = (CustomUserDetail) userDetailsService
								.loadUserByUsername(username);
						if (userDetails != null && jwtTokenUtil.validateToken(authToken, userDetails)) {
							UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
									userDetails, null, userDetails.getAuthorities());
							authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
							SecurityContextHolder.getContext().setAuthentication(authentication);
						}
					}
				}
			}
        }
        chain.doFilter(request, response);
       
    }
}
