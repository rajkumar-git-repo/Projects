package com.mli.model;

import java.util.List;

/**
 * 
 * @author Devendra.Kumar
 *
 */
public class RQModel {

	private Long id;
	private Long type;
	private Long subType;
	private String qType;
	private String question;
	private String isToolTip;
	private String toolTipDescription;
	private List<String> options;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getType() {
		return type;
	}

	public void setType(Long type) {
		this.type = type;
	}

	public Long getSubType() {
		return subType;
	}

	public void setSubType(Long subType) {
		this.subType = subType;
	}

	public String getqType() {
		return qType;
	}

	public void setqType(String qType) {
		this.qType = qType;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public List<String> getOptions() {
		return options;
	}

	public void setOptions(List<String> options) {
		this.options = options;
	}

	public String getIsToolTip() {
		return isToolTip;
	}

	public void setIsToolTip(String isToolTip) {
		this.isToolTip = isToolTip;
	}

	public String getToolTipDescription() {
		return toolTipDescription;
	}

	public void setToolTipDescription(String toolTipDescription) {
		this.toolTipDescription = toolTipDescription;
	}
}
