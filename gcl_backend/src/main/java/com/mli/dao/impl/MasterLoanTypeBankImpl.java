package com.mli.dao.impl;

import java.util.List;
import java.util.Set;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mli.dao.BaseDAO;
import com.mli.dao.MasterLoanTypeBankDAO;
import com.mli.entity.MasterLoanTypeBankEntity;

@Repository
@Transactional
public class MasterLoanTypeBankImpl extends BaseDAO<MasterLoanTypeBankEntity> implements MasterLoanTypeBankDAO {

	public MasterLoanTypeBankImpl() {
		super(MasterLoanTypeBankEntity.class);
	}

	@Override
	public MasterLoanTypeBankEntity findByUserId(Long id) {
		Criteria criteria = getSession().createCriteria(getPersistentClass());
		criteria.add(Restrictions.eq("id", id));
		return (MasterLoanTypeBankEntity) criteria.uniqueResult();
	}

	@Override
	public List<MasterLoanTypeBankEntity> findByloanTypeIn(Set<String> loanTypes) {
		Criteria criteria = getSession().createCriteria(getPersistentClass());
		criteria.add(Restrictions.in("loanType", loanTypes));
		return (List<MasterLoanTypeBankEntity>) criteria.list();
	}
	
	@Override
	public List<MasterLoanTypeBankEntity> getAllLoanType() {
		Criteria criteria = getSession().createCriteria(getPersistentClass());
		return (List<MasterLoanTypeBankEntity>) criteria.list();
	}
	
	@Override
	public void saveData(MasterLoanTypeBankEntity masterLoanTypeBankEntity) {
		this.save(masterLoanTypeBankEntity);
	}
	
}