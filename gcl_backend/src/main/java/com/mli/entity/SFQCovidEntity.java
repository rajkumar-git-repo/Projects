package com.mli.entity;

import javax.persistence.CascadeType;
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
@Table(name = "sfq_covid")
public class SFQCovidEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "sfq_covid_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long sfqCovidId;

	@Version
	@Column(name = "version")
	private long version;

	@Column(name = "covid_first_answer", length = 2000)
	private String covidFirstAnswer;
	@Column(name = "covid_second_answer", length = 2000)
	private String covidSecondAnswer;
	@Column(name = "covid_third_answer_a")
	private String covidThirdAnswer_a;
	@Column(name = "covid_third_answer_b", length = 2000)
	private String covidThirdAnswer_b;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "sfq_covid_test_id")
	private SFQCovidTestEntity sfqCovidTestEntity;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "sfq_covid_vaccine_id")
	private SFQCovidVaccineEntity sfqCovidVaccineEntity;

	@OneToOne
	@NotNull
	@JoinColumn(name = "sfq_health_detail_id")
	private SFQHealthDeclarationEntity sfqHealthDeclarationEntity;

	public Long getSfqCovidId() {
		return sfqCovidId;
	}

	public void setSfqCovidId(Long sfqCovidId) {
		this.sfqCovidId = sfqCovidId;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	public String getCovidFirstAnswer() {
		return covidFirstAnswer;
	}

	public void setCovidFirstAnswer(String covidFirstAnswer) {
		this.covidFirstAnswer = covidFirstAnswer;
	}

	public String getCovidSecondAnswer() {
		return covidSecondAnswer;
	}

	public void setCovidSecondAnswer(String covidSecondAnswer) {
		this.covidSecondAnswer = covidSecondAnswer;
	}

	public String getCovidThirdAnswer_a() {
		return covidThirdAnswer_a;
	}

	public void setCovidThirdAnswer_a(String covidThirdAnswer_a) {
		this.covidThirdAnswer_a = covidThirdAnswer_a;
	}

	public String getCovidThirdAnswer_b() {
		return covidThirdAnswer_b;
	}

	public void setCovidThirdAnswer_b(String covidThirdAnswer_b) {
		this.covidThirdAnswer_b = covidThirdAnswer_b;
	}

	public SFQCovidTestEntity getSfqCovidTestEntity() {
		return sfqCovidTestEntity;
	}

	public void setSfqCovidTestEntity(SFQCovidTestEntity sfqCovidTestEntity) {
		this.sfqCovidTestEntity = sfqCovidTestEntity;
	}

	public SFQCovidVaccineEntity getSfqCovidVaccineEntity() {
		return sfqCovidVaccineEntity;
	}

	public void setSfqCovidVaccineEntity(SFQCovidVaccineEntity sfqCovidVaccineEntity) {
		this.sfqCovidVaccineEntity = sfqCovidVaccineEntity;
	}

	public SFQHealthDeclarationEntity getSfqHealthDeclarationEntity() {
		return sfqHealthDeclarationEntity;
	}

	public void setSfqHealthDeclarationEntity(SFQHealthDeclarationEntity sfqHealthDeclarationEntity) {
		this.sfqHealthDeclarationEntity = sfqHealthDeclarationEntity;
	}

	@Override
	public String toString() {
		return "SFQCovidEntity [sfqCovidId=" + sfqCovidId + ", version=" + version + ", covidFirstAnswer="
				+ covidFirstAnswer + ", covidSecondAnswer=" + covidSecondAnswer + ", covidThirdAnswer_a="
				+ covidThirdAnswer_a + ", covidThirdAnswer_b=" + covidThirdAnswer_b + ", sfqCovidTestEntity="
				+ sfqCovidTestEntity + ", sfqCovidVaccineEntity=" + sfqCovidVaccineEntity
				+ ", sfqHealthDeclarationEntity=" + sfqHealthDeclarationEntity + "]";
	}

}
