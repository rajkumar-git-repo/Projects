package com.mli.filemaster;

import java.util.List;

import com.mli.dao.GenericDAO;

public interface SchemeDirDao extends GenericDAO<SchemeDirEntity> {

	SchemeDirEntity findByLoanAppNumberNdDocType(String loanAppNumber, String DocType);

	SchemeDirEntity findByBankDirIdNdSchemeTypeDocType(String loanAppNumber, Long bankPathId, String schemeType, String docType,String proposalNumber);
	
	SchemeDirEntity findByProposalNumberNdeDocType(String proposalNo,String docType);
	
	List<SchemeDirEntity> findByProposalNumber(String proposalNumber);

	SchemeDirEntity findByBankDirIdAndSchemeTypeAndProposalNumber(Long id, String docType, String proposalNumber);

}
