package com.mli.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.mli.entity.CamReportDetailsEntity;
import com.mli.enums.Status;
import com.mli.modal.FileUploadModel;

/**
 * @author Nikhilesh.Tiwari
 *
 */
@JsonInclude(Include.NON_NULL)
public class CustomerDetailsModel extends BaseModel {

	private static final long serialVersionUID = 2079867946901387254L;
	private Long customerDtlId;
	private String customerFirstName;
	private String customerLastName;
	private String loanType;
	private String schemeType;
	private String masterPolicyholderName;
	private String relationshipGpPolicyHolder;
	private String loanAppNumber;
	private Double sumAssured;
	private Long custMobileNo;
	private String custEmailId;
	private String proposalNumber;
	private String dob;
	private Double loanTenure;
	private Long appCompletionDate;
	private String verifiedOtp;
	private String adhaarNumber;
	private String loanTypeDesc;

	// added by Devendra.Kumar
	private String occupation;
	private String nationality;
	private Boolean isProposer;
	private ProposerDetailModel proposerDetails;
	private String gender;
	private Double loanAmount;
	private List<AnswerModel> occupationAnswerModel;
	private Object savedAnswers;
	private String otherPlace;

	// field added for front end
	private boolean flushNationality;
	private String status;

	private String selletName;
	private Long sellerMobileNumber;

	private String interestRate;
	private String financePremium;
	private String tenureEligible;

	private String tentativeEMI;
	private String incrementalEMI;
	private String medicalRequired;
	private String financialUWRequired;
	private String interested;
	private String reasonForNotInterested;

	private Long verifiedDate;
	private Long createdOn;

	private String totalPremium;

	private String sourceEmpCode;
	private String racLocationMapping;
	private String mliSalesManager;
	private String mliRM;

	private String baseOrBaseWithCI;
	private Integer percentageOfSumAssured;
	private Double ciRiderSumAssured;
	private String ciOption;
	private Integer ciTenureYears;
	private List<CamReportDetailsEntity> camReportUrls;
	
	private Boolean camReportUrlIsExits;
	
	private String fullAddress;
	
	private Integer ciRiderPermium;
	private Integer baseWithCIPremium;
	
	private String medicalUWCI;
	private String financialUWCI;
	
	private String overallUWMedical;
	private String overallUWFinancial; 
	
	private Integer tentativeEMIBaseWithCI;
	private Integer incrementalEMIBaseWithCI;
	
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
	
	private String formStatus;
	
	private List<FileUploadModel> physicalFileList;
	
	public String paymentLink;
	
	private String healthType;
	
	private List<CovidReportModel> covidReportList;
	
	private String roId;
	private String smId;

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

	public String getFullAddress() {
		return fullAddress;
	}

	public void setFullAddress(String fullAddress) {
		this.fullAddress = fullAddress;
	}

	public String getCustomerFirstName() {
		return customerFirstName;
	}

	public void setCustomerFirstName(String customerFirstName) {
		this.customerFirstName = customerFirstName;
	}

	public String getCustomerLastName() {
		return customerLastName;
	}

	public void setCustomerLastName(String customerLastName) {
		this.customerLastName = customerLastName;
	}

	public String getLoanType() {
		return loanType;
	}

	public void setLoanType(String loanType) {
		this.loanType = loanType;
	}

	public String getSchemeType() {
		return schemeType;
	}

	public void setSchemeType(String schemeType) {
		this.schemeType = schemeType;
	}

	public String getMasterPolicyholderName() {
		return masterPolicyholderName;
	}

	public void setMasterPolicyholderName(String masterPolicyholderName) {
		this.masterPolicyholderName = masterPolicyholderName;
	}

	public String getRelationshipGpPolicyHolder() {
		return relationshipGpPolicyHolder;
	}

	public void setRelationshipGpPolicyHolder(String relationshipGpPolicyHolder) {
		this.relationshipGpPolicyHolder = relationshipGpPolicyHolder;
	}

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

	public Long getCustMobileNo() {
		return custMobileNo;
	}

	public void setCustMobileNo(Long custMobileNo) {
		this.custMobileNo = custMobileNo;
	}

	public String getCustEmailId() {
		return custEmailId;
	}

	public void setCustEmailId(String custEmailId) {
		this.custEmailId = custEmailId;
	}

	public String getProposalNumber() {
		return proposalNumber;
	}

	public void setProposalNumber(String proposalNumber) {
		this.proposalNumber = proposalNumber;
	}

	public Double getLoanTenure() {
		return loanTenure;
	}

	public void setLoanTenure(Double loanTenure) {
		this.loanTenure = loanTenure;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public Long getAppCompletionDate() {
		return appCompletionDate;
	}

	public void setAppCompletionDate(Long appCompletionDate) {
		this.appCompletionDate = appCompletionDate;
	}

	public String getAdhaarNumber() {
		return adhaarNumber;
	}

	public void setAdhaarNumber(String adhaarNumber) {
		this.adhaarNumber = adhaarNumber;
	}

	public String getLoanTypeDesc() {
		return loanTypeDesc;
	}

	public void setLoanTypeDesc(String loanTypeDesc) {
		this.loanTypeDesc = loanTypeDesc;
	}

	public Long getCustomerDtlId() {
		return customerDtlId;
	}

	public void setCustomerDtlId(Long customerDtlId) {
		this.customerDtlId = customerDtlId;
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public Boolean getIsProposer() {
		return isProposer;
	}

	public void setIsProposer(Boolean isProposer) {
		this.isProposer = isProposer;
	}

	public ProposerDetailModel getProposerDetails() {
		return proposerDetails;
	}

	public void setProposerDetails(ProposerDetailModel proposerDetails) {
		this.proposerDetails = proposerDetails;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public Double getLoanAmount() {
		return loanAmount;
	}

	public void setLoanAmount(Double loanAmount) {
		this.loanAmount = loanAmount;
	}

	public List<AnswerModel> getOccupationAnswerModel() {
		return occupationAnswerModel;
	}

	public void setOccupationAnswerModel(List<AnswerModel> occupationAnswerModel) {
		this.occupationAnswerModel = occupationAnswerModel;
	}

	public Object getSavedAnswers() {
		return savedAnswers;
	}

	public void setSavedAnswers(Object savedAnswers) {
		this.savedAnswers = savedAnswers;
	}

	public String getOtherPlace() {
		return otherPlace;
	}

	public void setOtherPlace(String otherPlace) {
		this.otherPlace = otherPlace;
	}

	public boolean isFlushNationality() {
		return flushNationality;
	}

	public void setFlushNationality(boolean flushNationality) {
		this.flushNationality = flushNationality;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSelletName() {
		return selletName;
	}

	public void setSelletName(String selletName) {
		this.selletName = selletName;
	}

	public Long getSellerMobileNumber() {
		return sellerMobileNumber;
	}

	public void setSellerMobileNumber(Long sellerMobileNumber) {
		this.sellerMobileNumber = sellerMobileNumber;
	}

	public String getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(String interestRate) {
		this.interestRate = interestRate;
	}

	public String getFinancePremium() {
		return financePremium;
	}

	public void setFinancePremium(String financePremium) {
		this.financePremium = financePremium;
	}

	public String getTenureEligible() {
		return tenureEligible;
	}

	public void setTenureEligible(String tenureEligible) {
		this.tenureEligible = tenureEligible;
	}

	public String getTentativeEMI() {
		return tentativeEMI;
	}

	public void setTentativeEMI(String tentativeEMI) {
		this.tentativeEMI = tentativeEMI;
	}

	public String getIncrementalEMI() {
		return incrementalEMI;
	}

	public void setIncrementalEMI(String incrementalEMI) {
		this.incrementalEMI = incrementalEMI;
	}

	public String getMedicalRequired() {
		return medicalRequired;
	}

	public void setMedicalRequired(String medicalRequired) {
		this.medicalRequired = medicalRequired;
	}

	public String getFinancialUWRequired() {
		return financialUWRequired;
	}

	public void setFinancialUWRequired(String financialUWRequired) {
		this.financialUWRequired = financialUWRequired;
	}

	public String getInterested() {
		return interested;
	}

	public void setInterested(String interested) {
		this.interested = interested;
	}

	public String getReasonForNotInterested() {
		return reasonForNotInterested;
	}

	public void setReasonForNotInterested(String reasonForNotInterested) {
		this.reasonForNotInterested = reasonForNotInterested;
	}

	public Long getVerifiedDate() {
		return verifiedDate;
	}

	public void setVerifiedDate(Long verifiedDate) {
		this.verifiedDate = verifiedDate;
	}

	public Long getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Long createdOn) {
		this.createdOn = createdOn;
	}

	public String getTotalPremium() {
		return totalPremium;
	}

	public void setTotalPremium(String totalPremium) {
		this.totalPremium = totalPremium;
	}

	public String getSourceEmpCode() {
		return sourceEmpCode;
	}

	public void setSourceEmpCode(String sourceEmpCode) {
		this.sourceEmpCode = sourceEmpCode;
	}

	public String getRacLocationMapping() {
		return racLocationMapping;
	}

	public void setRacLocationMapping(String racLocationMapping) {
		this.racLocationMapping = racLocationMapping;
	}

	public String getMliSalesManager() {
		return mliSalesManager;
	}

	public void setMliSalesManager(String mliSalesManager) {
		this.mliSalesManager = mliSalesManager;
	}

	public String getMliRM() {
		return mliRM;
	}

	public void setMliRM(String mliRM) {
		this.mliRM = mliRM;
	}

	public String getVerifiedOtp() {
		return verifiedOtp;
	}

	public void setVerifiedOtp(String verifiedOtp) {
		this.verifiedOtp = verifiedOtp;
	}

	public String getBaseOrBaseWithCI() {
		return baseOrBaseWithCI;
	}

	public void setBaseOrBaseWithCI(String baseOrBaseWithCI) {
		this.baseOrBaseWithCI = baseOrBaseWithCI;
	}

	public Integer getPercentageOfSumAssured() {
		return percentageOfSumAssured;
	}

	public void setPercentageOfSumAssured(Integer percentageOfSumAssured) {
		this.percentageOfSumAssured = percentageOfSumAssured;
	}

	public Double getCiRiderSumAssured() {
		return ciRiderSumAssured;
	}

	public void setCiRiderSumAssured(Double ciRiderSumAssured) {
		this.ciRiderSumAssured = ciRiderSumAssured;
	}

	public String getCiOption() {
		return ciOption;
	}

	public void setCiOption(String ciOption) {
		this.ciOption = ciOption;
	}

	public Integer getCiTenureYears() {
		return ciTenureYears;
	}

	public Boolean getCamReportUrlIsExits() {
		return camReportUrlIsExits;
	}

	public void setCamReportUrlIsExits(Boolean camReportUrlIsExits) {
		this.camReportUrlIsExits = camReportUrlIsExits;
	}

	public void setCiTenureYears(Integer ciTenureYears) {
		this.ciTenureYears = ciTenureYears;
	}

	public List<CamReportDetailsEntity> getCamReportUrls() {
		return camReportUrls;
	}

	public void setCamReportUrls(List<CamReportDetailsEntity> camReportUrls) {
		this.camReportUrls = camReportUrls;
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

	public String getFormStatus() {
		return formStatus;
	}

	public void setFormStatus(String formStatus) {
		this.formStatus = formStatus;
	}

	public List<FileUploadModel> getPhysicalFileList() {
		return physicalFileList;
	}

	public void setPhysicalFileList(List<FileUploadModel> commonfileUpload) {
		this.physicalFileList = commonfileUpload;
	}

	public String getPaymentLink() {
		return paymentLink;
	}

	public void setPaymentLink(String paymentLink) {
		this.paymentLink = paymentLink;
	}

	public String getHealthType() {
		return healthType;
	}

	public void setHealthType(String healthType) {
		this.healthType = healthType;
	}
	
	public List<CovidReportModel> getCovidReportList() {
		return covidReportList;
	}

	public void setCovidReportList(List<CovidReportModel> covidReportList) {
		this.covidReportList = covidReportList;
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
		return "CustomerDetailsModel [customerDtlId=" + customerDtlId + ", customerFirstName=" + customerFirstName
				+ ", customerLastName=" + customerLastName + ", loanType=" + loanType + ", schemeType=" + schemeType
				+ ", masterPolicyholderName=" + masterPolicyholderName + ", relationshipGpPolicyHolder="
				+ relationshipGpPolicyHolder + ", loanAppNumber=" + loanAppNumber + ", sumAssured=" + sumAssured
				+ ", custMobileNo=" + custMobileNo + ", custEmailId=" + custEmailId + ", proposalNumber="
				+ proposalNumber + ", dob=" + dob + ", loanTenure=" + loanTenure + ", appCompletionDate="
				+ appCompletionDate + ", verifiedOtp=" + verifiedOtp + ", adhaarNumber=" + adhaarNumber
				+ ", loanTypeDesc=" + loanTypeDesc + ", occupation=" + occupation + ", nationality=" + nationality
				+ ", isProposer=" + isProposer + ", proposerDetails=" + proposerDetails + ", gender=" + gender
				+ ", loanAmount=" + loanAmount + ", occupationAnswerModel=" + occupationAnswerModel + ", savedAnswers="
				+ savedAnswers + ", otherPlace=" + otherPlace + ", flushNationality=" + flushNationality + ", status="
				+ status + ", selletName=" + selletName + ", sellerMobileNumber=" + sellerMobileNumber
				+ ", interestRate=" + interestRate + ", financePremium=" + financePremium + ", tenureEligible="
				+ tenureEligible + ", tentativeEMI=" + tentativeEMI + ", incrementalEMI=" + incrementalEMI
				+ ", medicalRequired=" + medicalRequired + ", financialUWRequired=" + financialUWRequired
				+ ", interested=" + interested + ", reasonForNotInterested=" + reasonForNotInterested
				+ ", verifiedDate=" + verifiedDate + ", createdOn=" + createdOn + ", totalPremium=" + totalPremium
				+ ", sourceEmpCode=" + sourceEmpCode + ", racLocationMapping=" + racLocationMapping
				+ ", mliSalesManager=" + mliSalesManager + ", mliRM=" + mliRM + ", baseOrBaseWithCI=" + baseOrBaseWithCI
				+ ", percentageOfSumAssured=" + percentageOfSumAssured + ", ciRiderSumAssured=" + ciRiderSumAssured
				+ ", ciOption=" + ciOption + ", ciTenureYears=" + ciTenureYears + ", camReportUrls=" + camReportUrls
				+ ", camReportUrlIsExits=" + camReportUrlIsExits + ", fullAddress=" + fullAddress + ", ciRiderPermium="
				+ ciRiderPermium + ", baseWithCIPremium=" + baseWithCIPremium + ", medicalUWCI=" + medicalUWCI
				+ ", financialUWCI=" + financialUWCI + ", overallUWMedical=" + overallUWMedical
				+ ", overallUWFinancial=" + overallUWFinancial + ", tentativeEMIBaseWithCI=" + tentativeEMIBaseWithCI
				+ ", incrementalEMIBaseWithCI=" + incrementalEMIBaseWithCI + ", street=" + street + ", address1="
				+ address1 + ", address2=" + address2 + ", address3=" + address3 + ", pinCode=" + pinCode + ", town="
				+ town + ", state=" + state + ", country=" + country + ", formStatus=" + formStatus
				+ ", physicalFileList=" + physicalFileList + ", paymentLink=" + paymentLink + ", healthType="
				+ healthType + ", covidReportList=" + covidReportList + ", roId=" + roId + ", smId=" + smId + "]";
	}
	
}
