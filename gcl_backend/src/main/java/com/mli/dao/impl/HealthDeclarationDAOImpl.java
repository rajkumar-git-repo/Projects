package com.mli.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mli.dao.BaseDAO;
import com.mli.dao.HealthDeclarationDAO;
import com.mli.entity.HealthDeclarationEntity;

/**
 * @author Nikhilesh.Tiwari
 *
 */
@Repository
@Transactional
public class HealthDeclarationDAOImpl extends BaseDAO<HealthDeclarationEntity> implements HealthDeclarationDAO {

	public HealthDeclarationDAOImpl() {
		super(HealthDeclarationEntity.class);
	}

	@Override
	public HealthDeclarationEntity findByCustomerDtlId(Long customerId) {
		Criteria criteria = getSession().createCriteria(getPersistentClass());
		criteria.add(Restrictions.eq("customerDtlId.customerDtlId", customerId));
		return (HealthDeclarationEntity) criteria.uniqueResult();
	}

	@Override
	public List<HealthDeclarationEntity> findByCustomerDtlIds(List<Long> customerId) {
		Criteria criteria = getSession().createCriteria(getPersistentClass());
		criteria.add(Restrictions.in("customerDtlId.customerDtlId", customerId));
		if (customerId != null && !customerId.isEmpty()) {
			return (List<HealthDeclarationEntity>) criteria.list();
		} else {
			return new ArrayList<>(1);
		}
	}
}
