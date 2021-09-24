package com.mli.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "creditcard_covid")
public class CreditCardCovidEntity extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "creditcard_covid_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long creditCardCovidId;

	@Version
	@Column(name = "version")
	private long version;

	@Column(name = "covid_declaration")
	private String covidDeclaration;

	@Column(name = "comment", length = 2000)
	private String comment;

	@OneToOne
	@NotNull
	@JoinColumn(name = "creditcard_health_id")
	private CreditCardHealthEntity creditCardHealthId;

	public Long getCreditCardCovidId() {
		return creditCardCovidId;
	}

	public void setCreditCardCovidId(Long creditCardCovidId) {
		this.creditCardCovidId = creditCardCovidId;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	public String getCovidDeclaration() {
		return covidDeclaration;
	}

	public void setCovidDeclaration(String covidDeclaration) {
		this.covidDeclaration = covidDeclaration;
	}

	public CreditCardHealthEntity getCreditCardHealthId() {
		return creditCardHealthId;
	}

	public void setCreditCardHealthId(CreditCardHealthEntity creditCardHealthId) {
		this.creditCardHealthId = creditCardHealthId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public String toString() {
		return "CreditCardCovidEntity [creditCardCovidId=" + creditCardCovidId + ", version=" + version
				+ ", covidDeclaration=" + covidDeclaration + ", comment=" + comment + ", creditCardHealthId="
				+ creditCardHealthId + "]";
	}
}
