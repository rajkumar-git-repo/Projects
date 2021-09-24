 package com.mli.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.mli.constants.Constant;
import com.mli.constants.DocType;
import com.mli.constants.FileExtention;
import com.mli.dao.CreditCardCustomerDAO;
import com.mli.dao.CronJobDAO;
import com.mli.dao.MISRecipientDAO;
import com.mli.dao.OtpDDAO;
import com.mli.entity.CreditCardCustomerEntity;
import com.mli.entity.CronJobEntity;
import com.mli.entity.MISRecipientEntity;
import com.mli.enums.CronJobType;
import com.mli.enums.MasterPolicyHolderName;
import com.mli.enums.OTPUserType;
import com.mli.enums.Status;
import com.mli.filemaster.FileUtilityModel;
import com.mli.modal.email.EmailModel;
import com.mli.modal.email.MliEmailServiceModel;
import com.mli.model.MISRecipientModel;
import com.mli.model.UserDetailsModel;
import com.mli.model.response.ResponseModel;
import com.mli.service.CustomerDetailService;
import com.mli.service.MliEmailService;
import com.mli.service.ReportEmailToBankService;
import com.mli.utils.DateUtil;
import com.mli.utils.ZipUtils;
import com.mli.utils.aws.AwsFileUtility;
import com.mli.utils.excel.ExcelGeneratorUtility;



/**
 * @author Nikhilesh.Tiwari
 *
 */
@Service
public class ReportEmailToBankServiceImpl implements ReportEmailToBankService {

	private static final Logger logger = Logger.getLogger(ReportEmailToBankServiceImpl.class);

	@Autowired
	private CustomerDetailService customerDetailService;

	@Autowired
	private ExcelGeneratorUtility excelUtility;

	@Autowired
	private MliEmailService mliEmailService;

	@Value("#{'${mli.axisbank.mailto}'}")
	private String MailToAxisBank;

	@Value("#{'${mli.axisbank.mailIdCc}'}")
	private String MailCCAxisBank;

	@Value("#{'${mli.axisbank.mailIdBcc}'}")
	private String MailBccAxisBank;

	@Value("#{'${mli.yesbank.mailto}'}")
	private String MailToYesBank;

	@Value("#{'${mli.yesbank.mailIdCc}'}")
	private String MailCCYesBank;

	@Value("#{'${mli.yesbank.mailIdBcc}'}")
	private String MailBccYesBank;

	@Value("#{'${mli.cron.file.server1}'}")
	private String mliCronFileServer1;
	
	@Value("#{'${mli.cron.file.server2}'}")
	private String mliCronFileServer2;
	
	@Value(value = "${doc.root}")
	private String docRoot;
	
	@Autowired
	private AwsFileUtility awsFileUtility;

	@Autowired
	private  OtpDDAO otpDAO;

	@Autowired
	private  CronJobDAO cronJobDAO;
	
	@Value("#{'${mli.uw.mailto}'}")
	private String mailToUWT;
	
	@Autowired
	private MISRecipientDAO misRecipientDao;
	
	@Autowired
	private CreditCardCustomerDAO creditCardCustomerDAO;

	
	/**
	 * Send a mail to bank automatically in excel format ,with user's current day data 
	 */
	@SuppressWarnings("resource")
	@Override
	@Transactional
	public void mailDailyReportToBank() throws Exception {
		String MailToAxisBank = "";
		String MailCCAxisBank = "";
		String MailBccAxisBank = "";
		String MailToYesBank = "";
		String MailCCYesBank = "";
		String MailBccYesBank = "";
		try
		{ 
			    List<MISRecipientEntity> misRecipientEntityList = misRecipientDao.getAll();
                if(!CollectionUtils.isEmpty(misRecipientEntityList)) {
                	for (MISRecipientEntity misRecipientEntity : misRecipientEntityList) {
            			if (Constant.AXIS.equals(misRecipientEntity.getMphType())) {
            				if (Constant.TO.equals(misRecipientEntity.getMailType()) && !StringUtils.isEmpty(misRecipientEntity.getMailId())) {
            					MailToAxisBank = misRecipientEntity.getMailId();
            				}
            				if (Constant.CC.equals(misRecipientEntity.getMailType()) && !StringUtils.isEmpty(misRecipientEntity.getMailId())) {
            					MailCCAxisBank = misRecipientEntity.getMailId();
            				}
            				if (Constant.BCC.equals(misRecipientEntity.getMailType()) && !StringUtils.isEmpty(misRecipientEntity.getMailId())) {
            					MailBccAxisBank = misRecipientEntity.getMailId();
            				}
            			}
            			if (Constant.YES.equals(misRecipientEntity.getMphType())) {
            				if (Constant.TO.equals(misRecipientEntity.getMailType()) && !StringUtils.isEmpty(misRecipientEntity.getMailId())) {
            					MailToYesBank = misRecipientEntity.getMailId();
            				}
            				if (Constant.CC.equals(misRecipientEntity.getMailType()) && !StringUtils.isEmpty(misRecipientEntity.getMailId())) {
            					MailCCYesBank = misRecipientEntity.getMailId();
            				}
            				if (Constant.BCC.equals(misRecipientEntity.getMailType()) && !StringUtils.isEmpty(misRecipientEntity.getMailId())) {
            					MailBccYesBank = misRecipientEntity.getMailId();
            				}
            			}
            		}
                }
				ResponseModel<MliEmailServiceModel> emailResponse = new ResponseModel<MliEmailServiceModel>();
//				for (Integer key : MasterPolicyHolderName.lookup.keySet()) {

//					List<UserDetailsModel> userDetailsModels = customerDetailService.getSubmitedProposalsByBankNdDate(
//							DateUtil.dateFormater(DateUtil.extractDateAsString(DateUtil.toCurrentUTCTimeStamp())),
//							MasterPolicyHolderName.getMasterPolicyHolder(key), Status.APP_COMPLETE);
					
					// if current date is 10-10-2018, from 08-10-2018 23:59:59 and to : 09-10-2018
					// 23:59:59
					Long to = DateUtil.addDaysWithUTCLastTS(-1);
					Long from = DateUtil.addDaysWithUTCLastTS(-2);
					List<UserDetailsModel> userDetailsModels = customerDetailService.getAllCustomerForExcel(from, to, null,
							Status.APP_VERIFIED);
				
					FileUtilityModel fileUtilityModel = new FileUtilityModel();
//					fileUtilityModel.setBankName(MasterPolicyHolderName.getMasterPolicyHolder(key).getLabel());
					fileUtilityModel.setBankName(Constant.AXIS);
					fileUtilityModel.setCompletionDate(DateUtil.toCurrentUTCTimeStamp());
					fileUtilityModel.setFileExtention(FileExtention.XLSX);
					String filePath = awsFileUtility.generateFilePath(fileUtilityModel);
//					excelUtility.createAndSaveExcelFile(userDetailsModels, filePath, fileUtilityModel.getBankName());
					excelUtility.getExcelData(userDetailsModels, filePath, fileUtilityModel.getBankName(),null);
					logger.info("Excel AWS S3 file path "+filePath);
					try{
						//Uploading file on AWS s3
						Long currentDateTime = DateUtil.toCurrentUTCTimeStamp();
						logger.info("::::::::::::::START::: Upload Excel on AWS S3 ::::::::::::::: " );
						/*FileUtilityModel fileDetails = awsFileUtility.getExcelFileDetails(currentDateTime,
								MasterPolicyHolderName.getMasterPolicyHolder(key));*/
						FileUtilityModel fileDetails = awsFileUtility.getExcelFileDetails(currentDateTime,
								Constant.AXIS);
						awsFileUtility.createFileOnAWSS3(filePath, fileDetails.getAwsFilePath());
//						awsFileUtility.updateAwsExcelFilePathInDB(currentDateTime, MasterPolicyHolderName.getMasterPolicyHolder(key), fileDetails.getAwsFilePath());
						awsFileUtility.updateAwsExcelFilePathInDB(currentDateTime, Constant.AXIS, fileDetails.getAwsFilePath());
						logger.info("::::::::::::::END::: Upload Excel on AWS S3 ::::::::::::::: " );
					}catch(Exception e){
						logger.error("::::::::::::::ERROR::: Upload Excel on AWS S3 ::::::::::::::: "+e.getMessage() );
					}
//				}    
//				for (Integer key : MasterPolicyHolderName.lookup.keySet()) {
					/*String xlsFilePath = awsFileUtility.getExcelFileLocalPath(DateUtil.toCurrentUTCTimeStamp(),
							MasterPolicyHolderName.getMasterPolicyHolder(key).getLabel());*/
					String xlsFilePath = awsFileUtility.getExcelFileLocalPath(DateUtil.toCurrentUTCTimeStamp(),
							Constant.AXIS);
					FileUtilityModel fileDetails = awsFileUtility.getExcelFileDetails(DateUtil.toCurrentUTCTimeStamp(),
							Constant.AXIS);
					logger.info("::::::File Name::::::::"+fileDetails.getFileName());
					EmailModel mliEmailModel= null;;
					String bankName = null;;
					try {
						mliEmailModel = new EmailModel();
						mliEmailModel.setMailUserType(OTPUserType.BANK.getLabel());
						mliEmailModel.setVerifiedDate(DateUtil.addDaysToCurrentTS(-1));
						mliEmailModel.setZipFileName(Constant.PROPOSAL_ZIP);
//						bankName = MasterPolicyHolderName.getMasterPolicyHolder(key).getLabel();
						bankName = Constant.AXIS;
						switch (bankName) {
						case "Yes":
							mliEmailModel.setMailIdTo(MailToYesBank);
							mliEmailModel.setMailIdBcc(MailBccYesBank);
							mliEmailModel.setMailIdCc(MailCCYesBank);
							break;
						case "Axis":
							mliEmailModel.setMailIdTo(MailToAxisBank);
							mliEmailModel.setMailIdBcc(MailBccAxisBank);
							mliEmailModel.setMailIdCc(MailCCAxisBank);
							break;
						}
						
						mliEmailModel.setName(fileDetails.getFileName());
						mliEmailModel.setType(FileExtention.XLSX);

						File readPdfFilePath = new File(xlsFilePath);
						byte[] byteArray = new byte[(int) readPdfFilePath.length()];
						/*if(readPdfFilePath.length()==0){
							continue;
						}*/
						FileInputStream fileInputStream = new FileInputStream(readPdfFilePath);
						fileInputStream.read(byteArray);
						String xslFile = new String(Base64.encodeBase64(byteArray));

						mliEmailModel.setBytes(xslFile);
					} catch (Exception e) {
						e.printStackTrace();
					}
					emailResponse = mliEmailService.sendEmail(mliEmailModel);
					if (!("200".equalsIgnoreCase(emailResponse.getCode())
							&& ("Ok").equalsIgnoreCase(emailResponse.getStatus()))) {
						logger.error("::::::::::::::::: Mail not Send to bank ::::::::::::::: " + mliEmailModel.getMailIdTo() + "at time : " + (new Date()));
					}else{
						logger.info("::::::::::::::::: Mail send  to bank  ::::::::::::::: "+bankName + " At emails : "+ mliEmailModel.getMailIdTo()+ ", at time : " + (new Date()));
					}
					sendMailToUWT();
//				}
			
			//cronJobEntity.setStatus(false);
			//cronJobDAO.saveOrUpdate(cronJobEntity);
		} catch (Exception e) {
			logger.error("::::::::::::::::::Error::::::mailDailyReportToBank::::::::::::"+e);
			e.printStackTrace();
		}

	}
	
	/**
	 * Sent mail to under writing team
	 */
	private void sendMailToUWT() {
		try {
			ResponseModel<MliEmailServiceModel> emailResponse = new ResponseModel<MliEmailServiceModel>();
			String xlsFilePath = awsFileUtility.getExcelFileLocalPath(DateUtil.toCurrentUTCTimeStamp(), Constant.AXIS);
			FileUtilityModel fileDetails = awsFileUtility.getExcelFileDetails(DateUtil.toCurrentUTCTimeStamp(),
					Constant.AXIS);
			EmailModel mliEmailModel = null;
			String bankName = null;
			try {
				mliEmailModel = new EmailModel();
				mliEmailModel.setMailUserType(OTPUserType.UWT.getLabel());
				mliEmailModel.setVerifiedDate(DateUtil.addDaysToCurrentTS(-1));
				mliEmailModel.setMailIdTo(mailToUWT);
//				mliEmailModel.setMailIdBcc(MailBccYesBank);
//				mliEmailModel.setMailIdCc(MailCCYesBank);

				mliEmailModel.setName(fileDetails.getFileName());
				mliEmailModel.setType(FileExtention.XLSX);

				File readPdfFilePath = new File(xlsFilePath);
				byte[] byteArray = new byte[(int) readPdfFilePath.length()];
				/*
				 * if(readPdfFilePath.length()==0){ continue; }
				 */
				FileInputStream fileInputStream = new FileInputStream(readPdfFilePath);
				fileInputStream.read(byteArray);
				String xslFile = new String(Base64.encodeBase64(byteArray));

				mliEmailModel.setBytes(xslFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
			emailResponse = mliEmailService.sendEmail(mliEmailModel);
			if (!("200".equalsIgnoreCase(emailResponse.getCode())
					&& ("Ok").equalsIgnoreCase(emailResponse.getStatus()))) {
				logger.error("::::::::::::::::: Mail not Send to Under Writing Team ::::::::::::::: " + mliEmailModel.getMailIdTo()
						+ "at time : " + (new Date()));
			} else {
				logger.info("::::::::::::::::: Mail send  to Under Writing Team  ::::::::::::::: " + bankName + " At emails : "
						+ mliEmailModel.getMailIdTo() + ", at time : " + (new Date()));
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	/**
	 * cron job to remove otp history from db
	 * @throws Exception
	 * @author rajkumar
	 */
	@Override
	@Transactional
	@Scheduled(cron = "${mli.cron.otp}")
	public void deleteOTPDetails() throws Exception {
		logger.info("::::::: cron start at time" + (new Date()));
		try {
			Integer totalDeletePr = otpDAO.removeOTPHistoryFromTable();
			logger.info("::::::: cron delete total number of OTP data  " + totalDeletePr + "at time" + (new Date()));
		} catch (Exception e) {
			logger.error(":::::::::::::::::::  Error in deleting from OTP History table ::::::::::");
			
		}
	}

	/**
	 * cron job to send daily report email to customer and yes bank customer
	 * 
	 * @author rajkumar
	 */
	@Override
	@Scheduled(cron = "${mli.cron.excel}")
	@Transactional
	public void sendExcelToBank() {
		try {
			mailDailyReportToBank();
			mailDailyYBLCCReportToBank();
			Path path = Paths.get(mliCronFileServer1);
			CronJobEntity cronJobEntity = cronJobDAO.getCronJobByType(CronJobType.EXCEL_TO_BANK, Boolean.FALSE);
			if((cronJobEntity != null) && Files.exists(path) ) {
				mailDailyReportToBank();
				cronJobEntity.setStatus(Boolean.TRUE);
				cronJobDAO.saveOrUpdate(cronJobEntity);	
				logger.info("::::::: Excel sent from SERVER1 ::::::::::::  " );
			}
		
		}catch(Exception e) {
			logger.error("::::::: Error in send excel from SERVER1  " );
			}
	}
	
	/**
	 * 
	 * retry to send daily report to customer
	 * @author rajkumar
	 */
	@Override
	@Scheduled(cron = "${mli.cron.excel.retry}")
	@Transactional
	public void retryExcelToBank() {
		try {
			Path path = Paths.get(mliCronFileServer2);
			CronJobEntity cronJobEntity = cronJobDAO.getCronJobByType(CronJobType.EXCEL_TO_BANK, Boolean.TRUE);
			if((cronJobEntity != null) && Files.exists(path) ) {
				if(cronJobEntity.getStatus()){
					cronJobEntity.setStatus(Boolean.FALSE);
					cronJobDAO.saveOrUpdate(cronJobEntity);
				}else{
					mailDailyReportToBank();
					cronJobEntity.setStatus(Boolean.FALSE);
					cronJobDAO.saveOrUpdate(cronJobEntity);
					logger.info("::::::: Excel sent in retry from SERVER2::::::::::::  " );
				}

			}
		}catch(Exception e) {
			logger.error("::::::: Error in retry excel from SERVER2 " );
		}


	}
	
	/**
	 * cron job to delete local folder in given path
	 * 
	 * @author rajkumar
	 */
	@Override
	@Scheduled(cron = "${mli.cron.local.folder}")
	@Transactional
	public void deleteLocalFolder() {		
		try{
			String  date =DateUtil.getYesturdayDateAsStringDashFormate();
			String path = docRoot + date;
			File dir = new File(path);
			awsFileUtility.rmdir(dir);
			logger.info(":::::: Local folder deleted for date ::::::::::::  "+ path);
		}catch(Exception e){
			logger.error("::::Error in ::: deleteLocalFolder:::::::: "+e.getMessage() );
		}
		
		
	}
	
	/**
	 * send daily application report email to customer based on current data
	 * @return
	 * @throws Exception
	 * @author rajkumar
	 */
	@SuppressWarnings("resource")
	@Override
	@Transactional
	public String mailDailyYBLCCReportToBank() throws Exception {
		String mailToYesBankCC = "";
		String mailCCYesBankCC = "";
		String mailBccYesBankCC = "";
		try {
			List<MISRecipientEntity> misRecipientEntityList = misRecipientDao.getAll();
			if (!CollectionUtils.isEmpty(misRecipientEntityList)) {
				for (MISRecipientEntity misRecipientEntity : misRecipientEntityList) {
					if (Constant.YESBANKCC.equals(misRecipientEntity.getMphType())) {
						if (Constant.TO.equals(misRecipientEntity.getMailType()) && !StringUtils.isEmpty(misRecipientEntity.getMailId())) {
							mailToYesBankCC = misRecipientEntity.getMailId();
						}
						if (Constant.CC.equals(misRecipientEntity.getMailType()) && !StringUtils.isEmpty(misRecipientEntity.getMailId())) {
							mailCCYesBankCC = misRecipientEntity.getMailId();
						}
						if (Constant.BCC.equals(misRecipientEntity.getMailType()) && !StringUtils.isEmpty(misRecipientEntity.getMailId())) {
							mailBccYesBankCC = misRecipientEntity.getMailId();
						}
					}
				}
			}
			ResponseModel<MliEmailServiceModel> emailResponse = new ResponseModel<MliEmailServiceModel>();

			Long from = DateUtil.addDaysWithUTCLastTS(-2);
			Long to = DateUtil.addDaysWithUTCLastTS(-1);
			List<CreditCardCustomerEntity> creditCardCustomerEntityList = creditCardCustomerDAO.getAllCustomerForExcelCron(from, to, Status.APP_VERIFIED);
			FileUtilityModel fileUtilityModel = new FileUtilityModel();
			fileUtilityModel.setBankName(Constant.YESBANKCC);
			fileUtilityModel.setCompletionDate(DateUtil.toCurrentUTCTimeStamp());
			fileUtilityModel.setFileExtention(FileExtention.XLSX);
			String filePath = awsFileUtility.generateFilePath(fileUtilityModel);
			excelUtility.getYBLCCExcelData(creditCardCustomerEntityList, filePath, fileUtilityModel.getBankName(),null);
			logger.info("Excel AWS S3 file path " + filePath);
			try {
				// Uploading file on AWS s3
				Long currentDateTime = DateUtil.toCurrentUTCTimeStamp();
				logger.info("::::::::::::::START::: Upload Excel on AWS S3 ::::::::::::::: ");
				FileUtilityModel fileDetails = awsFileUtility.getExcelFileDetails(currentDateTime, Constant.YESBANKCC);
				awsFileUtility.createFileOnAWSS3(filePath, fileDetails.getAwsFilePath());
				awsFileUtility.updateAwsExcelFilePathInDB(currentDateTime, Constant.YESBANKCC,fileDetails.getAwsFilePath());
				logger.info("::::::::::::::END::: Upload Excel on AWS S3 ::::::::::::::: ");
			} catch (Exception e) {
				logger.error("::::::::::::::ERROR::: Upload Excel on AWS S3 ::::::::::::::: " + e.getMessage());
			}
			String xlsFilePath = awsFileUtility.getExcelFileLocalPath(DateUtil.toCurrentUTCTimeStamp(), Constant.YESBANKCC);
			FileUtilityModel fileDetails = awsFileUtility.getExcelFileDetails(DateUtil.toCurrentUTCTimeStamp(), Constant.YESBANKCC);
			logger.info("::::::File Name::::::::" + fileDetails.getFileName());
			EmailModel mliEmailModel = null;
			String bankName = null;
			try {
				mliEmailModel = new EmailModel();
				mliEmailModel.setMailUserType(OTPUserType.YBLCCBANK.getLabel());
				mliEmailModel.setVerifiedDate(DateUtil.addDaysToCurrentTS(-1));
				mliEmailModel.setZipFileName(Constant.YBLCC_PROPOSAL_ZIP);
				mliEmailModel.setMailIdTo(mailToYesBankCC);
				mliEmailModel.setMailIdBcc(mailBccYesBankCC);
				mliEmailModel.setMailIdCc(mailCCYesBankCC);
				mliEmailModel.setName(fileDetails.getFileName());
				mliEmailModel.setType(FileExtention.XLSX);
				File readPdfFilePath = new File(xlsFilePath);
				byte[] byteArray = new byte[(int) readPdfFilePath.length()];
				FileInputStream fileInputStream = new FileInputStream(readPdfFilePath);
				fileInputStream.read(byteArray);
				String xslFile = new String(Base64.encodeBase64(byteArray));
				mliEmailModel.setBytes(xslFile);
			} catch (Exception e) {
				e.printStackTrace();
			}
			emailResponse = mliEmailService.sendEmail(mliEmailModel);
			if (!("200".equalsIgnoreCase(emailResponse.getCode())
					&& ("Ok").equalsIgnoreCase(emailResponse.getStatus()))) {
				logger.error("::::::::::::::::: Mail not Send to bank ::::::::::::::: " + mliEmailModel.getMailIdTo()+ "at time : " + (new Date()));
			} else {
				logger.info("::::::::::::::::: Mail send  to bank  ::::::::::::::: " + bankName + " At emails : "+ mliEmailModel.getMailIdTo() + ", at time : " + (new Date()));
			}
			// sendMailToUWT();
		} catch (Exception e) {
			logger.error("::::::::::::::::::Error::::::mailDailyReportToBank::::::::::::" + e);
			e.printStackTrace();
		}
		return "success";

	}

}