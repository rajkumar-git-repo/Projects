package com.mli.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * 
 * @author Devendra.Kumar
 *
 */
@Entity
@Table(name = "master_reflexive_question_type")
public class MasterReflexiveQuestionTypeEntity extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8967643929450115288L;

	@Id
	@Column(name = "reflexive_question_type_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "reflexive_question_type_value")
	private String typeValue;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "typeEntity")
	private List<MasterReflexiveQuestionSubTypeEntity> subTypeId;

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

	public List<MasterReflexiveQuestionSubTypeEntity> getSubTypeId() {
		return subTypeId;
	}

	public void setSubTypeId(List<MasterReflexiveQuestionSubTypeEntity> subTypeId) {
		this.subTypeId = subTypeId;
	}

	@Override
	public String toString() {
		return "MasterReflexiveQuestionTypeEntity [id=" + id + ", typeValue=" + typeValue + ", subTypeId=" + subTypeId
				+ "]";
	}
}
