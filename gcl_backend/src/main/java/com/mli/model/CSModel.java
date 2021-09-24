package com.mli.model;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

/**
 * 
 * Model which is used in customer-screen to edit HDF part
 *
 */
public class CSModel {

	@NotNull
	private List<AnswerModel> healthAnswerModel;

	// checksum
	@NotEmpty
	private String data;

	private Boolean isHealthDeclaration;
	private String otherHealthInfo;
	private Boolean isApplication;
	private String applicationNumber;

	private List<HealthAnswerModel> ciRiderQuestionAns;
	private String ciHealthDecsAns;
	
	private Covid_19Model covid_19Details;
	
	private SFQHealthDeclarationModel sfqHealthDeclarationModel;

	public List<AnswerModel> getHealthAnswerModel() {
		return healthAnswerModel;
	}

	public void setHealthAnswerModel(List<AnswerModel> healthAnswerModel) {
		this.healthAnswerModel = healthAnswerModel;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public Boolean getIsHealthDeclaration() {
		return isHealthDeclaration;
	}

	public void setIsHealthDeclaration(Boolean isHealthDeclaration) {
		this.isHealthDeclaration = isHealthDeclaration;
	}

	public String getOtherHealthInfo() {
		return otherHealthInfo;
	}

	public void setOtherHealthInfo(String otherHealthInfo) {
		this.otherHealthInfo = otherHealthInfo;
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

	public List<HealthAnswerModel> getCiRiderQuestionAns() {
		return ciRiderQuestionAns;
	}

	public void setCiRiderQuestionAns(List<HealthAnswerModel> ciRiderQuestionAns) {
		this.ciRiderQuestionAns = ciRiderQuestionAns;
	}

	public String getCiHealthDecsAns() {
		return ciHealthDecsAns;
	}

	public void setCiHealthDecsAns(String ciHealthDecsAns) {
		this.ciHealthDecsAns = ciHealthDecsAns;
	}

	public Covid_19Model getCovid_19Details() {
		return covid_19Details;
	}

	public void setCovid_19Details(Covid_19Model covid_19Details) {
		this.covid_19Details = covid_19Details;
	}

	public SFQHealthDeclarationModel getSfqHealthDeclarationModel() {
		return sfqHealthDeclarationModel;
	}

	public void setSfqHealthDeclarationModel(SFQHealthDeclarationModel sfqHealthDeclarationModel) {
		this.sfqHealthDeclarationModel = sfqHealthDeclarationModel;
	}
}
