package com.mli.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mli.dao.BaseDAO;
import com.mli.dao.NomineeDetailsDAO;
import com.mli.entity.NomineeDetailsEntity;

/**
 * @author Nikhilesh.Tiwari
 *
 */
@Repository
@Transactional
public class NomineeDetailsDAOImpl extends BaseDAO<NomineeDetailsEntity> implements NomineeDetailsDAO {

	public NomineeDetailsDAOImpl() {
		super(NomineeDetailsEntity.class);
	}

	@Override
	public NomineeDetailsEntity findByCustomerDtlId(Long customerId) {
		Criteria criteria = getSession().createCriteria(getPersistentClass());
		criteria.add(Restrictions.eq("customerDtlId.customerDtlId", customerId));
		return (NomineeDetailsEntity) criteria.uniqueResult();
	}

	@Override
	public List<NomineeDetailsEntity> findByCustomerDtlIds(List<Long> customerId) {
		Criteria criteria = getSession().createCriteria(getPersistentClass());
		criteria.add(Restrictions.in("customerDtlId.customerDtlId", customerId));
		if (customerId != null && !customerId.isEmpty()) {
			return (List<NomineeDetailsEntity>) criteria.list();
		} else {
			return new ArrayList<>(1);
		}
	}

}
