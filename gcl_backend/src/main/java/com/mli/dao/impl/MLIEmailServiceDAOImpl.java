package com.mli.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mli.dao.BaseDAO;
import com.mli.dao.MLIEmailServiceDAO;
import com.mli.entity.MliEmailEntity;

@Repository
@Transactional
public class MLIEmailServiceDAOImpl extends BaseDAO<MliEmailEntity> implements MLIEmailServiceDAO {

	public MLIEmailServiceDAOImpl() {
		super(MliEmailEntity.class);
	}

}
