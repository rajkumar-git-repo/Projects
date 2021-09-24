package com.mli.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import com.mli.enums.RelationshipWithAssured;

/**
 * @author Nikhilesh.Tiwari
 *
 */

@Entity
@Table(name = "nominee_details")
public class NomineeDetailsEntity extends BaseEntity {

	private static final long serialVersionUID = 4988812386612046716L;

	@Id
	@Column(name = "nominee_detail_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long nomineeDtlId;

	@OneToOne
	@NotNull
	@JoinColumn(name = "cust_detail_id")
	private CustomerDetailsEntity customerDtlId;

	@NotNull
	@Column(name = "nominee_first_name", length = 60)
	private String nomineeFirstName;
	
	@Column(name = "nominee_last_name", length = 60)
	private String nomineeLastName;

	@NotNull
	@Column(name = "date_of_birth", length = 55)
	private String dateOfBirth;

	@NotNull
	@Column(name = "gender", length = 20)
	private String gender;

	@NotNull
	@Column(name = "relationship_with_assured", length = 55)
	@Enumerated(EnumType.STRING)
	private RelationshipWithAssured relationshipWithAssured;

	@Version
	@Column(name = "version")
	private long version;

	@Column(name = "relation_with_nominee", length = 55)
	private String relationWithNominee;

	@Column(name = "is_appointee" )
	private Boolean isAppointee;

	public Long getNomineeDtlId() {
		return nomineeDtlId;
	}

	public void setNomineeDtlId(Long nomineeDtlId) {
		this.nomineeDtlId = nomineeDtlId;
	}

	public CustomerDetailsEntity getCustomerDtlId() {
		return customerDtlId;
	}

	public void setCustomerDtlId(CustomerDetailsEntity customerDtlId) {
		this.customerDtlId = customerDtlId;
	}

	public String getNomineeFirstName() {
		return nomineeFirstName;
	}

	public void setNomineeFirstName(String nomineeFirstName) {
		this.nomineeFirstName = nomineeFirstName;
	}

	public String getNomineeLastName() {
		return nomineeLastName;
	}

	public void setNomineeLastName(String nomineeLastName) {
		this.nomineeLastName = nomineeLastName;
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

	public Boolean isAppointee() {
		return isAppointee;
	}

	public void setAppointee(Boolean isAppointee) {
		this.isAppointee = isAppointee;
	}

	public RelationshipWithAssured getRelationshipWithAssured() {
		return relationshipWithAssured;
	}

	public void setRelationshipWithAssured(RelationshipWithAssured relationshipWithAssured) {
		this.relationshipWithAssured = relationshipWithAssured;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	public String getRelationWithNominee() {
		return relationWithNominee;
	}

	public void setRelationWithNominee(String relationWithNominee) {
		this.relationWithNominee = relationWithNominee;
	}

}
