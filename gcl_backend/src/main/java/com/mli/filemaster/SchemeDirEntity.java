package com.mli.filemaster;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import com.mli.entity.BaseEntity;

@Entity
@Table(name = "scheme_dir")
public class SchemeDirEntity extends BaseEntity {

	private static final long serialVersionUID = -8335466408364831529L;
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "scheme_name")
	private String schemeName;
	
	@Column(name = "loan_app_number")
	private String loanAppNo;
	
	@Column(name = "proposal_complete_date")
	private String proposalCompleteDate;
	
	@Column(name = "passport_upload_date")
	private String passportUploadDate;
	
	@Column(name = "file_extention")
	private String fileExtention;
	
	@ManyToOne
	@JoinColumn(name = "bank_dir_id")
	private BankDirEntity bankDirEntity;
	
	@Column(name = "doc_type")
	private String docType;
	
	@Column(name = "aws_file_path")
	private String awsFilePath;
	
	@Version
	@Column(name = "version")
	private long version;
	
	@Column(name = "proposal_number")
	private String proposalNumber;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSchemeName() {
		return schemeName;
	}

	public void setSchemeName(String schemeName) {
		this.schemeName = schemeName;
	}

	public String getLoanAppNo() {
		return loanAppNo;
	}

	public void setLoanAppNo(String loanAppNo) {
		this.loanAppNo = loanAppNo;
	}

	public String getProposalCompleteDate() {
		return proposalCompleteDate;
	}

	public void setProposalCompleteDate(String proposalCompleteDate) {
		this.proposalCompleteDate = proposalCompleteDate;
	}

	public String getFileExtention() {
		return fileExtention;
	}

	public void setFileExtention(String fileExtention) {
		this.fileExtention = fileExtention;
	}


	public BankDirEntity getBankDirEntity() {
		return bankDirEntity;
	}

	public void setBankDirEntity(BankDirEntity bankDirEntity) {
		this.bankDirEntity = bankDirEntity;
	}

	public String getDocType() {
		return docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}
	
	public String getAwsFilePath() {
		return awsFilePath;
	}

	public void setAwsFilePath(String awsFilePath) {
		this.awsFilePath = awsFilePath;
	}

	public String getPassportUploadDate() {
		return passportUploadDate;
	}

	public void setPassportUploadDate(String passportUploadDate) {
		this.passportUploadDate = passportUploadDate;
	}

	public String getProposalNumber() {
		return proposalNumber;
	}

	public void setProposalNumber(String proposalNumber) {
		this.proposalNumber = proposalNumber;
	}
}