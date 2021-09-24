package com.mli.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * 
 * @author Devendra.Kumar
 *
 */
@Entity
@Table(name = "reflexive_answer")
public class ReflexiveAnswerEntity extends BaseEntity {

	@Id
	@Column(name = "reflexive_answer_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToMany(mappedBy="answerEntity")
	private List<CustomerDetailsEntity> customerDetailsEntity;
	
	// Composite key 
	@Column(name = "custId_questionId")
	private String custIdQuestId;
	
	// Composite key, ex: 10_OCCUPATION
	@Column(name = "custId_questionType")
	private String custIdQuestType;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "answerEntity")
	private List<SelectOptionEntity> selectOptionEntities;

	@Column(name = "description")
	private String description;

	@Column(name = "flag")
	private String flag;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<CustomerDetailsEntity> getCustomerDetailsEntity() {
		return customerDetailsEntity;
	}

	public void setCustomerDetailsEntity(List<CustomerDetailsEntity> customerDetailsEntity) {
		this.customerDetailsEntity = customerDetailsEntity;
	}

	public String getCustIdQuestId() {
		return custIdQuestId;
	}

	public void setCustIdQuestId(String custIdQuestId) {
		this.custIdQuestId = custIdQuestId;
	}

	public String getCustIdQuestType() {
		return custIdQuestType;
	}

	public void setCustIdQuestType(String custIdQuestType) {
		this.custIdQuestType = custIdQuestType;
	}

	public List<SelectOptionEntity> getSelectOptionEntities() {
		return selectOptionEntities;
	}

	public void setSelectOptionEntities(List<SelectOptionEntity> selectOptionEntities) {
		this.selectOptionEntities = selectOptionEntities;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}
}
