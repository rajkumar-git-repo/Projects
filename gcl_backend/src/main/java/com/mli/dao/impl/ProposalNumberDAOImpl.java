package com.mli.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mli.dao.BaseDAO;
import com.mli.dao.ProposalNumberDAO;
import com.mli.entity.ProposalNumberEntity;

/**
 * @author Nikhilesh.Tiwari
 *
 */
@Repository
@Transactional
public class ProposalNumberDAOImpl extends BaseDAO<ProposalNumberEntity> implements ProposalNumberDAO {

	public ProposalNumberDAOImpl() {
		super(ProposalNumberEntity.class);
	}

	@Override
	public ProposalNumberEntity findByProposalNumberId(Long proposalId) {
		Criteria criteria = getSession().createCriteria(getPersistentClass());
		criteria.add(Restrictions.eq("proposalNumberId", proposalId));
		return (ProposalNumberEntity) criteria.uniqueResult();
	}

}
