package com.mli.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.mli.constants.Constant;
import com.mli.constants.DocType;
import com.mli.constants.FileExtention;
import com.mli.constants.MLIMessageConstants;
import com.mli.constants.Symbols;
import com.mli.dao.CamReportDetailsDao;
import com.mli.dao.CommonFileUploadDao;
import com.mli.dao.CovidReportDAO;
import com.mli.dao.CustomerDetailsDAO;
import com.mli.entity.CamReportDetailsEntity;
import com.mli.entity.CommonFileUploadEntity;
import com.mli.entity.CovidReportEntity;
import com.mli.entity.CustomerDetailsEntity;
import com.mli.enums.Status;
import com.mli.filemaster.FileUtilityModel;
import com.mli.model.response.ResponseModel;
import com.mli.security.JwtTokenUtil;
import com.mli.service.CreditCardJourneyService;
import com.mli.service.CustomerDetailService;
import com.mli.service.DownloadService;
import com.mli.service.SellerService;
import com.mli.utils.AES;
import com.mli.utils.CustomResponse;
import com.mli.utils.DateUtil;
import com.mli.utils.ZipFileNameUtil;
import com.mli.utils.ZipUtils;
import com.mli.utils.aws.AwsFileUtility;

/**
 * 
 * @author Haripal.Chauhan PDF and CDF download. Both PDF and CDF are
 *         downloading from AWS S3 bucket.
 *
 */
@RestController
public class DownloadFileController {

	@Value("${mli.xlx.file.sample}")
	private String xlxsFilePath;

	@Autowired
	private AwsFileUtility awsFileUtility;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private DownloadService downloadService;

	@Value("#{'${doc.temp.bulk}'}")
	private String tempBulkFilePath;

	@Value("${mli.aes.encryption.key}")
	private String aesSecratKey;

	@Autowired
	private SellerService sellerService;

	@Value("${mli.download.seller.excel}")
	private String sellerExcelPath;

	@Value("${mli.download.customer.excel}")
	private String customerExcelPath;

	@Value("${mli.download.zip}")
	private String zipDownloadPath;

	@Value("${mli.download.zip.bulk}")
	private String zipDownloadbulk;

	@Autowired
	private CustomerDetailService customerDetailService;

	@Autowired
	private CamReportDetailsDao camReportDetailsDao;
	
	@Autowired
	private CommonFileUploadDao commonFileUploadDao;
	
	@Autowired
	private CreditCardJourneyService creditCardJourneyService;
	
	@Autowired
	private CovidReportDAO covidReportDAO;
	
	@Autowired
	private CustomerDetailsDAO customerDetailsDAO;

	private static final Logger logger = Logger.getLogger(DownloadFileController.class);

	/*
	 * @RequestMapping(path = "/excel/downloadsample", method = RequestMethod.GET)
	 * public ResponseEntity<?> downloadExcelFile(@RequestParam(name = "token")
	 * String token) throws IOException { File file = new File(xlxsFilePath);
	 * HttpHeaders headers = new HttpHeaders(); headers.add("Cache-Control",
	 * "no-cache, no-store, must-revalidate"); headers.add("Content-Disposition",
	 * "attachment; filename=\"" + "download_sample"+ Symbols.DOT +
	 * FileExtention.XLSX + "\""); headers.add("Expires", "0");
	 * 
	 * InputStreamResource resource = new InputStreamResource(new
	 * FileInputStream(file)); return ResponseEntity.ok() .headers(headers)
	 * .contentLength(file.length())
	 * .contentType(MediaType.parseMediaType("application/octet-stream"))
	 * .body(resource); }
	 */
	/**
	 * To authorization request by token or data (AES access key which is getting
	 * from Custome-Screen)
	 * 
	 * @param token
	 * @param loanAppNo PDF download by loan app number.
	 */
	@RequestMapping(path = "/pdf/download", method = RequestMethod.GET)
	public ResponseEntity<?> downloadPDFFileFroms3(
			@RequestParam(name = "proposal", required = false) String proposalNumber,
			@RequestParam(name = "type", required = false) String fileType,
			@RequestParam(name = "trigger-screen", required = false) String triggerScreen,
			@RequestParam(name = "data", required = false) String data,
			@RequestParam(name = "mph", required = false) String mph,
			@RequestHeader(value = "Authorization", required = false) String token) throws IOException {
		logger.info("triggerScreen : " + triggerScreen);
		String decryptData = null;
		try {
			if (Constant.CS.equalsIgnoreCase(triggerScreen)) {
				decryptData = AES.decrypt(data, aesSecratKey);
				if (decryptData == null) {
					logger.info("decryptData : " + decryptData);
					return ResponseEntity.status(HttpStatus.OK)
							.body(new CustomResponse(Constant.FAILURE, "Invalid Access Key", null));
				}
				String values[] = decryptData.split("_");
				proposalNumber = values[1];
				fileType = null;
			} else {
				if (jwtTokenUtil.isTokenExpired(token)
						|| (!jwtTokenUtil.getUserRoleFromToken(token).contains("ROLE_ADMIN"))) {
					return ResponseEntity.badRequest().body(MLIMessageConstants.UNAUTHORIZED_REQUEST);
				}
			}

			// fileType is null, coming from customer-screen only
			if (fileType == null) {
				fileType = DocType.PROPOSAL;
			} else {
				fileType = fileType.toUpperCase();
				if (!(fileType.equals(DocType.PROPOSAL) || fileType.equals(DocType.PASSPORT))) {
					return ResponseEntity.status(HttpStatus.OK)
							.body(new CustomResponse(Constant.FAILURE, "Invalid file type", null));
				}
			}

			Map<String, Object> resultMap = downloadService.findByProposalNoNdDocType(proposalNumber, fileType, mph);
			String awsFilePath = "";
			StringBuilder fileName = new StringBuilder();
			if (resultMap != null) {
				awsFilePath = (String) resultMap.get(Constant.AWS_FILE_PATH);
				fileName.append(resultMap.get(Constant.LOAN_APP_NUMBER));
				if (awsFilePath != null) {
					if (fileType.equals(DocType.PROPOSAL)) {
						if (resultMap.get(Constant.APP_COMPLETION_DATE) != null) {
							fileName.append("-");
							fileName.append(resultMap.get(Constant.APP_COMPLETION_DATE));
						}
					}
				} else {
					return ResponseEntity.status(HttpStatus.OK).body(resultMap);
				}
			}
			HttpHeaders headers = new HttpHeaders();
			headers.set("fileName", fileName + Symbols.DOT + FileExtention.PDF);
			headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
			headers.add("Content-Disposition",
					"attachment; filename=\"" + fileName + Symbols.DOT + FileExtention.PDF + "\"");
			headers.add("Expires", "0");
			InputStreamResource resource = new InputStreamResource(
					awsFileUtility.getFileAsInputStreamFromAWSS3(awsFilePath));
			logger.info("token : " + token);
			if (token != null && !token.isEmpty()) {
				logger.info(":::::::::::: " + fileType + " is Downloaded successfully by Admin Username : "
						+ jwtTokenUtil.getUsernameFromToken(token) + " ::::::::::::");
			} else {
				logger.info(":::::::::::: " + fileType + " is Downloaded successfully by customer : " + decryptData
						+ " ::::::::::::");
			}
			return ResponseEntity.ok().headers(headers).contentType(MediaType.parseMediaType("application/pdf"))
					.body(resource);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(MLIMessageConstants.BAD_REQUEST);
		}
	}

	/**
	 * 
	 * @param token
	 * @param loanAppNo CDF download by loan app number.
	 */
//	@RequestMapping(path = "/cdf/download/{loanAppNo}", method = RequestMethod.GET)
	public ResponseEntity<?> downloadCDFFileFroms3(@RequestHeader(value = "Authorization", required = true) String token,
			@PathVariable("loanAppNo") String loanAppNo, @RequestParam(name = "proposalNumber") String proposalNumber)
			throws IOException {
		try {
			if (jwtTokenUtil.isTokenExpired(token)
					|| (!jwtTokenUtil.getUserRoleFromToken(token).contains("ROLE_ADMIN"))) {
				return ResponseEntity.badRequest().body(MLIMessageConstants.UNAUTHORIZED_REQUEST);
			}
			FileUtilityModel fileUtilityModel = new FileUtilityModel();
			fileUtilityModel.setLoanAppNo(loanAppNo);
			fileUtilityModel.setDocType(DocType.CDF);
			fileUtilityModel.setProposalNumber(proposalNumber);
			fileUtilityModel = awsFileUtility.getPDFFileNdDirectoryDetails(fileUtilityModel);
			logger.info("::::::::::::::::::::CDF Download file Path= " + fileUtilityModel.getAwsFilePath());
			HttpHeaders headers = new HttpHeaders();
			headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
			headers.add("Content-Disposition", "attachment; filename=\"" + fileUtilityModel.getFileName() + Symbols.DOT
					+ FileExtention.PDF + "\"");
			headers.add("Expires", "0");
			InputStreamResource resource = new InputStreamResource(
					awsFileUtility.getFileAsInputStreamFromAWSS3(fileUtilityModel.getAwsFilePath()));
			return ResponseEntity.ok().headers(headers)
					.contentType(MediaType.parseMediaType("application/octet-stream")).body(resource);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(MLIMessageConstants.BAD_REQUEST);
		}
	}

	/**
	 * api to download passport docs based on proposal number 
	 * @param proposalNumber
	 * @return
	 * @throws IOException
	 * @author rajkumar
	 */
	@RequestMapping(path = "/passport/download", method = RequestMethod.GET)
	public ResponseEntity<?> passportDownloadPDFFileFroms3(@RequestParam(name = "proposalNumber") String proposalNumber)
			throws IOException {
		try {
			/*
			 * if(jwtTokenUtil.isTokenExpired(token) ||
			 * (!jwtTokenUtil.getUserRoleFromToken(token).contains("ROLE_ADMIN"))){ return
			 * ResponseEntity.badRequest().body(MLIMessageConstants.UNAUTHORIZED_REQUEST); }
			 */
			Map<String, Object> resultMap = downloadService.findByProposalNoNdDocType(proposalNumber, DocType.PASSPORT,Constant.AXIS_BANK_LTD);
			String awsFilePath = "";
			if (resultMap != null) {
				awsFilePath = (String) resultMap.get(Constant.AWS_FILE_PATH);
				if (awsFilePath == null) {
					return ResponseEntity.status(HttpStatus.OK)
							.body(new CustomResponse(Constant.FAILURE, "Passport download Service", resultMap));
				}
			} else {
				return ResponseEntity.badRequest().body(Constant.FAILURE_MSG);
			}
			HttpHeaders headers = new HttpHeaders();
			headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
			headers.add("Content-Disposition",
					"attachment; filename=\"" + proposalNumber + Symbols.DOT + FileExtention.PDF + "\"");
			headers.add("Expires", "0");
			InputStreamResource resource = new InputStreamResource(
					awsFileUtility.getFileAsInputStreamFromAWSS3(awsFilePath));
			return ResponseEntity.ok().headers(headers)
					.contentType(MediaType.parseMediaType("application/octet-stream")).body(resource);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(MLIMessageConstants.BAD_REQUEST);
		}
	}

	/**
	 * Currently working on it
	 */
	@RequestMapping(path = "/passport/download/folder", method = RequestMethod.GET)
	public ResponseEntity<?> passportFolderDownloadPDFFileFroms3() throws IOException {
		try {
			AWSCredentials credentials = new BasicAWSCredentials("AKIAJD5OIP6YDLVNTSGA",
					"Lk1Vi1XUA3O4kjFMhvVLtGETxWmvYn78QYvIozX7");
			AmazonS3 s3 = new AmazonS3Client(credentials);

			/*
			 * String sourceDownloadPath =
			 * ZipUtils.getDirNameOfVerified(DateUtil.toCurrentUTCTimeStamp(),
			 * DocType.PROPOSAL,null);
			 * logger.info("sourceDownloadPath : "+sourceDownloadPath);
			 * awsFileUtility.bulkDownloadFromS3ToLocal(sourceDownloadPath,
			 * "/tmp/bulk_download",DocType.PROPOSAL);
			 * logger.info("HDF downloaded and zipped successfully to Local");
			 */

			// 1.) Bulk download from s3 and zipped it
//		    String key = "GCL/vr/test";
//		    String key = "GCL/14-09-2018/Axis/Level/PASSPORT";
			/*
			 * String key = "verfied_01-10-2018/PROPOSAL"; File dir = new File("/tmp/bulk");
			 * TransferManager transferManager = new TransferManager(s3);
			 * MultipleFileDownload download =
			 * transferManager.downloadDirectory("mlifortest", key, dir);
			 * download.waitForCompletion(); ZipUtils zipUtils = new ZipUtils(); boolean
			 * zippedFlag = zipUtils.zipDirectory("/tmp/bulk/"+key, "bulk.zip"); // boolean
			 * zippedFlag = zipUtils.zipDirectory("/tmp/bulk", "bulk.zip");
			 * logger.info("zippedFlag : "+zippedFlag); if (zippedFlag) { if (dir.exists())
			 * { logger.info("Deleted : " + dir.delete()); zipUtils.deleteFolder(dir); } }
			 */

			// 2.) Copy file from one location to another in s3
//			CopyObjectRequest req = new CopyObjectRequest("mlifortest", key+"/P1809201800004-Devendra-26-09-2018.pdf", "mlifortest", "GCL/vr/test");
//			CopyObjectResult res = s3.copyObject(req);

			// 3.) Download a zip file
			/*
			 * File file = new File("bulk.zip"); if (!file.exists()) {
			 * logger.info("file not found"); } FileInputStream in = new
			 * FileInputStream(file); InputStreamResource resource = new
			 * InputStreamResource(in); HttpHeaders headers = new HttpHeaders();
			 * headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
			 * headers.add("Content-Disposition", "attachment; filename=\"" + "zippedfile" +
			 * Symbols.DOT + "zip" + "\""); headers.add("Expires", "0"); return
			 * ResponseEntity.ok().headers(headers)
			 * .contentType(MediaType.parseMediaType("application/octet-stream")).body(
			 * resource);
			 */

			// 4.) Delete zip file (cron)
			/*
			 * File file = new File("bulk.zip"); if(file.exists()) {
			 * logger.info("file.delete() : "+file.delete()); }
			 */

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body(MLIMessageConstants.BAD_REQUEST);
		}
		return null;
	}

	/**
	 * download all PDF & Passport which is used by EOD report
	 * 
	 * @param verfiedDate
	 * @param fileName
	 * @param fileType
	 * @return
	 * @throws FileNotFoundException
	 */
	@GetMapping("/download/zip")
	public ResponseEntity<?> downloadZipFile(@RequestParam(name = "d", required = false) String verfiedDate,
			@RequestParam(name = "f", required = false) String fileName,
			@RequestParam(name = "t", required = false) String fileType) throws FileNotFoundException {

		String decryptDate = AES.decrypt(verfiedDate, aesSecratKey);
		logger.info("decryptDate :" + decryptDate);
		fileType = AES.decrypt(fileType, aesSecratKey);
		logger.info("fileType :" + fileType);
		boolean isValidDate = DateUtil.validateDateFormate(decryptDate, Constant.DATE_FORMAT);
		if (isValidDate) {
			/*
			 * if (!(DocType.PROPOSAL.equalsIgnoreCase(fileType) ||
			 * DocType.PASSPORT.equalsIgnoreCase(fileType))) { return
			 * ResponseEntity.status(HttpStatus.OK) .body(new
			 * CustomResponse(Constant.FAILURE, "Passport download Service",
			 * "Link is Invalid")); }
			 */
			Long veriDate = DateUtil.getDateStringToLong(decryptDate, Constant.DATE_FORMAT);

			File file = null;
//			File file = new File(zipDownloadPath+fileName);
//			if (!file.exists()) {
			logger.info("file not found");
			// download from s3
			try {
				String dirName = ZipUtils.getDirNameOfVerified(veriDate, fileType, null);
				awsFileUtility.bulkDownloadFromS3ToLocal(dirName, tempBulkFilePath, fileType);
				file = new File(zipDownloadPath + fileName);
				logger.info("File not found on server/local, New file download from s3 to server/local");
				if (!file.exists()) {
					logger.info("file not found (again)");
				}
			} catch (Exception exception) {
				exception.printStackTrace();
			}
//			}
			logger.info("File : " + file);
			try {
				FileInputStream in = new FileInputStream(file);
				InputStreamResource resource = new InputStreamResource(in);
				HttpHeaders headers = new HttpHeaders();
				headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
				headers.add("Content-Disposition", "attachment; filename=\"" + fileType + Symbols.DOT + "zip" + "\"");
				headers.add("Expires", "0");
				return ResponseEntity.ok().headers(headers)
						.contentType(MediaType.parseMediaType("application/octet-stream")).body(resource);
			} catch (Exception exception) {
				return ResponseEntity.status(HttpStatus.OK)
						.body(new CustomResponse(Constant.FAILURE, "Passport download Service", Constant.FAILURE_MSG));
			}
		}
		return ResponseEntity.status(HttpStatus.OK)
				.body(new CustomResponse(Constant.FAILURE, "Passport download Service", "Link Expired"));
	}

	/**
	 * Download all seller in excel
	 * 
	 * @param token
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(path = "/download/seller", method = RequestMethod.GET)
	public ResponseEntity<?> downloadSellerExcelFile(@RequestHeader("Authorization") String token) throws IOException {
		if (jwtTokenUtil.isTokenExpired(token) || (!jwtTokenUtil.getUserRoleFromToken(token).contains("ROLE_ADMIN"))) {
			return ResponseEntity.badRequest().body(MLIMessageConstants.UNAUTHORIZED_REQUEST);
		}
		boolean sellerDowonloadFlag = sellerService.getSellerInBulk();
		if (!sellerDowonloadFlag) {
			return ResponseEntity.status(HttpStatus.OK)
					.body(new CustomResponse(Constant.FAILURE, "Seller Excel download Service", Constant.FAILURE_MSG));
		}
		File file = new File(sellerExcelPath);
		if (!file.exists()) {
			return ResponseEntity.status(HttpStatus.OK)
					.body(new CustomResponse(Constant.FAILURE, "Seller Excel download Service", "Excel not found"));
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		headers.add("Content-Disposition",
				"attachment; filename=\"" + "seller" + Symbols.DOT + FileExtention.XLSX + "\"");
		headers.add("Expires", "0");

		InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
		return ResponseEntity.ok().headers(headers).contentLength(file.length())
				.contentType(MediaType.parseMediaType("application/octet-stream")).body(resource);
	}

	/**
	 * Download all customer in excel
	 * 
	 * @param token
	 * @throws IOException
	 */
	@RequestMapping(path = "/excel/customer", method = RequestMethod.GET)
	public ResponseEntity<?> downloadCustomerInExcel(@RequestHeader("Authorization") String token,
			@RequestParam(name = "from", required = false) String from,
			@RequestParam(name = "to", required = false) String to) throws IOException {
		if (jwtTokenUtil.isTokenExpired(token) || (!jwtTokenUtil.getUserRoleFromToken(token).contains("ROLE_ADMIN"))) {
			return ResponseEntity.badRequest().body(MLIMessageConstants.UNAUTHORIZED_REQUEST);
		}
		boolean customerDowonloadFlag = customerDetailService.getCustomerInBulk(from, to);
		if (!customerDowonloadFlag) {
			return ResponseEntity.status(HttpStatus.OK).body(
					new CustomResponse(Constant.FAILURE, "Customer Excel download Service", Constant.FAILURE_MSG));
		}
		File file = new File(customerExcelPath);
		if (!file.exists()) {
			return ResponseEntity.status(HttpStatus.OK)
					.body(new CustomResponse(Constant.FAILURE, "Customer Excel download Service", "Excel not found"));
		}
		HttpHeaders headers = new HttpHeaders();
		headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		headers.add("Content-Disposition",
				"attachment; filename=\"" + "customer" + Symbols.DOT + FileExtention.XLSX + "\"");
		headers.add("Expires", "0");

		InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
		return ResponseEntity.ok().headers(headers).contentLength(file.length())
				.contentType(MediaType.parseMediaType("application/octet-stream")).body(resource);
	}

	/**
	 * Download all PDF OR Passport(for Verified only) OR Excel depending on given
	 * date range
	 * 
	 * @throws Exception
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/download/admin")
	public ResponseEntity<?> download7daysZipFile(@RequestParam(name = "d", required = false) String verfiedDate,
			@RequestHeader("Authorization") String token, @RequestParam(name = "type", required = true) String fileType,
			@RequestParam(name = "from", required = false) String fromDate,
			@RequestParam(name = "to", required = false) String toDate,
			@RequestParam(name = "mphType", required = false) String mphType) throws FileNotFoundException, Exception {
		logger.info("fileType " + fileType + " ,from : " + fromDate + " ,to : " + toDate);
		try {
			// Download customer for last day
//			if ((fromDate == null && toDate == null) || (fromDate.isEmpty() && toDate.isEmpty())) {
//				fileType = Constant.EXCEL;
//			}
			StringBuilder fileName = new StringBuilder();
			StringBuilder extenstion = new StringBuilder();
			if (jwtTokenUtil.isTokenExpired(token)
					|| (!jwtTokenUtil.getUserRoleFromToken(token).contains("ROLE_ADMIN"))) {
				return ResponseEntity.badRequest().body(MLIMessageConstants.UNAUTHORIZED_REQUEST);
			}
			File file = null;
			fileType = DocType.Physical_Form_Journey.equals(fileType) || DocType.COVID_REPORT.equals(fileType) ? fileType : fileType.toUpperCase();
			if(Constant.AXIS_FINANCE.equalsIgnoreCase(mphType) && !fileType.equals(Constant.EXCEL)){
				fileType = "AFL_"+fileType;
			}
			if (fileType.equals(Constant.EXCEL)) {
				boolean customerDowonloadFlag = false;
				if(Constant.AXIS.equalsIgnoreCase(mphType) || Constant.AXIS_FINANCE.equalsIgnoreCase(mphType)) {
					customerDowonloadFlag = customerDetailService.getCustomerInBulk(fromDate, toDate);
				}else if(Constant.YESBANKCC.equalsIgnoreCase(mphType)) {
					customerDowonloadFlag = creditCardJourneyService.getYBLCCCustomerInBulk(fromDate, toDate);
				}
				if (!customerDowonloadFlag) {
					return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(Constant.FAILURE,
							"Customer Excel download Service", Constant.FAILURE_MSG));
				}
				file = new File(customerExcelPath);
				if (!file.exists()) {
					return ResponseEntity.status(HttpStatus.OK)
							.body(new CustomResponse(Constant.FAILURE, "Excel not found", null));
				}
				fileName.append(ZipFileNameUtil.getMISReportFileName(mphType, fromDate, toDate));
				extenstion.append(FileExtention.XLSX);
			} else {
				// Download zip file of PROPOSAL/PASSPORT
				Long from = DateUtil.dateFormaterInIST(fromDate + " " + Constant.START_TS,
						Constant.DATE_WITH_TS_FORMAT);
				Long to = DateUtil.dateFormaterInIST(toDate + " " + Constant.END_TS, Constant.DATE_WITH_TS_FORMAT);

				List<Long> dates = DateUtil.getAllDatesInLong(from, to);
				if (dates.size() > 7) {
					return ResponseEntity.status(HttpStatus.OK)
							.body(new CustomResponse(Constant.FAILURE, "Date Range should not be greater the 7", null));
				}
				List<String> dt = new ArrayList<>();
				for (Long d : dates) {
					dt.add(DateUtil.todayDateddmmyyyy(d, Constant.DATE_FORMAT));
					logger.info("Date : " + DateUtil.todayDateddmmyyyy(d, Constant.DATE_FORMAT));
					String dirName = ZipUtils.getDirNameOfVerifiedFor7days(d, fileType);
					try {
						logger.info(fileType + " : coping from : " + dirName);
						awsFileUtility.bulkDownloadFromS3ToLocalFor7days(dirName, tempBulkFilePath + "/admin",
								fileType);
					} catch (AmazonClientException | InterruptedException | IOException e) {
						e.printStackTrace();
					}

					// copy all files from tempBulkFilePath to zipDownloadbulk
					File source = new File(tempBulkFilePath + "/admin/" + dirName);
					File dest = new File(tempBulkFilePath + "/" + fileType);
					try {
						if (!source.exists()) {
							continue;
						}
						FileUtils.copyDirectory(source, dest);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				ZipUtils zipUtils = new ZipUtils();
				// deleted temp folder ,now all files are present in zipDownloadbulk folder
				zipUtils.deleteFolder(new File(tempBulkFilePath + "/admin"));

				file = new File(zipDownloadbulk);
				if (!file.exists()) {
					file.mkdirs();
				}
				boolean zippedFlag = zipUtils.zipDirectory(tempBulkFilePath + "/" + fileType,
						zipDownloadbulk + fileType + ".zip");
				logger.info("zippedFlag : " + zippedFlag);
				if (zippedFlag) {
					zipUtils.deleteFolder(new File(tempBulkFilePath));
				}
				file = new File(zipDownloadbulk + fileType + ".zip");
				logger.info("File : " + file);

				fileName.append(ZipFileNameUtil.getZIPReportFileName(fileType, mphType, fromDate, toDate));
				extenstion.append("zip");
			}
			try {
				FileInputStream in = new FileInputStream(file);
				InputStreamResource resource = new InputStreamResource(in);
				HttpHeaders headers = new HttpHeaders();
				headers.set("fileName", fileName.toString() + Symbols.DOT + extenstion.toString());
				headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
				headers.add("Content-Disposition",
						"attachment; filename=\"" + fileName.toString() + Symbols.DOT + extenstion.toString() + "\"");
				headers.add("Expires", "0");
				logger.info(":::::::::::: " + fileType + " is Downloaded successfully by Admin Username : "
						+ jwtTokenUtil.getUsernameFromToken(token) + " ::::::::::::");
				return ResponseEntity.ok().headers(headers)
						.contentType(MediaType.parseMediaType("application/octet-stream")).body(resource);
			} catch (Exception exception) {
				exception.printStackTrace();
				return ResponseEntity.status(HttpStatus.OK)
						.body(new CustomResponse(Constant.FAILURE, Constant.FAILURE_MSG, null));
			}
		} catch (Exception exception) {
			exception.printStackTrace();
			return ResponseEntity.status(HttpStatus.OK)
					.body(new CustomResponse(Constant.FAILURE, Constant.FAILURE_MSG, null));
		}
	}

	/**
	 * @a: download cam documents.
	 * @return the ResponseEntity with status 201 (Created) , return file
	 * @throws Exception
	 */
	@GetMapping("/downloads/cam-report")
	public ResponseEntity<?> downloadeCamFile(@RequestHeader("Authorization") String token, String fileUrl,
			@RequestParam(name = "proposalNumber", required = true) String proposalNumber) throws Exception {
		List<CamReportDetailsEntity> camReportList = downloadCamFile(proposalNumber);
		CustomerDetailsEntity customerDetailsEntity = customerDetailsDAO.findByProposalNumber(proposalNumber);
		String docType = Constant.AXIS_BANK_LTD.equalsIgnoreCase(customerDetailsEntity.getMasterPolicyHolderName()) ? DocType.CAM_REPORT: DocType.AFL_CAM_REPORT;
		String awsPath = null;
		if (!camReportList.isEmpty()) {
			awsPath = camReportList.get(0).getCamFolderPath();
		}else {
			return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(Constant.FAILURE, "File not found for a given proposal number", null));
		}

		StringBuilder fileName = new StringBuilder();
		File file = null;
		StringBuilder extenstion = new StringBuilder();
		awsFileUtility.bulkDownloadFromS3ToLocalFor7days(awsPath, tempBulkFilePath + "/admin", docType);
		ZipUtils zipUtils = new ZipUtils();
		String fileType = docType;

		// copy all files from tempBulkFilePath to zipDownloadbulk
		File source = new File(tempBulkFilePath + "/admin/" + awsPath);
		File dest = new File(tempBulkFilePath + "/" + awsPath);
		try {
			FileUtils.copyDirectory(source, dest);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// deleted temp folder ,now all files are present in zipDownloadbulk folder
		zipUtils.deleteFolder(new File(tempBulkFilePath + "/admin"));

		file = new File(zipDownloadbulk);
		if (!file.exists()) {
			file.mkdirs();
		}
		boolean zippedFlag = zipUtils.zipDirectory(tempBulkFilePath + "/" + fileType,
				zipDownloadbulk + fileType + ".zip");
		logger.info("zippedFlag : " + zippedFlag);

		if (zippedFlag) {
			zipUtils.deleteFolder(new File(tempBulkFilePath));
		}
		file = new File(zipDownloadbulk + fileType + ".zip");
		logger.info("File : " + file);

		fileName.append(fileType);
		extenstion.append("zip");
		try {
			FileInputStream in = new FileInputStream(file);
			InputStreamResource resource = new InputStreamResource(in);
			HttpHeaders headers = new HttpHeaders();
			headers.set("fileName", fileName.toString() + Symbols.DOT + extenstion.toString());
			headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
			headers.add("Content-Disposition",
					"attachment; filename=\"" + fileName.toString() + Symbols.DOT + extenstion.toString() + "\"");
			headers.add("Expires", "0");
			logger.info(":::::::::::: " + fileType + " is Downloaded successfully by Admin Username : "
					+ jwtTokenUtil.getUsernameFromToken(token) + " ::::::::::::");
			return ResponseEntity.ok().headers(headers)
					.contentType(MediaType.parseMediaType("application/octet-stream")).body(resource);
		} catch (Exception exception) {
			exception.printStackTrace();
			return ResponseEntity.status(HttpStatus.OK)
					.body(new CustomResponse(Constant.FAILURE, Constant.FAILURE_MSG, null));
		}

	}

	public List<CamReportDetailsEntity> downloadCamFile(String proposalNumber) throws Exception {
		List<CamReportDetailsEntity> camreportList = camReportDetailsDao.getProposals(proposalNumber);
		return camreportList;
	}

	/**
	 * @a: download Physical Form Journey.
	 * @return the ResponseEntity with status 201 (Created) , return file
	 * @throws Exception
	 */
	@GetMapping("/downloads/physical-form")
	public ResponseEntity<?> downloadePhysicalFormFile(@RequestHeader("Authorization") String token,
			@RequestParam(name = "proposalNumber", required = true) String proposalNumber) throws Exception {
		List<CommonFileUploadEntity> physicalDocList = downloadPhysicalFormFile(proposalNumber);
		CustomerDetailsEntity customerDetailsEntity = customerDetailsDAO.findByProposalNumber(proposalNumber);
	    String docType = Status.PHYSICAL_FORM_VERIFICATION == customerDetailsEntity.getStatus() ? DocType.Physical_Form_Journey : DocType.AFL_PHYSICAL_FROM;
		String awsPath = null;
		if (!physicalDocList.isEmpty()) {
			awsPath = physicalDocList.get(0).getFileFolderPath();
		}else {
			return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(Constant.FAILURE, "File not found for a given proposal number", null));
		}

		StringBuilder fileName = new StringBuilder();
		File file = null;
		StringBuilder extenstion = new StringBuilder();
		awsFileUtility.bulkDownloadFromS3ToLocalFor7days(awsPath, tempBulkFilePath + "/admin", docType);
		ZipUtils zipUtils = new ZipUtils();
		String fileType = docType;

		// copy all files from tempBulkFilePath to zipDownloadbulk
		File source = new File(tempBulkFilePath + "/admin/" + awsPath);
		File dest = new File(tempBulkFilePath + "/" + awsPath);
		try {
			FileUtils.copyDirectory(source, dest);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// deleted temp folder ,now all files are present in zipDownloadbulk folder
		zipUtils.deleteFolder(new File(tempBulkFilePath + "/admin"));

		file = new File(zipDownloadbulk);
		if (!file.exists()) {
			file.mkdirs();
		}
		boolean zippedFlag = zipUtils.zipDirectory(tempBulkFilePath + "/" + fileType,
				zipDownloadbulk + fileType + ".zip");
		logger.info("zippedFlag : " + zippedFlag);

		if (zippedFlag) {
			zipUtils.deleteFolder(new File(tempBulkFilePath));
		}
		file = new File(zipDownloadbulk + fileType + ".zip");
		logger.info("File : " + file);

		fileName.append(fileType+"#"+proposalNumber);
		extenstion.append("zip");
		try {
			FileInputStream in = new FileInputStream(file);
			InputStreamResource resource = new InputStreamResource(in);
			HttpHeaders headers = new HttpHeaders();
			headers.set("fileName", fileName.toString() + Symbols.DOT + extenstion.toString());
			headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
			headers.add("Content-Disposition",
					"attachment; filename=\"" + fileName.toString() + Symbols.DOT + extenstion.toString() + "\"");
			headers.add("Expires", "0");
			logger.info(":::::::::::: " + fileType + " is Downloaded successfully by Admin Username : "
					+ jwtTokenUtil.getUsernameFromToken(token) + " ::::::::::::");
			return ResponseEntity.ok().headers(headers)
					.contentType(MediaType.parseMediaType("application/octet-stream")).body(resource);
		} catch (Exception exception) {
			exception.printStackTrace();
			return ResponseEntity.status(HttpStatus.OK)
					.body(new CustomResponse(Constant.FAILURE, Constant.FAILURE_MSG, null));
		}

	}

	public List<CommonFileUploadEntity> downloadPhysicalFormFile(String proposalNumber) throws Exception {
		List<CommonFileUploadEntity> physicalFormList = commonFileUploadDao.getProposals(proposalNumber);
		return physicalFormList;
	}
	
	
	/**
	 * download physical form uploaded file
	 * @param proposalNumber
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(path = "/downloads/physical-file", method = RequestMethod.GET)
	public ResponseEntity<?> downloadePhysicalFormFileFromS3(@RequestParam(name = "proposalNumber", required = true) String proposalNumber,
			@RequestParam(name = "fileName", required = true) String fileName) throws IOException {
		try {
			List<CommonFileUploadEntity> physicalDocList = downloadPhysicalFormFile(proposalNumber);
			CommonFileUploadEntity commonFileUploadEntity = null;
			List<String> fileNameList = new ArrayList<String>(physicalDocList.size());
			if (!CollectionUtils.isEmpty(physicalDocList)) {
				commonFileUploadEntity = physicalDocList.get(0);
				fileNameList = physicalDocList.stream().map(CommonFileUploadEntity::getFileName).collect(Collectors.toList());
			} else {
				return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(Constant.FAILURE, "File not found for a given proposal number", null));
			}
			if(fileNameList.contains(fileName)) {
				List<String> allowedList = Arrays.asList("gif","png","jpg","jpeg","pdf");
				int i = fileName.lastIndexOf('.');
			    String extention = i > 0 ? fileName.substring(i + 1) : "";
				if (allowedList.contains(extention)) {
					String folderName = commonFileUploadEntity.getFileFolderPath();
					HttpHeaders headers = new HttpHeaders();
					headers.set("fileName", fileName);
					headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
					headers.add("Content-Disposition",
							"attachment; filename=\"" + fileName + "\"");
					headers.add("Expires", "0");
					InputStreamResource resource = new InputStreamResource(
							awsFileUtility.getFileAsInputStreamFromAWSS3(folderName + "/" +fileName));
					return ResponseEntity.ok().headers(headers).contentType(MediaType.parseMediaType("application/octet-stream"))
							.body(resource);
				}else {
					return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(Constant.FAILURE, "Extention not allowed", null));
			    }
			}else {
				return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(Constant.FAILURE, "File not found for a given file name", null));
			}
		} catch (Exception exception) {
			exception.printStackTrace();
			return ResponseEntity.status(HttpStatus.OK)
					.body(new CustomResponse(Constant.FAILURE, Constant.FAILURE_MSG, null));
		}
	}
	
	/**
	 * api to download physical file in base64 format
	 * @param proposalNumber
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(path = "/downloads/physicalForm-base64", method = RequestMethod.GET)
	public ResponseEntity<?> downloadePhysicalFormBase64FromS3(@RequestParam(name = "proposalNumber", required = true) String proposalNumber,
			@RequestParam(name = "fileName", required = true) String fileName) throws IOException {
		try {
			List<CommonFileUploadEntity> physicalDocList = downloadPhysicalFormFile(proposalNumber);
			CommonFileUploadEntity commonFileUploadEntity = null;
			List<String> fileNameList = new ArrayList<String>(physicalDocList.size());
			if (!CollectionUtils.isEmpty(physicalDocList)) {
				commonFileUploadEntity = physicalDocList.get(0);
				fileNameList = physicalDocList.stream().map(CommonFileUploadEntity::getFileName).collect(Collectors.toList());
			} else {
				return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(Constant.FAILURE, "File not found for a given proposal number", null));
			}
			if(fileNameList.contains(fileName)) {
				List<String> allowedList = Arrays.asList("gif","png","jpg","jpeg","pdf");
				int i = fileName.lastIndexOf('.');
			    String extention = i > 0 ? fileName.substring(i + 1) : "";
				if (allowedList.contains(extention)) {
					String folderName = commonFileUploadEntity.getFileFolderPath();
					InputStream in = awsFileUtility.getFileAsInputStreamFromAWSS3(folderName + "/" +fileName);
					byte[] bytes = IOUtils.toByteArray(in);
					String encoded = Base64.getEncoder().encodeToString(bytes);
					return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(Constant.SUCCESS, "Base64 generated successfully", encoded));
				}else {
					return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(Constant.FAILURE, "Extention not allowed", null));
			    }
			}else {
				return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(Constant.FAILURE, "File not found for a given file name", null));
			}
		} catch (Exception exception) {
			exception.printStackTrace();
			return ResponseEntity.status(HttpStatus.OK)
					.body(new CustomResponse(Constant.FAILURE, Constant.FAILURE_MSG, null));
		}
	}
	
	/**
	 * api to download covid report in base64 format
	 * @param proposalNumber
	 * @param fileName
	 * @param fileType
	 * @return
	 * @throws IOException
	 * @author rajkumar
	 */
	@RequestMapping(path = "/downloads/covidReport-base64", method = RequestMethod.GET)
	public ResponseEntity<?> downloadCovidReportBase64FromS3(@RequestParam(name = "proposalNumber", required = true) String proposalNumber,
			@RequestParam(name = "fileName", required = true) String fileName,@RequestParam(name = "fileType", required = true) String fileType) throws IOException {
		ResponseModel<String> model = downloadService.downloadBase64CovidReport(proposalNumber, fileName, fileType);
		return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(model.getStatus(), model.getMessage(), model.getData()));
	}
	
	
	/**
	 * api to download covid report in zip format
	 * @param token
	 * @param proposalNumber
	 * @return
	 * @throws Exception
	 * @author rajkumar
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/downloads/covidReport-zip")
	public ResponseEntity<?> downloadCovidReport(@RequestHeader("Authorization") String token,
			@RequestParam(name = "proposalNumber", required = true) String proposalNumber) throws Exception {
		List<CovidReportEntity> covidReportList = covidReportDAO.findAllReport(proposalNumber);
		CustomerDetailsEntity customerDetailsEntity = customerDetailsDAO.findByProposalNumber(proposalNumber);
		String docType = Constant.AXIS_BANK_LTD.equalsIgnoreCase(customerDetailsEntity.getMasterPolicyHolderName()) ? DocType.COVID_REPORT: DocType.AFL_COVID_REPORT;
		String awsPath = null;
		if (!covidReportList.isEmpty()) {
			awsPath = covidReportList.get(0).getFileFolderPath();
		} else {
			return ResponseEntity.status(HttpStatus.OK).body(new CustomResponse(Constant.FAILURE, "File not found for a given proposal number", null));
		}

		StringBuilder fileName = new StringBuilder();
		File file = null;
		StringBuilder extenstion = new StringBuilder();
		awsFileUtility.bulkDownloadFromS3ToLocalFor7days(awsPath, tempBulkFilePath + "/admin", docType);
		ZipUtils zipUtils = new ZipUtils();
		String fileType = docType;

		// copy all files from tempBulkFilePath to zipDownloadbulk
		File source = new File(tempBulkFilePath + "/admin/" + awsPath);
		File dest = new File(tempBulkFilePath + "/" + awsPath);
		try {
			FileUtils.copyDirectory(source, dest);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// deleted temp folder ,now all files are present in zipDownloadbulk folder
		zipUtils.deleteFolder(new File(tempBulkFilePath + "/admin"));

		file = new File(zipDownloadbulk);
		if (!file.exists()) {
			file.mkdirs();
		}
		boolean zippedFlag = zipUtils.zipDirectory(tempBulkFilePath + "/" + fileType,
				zipDownloadbulk + fileType + ".zip");
		logger.info("zippedFlag : " + zippedFlag);

		if (zippedFlag) {
			zipUtils.deleteFolder(new File(tempBulkFilePath));
		}
		file = new File(zipDownloadbulk + fileType + ".zip");
		logger.info("File : " + file);

		fileName.append(proposalNumber+" HDF Supplementary Documents");
		extenstion.append("zip");
		try {
			FileInputStream in = new FileInputStream(file);
			InputStreamResource resource = new InputStreamResource(in);
			HttpHeaders headers = new HttpHeaders();
			headers.set("fileName", fileName.toString() + Symbols.DOT + extenstion.toString());
			headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
			headers.add("Content-Disposition",
					"attachment; filename=\"" + fileName.toString() + Symbols.DOT + extenstion.toString() + "\"");
			headers.add("Expires", "0");
			logger.info(":::::::::::: " + fileType + " is Downloaded successfully by Admin Username : "
					+ jwtTokenUtil.getUsernameFromToken(token) + " ::::::::::::");
			return ResponseEntity.ok().headers(headers)
					.contentType(MediaType.parseMediaType("application/octet-stream")).body(resource);
		} catch (Exception exception) {
			exception.printStackTrace();
			return ResponseEntity.status(HttpStatus.OK)
					.body(new CustomResponse(Constant.FAILURE, Constant.FAILURE_MSG, null));
		}

	}
}

