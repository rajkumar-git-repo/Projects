package com.mli.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mli.dao.BaseDAO;
import com.mli.dao.MasterReflexiveQuestionTypeDao;
import com.mli.entity.MasterReflexiveQuestionTypeEntity;

@Repository
@Transactional
public class MasterReflexiveQuestionTypeDaoImpl extends BaseDAO<MasterReflexiveQuestionTypeEntity>
		implements MasterReflexiveQuestionTypeDao {

	public MasterReflexiveQuestionTypeDaoImpl() {
		super(MasterReflexiveQuestionTypeEntity.class);
	}

	@Override
	public MasterReflexiveQuestionTypeEntity findTypeByRQType(String type) {
		Criteria criteria = getSession().createCriteria(getPersistentClass());
		criteria.add(Restrictions.eq("typeValue", type));
		return (MasterReflexiveQuestionTypeEntity) criteria.uniqueResult();
	}
}
