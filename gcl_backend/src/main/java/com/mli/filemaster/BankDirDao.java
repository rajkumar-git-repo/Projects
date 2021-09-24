package com.mli.filemaster;

import com.mli.dao.GenericDAO;

public interface BankDirDao extends GenericDAO<BankDirEntity> {

	BankDirEntity findByDateDirIdNdBankName(Long dataPathId, String bankName);

}
