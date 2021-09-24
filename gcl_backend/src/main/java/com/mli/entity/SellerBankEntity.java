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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import com.mli.enums.MasterPolicyHolderName;
import com.mli.enums.Status;

@Entity
@Table(name = "seller_banks")
public class SellerBankEntity extends BaseEntity {

	private static final long serialVersionUID = -3378765323876617861L;

	@Id
	@Column(name = "seller_bank_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long sellerBankId;

	@ManyToOne
	@JoinColumn(name = "seller_detail_id")
	private SellerDetailEntity sellerDetailEntity;

	@NotNull
	@Column(name = "bank_name", length = 55)
	@Enumerated(EnumType.STRING)
	private MasterPolicyHolderName bankName;

	@Column(name = "status", length = 55)
	@Enumerated(EnumType.STRING)
	private Status status;

	@Version
	@Column(name = "version")
	private long version;
	

	public Long getSellerBankId() {
		return sellerBankId;
	}

	public void setSellerBankId(Long sellerBankId) {
		this.sellerBankId = sellerBankId;
	}

	public SellerDetailEntity getSellerDetailEntity() {
		return sellerDetailEntity;
	}

	public void setSellerDetailEntity(SellerDetailEntity sellerDetailEntity) {
		this.sellerDetailEntity = sellerDetailEntity;
	}

	public MasterPolicyHolderName getBankName() {
		return bankName;
	}

	public void setBankName(MasterPolicyHolderName bankName) {
		this.bankName = bankName;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	
	
}
