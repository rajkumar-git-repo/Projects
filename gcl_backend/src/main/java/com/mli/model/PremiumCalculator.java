package com.mli.model;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

/**
 * 
 * @author Devendra.Kumar
 *
 */

public class PremiumCalculator implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@NotBlank
	private String loanAppNumber;
	@NotNull
	private Double sumAssured;
	@NotBlank
	private String dob;
	@NotNull
	private Long sellerContactNo;
	@NotNull
	private Double loanAmount;
	@NotNull
	private Double tenureYear;
	@NotBlank
	private String interestRate;
	// @NotNull
	private String isFinancePremium;
	@NotBlank
	private String tentativeEMI;
	@NotNull
	private Double age; // can calculate
	@NotNull
	private Double totalPremium;
	@NotBlank
	private String incrementalEMI;
	// @NotNull
	private String isMedicalUnderWritingRequired;
	// @NotNull
	private String isFinancialUnderWritingRequired;
	// @NotNull
	private String isInterested;
	// @NotBlank
	private String reseanForNotInterested;
	@NotBlank
	private String schemeType;
	@NotBlank
	private String loanType;

	@NotNull
	private Double tenureEligible;

	@NotBlank
	private String channel;

	private String checksum;

	/**
	 * Fields for CI reider
	 */

	private Integer baseOrBaseWithCI;
	private Integer percentageSumAssured;
	private Double ciRiderSumAssured;
	private Integer ciOption;
	private Integer ciTenureYears;

	private Integer ciRiderPermium;
	private Integer baseWithCIPremium;

	private String medicalUWCI;
	private String financialUWCI;

	private String overallUWMedical;
	private String overallUWFinancial;

	private Integer tentativeEMIBaseWithCI;
	private Integer incrementalEMIBaseWithCI;

	private String gender;

	/*
	 * this is add for address specific user self funded
	 */
	private String street;
	private String address1;
	private String address2;
	private String address3;
	private String pinCode;
	private String town;
	private String state;
	private String country;
	@NotBlank
	private String mph;
	private String roId;
	private String smId;

	public String getLoanAppNumber() {
		return loanAppNumber;
	}

	public void setLoanAppNumber(String loanAppNumber) {
		this.loanAppNumber = loanAppNumber;
	}

	public Double getSumAssured() {
		return sumAssured;
	}

	public void setSumAssured(Double sumAssured) {
		this.sumAssured = sumAssured;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public Long getSellerContactNo() {
		return sellerContactNo;
	}

	public void setSellerContactNo(Long sellerContactNo) {
		this.sellerContactNo = sellerContactNo;
	}

	public Double getLoanAmount() {
		return loanAmount;
	}

	public void setLoanAmount(Double loanAmount) {
		this.loanAmount = loanAmount;
	}

	public Double getTenureYear() {
		return tenureYear;
	}

	public void setTenureYear(Double tenureYear) {
		this.tenureYear = tenureYear;
	}

	public String getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(String interestRate) {
		this.interestRate = interestRate;
	}

	public String getIsFinancePremium() {
		return isFinancePremium;
	}

	public void setIsFinancePremium(String isFinancePremium) {
		this.isFinancePremium = isFinancePremium;
	}

	public String getTentativeEMI() {
		return tentativeEMI;
	}

	public void setTentativeEMI(String tentativeEMI) {
		this.tentativeEMI = tentativeEMI;
	}

	public Double getAge() {
		return age;
	}

	public void setAge(Double age) {
		this.age = age;
	}

	public Double getTotalPremium() {
		return totalPremium;
	}

	public void setTotalPremium(Double totalPremium) {
		this.totalPremium = totalPremium;
	}

	public String getIncrementalEMI() {
		return incrementalEMI;
	}

	public void setIncrementalEMI(String incrementalEMI) {
		this.incrementalEMI = incrementalEMI;
	}

	public String getIsMedicalUnderWritingRequired() {
		return isMedicalUnderWritingRequired;
	}

	public void setIsMedicalUnderWritingRequired(String isMedicalUnderWritingRequired) {
		this.isMedicalUnderWritingRequired = isMedicalUnderWritingRequired;
	}

	public String getIsFinancialUnderWritingRequired() {
		return isFinancialUnderWritingRequired;
	}

	public void setIsFinancialUnderWritingRequired(String isFinancialUnderWritingRequired) {
		this.isFinancialUnderWritingRequired = isFinancialUnderWritingRequired;
	}

	public String getIsInterested() {
		return isInterested;
	}

	public void setIsInterested(String isInterested) {
		this.isInterested = isInterested;
	}

	public String getReseanForNotInterested() {
		return reseanForNotInterested;
	}

	public void setReseanForNotInterested(String reseanForNotInterested) {
		this.reseanForNotInterested = reseanForNotInterested;
	}

	public String getSchemeType() {
		return schemeType;
	}

	public void setSchemeType(String schemeType) {
		this.schemeType = schemeType;
	}

	public String getLoanType() {
		return loanType;
	}

	public void setLoanType(String loanType) {
		this.loanType = loanType;
	}

	public Double getTenureEligible() {
		return tenureEligible;
	}

	public void setTenureEligible(Double tenureEligible) {
		this.tenureEligible = tenureEligible;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getChecksum() {
		return checksum;
	}

	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}

	public Integer getBaseOrBaseWithCI() {
		return baseOrBaseWithCI;
	}

	public void setBaseOrBaseWithCI(Integer baseOrBaseWithCI) {
		this.baseOrBaseWithCI = baseOrBaseWithCI;
	}

	public Integer getPercentageSumAssured() {
		return percentageSumAssured;
	}

	public void setPercentageSumAssured(Integer percentageSumAssured) {
		this.percentageSumAssured = percentageSumAssured;
	}

	public Double getCiRiderSumAssured() {
		return ciRiderSumAssured;
	}

	public void setCiRiderSumAssured(Double ciRiderSumAssured) {
		this.ciRiderSumAssured = ciRiderSumAssured;
	}

	public Integer getCiOption() {
		return ciOption;
	}

	public void setCiOption(Integer ciOption) {
		this.ciOption = ciOption;
	}

	public Integer getCiTenureYears() {
		return ciTenureYears;
	}

	public void setCiTenureYears(Integer ciTenureYears) {
		this.ciTenureYears = ciTenureYears;
	}

	public Integer getCiRiderPermium() {
		return ciRiderPermium;
	}

	public void setCiRiderPermium(Integer ciRiderPermium) {
		this.ciRiderPermium = ciRiderPermium;
	}

	public Integer getBaseWithCIPremium() {
		return baseWithCIPremium;
	}

	public void setBaseWithCIPremium(Integer baseWithCIPremium) {
		this.baseWithCIPremium = baseWithCIPremium;
	}

	public String getMedicalUWCI() {
		return medicalUWCI;
	}

	public void setMedicalUWCI(String medicalUWCI) {
		this.medicalUWCI = medicalUWCI;
	}

	public String getFinancialUWCI() {
		return financialUWCI;
	}

	public void setFinancialUWCI(String financialUWCI) {
		this.financialUWCI = financialUWCI;
	}

	public String getOverallUWMedical() {
		return overallUWMedical;
	}

	public void setOverallUWMedical(String overallUWMedical) {
		this.overallUWMedical = overallUWMedical;
	}

	public String getOverallUWFinancial() {
		return overallUWFinancial;
	}

	public void setOverallUWFinancial(String overallUWFinancial) {
		this.overallUWFinancial = overallUWFinancial;
	}

	public Integer getTentativeEMIBaseWithCI() {
		return tentativeEMIBaseWithCI;
	}

	public void setTentativeEMIBaseWithCI(Integer tentativeEMIBaseWithCI) {
		this.tentativeEMIBaseWithCI = tentativeEMIBaseWithCI;
	}

	public Integer getIncrementalEMIBaseWithCI() {
		return incrementalEMIBaseWithCI;
	}

	public void setIncrementalEMIBaseWithCI(Integer incrementalEMIBaseWithCI) {
		this.incrementalEMIBaseWithCI = incrementalEMIBaseWithCI;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getAddress3() {
		return address3;
	}

	public void setAddress3(String address3) {
		this.address3 = address3;
	}

	public String getPinCode() {
		return pinCode;
	}

	public void setPinCode(String pinCode) {
		this.pinCode = pinCode;
	}

	public String getTown() {
		return town;
	}

	public void setTown(String town) {
		this.town = town;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getMph() {
		return mph;
	}

	public void setMph(String mph) {
		this.mph = mph;
	}

	public String getRoId() {
		return roId;
	}

	public void setRoId(String roId) {
		this.roId = roId;
	}

	public String getSmId() {
		return smId;
	}

	public void setSmId(String smId) {
		this.smId = smId;
	}

	@Override
	public String toString() {
		return "PremiumCalculator [loanAppNumber=" + loanAppNumber + ", sumAssured=" + sumAssured + ", dob=" + dob
				+ ", sellerContactNo=" + sellerContactNo + ", loanAmount=" + loanAmount + ", tenureYear=" + tenureYear
				+ ", interestRate=" + interestRate + ", isFinancePremium=" + isFinancePremium + ", tentativeEMI="
				+ tentativeEMI + ", age=" + age + ", totalPremium=" + totalPremium + ", incrementalEMI="
				+ incrementalEMI + ", isMedicalUnderWritingRequired=" + isMedicalUnderWritingRequired
				+ ", isFinancialUnderWritingRequired=" + isFinancialUnderWritingRequired + ", isInterested="
				+ isInterested + ", reseanForNotInterested=" + reseanForNotInterested + ", schemeType=" + schemeType
				+ ", loanType=" + loanType + ", tenureEligible=" + tenureEligible + ", channel=" + channel
				+ ", checksum=" + checksum + ", baseOrBaseWithCI=" + baseOrBaseWithCI + ", percentageSumAssured="
				+ percentageSumAssured + ", ciRiderSumAssured=" + ciRiderSumAssured + ", ciOption=" + ciOption
				+ ", ciTenureYears=" + ciTenureYears + ", ciRiderPermium=" + ciRiderPermium + ", baseWithCIPremium="
				+ baseWithCIPremium + ", medicalUWCI=" + medicalUWCI + ", financialUWCI=" + financialUWCI
				+ ", overallUWMedical=" + overallUWMedical + ", overallUWFinancial=" + overallUWFinancial
				+ ", tentativeEMIBaseWithCI=" + tentativeEMIBaseWithCI + ", incrementalEMIBaseWithCI="
				+ incrementalEMIBaseWithCI + ", gender=" + gender + ", street=" + street + ", address1=" + address1
				+ ", address2=" + address2 + ", address3=" + address3 + ", pinCode=" + pinCode + ", town=" + town
				+ ", state=" + state + ", country=" + country + ", mph=" + mph + ", roId=" + roId + ", smId=" + smId
				+ "]";
	}

}
