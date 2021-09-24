package com.mli.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

/**
 * @author Nikhilesh.Tiwari
 *
 */
@Entity
@Table(name = "health_declaration")
public class HealthDeclarationEntity extends BaseEntity {

	private static final long serialVersionUID = -4258988274403152103L;

	@Id
	@Column(name = "health_dcl_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long healthDclId;

	@OneToOne
	@NotNull
	@JoinColumn(name = "cust_detail_id")
	private CustomerDetailsEntity customerDtlId;

	@Column(name = "health_declaration")
	private Boolean isHealthDeclaration;

	@Column(name = "negative_declaration", length = 150)
	private String negativeDeclaration;

	@Column(name = "other_insurance")
	private String otherInsurance;

	@Version
	@Column(name = "version")
	private Long version;

	// added by Devendra.Kumar
	@Column(name = "any_application_no")
	private Boolean isApplication;

	@Column(name = "application_number")
	private String applicationNumber;

	@Column(name = "other_health_info",length = 300)
	private String otherHealthInfo;

	@Column(name = "diabetes_trigger_msg")
	private String diabetesTriggerMsg;

	@Column(name = "hypertension_trigger_msg")
	private String hypertensionTriggerMsg;

	@Column(name = "health_ques_ci_rider")
	private String healthDeclarationForCIRider;

	@Column(name = "ci_health_decs_ans")
	private String ciHealthDecsAns;

	public Long getHealthDclId() {
		return healthDclId;
	}

	public void setHealthDclId(Long healthDclId) {
		this.healthDclId = healthDclId;
	}

	public CustomerDetailsEntity getCustomerDtlId() {
		return customerDtlId;
	}

	public void setCustomerDtlId(CustomerDetailsEntity customerDtlId) {
		this.customerDtlId = customerDtlId;
	}

	public Boolean isHealthDeclaration() {
		return isHealthDeclaration;
	}

	public void setHealthDeclaration(Boolean isHealthDeclaration) {
		this.isHealthDeclaration = isHealthDeclaration;
	}

	public String getNegativeDeclaration() {
		return negativeDeclaration;
	}

	public void setNegativeDeclaration(String negativeDeclaration) {
		this.negativeDeclaration = negativeDeclaration;
	}

	public String getOtherInsurance() {
		return otherInsurance;
	}

	public void setOtherInsurance(String otherInsurance) {
		this.otherInsurance = otherInsurance;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	public Boolean getIsHealthDeclaration() {
		return isHealthDeclaration;
	}

	public void setIsHealthDeclaration(Boolean isHealthDeclaration) {
		this.isHealthDeclaration = isHealthDeclaration;
	}

	public Boolean getIsApplication() {
		return isApplication;
	}

	public void setIsApplication(Boolean isApplication) {
		this.isApplication = isApplication;
	}

	public String getApplicationNumber() {
		return applicationNumber;
	}

	public void setApplicationNumber(String applicationNumber) {
		this.applicationNumber = applicationNumber;
	}

	public String getOtherHealthInfo() {
		return otherHealthInfo;
	}

	public void setOtherHealthInfo(String otherHealthInfo) {
		this.otherHealthInfo = otherHealthInfo;
	}

	public String getDiabetesTriggerMsg() {
		return diabetesTriggerMsg;
	}

	public void setDiabetesTriggerMsg(String diabetesTriggerMsg) {
		this.diabetesTriggerMsg = diabetesTriggerMsg;
	}

	public String getHypertensionTriggerMsg() {
		return hypertensionTriggerMsg;
	}

	public void setHypertensionTriggerMsg(String hypertensionTriggerMsg) {
		this.hypertensionTriggerMsg = hypertensionTriggerMsg;
	}

	public String getHealthDeclarationForCIRider() {
		return healthDeclarationForCIRider;
	}

	public void setHealthDeclarationForCIRider(String healthDeclarationForCIRider) {
		this.healthDeclarationForCIRider = healthDeclarationForCIRider;
	}

	public String getCiHealthDecsAns() {
		return ciHealthDecsAns;
	}

	public void setCiHealthDecsAns(String ciHealthDecsAns) {
		this.ciHealthDecsAns = ciHealthDecsAns;
	}

}
