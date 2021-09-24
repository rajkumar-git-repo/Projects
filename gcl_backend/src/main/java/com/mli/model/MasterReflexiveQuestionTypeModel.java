package com.mli.model;

import java.util.List;

import com.mli.entity.BaseEntity;

public class MasterReflexiveQuestionTypeModel extends BaseEntity {

	private Long id;

	private String typeValue;

	private List<MasterReflexiveQuestionSubTypeModel> subTypeId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTypeValue() {
		return typeValue;
	}

	public void setTypeValue(String typeValue) {
		this.typeValue = typeValue;
	}

	public List<MasterReflexiveQuestionSubTypeModel> getSubTypeId() {
		return subTypeId;
	}

	public void setSubTypeId(List<MasterReflexiveQuestionSubTypeModel> subTypeId) {
		this.subTypeId = subTypeId;
	}
}
