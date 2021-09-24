package com.mli.model;

import java.util.List;

public class NationalityDetail {

	private List<AnswerModel> nationalityAnswerModel;
	private Object savedAnswers;
	private boolean isImageExist;

	public List<AnswerModel> getNationalityAnswerModel() {
		return nationalityAnswerModel;
	}

	public void setNationalityAnswerModel(List<AnswerModel> nationalityAnswerModel) {
		this.nationalityAnswerModel = nationalityAnswerModel;
	}

	public Object getSavedAnswers() {
		return savedAnswers;
	}

	public void setSavedAnswers(Object savedAnswers) {
		this.savedAnswers = savedAnswers;
	}

	public boolean isImageExist() {
		return isImageExist;
	}

	public void setImageExist(boolean isImageExist) {
		this.isImageExist = isImageExist;
	}

}
