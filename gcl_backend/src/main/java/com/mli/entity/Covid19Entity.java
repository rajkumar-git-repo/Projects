package com.mli.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "covid_19")
public class Covid19Entity {

	@Id
	@Column(name = "covid_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long covidId;

	@Column(name = "covid_first_answer", length = 2000)
	private String covidAnsOne;
	@Column(name = "covid_second_answer", length = 2000)
	private String covidAnsTwo;
	@Column(name = "covid_third_answer_a", length = 2000)
	private String covidAnsThree_a;
	@Column(name = "covid_third_answer_b", length = 2000)
	private String covidAnsThree_b;
	@Column(name = "covid_fourth_answer", length = 2000)
	private String covidAnsFour;
	@Column(name = "covid_fifth_answer", length = 2000)
	private String covidAnsFive;
	@Column(name = "covid_declaration")
	private String covidDeclaration;

	@OneToOne
	@NotNull
	@JoinColumn(name = "health_detail_id")
	private HealthDeclarationEntity healthDeclarationEntity;

	public Long getCovidId() {
		return covidId;
	}

	public void setCovidId(Long covidId) {
		this.covidId = covidId;
	}

	public String getCovidAnsOne() {
		return covidAnsOne;
	}

	public void setCovidAnsOne(String covidAnsOne) {
		this.covidAnsOne = covidAnsOne;
	}

	public String getCovidAnsTwo() {
		return covidAnsTwo;
	}

	public void setCovidAnsTwo(String covidAnsTwo) {
		this.covidAnsTwo = covidAnsTwo;
	}

	public String getCovidAnsThree_a() {
		return covidAnsThree_a;
	}

	public void setCovidAnsThree_a(String covidAnsThree_a) {
		this.covidAnsThree_a = covidAnsThree_a;
	}

	public String getCovidAnsThree_b() {
		return covidAnsThree_b;
	}

	public void setCovidAnsThree_b(String covidAnsThree_b) {
		this.covidAnsThree_b = covidAnsThree_b;
	}

	public String getCovidAnsFour() {
		return covidAnsFour;
	}

	public void setCovidAnsFour(String covidAnsFour) {
		this.covidAnsFour = covidAnsFour;
	}

	public String getCovidAnsFive() {
		return covidAnsFive;
	}

	public void setCovidAnsFive(String covidAnsFive) {
		this.covidAnsFive = covidAnsFive;
	}

	public HealthDeclarationEntity getHealthDeclarationEntity() {
		return healthDeclarationEntity;
	}

	public void setHealthDeclarationEntity(HealthDeclarationEntity healthDeclarationEntity) {
		this.healthDeclarationEntity = healthDeclarationEntity;
	}

	public String getCovidDeclaration() {
		return covidDeclaration;
	}

	public void setCovidDeclaration(String covidDeclaration) {
		this.covidDeclaration = covidDeclaration;
	}

	@Override
	public String toString() {
		return "Covid19Entity [covidId=" + covidId + ", covidAnsOne=" + covidAnsOne + ", covidAnsTwo=" + covidAnsTwo
				+ ", covidAnsThree_a=" + covidAnsThree_a + ", covidAnsThree_b=" + covidAnsThree_b + ", covidAnsFour="
				+ covidAnsFour + ", covidAnsFive=" + covidAnsFive + ", covidDeclaration=" + covidDeclaration
				+ ", healthDeclarationEntity=" + healthDeclarationEntity + "]";
	}

}
