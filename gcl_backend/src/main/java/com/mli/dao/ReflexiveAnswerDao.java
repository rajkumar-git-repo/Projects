package com.mli.dao;

import java.util.List;

import com.mli.entity.ReflexiveAnswerEntity;

/**
 * 
 * @author Devendra.Kumar
 *
 */
public interface ReflexiveAnswerDao extends GenericDAO<ReflexiveAnswerEntity> {

	public List<ReflexiveAnswerEntity> findByCustomerId(Long custId);
	
	public ReflexiveAnswerEntity findByQuestionId(Long questionId,Long custId);

	public List<ReflexiveAnswerEntity> findByCustomerIdAndType(Long customerDtlId, String type);
}
