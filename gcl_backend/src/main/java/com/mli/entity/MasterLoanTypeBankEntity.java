package com.mli.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "master_loan_type_bank")
public class MasterLoanTypeBankEntity extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "loan_type")
	private String loanType;

	@Column(name = "cover_type")
	private String coverType;

	@Column(name = "ci_rider")
	private String ciRider;

	@Column(name = "cirider_type")
	private String ciRiderType;
	
	@Column(name = "mph")
	private String mph;

	@Column(name = "percentage")
	private String percentage;
	
	@Column(name = "balic_premimum")
	private String balicPremimum;
	
	@Column(name = "reducing_cover_interest_rates")
	private String reducingCoverInterestRates;
	
	@Column(name = "reducing_max_rates")
	private String reducingMaxRates;
	
	@Column(name = "level_rates")
	private String levelRates;
	
	@Column(name = "interest")
	private String interest;
	
	public MasterLoanTypeBankEntity(Long id, String loanType, String coverType, String ciRider, String ciRiderType,
			String mph, String percentage, String balicPremimum, String reducingCoverInterestRates,
			String reducingMaxRates, String levelRates, String interest) {
		super();
		this.id = id;
		this.loanType = loanType;
		this.coverType = coverType;
		this.ciRider = ciRider;
		this.ciRiderType = ciRiderType;
		this.mph = mph;
		this.percentage = percentage;
		this.balicPremimum = balicPremimum;
		this.reducingCoverInterestRates = reducingCoverInterestRates;
		this.reducingMaxRates = reducingMaxRates;
		this.levelRates = levelRates;
		this.interest = interest;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMph() {
		return mph;
	}

	public void setMph(String mph) {
		this.mph = mph;
	}

	public String getLoanType() {
		return loanType;
	}

	public void setLoanType(String loanType) {
		this.loanType = loanType;
	}

	public String getCoverType() {
		return coverType;
	}

	public void setCoverType(String coverType) {
		this.coverType = coverType;
	}

	

	public String getCiRiderType() {
		return ciRiderType;
	}

	public void setCiRiderType(String ciRiderType) {
		this.ciRiderType = ciRiderType;
	}

	public String getPercentage() {
		return percentage;
	}

	public void setPercentage(String percentage) {
		this.percentage = percentage;
	}
	
	

	public String getCiRider() {
		return ciRider;
	}

	public void setCiRider(String ciRider) {
		this.ciRider = ciRider;
	}


	public MasterLoanTypeBankEntity() {
		super();
	}

	public String getBalicPremimum() {
		return balicPremimum;
	}

	public void setBalicPremimum(String balicPremimum) {
		this.balicPremimum = balicPremimum;
	}

	public String getReducingCoverInterestRates() {
		return reducingCoverInterestRates;
	}

	public void setReducingCoverInterestRates(String reducingCoverInterestRates) {
		this.reducingCoverInterestRates = reducingCoverInterestRates;
	}

	public String getReducingMaxRates() {
		return reducingMaxRates;
	}

	public void setReducingMaxRates(String reducingMaxRates) {
		this.reducingMaxRates = reducingMaxRates;
	}

	public String getLevelRates() {
		return levelRates;
	}

	public void setLevelRates(String levelRates) {
		this.levelRates = levelRates;
	}

	public String getInterest() {
		return interest;
	}

	public void setInterest(String interest) {
		this.interest = interest;
	}

	@Override
	public String toString() {
		return "MasterLoanTypeBankEntity [id=" + id + ", loanType=" + loanType + ", coverType=" + coverType
				+ ", ciRider=" + ciRider + ", ciRiderType=" + ciRiderType + ", mph=" + mph + ", percentage="
				+ percentage + ", balicPremimum=" + balicPremimum + ", reducingCoverInterestRates="
				+ reducingCoverInterestRates + ", reducingMaxRates=" + reducingMaxRates + ", levelRates=" + levelRates
				+ ", interest=" + interest + "]";
	}

	

	
	
}
