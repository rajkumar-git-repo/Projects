package com.mli.filemaster;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.mli.dao.BaseDAO;

@Repository
public class SchemeDirDaoImpl extends BaseDAO<SchemeDirEntity> implements SchemeDirDao{

	private static final Logger logger = Logger.getLogger(SchemeDirDaoImpl.class);

	
	public SchemeDirDaoImpl() {
		super(SchemeDirEntity.class);
	}
	@Override
	public SchemeDirEntity findByLoanAppNumberNdDocType(String loanAppNumber, String DocType) {
		Criteria criteria = getSession().createCriteria(getPersistentClass());
		criteria.add(Restrictions.eq("loanAppNo", loanAppNumber));
		criteria.add(Restrictions.eq("docType", DocType));
		return (SchemeDirEntity) criteria.uniqueResult();
	}
	
	@Override
	public SchemeDirEntity findByBankDirIdNdSchemeTypeDocType(String loanAppNumber, Long bankPathId, String schemeType, String docType,String proposalNumebr) {
		Criteria criteria = getSession().createCriteria(getPersistentClass());
		criteria.add(Restrictions.eq("bankDirEntity.id", bankPathId));
		criteria.add(Restrictions.eq("schemeName", schemeType));
		criteria.add(Restrictions.eq("docType", docType));
		criteria.add(Restrictions.eq("loanAppNo", loanAppNumber));
		criteria.add(Restrictions.eq("proposalNumber", proposalNumebr));
		return (SchemeDirEntity) criteria.uniqueResult();
	}
	@Override
	public SchemeDirEntity findByProposalNumberNdeDocType(String proposalNo, String docType) {
		try {
			Criteria criteria = getSession().createCriteria(getPersistentClass());
			criteria.add(Restrictions.eq("proposalNumber", proposalNo));
			criteria.add(Restrictions.eq("docType", docType));
			return (SchemeDirEntity) criteria.uniqueResult();
		} catch (Exception e) {
			logger.error("-----------getting error here ----------");
			return null;
		}
	}
	@Override
	public List<SchemeDirEntity> findByProposalNumber(String proposalNumber) {
		Criteria criteria = getSession().createCriteria(getPersistentClass());
		criteria.add(Restrictions.eq("proposalNumber", proposalNumber));
		return (List<SchemeDirEntity>)criteria.list();
	}
	
	@Override
	public SchemeDirEntity findByBankDirIdAndSchemeTypeAndProposalNumber(Long bankPathId, String docType, String proposalNumber) {
		Criteria criteria = getSession().createCriteria(getPersistentClass());
		criteria.add(Restrictions.eq("bankDirEntity.id", bankPathId));
		criteria.add(Restrictions.eq("docType", docType));
		criteria.add(Restrictions.eq("proposalNumber", proposalNumber));
		return (SchemeDirEntity) criteria.uniqueResult();
	}

}