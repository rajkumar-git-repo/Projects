package com.mli.dao;

import java.util.List;

import com.mli.entity.HealthDeclarationEntity;

/**
 * @author Nikhilesh.Tiwari
 *
 */
public interface HealthDeclarationDAO extends GenericDAO<HealthDeclarationEntity>{

	public HealthDeclarationEntity findByCustomerDtlId(Long customerId);
	
	public List<HealthDeclarationEntity> findByCustomerDtlIds(List<Long> customerId);

}
