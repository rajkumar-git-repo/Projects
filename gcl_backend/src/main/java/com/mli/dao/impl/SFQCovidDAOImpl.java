package com.mli.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.mli.dao.BaseDAO;
import com.mli.dao.SFQCovidDAO;
import com.mli.entity.SFQCovidEntity;

@Repository
public class SFQCovidDAOImpl extends BaseDAO<SFQCovidEntity> implements SFQCovidDAO{

	public SFQCovidDAOImpl() {
		super(SFQCovidEntity.class);
	}
	
	@Override
	public SFQCovidEntity findBySFQHealthDtlId(Long healthDtlId) {
		Criteria criteria = getSession().createCriteria(getPersistentClass());
		criteria.add(Restrictions.eq("sfqHealthDeclarationEntity.healthDclId", healthDtlId));
		return (SFQCovidEntity) criteria.uniqueResult();
	}

}
