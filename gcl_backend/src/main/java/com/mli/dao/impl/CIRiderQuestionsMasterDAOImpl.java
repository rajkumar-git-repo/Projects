package com.mli.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mli.dao.BaseDAO;
import com.mli.dao.CIRiderQuestionsMasterDAO;
import com.mli.entity.CIRiderQuestionsMaster;

@Repository
@Transactional
public class CIRiderQuestionsMasterDAOImpl extends BaseDAO<CIRiderQuestionsMaster>
		implements CIRiderQuestionsMasterDAO {

	public CIRiderQuestionsMasterDAOImpl() {
		super(CIRiderQuestionsMaster.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CIRiderQuestionsMaster> getAllQuestions() {
		Criteria criteria = getSession().createCriteria(getPersistentClass());
		return criteria.list();

	}
}
