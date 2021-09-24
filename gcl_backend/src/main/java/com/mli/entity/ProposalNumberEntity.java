package com.mli.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

/**
 * @author Nikhilesh.Tiwari
 *
 */
@Entity
@Table(name = "proposal_number_dtl")
public class ProposalNumberEntity extends BaseEntity {

	private static final long serialVersionUID = 3066473163429029056L;

	@Id
	@Column(name = "proposal_number_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long proposalNumberId;

	@NotNull
	@Column(name = "current_day_time")
	private Long dateInDB;

	@NotNull
	@Column(name = "start_number", columnDefinition = "Integer DEFAULT 1")
	private Integer startNumber;

	@NotNull
	@Column(name = "use_number")
	private Integer useNumber;
	
	@Version
	@Column(name = "version")
	private long version;

	public Long getProposalNumberId() {
		return proposalNumberId;
	}

	public void setProposalNumberId(Long proposalNumberId) {
		this.proposalNumberId = proposalNumberId;
	}

	public Integer getStartNumber() {
		return startNumber;
	}

	public void setStartNumber(Integer startNumber) {
		this.startNumber = startNumber;
	}

	public Integer getUseNumber() {
		return useNumber;
	}

	public void setUseNumber(Integer useNumber) {
		this.useNumber = useNumber;
	}

	public Long getDateInDB() {
		return dateInDB;
	}

	public void setDateInDB(Long dateInDB) {
		this.dateInDB = dateInDB;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

}
