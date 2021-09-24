package com.mli.dao;

import java.util.List;

import com.mli.entity.SFQHealthDeclarationEntity;

public interface SFQHealthDeclarationDAO extends GenericDAO<SFQHealthDeclarationEntity>{

	public SFQHealthDeclarationEntity findByCustomerDtlId(Long customerId);
	
	public List<SFQHealthDeclarationEntity> findByCustomerDtlIds(List<Long> customerId);

}
