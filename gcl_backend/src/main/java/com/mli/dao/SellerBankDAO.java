package com.mli.dao;

import java.util.List;

import com.mli.entity.SellerBankEntity;


public interface SellerBankDAO extends GenericDAO<SellerBankEntity>{
	public List<SellerBankEntity> findBySellerId(Long sellerId);	
	
}
