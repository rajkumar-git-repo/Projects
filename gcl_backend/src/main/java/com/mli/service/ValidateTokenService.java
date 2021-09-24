package com.mli.service;

public interface ValidateTokenService {

	public boolean saveToken(String userName,String authToken,String clientIp);

	boolean updateToken(String userName, String clientIp);
}
