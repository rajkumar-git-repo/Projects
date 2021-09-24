package com.mli.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@Table(name = "sfq_covid_vaccine")
public class SFQCovidVaccineEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "sfq_covid_vaccine_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long sfqCovidVaccineId;

	@Version
	@Column(name = "version")
	private long version;

	@Column(name = "is_vaccinated")
	private boolean vaccinated;
	@Column(name = "first_dose_date", length = 50)
	private String firstDoseDate;
	@Column(name = "second_dose_date", length = 50)
	private String secondDoseDate;
	@Column(name = "vaccine_name", length = 50)
	private String vaccineName;
	@Column(name = "declaration", length = 2000)
	private String declaration;

	@OneToOne(mappedBy = "sfqCovidVaccineEntity")
	private SFQCovidEntity sfqCovidEntity;

	public Long getSfqCovidVaccineId() {
		return sfqCovidVaccineId;
	}

	public void setSfqCovidVaccineId(Long sfqCovidVaccineId) {
		this.sfqCovidVaccineId = sfqCovidVaccineId;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}
	
	public boolean isVaccinated() {
		return vaccinated;
	}

	public void setVaccinated(boolean vaccinated) {
		this.vaccinated = vaccinated;
	}

	public String getFirstDoseDate() {
		return firstDoseDate;
	}

	public void setFirstDoseDate(String firstDoseDate) {
		this.firstDoseDate = firstDoseDate;
	}

	public String getSecondDoseDate() {
		return secondDoseDate;
	}

	public void setSecondDoseDate(String secondDoseDate) {
		this.secondDoseDate = secondDoseDate;
	}

	public String getVaccineName() {
		return vaccineName;
	}

	public void setVaccineName(String vaccineName) {
		this.vaccineName = vaccineName;
	}

	public String getDeclaration() {
		return declaration;
	}

	public void setDeclaration(String declaration) {
		this.declaration = declaration;
	}

	public SFQCovidEntity getSfqCovidEntity() {
		return sfqCovidEntity;
	}

	public void setSfqCovidEntity(SFQCovidEntity sfqCovidEntity) {
		this.sfqCovidEntity = sfqCovidEntity;
	}

	@Override
	public String toString() {
		return "SFQCovidVaccineEntity [sfqCovidVaccineId=" + sfqCovidVaccineId + ", version=" + version
				+ ", isVaccinated=" + vaccinated + ", firstDoseDate=" + firstDoseDate + ", secondDoseDate="
				+ secondDoseDate + ", vaccineName=" + vaccineName + ", declaration=" + declaration + ", sfqCovidEntity="
				+ sfqCovidEntity + "]";
	}

}
