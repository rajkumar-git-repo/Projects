package com.mli.model;

import com.mli.entity.BaseEntity;

/**
 * 
 * @author Devendra.Kumar
 *
 */
public class MasterOptionModel extends BaseEntity {

	private Long id;

	private String value;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
