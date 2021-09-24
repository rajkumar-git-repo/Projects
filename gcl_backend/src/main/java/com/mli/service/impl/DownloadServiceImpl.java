package com.mli.service.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.mli.constants.Constant;
import com.mli.dao.CovidReportDAO;
import com.mli.dao.CreditCardCustomerDAO;
import com.mli.dao.CustomerDetailsDAO;
import com.mli.entity.CovidReportEntity;
import com.mli.entity.CreditCardCustomerEntity;
import com.mli.entity.CustomerDetailsEntity;
import com.mli.filemaster.SchemeDirDao;
import com.mli.filemaster.SchemeDirEntity;
import com.mli.model.UserDetailsModel;
import com.mli.model.response.ResponseModel;
import com.mli.service.DownloadService;
import com.mli.utils.CustomResponse;
import com.mli.utils.DateUtil;
import com.mli.utils.aws.AwsFileUtility;

@Service
public class DownloadServiceImpl implements DownloadService {

	private static final Logger logger = LoggerFactory.getLogger(DownloadServiceImpl.class);
	
	@Autowired
	private SchemeDirDao schemeDirDao;
	
	@Autowired
	private CustomerDetailsDAO customerDetailsDAO; 
	
	@Autowired
	private CreditCardCustomerDAO creditCardCustomerDAO;
	
	@Autowired
	private CovidReportDAO covidReportDAO;
	
	@Autowired
	private AwsFileUtility awsFileUtility;
	
	/**
	 * return list of SchemeDirEntity map object based on given parameter
	 * @param proposalNumber
	 * @param docType
	 * @param mph
	 * @return
	 * @author rajkumar
	 */
	@Transactional
	@Override
	public Map<String, Object> findByProposalNoNdDocType(String proposalNumber, String docType, String mph) {
		Map<String, Object> resultMap = new HashMap<>();
		try {
			SchemeDirEntity schemeDirEntity = schemeDirDao.findByProposalNumberNdeDocType(proposalNumber, docType);
			if (schemeDirEntity != null) {
				resultMap.put(Constant.AWS_FILE_PATH, schemeDirEntity.getAwsFilePath());
				if(Constant.YBLCC.equals(mph)) {
					CreditCardCustomerEntity creditCardCustomerEntity = creditCardCustomerDAO.findByProposalNumber(proposalNumber);
					if (!ObjectUtils.isEmpty(creditCardCustomerEntity)) {
						resultMap.put(Constant.LOAN_APP_NUMBER, creditCardCustomerEntity.getProposalNumber());
						resultMap.put(Constant.APP_COMPLETION_DATE,
								creditCardCustomerEntity.getAppCompletionDate() != null
										? DateUtil.extractDateAsStringDashFormate(creditCardCustomerEntity.getAppCompletionDate())
										: null);
					}
				} else {
					CustomerDetailsEntity customerDtl = customerDetailsDAO.findByProposalNumber(proposalNumber);
					if (customerDtl != null) {
						resultMap.put(Constant.LOAN_APP_NUMBER, customerDtl.getLoanAppNumber());
						resultMap.put(Constant.APP_COMPLETION_DATE,
								customerDtl.getAppCompletionDate() != null
										? DateUtil.extractDateAsStringDashFormate(customerDtl.getAppCompletionDate())
										: null);
					}
				}
			} else {
				resultMap.put(Constant.STATUS, Constant.FAILURE);
				resultMap.put(Constant.MESSAGE, "Aws file path is not exist for given Proposal Number " + proposalNumber
						+ " and DocType " + docType);
			}
		} catch (Exception exception) {
			logger.error("Exception occured while fetching SchemeDirEntity :", exception);
			resultMap.put(Constant.STATUS, Constant.FAILURE);
			resultMap.put(Constant.MESSAGE, "Exception occured while fetching Aws file path");
			exception.printStackTrace();
		}
		return resultMap;
	}

	/**
	 * method is used to download covid report in base64 format
	 * @param proposalNumber
	 * @param fileName
	 * @param fileType
	 * @return
	 * @author rajkumar
	 */
	@Transactional
	@Override
	public ResponseModel<String> downloadBase64CovidReport(String proposalNumber, String fileName, String fileType) {
		ResponseModel<String> responseModel = new ResponseModel<String>();
		try {
			List<CovidReportEntity> covidReportList = covidReportDAO.findByFileType(proposalNumber, fileType);
			CovidReportEntity covidReportEntity = null;
			List<String> fileNameList = new ArrayList<String>(covidReportList.size());
			String fileTypeFolder = Constant.TEST_REPORT.equals(fileType) ? "TestReport" : Constant.VACCINE_REPORT.equals(fileType) ? "VaccineReport" : fileType;
			if (!CollectionUtils.isEmpty(covidReportList)) {
				covidReportEntity = covidReportList.get(0);
				fileNameList = covidReportList.stream().map(CovidReportEntity::getFileName).collect(Collectors.toList());
			} else {
				responseModel.setData(null);
				responseModel.setStatus(Constant.FAILURE);
				responseModel.setMessage("File not found for a given proposal number");
				return responseModel;
			}
			if(fileNameList.contains(fileName)) {
				List<String> allowedList = Arrays.asList("gif","png","jpg","jpeg","pdf");
				int i = fileName.lastIndexOf('.');
			    String extention = i > 0 ? fileName.substring(i + 1) : "";
				if (allowedList.contains(extention)) {
					String folderName = covidReportEntity.getFileFolderPath();
					InputStream in = awsFileUtility.getFileAsInputStreamFromAWSS3(folderName +"/"+fileTypeFolder+ "/" +fileName);
					byte[] bytes = IOUtils.toByteArray(in);
					String encoded = Base64.getEncoder().encodeToString(bytes);
					responseModel.setData(encoded);
					responseModel.setStatus(Constant.SUCCESS);
					responseModel.setMessage("Base64 generated successfully");
					return responseModel;
				}else {
					responseModel.setData(null);
					responseModel.setStatus(Constant.FAILURE);
					responseModel.setMessage("Extention not allowed");
					return responseModel;
			    }
			}else {
				responseModel.setData(null);
				responseModel.setStatus(Constant.FAILURE);
				responseModel.setMessage("File not found for a given file name");
				return responseModel;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
			responseModel.setData(null);
			responseModel.setStatus(Constant.FAILURE);
			responseModel.setMessage(Constant.FAILURE_MSG);
			return responseModel;
		}
	}
}
