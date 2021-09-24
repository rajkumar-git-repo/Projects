package com.mli.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="covid_report")
public class CovidReportEntity implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id", unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name = "file_url")
	private String fileUrl;

	@Column(name = "file_name")
	private String fileName;

	@Column(name = "proposal_number", length = 55)
	private String proposalNumber;

	@Column(name = "file_folder_path")
	private String fileFolderPath;

	@Column(name = "file_type")
	private String fileType;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getProposalNumber() {
		return proposalNumber;
	}

	public void setProposalNumber(String proposalNumber) {
		this.proposalNumber = proposalNumber;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getFileFolderPath() {
		return fileFolderPath;
	}

	public void setFileFolderPath(String fileFolderPath) {
		this.fileFolderPath = fileFolderPath;
	}

	@Override
	public String toString() {
		return "CovidReportEntity [id=" + id + ", fileUrl=" + fileUrl + ", fileName=" + fileName + ", proposalNumber="
				+ proposalNumber + ", fileFolderPath=" + fileFolderPath + ", fileType=" + fileType + "]";
	}
}
