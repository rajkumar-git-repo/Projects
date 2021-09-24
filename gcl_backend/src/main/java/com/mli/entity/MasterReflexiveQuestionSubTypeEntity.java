package com.mli.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * 
 * @author Devendra.Kumar
 *
 */
@Entity
@Table(name = "master_reflexive_question_subtype")
public class MasterReflexiveQuestionSubTypeEntity extends BaseEntity {


	private static final long serialVersionUID = 8843076088447257793L;

	@Id
	@Column(name = "reflexive_question_subtype_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "reflexive_question_subtype_value")
	private String subTypeValue;
	
	@Column(name = "mendatory")
	private String isMendatory;
	
	@Column(name = "tool_tip")
	private String isToolTip;
	
	@Column(name = "tool_tip_description")
	private String toolTipDescription;
	
	@ManyToOne
	@JoinColumn(name="reflexive_question_type_id")	
	private MasterReflexiveQuestionTypeEntity typeEntity;
	
	@OneToMany(cascade = CascadeType.ALL,mappedBy="subTypeEntity")
	private List<MasterReflexiveQuestionEntity> questions;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSubTypeValue() {
		return subTypeValue;
	}

	public void setSubTypeValue(String subTypeValue) {
		this.subTypeValue = subTypeValue;
	}

	public String getIsMendatory() {
		return isMendatory;
	}

	public void setIsMendatory(String isMendatory) {
		this.isMendatory = isMendatory;
	}

	public MasterReflexiveQuestionTypeEntity getTypeEntity() {
		return typeEntity;
	}

	public void setTypeEntity(MasterReflexiveQuestionTypeEntity typeEntity) {
		this.typeEntity = typeEntity;
	}

	public List<MasterReflexiveQuestionEntity> getQuestions() {
		return questions;
	}

	public void setQuestions(List<MasterReflexiveQuestionEntity> questions) {
		this.questions = questions;
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
