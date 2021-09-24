package com.mli.entity;

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
@Table(name = "master_option")
public class MasterOptionEntity extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "value")
	private String value;
	
	@ManyToOne
	@JoinColumn(name="question_id")	
	private MasterReflexiveQuestionEntity questionEntity;
	

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

	public MasterReflexiveQuestionEntity getQuestionEntity() {
		return questionEntity;
	}

	public void setQuestionEntity(MasterReflexiveQuestionEntity questionEntity) {
		this.questionEntity = questionEntity;
	}
}
