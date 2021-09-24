package com.mli.dao.impl;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.mli.dao.BaseDAO;
import com.mli.dao.CreditCardHealthDAO;
import com.mli.entity.CreditCardHealthEntity;

@Repository
@Transactional
public class CreditCardHealthDAOImpl  extends BaseDAO<CreditCardHealthEntity> implements CreditCardHealthDAO{

	public CreditCardHealthDAOImpl() {
		super(CreditCardHealthEntity.class);
	}

	@Override
	public CreditCardHealthEntity findByCustomerDtlId(Long customerId) {
		Criteria criteria = getSession().createCriteria(getPersistentClass());
		criteria.add(Restrictions.eq("creditCardCustomerId.creditCardCustomerId", customerId));
		return (CreditCardHealthEntity) criteria.uniqueResult();
	}
}
