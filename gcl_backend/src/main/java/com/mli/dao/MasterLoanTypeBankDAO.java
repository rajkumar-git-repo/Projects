package com.mli.dao;

import java.util.List;
import java.util.Set;

import com.mli.entity.MasterLoanTypeBankEntity;

public interface MasterLoanTypeBankDAO extends GenericDAO<MasterLoanTypeBankEntity> {

	MasterLoanTypeBankEntity findByUserId(Long id);

	List<MasterLoanTypeBankEntity> findByloanTypeIn(Set<String> loanTypes);
	
	List<MasterLoanTypeBankEntity> getAllLoanType();

	void saveData(MasterLoanTypeBankEntity masterLoanTypeBankEntity);
}