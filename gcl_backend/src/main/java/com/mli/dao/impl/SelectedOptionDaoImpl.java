package com.mli.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mli.dao.BaseDAO;
import com.mli.dao.SelectedOptionDao;
import com.mli.entity.SelectOptionEntity;

/**
 * 
 * @author Devendra.Kumar
 *
 */
@Repository
@Transactional
public class SelectedOptionDaoImpl extends BaseDAO<SelectOptionEntity> implements SelectedOptionDao {

	public SelectedOptionDaoImpl() {
		super(SelectOptionEntity.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SelectOptionEntity> findByReflexiveAnswerId(Long ansId) {
		Criteria criteria = getSession().createCriteria(SelectOptionEntity.class);
		criteria.add(Restrictions.eq("answerEntity.id", ansId));
		return (List<SelectOptionEntity>)criteria.list();
	}

	@Override
	public SelectOptionEntity findByMasterOptionIdAndAnswerId(Long masterOptionId,Long answerId) {
		Criteria criteria = getSession().createCriteria(SelectOptionEntity.class);
		criteria.add(Restrictions.eq("masterOptionEntity.id", masterOptionId));
		criteria.add(Restrictions.eq("answerEntity.id", answerId));
		return (SelectOptionEntity)criteria.uniqueResult();
	}
}
