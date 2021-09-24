package com.mli.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="sfq_covid_test")
public class SFQCovidTestEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "sfq_covid_test_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long sfqCovidTestId;

	@Version
	@Column(name = "version")
	private long version;

	@Column(name = "covid_first_answer", length = 2000)
	private String covidFirstAnswer;
	@Column(name = "covid_second_answer", length = 2000)
	private String covidSecondAnswer;
	@Column(name = "covid_third_answer")
	private String covidThirdAnswer;
	@Column(name = "covid_fourth_answer", length = 2000)
	private String covidFourthAnswer;
	
	@OneToOne(mappedBy = "sfqCovidTestEntity")
    private SFQCovidEntity sfqCovidEntity;

	public Long getSfqCovidTestId() {
		return sfqCovidTestId;
	}

	public void setSfqCovidTestId(Long sfqCovidTestId) {
		this.sfqCovidTestId = sfqCovidTestId;
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

	public String getCovidThirdAnswer() {
		return covidThirdAnswer;
	}

	public void setCovidThirdAnswer(String covidThirdAnswer) {
		this.covidThirdAnswer = covidThirdAnswer;
	}

	public String getCovidFourthAnswer() {
		return covidFourthAnswer;
	}

	public void setCovidFourthAnswer(String covidFourthAnswer) {
		this.covidFourthAnswer = covidFourthAnswer;
	}

	public SFQCovidEntity getSfqCovidEntity() {
		return sfqCovidEntity;
	}

	public void setSfqCovidEntity(SFQCovidEntity sfqCovidEntity) {
		this.sfqCovidEntity = sfqCovidEntity;
	}

	@Override
	public String toString() {
		return "SFQCovidTestEntity [sfqCovidTestId=" + sfqCovidTestId + ", version=" + version + ", covidFirstAnswer="
				+ covidFirstAnswer + ", covidSecondAnswer=" + covidSecondAnswer + ", covidThirdAnswer="
				+ covidThirdAnswer + ", covidFourthAnswer=" + covidFourthAnswer + ", sfqCovidEntity=" + sfqCovidEntity
				+ "]";
	}
}
