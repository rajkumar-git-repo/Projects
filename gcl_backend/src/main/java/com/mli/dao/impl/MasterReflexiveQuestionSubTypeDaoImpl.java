package com.mli.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mli.dao.BaseDAO;
import com.mli.dao.MasterReflexiveQuestionSubTypeDao;
import com.mli.entity.MasterReflexiveQuestionSubTypeEntity;

@Repository
@Transactional
public class MasterReflexiveQuestionSubTypeDaoImpl extends BaseDAO<MasterReflexiveQuestionSubTypeEntity>
		implements MasterReflexiveQuestionSubTypeDao {

	public MasterReflexiveQuestionSubTypeDaoImpl() {
		super(MasterReflexiveQuestionSubTypeEntity.class);
	}

	@Override
	public MasterReflexiveQuestionSubTypeEntity findByRQTypeId(String typeId) {
		Criteria criteria = getSession().createCriteria(getPersistentClass());
		criteria.add(Restrictions.eq("typeValue", typeId));
		return (MasterReflexiveQuestionSubTypeEntity) criteria.uniqueResult();
	}

	@Override
	public MasterReflexiveQuestionSubTypeEntity findSubTypeId(Long subTypeID) {
		Criteria criteria = getSession().createCriteria(getPersistentClass());
		criteria.add(Restrictions.eq("id", subTypeID));
		return (MasterReflexiveQuestionSubTypeEntity) criteria.uniqueResult();
	}
}
