package com.mli.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mli.entity.BaseEntity;

/**
 * 
 * @author Devendra.Kumar
 *
 */
public class MasterReflexiveQuestionSubTypeModel extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private Long id;

	private String subTypeValue;

	private String isMendatory;

	private String isToolTip;

	private String toolTipDescription;

	private MasterReflexiveQuestionTypeModel typeEntity;

	private List<MasterReflexiveQuestionModel> questions;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSubTypeValue() {
		return subTypeValue;
	}

	public void setSubTypeValue(String subTypeValue) {
		this.subTypeValue = subTypeValue;
	}

	public String getIsMendatory() {
		return isMendatory;
	}

	public void setIsMendatory(String isMendatory) {
		this.isMendatory = isMendatory;
	}

	@JsonIgnore
	public MasterReflexiveQuestionTypeModel getTypeEntity() {
		return typeEntity;
	}

	public void setTypeEntity(MasterReflexiveQuestionTypeModel typeEntity) {
		this.typeEntity = typeEntity;
	}

	public List<MasterReflexiveQuestionModel> getQuestions() {
		return questions;
	}

	public void setQuestions(List<MasterReflexiveQuestionModel> questions) {
		this.questions = questions;
	}

	public String getIsToolTip() {
		return isToolTip;
	}

	public void setIsToolTip(String isToolTip) {
		this.isToolTip = isToolTip;
	}

	public String getToolTipDescription() {
		return toolTipDescription;
	}

	public void setToolTipDescription(String toolTipDescription) {
		this.toolTipDescription = toolTipDescription;
	}
}
