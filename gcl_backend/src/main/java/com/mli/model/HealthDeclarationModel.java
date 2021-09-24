package com.mli.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author Nikhilesh.Tiwari
 *
 */
@JsonInclude(Include.NON_NULL)
public class HealthDeclarationModel extends BaseModel {

	private static final long serialVersionUID = -6167910709876601565L;

	private Boolean isHealthDeclaration;
	private String negativeDeclaration;
	private String otherInsurance;

	private List<AnswerModel> healthAnswerModel;
	private Boolean isApplication;
	private String applicationNumber;
	private Object savedAnswers;
	private String otherHealthInfo;
	private String triggerMsg;
	private List<HealthAnswerModel> ciRiderQuestionAns;
	private String ciHealthDecsAns;
	private Covid_19Model covid_19Details;

	public Boolean getIsHealthDeclaration() {
		return isHealthDeclaration;
	}

	public void setIsHealthDeclaration(Boolean isHealthDeclaration) {
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

	public List<AnswerModel> getHealthAnswerModel() {
		return healthAnswerModel;
	}

	public void setHealthAnswerModel(List<AnswerModel> healthAnswerModel) {
		this.healthAnswerModel = healthAnswerModel;
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

	public Object getSavedAnswers() {
		return savedAnswers;
	}

	public void setSavedAnswers(Object savedAnswers) {
		this.savedAnswers = savedAnswers;
	}

	public String getOtherHealthInfo() {
		return otherHealthInfo;
	}

	public void setOtherHealthInfo(String otherHealthInfo) {
		this.otherHealthInfo = otherHealthInfo;
	}

	public String getTriggerMsg() {
		return triggerMsg;
	}

	public void setTriggerMsg(String triggerMsg) {
		this.triggerMsg = triggerMsg;
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
}