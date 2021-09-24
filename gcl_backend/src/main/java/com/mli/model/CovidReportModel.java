package com.mli.model;

public class CovidReportModel {

	private String fileName;
	private String fileUrl;
	private String fileType;
	private String fileFolderPath;
	private String proposalNumber;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
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

	public String getProposalNumber() {
		return proposalNumber;
	}

	public void setProposalNumber(String proposalNumber) {
		this.proposalNumber = proposalNumber;
	}

	@Override
	public String toString() {
		return "CovidReportModel [fileName=" + fileName + ", fileUrl=" + fileUrl + ", fileType=" + fileType
				+ ", fileFolderPath=" + fileFolderPath + ", proposalNumber=" + proposalNumber + "]";
	}
}
