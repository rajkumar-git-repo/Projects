package com.mli.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mli.dao.BaseDAO;
import com.mli.dao.ValidateTokenDAO;
import com.mli.entity.ValidateTokenEntity;

@Repository
@Transactional
public class ValidateTokenDAOImpl extends BaseDAO<ValidateTokenEntity> implements ValidateTokenDAO{

	public ValidateTokenDAOImpl() {
		super(ValidateTokenEntity.class);
	}
	
	
	@Override
    public ValidateTokenEntity findByUserNameAndClientIp(String userName,String clientIp) {
        Criteria criteria = getSession().createCriteria(getPersistentClass());
        criteria.add(Restrictions.eq("userName", userName));
        criteria.add(Restrictions.eq("clientIp", clientIp));
        return (ValidateTokenEntity) criteria.uniqueResult();
    }
	
	@Override
    public ValidateTokenEntity findByUserName(String userName) {
        Criteria criteria = getSession().createCriteria(getPersistentClass());
        criteria.add(Restrictions.eq("userName", userName));
        return (ValidateTokenEntity) criteria.uniqueResult();
    }

}
