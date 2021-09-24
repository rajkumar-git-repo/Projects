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

@Entity
@Table(name = "creditcard_health")
public class CreditCardHealthEntity extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "creditcard_health_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long creditCardHealthId;
	@Version
	@Column(name = "version")
	private long version;
	@OneToOne
	@NotNull
	@JoinColumn(name = "creditcard_customer_id")
	private CreditCardCustomerEntity creditCardCustomerId;

	@Column(name = "health_first_answer")
	private String healthFirstAnswer;
	@Column(name = "health_second_answer")
	private String healthSecondAnswer;
	@Column(name = "health_third_answer")
	private String healthThirdAnswer;
	@Column(name = "is_smoker")
	private String isSmoker;
	@Column(name = "smoke_per_day")
	private String smokePerDay;
	@Column(name = "declaration", length = 2000)
	private String declaration;
	@Column(name = "app_number")
	private String appNumber;

	public Long getCreditCardHealthId() {
		return creditCardHealthId;
	}

	public void setCreditCardHealthId(Long creditCardHealthId) {
		this.creditCardHealthId = creditCardHealthId;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	public CreditCardCustomerEntity getCreditCardCustomerId() {
		return creditCardCustomerId;
	}

	public void setCreditCardCustomerId(CreditCardCustomerEntity creditCardCustomerId) {
		this.creditCardCustomerId = creditCardCustomerId;
	}
	
	public String getHealthFirstAnswer() {
		return healthFirstAnswer;
	}

	public void setHealthFirstAnswer(String healthFirstAnswer) {
		this.healthFirstAnswer = healthFirstAnswer;
	}

	public String getHealthSecondAnswer() {
		return healthSecondAnswer;
	}

	public void setHealthSecondAnswer(String healthSecondAnswer) {
		this.healthSecondAnswer = healthSecondAnswer;
	}

	public String getHealthThirdAnswer() {
		return healthThirdAnswer;
	}

	public void setHealthThirdAnswer(String healthThirdAnswer) {
		this.healthThirdAnswer = healthThirdAnswer;
	}

	public String getIsSmoker() {
		return isSmoker;
	}

	public void setIsSmoker(String isSmoker) {
		this.isSmoker = isSmoker;
	}

	public String getSmokePerDay() {
		return smokePerDay;
	}

	public void setSmokePerDay(String smokePerDay) {
		this.smokePerDay = smokePerDay;
	}

	public String getDeclaration() {
		return declaration;
	}

	public void setDeclaration(String declaration) {
		this.declaration = declaration;
	}

	public String getAppNumber() {
		return appNumber;
	}

	public void setAppNumber(String appNumber) {
		this.appNumber = appNumber;
	}

	@Override
	public String toString() {
		return "CreditCardHealthEntity [creditCardHealthId=" + creditCardHealthId + ", version=" + version
				+ ", creditCardCustomerId=" + creditCardCustomerId + ", healthFirstAnswer=" + healthFirstAnswer
				+ ", healthSecondAnswer=" + healthSecondAnswer + ", healthThirdAnswer=" + healthThirdAnswer
				+ ", isSmoker=" + isSmoker + ", smokePerDay=" + smokePerDay + ", declaration=" + declaration
				+ ", appNumber=" + appNumber + "]";
	}
}
