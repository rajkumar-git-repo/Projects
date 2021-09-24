package com.mli.dao;

import com.mli.entity.MasterReflexiveQuestionTypeEntity;

public interface MasterReflexiveQuestionTypeDao extends GenericDAO<MasterReflexiveQuestionTypeEntity> {

	public MasterReflexiveQuestionTypeEntity findTypeByRQType(String type);
}
