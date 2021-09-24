package com.mli.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mli.dao.BaseDAO;
import com.mli.dao.LoanTypeSellerDao;
import com.mli.entity.LoanTypeSellerEntity;

@Repository
@Transactional
public class LoanTypeSellerDaoImpl extends BaseDAO<LoanTypeSellerEntity> implements  LoanTypeSellerDao{

	public LoanTypeSellerDaoImpl() {
		super(LoanTypeSellerEntity.class);
	}

	@Override
	public List<LoanTypeSellerEntity> findBySellerId(Long id) {
		Criteria criteria = getSession().createCriteria(LoanTypeSellerEntity.class);
		criteria.add(Restrictions.eq("sellerDetailEntity.sellerDtlId", id));
		return (List<LoanTypeSellerEntity>) criteria.list();
	}

}
