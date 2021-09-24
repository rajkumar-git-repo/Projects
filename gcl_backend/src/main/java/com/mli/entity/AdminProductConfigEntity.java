package com.mli.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "gcl_config")
public class AdminProductConfigEntity extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "bank_name")
	private String bankName;

	@Column(name = "isRider_enable")
	private String isRiderEnable;

	@Column(name = "isCovid_enable")
	private String isCovidEnable;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getIsRiderEnable() {
		return isRiderEnable;
	}

	public void setIsRiderEnable(String isRiderEnable) {
		this.isRiderEnable = isRiderEnable;
	}

	public String getIsCovidEnable() {
		return isCovidEnable;
	}

	public void setIsCovidEnable(String isCovidEnable) {
		this.isCovidEnable = isCovidEnable;
	}

	@Override
	public String toString() {
		return "AdminProductConfigEntity [id=" + id + ", bankName=" + bankName + ", isRiderEnable=" + isRiderEnable
				+ ", isCovidEnable=" + isCovidEnable + "]";
	}

}
