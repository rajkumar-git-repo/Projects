package com.mli.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.mli.dao.BaseDAO;
import com.mli.dao.SellerBankDAO;
import com.mli.entity.SellerBankEntity;

public class SellerBankDAOImpl extends BaseDAO<SellerBankEntity> implements SellerBankDAO{
	public SellerBankDAOImpl() {
		super(SellerBankEntity.class);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SellerBankEntity> findBySellerId(Long sellerId) {
		Criteria criteria = getSession().createCriteria(getPersistentClass());
		criteria.add(Restrictions.eq("sellerDetailEntity.sellerDtlId", sellerId));
		return (List<SellerBankEntity>) criteria.list();
	}
	
	
}
