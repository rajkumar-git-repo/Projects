package com.mli.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

/**
 * @author nikhilesh.tiwari
 *
 */
@Entity
@Table(name = "ci_rider_ques_master")
public class CIRiderQuestionsMaster extends BaseEntity {

	private static final long serialVersionUID = -6418886563609775513L;

	@Id
	@Column(name = "ci_rider_ques_id", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long ciRiderQuesId;

	@Column(name = "question")
	private String text;

	@Version
	@Column(name = "version")
	private long version;

	public Long getCiRiderQuesId() {
		return ciRiderQuesId;
	}

	public void setCiRiderQuesId(Long ciRiderQuesId) {
		this.ciRiderQuesId = ciRiderQuesId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

}
