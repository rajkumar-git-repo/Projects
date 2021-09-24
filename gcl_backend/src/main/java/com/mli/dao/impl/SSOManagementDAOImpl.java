package com.mli.dao.impl;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.mli.dao.BaseDAO;
import com.mli.dao.SSOManagementDAO;
import com.mli.entity.SSOManagementEntity;

@Repository
@Transactional
public class SSOManagementDAOImpl extends BaseDAO<SSOManagementEntity> implements SSOManagementDAO {

	public SSOManagementDAOImpl() {
		super(SSOManagementEntity.class);
	}

	@Override
	public SSOManagementEntity findByRoId(String roId) {
		Criteria criteria = getSession().createCriteria(getPersistentClass());
		criteria.add(Restrictions.eq("roId", roId));
		return (SSOManagementEntity) criteria.uniqueResult();
	}

	@Override
	public SSOManagementEntity findBySmId(String smId) {
		Criteria criteria = getSession().createCriteria(getPersistentClass());
		criteria.add(Restrictions.eq("smId", smId));
		return (SSOManagementEntity) criteria.uniqueResult();
	}
}
