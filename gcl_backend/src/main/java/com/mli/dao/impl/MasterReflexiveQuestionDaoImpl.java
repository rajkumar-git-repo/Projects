package com.mli.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mli.dao.BaseDAO;
import com.mli.dao.MasterReflexiveQuestionDao;
import com.mli.entity.MasterReflexiveQuestionEntity;

/**
 * 
 * @author Devendra.Kumar
 *
 */
@Repository
@Transactional
public class MasterReflexiveQuestionDaoImpl extends BaseDAO<MasterReflexiveQuestionEntity>
		implements MasterReflexiveQuestionDao {

	public MasterReflexiveQuestionDaoImpl() {
		super(MasterReflexiveQuestionEntity.class);
	}

	@Override
	public List<MasterReflexiveQuestionEntity> findByType(String type) {
		Criteria criteria = getSession().createCriteria(MasterReflexiveQuestionEntity.class);
		Criteria subTypeCriteria = criteria.createCriteria("subTypeEntity");
//		Criterion qSubTypeCr1 = Restrictions.eq("subTypeEntity.subTypeValue", Constant.DIABETES);
//		Criterion qSubTypeCr2 = Restrictions.eq("subTypeEntity.subTypeValue", Constant.HYPERTENSION);
//		LogicalExpression orExp = Restrictions.or(qSubTypeCr1, qSubTypeCr2);
//		subTypeCriteria.add(orExp);
		Criteria typeCriteria = subTypeCriteria.createCriteria("typeEntity");
		typeCriteria.add(Restrictions.eq("typeValue", type));
		return (List<MasterReflexiveQuestionEntity>) typeCriteria.list();
	}

	@Override
	public MasterReflexiveQuestionEntity findByQuestion(String question) {
		Criteria criteria = getSession().createCriteria(MasterReflexiveQuestionEntity.class);
		criteria.add(Restrictions.eq("question", question));
		return (MasterReflexiveQuestionEntity) criteria.uniqueResult();
	}
}
