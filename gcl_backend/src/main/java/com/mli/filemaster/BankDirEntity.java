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
@Table(name = "bank_dir")
public class BankDirEntity extends BaseEntity {

	private static final long serialVersionUID = -8335466408364831529L;
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name = "bank_name")
	private String bankName;
	
	@Column(name = "file_extention")
	private String fileExtention;
	
	@ManyToOne
	@JoinColumn(name = "date_dir_id")
	private DateDirEntity dateDirEntity;
	
	@Column(name = "aws_file_path")
	private String awsFilePath;
	
	@Version
	@Column(name = "version")
	private long version;

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

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

	public String getFileExtention() {
		return fileExtention;
	}

	public void setFileExtention(String fileExtention) {
		this.fileExtention = fileExtention;
	}

	public DateDirEntity getDateDirEntity() {
		return dateDirEntity;
	}

	public void setDateDirEntity(DateDirEntity dateDirEntity) {
		this.dateDirEntity = dateDirEntity;
	}

	public String getAwsFilePath() {
		return awsFilePath;
	}

	public void setAwsFilePath(String awsFilePath) {
		this.awsFilePath = awsFilePath;
	}
	
	
	
}