package com.mli.model;
import java.util.List;

public class TokenModel extends BaseModel{

	private static final long serialVersionUID = 1793903283257204734L;
	    private String token;
	    private List<String> roles;
	    private Long expiredTime;

	    public TokenModel() {}

	    public TokenModel(String token) {
	        this.setToken(token);
	    }
	    public TokenModel(String token, List<String> roles, Long expiredTime) {
	        this.setToken(token);
	        this.setRoles(roles);
	        this.setExpiredTime(expiredTime);
	    }

	    public String getToken() {
	        return this.token;
	    }

	    public void setToken(String token) {
	        this.token = token;
	    }

	    public List<String> getRoles() {
	        return roles;
	    }

	    public void setRoles(List<String> roles) {
	        this.roles = roles;
	    }

		public Long getExpiredTime() {
			return expiredTime;
		}

		public void setExpiredTime(Long expiredTime) {
			this.expiredTime = expiredTime;
		}
	    
}
