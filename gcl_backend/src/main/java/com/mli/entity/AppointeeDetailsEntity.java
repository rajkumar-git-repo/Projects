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
@Table(name = "appointee_details")
public class AppointeeDetailsEntity extends BaseEntity {

	private static final long serialVersionUID = 3812623826608671259L;

	@Id
	@Column(name = "appointee_detail_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long appointeeDtlId;

	@OneToOne
	@JoinColumn(name = "nominee_Id")
	private NomineeDetailsEntity nomineeDtlId;

	@Column(name = "appointee_first_name", length = 60)
	private String appointeeFirstName;
	
	@Column(name = "appointee_last_name", length = 60)
	private String appointeeLastName;

	@Column(name = "date_of_birth", length = 55)
	private String dateOfBirth;

	@Column(name = "gender", length = 20)
	private String gender;

	@NotNull
	@Column(name = "relationship_with_assured", length = 55)
	@Enumerated(EnumType.STRING)
	private RelationshipWithAssured relationshipWithAssured;

	@Column(name = "is_relation_with_appointee")
	private Boolean isrelationWithAppointeeOthers;

	@Column(name = "appointee_relationship_with_beneficiary", length = 55)
	private String relationWithAppointee;
	
	@Version
	@Column(name = "version")
	private long version;

	public Long getAppointeeDtlId() {
		return appointeeDtlId;
	}

	public void setAppointeeDtlId(Long appointeeDtlId) {
		this.appointeeDtlId = appointeeDtlId;
	}

	public NomineeDetailsEntity getNomineeDtlId() {
		return nomineeDtlId;
	}

	public void setNomineeDtlId(NomineeDetailsEntity nomineeDtlId) {
		this.nomineeDtlId = nomineeDtlId;
	}

	public String getAppointeeFirstName() {
		return appointeeFirstName;
	}

	public void setAppointeeFirstName(String appointeeFirstName) {
		this.appointeeFirstName = appointeeFirstName;
	}

	public String getAppointeeLastName() {
		return appointeeLastName;
	}

	public void setAppointeeLastName(String appointeeLastName) {
		this.appointeeLastName = appointeeLastName;
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

	public RelationshipWithAssured getRelationshipWithAssured() {
		return relationshipWithAssured;
	}

	public void setRelationshipWithAssured(RelationshipWithAssured relationshipWithAssured) {
		this.relationshipWithAssured = relationshipWithAssured;
	}

	public Boolean isIsrelationWithAppointeeOthers() {
		return isrelationWithAppointeeOthers;
	}

	public void setIsrelationWithAppointeeOthers(Boolean isrelationWithAppointeeOthers) {
		this.isrelationWithAppointeeOthers = isrelationWithAppointeeOthers;
	}

	public String getRelationWithAppointee() {
		return relationWithAppointee;
	}

	public void setRelationWithAppointee(String relationWithAppointee) {
		this.relationWithAppointee = relationWithAppointee;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

}
