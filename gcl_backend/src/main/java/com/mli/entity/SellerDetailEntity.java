package com.mli.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import com.mli.enums.UserDesignation;

/**
 * @author Nikhilesh.Tiwari
 *
 */
@Entity
@Table(name = "seller_detail")
public class SellerDetailEntity extends BaseEntity {

	private static final long serialVersionUID = -4417173859165842934L;
	@Id
	@Column(name = "seller_detail_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long sellerDtlId;

	@NotNull
	@Column(name = "contact_no", unique = true)
	private Long contactNo;

	@Column(name = "seller_email_id")
	private String sellerEmailId;

	@Column(name = "seller_name", length = 55)
	private String sellerName;

	@Column(name = "connector", length = 55)
	@Enumerated(EnumType.STRING)
	private UserDesignation connector;

	@Column(name = "group_policy_number", length = 55)
	private String groupPolicyNumber;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "sellerDetailEntity", fetch = FetchType.EAGER)
	private Set<SellerBankEntity> sellerBankEntity;
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy = "sellerDetailEntity", fetch = FetchType.EAGER)
	private Set<LoanTypeSellerEntity> loanTypeSellerEntity;

	@OneToOne
	@JoinColumn(name = "user_id")
    private UserEntity userId;

	public Set<SellerBankEntity> getSellerBankEntity() {
		return sellerBankEntity;
	}

	public void setSellerBankEntity(Set<SellerBankEntity> sellerBankEntity) {
		this.sellerBankEntity = sellerBankEntity;
	}

	@Version
	@Column(name = "version")
	private long version;

	@Column(name = "is_password_set" )
	private Boolean isPasswordSet;
	
	@Column(name = "source_emp_code" )
	private String sourceEmpCode;
	
	@Column(name = "status" )
	private String status;
	
	@Column(name = "rac_location_mapping" )
	private String racLocationMapping;
	
	@Column(name = "mli_sales_manager" )
	private String mliSalesManager;
	
	@Column(name = "mli_sm_code" )
	private String mliSMCode;
	
	@Column(name = "mli_rm" )
	private String mliRM;
	
	@Column(name = "mli_rm_code" )
	private String mliRMCode;
	
	@Column(name = "role")
	private String role;
	
	@Column(name = "last_login", length = 30)
	private Long lastLogIn;
	
	@Column(name = "current_login", length = 30)
	private Long currentLogIn;
	
	

	public Long getSellerDtlId() {
		return sellerDtlId;
	}

	public void setSellerDtlId(Long sellerDtlId) {
		this.sellerDtlId = sellerDtlId;
	}

	public Long getContactNo() {
		return contactNo;
	}

	public void setContactNo(Long contactNo) {
		this.contactNo = contactNo;
	}

	public String getSellerEmailId() {
		return sellerEmailId;
	}

	public void setSellerEmailId(String sellerEmailId) {
		this.sellerEmailId = sellerEmailId;
	}

	public String getSellerName() {
		return sellerName;
	}

	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}

	public UserDesignation getConnector() {
		return connector;
	}

	public void setConnector(UserDesignation connector) {
		this.connector = connector;
	}

	public String getGroupPolicyNumber() {
		return groupPolicyNumber;
	}

	public void setGroupPolicyNumber(String groupPolicyNumber) {
		this.groupPolicyNumber = groupPolicyNumber;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	public boolean getIsPasswordSet() {
		return isPasswordSet;
	}

	public void setIsPasswordSet(boolean isPasswordSet) {
		this.isPasswordSet = isPasswordSet;
	}

	public UserEntity getUserId() {
		return userId;
	}

	public void setUserId(UserEntity userId) {
		this.userId = userId;
	}

	public String getSourceEmpCode() {
		return sourceEmpCode;
	}

	public void setSourceEmpCode(String sourceEmpCode) {
		this.sourceEmpCode = sourceEmpCode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getMliSMCode() {
		return mliSMCode;
	}

	public void setMliSMCode(String mliSMCode) {
		this.mliSMCode = mliSMCode;
	}

	public String getMliRM() {
		return mliRM;
	}

	public void setMliRM(String mliRM) {
		this.mliRM = mliRM;
	}

	public String getMliRMCode() {
		return mliRMCode;
	}

	public void setMliRMCode(String mliRMCode) {
		this.mliRMCode = mliRMCode;
	}

	public void setIsPasswordSet(Boolean isPasswordSet) {
		this.isPasswordSet = isPasswordSet;
	}
	
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Long getLastLogIn() {
		return lastLogIn;
	}

	public void setLastLogIn(Long lastLogIn) {
		this.lastLogIn = lastLogIn;
	}
	
	public Set<LoanTypeSellerEntity> getLoanTypeSellerEntity() {
		return loanTypeSellerEntity;
	}

	public void setLoanTypeSellerEntity(Set<LoanTypeSellerEntity> loanTypeSellerEntity) {
		this.loanTypeSellerEntity = loanTypeSellerEntity;
	}
	
	public Long getCurrentLogIn() {
		return currentLogIn;
	}

	public void setCurrentLogIn(Long currentLogIn) {
		this.currentLogIn = currentLogIn;
	}

	@Override
	public String toString() {
		return "SellerDetailEntity [sellerDtlId=" + sellerDtlId + ", contactNo=" + contactNo + ", sellerEmailId="
				+ sellerEmailId + ", sellerName=" + sellerName + ", connector=" + connector + ", groupPolicyNumber="
				+ groupPolicyNumber + ", sellerBankEntity=" + sellerBankEntity + ", loanTypeSellerEntity="
				+ loanTypeSellerEntity + ", userId=" + userId + ", version=" + version + ", isPasswordSet="
				+ isPasswordSet + ", sourceEmpCode=" + sourceEmpCode + ", status=" + status + ", racLocationMapping="
				+ racLocationMapping + ", mliSalesManager=" + mliSalesManager + ", mliSMCode=" + mliSMCode + ", mliRM="
				+ mliRM + ", mliRMCode=" + mliRMCode + ", role=" + role + ", lastLogIn=" + lastLogIn + ", currentLogIn="
				+ currentLogIn + "]";
	}
}
