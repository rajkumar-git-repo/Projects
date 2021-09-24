package com.mli.dao;

import java.util.List;

import com.mli.entity.MasterReflexiveQuestionEntity;

/**
 * 
 * @author Devendra.Kumar
 *
 */
public interface MasterReflexiveQuestionDao extends GenericDAO<MasterReflexiveQuestionEntity> {

	public List<MasterReflexiveQuestionEntity> findByType(String type);
	
	public MasterReflexiveQuestionEntity findByQuestion(String question);
}
