package com.mli.utils.aes;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AESServiceImpl implements AESService {

	@Value("${app.aes.key}")
	private String secret;
	
	private static final Logger log = LoggerFactory.getLogger(AESServiceImpl.class);

	@Override
	public String decryptData(String cipherText) {
		try {
			String decryptedText = AES256Cryptor.decrypt(cipherText, secret);
			return decryptedText;
		} catch (Exception e) {
			log.error("::::::::::::::::: Error while decryption :::::::::::::::" + e.getMessage());
			System.out.println(e);
		}
		return null;
	}

	@Override
	public String encryptData(String cipherText) {
		try {
			String encryptedText = AES256Cryptor.encrypt(cipherText, secret);
			return encryptedText;
		} catch (Exception e) {
			log.error("::::::::::::::::: Error while encryption :::::::::::::::" + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
}
