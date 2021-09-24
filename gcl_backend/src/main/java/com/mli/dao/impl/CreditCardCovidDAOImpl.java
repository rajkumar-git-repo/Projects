package com.mli.dao.impl;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.mli.dao.BaseDAO;
import com.mli.dao.CreditCardCovidDAO;
import com.mli.entity.Covid19Entity;
import com.mli.entity.CreditCardCovidEntity;

@Repository
@Transactional
public class CreditCardCovidDAOImpl extends BaseDAO<CreditCardCovidEntity> implements CreditCardCovidDAO{

	public CreditCardCovidDAOImpl() {
		super(CreditCardCovidEntity.class);
	}
	
	
	@Override
	public CreditCardCovidEntity findByCreditCardHealthId(Long creditCardHealthId) {
		Criteria criteria = getSession().createCriteria(getPersistentClass());
		criteria.add(Restrictions.eq("creditCardHealthId.creditCardHealthId", creditCardHealthId));
		return (CreditCardCovidEntity) criteria.uniqueResult();
	}
}
