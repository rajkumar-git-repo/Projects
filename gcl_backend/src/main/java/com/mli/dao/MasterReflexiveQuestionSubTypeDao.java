package com.mli.dao;

import com.mli.entity.MasterReflexiveQuestionSubTypeEntity;

public interface MasterReflexiveQuestionSubTypeDao extends GenericDAO<MasterReflexiveQuestionSubTypeEntity>{

	public MasterReflexiveQuestionSubTypeEntity findByRQTypeId(String id);
	
	public MasterReflexiveQuestionSubTypeEntity findSubTypeId(Long subTypeId);
}
