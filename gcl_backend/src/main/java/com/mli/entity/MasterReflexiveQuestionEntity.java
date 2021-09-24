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
@Table(name = "master_reflexive_question")
public class MasterReflexiveQuestionEntity extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6178030401179931749L;

	@Id
	@Column(name = "reflexive_question_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "tool_tip")
	private String isToolTip;
	
	@Column(name = "tool_tip_description")
	private String toolTipDescription;
	
	@Column(name = "question_type")
	private String qType;

	@OneToMany(cascade = CascadeType.ALL,mappedBy="questionEntity")
	private List<MasterOptionEntity> options;
	
	@ManyToOne
	@JoinColumn(name="reflexive_question_subtype_id")	
	private MasterReflexiveQuestionSubTypeEntity subTypeEntity;

	@Column(name = "question")
	private String question;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public MasterReflexiveQuestionSubTypeEntity getSubTypeEntity() {
		return subTypeEntity;
	}

	public void setSubTypeEntity(MasterReflexiveQuestionSubTypeEntity subTypeEntity) {
		this.subTypeEntity = subTypeEntity;
	}

	public String getqType() {
		return qType;
	}

	public void setqType(String qType) {
		this.qType = qType;
	}

	public List<MasterOptionEntity> getOptions() {
		return options;
	}

	public void setOptions(List<MasterOptionEntity> options) {
		this.options = options;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
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
