package com.mli.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.mli.dao.BaseDAO;
import com.mli.dao.SFQHealthDeclarationDAO;
import com.mli.entity.SFQHealthDeclarationEntity;

@Repository
@Transactional
public class SFQHealthDeclarationDAOImpl extends BaseDAO<SFQHealthDeclarationEntity> implements SFQHealthDeclarationDAO {

	public SFQHealthDeclarationDAOImpl() {
		super(SFQHealthDeclarationEntity.class);
	}

	@Override
	public SFQHealthDeclarationEntity findByCustomerDtlId(Long customerId) {
		Criteria criteria = getSession().createCriteria(getPersistentClass());
		criteria.add(Restrictions.eq("customerDtlId.customerDtlId", customerId));
		return (SFQHealthDeclarationEntity) criteria.uniqueResult();
	}

	@Override
	public List<SFQHealthDeclarationEntity> findByCustomerDtlIds(List<Long> customerId) {
		Criteria criteria = getSession().createCriteria(getPersistentClass());
		criteria.add(Restrictions.in("customerDtlId.customerDtlId", customerId));
		if (customerId != null && !customerId.isEmpty()) {
			return (List<SFQHealthDeclarationEntity>) criteria.list();
		} else {
			return new ArrayList<>(1);
		}
	}
}
