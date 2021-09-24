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

@Entity
@Table(name = "creditcard_mandatory")
public class CreditCardMandatoryEntity extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "creditcard_mandatory_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long creditCardMandatoryId;

	@Version
	@Column(name = "version")
	private long version;

	@NotNull
	@OneToOne
	@JoinColumn(name = "creditcard_customer_id")
	private CreditCardCustomerEntity creditCardCustomerId;

	@NotNull
	@Column(name = "policy_holder_name", length = 55)
	@Enumerated(EnumType.STRING)
	private MasterPolicyHolderName policyHolderName;

	@Column(name = "mandatory_declaration")
	private String isMandatoryDeclaration;

	@NotNull
	@Column(name = "place", length = 30)
	private String place;

	public Long getCreditCardMandatoryId() {
		return creditCardMandatoryId;
	}

	public void setCreditCardMandatoryId(Long creditCardMandatoryId) {
		this.creditCardMandatoryId = creditCardMandatoryId;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	public CreditCardCustomerEntity getCreditCardCustomerId() {
		return creditCardCustomerId;
	}

	public void setCreditCardCustomerId(CreditCardCustomerEntity creditCardCustomerId) {
		this.creditCardCustomerId = creditCardCustomerId;
	}

	public MasterPolicyHolderName getPolicyHolderName() {
		return policyHolderName;
	}

	public void setPolicyHolderName(MasterPolicyHolderName policyHolderName) {
		this.policyHolderName = policyHolderName;
	}

	public String getIsMandatoryDeclaration() {
		return isMandatoryDeclaration;
	}

	public void setIsMandatoryDeclaration(String isMandatoryDeclaration) {
		this.isMandatoryDeclaration = isMandatoryDeclaration;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	@Override
	public String toString() {
		return "CreditCardMandatoryEntity [creditCardMandatoryId=" + creditCardMandatoryId + ", version=" + version
				+ ", creditCardCustomerId=" + creditCardCustomerId + ", policyHolderName=" + policyHolderName
				+ ", isMandatoryDeclaration=" + isMandatoryDeclaration + ", place=" + place + "]";
	}
}
