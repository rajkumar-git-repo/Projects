package com.mli.dao;

import com.mli.entity.ValidateTokenEntity;

public interface ValidateTokenDAO extends GenericDAO<ValidateTokenEntity>{

	ValidateTokenEntity findByUserNameAndClientIp(String userName, String clientIp);

	ValidateTokenEntity findByUserName(String userName);

}
