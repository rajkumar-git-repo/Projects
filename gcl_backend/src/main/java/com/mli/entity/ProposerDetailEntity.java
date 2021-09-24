package com.mli.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * @author Devendra.Kumar
 *
 */
@Entity
@Table(name = "proprser_details")
public class ProposerDetailEntity extends BaseEntity {
	
	@Id
	@Column(name = "proposer_detail_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long proposerDtlId;

	@Column(name = "proposer_first_name")
	private String proposerFirstName;
	
	@Column(name = "proposer_last_name")
	private String proposerLastName;
	
	@Column(name = "date_of_birth", length = 55)
	private String dateOfBirth;
	
	@Column(name = "gender", length = 20)
	private String gender;
	
	@Column(name = "relationship_with_assured", length = 55)
	private String relationWithAssured;

	@Column(name = "proposer_relationship_with_beneficiary", length = 55)
	private String relationWithProposer;
	
	@OneToOne
	@JoinColumn(name = "customer_id")
	private CustomerDetailsEntity customerDetailsEntity;

	public Long getProposerDtlId() {
		return proposerDtlId;
	}

	public void setProposerDtlId(Long proposerDtlId) {
		this.proposerDtlId = proposerDtlId;
	}

	public String getProposerFirstName() {
		return proposerFirstName;
	}

	public void setProposerFirstName(String proposerFirstName) {
		this.proposerFirstName = proposerFirstName;
	}

	public String getProposerLastName() {
		return proposerLastName;
	}

	public void setProposerLastName(String proposerLastName) {
		this.proposerLastName = proposerLastName;
	}

	public String getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getRelationWithAssured() {
		return relationWithAssured;
	}

	public void setRelationWithAssured(String relationWithAssured) {
		this.relationWithAssured = relationWithAssured;
	}

	public String getRelationWithProposer() {
		return relationWithProposer;
	}

	public void setRelationWithProposer(String relationWithProposer) {
		this.relationWithProposer = relationWithProposer;
	}

	public CustomerDetailsEntity getCustomerDetailsEntity() {
		return customerDetailsEntity;
	}

	public void setCustomerDetailsEntity(CustomerDetailsEntity customerDetailsEntity) {
		this.customerDetailsEntity = customerDetailsEntity;
	}
}
