package com.mli.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import com.mli.enums.LoanType;
import com.mli.enums.RelationshipGpPolicyHolder;
import com.mli.enums.SchemeType;
import com.mli.enums.Status;

/**
 * @author Nikhilesh.Tiwari
 *
 */
@Entity
@Table(name = "customer_details")
public class CustomerDetailsEntity extends BaseEntity {

	private static final long serialVersionUID = -8005969176534107776L;

	@Id
	@Column(name = "customer_detail_id", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long customerDtlId;

	@Column(name = "customer_first_name", length = 60)
	private String customerFirstName;

	@Column(name = "customer_last_name", length = 60)
	private String customerLastName;

	@Column(name = "loan_type", length = 55)
	@Enumerated(EnumType.STRING)
	private LoanType loanType;

	@Column(name = "scheme_type", length = 55)
	@Enumerated(EnumType.STRING)
	private SchemeType schemeType;

	@Column(name = "policy_holder_name", length = 55)
	private String masterPolicyHolderName;

	@NotNull
	@Column(name = "loan_app_number", length = 16, unique = true)
	private String loanAppNumber;

	@Column(name = "proposal_number", unique = true, length = 55)
	private String proposalNumber;

	@Column(name = "cust_mobile_no")
	private Long custMobileNo;

	@Column(name = "cust_emailId", length = 55)
	private String custEmailId;

	@Column(name = "hdf_signed_date")
	private Long hDFSignedDate;

	@NotNull
	@Column(name = "sum_assured", columnDefinition = "double precision DEFAULT 0")
	private double sumAssured;

	@Enumerated(EnumType.STRING)
	@Column(name = "relationship_with_gp_policy_holder", length = 55)
	private RelationshipGpPolicyHolder relationshipGpPolicyHolder;

	@ManyToOne
	@JoinColumn(name = "seller_detail_id")
	private SellerDetailEntity slrDtlId;

	@Column(name = "app_status", length = 55)
	@Enumerated(EnumType.STRING)
	private Status status;

	@Column(name = "application_step", length = 55)
	@Enumerated(EnumType.STRING)
	private Status appStep;

	@NotNull
	@Column(name = "dob")
	private Long dob;

	@Column(name = "loan_tenure", columnDefinition = "double precision DEFAULT 0")
	private Double loanTenure;

	@Version
	@Column(name = "version")
	private long version;

	@Column(name = "application_completion_date")
	private Long appCompletionDate;

	@Column(name = "cust_otp_verified_date")
	private Long custOtpVerifiedDate;

	@Column(name = "adhaar_number", length = 12)
	private String adhaarNumber;

	@Column(name = "verified_otp")
	private String verifiedOtp;

	// -------------------------- added new fields
	// -----------------------------------//
	// for Premium calculator
	@Column(name = "loan_amount")
	private Double loanAmount;

	@Column(name = "interest_rate")
	private String interestRate;

	@Column(name = "finance_premium")
	private String isFinancePremium;

	@Column(name = "tentative_emi")
	private String tentativeEMI;

	@Column(name = "total_premium")
	private Double totalPremium;

	@Column(name = "incremental_emi")
	private String incrementalEMI;

	@Column(name = "medical_underWriting_required")
	private String isMedicalUnderWritingRequired;

	@Column(name = "financial_underWriting_required")
	private String isFinancialUnderWritingRequired;

	@Column(name = "interested")
	private String isInterested;

	@Column(name = "resean_for_interested")
	private String reseanForInterested;

	@Column(name = "gender")
	private String gender;

	@Column(name = "age")
	private Double age;

	@Column(name = "tenure_eligible")
	private Double tenureEligible;

	// for reflexive question
	@Column(name = "file_upload_path")
	private String fileUploadPath;

	@Column(name = "passport_upload_date")
	private Long passportUploadDate;

	// @OneToMany(cascade = CascadeType.ALL,mappedBy="customerDetailsEntity")
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "customer_answer", joinColumns = {
			@JoinColumn(referencedColumnName = "customer_detail_id") }, inverseJoinColumns = {
					@JoinColumn(referencedColumnName = "reflexive_answer_id") })
	private List<ReflexiveAnswerEntity> answerEntity;

	@Column(name = "nationality")
	private String nationality;

	@Column(name = "occupation")
	private String occupation;

	@Column(name = "other_palce")
	private String otherPalce;

	@Column(name = "sent_for_verification_timestamp")
	private Long sfvTimeStamp;

	// for proposer
	@OneToOne(cascade = CascadeType.ALL, mappedBy = "customerDetailsEntity")
	private ProposerDetailEntity proposerEntity;

	/**
	 * CI rider's fields
	 */

	@Column(name = "base_with_or_without_ci")
	private String baseOrBaseWithCI;

	@Column(name = "percentage_sum_assured")
	private Integer percentageOfSumAssured;

	@Column(name = "ci_rider_sum_assured")
	private Double ciRiderSumAssured;

	@Column(name = "ci_option")
	private String ciOption;

	@Column(name = "ci_tenure_years")
	private Integer ciTenureYears;
	
	@Column(name = "ci_rider_premium")
	private Integer ciRiderPermium;
	
	@Column(name = "base_withci_premium")
	private Integer baseWithCIPremium;
	
	@Column(name = "medical_uw_ci")
	private String medicalUWCI;
	
	@Column(name = "financial_uw_ci")
	private String financialUWCI;
	
	@Column(name = "overall_uw_medical")
	private String overallUWMedical;
	
	@Column(name = "overall_uw_financila")
	private String overallUWFinancial; 
	
	@Column(name = "tentative_emi_base_withci")
	private Integer tentativeEMIBaseWithCI;
	
	@Column(name = "incremental_emi_basewith_ci")
	private Integer incrementalEMIBaseWithCI;
	
	@Column(name = "street" ,length = 30)
	private String street;
	
	@Column(name = "address1",length = 30)
	private String address1;
	
	@Column(name = "address2",length = 30)
	private String address2;
	
	@Column(name = "address3",length = 30)
	private String address3;
	
	@Column(name = "town",length = 30)
	private String town;
	
	@Column(name = "pinCode")
	private String pinCode;
	
	@Column(name = "state")
	private String state;
	
	@Column(name = "country")
	private String country;
	
	
	@Column(name = "form_status", length = 55)
	private String formStatus;
	
	@Column(name = "health_type")
	private String healthType;
	
	@Column(name = "ro_id")
	private String roId;
	
	@Column(name = "sm_id")
	private String smId;
	

	public Long getCustomerDtlId() {
		return customerDtlId;
	}

	public void setCustomerDtlId(Long customerDtlId) {
		this.customerDtlId = customerDtlId;
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

	public LoanType getLoanType() {
		return loanType;
	}

	public void setLoanType(LoanType loanType) {
		this.loanType = loanType;
	}

	public SchemeType getSchemeType() {
		return schemeType;
	}

	public void setSchemeType(SchemeType schemeType) {
		this.schemeType = schemeType;
	}

	public String getLoanAppNumber() {
		return loanAppNumber;
	}

	public void setLoanAppNumber(String loanAppNumber) {
		this.loanAppNumber = loanAppNumber;
	}

	public String getProposalNumber() {
		return proposalNumber;
	}

	public void setProposalNumber(String proposalNumber) {
		this.proposalNumber = proposalNumber;
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

	public Long gethDFSignedDate() {
		return hDFSignedDate;
	}

	public void sethDFSignedDate(Long hDFSignedDate) {
		this.hDFSignedDate = hDFSignedDate;
	}

	public double getSumAssured() {
		return sumAssured;
	}

	public void setSumAssured(double sumAssured) {
		this.sumAssured = sumAssured;
	}

	public RelationshipGpPolicyHolder getRelationshipGpPolicyHolder() {
		return relationshipGpPolicyHolder;
	}

	public void setRelationshipGpPolicyHolder(RelationshipGpPolicyHolder relationshipGpPolicyHolder) {
		this.relationshipGpPolicyHolder = relationshipGpPolicyHolder;
	}

	public SellerDetailEntity getSlrDtlId() {
		return slrDtlId;
	}

	public void setSlrDtlId(SellerDetailEntity slrDtlId) {
		this.slrDtlId = slrDtlId;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Status getAppStep() {
		return appStep;
	}

	public void setAppStep(Status appStep) {
		this.appStep = appStep;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	public String getMasterPolicyHolderName() {
		return masterPolicyHolderName;
	}

	public void setMasterPolicyHolderName(String masterPolicyHolderName) {
		this.masterPolicyHolderName = masterPolicyHolderName;
	}

	public Long getDob() {
		return dob;
	}

	public void setDob(Long dob) {
		this.dob = dob;
	}

	public Double getLoanTenure() {
		return loanTenure;
	}

	public void setLoanTenure(Double loanTenure) {
		this.loanTenure = loanTenure;
	}

	public Long getAppCompletionDate() {
		return appCompletionDate;
	}

	public void setAppCompletionDate(Long appCompletionDate) {
		this.appCompletionDate = appCompletionDate;
	}

	public Long getCustOtpVerifiedDate() {
		return custOtpVerifiedDate;
	}

	public void setCustOtpVerifiedDate(Long custOtpVerifiedDate) {
		this.custOtpVerifiedDate = custOtpVerifiedDate;
	}

	public String getAdhaarNumber() {
		return adhaarNumber;
	}

	public void setAdhaarNumber(String adhaarNumber) {
		this.adhaarNumber = adhaarNumber;
	}

	public Double getLoanAmount() {
		return loanAmount;
	}

	public void setLoanAmount(Double loanAmount) {
		this.loanAmount = loanAmount;
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

	public String getReseanForInterested() {
		return reseanForInterested;
	}

	public void setReseanForInterested(String reseanForInterested) {
		this.reseanForInterested = reseanForInterested;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public List<ReflexiveAnswerEntity> getAnswerEntity() {
		return answerEntity;
	}

	public void setAnswerEntity(List<ReflexiveAnswerEntity> answerEntity) {
		this.answerEntity = answerEntity;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public Double getAge() {
		return age;
	}

	public void setAge(Double age) {
		this.age = age;
	}

	public String getFileUploadPath() {
		return fileUploadPath;
	}

	public void setFileUploadPath(String fileUploadPath) {
		this.fileUploadPath = fileUploadPath;
	}

	public ProposerDetailEntity getProposerEntity() {
		return proposerEntity;
	}

	public void setProposerEntity(ProposerDetailEntity proposerEntity) {
		this.proposerEntity = proposerEntity;
	}

	public String getOtherPalce() {
		return otherPalce;
	}

	public void setOtherPalce(String otherPalce) {
		this.otherPalce = otherPalce;
	}

	public Double getTenureEligible() {
		return tenureEligible;
	}

	public void setTenureEligible(Double tenureEligible) {
		this.tenureEligible = tenureEligible;
	}

	public Long getPassportUploadDate() {
		return passportUploadDate;
	}

	public void setPassportUploadDate(Long passportUploadDate) {
		this.passportUploadDate = passportUploadDate;
	}

	public Long getSfvTimeStamp() {
		return sfvTimeStamp;
	}

	public void setSfvTimeStamp(Long sfvTimeStamp) {
		this.sfvTimeStamp = sfvTimeStamp;
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

	public String getCiOption() {
		return ciOption;
	}

	public void setCiOption(String ciOption) {
		this.ciOption = ciOption;
	}

	public Integer getCiTenureYears() {
		return ciTenureYears;
	}

	public void setCiTenureYears(Integer ciTenureYears) {
		this.ciTenureYears = ciTenureYears;
	}
	public void setCiRiderSumAssured(Double ciRiderSumAssured) {
		this.ciRiderSumAssured = ciRiderSumAssured;
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

	public String getFormStatus() {
		return formStatus;
	}

	public void setFormStatus(String formStatus) {
		this.formStatus = formStatus;
	}
	
	public String getHealthType() {
		return healthType;
	}

	public void setHealthType(String healthType) {
		this.healthType = healthType;
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
		return "CustomerDetailsEntity [customerDtlId=" + customerDtlId + ", customerFirstName=" + customerFirstName
				+ ", customerLastName=" + customerLastName + ", loanType=" + loanType + ", schemeType=" + schemeType
				+ ", masterPolicyHolderName=" + masterPolicyHolderName + ", loanAppNumber=" + loanAppNumber
				+ ", proposalNumber=" + proposalNumber + ", custMobileNo=" + custMobileNo + ", custEmailId="
				+ custEmailId + ", hDFSignedDate=" + hDFSignedDate + ", sumAssured=" + sumAssured
				+ ", relationshipGpPolicyHolder=" + relationshipGpPolicyHolder + ", slrDtlId=" + slrDtlId + ", status="
				+ status + ", appStep=" + appStep + ", dob=" + dob + ", loanTenure=" + loanTenure + ", version="
				+ version + ", appCompletionDate=" + appCompletionDate + ", custOtpVerifiedDate=" + custOtpVerifiedDate
				+ ", adhaarNumber=" + adhaarNumber + ", verifiedOtp=" + verifiedOtp + ", loanAmount=" + loanAmount
				+ ", interestRate=" + interestRate + ", isFinancePremium=" + isFinancePremium + ", tentativeEMI="
				+ tentativeEMI + ", totalPremium=" + totalPremium + ", incrementalEMI=" + incrementalEMI
				+ ", isMedicalUnderWritingRequired=" + isMedicalUnderWritingRequired
				+ ", isFinancialUnderWritingRequired=" + isFinancialUnderWritingRequired + ", isInterested="
				+ isInterested + ", reseanForInterested=" + reseanForInterested + ", gender=" + gender + ", age=" + age
				+ ", tenureEligible=" + tenureEligible + ", fileUploadPath=" + fileUploadPath + ", passportUploadDate="
				+ passportUploadDate + ", answerEntity=" + answerEntity + ", nationality=" + nationality
				+ ", occupation=" + occupation + ", otherPalce=" + otherPalce + ", sfvTimeStamp=" + sfvTimeStamp
				+ ", proposerEntity=" + proposerEntity + ", baseOrBaseWithCI=" + baseOrBaseWithCI
				+ ", percentageOfSumAssured=" + percentageOfSumAssured + ", ciRiderSumAssured=" + ciRiderSumAssured
				+ ", ciOption=" + ciOption + ", ciTenureYears=" + ciTenureYears + ", ciRiderPermium=" + ciRiderPermium
				+ ", baseWithCIPremium=" + baseWithCIPremium + ", medicalUWCI=" + medicalUWCI + ", financialUWCI="
				+ financialUWCI + ", overallUWMedical=" + overallUWMedical + ", overallUWFinancial="
				+ overallUWFinancial + ", tentativeEMIBaseWithCI=" + tentativeEMIBaseWithCI
				+ ", incrementalEMIBaseWithCI=" + incrementalEMIBaseWithCI + ", street=" + street + ", address1="
				+ address1 + ", address2=" + address2 + ", address3=" + address3 + ", town=" + town + ", pinCode="
				+ pinCode + ", state=" + state + ", country=" + country + ", formStatus=" + formStatus + ", healthType="
				+ healthType + ", roId=" + roId + ", smId=" + smId + "]";
	}
	
}
