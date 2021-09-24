package com.mli.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mli.dao.BaseDAO;
import com.mli.dao.ReflexiveAnswerDao;
import com.mli.entity.ReflexiveAnswerEntity;

/**
 * 
 * @author Devendra.Kumar
 *
 */
@Repository
@Transactional
public class ReflexiveAnswerDaoImpl extends BaseDAO<ReflexiveAnswerEntity> implements ReflexiveAnswerDao {

	public ReflexiveAnswerDaoImpl() {
		super(ReflexiveAnswerEntity.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ReflexiveAnswerEntity> findByCustomerId(Long custId) {
		Criteria criteria = getSession().createCriteria(ReflexiveAnswerEntity.class);
		Criteria customerCriteria = criteria.createCriteria("customerDetailsEntity");
		customerCriteria.add(Restrictions.eq("customerDtlId", custId));
		return (List<ReflexiveAnswerEntity>) criteria.list();
	}

	@Override
	public ReflexiveAnswerEntity findByQuestionId(Long questionId,Long custId) {
		Criteria criteria = getSession().createCriteria(ReflexiveAnswerEntity.class);
		Criteria masterQuestCriteria = criteria.createCriteria("masterReflexiveQuestionEntity");
		masterQuestCriteria.add(Restrictions.eq("id", questionId));
		Criteria customerCriteria = criteria.createCriteria("customerDetailsEntity");
		customerCriteria.add(Restrictions.eq("customerDtlId", custId));
		return (ReflexiveAnswerEntity)criteria.uniqueResult();
	}
	
	@Override
	public List<ReflexiveAnswerEntity> findByCustomerIdAndType(Long customerDtlId, String type) {
		String custIdQuestType= customerDtlId+"_"+type;
		Criteria criteria = getSession().createCriteria(ReflexiveAnswerEntity.class);
		criteria.add(Restrictions.eq("custIdQuestType",custIdQuestType));
		Criteria customerCriteria = criteria.createCriteria("customerDetailsEntity");
		customerCriteria.add(Restrictions.eq("customerDtlId", customerDtlId));
		return (List<ReflexiveAnswerEntity>) criteria.list();
	}
}
