package com.mli.dao;

import java.util.List;

import com.mli.entity.LoanTypeSellerEntity;

public interface LoanTypeSellerDao extends  GenericDAO<LoanTypeSellerEntity>{

	public List<LoanTypeSellerEntity> findBySellerId(Long id);

}
