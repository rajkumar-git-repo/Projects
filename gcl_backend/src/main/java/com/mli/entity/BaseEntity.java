package com.mli.entity;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

/**
 * @author Nikhilesh.Tiwari
 *
 */
@MappedSuperclass
public class BaseEntity extends SerializableEntity {

	private static final long serialVersionUID = 808571484650450927L;

	@Column(name = "created_by", length = 50)
	private String createdBy;

	@Column(name = "created_on", length = 30)
	private Long createdOn;

	@LastModifiedBy
	@Column(name = "modified_by", length = 50)
	private String modifiedBy;

	@LastModifiedDate
	@Column(name = "modified_on", length = 30)
	private Long modifiedOn;

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Long getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Long createdOn) {
		this.createdOn = createdOn;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public Long getModifiedOn() {
		return modifiedOn;
	}

	public void setModifiedOn(Long modifiedOn) {
		this.modifiedOn = modifiedOn;
	}

}
