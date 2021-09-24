package com.mli.dao.impl;


import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.mli.dao.BaseDAO;
import com.mli.dao.CreditCardNomineeDAO;
import com.mli.entity.CreditCardNomineeEntity;

@Repository
@Transactional
public class CreditCardNomineeDAOImpl extends BaseDAO<CreditCardNomineeEntity> implements CreditCardNomineeDAO{

	public CreditCardNomineeDAOImpl() {
		super(CreditCardNomineeEntity.class);
	}
	
	@Override
	public CreditCardNomineeEntity findByCustomerDtlId(Long customerId) {
		Criteria criteria = getSession().createCriteria(getPersistentClass());
		criteria.add(Restrictions.eq("creditCardCustomerId.creditCardCustomerId", customerId));
		return (CreditCardNomineeEntity) criteria.uniqueResult();
	}
}
