package com.mli.filemaster;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.mli.dao.BaseDAO;

@Repository
public class BankDirDaoImpl extends BaseDAO<BankDirEntity> implements BankDirDao{


	public BankDirDaoImpl() {
		super(BankDirEntity.class);
	}
	@Override
	public BankDirEntity findByDateDirIdNdBankName(Long dataPathId, String bankName) {
		Criteria criteria = getSession().createCriteria(getPersistentClass());
		criteria.add(Restrictions.eq("bankName", bankName));
		criteria.add(Restrictions.eq("dateDirEntity.id", dataPathId));
		return (BankDirEntity) criteria.setMaxResults(1).uniqueResult();
	}

}

