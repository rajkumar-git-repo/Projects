package com.mli.dao;

import java.util.List;

import com.mli.entity.NomineeDetailsEntity;

public interface NomineeDetailsDAO extends GenericDAO<NomineeDetailsEntity> {

	public NomineeDetailsEntity findByCustomerDtlId(Long customerId);
	
	public List<NomineeDetailsEntity> findByCustomerDtlIds(List<Long> customerId);

}
