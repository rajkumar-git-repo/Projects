package com.mli.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "cam_reports")
public class CamReportDetailsEntity extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CamReportDetailsEntity other = (CamReportDetailsEntity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "cam_report_urls")
	private String camReportUrls;

	@Column(name = "cam_report_urls_name")
	private String camReportUrlsName;

	@Column(name = "proposal_number", length = 55)
	private String proposalNumber;

	@Column(name = "cam_folder_path")
	private String camFolderPath;

	public String getCamReportUrls() {
		return camReportUrls;
	}

	public void setCamReportUrls(String camReportUrls) {
		this.camReportUrls = camReportUrls;
	}

	public String getCamReportUrlsName() {
		return camReportUrlsName;
	}

	public void setCamReportUrlsName(String camReportUrlsName) {
		this.camReportUrlsName = camReportUrlsName;
	}

	public String getProposalNumber() {
		return proposalNumber;
	}

	public void setProposalNumber(String proposalNumber) {
		this.proposalNumber = proposalNumber;
	}

	
	public String getCamFolderPath() {
		return camFolderPath;
	}

	public void setCamFolderPath(String camFolderPath) {
		this.camFolderPath = camFolderPath;
	}

	

}
