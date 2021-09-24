package com.mli.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mli.dao.AppointeeDAO;
import com.mli.dao.BaseDAO;
import com.mli.entity.AppointeeDetailsEntity;

/**
 * @author Nikhilesh.Tiwari
 *
 */
@Repository
@Transactional
public class AppointeeDAOImpl extends BaseDAO<AppointeeDetailsEntity> implements AppointeeDAO {

	public AppointeeDAOImpl() {
		super(AppointeeDetailsEntity.class);
	}

	/**
	 * find AppointeeDetailsEntity based on nomineeDtlId
	 * @param nomineeDtlId
	 * @return
	 * @author rajkumar
	 */
	@Override
	public AppointeeDetailsEntity findByNomineeDtlId(Long nomineeDtlId) {
		Criteria criteria = getSession().createCriteria(getPersistentClass());
		criteria.add(Restrictions.eq("nomineeDtlId.nomineeDtlId", nomineeDtlId));
		return (AppointeeDetailsEntity) criteria.uniqueResult();
	}

	/**
	 * find List of AppointeeDetailsEntity based on given list of nomineeDtlId
	 * @param nomineeDtlId
	 * @return
	 * @author rajkumar
	 */
	@Override
	public List<AppointeeDetailsEntity> findByNomineeDtlIds(List<Long> nomineeDtlId) {
		Criteria criteria = getSession().createCriteria(getPersistentClass());
		criteria.add(Restrictions.in("nomineeDtlId.nomineeDtlId", nomineeDtlId));
		if (nomineeDtlId != null && !nomineeDtlId.isEmpty()) {
			return (List<AppointeeDetailsEntity>) criteria.list();
		} else {
			return new ArrayList<>(1);
		}
	}

}
