package com.mli.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import com.mli.enums.MasterPolicyHolderName;

/**
 * @author Nikhilesh.Tiwari
 *
 */
@Entity
@Table(name = "mandatory_declaration")
public class MandatoryDeclarationEntity extends BaseEntity {

	private static final long serialVersionUID = 8444621770802753163L;

	@Id
	@Column(name = "mandatory_dcl_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long mandDclId;

	@NotNull
	@OneToOne
	@JoinColumn(name = "cust_detail_id")
	private CustomerDetailsEntity customerDtlId;

	@NotNull
	@Column(name = "policy_holder_name", length = 55)
	@Enumerated(EnumType.STRING)
	private MasterPolicyHolderName policyHolderName;

	@Column(name = "mandatory_declaration")
	private Boolean isMandatoryDeclaration;

	@NotNull
	@Column(name = "signed_date")
	private Long SignedDate;

	@NotNull
	@Column(name = "place", length = 30)
	private String place;

	@Version
	@Column(name = "version")
	private long version;

	public Long getMandDclId() {
		return mandDclId;
	}

	public void setMandDclId(Long mandDclId) {
		this.mandDclId = mandDclId;
	}

	public CustomerDetailsEntity getCustomerDtlId() {
		return customerDtlId;
	}

	public void setCustomerDtlId(CustomerDetailsEntity customerDtlId) {
		this.customerDtlId = customerDtlId;
	}

	public MasterPolicyHolderName getPolicyHolderName() {
		return policyHolderName;
	}

	public void setPolicyHolderName(MasterPolicyHolderName policyHolderName) {
		this.policyHolderName = policyHolderName;
	}

	public boolean getIsMandatoryDeclaration() {
		return isMandatoryDeclaration;
	}

	public void setIsMandatoryDeclaration(boolean isMandatoryDeclaration) {
		this.isMandatoryDeclaration = isMandatoryDeclaration;
	}

	public Long getSignedDate() {
		return SignedDate;
	}

	public void setSignedDate(Long signedDate) {
		SignedDate = signedDate;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

}
