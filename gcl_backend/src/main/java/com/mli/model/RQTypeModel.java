package com.mli.model;

import java.util.List;

/**
 * 
 * @author Devendra.Kumar
 *
 */
public class RQTypeModel {

	private String rqType;
	private List<RQSubTypeModel> rqSubType;

	public String getRqType() {
		return rqType;
	}

	public void setRqType(String rqType) {
		this.rqType = rqType;
	}

	public List<RQSubTypeModel> getRqSubType() {
		return rqSubType;
	}

	public void setRqSubType(List<RQSubTypeModel> rqSubType) {
		this.rqSubType = rqSubType;
	}

}
