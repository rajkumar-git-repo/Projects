package com.mli.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mli.dao.BaseDAO;
import com.mli.dao.CronJobDAO;
import com.mli.entity.CronJobEntity;
import com.mli.enums.CronJobType;

@Repository
@Transactional
public class CronJobDAOImpl extends BaseDAO<CronJobEntity> implements CronJobDAO {

	public CronJobDAOImpl() {
		super(CronJobEntity.class);
	}

	@Override
	public CronJobEntity getCronJobByType(CronJobType cronJobType, boolean status) {		
			Criteria criteria = getSession().createCriteria(getPersistentClass());
			criteria.add(Restrictions.eq("cronJobType", cronJobType));
			criteria.add(Restrictions.eq("status", status));
			return (CronJobEntity) criteria.uniqueResult();
	
	}

	

}
