package com.mli.service;

import java.util.List;

import com.mli.entity.ReflexiveAnswerEntity;
import com.mli.model.RQModel;
import com.mli.model.RQTypeModel;

public interface ReflexiveQuestionService {

	public String save(RQModel question);
	public RQModel getAll();
	public RQModel getById(String id);
	public Object saveQuestionTypes(RQTypeModel rqTypeModel);
	public Object getQuestionTypes(String typeId);
	public Object getAllQuestion(String stepNo);
	public Object getAllAnswer(String stepNo,Long custId,String proposerNo);
	public Object convertToModelSavedAnswer(List<ReflexiveAnswerEntity> eAnswerEntities);
}
