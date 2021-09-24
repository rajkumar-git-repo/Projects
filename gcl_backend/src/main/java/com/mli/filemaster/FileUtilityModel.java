package com.mli.filemaster;

import com.mli.enums.SchemeType;

public class FileUtilityModel {
	private String bankName;
	private String loanAppNo;
	private SchemeType scheme;
	private Long completionDate;
	private String fileExtention;
	private String docType;
	private String fileName;
	private String dirName;
	private String awsFilePath;
	private String custFirstName;
	private Long passportUploadDate;
	private String proposalNumber;
	private Long createdOn;
	
	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getLoanAppNo() {
		return loanAppNo;
	}

	public void setLoanAppNo(String loanAppNo) {
		this.loanAppNo = loanAppNo;
	}

	public SchemeType getScheme() {
		return scheme;
	}

	public void setScheme(SchemeType scheme) {
		this.scheme = scheme;
	}

	public Long getCompletionDate() {
		return completionDate;
	}

	public void setCompletionDate(Long completionDate) {
		this.completionDate = completionDate;
	}

	public String getFileExtention() {
		return fileExtention;
	}

	public void setFileExtention(String fileExtention) {
		this.fileExtention = fileExtention;
	}

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getDirName() {
		return dirName;
	}

	public void setDirName(String dirName) {
		this.dirName = dirName;
	}

	public String getAwsFilePath() {
		return awsFilePath;
	}

	public void setAwsFilePath(String awsFilePath) {
		this.awsFilePath = awsFilePath;
	}
	
	public String getCustFirstName() {
		return custFirstName;
	}

	public void setCustFirstName(String custFirstName) {
		this.custFirstName = custFirstName;
	}
	
	public Long getPassportUploadDate() {
		return passportUploadDate;
	}

	public void setPassportUploadDate(Long passportUploadDate) {
		this.passportUploadDate = passportUploadDate;
	}
	
	public String getProposalNumber() {
		return proposalNumber;
	}

	public void setProposalNumber(String proposalNumber) {
		this.proposalNumber = proposalNumber;
	}

	public Long getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Long createdOn) {
		this.createdOn = createdOn;
	}

	@Override
	public String toString() {
		return "FileUtilityModel [bankName=" + bankName + ", loanAppNo=" + loanAppNo + ", scheme=" + scheme
				+ ", completionDate=" + completionDate + ", fileExtention=" + fileExtention + ", docType=" + docType
				+ ", fileName=" + fileName + ", dirName=" + dirName + ", awsFilePath=" + awsFilePath
				+ ", custFirstName=" + custFirstName + ", passportUploadDate=" + passportUploadDate
				+ ", proposalNumber=" + proposalNumber + ", createdOn=" + createdOn + "]";
	}
}
