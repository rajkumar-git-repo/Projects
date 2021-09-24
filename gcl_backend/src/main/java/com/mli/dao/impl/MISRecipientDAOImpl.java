package com.mli.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mli.dao.BaseDAO;
import com.mli.dao.MISRecipientDAO;
import com.mli.entity.MISRecipientEntity;
import com.mli.entity.MasterLoanTypeBankEntity;

@Repository
@Transactional
public class MISRecipientDAOImpl extends BaseDAO<MISRecipientEntity> implements MISRecipientDAO{

	public MISRecipientDAOImpl() {
		super(MISRecipientEntity.class);
	}

	@Override
	public MISRecipientEntity findByMPHTypeAndMailType(String mphType, String mailType) {		
			Criteria criteria = getSession().createCriteria(getPersistentClass());
			criteria.add(Restrictions.eq("mphType", mphType));
			criteria.add(Restrictions.eq("mailType", mailType));
			return (MISRecipientEntity) criteria.uniqueResult();
	}

	@Override
	public List<MISRecipientEntity> findByMphType(String mphType) {
		Criteria criteria = getSession().createCriteria(getPersistentClass());
		criteria.add(Restrictions.eq("mphType", mphType));
		return (List<MISRecipientEntity>) criteria.list();
	}
	
	@Override
	public void saveData(MISRecipientEntity misRecipientEntity) {
		this.save(misRecipientEntity);
	}
}
