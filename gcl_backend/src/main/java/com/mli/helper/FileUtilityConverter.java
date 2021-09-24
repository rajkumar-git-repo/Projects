package com.mli.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mli.entity.CreditCardCustomerEntity;
import com.mli.entity.CustomerDetailsEntity;
import com.mli.filemaster.FileUtilityModel;

public class FileUtilityConverter {

	private static Logger logger = LoggerFactory.getLogger(FileUtilityConverter.class);

	public static FileUtilityModel getFileUtilityModel(String fileExtension, String docType,
			CustomerDetailsEntity customerDetails) {
		logger.info("::::::::::::::::::::: Generating fileUtility ::::::::::::::::::::::");	
		FileUtilityModel fileUtilityModel = new FileUtilityModel();		
		fileUtilityModel.setBankName(customerDetails.getMasterPolicyHolderName());
		fileUtilityModel.setLoanAppNo(customerDetails.getLoanAppNumber());
		fileUtilityModel.setScheme(customerDetails.getSchemeType());
		fileUtilityModel.setCompletionDate(customerDetails.getAppCompletionDate());
		fileUtilityModel.setDocType(docType);
		fileUtilityModel.setFileExtention(fileExtension);		
		fileUtilityModel.setCustFirstName(customerDetails.getCustomerFirstName());	
		fileUtilityModel.setPassportUploadDate(customerDetails.getPassportUploadDate());
		fileUtilityModel.setProposalNumber(customerDetails.getProposalNumber());
		fileUtilityModel.setCreatedOn(customerDetails.getCreatedOn());
		return fileUtilityModel;
	}
	
	/**
	 * 
	 * @param fileExtension
	 * @param docType
	 * @param creditCardCustomerEntity
	 * @return
	 * @author rajkumar
	 */
	public static FileUtilityModel getYBLCCFileUtilityModel(String fileExtension, String docType,CreditCardCustomerEntity creditCardCustomerEntity) {
		logger.info("::::::::::::::::::::: Generating fileUtility ::::::::::::::::::::::");	
		FileUtilityModel fileUtilityModel = new FileUtilityModel();		
		fileUtilityModel.setBankName(creditCardCustomerEntity.getMasterPolicyHolderName());
		fileUtilityModel.setCompletionDate(creditCardCustomerEntity.getAppCompletionDate());
		fileUtilityModel.setDocType(docType);
		fileUtilityModel.setFileExtention(fileExtension);		
		fileUtilityModel.setCustFirstName(creditCardCustomerEntity.getFirstName());	
		fileUtilityModel.setProposalNumber(creditCardCustomerEntity.getProposalNumber());
		fileUtilityModel.setCreatedOn(creditCardCustomerEntity.getCreatedOn());
		return fileUtilityModel;
	}
}
