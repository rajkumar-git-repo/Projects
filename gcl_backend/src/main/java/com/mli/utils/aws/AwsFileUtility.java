package com.mli.utils.aws;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.CopyObjectResult;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.transfer.MultipleFileDownload;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.util.IOUtils;
import com.mli.constants.Constant;
import com.mli.constants.DocType;
import com.mli.constants.FileExtention;
import com.mli.constants.Symbols;
import com.mli.controller.UserController;
import com.mli.filemaster.BankDirDao;
import com.mli.filemaster.BankDirEntity;
import com.mli.filemaster.DateDirDao;
import com.mli.filemaster.DateDirEntity;
import com.mli.filemaster.FileUtilityModel;
import com.mli.filemaster.SchemeDirDao;
import com.mli.filemaster.SchemeDirEntity;
import com.mli.modal.CamResponseModel;
import com.mli.modal.FileUploadModel;
import com.mli.model.CovidReportModel;
import com.mli.utils.DateUtil;
import com.mli.utils.ZipUtils;

/**
 * 
 * @author Haripal.Chauhan AWS S3 bucket file upload and download utility.
 *         DPF,CDF, and Excel are first stored locally for temporarily and then
 *         it is processed and push to AWS S3 bucket. locally temporarily file
 *         and folders is being deleted next day via cron. AWS S3 defaultClient
 *         is fetch from IAM Users account on server.
 */

@Component
public class AwsFileUtility {
	private static final Logger logger = Logger.getLogger(UserController.class);

	@Autowired
	private DateDirDao dateDirDao;

	@Autowired
	private BankDirDao bankDirDao;

	@Autowired
	private SchemeDirDao schemeDirDao;

	@Value(value = "${doc.root}")
	private String docRoot;

	@Value(value = "${symbol.forwardslash}")
	private String symbolForwardslash;

	@Value(value = "${aws.s3.bucket}")
	private String awsS3Bucket;

	@Value(value = "${awss3.folder.root}")
	private String awss3FolderRoot;
	
	  
	  @Value(value = "${aws.s3.accesskeyid}") private String accessKeyId;
	  
	  @Value(value = "${aws.s3.secretaccesskey}") private String secretAccessKey;
	  
	 

	@Value("${mli.download.zip}")
	private String zipDownloadPath;

	@Value("${mli.download.zip.bulk}")
	private String zipDownloadbulk;

	@Value("${aws.baseurl}")
	private String baseurl;
	
	@Value("${mli.numberOfLenght}")
	private Integer lenght;

	// private static final String awss3FolderRoot = "GCL/";

	/**
	 * @author Haripal
	 * 
	 *         This method returning file path to be stored as temporary in local
	 *         system. If path is not exist in system it is creating directories to
	 *         making correct file path. Name nomenclature of proposal PDF= <Loan
	 *         Application number><date of proposal complete>.pdf Name nomenclature
	 *         of CDF image PDF= CDF_<Loan Application number<date of proposal
	 *         complete>.pdf Name nomenclature for excel file to be sent to bank/MLI
	 *         Ops/saved on MLI Server - <GCL_Date_Bank Name>
	 */
	@Transactional
	public String generateFilePath(FileUtilityModel utilityModel) {
		// Check if dot pdf file is exist, if file exist locally returning path and
		// deleting from local
		String existingFilePath = null;
		if (utilityModel.getFileExtention().equalsIgnoreCase(FileExtention.PDF)) {
			existingFilePath = getPdfFilePath(utilityModel);
			if (existingFilePath != null) {
				deleteFileIfExistFromLocal(utilityModel);
				return existingFilePath;
			}
		}
		// String directoryPath = docRoot;
		logger.info(":::::START :::::::::generateFilePath:::::" + utilityModel);
		StringBuilder directoryPath = new StringBuilder(docRoot);
		StringBuilder filePath = new StringBuilder();
		boolean isDirectoryCreated = true;
		Long currentTimeStamp = DateUtil.toCurrentUTCTimeStamp();
		try {
			String date = DateUtil.extractDateAsStringDashFormate(currentTimeStamp);
//			String proposalCompletionDate = DateUtil.extractDateAsStringDashFormate(utilityModel.getCompletionDate());
			String proposalCompletionDate = DateUtil.extractDateAsStringDashFormate(currentTimeStamp);
			DateDirEntity dateDirEntity = dateDirDao.findByDate(date);
			if (utilityModel.getFileExtention().equalsIgnoreCase(FileExtention.PDF)) {
				directoryPath.append(DateUtil.extractDateAsStringDashFormate(utilityModel.getCreatedOn()));
				directoryPath.append(symbolForwardslash);
				if (utilityModel.getBankName() != null) {
					directoryPath.append(utilityModel.getBankName());
				}
				if (utilityModel.getScheme() != null) {
					directoryPath.append(symbolForwardslash);
					directoryPath.append(utilityModel.getScheme().getLabel());
				}
				directoryPath.append(symbolForwardslash);
				directoryPath.append(DocType.PROPOSAL);
				if (dateDirEntity == null) {
					// Making entry in DB
					isDirectoryCreated = false;
					dateDirEntity = new DateDirEntity();
					dateDirEntity.setDate(date);
					dateDirEntity.setCreatedOn(currentTimeStamp);
					dateDirDao.save(dateDirEntity);

					BankDirEntity bankDirEntity = new BankDirEntity();
					if (utilityModel.getBankName() != null) {
						bankDirEntity.setBankName(utilityModel.getBankName());
					}
					bankDirEntity.setDateDirEntity(dateDirEntity);
					bankDirEntity.setCreatedOn(currentTimeStamp);
					bankDirDao.save(bankDirEntity);

					SchemeDirEntity schemeDirEntity = new SchemeDirEntity();
					schemeDirEntity.setBankDirEntity(bankDirEntity);
					if (utilityModel.getScheme() != null) {
						schemeDirEntity.setSchemeName(utilityModel.getScheme().getLabel());
					}
					schemeDirEntity.setProposalNumber(utilityModel.getProposalNumber());
					schemeDirEntity.setLoanAppNo(utilityModel.getLoanAppNo());
					schemeDirEntity.setProposalCompleteDate(proposalCompletionDate);
					schemeDirEntity.setFileExtention(utilityModel.getFileExtention());
					schemeDirEntity.setDocType(utilityModel.getDocType());
					schemeDirEntity.setCreatedOn(currentTimeStamp);
					schemeDirDao.save(schemeDirEntity);

				} else {
					BankDirEntity bankDirEntity = bankDirDao.findByDateDirIdNdBankName(dateDirEntity.getId(),
							utilityModel.getBankName());
					if (bankDirEntity == null) {
						isDirectoryCreated = false;
						bankDirEntity = new BankDirEntity();
						bankDirEntity.setBankName(utilityModel.getBankName());
						bankDirEntity.setDateDirEntity(dateDirEntity);
						bankDirEntity.setCreatedOn(currentTimeStamp);
						bankDirDao.save(bankDirEntity);

						SchemeDirEntity schemeDirEntity = new SchemeDirEntity();
						schemeDirEntity.setProposalNumber(utilityModel.getProposalNumber());
						schemeDirEntity.setBankDirEntity(bankDirEntity);
						if(utilityModel.getScheme() != null)
						schemeDirEntity.setSchemeName(utilityModel.getScheme().getLabel());
						schemeDirEntity.setLoanAppNo(utilityModel.getLoanAppNo());
						schemeDirEntity.setProposalCompleteDate(proposalCompletionDate);
						schemeDirEntity.setFileExtention(utilityModel.getFileExtention());
						schemeDirEntity.setDocType(utilityModel.getDocType());
						schemeDirEntity.setCreatedOn(currentTimeStamp);
						schemeDirDao.save(schemeDirEntity);

					} else {
						SchemeDirEntity schemeDirEntity = null;
						if(Constant.YBLCC.equals(utilityModel.getBankName())) {
							schemeDirEntity = schemeDirDao.findByBankDirIdAndSchemeTypeAndProposalNumber(bankDirEntity.getId(), 
									utilityModel.getDocType(), utilityModel.getProposalNumber());
						}else {
						     schemeDirEntity = schemeDirDao.findByBankDirIdNdSchemeTypeDocType(
								utilityModel.getLoanAppNo(), bankDirEntity.getId(), utilityModel.getScheme().getLabel(),
								utilityModel.getDocType(), utilityModel.getProposalNumber());
						}
						if (schemeDirEntity == null) {
							isDirectoryCreated = false;
							schemeDirEntity = new SchemeDirEntity();
							schemeDirEntity.setBankDirEntity(bankDirEntity);
							schemeDirEntity.setProposalNumber(utilityModel.getProposalNumber());
							if(utilityModel.getScheme() != null)
							schemeDirEntity.setSchemeName(utilityModel.getScheme().getLabel());
							schemeDirEntity.setLoanAppNo(utilityModel.getLoanAppNo());
							schemeDirEntity.setDocType(utilityModel.getDocType());
							schemeDirEntity.setProposalCompleteDate(proposalCompletionDate);
							schemeDirEntity.setFileExtention(utilityModel.getFileExtention());
							schemeDirEntity.setCreatedOn(currentTimeStamp);
							schemeDirDao.save(schemeDirEntity);
						}
					}
				}
				filePath.append(directoryPath.toString());
				filePath.append(symbolForwardslash);
				/*
				 * if(utilityModel.getDocType().equalsIgnoreCase(DocType.CDF)){
				 * filePath.append(utilityModel.getDocType()); filePath.append(Symbols.HYPHEN);
				 * }
				 */
				filePath.append(utilityModel.getProposalNumber());
				filePath.append(Symbols.DASH);
				filePath.append(utilityModel.getCustFirstName());
				filePath.append(Symbols.DASH);
				filePath.append(proposalCompletionDate);
				filePath.append(Symbols.DOT);
				filePath.append(utilityModel.getFileExtention());

			} else if (utilityModel.getFileExtention().equalsIgnoreCase(FileExtention.XLSX)) {
				directoryPath.append(date);
				directoryPath.append(symbolForwardslash);
				directoryPath.append(utilityModel.getBankName());
				if (dateDirEntity == null) {
					// Making entry in DB
					isDirectoryCreated = false;
					dateDirEntity = new DateDirEntity();
					dateDirEntity.setDate(date);
					dateDirEntity.setCreatedOn(currentTimeStamp);
					dateDirDao.save(dateDirEntity);

					BankDirEntity bankDirEntity = new BankDirEntity();
					bankDirEntity.setBankName(utilityModel.getBankName());
					bankDirEntity.setDateDirEntity(dateDirEntity);
					bankDirEntity.setFileExtention(utilityModel.getFileExtention());
					bankDirEntity.setCreatedOn(currentTimeStamp);
					bankDirDao.save(bankDirEntity);

				} else {
					BankDirEntity bankDirEntity = bankDirDao.findByDateDirIdNdBankName(dateDirEntity.getId(),
							utilityModel.getBankName());
					if (bankDirEntity == null) {
						isDirectoryCreated = false;
						bankDirEntity = new BankDirEntity();
						bankDirEntity.setBankName(utilityModel.getBankName());
						bankDirEntity.setDateDirEntity(dateDirEntity);
						bankDirEntity.setCreatedOn(currentTimeStamp);
					}
					if (bankDirEntity != null) {
						bankDirEntity.setModifiedOn(currentTimeStamp);
					}
					bankDirEntity.setFileExtention(utilityModel.getFileExtention());
					bankDirDao.saveOrUpdate(bankDirEntity);
				}
				filePath.append(directoryPath.toString());
				filePath.append(symbolForwardslash);
				filePath.append(proposalCompletionDate);
				filePath.append(Symbols.DASH);
				filePath.append(utilityModel.getBankName());
				filePath.append(Symbols.DOT);
				filePath.append(utilityModel.getFileExtention());
			}
			if (!isDirectoryCreated) {
				File dir = new File(directoryPath.toString());

				// attempt to create the directory here
				boolean successful = dir.mkdirs();
				if (successful) {
					// creating the directory succeeded
					System.out.println("directory was created successfully");
				} else {
					// creating the directory failed
					System.out.println("failed trying to create the directory");
				}
			}
			// deleting file if already exist
//			deleteFileIfExistFromLocal(utilityModel);
			logger.info(":::::END :::::::::generateFilePath:::::" + utilityModel + "::::::::filePath="
					+ filePath.toString());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("::::::Error while generating file path::::: " + e);
		}
		return filePath.toString();

	}

	@Transactional
	public String generateFilePathForUploadPassport(FileUtilityModel utilityModel, String passportSide) {
		// Check if dot pdf file is exist, if file exist locally returning path and
		// deleting from local
		String existingFilePath = null;
		if (utilityModel.getFileExtention().equalsIgnoreCase(FileExtention.PDF)) {
			existingFilePath = getPassportPdfFilePath(utilityModel, passportSide);
//			existingFilePath = getPassportDirForDelete(utilityModel);
			if (existingFilePath != null) {
				deletePassportFileIfExistFromLocal(utilityModel, passportSide);
				return existingFilePath;
			}
		}
		// String directoryPath = docRoot;
		logger.info(":::::START :::::::::generateFilePath:::::" + utilityModel);
		StringBuilder directoryPath = new StringBuilder(docRoot);
		StringBuilder filePath = new StringBuilder();
		boolean isDirectoryCreated = true;
		Long currentTimeStamp = DateUtil.toCurrentUTCTimeStamp();
		try {
			String date = DateUtil.extractDateAsStringDashFormate(currentTimeStamp);
//			String proposalCompletionDate = DateUtil.extractDateAsStringDashFormate(utilityModel.getCompletionDate());
			String proposalCompletionDate = DateUtil.extractDateAsStringDashFormate(currentTimeStamp);
			DateDirEntity dateDirEntity = dateDirDao.findByDate(date);
			if (utilityModel.getFileExtention().equalsIgnoreCase(FileExtention.PDF)) {
				directoryPath.append(DateUtil.extractDateAsStringDashFormate(utilityModel.getCreatedOn()));
				directoryPath.append(symbolForwardslash);
				if (utilityModel.getBankName() != null) {
					directoryPath.append(utilityModel.getBankName());
				}
				directoryPath.append(symbolForwardslash);
				if (utilityModel.getScheme() != null) {
					directoryPath.append(utilityModel.getScheme().getLabel());
					directoryPath.append(symbolForwardslash);
				}
				directoryPath.append(DocType.PASSPORT);
//				directoryPath.append(symbolForwardslash);
				if (dateDirEntity == null) {
					// Making entry in DB
					isDirectoryCreated = false;
					dateDirEntity = new DateDirEntity();
					dateDirEntity.setDate(date);
					dateDirEntity.setCreatedOn(currentTimeStamp);
					dateDirDao.save(dateDirEntity);

					BankDirEntity bankDirEntity = new BankDirEntity();
					if (utilityModel.getBankName() != null) {
						bankDirEntity.setBankName(utilityModel.getBankName());
					}
					bankDirEntity.setDateDirEntity(dateDirEntity);
					bankDirEntity.setCreatedOn(currentTimeStamp);
					bankDirDao.save(bankDirEntity);

					SchemeDirEntity schemeDirEntity = new SchemeDirEntity();
					schemeDirEntity.setBankDirEntity(bankDirEntity);
					if (utilityModel.getScheme() != null) {
						schemeDirEntity.setSchemeName(utilityModel.getScheme().getLabel());
					}
					schemeDirEntity.setProposalNumber(utilityModel.getProposalNumber());
					schemeDirEntity.setLoanAppNo(utilityModel.getLoanAppNo());
					schemeDirEntity.setProposalCompleteDate(proposalCompletionDate);
					schemeDirEntity.setPassportUploadDate(proposalCompletionDate);
					schemeDirEntity.setFileExtention(utilityModel.getFileExtention());
					schemeDirEntity.setDocType(utilityModel.getDocType());
					schemeDirEntity.setCreatedOn(currentTimeStamp);
					schemeDirDao.save(schemeDirEntity);

				} else {
					BankDirEntity bankDirEntity = bankDirDao.findByDateDirIdNdBankName(dateDirEntity.getId(),
							utilityModel.getBankName());
					if (bankDirEntity == null) {
						isDirectoryCreated = false;
						bankDirEntity = new BankDirEntity();
						bankDirEntity.setBankName(utilityModel.getBankName());
						bankDirEntity.setDateDirEntity(dateDirEntity);
						bankDirEntity.setCreatedOn(currentTimeStamp);
						bankDirDao.save(bankDirEntity);

						SchemeDirEntity schemeDirEntity = new SchemeDirEntity();
						schemeDirEntity.setProposalNumber(utilityModel.getProposalNumber());
						schemeDirEntity.setBankDirEntity(bankDirEntity);
						schemeDirEntity.setSchemeName(utilityModel.getScheme().getLabel());
						schemeDirEntity.setLoanAppNo(utilityModel.getLoanAppNo());
						schemeDirEntity.setProposalCompleteDate(proposalCompletionDate);
						schemeDirEntity.setFileExtention(utilityModel.getFileExtention());
						schemeDirEntity.setDocType(utilityModel.getDocType());
						schemeDirEntity.setCreatedOn(currentTimeStamp);
						schemeDirEntity.setPassportUploadDate(proposalCompletionDate);
						schemeDirDao.save(schemeDirEntity);

					} else {
						SchemeDirEntity schemeDirEntity = schemeDirDao.findByBankDirIdNdSchemeTypeDocType(
								utilityModel.getLoanAppNo(), bankDirEntity.getId(), utilityModel.getScheme().getLabel(),
								utilityModel.getDocType(), utilityModel.getProposalNumber());
						if (schemeDirEntity == null) {
							isDirectoryCreated = false;
							schemeDirEntity = new SchemeDirEntity();
							schemeDirEntity.setProposalNumber(utilityModel.getProposalNumber());
							schemeDirEntity.setBankDirEntity(bankDirEntity);
							schemeDirEntity.setSchemeName(utilityModel.getScheme().getLabel());
							schemeDirEntity.setLoanAppNo(utilityModel.getLoanAppNo());
							schemeDirEntity.setDocType(utilityModel.getDocType());
							schemeDirEntity.setProposalCompleteDate(proposalCompletionDate);
							schemeDirEntity.setFileExtention(utilityModel.getFileExtention());
							schemeDirEntity.setCreatedOn(currentTimeStamp);
							schemeDirEntity.setPassportUploadDate(proposalCompletionDate);
							schemeDirDao.save(schemeDirEntity);
						}
					}
				}
				filePath.append(directoryPath.toString());
				filePath.append(symbolForwardslash);
				/*
				 * if(utilityModel.getDocType().equalsIgnoreCase(DocType.PASSPORT)){
				 * filePath.append(utilityModel.getDocType()); filePath.append(Symbols.HYPHEN);
				 * }
				 */
				filePath.append(utilityModel.getProposalNumber());
				filePath.append(Symbols.DASH);
				filePath.append(passportSide);
				filePath.append(Symbols.DASH);
				filePath.append(utilityModel.getCustFirstName());
				filePath.append(Symbols.DASH);
				filePath.append(proposalCompletionDate);
				filePath.append(Symbols.DOT);
				filePath.append(utilityModel.getFileExtention());

			}
			if (!isDirectoryCreated) {
				File dir = new File(directoryPath.toString());
				boolean successful = dir.mkdirs();
				if (successful) {
					// creating the directory succeeded
					System.out.println("directory was created successfully");
				} else {
					// creating the directory failed
					System.out.println("failed trying to create the directory");
				}
			}
			// deleting file if already exist
			deletePassportFileIfExistFromLocal(utilityModel, passportSide);
			logger.info(":::::END :::::::::generateFilePath:::::" + utilityModel + "::::::::filePath="
					+ filePath.toString());
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("::::::Error while generating file path::::: " + e);
		}
		return filePath.toString();

	}

	@Transactional
	public String getPassportPdfFilePath(FileUtilityModel utilityModel, String passSide) {
		try {

			logger.info(":::::START :::::::::getPdfFilePath:::::" + utilityModel);
			StringBuilder filePath = new StringBuilder(docRoot);
			SchemeDirEntity schemeDirEntity = null;
			BankDirEntity bankDirEntity = null;
			DateDirEntity dateDirEntity = null;
			schemeDirEntity = schemeDirDao.findByProposalNumberNdeDocType(utilityModel.getProposalNumber(),
					utilityModel.getDocType());
			if (schemeDirEntity == null) {
				return null;
			}
			bankDirEntity = bankDirDao.getEntity(BankDirEntity.class, schemeDirEntity.getBankDirEntity().getId());
			if (bankDirEntity == null)
				return null;
			dateDirEntity = dateDirDao.getEntity(DateDirEntity.class, bankDirEntity.getDateDirEntity().getId());
			if (dateDirEntity == null)
				return null;
			filePath.append(dateDirEntity.getDate());
			filePath.append(symbolForwardslash);
			filePath.append(bankDirEntity.getBankName());
			filePath.append(symbolForwardslash);
			filePath.append(schemeDirEntity.getSchemeName());
			filePath.append(symbolForwardslash);
			filePath.append(DocType.PASSPORT);
			filePath.append(symbolForwardslash);
			/*
			 * if(schemeDirEntity.getDocType().equalsIgnoreCase(DocType.PASSPORT)){
			 * filePath.append(utilityModel.getDocType()); filePath.append(Symbols.HYPHEN);
			 * }
			 */

			filePath.append(schemeDirEntity.getProposalNumber());
			filePath.append(Symbols.DASH);
			filePath.append(passSide);
			filePath.append(Symbols.DASH);
			filePath.append(utilityModel.getCustFirstName());
			filePath.append(Symbols.DASH);
			filePath.append(schemeDirEntity.getProposalCompleteDate());
			filePath.append(Symbols.DOT);
			filePath.append(schemeDirEntity.getFileExtention());
			logger.info(":::::END :::::::::getPdfFilePath:::::" + utilityModel + ":::::File path::::::::"
					+ filePath.toString());
			return filePath.toString();
		} catch (Exception e) {
			logger.error("::::::getPdfFilePath::::: " + e);
			return null;
		}

	}

	@Transactional
	public String getPassportDirForDelete(FileUtilityModel utilityModel) {
		try {
			logger.info(":::::START :::::::::getPdfFilePath:::::" + utilityModel);
			StringBuilder filePath = new StringBuilder(docRoot);
			SchemeDirEntity schemeDirEntity = null;
			BankDirEntity bankDirEntity = null;
			DateDirEntity dateDirEntity = null;
			schemeDirEntity = schemeDirDao.findByProposalNumberNdeDocType(utilityModel.getProposalNumber(),
					utilityModel.getDocType());
			if (schemeDirEntity == null) {
				return null;
			}
			bankDirEntity = bankDirDao.getEntity(BankDirEntity.class, schemeDirEntity.getBankDirEntity().getId());
			if (bankDirEntity == null)
				return null;
			dateDirEntity = dateDirDao.getEntity(DateDirEntity.class, bankDirEntity.getDateDirEntity().getId());
			if (dateDirEntity == null)
				return null;
			filePath.append(dateDirEntity.getDate());
			filePath.append(symbolForwardslash);
			filePath.append(bankDirEntity.getBankName());
			filePath.append(symbolForwardslash);
			filePath.append(schemeDirEntity.getSchemeName());
			filePath.append(symbolForwardslash);
			filePath.append(DocType.PASSPORT);
			/*
			 * if(schemeDirEntity.getDocType().equalsIgnoreCase(DocType.PASSPORT)){
			 * filePath.append(utilityModel.getDocType()); filePath.append(Symbols.HYPHEN);
			 * }
			 */
			logger.info(":::::END :::::::::getPdfFilePath:::::" + utilityModel + ":::::File path::::::::"
					+ filePath.toString());
			return filePath.toString();
		} catch (Exception e) {
			logger.error("::::::getPdfFilePath::::: " + e);
			return null;
		}

	}

	/**
	 * 
	 * @param utilityModel * Getting pdf file path by loan app number and doc
	 *                     type(doc type: PROPOSAL, CDP);
	 */
	@Transactional
	public String getPdfFilePath(FileUtilityModel utilityModel) {
		try {

			logger.info(":::::START :::::::::getPdfFilePath:::::" + utilityModel);
			StringBuilder filePath = new StringBuilder(docRoot);
			SchemeDirEntity schemeDirEntity = null;
			BankDirEntity bankDirEntity = null;
//			DateDirEntity dateDirEntity = null;
			logger.info("---Start scheme detaisl -----------");
			try {
				schemeDirEntity = schemeDirDao.findByProposalNumberNdeDocType(utilityModel.getProposalNumber(),
						utilityModel.getDocType());
				if (schemeDirEntity == null) {
					return null;
				}
			} catch (Exception e) {
				logger.info("--- error getting in scheme details -----------");
			}

			logger.info("---Strat Bank details -----------");
			bankDirEntity = bankDirDao.getEntity(BankDirEntity.class, schemeDirEntity.getBankDirEntity().getId());
			if (bankDirEntity == null)
				return null;
			logger.info("---Strat Bakn detaisl -----------");
//			dateDirEntity = dateDirDao.getEntity(DateDirEntity.class, bankDirEntity.getDateDirEntity().getId());
//			if(dateDirEntity == null)
//				return null;
			filePath.append(DateUtil.extractDateAsStringDashFormate(utilityModel.getCreatedOn()));
			filePath.append(symbolForwardslash);
			filePath.append(bankDirEntity.getBankName());
			if(!StringUtils.isEmpty(schemeDirEntity.getSchemeName())) {
				filePath.append(symbolForwardslash);
				filePath.append(schemeDirEntity.getSchemeName());
			}
			filePath.append(symbolForwardslash);
			filePath.append(DocType.PROPOSAL);
			filePath.append(symbolForwardslash);
			if (schemeDirEntity.getDocType().equalsIgnoreCase(DocType.CDF)) {
				filePath.append(utilityModel.getDocType());
				filePath.append(Symbols.HYPHEN);
			}
			filePath.append(schemeDirEntity.getProposalNumber());
			filePath.append(Symbols.DASH);
			filePath.append(utilityModel.getCustFirstName());
			filePath.append(Symbols.DASH);
			filePath.append(schemeDirEntity.getProposalCompleteDate());
			filePath.append(Symbols.DOT);
			filePath.append(schemeDirEntity.getFileExtention());
			logger.info(":::::END :::::::::getPdfFilePath:::::" + utilityModel + ":::::File path::::::::"
					+ filePath.toString());
			return filePath.toString();
		} catch (Exception e) {
			logger.error("::::::getPdfFilePath::::: " + e);
			return null;
		}

	}

	/**
	 * 
	 * @param utilityModel local PDF file and AWS file path and directory path by
	 *                     loan app number and doc type.
	 */
	@Transactional
	public FileUtilityModel getPDFFileNdDirectoryDetails(FileUtilityModel utilityModel) {
		try {
			logger.info(":::::START :::::::::getPDFFileNdDirectoryDetails:::::" + utilityModel);
			StringBuilder filePath = new StringBuilder(awss3FolderRoot);
			StringBuilder fileDir = new StringBuilder();
			StringBuilder fileName = new StringBuilder();
			SchemeDirEntity schemeDirEntity = null;
			BankDirEntity bankDirEntity = null;
//		DateDirEntity dateDirEntity = null;
			schemeDirEntity = schemeDirDao.findByProposalNumberNdeDocType(utilityModel.getProposalNumber(),
					utilityModel.getDocType());
			if (schemeDirEntity == null) {
				return null;
			}
			bankDirEntity = bankDirDao.getEntity(BankDirEntity.class, schemeDirEntity.getBankDirEntity().getId());
//		dateDirEntity = dateDirDao.getEntity(DateDirEntity.class, bankDirEntity.getDateDirEntity().getId());
			filePath.append(DateUtil.extractDateAsStringDashFormate(utilityModel.getCreatedOn()));
			filePath.append(symbolForwardslash);
			filePath.append(bankDirEntity.getBankName());
			if (!StringUtils.isEmpty(schemeDirEntity.getSchemeName())) {
				filePath.append(symbolForwardslash);
				filePath.append(schemeDirEntity.getSchemeName());
			}
			filePath.append(symbolForwardslash);
			filePath.append(DocType.PROPOSAL);
			filePath.append(symbolForwardslash);
			fileDir.append(filePath.toString());
			if (schemeDirEntity.getDocType().equalsIgnoreCase(DocType.CDF)) {
				filePath.append(utilityModel.getDocType());
				filePath.append(Symbols.HYPHEN);
				fileName.append(utilityModel.getDocType());
				fileName.append(Symbols.HYPHEN);
			}
			filePath.append(schemeDirEntity.getProposalNumber());
			filePath.append(Symbols.DASH);
			filePath.append(utilityModel.getCustFirstName());
			filePath.append(Symbols.DASH);
			filePath.append(schemeDirEntity.getProposalCompleteDate());
			filePath.append(Symbols.DOT);
			filePath.append(schemeDirEntity.getFileExtention());
			// file name
			if(!StringUtils.isEmpty(schemeDirEntity.getLoanAppNo())) {
			    fileName.append(schemeDirEntity.getLoanAppNo());
			}else {
				fileName.append(schemeDirEntity.getProposalNumber());
			}
			fileName.append(Symbols.DASH);
			fileName.append(schemeDirEntity.getProposalCompleteDate());
			utilityModel.setFileName(fileName.toString());
			utilityModel.setDirName(fileDir.toString());
			utilityModel.setFileExtention(schemeDirEntity.getFileExtention());
			utilityModel.setAwsFilePath(filePath.toString());
			logger.info(":::::END :::::::::getPDFFileNdDirectoryDetails:::::" + utilityModel);

		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return utilityModel;
	}

	@Transactional
	public FileUtilityModel getPDFFileNdDirectoryForPassport(FileUtilityModel utilityModel) {
		try {
			logger.info(":::::START :::::::::getPDFFileNdDirectoryDetails:::::" + utilityModel);
			StringBuilder filePath = new StringBuilder(awss3FolderRoot);
			StringBuilder fileDir = new StringBuilder();
			StringBuilder fileName = new StringBuilder();
			SchemeDirEntity schemeDirEntity = null;
			BankDirEntity bankDirEntity = null;
//		DateDirEntity dateDirEntity = null;
			schemeDirEntity = schemeDirDao.findByProposalNumberNdeDocType(utilityModel.getProposalNumber(),
					utilityModel.getDocType());
			if (schemeDirEntity == null) {
				return null;
			}
			bankDirEntity = bankDirDao.getEntity(BankDirEntity.class, schemeDirEntity.getBankDirEntity().getId());
//		dateDirEntity = dateDirDao.getEntity(DateDirEntity.class, bankDirEntity.getDateDirEntity().getId());
			filePath.append(DateUtil.extractDateAsStringDashFormate(utilityModel.getCreatedOn()));
			filePath.append(symbolForwardslash);
			filePath.append(bankDirEntity.getBankName());
			filePath.append(symbolForwardslash);
			filePath.append(schemeDirEntity.getSchemeName());
			filePath.append(symbolForwardslash);
			filePath.append(DocType.PASSPORT);
			filePath.append(symbolForwardslash);
			fileDir.append(filePath.toString());
			/*
			 * if(schemeDirEntity.getDocType().equalsIgnoreCase(DocType.CDF)){
			 * filePath.append(utilityModel.getDocType()); filePath.append(Symbols.HYPHEN);
			 * fileName.append(utilityModel.getDocType()); fileName.append(Symbols.HYPHEN);
			 * }
			 */
			filePath.append(schemeDirEntity.getProposalNumber());
			filePath.append(Symbols.DASH);
			filePath.append(utilityModel.getCustFirstName());
			filePath.append(Symbols.DASH);
			filePath.append(schemeDirEntity.getPassportUploadDate());
			filePath.append(Symbols.DOT);
			filePath.append(schemeDirEntity.getFileExtention());
			// file name
			fileName.append(schemeDirEntity.getLoanAppNo());
			fileName.append(Symbols.DASH);
			fileName.append(utilityModel.getCustFirstName());
			fileName.append(Symbols.DASH);
			fileName.append(schemeDirEntity.getProposalCompleteDate());
			utilityModel.setFileName(fileName.toString());
			utilityModel.setDirName(fileDir.toString());
			utilityModel.setFileExtention(schemeDirEntity.getFileExtention());
			utilityModel.setAwsFilePath(filePath.toString());
			logger.info(":::::END :::::::::getPDFFileNdDirectoryDetails:::::" + utilityModel);

		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return utilityModel;
	}

	/**
	 * @param date
	 * @param bank Excel file local path by date and bank name.
	 */
	@Transactional
	public String getExcelFileLocalPath(Long date, String bank) {
		logger.info(":::::START :::::::::getExcelFilePath:::::date" + date + "::::::::::::bank" + bank);
		StringBuilder filePath = new StringBuilder(docRoot);
		BankDirEntity bankDirEntity = null;
		String dateFolder = DateUtil.extractDateAsStringDashFormate(date);
		DateDirEntity dateDirEntity = dateDirDao.findByDate(dateFolder);
		if (dateDirEntity == null) {
			return null;
		}
		bankDirEntity = bankDirDao.findByDateDirIdNdBankName(dateDirEntity.getId(), bank);
		if (bankDirEntity == null) {
			return null;
		}
		filePath.append(dateFolder);
		filePath.append(symbolForwardslash);
		filePath.append(bankDirEntity.getBankName());
		filePath.append(symbolForwardslash);
		filePath.append(dateFolder);
		filePath.append(Symbols.DASH);
		filePath.append(bankDirEntity.getBankName());
		filePath.append(Symbols.DOT);
		filePath.append(bankDirEntity.getFileExtention());
		logger.info(":::::END :::::::::getExcelFilePath:::::fil pathe::::::::" + filePath.toString());
		return filePath.toString();

	}

	@Transactional
	public FileUtilityModel getExcelFileDetails(Long date, String bank) {
		FileUtilityModel fileUtilityModel = new FileUtilityModel();
		StringBuilder awsFilePath = new StringBuilder(awss3FolderRoot);
		StringBuilder filenNme = new StringBuilder();
		BankDirEntity bankDirEntity = null;
		String dateFolder = DateUtil.extractDateAsStringDashFormate(date);
		logger.info("datefolder : " + dateFolder);
		DateDirEntity dateDirEntity = dateDirDao.findByDate(dateFolder);
		if (dateDirEntity == null)
			return null;
		bankDirEntity = bankDirDao.findByDateDirIdNdBankName(dateDirEntity.getId(), bank);
		filenNme.append(dateFolder);
		filenNme.append(Symbols.DASH);
		filenNme.append(bankDirEntity.getBankName());
		fileUtilityModel.setFileName(filenNme.toString());
		awsFilePath.append(dateFolder).append(symbolForwardslash).append(bankDirEntity.getBankName())
				.append(symbolForwardslash).append(filenNme).append(Symbols.DOT).append(FileExtention.XLSX);
		fileUtilityModel.setAwsFilePath(awsFilePath.toString());
		return fileUtilityModel;

	}

	public void deletePassportFileIfExistFromLocal(FileUtilityModel utilityModel, String passSide) {
		String filePath = null;
		try {
			if (utilityModel.getFileExtention().equalsIgnoreCase(FileExtention.PDF)) {
				filePath = getPassportPdfFilePath(utilityModel, passSide);
//				filePath = getPassportDirForDelete(utilityModel);
			}
			if (filePath != null) {
				File dir = new File(new File(filePath).getParent());
				if (!dir.isDirectory()) {
					dir.mkdirs();
				}
				Path path = Paths.get(filePath);
//				logger.info("dir.list().length : "+dir.list().length);
//				if (dir!= null && dir.list().length == 2) {
				Files.deleteIfExists(path);
//				}
			}
		} catch (IOException e) {
			logger.error("::::::Error in deleting or creating directory file::::: " + e.getMessage());
		}
	}

	/**
	 * Deleting file if already exist locally and also checking if other servers
	 * make an entry in DB, in that case also we need to check whether directory is
	 * present or not for current server. if not present locally and entry in DB, we
	 * are creating directory.
	 */
	public void deleteFileIfExistFromLocal(FileUtilityModel utilityModel) {
		String filePath = null;
		try {
			if (utilityModel.getFileExtention().equalsIgnoreCase(FileExtention.PDF)) {
				filePath = getPdfFilePath(utilityModel);
			}
			if (utilityModel.getFileExtention().equalsIgnoreCase(FileExtention.XLSX)) {
				filePath = getExcelFileLocalPath(utilityModel.getCompletionDate(), utilityModel.getBankName());
			}
			File dir = new File(new File(filePath).getParent());
			if (!dir.isDirectory()) {
				dir.mkdirs();
			}
			Path path = Paths.get(filePath);
			Files.deleteIfExists(path);
		} catch (IOException e) {
			logger.error("::::::Error in deleting or creating directory file::::: " + e.getMessage());
		}
	}

	/**
	 * 
	 * @param file
	 * @param name
	 */
	public boolean isFileExist(String file) {
		AWSCredentials credentials = new BasicAWSCredentials(accessKeyId,secretAccessKey);
		AmazonS3 s3client = new AmazonS3Client(credentials);
		ObjectListing objects = s3client
				.listObjects(new ListObjectsRequest().withBucketName(awsS3Bucket).withPrefix(file));

		for (S3ObjectSummary objectSummary : objects.getObjectSummaries()) {
			if (objectSummary.getKey().equals(file)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Currently we are using IAM(Identity and Access Management) for accessing
	 * aws_access_key_id and aws_secret_access_key. We can set it environment
	 * variable and can use in application.
	 */
	public boolean createFileOnAWSS3(String filePath, String key_name) {
		try {
			logger.info("::START::::createFileOnAWSS3::::: filePath=" + filePath + ":::::key_name=" + key_name);
			 AWSCredentials credentials = new BasicAWSCredentials(accessKeyId,secretAccessKey);
			 AmazonS3 s3client = new AmazonS3Client(credentials);
			// upload file in aws s3 bucket
			s3client.putObject(new PutObjectRequest(awsS3Bucket, key_name, new File(filePath)));
			logger.info("::END::::createFileOnAWSS3:::::");
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("::ERROR IN::::createFileOnAWSS3::::: " + e);
			return false;
		}
		return true;
	}

	/**
	 * Delete file from S3
	 * 
	 * @param filePath
	 * @param key_name
	 * @return
	 */
	public boolean deleteFileOnAWSS3(String filePath) {
		try {

			 AWSCredentials credentials = new BasicAWSCredentials(accessKeyId, secretAccessKey);
			 AmazonS3 s3client = new AmazonS3Client(credentials);
			logger.info("Before deleting file path" + filePath);
			s3client.deleteObject(awsS3Bucket, filePath);
			logger.info("::END::::deleteFileOnAWSS3:::::");
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("::ERROR IN::::dleteFileOnAWSS3::::: " + e);
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @param key_name
	 * @return InputStream Getting file from AWS S3 bucket and returning as
	 *         InputStream.
	 */
	public InputStream getFileAsInputStreamFromAWSS3(String key_name) {
		 logger.info("::START::::getFileAsInputStreamFromAWSS3::::: key_name=" + key_name);
		 AWSCredentials credentials = new BasicAWSCredentials(accessKeyId,secretAccessKey);
		 AmazonS3 s3 = new AmazonS3Client(credentials);
		 InputStream imputStream = null;
		try {
			S3Object o = s3.getObject(awsS3Bucket, key_name);
			imputStream = o.getObjectContent();
			logger.info("::END::::getFileAsInputStreamFromAWSS3::::: ");
		} catch (AmazonServiceException e) {
			logger.error("::ERROR::::getFileAsInputStreamFromAWSS3::::: " + e.getErrorMessage());
			e.printStackTrace();
		} catch (Exception e) {
			logger.error("::ERROR::::getFileAsInputStreamFromAWSS3::::: " + e.getMessage());
			e.printStackTrace();
		}
		return imputStream;
	}

	/**
	 * 
	 * @param bucketName
	 * @param folderName
	 * @param client     Creating empty folder on AWS S3 bucket.
	 */
	public void createFolder(String bucketName, String folderName, AmazonS3 client) {
		// create meta-data for your folder and set content-length to 0
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(0);
		// create empty content
		InputStream emptyContent = new ByteArrayInputStream(new byte[0]);
		// create a PutObjectRequest passing the folder name suffixed by /
		PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, folderName, emptyContent, metadata);
		// send request to S3 to create folder
		client.putObject(putObjectRequest);
	}

	@Transactional
	public boolean updateAwsPassportFilePathInDB(FileUtilityModel utilityModel) {
		SchemeDirEntity schemeDirEntity = null;
		schemeDirEntity = schemeDirDao.findByProposalNumberNdeDocType(utilityModel.getProposalNumber(),
				utilityModel.getDocType());
		if (schemeDirEntity == null) {
			return false;
		} else {
			schemeDirEntity.setAwsFilePath(utilityModel.getAwsFilePath());
			schemeDirDao.saveOrUpdate(schemeDirEntity);
		}
		return true;
	}

	@Transactional
	public boolean updateAwsPDFNdCDFFilePathInDB(FileUtilityModel utilityModel) {
		SchemeDirEntity schemeDirEntity = null;
		schemeDirEntity = schemeDirDao.findByProposalNumberNdeDocType(utilityModel.getProposalNumber(),
				utilityModel.getDocType());
		if (schemeDirEntity == null) {
			return false;
		} else {
			schemeDirEntity.setAwsFilePath(utilityModel.getAwsFilePath());
			schemeDirDao.saveOrUpdate(schemeDirEntity);
		}
		return true;
	}

	@Transactional
	public boolean updateAwsExcelFilePathInDB(Long date, String bank, String awsFilePath) {
		BankDirEntity bankDirEntity = null;
		String dateFolder = DateUtil.extractDateAsStringDashFormate(date);
		DateDirEntity dateDirEntity = dateDirDao.findByDate(dateFolder);
		if (dateDirEntity == null)
			return false;
		bankDirEntity = bankDirDao.findByDateDirIdNdBankName(dateDirEntity.getId(), bank);
		if (bankDirEntity == null)
			return false;
		bankDirEntity.setAwsFilePath(awsFilePath);
		bankDirDao.saveOrUpdate(bankDirEntity);
		return true;
	}

	/**
	 * Empty and delete a folder (and subfolders).
	 */
	public void rmdir(final File dirPath) {
		if (dirPath.exists()) {
			File[] files = dirPath.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					rmdir(files[i]);
				} else {
					files[i].delete();
				}
			}
		}
		dirPath.delete();
	}

	public void copyObject(String source, String destination) {
		AWSCredentials credentials = new BasicAWSCredentials(accessKeyId,secretAccessKey);
		AmazonS3 s3 = new AmazonS3Client(credentials);
		CopyObjectRequest req = new CopyObjectRequest(awsS3Bucket, source, awsS3Bucket, destination);
		CopyObjectResult res = s3.copyObject(req);
	}

	/**
	 * 1) Download folder from S3 to local/server 2) zip it 3) delete downloaded
	 * folder
	 * 
	 * @throws IOException
	 * 
	 */
	public void bulkDownloadFromS3ToLocal(String sourceAwsPath, String destiLocalPath, String docType)
			throws AmazonServiceException, AmazonClientException, InterruptedException, IOException {
		logger.info("Inside bulkDownloadFromS3ToLocal() method.....");
		AWSCredentials credentials = new BasicAWSCredentials(accessKeyId, secretAccessKey);
		AmazonS3 s3 = new AmazonS3Client(credentials);
		File destinationDir = new File(destiLocalPath);
		TransferManager transferManager = new TransferManager(s3);
		logger.info("sourceAwsPath : " + sourceAwsPath);
		logger.info("destiLocalPath : " + destiLocalPath);
		MultipleFileDownload download = transferManager.downloadDirectory(awsS3Bucket, sourceAwsPath, destinationDir);
		download.waitForCompletion();
		ZipUtils zipUtils = new ZipUtils();
		StringBuilder zippedFileName = new StringBuilder();
		if (DocType.PROPOSAL.equals(docType)) {
			zippedFileName.append("proposal.zip");
		} else if (DocType.PASSPORT.equals(docType)) {
			zippedFileName.append("passport.zip");
		} else if (DocType.CAM_REPORT.equals(docType)) {
			zippedFileName.append("camreport.zip");
		} else if (DocType.YBLCCPROPOSAL.equals(docType)) {
			zippedFileName.append("yblcc_proposal.zip");
		}
		File file = new File(zipDownloadPath);
		if (!file.exists()) {
			file.mkdirs();
		}
		boolean zippedFlag = zipUtils.zipDirectory(destiLocalPath + "/" + sourceAwsPath,
				zipDownloadPath + zippedFileName.toString());
		logger.info("zippedFlag : " + zippedFlag);
		if (zippedFlag) {
			if (destinationDir.exists()) {
				logger.info("Deleted : " + destinationDir.delete());
				zipUtils.deleteFolder(destinationDir);
			}
		}
	}

	public void bulkDownloadFromS3ToLocalFor7days(String sourceAwsPath, String destiLocalPath, String docType)
			throws AmazonServiceException, AmazonClientException, InterruptedException, IOException {
		logger.info("Inside bulkDownloadFromS3ToLocalFor7days() method.....");
		AWSCredentials credentials = new BasicAWSCredentials(accessKeyId, secretAccessKey);
		ClientConfiguration clientConfiguration = new ClientConfiguration();
		clientConfiguration.setConnectionTimeout(10*60*60*1000);
		clientConfiguration.setSocketTimeout(10*60*60*1000);
		File destinationDir = new File(destiLocalPath);
	    AmazonS3 s3 = new AmazonS3Client(credentials,clientConfiguration);
		TransferManager transferManager = new TransferManager(s3);
		logger.info("sourceAwsPath : " + sourceAwsPath);
		logger.info("destiLocalPath : " + destiLocalPath);
		MultipleFileDownload download = transferManager.downloadDirectory(awsS3Bucket, sourceAwsPath, destinationDir);
		download.waitForCompletion();
	}

	public String getFileName(String docType) {
		StringBuilder zippedFileName = new StringBuilder();
		if (DocType.PROPOSAL.equals(docType)) {
			zippedFileName.append("proposal.zip");
		} else if (DocType.PASSPORT.equals(docType)) {
			zippedFileName.append("passport.zip");
		} else if (DocType.CAM_REPORT.equals(docType)) {
			zippedFileName.append("AdditonalDocument.zip");
		}
		return zippedFileName.toString();
	}

	/**
	 * cron to delete verified folder <code>(dd-MM-yyyy_verfied) from s3 which is
	 * older then 62 days
	 */
	@Scheduled(cron = "${mli.cron.delete.verified}")
	public void deleteVerifiedFolderFromS3() {
		try {
			StringBuilder destination = new StringBuilder(Constant.VERIFIED);
			destination.append("_");
			destination.append(DateUtil.extractDateAsStringDashFormate(DateUtil.addDaysToCurrentTS(-63)));
			logger.info("Deleting directory : " + destination.toString());
			 AWSCredentials credentials = new BasicAWSCredentials(accessKeyId,secretAccessKey);
			 AmazonS3 s3 = new AmazonS3Client(credentials);
			int objectCount = 0;
			for (S3ObjectSummary file : s3.listObjects(awsS3Bucket, destination.toString()).getObjectSummaries()) {
				objectCount++;
				logger.info("Deleting file name is : " + file.getKey());
				s3.deleteObject(awsS3Bucket, file.getKey());
			}
			logger.info("Total number of object to be deleted :" + objectCount + " ,from directory : "
					+ destination.toString() + " at ,Today : " + new Date());
		} catch (Exception exception) {
			logger.error("Exception occured while deleting verified folder from S3 : " + exception.getMessage());
			exception.printStackTrace();
		}
	}

	public List<CamResponseModel> uploadFileS3BucketByMultipart(String proposalNumber, List<File> filesList,String docType) {
		List<CamResponseModel> camResponseModelList = new ArrayList<>();
		
		try {
			logger.info("::START::::createFileOnAWSS3::::: filePath=" + proposalNumber + ":::::key_name=" + filesList);
			AWSCredentials credentials = new BasicAWSCredentials(accessKeyId,secretAccessKey);
			 AmazonS3 s3client = new AmazonS3Client(credentials);
			for (File file : filesList) {
				String awspath = docType + File.separator + proposalNumber + File.separator + file.getName();
				CamResponseModel camResponseModel = new CamResponseModel();
				s3client.putObject(new PutObjectRequest(awsS3Bucket, awspath, file));
				camResponseModel.setCamReportUrlsName(file.getName());
				camResponseModel.setCamReportUrls(baseurl + docType + File.separator + proposalNumber
						+ File.separator + file.getName());
				camResponseModel.setCamFolderPath(docType + File.separator + proposalNumber);
				camResponseModelList.add(camResponseModel);
			}
			logger.info("::END::::createFileOnAWSS3:::::");
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("::ERROR IN::::createFileOnAWSS3::::: " + e);

		}
		return camResponseModelList;
	}

	public byte[] downloadAllFilesById(String key_name) {
		logger.info("::START::::getFileAsInputStreamFromAWSS3::::: key_name=" + key_name);
		 AWSCredentials credentials = new BasicAWSCredentials(accessKeyId,secretAccessKey);
		 AmazonS3 s3 = new AmazonS3Client(credentials);
		InputStream imputStream = null;
		byte[] bytes = null;
		try {
			S3Object o = s3.getObject(awsS3Bucket, key_name);
			imputStream = o.getObjectContent();
			logger.info("::END::::getFileAsInputStreamFromAWSS3::::: ");
		} catch (AmazonServiceException e) {
			logger.error("::ERROR::::getFileAsInputStreamFromAWSS3::::: " + e.getErrorMessage());
			e.printStackTrace();
		} catch (Exception e) {
			logger.error("::ERROR::::getFileAsInputStreamFromAWSS3::::: " + e.getMessage());
			e.printStackTrace();
		}
		try {
			bytes = IOUtils.toByteArray(imputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return bytes;
	}

	public void downloadFolder(String destiLocalPath, String sourceAwsPath)
			throws AmazonServiceException, AmazonClientException, InterruptedException {
		logger.info("Inside downloadFolder() method.....");
		AWSCredentials credentials = new BasicAWSCredentials(accessKeyId,secretAccessKey);
		File destinationDir = new File(sourceAwsPath);
		AmazonS3 s3 = new AmazonS3Client(credentials);
		TransferManager transferManager = new TransferManager(s3);
		logger.info("sourceAwsPath : " + sourceAwsPath);
		logger.info("destiLocalPath : " + destiLocalPath);
		MultipleFileDownload download = transferManager.downloadDirectory(awsS3Bucket, destiLocalPath, destinationDir);
		download.waitForCompletion();
	}

	/**
	 * @param source
	 * @param destination
	 * @logic this method is specially copy all folder source to destination
	 */
	public void copyFolder(String source, String destination) {
		 AWSCredentials credentials = new BasicAWSCredentials(accessKeyId, secretAccessKey);
		 AmazonS3 s3client = new AmazonS3Client(credentials);
		ObjectListing objects = s3client
				.listObjects(new ListObjectsRequest().withBucketName(awsS3Bucket).withPrefix(source));

		for (S3ObjectSummary objectSummary : objects.getObjectSummaries()) {
			s3client.copyObject(this.awsS3Bucket, objectSummary.getKey(), this.awsS3Bucket,
					destination + objectSummary.getKey());

		}
	}

	/**
	 * Delete file from S3
	 * 
	 * @param filePath
	 * @param key_name
	 * @return
	 */
	public boolean deleteCamFileOnAWSS3(String filePath) {
		try {
			String fileFolderPath=filePath.substring(lenght);
			AWSCredentials credentials = new BasicAWSCredentials(accessKeyId, secretAccessKey);
			AmazonS3 s3client = new AmazonS3Client(credentials);
			logger.info("Cam Report Before deleting file path=========" + fileFolderPath);
			s3client.deleteObject(awsS3Bucket, fileFolderPath);
			logger.info("::END::::deleteFileOnAWSS3:::::");
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("::ERROR IN::::dleteFileOnAWSS3::::: " + e);
			return false;
		}
		return true;
	}
	
	

	
	

	/**
	 * Delete file from S3 in customer form journey file
	 * 
	 * @param filePath
	 * @return true
	 */
	public boolean deleteCommonFileOnAWSS3(String filePath) {
		try {
			String fileFolderPath=filePath.substring(lenght);
			AWSCredentials credentials = new BasicAWSCredentials(accessKeyId, secretAccessKey);
			AmazonS3 s3client = new AmazonS3Client(credentials);
			logger.info("customer form journey Before deleting file path=========" + fileFolderPath);
			s3client.deleteObject(awsS3Bucket, fileFolderPath);
			logger.info("::END::::deleteFileOnAWSS3:::::");
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("::ERROR IN::::dleteFileOnAWSS3::::: " + e);
			return false;
		}
		return true;
	}
	
	public List<FileUploadModel> uploadFileS3BucketphysicalForm(String proposalNumber, List<File> filesList,String docType) {
		List<FileUploadModel> fileUploadModelList = new ArrayList<>();
		
		try {
			logger.info("::START::::create PhysicalForm FileOnAWSS3::::: filePath=" + proposalNumber + ":::::key_name=" + filesList);
			AWSCredentials credentials = new BasicAWSCredentials(accessKeyId,secretAccessKey);
			 AmazonS3 s3client = new AmazonS3Client(credentials);
			for (File file : filesList) {
				String awspath = docType + File.separator + proposalNumber + File.separator + file.getName();
				FileUploadModel fileUploadModel = new FileUploadModel();
				s3client.putObject(new PutObjectRequest(awsS3Bucket, awspath, file));
				fileUploadModel.setFileName(file.getName());
				fileUploadModel.setFileUrl(baseurl + docType + File.separator + proposalNumber
						+ File.separator + file.getName());
				
				fileUploadModel.setFileFolderPath(docType + File.separator + proposalNumber);
				fileUploadModelList.add(fileUploadModel);
			}
			logger.info("::END::::createFileOnAWSS3:::::");
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("::ERROR IN::::createFileOnAWSS3::::: " + e);

		}
		return fileUploadModelList;
	}

	public List<CovidReportModel> uploadCovidReportOnS3Bucket(String proposalNumber,String fileType, List<File> filesList,String docType) {
		String fileTypeFolder = Constant.TEST_REPORT.equals(fileType) ? "TestReport" : Constant.VACCINE_REPORT.equals(fileType) ? "VaccineReport" : fileType;
		List<CovidReportModel> covidReportModelList = new ArrayList<>();	
		try {
			logger.info("::START::::create Covid Report FileOnAWSS3::::: filePath=" + proposalNumber + ":::::key_name=" + filesList);
			AWSCredentials credentials = new BasicAWSCredentials(accessKeyId,secretAccessKey);
			AmazonS3 s3client = new AmazonS3Client(credentials);
			for (File file : filesList) {
				String awspath = docType + File.separator + proposalNumber + File.separator +fileTypeFolder+ File.separator + file.getName();
				CovidReportModel covidReportModel = new CovidReportModel();
				s3client.putObject(new PutObjectRequest(awsS3Bucket, awspath, file));
				covidReportModel.setFileName(file.getName());
				covidReportModel.setProposalNumber(proposalNumber);
				covidReportModel.setFileType(fileType);
				covidReportModel.setFileUrl(baseurl + docType + File.separator + proposalNumber + File.separator +fileTypeFolder + File.separator + file.getName());
				covidReportModel.setFileFolderPath(docType + File.separator + proposalNumber);
				covidReportModelList.add(covidReportModel);
			}
			logger.info("::END::::createFileOnAWSS3:::::");
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("::ERROR IN::::createFileOnAWSS3::::: " + e);
		}
		return covidReportModelList;
	}
	
	
	

}