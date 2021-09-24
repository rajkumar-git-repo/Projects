package com.mli.model;

import java.io.Serializable;
import java.util.List;

public class CardTypeModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String cardSegment;
	private List<Coverage> coverages;
	
	public CardTypeModel(){
		
	}

	public CardTypeModel(String cardSegment, List<Coverage> coverages) {
		super();
		this.cardSegment = cardSegment;
		this.coverages = coverages;
	}

	public String getCardSegment() {
		return cardSegment;
	}

	public void setCardSegment(String cardSegment) {
		this.cardSegment = cardSegment;
	}

	public List<Coverage> getCoverages() {
		return coverages;
	}

	public void setCoverages(List<Coverage> coverages) {
		this.coverages = coverages;
	}

	@Override
	public String toString() {
		return "CardTypeModel [cardSegment=" + cardSegment + ", coverages=" + coverages + "]";
	}
}
