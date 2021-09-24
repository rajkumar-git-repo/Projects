package com.mli.security;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.mli.model.CustomUserDetail;
import com.mli.model.LoginModel;
import com.mli.model.TokenModel;
import com.mli.utils.AES;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
/**
 * 
 * @author Haripal.Chauhan
 * JWT token utility methods.
 *
 */
@Component
public class JwtTokenUtil implements Serializable {

    private static final long serialVersionUID = -3787799406661972082L;
    private static final String CLAIM_KEY_USERNAME = "sub";
    private static final String CLAIM_KEY_SCOPE = "scope";
    private static final String CLAIM_KEY_JTI = "jti";
    private static final String CLAIM_KEY_CREATED = "created";
    private static final String CLAIM_LIST_DELIMITERS = ",";

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.claim.secret}")
    private String claimSecret;

    @Value("${jwt.expiration}")
    private Long expiration;
    
    @Value("${mli.aes.encryption.key}")
	private String aesSecratKey;
    
    @Autowired
	private UserDetailsService userDetailsService;

    public String getUsernameFromToken(String token) {
        String username = null;
        try {
            final Claims claims = getClaimsFromToken(token);
            if(claims != null){            	
            	username = AES.decrypt(MliCryptoUtil.parseSecureToken(claims.getSubject(), claimSecret),aesSecratKey);
            }
        } catch (Exception e) {
        }
        return username;
    }

    public Date getCreatedDateFromToken(String token) {
        Date created = null;
        try {
            final Claims claims = getClaimsFromToken(token);
            if(claims != null){
            	created = new Date((Long) claims.get(CLAIM_KEY_CREATED));            	
            }
        } catch (Exception e) {
        }
        return created;
    }

    public Date getExpirationDateFromToken(String token) {
        Date expiration = null;
        try {
            final Claims claims = getClaimsFromToken(token);
            if(claims != null){            	
            	expiration = claims.getExpiration();
            }
        } catch (Exception e) {
        }
        return expiration;
    }

    public Long getUserIdFromToken(String token) {
        Long userId = null;
        try {
            final Claims claims = getClaimsFromToken(token);
            String userIdStr = null;
            if(claims != null){
            	userIdStr = AES.decrypt(MliCryptoUtil.parseSecureToken(claims.getId(), claimSecret),aesSecratKey);
            }
            if (!StringUtils.isEmpty(userIdStr)) {
                userId = Long.parseLong(userIdStr);
            }
        } catch (Exception e) {
        }
        return userId;
    }

    public List<String> getUserRoleFromToken(String token) {
        List<String> roles = null;
        try {
            final Claims claims = getClaimsFromToken(token);
            String roleStr = null;
            if(claims != null){
            	roleStr = AES.decrypt(MliCryptoUtil.parseSecureToken((String) claims.get(CLAIM_KEY_SCOPE), claimSecret),aesSecratKey);
            	roles = Arrays.asList(roleStr.split(CLAIM_LIST_DELIMITERS));
            }
        } catch (Exception e) {
        }
        return roles;
    }

    private Claims getClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + expiration * 1000);
    }

    public Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        if(expiration == null) {
        	return Boolean.TRUE;
        }
        return expiration.before(new Date());
    }
    
    public String generateToken(CustomUserDetail userDetails) {
        Map<String, Object> claims = new HashMap<>();

        /*claims.put(CLAIM_KEY_JTI, MliCryptoUtil.generateSecureToken(String.valueOf(userDetails.getId()), claimSecret));
        claims.put(CLAIM_KEY_USERNAME, MliCryptoUtil.generateSecureToken(userDetails.getUsername(), claimSecret));
        String roles = userDetails.getUserRoles().stream().map(i -> i.toString())
                .collect(Collectors.joining(CLAIM_LIST_DELIMITERS));
        claims.put(CLAIM_KEY_SCOPE, MliCryptoUtil.generateSecureToken(roles, claimSecret));
        claims.put(CLAIM_KEY_CREATED, new Date());*/
    	
        claims.put(CLAIM_KEY_JTI, AES.encrypt(MliCryptoUtil.generateSecureToken(String.valueOf(userDetails.getId()), claimSecret), aesSecratKey));
        claims.put(CLAIM_KEY_USERNAME, AES.encrypt(MliCryptoUtil.generateSecureToken(userDetails.getUsername(), claimSecret), aesSecratKey));
        String roles = userDetails.getUserRoles().stream().map(i -> i.toString())
                .collect(Collectors.joining(CLAIM_LIST_DELIMITERS));
        claims.put(CLAIM_KEY_SCOPE, AES.encrypt(MliCryptoUtil.generateSecureToken(roles, claimSecret), aesSecratKey));
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }

    private String generateToken(Map<String, Object> claims) {
        return Jwts.builder().setClaims(claims).setExpiration(generateExpirationDate())
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    public String refreshToken(String token) {
        String refreshedToken = null;
        try {
            final Claims claims = getClaimsFromToken(token);
            if(claims != null){            	
            	claims.put(CLAIM_KEY_CREATED, new Date());
            	refreshedToken = generateToken(claims);
            }
        } catch (Exception e) {
        }
        return refreshedToken;
    }

    public Boolean validateToken(String token, CustomUserDetail userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    } 
    
    public TokenModel getTokenModelWhenLoginWithOTP(LoginModel loginModel) {
    	CustomUserDetail userDetails =
				(CustomUserDetail) userDetailsService.loadUserByUsername(loginModel.getUsername());
		final String token = generateToken(userDetails);
		return new TokenModel(token, userDetails.getUserRoles(), expiration);
       }
}
