package com.mli.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mli.dao.BaseDAO;
import com.mli.dao.MandatoryDeclarationDAO;
import com.mli.entity.MandatoryDeclarationEntity;

/**
 * @author Nikhilesh.Tiwari
 *
 */
@Repository
@Transactional
public class MandatoryDeclarationDAOImpl extends BaseDAO<MandatoryDeclarationEntity>
		implements MandatoryDeclarationDAO {

	public MandatoryDeclarationDAOImpl() {
		super(MandatoryDeclarationEntity.class);
	}

	@Override
	public MandatoryDeclarationEntity findbyCustomerDtlId(Long customerId) {
		Criteria criteria = getSession().createCriteria(getPersistentClass());
		criteria.add(Restrictions.eq("customerDtlId.customerDtlId", customerId));
		return (MandatoryDeclarationEntity) criteria.uniqueResult();
	}

	@Override
	public List<MandatoryDeclarationEntity> findbyCustomerDtlIds(List<Long> customerId) {
		Criteria criteria = getSession().createCriteria(getPersistentClass());
		criteria.add(Restrictions.in("customerDtlId.customerDtlId", customerId));
		if(customerId != null && !customerId.isEmpty()) {
			return (List<MandatoryDeclarationEntity>) criteria.list();
		} else {
			return new ArrayList<>(1);
		}
	}

}
