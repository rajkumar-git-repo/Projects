package com.mli.modal;

/**
 * @author nikhilesh
 *
 */
public class CamResponseModel {

	private String camReportUrlsName;
	private String camReportUrls;
	private String status;
	private String camFolderPath;
	private String proposalNumber;
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCamReportUrlsName() {
		return camReportUrlsName;
	}

	public void setCamReportUrlsName(String camReportUrlsName) {
		this.camReportUrlsName = camReportUrlsName;
	}

	public String getCamReportUrls() {
		return camReportUrls;
	}

	public void setCamReportUrls(String camReportUrls) {
		this.camReportUrls = camReportUrls;
	}

	public String getCamFolderPath() {
		return camFolderPath;
	}

	public void setCamFolderPath(String camFolderPath) {
		this.camFolderPath = camFolderPath;
	}

	public String getProposalNumber() {
		return proposalNumber;
	}

	public void setProposalNumber(String proposalNumber) {
		this.proposalNumber = proposalNumber;
	}

	

}
