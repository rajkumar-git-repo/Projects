package com.mli.service.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.mli.dao.ValidateTokenDAO;
import com.mli.entity.ValidateTokenEntity;
import com.mli.service.ValidateTokenService;
import com.mli.utils.DateUtil;


@CrossOrigin
@Service
public class ValidateTokenServiceImpl implements ValidateTokenService{
	
	private static final Logger logger = Logger.getLogger(ValidateTokenServiceImpl.class);

	@Autowired
	private ValidateTokenDAO validateTokenDAO;
	
	/**
	 * method is used to save token based on username
	 */
	@Override
	@Transactional
	public boolean saveToken(String userName, String authToken, String clientIp) {
		try {
			ValidateTokenEntity validateTokenEntity = validateTokenDAO.findByUserName(userName);
			if (!ObjectUtils.isEmpty(validateTokenEntity)) {
				validateTokenEntity.setAuthToken(authToken);
				validateTokenEntity.setModifiedOn(DateUtil.toCurrentUTCTimeStamp());
				validateTokenDAO.saveOrUpdate(validateTokenEntity);
			} else {
				ValidateTokenEntity tokenEntity = new ValidateTokenEntity();
				tokenEntity.setUserName(userName);
				tokenEntity.setClientIp(clientIp);
				tokenEntity.setAuthToken(authToken);
				tokenEntity.setCreatedOn(DateUtil.toCurrentUTCTimeStamp());
				validateTokenDAO.save(tokenEntity);
			}
			logger.info("****TOKEN SAVED SUCCESSFULLY****");
		} catch (Exception e) {
			logger.error("*************Error in saving token details*************" + e);
			e.printStackTrace();
		}
		return true;
	}
	
	
	/**
	 * method is used to update token based on username
	 */
	@Override
	@Transactional
	public boolean updateToken(String userName, String clientIp) {
		try {
			ValidateTokenEntity validateTokenEntity = validateTokenDAO.findByUserName(userName);
			if (!ObjectUtils.isEmpty(validateTokenEntity)) {
				validateTokenEntity.setAuthToken(null);
				validateTokenEntity.setModifiedOn(DateUtil.toCurrentUTCTimeStamp());
				validateTokenDAO.saveOrUpdate(validateTokenEntity);
			}
			logger.info("****TOKEN SAVED SUCCESSFULLY****");
		} catch (Exception e) {
			logger.error("*************Error in saving token details*************" + e);
			e.printStackTrace();
		}
		return true;
	}

}
