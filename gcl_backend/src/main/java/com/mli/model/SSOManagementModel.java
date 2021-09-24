package com.mli.model;

import org.hibernate.validator.constraints.NotEmpty;

public class SSOManagementModel {

	private Long id;
	@NotEmpty
	private String roId;
	@NotEmpty
	private String roName;
	@NotEmpty
	private String smId;
	@NotEmpty
	private String smName;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRoId() {
		return roId;
	}

	public void setRoId(String roId) {
		this.roId = roId;
	}

	public String getRoName() {
		return roName;
	}

	public void setRoName(String roName) {
		this.roName = roName;
	}

	public String getSmId() {
		return smId;
	}

	public void setSmId(String smId) {
		this.smId = smId;
	}

	public String getSmName() {
		return smName;
	}

	public void setSmName(String smName) {
		this.smName = smName;
	}

	@Override
	public String toString() {
		return "SSOManagementModel [id=" + id + ", roId=" + roId + ", roName=" + roName + ", smId=" + smId + ", smName="
				+ smName + "]";
	}
}
