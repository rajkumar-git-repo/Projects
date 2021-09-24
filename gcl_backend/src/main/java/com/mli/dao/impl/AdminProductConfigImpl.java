package com.mli.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mli.dao.AdminProductConfigDao;
import com.mli.dao.BaseDAO;
import com.mli.entity.AdminProductConfigEntity;
import com.mli.entity.AppointeeDetailsEntity;
import com.mli.model.AdminProductConfigModel;

@Repository
@Transactional
public class AdminProductConfigImpl extends BaseDAO<AdminProductConfigEntity> implements AdminProductConfigDao{
	
	
	public AdminProductConfigImpl() {
		super(AdminProductConfigEntity.class);
	}

	/**
	 * fetch AdminProductConfigEntity based on bank name
	 * @param bankName
	 * @return
	 * @author rajkumar
	 */
	@Override
	public AdminProductConfigEntity findByBankName(String bankName) {
		Criteria criteria = getSession().createCriteria(getPersistentClass());
		criteria.add(Restrictions.eq("bankName", bankName));
		return (AdminProductConfigEntity) criteria.uniqueResult();
	}
}
