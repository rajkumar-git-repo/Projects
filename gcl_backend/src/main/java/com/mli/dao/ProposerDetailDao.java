package com.mli.dao;

import com.mli.entity.ProposerDetailEntity;

public interface ProposerDetailDao extends GenericDAO<ProposerDetailEntity> {

	public ProposerDetailEntity findByCustomerDtlId(Long id);
}
