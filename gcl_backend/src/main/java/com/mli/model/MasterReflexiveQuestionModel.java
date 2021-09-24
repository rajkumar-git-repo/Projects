package com.mli.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mli.entity.BaseEntity;

/**
 * 
 * @author Devendra.Kumar
 *
 */
public class MasterReflexiveQuestionModel extends BaseEntity {

	private Long id;

	private String isToolTip;
	
	private String toolTipDescription;
	
	private String qType;

	private List<MasterOptionModel> options;
	
	private MasterReflexiveQuestionSubTypeModel subTypeEntity;

	private String question;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@JsonIgnore
	public MasterReflexiveQuestionSubTypeModel getSubTypeEntity() {
		return subTypeEntity;
	}

	public void setSubTypeEntity(MasterReflexiveQuestionSubTypeModel subTypeEntity) {
		this.subTypeEntity = subTypeEntity;
	}

	public String getqType() {
		return qType;
	}

	public void setqType(String qType) {
		this.qType = qType;
	}

	public List<MasterOptionModel> getOptions() {
		return options;
	}

	public void setOptions(List<MasterOptionModel> options) {
		this.options = options;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
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
