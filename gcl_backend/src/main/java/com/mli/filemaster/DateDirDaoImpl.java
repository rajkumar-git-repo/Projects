package com.mli.filemaster;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.mli.dao.BaseDAO;

@Repository
public class DateDirDaoImpl extends BaseDAO<DateDirEntity> implements DateDirDao{


	public DateDirDaoImpl() {
		super(DateDirEntity.class);
	}
	@Override
	public DateDirEntity findByDate(String date) {
		Criteria criteria = getSession().createCriteria(getPersistentClass());
		criteria.add(Restrictions.eq("date", date));
		return (DateDirEntity) criteria.setMaxResults(1).uniqueResult();
	}

}