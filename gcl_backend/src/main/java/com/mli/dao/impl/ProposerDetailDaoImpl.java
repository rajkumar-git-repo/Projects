package com.mli.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mli.dao.BaseDAO;
import com.mli.dao.ProposerDetailDao;
import com.mli.entity.ProposerDetailEntity;

@Repository
@Transactional
public class ProposerDetailDaoImpl extends BaseDAO<ProposerDetailEntity> implements ProposerDetailDao {
	
	public ProposerDetailDaoImpl() {
		super(ProposerDetailEntity.class);
	}

	@Override
	public ProposerDetailEntity findByCustomerDtlId(Long id) {
		Criteria criteria = getSession().createCriteria(getPersistentClass());
		criteria.add(Restrictions.eq("customerDetailsEntity.customerDtlId", id));
		return (ProposerDetailEntity) criteria.uniqueResult();
	}
}
