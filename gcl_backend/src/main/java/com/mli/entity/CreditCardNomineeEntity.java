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

@Entity
@Table(name = "creditcard_nominee")
public class CreditCardNomineeEntity extends BaseEntity{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "creditcard_nominee_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long creditCardNomineeId;
	
	@Version
	@Column(name = "version")
	private long version;

	@OneToOne
	@NotNull
	@JoinColumn(name = "creditcard_customer_id")
	private CreditCardCustomerEntity creditCardCustomerId;

	@NotNull
	@Column(name = "nominee_first_name", length = 60)
	private String nomineeFirstName;
	
	@Column(name = "nominee_last_name", length = 60)
	private String nomineeLastName;

	@NotNull
	@Column(name = "nominee_dob", length = 55)
	private String nomineeDob;

	@NotNull
	@Column(name = "nominee_gender", length = 20)
	private String nomineeGender;

	@NotNull
	@Column(name = "relationship_with_assured", length = 55)
	@Enumerated(EnumType.STRING)
	private RelationshipWithAssured relationshipWithAssured;

	@Column(name = "is_appointee" )
	private boolean isAppointee;
	
	@Column(name = "appointee_first_name", length = 60)
	private String appointeeFirstName;
	
	@Column(name = "appointee_last_name", length = 60)
	private String appointeeLastName;

	@Column(name = "appointee_dob", length = 55)
	private String appointeeDob;

	@Column(name = "appointee_gender", length = 20)
	private String appointeeGender;

	@Column(name = "appointee_relationship_with_nominee", length = 55)
	@Enumerated(EnumType.STRING)
	private RelationshipWithAssured relationWithNominee;

	public Long getCreditCardNomineeId() {
		return creditCardNomineeId;
	}

	public void setCreditCardNomineeId(Long creditCardNomineeId) {
		this.creditCardNomineeId = creditCardNomineeId;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	public CreditCardCustomerEntity getCreditCardCustomerEntity() {
		return creditCardCustomerId;
	}

	public void setCreditCardCustomerEntity(CreditCardCustomerEntity creditCardCustomerId) {
		this.creditCardCustomerId = creditCardCustomerId;
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

	public String getNomineeDob() {
		return nomineeDob;
	}

	public void setNomineeDob(String nomineeDob) {
		this.nomineeDob = nomineeDob;
	}

	public String getNomineeGender() {
		return nomineeGender;
	}

	public void setNomineeGender(String nomineeGender) {
		this.nomineeGender = nomineeGender;
	}

	public RelationshipWithAssured getRelationshipWithAssured() {
		return relationshipWithAssured;
	}

	public void setRelationshipWithAssured(RelationshipWithAssured relationshipWithAssured) {
		this.relationshipWithAssured = relationshipWithAssured;
	}

	public boolean isAppointee() {
		return isAppointee;
	}

	public void setAppointee(boolean isAppointee) {
		this.isAppointee = isAppointee;
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

	public String getAppointeeDob() {
		return appointeeDob;
	}

	public void setAppointeeDob(String appointeeDob) {
		this.appointeeDob = appointeeDob;
	}

	public String getAppointeeGender() {
		return appointeeGender;
	}

	public void setAppointeeGender(String appointeeGender) {
		this.appointeeGender = appointeeGender;
	}

	public RelationshipWithAssured getRelationWithNominee() {
		return relationWithNominee;
	}

	public void setRelationWithNominee(RelationshipWithAssured relationWithNominee) {
		this.relationWithNominee = relationWithNominee;
	}

	@Override
	public String toString() {
		return "CreditCardNomineeEntity [creditCardNomineeId=" + creditCardNomineeId + ", version=" + version
				+ ", creditCardCustomerId=" + creditCardCustomerId + ", nomineeFirstName=" + nomineeFirstName
				+ ", nomineeLastName=" + nomineeLastName + ", nomineeDob=" + nomineeDob + ", nomineeGender="
				+ nomineeGender + ", relationshipWithAssured=" + relationshipWithAssured + ", isAppointee="
				+ isAppointee + ", appointeeFirstName=" + appointeeFirstName + ", appointeeLastName="
				+ appointeeLastName + ", appointeeDob=" + appointeeDob + ", appointeeGender=" + appointeeGender
				+ ", relationWithNominee=" + relationWithNominee + "]";
	}
}
