package com.mli.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "sso_management")
public class SSOManagementEntity {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Version
	@Column(name = "version")
	private long version;

	@Column(name = "ro_id",unique = true)
	private String roId;
	@Column(name = "ro_name")
	private String roName;
	@Column(name = "sm_id",unique = true)
	private String smId;
	@Column(name = "sm_name")
	private String smName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
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
		return "SSOManagement [id=" + id + ", version=" + version + ", roId=" + roId + ", roName=" + roName + ", smId="
				+ smId + ", smName=" + smName + "]";
	}

}
