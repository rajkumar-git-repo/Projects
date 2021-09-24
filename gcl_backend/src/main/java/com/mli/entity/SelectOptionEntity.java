package com.mli.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 
 * @author Devendra.Kumar
 *
 */
@Entity
@Table(name = "selected_option")
public class SelectOptionEntity extends BaseEntity {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	// composite key
	@Column(name = "custId_master_opId")
	private String custIdMasterOpId;
	
	@ManyToOne(cascade=CascadeType.ALL)
	@JoinColumn(name = "answer_id")
	private ReflexiveAnswerEntity answerEntity;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCustIdMasterOpId() {
		return custIdMasterOpId;
	}

	public void setCustIdMasterOpId(String custIdMasterOpId) {
		this.custIdMasterOpId = custIdMasterOpId;
	}

	public ReflexiveAnswerEntity getAnswerEntity() {
		return answerEntity;
	}

	public void setAnswerEntity(ReflexiveAnswerEntity answerEntity) {
		this.answerEntity = answerEntity;
	}
}
