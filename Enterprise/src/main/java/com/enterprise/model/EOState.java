package com.enterprise.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class EOState {

	@Id
	private int primarykey;
	private String stateName;

	public int getPrimarykey() {
		return primarykey;
	}

	public void setPrimarykey(int primarykey) {
		this.primarykey = primarykey;
	}

	public String getStateName() {
		return stateName;
	}

	public void setStateName(String stateName) {
		this.stateName = stateName;
	}

	@Override
	public String toString() {
		return "EOState [primarykey=" + primarykey + ", stateName=" + stateName + "]";
	}
}
