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
@Table(name = "sfq_health_declaration")
public class SFQHealthDeclarationEntity extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "sfq_health_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long healthDclId;

	@Version
	@Column(name = "version")
	private Long version;

	@OneToOne
	@NotNull
	@JoinColumn(name = "cust_detail_id")
	private CustomerDetailsEntity customerDtlId;
	@NotNull
	@Column(name = "height")
	private int height;
	@NotNull
	@Column(name = "weight")
	private int weight;
	@Column(name = "health_first_answer")
	private String healthFirstAnswer;
	@Column(name = "health_second_answer")
	private String healthSecondAnswer;
	@Column(name = "health_third_answer")
	private String healthThirdAnswer;
	@Column(name = "health_fourth_answer")
	private String healthFourthAnswer;
	@Column(name = "health_fifth_answer")
	private String healthFifthAnswer;
	@Column(name = "health_sixth_answer")
	private String healthSixthAnswer;
	@Column(name = "is_smoker")
	private boolean smoker;
	@Column(name = "smoke_per_day")
	private int smokePerDay;

	public Long getHealthDclId() {
		return healthDclId;
	}

	public void setHealthDclId(Long healthDclId) {
		this.healthDclId = healthDclId;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	public CustomerDetailsEntity getCustomerDtlId() {
		return customerDtlId;
	}

	public void setCustomerDtlId(CustomerDetailsEntity customerDtlId) {
		this.customerDtlId = customerDtlId;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
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

	public String getHealthFourthAnswer() {
		return healthFourthAnswer;
	}

	public void setHealthFourthAnswer(String healthFourthAnswer) {
		this.healthFourthAnswer = healthFourthAnswer;
	}

	public String getHealthFifthAnswer() {
		return healthFifthAnswer;
	}

	public void setHealthFifthAnswer(String healthFifthAnswer) {
		this.healthFifthAnswer = healthFifthAnswer;
	}

	public String getHealthSixthAnswer() {
		return healthSixthAnswer;
	}

	public void setHealthSixthAnswer(String healthSixthAnswer) {
		this.healthSixthAnswer = healthSixthAnswer;
	}

	public boolean isSmoker() {
		return smoker;
	}

	public void setSmoker(boolean smoker) {
		this.smoker = smoker;
	}

	public int getSmokePerDay() {
		return smokePerDay;
	}

	public void setSmokePerDay(int smokePerDay) {
		this.smokePerDay = smokePerDay;
	}

	@Override
	public String toString() {
		return "SFQHealthDeclarationEntity [healthDclId=" + healthDclId + ", version=" + version + ", customerDtlId="
				+ customerDtlId + ", height=" + height + ", weight=" + weight + ", healthFirstAnswer="
				+ healthFirstAnswer + ", healthSecondAnswer=" + healthSecondAnswer + ", healthThirdAnswer="
				+ healthThirdAnswer + ", healthFourthAnswer=" + healthFourthAnswer + ", healthFifthAnswer="
				+ healthFifthAnswer + ", healthSixthAnswer=" + healthSixthAnswer + ", isSmoker=" + smoker
				+ ", smokePerDay=" + smokePerDay + "]";
	}

}
