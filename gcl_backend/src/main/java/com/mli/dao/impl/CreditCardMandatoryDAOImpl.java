package com.mli.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mli.dao.BaseDAO;
import com.mli.dao.CreditCardMandatoryDAO;
import com.mli.entity.CreditCardMandatoryEntity;

@Repository
@Transactional
public class CreditCardMandatoryDAOImpl extends BaseDAO<CreditCardMandatoryEntity> implements CreditCardMandatoryDAO {

	CreditCardMandatoryDAOImpl(){
		super(CreditCardMandatoryEntity.class);
	}
	
	@Override
	public CreditCardMandatoryEntity findbyCreditCardCustomerId(Long creditCardCustomerId) {
		Criteria criteria = getSession().createCriteria(getPersistentClass());
		criteria.add(Restrictions.eq("creditCardCustomerId.creditCardCustomerId", creditCardCustomerId));
		return (CreditCardMandatoryEntity) criteria.uniqueResult();
	}
}
