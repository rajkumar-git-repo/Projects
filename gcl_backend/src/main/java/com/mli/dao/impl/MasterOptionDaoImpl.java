package com.mli.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mli.dao.BaseDAO;
import com.mli.dao.MasterOptionDao;
import com.mli.entity.MasterOptionEntity;

/**
 * 
 * @author Devendra.Kumar
 *
 */
@Repository
@Transactional
public class MasterOptionDaoImpl  extends BaseDAO<MasterOptionEntity> implements MasterOptionDao{

	public MasterOptionDaoImpl() {
		super(MasterOptionEntity.class);
	}
}
