package com.mli.model;

import java.util.List;

/**
 * 
 * @author Devendra.Kumar
 *
 */
public class AnswerModel {

	private String proposalNumber;
	private Long questionId;
	private List<SelectedOptionModel> selectedOptions;
	private String description;
	private String flag;
	private boolean updatedAnswerFlag;

	public Long getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Long questionId) {
		this.questionId = questionId;
	}

	public List<SelectedOptionModel> getSelectedOptions() {
		return selectedOptions;
	}

	public void setSelectedOptions(List<SelectedOptionModel> selectedOptions) {
		this.selectedOptions = selectedOptions;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public boolean isUpdatedAnswerFlag() {
		return updatedAnswerFlag;
	}

	public void setUpdatedAnswerFlag(boolean updatedAnswerFlag) {
		this.updatedAnswerFlag = updatedAnswerFlag;
	}

	public String getProposalNumber() {
		return proposalNumber;
	}

	public void setProposalNumber(String proposalNumber) {
		this.proposalNumber = proposalNumber;
	}
	
}
