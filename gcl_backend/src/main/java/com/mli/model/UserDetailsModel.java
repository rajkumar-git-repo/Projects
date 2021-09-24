package com.mli.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.mli.modal.CamResponseModel;
import com.mli.model.otp.CustOTPVerificationModel;

/**
 * @author Nikhilesh.Tiwari
 *
 */
@JsonInclude(Include.NON_NULL)
public class UserDetailsModel extends BaseModel {

	private static final long serialVersionUID = -4665322952646502185L;

	private NomineeDetailsModel nomineeDetails;
	private MandatoryDeclarationModel mandatoryDeclaration;
	private HealthDeclarationModel healthDeclaration;
	private SFQHealthDeclarationModel sfqHealthDeclaration;
	private CustomerDetailsModel customerDetails;
	private CustOTPVerificationModel custOTPVerificationModel;

	private NationalityDetail nationalityDetails;
	
	private CamResponseModel camResponseModel;
	private Boolean camReportUrlIsExits;
	private Integer steps;
	private String proposalNumber;
	private String sellerContNumber;
	private String homeLoanGroupPolicyNo;

	private Long sfvTimeStamp;
	private String status;

	private String mliRMCode;
	private String mliSMCode;

	public NomineeDetailsModel getNomineeDetails() {
		return nomineeDetails;
	}

	public void setNomineeDetails(NomineeDetailsModel nomineeDetails) {
		this.nomineeDetails = nomineeDetails;
	}

	public MandatoryDeclarationModel getMandatoryDeclaration() {
		return mandatoryDeclaration;
	}

	public void setMandatoryDeclaration(MandatoryDeclarationModel mandatoryDeclaration) {
		this.mandatoryDeclaration = mandatoryDeclaration;
	}

	public HealthDeclarationModel getHealthDeclaration() {
		return healthDeclaration;
	}

	public void setHealthDeclaration(HealthDeclarationModel healthDeclaration) {
		this.healthDeclaration = healthDeclaration;
	}

	public SFQHealthDeclarationModel getSfqHealthDeclaration() {
		return sfqHealthDeclaration;
	}

	public void setSfqHealthDeclaration(SFQHealthDeclarationModel sfqHealthDeclaration) {
		this.sfqHealthDeclaration = sfqHealthDeclaration;
	}

	public CustomerDetailsModel getCustomerDetails() {
		return customerDetails;
	}

	public void setCustomerDetails(CustomerDetailsModel customerDetails) {
		this.customerDetails = customerDetails;
	}

	public Integer getSteps() {
		return steps;
	}

	public void setSteps(Integer steps) {
		this.steps = steps;
	}

	public String getProposalNumber() {
		return proposalNumber;
	}

	public void setProposalNumber(String proposalNumber) {
		this.proposalNumber = proposalNumber;
	}

	public CustOTPVerificationModel getCustOTPVerificationModel() {
		return custOTPVerificationModel;
	}

	public void setCustOTPVerificationModel(CustOTPVerificationModel custOTPVerificationModel) {
		this.custOTPVerificationModel = custOTPVerificationModel;
	}

	public String getSellerContNumber() {
		return sellerContNumber;
	}

	public void setSellerContNumber(String sellerContNumber) {
		this.sellerContNumber = sellerContNumber;
	}

	public String getHomeLoanGroupPolicyNo() {
		return homeLoanGroupPolicyNo;
	}

	public void setHomeLoanGroupPolicyNo(String homeLoanGroupPolicyNo) {
		this.homeLoanGroupPolicyNo = homeLoanGroupPolicyNo;
	}

	public NationalityDetail getNationalityDetails() {
		return nationalityDetails;
	}

	public void setNationalityDetails(NationalityDetail nationalityDetails) {
		this.nationalityDetails = nationalityDetails;
	}

	public Long getSfvTimeStamp() {
		return sfvTimeStamp;
	}

	public void setSfvTimeStamp(Long sfvTimeStamp) {
		this.sfvTimeStamp = sfvTimeStamp;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMliRMCode() {
		return mliRMCode;
	}

	public void setMliRMCode(String mliRMCode) {
		this.mliRMCode = mliRMCode;
	}

	public Boolean getCamReportUrlIsExits() {
		return camReportUrlIsExits;
	}

	public void setCamReportUrlIsExits(Boolean camReportUrlIsExits) {
		this.camReportUrlIsExits = camReportUrlIsExits;
	}

	public String getMliSMCode() {
		return mliSMCode;
	}
	public void setMliSMCode(String mliSMCode) {
		this.mliSMCode = mliSMCode;
	}
	

	public CamResponseModel getCamResponseModel() {
		return camResponseModel;
	}

	public void setCamResponseModel(CamResponseModel camResponseModel) {
		this.camResponseModel = camResponseModel;
	}

	@Override
	public String toString() {
		return "UserDetailsModel [nomineeDetails=" + nomineeDetails + ", mandatoryDeclaration=" + mandatoryDeclaration
				+ ", healthDeclaration=" + healthDeclaration + ", sfqHealthDeclaration=" + sfqHealthDeclaration
				+ ", customerDetails=" + customerDetails + ", custOTPVerificationModel=" + custOTPVerificationModel
				+ ", nationalityDetails=" + nationalityDetails + ", camResponseModel=" + camResponseModel
				+ ", camReportUrlIsExits=" + camReportUrlIsExits + ", steps=" + steps + ", proposalNumber="
				+ proposalNumber + ", sellerContNumber=" + sellerContNumber + ", homeLoanGroupPolicyNo="
				+ homeLoanGroupPolicyNo + ", sfvTimeStamp=" + sfvTimeStamp + ", status=" + status + ", mliRMCode="
				+ mliRMCode + ", mliSMCode=" + mliSMCode + "]";
	}
}
