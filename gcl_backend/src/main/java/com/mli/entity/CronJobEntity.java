package com.mli.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import com.mli.enums.CronJobType;

@Entity
@Table(name = "cron_job")
public class CronJobEntity extends BaseEntity {

	private static final long serialVersionUID = -8885439961409425122L;

	@Id
	@Column(name = "cron_job_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long cronJobId;

	@Column(name = "status")
	private Boolean status;

	@Column(name = "cron_type")
	@Enumerated(EnumType.STRING)
	private CronJobType cronJobType;
	
	@Version
	@Column(name = "version")
	private long version;

	public Long getCronJobId() {
		return cronJobId;
	}

	public void setCronJobId(Long cronJobId) {
		this.cronJobId = cronJobId;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	

	public CronJobType getCronJobType() {
		return cronJobType;
	}

	public void setCronJobType(CronJobType cronJobType) {
		this.cronJobType = cronJobType;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}
	


}