package com.mli.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mli.dao.BaseDAO;
import com.mli.dao.Covid19Dao;
import com.mli.entity.Covid19Entity;

@Repository
@Transactional
public class Covid19DAOImpl extends BaseDAO<Covid19Entity> implements Covid19Dao{

	public Covid19DAOImpl() {
		super(Covid19Entity.class);
	}
	
	@Override
	public Covid19Entity findByHealthDtlId(Long healthDtlId) {
		Criteria criteria = getSession().createCriteria(getPersistentClass());
		criteria.add(Restrictions.eq("healthDeclarationEntity.healthDclId", healthDtlId));
		return (Covid19Entity) criteria.uniqueResult();
	}

}
