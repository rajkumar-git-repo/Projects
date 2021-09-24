package com.mli.dao.impl;

import org.springframework.transaction.annotation.Transactional;

import com.mli.dao.BaseDAO;
import com.mli.dao.UploadCSVDataDAO;
import com.mli.entity.SellerDetailEntity;

@Transactional
public class UploadCSVDataDAOImpl extends BaseDAO<SellerDetailEntity> implements UploadCSVDataDAO {

	public UploadCSVDataDAOImpl() {
		super(SellerDetailEntity.class);
	}

}
