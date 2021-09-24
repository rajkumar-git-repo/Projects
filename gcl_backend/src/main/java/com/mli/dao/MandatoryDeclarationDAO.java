package com.mli.dao;

import java.util.List;

import com.mli.entity.MandatoryDeclarationEntity;

public interface MandatoryDeclarationDAO extends GenericDAO<MandatoryDeclarationEntity>{

	public MandatoryDeclarationEntity findbyCustomerDtlId(Long customerId);
	
	public List<MandatoryDeclarationEntity> findbyCustomerDtlIds(List<Long> customerId);

}
