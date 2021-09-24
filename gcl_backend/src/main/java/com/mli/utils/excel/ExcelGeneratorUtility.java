package com.mli.utils.excel;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.crypt.EncryptionMode;
import org.apache.poi.poifs.crypt.Encryptor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.mli.constants.Constant;
import com.mli.constants.Covid19QuestionConstant;
import com.mli.constants.DocType;
import com.mli.constants.SFQHealthQuestionConstant;
import com.mli.constants.YBLCCHealthQuestion;
import com.mli.dao.CreditCardCovidDAO;
import com.mli.dao.CreditCardHealthDAO;
import com.mli.dao.CreditCardMandatoryDAO;
import com.mli.dao.CreditCardNomineeDAO;
import com.mli.dao.MasterReflexiveQuestionDao;
import com.mli.dao.PaymentDAO;
import com.mli.dao.SellerDAO;
import com.mli.entity.CreditCardCovidEntity;
import com.mli.entity.CreditCardCustomerEntity;
import com.mli.entity.CreditCardHealthEntity;
import com.mli.entity.CreditCardMandatoryEntity;
import com.mli.entity.CreditCardNomineeEntity;
import com.mli.entity.MasterReflexiveQuestionEntity;
import com.mli.entity.PaymentEntity;
import com.mli.entity.SellerDetailEntity;
import com.mli.enums.LoanType;
import com.mli.enums.RelationshipWithAssured;
import com.mli.model.Covid_19Model;
import com.mli.model.CreditCardCustomerModel;
import com.mli.model.CreditCardHealthModel;
import com.mli.model.CreditCardJourneyModel;
import com.mli.model.CreditCardMandatoryModel;
import com.mli.model.CreditCardNomineeModel;
import com.mli.model.CustomerDetailsModel;
import com.mli.model.HealthAnswerModel;
import com.mli.model.HealthDeclarationModel;
import com.mli.model.MandatoryDeclarationModel;
import com.mli.model.NomineeDetailsModel;
import com.mli.model.SFQCovidModel;
import com.mli.model.SFQCovidTestModel;
import com.mli.model.SFQCovidVaccineModel;
import com.mli.model.SFQHealthDeclarationModel;
import com.mli.model.SellerBankModel;
import com.mli.model.SellerDetailModel;
import com.mli.model.UserDetailsModel;
import com.mli.utils.CIRiderQuesConstant;
import com.mli.utils.DateUtil;
import com.mli.utils.PasswordGenerateUtil;
import com.mli.utils.ZipUtils;
import com.mli.utils.aws.AwsFileUtility;

/**
 * 
 * @author Haripal.Chauhan Generating excel with XSSFWorkbook class and
 *         protecting with password. Password pattern is: YES<mmddyyyy> format.
 */
@Component
public class ExcelGeneratorUtility {

	@Value("#{'${doc.temp}'}")
	private String tempFilePath;

	@Value("#{'${doc.temp.bulk}'}")
	private String tempBulkFilePath;

	@Value("${mli.download.seller.excel}")
	private String sellerExcelPath;

	@Autowired
	private MasterReflexiveQuestionDao masterReflexiveQuestionDao;

	@Autowired
	private AwsFileUtility awsFileUtility;

	@Autowired
	private SellerDAO sellerDAO;
	
	@Autowired
    private CreditCardNomineeDAO creditCardNomineeDAO;
	
	@Autowired
	private CreditCardHealthDAO creditCardHealthDAO;
	
	@Autowired
	private CreditCardCovidDAO creditCardCovidDAO;
	
	@Autowired
	private CreditCardMandatoryDAO creditCardMandatoryDAO;
	
	@Autowired
	private PaymentDAO paymentDAO;
	
	private static final String AGREE = "Agree";
	private static final String DISAGREE = "Disagree";
	private static final Logger logger = Logger.getLogger(ExcelGeneratorUtility.class);

	/**
	 * 
	 * @param userDetailModels
	 * @param filePath
	 * @param masterPolicyHolderName Excel generation for completed proposal.
	 */
	public void createAndSaveExcelFile(List<UserDetailsModel> userDetailModels, String filePath,
			String masterPolicyHolderName) {
		try {
			XSSFWorkbook workbook = new XSSFWorkbook();
			logger.info("::::START::::createAndSaveExcelFile::::::::::::::::::");
			// create excel xls sheet
			XSSFSheet sheet = workbook.createSheet("Customer Proposals");
			// create style for header cells
			XSSFCellStyle style = workbook.createCellStyle();
			XSSFCellStyle styleNumber = workbook.createCellStyle();
			XSSFFont font = workbook.createFont();
			font.setFontName("Normal");
			font.setBold(true);
			style.setFont(font);
			DataFormat format = workbook.createDataFormat();
			styleNumber.setDataFormat(format.getFormat("0.00"));
			sheet.setColumnWidth(0, 7000);
			sheet.setColumnWidth(1, 7000);
			sheet.setColumnWidth(2, 7000);
			sheet.setColumnWidth(3, 7000);
			sheet.setColumnWidth(4, 7000);
			sheet.setColumnWidth(5, 7000);
			sheet.setColumnWidth(6, 7000);
			sheet.setColumnWidth(7, 7000);
			sheet.setColumnWidth(8, 7000);
			sheet.setColumnWidth(9, 7000);
			sheet.setColumnWidth(10, 7000);
			sheet.setColumnWidth(11, 7000);
			sheet.setColumnWidth(12, 7000);
			sheet.setColumnWidth(13, 7000);
			sheet.setColumnWidth(14, 7000);
			sheet.setColumnWidth(15, 7000);
			sheet.setColumnWidth(16, 7000);
			sheet.setColumnWidth(17, 7000);
			sheet.setColumnWidth(18, 7000);
			sheet.setColumnWidth(18, 7000);
			sheet.setColumnWidth(19, 7000);
			sheet.setColumnWidth(20, 7000);
			sheet.setColumnWidth(21, 7000);
			sheet.setColumnWidth(22, 7000);
			sheet.setColumnWidth(23, 7000);
			sheet.setColumnWidth(24, 7000);
			sheet.setColumnWidth(25, 7000);
			sheet.setColumnWidth(26, 7000);
			sheet.setColumnWidth(27, 7000);

			// create header row
			XSSFRow header = sheet.createRow(0);
			header.createCell(0).setCellValue("Customer Name");
			header.getCell(0).setCellStyle(style);

			header.createCell(1).setCellValue("Type of Loan");
			header.getCell(1).setCellStyle(style);

			header.createCell(2).setCellValue("Loan Tenure/Coverage Tenure");
			header.getCell(2).setCellStyle(style);

			header.createCell(3).setCellValue("Coverage Scheme type");
			header.getCell(3).setCellStyle(style);

			header.createCell(4).setCellValue("Master Policyholder Name");
			header.getCell(4).setCellStyle(style);

			header.createCell(5).setCellValue("Customer Relationship with Group Policyholder");
			header.getCell(5).setCellStyle(style);

			header.createCell(6).setCellValue("Loan Application No");
			header.getCell(6).setCellStyle(style);

			header.createCell(7).setCellValue("Loan Amount/ Sum assured");
			header.getCell(7).setCellStyle(style);

			header.createCell(8).setCellValue("Customer Date of Birth");
			header.getCell(8).setCellStyle(style);

			header.createCell(9).setCellValue("Customer Mobile no.");
			header.getCell(9).setCellStyle(style);

			header.createCell(10).setCellValue("Email ID");
			header.getCell(10).setCellStyle(style);

			header.createCell(11).setCellValue("Name of Beneficiary (Given & Surname)");
			header.getCell(11).setCellStyle(style);

			header.createCell(12).setCellValue("Beneficiary Date of Birth");
			header.getCell(12).setCellStyle(style);

			header.createCell(13).setCellValue("Gender");
			header.getCell(13).setCellStyle(style);

			header.createCell(14).setCellValue("Relationship with the Assured");
			header.getCell(14).setCellStyle(style);

			header.createCell(15).setCellValue("Name of Appointee");
			header.getCell(15).setCellStyle(style);

			header.createCell(16).setCellValue("Appointee Date of Birth");
			header.getCell(16).setCellStyle(style);

			header.createCell(17).setCellValue("Gender");
			header.getCell(17).setCellStyle(style);

			header.createCell(18).setCellValue("Relationship with the Beneficiary");
			header.getCell(18).setCellStyle(style);
			header.createCell(19).setCellValue("Health Declaration Form");
			header.getCell(19).setCellStyle(style);

			header.createCell(20).setCellValue("If declaration is negative, please provide details");
			header.getCell(20).setCellStyle(style);

			header.createCell(21)
					.setCellValue("If you have other Life insurance or Health insurance policy, please provide number");
			header.getCell(21).setCellStyle(style);

			header.createCell(22).setCellValue("Mandatory Declarations");
			header.getCell(22).setCellStyle(style);

			header.createCell(23).setCellValue("Signed date");
			header.getCell(23).setCellStyle(style);

			header.createCell(24).setCellValue("Place");
			header.getCell(24).setCellStyle(style);

			header.createCell(25).setCellValue("Application Submission  Date");
			header.getCell(25).setCellStyle(style);

			header.createCell(26).setCellValue("Aadhaar No");
			header.getCell(26).setCellStyle(style);

			header.createCell(27).setCellValue("CAS ID");
			header.getCell(27).setCellStyle(style);

			int rowCount = 1;

			for (UserDetailsModel userDetailsModel : userDetailModels) {
				XSSFRow userRow = sheet.createRow(rowCount++);
				CustomerDetailsModel customerDetailsModel = userDetailsModel.getCustomerDetails();
				NomineeDetailsModel nomineeDetails = userDetailsModel.getNomineeDetails();
				MandatoryDeclarationModel mandatoryDeclaration = userDetailsModel.getMandatoryDeclaration();
				HealthDeclarationModel healthDeclaration = userDetailsModel.getHealthDeclaration();

				if (customerDetailsModel != null) {
					StringBuilder fullName = new StringBuilder();
					if (customerDetailsModel.getCustomerFirstName() != null) {
						fullName.append(customerDetailsModel.getCustomerFirstName());
					}
					if (customerDetailsModel.getCustomerLastName() != null) {
						fullName.append(" ");
						fullName.append(customerDetailsModel.getCustomerLastName());
					}
					userRow.createCell(0).setCellValue(fullName.toString());
					LoanType loanType = LoanType.getLoanType(userDetailsModel.getCustomerDetails().getLoanType());
					userRow.createCell(1).setCellValue(loanType.getCode() + "-" + loanType.getText());
					userRow.createCell(2).setCellValue(customerDetailsModel.getLoanTenure());
					userRow.createCell(3).setCellValue(customerDetailsModel.getSchemeType());
					userRow.createCell(4).setCellValue(customerDetailsModel.getMasterPolicyholderName());
					userRow.createCell(5).setCellValue(customerDetailsModel.getRelationshipGpPolicyHolder());
					userRow.createCell(6).setCellValue(customerDetailsModel.getLoanAppNumber());
					DecimalFormat decimalFormat = new DecimalFormat("0.00");
					String sumAssuredAsString = null;
					if (userDetailsModel.getCustomerDetails().getSumAssured() != null) {
						sumAssuredAsString = decimalFormat
								.format(userDetailsModel.getCustomerDetails().getSumAssured());
					}
					userRow.createCell(7).setCellValue(sumAssuredAsString);
					userRow.getCell(7).setCellStyle(styleNumber);
					userRow.createCell(8).setCellValue(customerDetailsModel.getDob());
					userRow.createCell(9).setCellValue(customerDetailsModel.getCustMobileNo());
					userRow.createCell(10).setCellValue(customerDetailsModel.getCustEmailId());

				}

				if (nomineeDetails != null) {
					StringBuilder fullName = new StringBuilder();
					if (nomineeDetails.getNomineeFirstName() != null) {
						fullName.append(nomineeDetails.getNomineeFirstName());
					}
					if (nomineeDetails.getNomineeLastName() != null) {
						fullName.append(" ");
						fullName.append(nomineeDetails.getNomineeLastName());
					}
					userRow.createCell(11).setCellValue(fullName.toString());
					userRow.createCell(12).setCellValue(nomineeDetails.getDateOfBirth());
					userRow.createCell(13).setCellValue(nomineeDetails.getGender());
					if (nomineeDetails.getRelationWitHAssured()
							.equalsIgnoreCase(RelationshipWithAssured.OTHERS.getLabel())) {
						userRow.createCell(14).setCellValue(nomineeDetails.getRelationWithNominee());
					} else {
						userRow.createCell(14).setCellValue(nomineeDetails.getRelationWitHAssured());
					}

					if (nomineeDetails.getAppointeeDetails() != null) {
						StringBuilder nomFullName = new StringBuilder();
						if (nomineeDetails.getAppointeeDetails().getAppointeeFirstName() != null) {
							nomFullName.append(nomineeDetails.getAppointeeDetails().getAppointeeFirstName());
						}
						if (nomineeDetails.getAppointeeDetails().getAppointeeLastName() != null) {
							nomFullName.append(" ");
							nomFullName.append(nomineeDetails.getAppointeeDetails().getAppointeeLastName());
						}
						userRow.createCell(15).setCellValue(nomFullName.toString());
						userRow.createCell(16).setCellValue(nomineeDetails.getAppointeeDetails().getDateOfBirth());
						userRow.createCell(17).setCellValue(nomineeDetails.getAppointeeDetails().getGender());
						if (nomineeDetails.getAppointeeDetails().getRelationWithAssured()
								.equalsIgnoreCase(RelationshipWithAssured.OTHERS.getLabel())) {
							userRow.createCell(18)
									.setCellValue(nomineeDetails.getAppointeeDetails().getRelationWithAppointee());
						} else {
							userRow.createCell(18)
									.setCellValue(nomineeDetails.getAppointeeDetails().getRelationWithAssured());
						}
					}
				}

				if (healthDeclaration != null) {
					if (!healthDeclaration.getIsHealthDeclaration()) {
						userRow.createCell(19).setCellValue(AGREE);
					} else {
						userRow.createCell(19).setCellValue(DISAGREE);
					}
					userRow.createCell(20).setCellValue(healthDeclaration.getNegativeDeclaration());
					userRow.createCell(21).setCellValue(healthDeclaration.getOtherInsurance());
				}

				if (mandatoryDeclaration != null) {
					if (mandatoryDeclaration.getIsMandatoryDeclaration()) {
						userRow.createCell(22).setCellValue(AGREE);
					} else {
						userRow.createCell(22).setCellValue(DISAGREE);
					}
					userRow.createCell(23)
							.setCellValue(DateUtil.extractDateAsString(mandatoryDeclaration.getSignedDate()));
					userRow.createCell(24).setCellValue(mandatoryDeclaration.getPlace());

				}
				if (customerDetailsModel != null) {
					userRow.createCell(25)
							.setCellValue(DateUtil.extractDateAsString(customerDetailsModel.getAppCompletionDate()));
					userRow.createCell(26).setCellValue(customerDetailsModel.getAdhaarNumber().toString());
//					userRow.createCell(27).setCellValue(customerDetailsModel.getCasID());
					userRow.createCell(27).setCellValue("");
				}
			}
			// saves sheet as read write password protected
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
			encrypt(bis, new FileOutputStream(filePath), PasswordGenerateUtil.getExcelPassword(masterPolicyHolderName));
			bis.close();
			bos.close();
			workbook.close();
			logger.info("::::END::::createAndSaveExcelFile:::Excel file has been generated!:::::::::::::::");
		} catch (Exception ex) {
			logger.error("::::::::Problem in Excel utility createAndSaveExcelFile:::::::::::::::::" + ex);
		}
	}

	public void getExcelData(List<UserDetailsModel> userDetailModels, String filePath, String masterPolicyHolderName,
			String action) {
		try {
			XSSFWorkbook workbook = new XSSFWorkbook();
			logger.info("::::START::::getExcelData::::::::::::::::::");
			// create excel xls sheet
			XSSFSheet sheet = workbook.createSheet("Customer Proposals");
			// create style for header cells
			XSSFCellStyle style = workbook.createCellStyle();
			XSSFCellStyle styleNumber = workbook.createCellStyle();
			XSSFFont font = workbook.createFont();
			font.setFontName("Normal");
			font.setBold(true);
			style.setFont(font);
			style.setBorderLeft(BorderStyle.THIN);
			style.setBorderRight(BorderStyle.THIN);
			style.setBorderBottom(BorderStyle.THIN);
			DataFormat format = workbook.createDataFormat();
			styleNumber.setDataFormat(format.getFormat("0.00"));
			for (int columnCount = 0; columnCount < 200; columnCount++) {
				sheet.setColumnWidth(columnCount, 7000);
			}

			CellStyle backgroundHdrStyle = workbook.createCellStyle();
			backgroundHdrStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
			backgroundHdrStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			backgroundHdrStyle.setBorderLeft(BorderStyle.THIN);
			backgroundHdrStyle.setBorderRight(BorderStyle.THIN);
			backgroundHdrStyle.setBorderBottom(BorderStyle.THIN);
			backgroundHdrStyle.setFont(font);

			XSSFRow header = sheet.createRow(0);
			header.createCell(0).setCellValue("Processing date");
			header.getCell(0).setCellStyle(backgroundHdrStyle);

			header.createCell(1).setCellValue("Seller Name");
			header.getCell(1).setCellStyle(backgroundHdrStyle);

			header.createCell(2).setCellValue("Seller Mobile No");
			header.getCell(2).setCellStyle(backgroundHdrStyle);

			header.createCell(3).setCellValue("Source Emp Code");
			header.getCell(3).setCellStyle(backgroundHdrStyle);

			header.createCell(4).setCellValue("RAC Location Mapping");
			header.getCell(4).setCellStyle(backgroundHdrStyle);

			header.createCell(5).setCellValue("MLI Sales Manager name");
			header.getCell(5).setCellStyle(backgroundHdrStyle);

			header.createCell(6).setCellValue("MLI SM-Code");
			header.getCell(6).setCellStyle(backgroundHdrStyle);

			header.createCell(7).setCellValue("MLI RM");
			header.getCell(7).setCellStyle(backgroundHdrStyle);

			header.createCell(8).setCellValue("MLI RM-Code");
			header.getCell(8).setCellStyle(backgroundHdrStyle);

			header.createCell(9).setCellValue("Client Name");
			header.getCell(9).setCellStyle(backgroundHdrStyle);

			header.createCell(10).setCellValue("Type of Loan");
			header.getCell(10).setCellStyle(backgroundHdrStyle);

			header.createCell(11).setCellValue("Scheme type");
			header.getCell(11).setCellStyle(backgroundHdrStyle);

			header.createCell(12).setCellValue("Loan Amount");
			header.getCell(12).setCellStyle(backgroundHdrStyle);

			header.createCell(13).setCellValue("Tenure of Loan");
			header.getCell(13).setCellStyle(backgroundHdrStyle);

			header.createCell(14).setCellValue("Interest Rate");
			header.getCell(14).setCellStyle(backgroundHdrStyle);

			header.createCell(15).setCellValue("Finance Premium");
			header.getCell(15).setCellStyle(backgroundHdrStyle);

			header.createCell(16).setCellValue("Tenure Eligible");
			header.getCell(16).setCellStyle(backgroundHdrStyle);

			header.createCell(17).setCellValue("Sum Assured");
			header.getCell(17).setCellStyle(backgroundHdrStyle);

			header.createCell(18).setCellValue("Premium Including GST");
			header.getCell(18).setCellStyle(backgroundHdrStyle);

			header.createCell(19).setCellValue("Tentative EMI");
			header.getCell(19).setCellStyle(backgroundHdrStyle);

			header.createCell(20).setCellValue("Incremental EMI");
			header.getCell(20).setCellStyle(backgroundHdrStyle);

			header.createCell(21).setCellValue("Loan Application ID/Bar Code No");
			header.getCell(21).setCellStyle(backgroundHdrStyle);

			header.createCell(22).setCellValue("Medical/Non-Medical");
			header.getCell(22).setCellStyle(backgroundHdrStyle);

			header.createCell(23).setCellValue("Financial Underwriting Required");
			header.getCell(23).setCellStyle(backgroundHdrStyle);

			header.createCell(24).setCellValue("Interested/ Not Interested");
			header.getCell(24).setCellStyle(backgroundHdrStyle);

			header.createCell(25).setCellValue("Not Interested Reason if not \ninterested is picked");
			header.getCell(25).setCellStyle(backgroundHdrStyle);

			header.createCell(26).setCellValue("CI Rider opted for");
			header.getCell(26).setCellStyle(backgroundHdrStyle);

			header.createCell(27).setCellValue("CI Rider as % sum assured of base");
			header.getCell(27).setCellStyle(backgroundHdrStyle);

			header.createCell(28).setCellValue("CI Rider Sum assured");
			header.getCell(28).setCellStyle(backgroundHdrStyle);

			header.createCell(29).setCellValue("CI Option: Gold/ Silver");
			header.getCell(29).setCellStyle(backgroundHdrStyle);

			header.createCell(30).setCellValue("CI Tenure in Years");
			header.getCell(30).setCellStyle(backgroundHdrStyle);

			header.createCell(31).setCellValue("Tentative EMI (Base + CI)");
			header.getCell(31).setCellStyle(backgroundHdrStyle);

			header.createCell(32).setCellValue("Incremental EMI (Base+CI) (Rs)");
			header.getCell(32).setCellStyle(backgroundHdrStyle);

			header.createCell(33).setCellValue("Total Premium (Base+CI)\n inclusive of GST @18%");
			header.getCell(33).setCellStyle(backgroundHdrStyle);

			header.createCell(34).setCellValue("Status of Application in Loan \nSecure App");
			header.getCell(34).setCellStyle(backgroundHdrStyle);

			header.createCell(35).setCellValue("Customer First Name");
			header.getCell(35).setCellStyle(backgroundHdrStyle);

			header.createCell(36).setCellValue("Customer Last Name");
			header.getCell(36).setCellStyle(backgroundHdrStyle);

			header.createCell(37).setCellValue("Customer Gender");
			header.getCell(37).setCellStyle(backgroundHdrStyle);

			header.createCell(38).setCellValue("Date of Birth");
			header.getCell(38).setCellStyle(backgroundHdrStyle);

			header.createCell(39).setCellValue("Relationship with Group Policy \nHolder");
			header.getCell(39).setCellStyle(backgroundHdrStyle);

			header.createCell(40).setCellValue("Customer Email ID");
			header.getCell(40).setCellStyle(backgroundHdrStyle);

			header.createCell(41).setCellValue("Customer Mobile Number");
			header.getCell(41).setCellStyle(backgroundHdrStyle);

			/*
			 * header.createCell(42).setCellValue("Customer Full Address");
			 * header.getCell(42).setCellStyle(backgroundHdrStyle);
			 */

			/* address related changes */

			header.createCell(42).setCellValue("Address1");
			header.getCell(42).setCellStyle(backgroundHdrStyle);

			header.createCell(43).setCellValue("Address2");
			header.getCell(43).setCellStyle(backgroundHdrStyle);

			header.createCell(44).setCellValue("Address3");
			header.getCell(44).setCellStyle(backgroundHdrStyle);

			header.createCell(45).setCellValue("Street");
			header.getCell(45).setCellStyle(backgroundHdrStyle);

			header.createCell(46).setCellValue("Town");
			header.getCell(46).setCellStyle(backgroundHdrStyle);

			header.createCell(47).setCellValue("State");
			header.getCell(47).setCellStyle(backgroundHdrStyle);

			header.createCell(48).setCellValue("Pin-Code");
			header.getCell(48).setCellStyle(backgroundHdrStyle);

			header.createCell(49).setCellValue("Country");
			header.getCell(49).setCellStyle(backgroundHdrStyle);

			/*
			 * header.createCell(32).setCellValue("Occupation");
			 * header.getCell(32).setCellStyle(backgroundHdrStyle);
			 */

			int cellNumberUptoOccupation = 49;
			String question = "";
			List<MasterReflexiveQuestionEntity> rqEntity = masterReflexiveQuestionDao.getAll();
			logger.info("Number of Reflexive Question : " + rqEntity.size());

			// plz don't remove this code
			/*
			 * for (MasterReflexiveQuestionEntity rq : rqEntity) { if
			 * (Constant.OCCUPATION.trim()
			 * .equalsIgnoreCase(rq.getSubTypeEntity().getTypeEntity().getTypeValue().trim()
			 * )) { cellNumberUptoOccupation = cellNumberUptoOccupation+1; question =
			 * getQuestionWithNewLineIfReq(new StringBuilder(rq.getQuestion()));
			 * header.createCell(cellNumberUptoOccupation).setCellValue(question);
			 * header.getCell(cellNumberUptoOccupation).setCellStyle(backgroundHdrStyle); }
			 * }
			 */
			header.createCell(++cellNumberUptoOccupation).setCellValue("Nationality");
			int nationalityCell = cellNumberUptoOccupation;
			header.getCell(cellNumberUptoOccupation).setCellStyle(backgroundHdrStyle);
			for (MasterReflexiveQuestionEntity rq : rqEntity) {
				if (Constant.NATIONALITY.trim()
						.equalsIgnoreCase(rq.getSubTypeEntity().getTypeEntity().getTypeValue().trim())) {
					cellNumberUptoOccupation = cellNumberUptoOccupation + 1;
					question = getQuestionWithNewLineIfReq(new StringBuilder(rq.getQuestion()));
					header.createCell(cellNumberUptoOccupation).setCellValue(question);
					header.getCell(cellNumberUptoOccupation).setCellStyle(backgroundHdrStyle);
				}
			}
			int healthStartRQCell = cellNumberUptoOccupation;
			header.createCell(++cellNumberUptoOccupation).setCellValue("Diabetes");
			int diabetesCellNumber = cellNumberUptoOccupation;
			header.getCell(cellNumberUptoOccupation).setCellStyle(backgroundHdrStyle);
			for (MasterReflexiveQuestionEntity rq : rqEntity) {
				if (Constant.DISEASE.trim()
						.equalsIgnoreCase(rq.getSubTypeEntity().getTypeEntity().getTypeValue().trim())
						&& Constant.DIABETES.trim().equalsIgnoreCase(rq.getSubTypeEntity().getSubTypeValue())) {
					cellNumberUptoOccupation = cellNumberUptoOccupation + 1;
					question = getQuestionWithNewLineIfReq(new StringBuilder(rq.getQuestion()));
					header.createCell(cellNumberUptoOccupation).setCellValue(question);
					header.getCell(cellNumberUptoOccupation).setCellStyle(backgroundHdrStyle);
				}
			}
			header.createCell(++cellNumberUptoOccupation).setCellValue("Hypertension");
			int hypertensionCellNumber = cellNumberUptoOccupation;
			header.getCell(cellNumberUptoOccupation).setCellStyle(backgroundHdrStyle);
			for (MasterReflexiveQuestionEntity rq : rqEntity) {
				if (Constant.DISEASE.trim()
						.equalsIgnoreCase(rq.getSubTypeEntity().getTypeEntity().getTypeValue().trim())
						&& Constant.HYPERTENSION.trim().equalsIgnoreCase(rq.getSubTypeEntity().getSubTypeValue())) {
					cellNumberUptoOccupation = cellNumberUptoOccupation + 1;
					question = getQuestionWithNewLineIfReq(new StringBuilder(rq.getQuestion()));
					header.createCell(cellNumberUptoOccupation).setCellValue(question);
					header.getCell(cellNumberUptoOccupation).setCellStyle(backgroundHdrStyle);
				}
			}
			header.createCell(++cellNumberUptoOccupation).setCellValue("Asthma");
			int asthmaCellNumber = cellNumberUptoOccupation;
			header.getCell(cellNumberUptoOccupation).setCellStyle(backgroundHdrStyle);
			for (MasterReflexiveQuestionEntity rq : rqEntity) {
				if (Constant.DISEASE.trim()
						.equalsIgnoreCase(rq.getSubTypeEntity().getTypeEntity().getTypeValue().trim())
						&& Constant.ASTHMA.trim().equalsIgnoreCase(rq.getSubTypeEntity().getSubTypeValue())) {
					cellNumberUptoOccupation = cellNumberUptoOccupation + 1;
					question = getQuestionWithNewLineIfReq(new StringBuilder(rq.getQuestion()));
					header.createCell(cellNumberUptoOccupation).setCellValue(question);
					header.getCell(cellNumberUptoOccupation).setCellStyle(backgroundHdrStyle);
				}
			}

			int lastIndexOfRQ = cellNumberUptoOccupation;
			header.createCell(++cellNumberUptoOccupation).setCellValue("Other Health Related Confirmation");
			header.getCell(cellNumberUptoOccupation).setCellStyle(backgroundHdrStyle);
			header.createCell(++cellNumberUptoOccupation).setCellValue("UW Medical Trigger");
			header.getCell(cellNumberUptoOccupation).setCellStyle(backgroundHdrStyle);

			/*Covid19 question cell*/
			int covidIndexStart = cellNumberUptoOccupation;
			header.createCell(++cellNumberUptoOccupation).setCellValue(Covid19QuestionConstant.QUESTION1);
			header.getCell(cellNumberUptoOccupation).setCellStyle(backgroundHdrStyle);
			header.createCell(++cellNumberUptoOccupation).setCellValue(Covid19QuestionConstant.QUESTION2);
			header.getCell(cellNumberUptoOccupation).setCellStyle(backgroundHdrStyle);
			header.createCell(++cellNumberUptoOccupation).setCellValue(Covid19QuestionConstant.QUESTION3A);
			header.getCell(cellNumberUptoOccupation).setCellStyle(backgroundHdrStyle);
			header.createCell(++cellNumberUptoOccupation).setCellValue(Covid19QuestionConstant.QUESTION3B);
			header.getCell(cellNumberUptoOccupation).setCellStyle(backgroundHdrStyle);			
			header.createCell(++cellNumberUptoOccupation).setCellValue(Covid19QuestionConstant.QUESTION4);
			header.getCell(cellNumberUptoOccupation).setCellStyle(backgroundHdrStyle);
			header.createCell(++cellNumberUptoOccupation).setCellValue(Covid19QuestionConstant.QUESTION5);
			header.getCell(cellNumberUptoOccupation).setCellStyle(backgroundHdrStyle);
			
			/*Ci rider questions*/

			int ciRiderIndexStart = cellNumberUptoOccupation;
			header.createCell(++cellNumberUptoOccupation).setCellValue(CIRiderQuesConstant.QUESTION1);
			header.getCell(cellNumberUptoOccupation).setCellStyle(backgroundHdrStyle);
			header.createCell(++cellNumberUptoOccupation).setCellValue(CIRiderQuesConstant.QUESTION2);
			header.getCell(cellNumberUptoOccupation).setCellStyle(backgroundHdrStyle);
			header.createCell(++cellNumberUptoOccupation).setCellValue(CIRiderQuesConstant.QUESTION3);
			header.getCell(cellNumberUptoOccupation).setCellStyle(backgroundHdrStyle);
			header.createCell(++cellNumberUptoOccupation).setCellValue(CIRiderQuesConstant.QUESTION4);
			header.getCell(cellNumberUptoOccupation).setCellStyle(backgroundHdrStyle);
			header.createCell(++cellNumberUptoOccupation).setCellValue(CIRiderQuesConstant.QUESTION5);
			header.getCell(cellNumberUptoOccupation).setCellStyle(backgroundHdrStyle);
			header.createCell(++cellNumberUptoOccupation).setCellValue(CIRiderQuesConstant.QUESTION6);
			header.getCell(cellNumberUptoOccupation).setCellStyle(backgroundHdrStyle);
			header.createCell(++cellNumberUptoOccupation).setCellValue(CIRiderQuesConstant.QUESTION7);
			header.getCell(cellNumberUptoOccupation).setCellStyle(backgroundHdrStyle);

			int nomineeStartCell = cellNumberUptoOccupation;

			header.createCell(++cellNumberUptoOccupation).setCellValue("Name of Nominee: First Name ");
			header.getCell(cellNumberUptoOccupation).setCellStyle(backgroundHdrStyle);
			header.createCell(++cellNumberUptoOccupation).setCellValue("Name of Nominee: Last Name");
			header.getCell(cellNumberUptoOccupation).setCellStyle(backgroundHdrStyle);
			header.createCell(++cellNumberUptoOccupation).setCellValue("Nominee Date of Birth");
			header.getCell(cellNumberUptoOccupation).setCellStyle(backgroundHdrStyle);
			header.createCell(++cellNumberUptoOccupation).setCellValue("Gender");
			header.getCell(cellNumberUptoOccupation).setCellStyle(backgroundHdrStyle);
			header.createCell(++cellNumberUptoOccupation).setCellValue("Relationship with the Assured");
			header.getCell(cellNumberUptoOccupation).setCellStyle(backgroundHdrStyle);
			header.createCell(++cellNumberUptoOccupation).setCellValue("Name of Appointee: First Name ");
			header.getCell(cellNumberUptoOccupation).setCellStyle(backgroundHdrStyle);
			header.createCell(++cellNumberUptoOccupation).setCellValue("Name of Appointee  Last Name");
			header.getCell(cellNumberUptoOccupation).setCellStyle(backgroundHdrStyle);
			header.createCell(++cellNumberUptoOccupation).setCellValue("Appointee Date of Birth");
			header.getCell(cellNumberUptoOccupation).setCellStyle(backgroundHdrStyle);
			header.createCell(++cellNumberUptoOccupation).setCellValue("Gender");
			header.getCell(cellNumberUptoOccupation).setCellStyle(backgroundHdrStyle);
			header.createCell(++cellNumberUptoOccupation).setCellValue("Relationship with the Nominee");
			header.getCell(cellNumberUptoOccupation).setCellStyle(backgroundHdrStyle);

			int proposerStartCell = cellNumberUptoOccupation;
			header.createCell(++cellNumberUptoOccupation).setCellValue("Name of Proposer: First Name");
			header.getCell(cellNumberUptoOccupation).setCellStyle(backgroundHdrStyle);
			header.createCell(++cellNumberUptoOccupation).setCellValue("Name of Proposer: Last Name");
			header.getCell(cellNumberUptoOccupation).setCellStyle(backgroundHdrStyle);
			header.createCell(++cellNumberUptoOccupation).setCellValue("Proposer Date of Birth");
			header.getCell(cellNumberUptoOccupation).setCellStyle(backgroundHdrStyle);
			header.createCell(++cellNumberUptoOccupation).setCellValue("Gender");
			header.getCell(cellNumberUptoOccupation).setCellStyle(backgroundHdrStyle);
			header.createCell(++cellNumberUptoOccupation).setCellValue("Relationship with the Life Assured");
			header.getCell(cellNumberUptoOccupation).setCellStyle(backgroundHdrStyle);
			header.createCell(++cellNumberUptoOccupation).setCellValue("Health Declaration Form");
			header.getCell(cellNumberUptoOccupation).setCellStyle(backgroundHdrStyle);
			header.createCell(++cellNumberUptoOccupation).setCellValue(
					"Do you have any life insurance \npolicy issued/pending/lapsed with \nMax Life Insurance Company Ltd? \nIf yes, please provide the \nproposal/policy number/application no.-");
			header.getCell(cellNumberUptoOccupation).setCellStyle(backgroundHdrStyle);
			header.createCell(++cellNumberUptoOccupation).setCellValue("Agreement of T&C");
			header.getCell(cellNumberUptoOccupation).setCellStyle(backgroundHdrStyle);
			int medatoryTNCCell = cellNumberUptoOccupation;
			header.createCell(++cellNumberUptoOccupation).setCellValue("Place/Location where T&C is agreed to");
			header.getCell(cellNumberUptoOccupation).setCellStyle(backgroundHdrStyle);
			header.createCell(++cellNumberUptoOccupation).setCellValue("Date of Submission of Forms");
			header.getCell(cellNumberUptoOccupation).setCellStyle(backgroundHdrStyle);

			int startCellOfVerificationData = cellNumberUptoOccupation;
			header.createCell(++cellNumberUptoOccupation)
					.setCellValue("Filled PDF form Received Date \nby Customer (SMS)");
			header.getCell(cellNumberUptoOccupation).setCellStyle(backgroundHdrStyle);
			header.createCell(++cellNumberUptoOccupation)
					.setCellValue("Filled PDF form Received Date \nby Customer (email)");
			header.getCell(cellNumberUptoOccupation).setCellStyle(backgroundHdrStyle);
			header.createCell(++cellNumberUptoOccupation).setCellValue("OTP No");
			header.getCell(cellNumberUptoOccupation).setCellStyle(backgroundHdrStyle);
			header.createCell(++cellNumberUptoOccupation)
					.setCellValue("Autorization through OTP sent date \nto Customer");
			header.getCell(cellNumberUptoOccupation).setCellStyle(backgroundHdrStyle);
			header.createCell(++cellNumberUptoOccupation)
					.setCellValue("e-Form Processing/ Verification \ndate by Customer");
			header.getCell(cellNumberUptoOccupation).setCellStyle(backgroundHdrStyle);
			header.createCell(++cellNumberUptoOccupation).setCellValue("SMS confirmation to seller & Customer");
			header.getCell(cellNumberUptoOccupation).setCellStyle(backgroundHdrStyle);
			header.createCell(++cellNumberUptoOccupation).setCellValue("Proposal Number");
			header.getCell(cellNumberUptoOccupation).setCellStyle(backgroundHdrStyle);
			header.createCell(++cellNumberUptoOccupation).setCellValue("RO SSO ID");
			header.getCell(cellNumberUptoOccupation).setCellStyle(backgroundHdrStyle);
			header.createCell(++cellNumberUptoOccupation).setCellValue("SM SSO ID");
			header.getCell(cellNumberUptoOccupation).setCellStyle(backgroundHdrStyle);
			header.createCell(++cellNumberUptoOccupation).setCellValue("Health Type");
			header.getCell(cellNumberUptoOccupation).setCellStyle(backgroundHdrStyle);
			header.createCell(++cellNumberUptoOccupation).setCellValue("Height (In CMs)");
			header.getCell(cellNumberUptoOccupation).setCellStyle(backgroundHdrStyle);
			header.createCell(++cellNumberUptoOccupation).setCellValue("Weight (in KGs)");
			header.getCell(cellNumberUptoOccupation).setCellStyle(backgroundHdrStyle);
			header.createCell(++cellNumberUptoOccupation).setCellValue(SFQHealthQuestionConstant.HEALTH_FIRST_QUESTION);
			header.getCell(cellNumberUptoOccupation).setCellStyle(backgroundHdrStyle);
			header.createCell(++cellNumberUptoOccupation).setCellValue(SFQHealthQuestionConstant.HEALTH_SECOND_QUESTION);
			header.getCell(cellNumberUptoOccupation).setCellStyle(backgroundHdrStyle);
			header.createCell(++cellNumberUptoOccupation).setCellValue(SFQHealthQuestionConstant.HEALTH_THIRD_QUESTION);
			header.getCell(cellNumberUptoOccupation).setCellStyle(backgroundHdrStyle);
			header.createCell(++cellNumberUptoOccupation).setCellValue(SFQHealthQuestionConstant.HEALTH_FOURTH_QUESTION);
			header.getCell(cellNumberUptoOccupation).setCellStyle(backgroundHdrStyle);
			header.createCell(++cellNumberUptoOccupation).setCellValue(SFQHealthQuestionConstant.HEALTH_FIFTH_QUESTION);
			header.getCell(cellNumberUptoOccupation).setCellStyle(backgroundHdrStyle);
			header.createCell(++cellNumberUptoOccupation).setCellValue(SFQHealthQuestionConstant.HEALTH_SIXTH_QUESTION);
			header.getCell(cellNumberUptoOccupation).setCellStyle(backgroundHdrStyle);
			
			
			header.createCell(++cellNumberUptoOccupation).setCellValue(SFQHealthQuestionConstant.COVID_FIRST_QUESTION);
			header.getCell(cellNumberUptoOccupation).setCellStyle(backgroundHdrStyle);
			header.createCell(++cellNumberUptoOccupation).setCellValue(SFQHealthQuestionConstant.COVID_SECOND_QUESTION);
			header.getCell(cellNumberUptoOccupation).setCellStyle(backgroundHdrStyle);
			
			header.createCell(++cellNumberUptoOccupation).setCellValue(SFQHealthQuestionConstant.COVID_TEST_FIRST_QUESTION);
			header.getCell(cellNumberUptoOccupation).setCellStyle(backgroundHdrStyle);
			header.createCell(++cellNumberUptoOccupation).setCellValue(SFQHealthQuestionConstant.COVID_TEST_SECOND_QUESTION);
			header.getCell(cellNumberUptoOccupation).setCellStyle(backgroundHdrStyle);
			header.createCell(++cellNumberUptoOccupation).setCellValue(SFQHealthQuestionConstant.COVID_TEST_THIRD_QUESTION);
			header.getCell(cellNumberUptoOccupation).setCellStyle(backgroundHdrStyle);
			header.createCell(++cellNumberUptoOccupation).setCellValue(SFQHealthQuestionConstant.COVID_TEST_FOURTH_QUESTION);
			header.getCell(cellNumberUptoOccupation).setCellStyle(backgroundHdrStyle);
			
			
			header.createCell(++cellNumberUptoOccupation).setCellValue(SFQHealthQuestionConstant.COVID_THIRD_QUESTION);
			header.getCell(cellNumberUptoOccupation).setCellStyle(backgroundHdrStyle);
			header.createCell(++cellNumberUptoOccupation).setCellValue(SFQHealthQuestionConstant.COVID_THIRD_QUESTION_A);
			header.getCell(cellNumberUptoOccupation).setCellStyle(backgroundHdrStyle);
			header.createCell(++cellNumberUptoOccupation).setCellValue(SFQHealthQuestionConstant.COVID_THIRD_QUESTION_B );
			header.getCell(cellNumberUptoOccupation).setCellStyle(backgroundHdrStyle);
			
			header.createCell(++cellNumberUptoOccupation).setCellValue(SFQHealthQuestionConstant.COVID_VACCINE_QUESTION);
			header.getCell(cellNumberUptoOccupation).setCellStyle(backgroundHdrStyle);
			header.createCell(++cellNumberUptoOccupation).setCellValue(SFQHealthQuestionConstant.COVID_VACCINE_FIRST_QUESTION);
			header.getCell(cellNumberUptoOccupation).setCellStyle(backgroundHdrStyle);
			header.createCell(++cellNumberUptoOccupation).setCellValue(SFQHealthQuestionConstant.COVID_VACCINE_SECOND_QUESTION);
			header.getCell(cellNumberUptoOccupation).setCellStyle(backgroundHdrStyle);
			header.createCell(++cellNumberUptoOccupation).setCellValue(SFQHealthQuestionConstant.COVID_VACCINE_THIRD_QUESTION);
			header.getCell(cellNumberUptoOccupation).setCellStyle(backgroundHdrStyle);
			header.createCell(++cellNumberUptoOccupation).setCellValue(SFQHealthQuestionConstant.COVID_VACCINE_FOURTH_QUESTION);
			header.getCell(cellNumberUptoOccupation).setCellStyle(backgroundHdrStyle);
			
			
			header.createCell(++cellNumberUptoOccupation).setCellValue("Is Smoker");
			header.getCell(cellNumberUptoOccupation).setCellStyle(backgroundHdrStyle);
			header.createCell(++cellNumberUptoOccupation).setCellValue("SmokePerDay");
			header.getCell(cellNumberUptoOccupation).setCellStyle(backgroundHdrStyle);

			logger.info(
					"Today date : " + new Date() + ", Total number of column in excel: " + cellNumberUptoOccupation);

			int rowCount = 1;
			for (UserDetailsModel userDetailsModel : userDetailModels) {
				XSSFRow userRow = sheet.createRow(rowCount++);
				CustomerDetailsModel customerDetailsModel = userDetailsModel.getCustomerDetails();
				NomineeDetailsModel nomineeDetails = userDetailsModel.getNomineeDetails();
				MandatoryDeclarationModel mandatoryDeclaration = userDetailsModel.getMandatoryDeclaration();
				HealthDeclarationModel healthDeclaration = userDetailsModel.getHealthDeclaration();

				userRow.createCell(0)
						.setCellValue(customerDetailsModel.getCreatedOn() != null
								? DateUtil.extractDateAsStringSlashFormate(customerDetailsModel.getCreatedOn())
								: "");

				userRow.createCell(1).setCellValue(customerDetailsModel.getSelletName());
				userRow.createCell(2).setCellValue(customerDetailsModel.getSellerMobileNumber());
				userRow.createCell(3).setCellValue(customerDetailsModel.getSourceEmpCode());// Source Emp Code
				userRow.createCell(4).setCellValue(customerDetailsModel.getRacLocationMapping());// RAC Location Mapping
				userRow.createCell(5).setCellValue(customerDetailsModel.getMliSalesManager());// MLI Sales Manager name

				userRow.createCell(6)
						.setCellValue(userDetailsModel.getMliSMCode() != null ? userDetailsModel.getMliSMCode() : "NA");
				userRow.createCell(7).setCellValue(customerDetailsModel.getMliRM());// MLI RM
				userRow.createCell(8)
						.setCellValue(userDetailsModel.getMliRMCode() != null ? userDetailsModel.getMliRMCode() : "NA");
                if(customerDetailsModel.getMasterPolicyholderName() != null) {
				   userRow.createCell(9).setCellValue(customerDetailsModel.getMasterPolicyholderName());
                } else if(Constant.AFL_PHYSICAL_FORM_VERIFICATION.equals(customerDetailsModel.getStatus())) {
                   userRow.createCell(9).setCellValue("Axis Finance Physical Form");
                } else if(Constant.PHYSICAL_FORM_VERIFICATION.equals(customerDetailsModel.getStatus())) {
                    userRow.createCell(9).setCellValue("Axis Bank Physical Form");
                }
				userRow.createCell(10).setCellValue(customerDetailsModel.getLoanType());

				userRow.createCell(11).setCellValue(customerDetailsModel.getSchemeType());
				userRow.createCell(12).setCellValue(
						customerDetailsModel.getLoanAmount() == null ? 0.0 : customerDetailsModel.getLoanAmount());
				userRow.createCell(13).setCellValue(
						customerDetailsModel.getLoanTenure() == null ? 0.0 : customerDetailsModel.getLoanTenure());
				userRow.createCell(14).setCellValue(customerDetailsModel.getInterestRate());
				userRow.createCell(15).setCellValue(customerDetailsModel.getFinancePremium());
				userRow.createCell(16).setCellValue(customerDetailsModel.getTenureEligible());
				userRow.createCell(17).setCellValue(
						customerDetailsModel.getSumAssured() == null ? 0.0 : customerDetailsModel.getSumAssured());
				userRow.createCell(18).setCellValue(customerDetailsModel.getTotalPremium());

				userRow.createCell(19).setCellValue(customerDetailsModel.getTentativeEMI());
				userRow.createCell(20).setCellValue(customerDetailsModel.getIncrementalEMI());
				userRow.createCell(21).setCellValue(customerDetailsModel.getLoanAppNumber());
				userRow.createCell(22).setCellValue(customerDetailsModel.getMedicalRequired());
				userRow.createCell(23).setCellValue(customerDetailsModel.getFinancialUWRequired());
				userRow.createCell(24).setCellValue(customerDetailsModel.getInterested());
				userRow.createCell(25).setCellValue(customerDetailsModel.getReasonForNotInterested());

				if (customerDetailsModel.getBaseOrBaseWithCI() != null) {
					userRow.createCell(26)
							.setCellValue(customerDetailsModel.getBaseOrBaseWithCI() != null
									? customerDetailsModel.getBaseOrBaseWithCI()
									: "NA");
					userRow.createCell(27)
							.setCellValue(customerDetailsModel.getPercentageOfSumAssured() != null
									? customerDetailsModel.getPercentageOfSumAssured()
									: 0);
					userRow.createCell(28)
							.setCellValue(customerDetailsModel.getCiRiderSumAssured() != null
									? customerDetailsModel.getCiRiderSumAssured()
									: 0.0);
					userRow.createCell(29).setCellValue(
							customerDetailsModel.getCiOption() != null ? customerDetailsModel.getCiOption() : "NA");
					userRow.createCell(30)
							.setCellValue(customerDetailsModel.getCiTenureYears() != null
									? customerDetailsModel.getCiTenureYears()
									: 0);
					userRow.createCell(31)
							.setCellValue(customerDetailsModel.getTentativeEMIBaseWithCI() != null
									? customerDetailsModel.getTentativeEMIBaseWithCI()
									: 0);
					userRow.createCell(32)
							.setCellValue(customerDetailsModel.getIncrementalEMIBaseWithCI() != null
									? customerDetailsModel.getIncrementalEMIBaseWithCI()
									: 0);
					userRow.createCell(33)
							.setCellValue(customerDetailsModel.getBaseWithCIPremium() != null
									? customerDetailsModel.getBaseWithCIPremium()
									: 0.0);
				}

				userRow.createCell(34).setCellValue(customerDetailsModel.getStatus());

				userRow.createCell(35).setCellValue(customerDetailsModel.getCustomerFirstName());
				userRow.createCell(36).setCellValue(customerDetailsModel.getCustomerLastName());
				userRow.createCell(37).setCellValue(customerDetailsModel.getGender());
				userRow.createCell(38).setCellValue(customerDetailsModel.getDob());
				userRow.createCell(39).setCellValue(customerDetailsModel.getRelationshipGpPolicyHolder());
				userRow.createCell(40).setCellValue(customerDetailsModel.getCustEmailId());
				userRow.createCell(41).setCellValue(
						customerDetailsModel.getCustMobileNo() == null ? 0l : customerDetailsModel.getCustMobileNo());

				userRow.createCell(42).setCellValue(customerDetailsModel.getAddress1());
				userRow.createCell(43).setCellValue(customerDetailsModel.getAddress2());
				userRow.createCell(44).setCellValue(customerDetailsModel.getAddress3());
				userRow.createCell(45).setCellValue(customerDetailsModel.getStreet());
				userRow.createCell(46).setCellValue(customerDetailsModel.getTown());
				userRow.createCell(47).setCellValue(customerDetailsModel.getState());
				userRow.createCell(48).setCellValue(customerDetailsModel.getPinCode());
				userRow.createCell(49).setCellValue(customerDetailsModel.getCountry());

//				userRow.createCell(32).setCellValue(customerDetailsModel.getOccupation());

				int cellIndex = 49;
				// plz don't remove this code
				/*
				 * for (int startRQIndex = cellIndex; startRQIndex <= lastIndexOfRQ;
				 * startRQIndex++) { setAnswer(header, startRQIndex, userRow,
				 * customerDetailsModel, Constant.OCCUPATION); }
				 */
				userRow.createCell(nationalityCell).setCellValue(customerDetailsModel.getNationality());
				int otherPlace = nationalityCell + 1;
				userRow.createCell(otherPlace).setCellValue(customerDetailsModel.getOtherPlace());
				for (int startRQIndex = nationalityCell; startRQIndex <= lastIndexOfRQ; startRQIndex++) {
					setAnswer(header, startRQIndex, userRow, customerDetailsModel, Constant.NATIONALITY);
				}
				for (int startRQIndex = healthStartRQCell; startRQIndex <= lastIndexOfRQ; startRQIndex++) {
					setAnswer(header, startRQIndex, userRow, customerDetailsModel, Constant.DISEASE);
				}

				// for Diabetes,Hypertenstion,Asthma : Yes/No
				try {
					String diabetesExist = userRow.getCell(++diabetesCellNumber).getStringCellValue();
					if (diabetesExist != null && !diabetesExist.isEmpty()) {
						userRow.createCell(--diabetesCellNumber).setCellValue("Yes");
					} else {
						userRow.createCell(--diabetesCellNumber).setCellValue("No");
					}
				} catch (Exception exception) {
					userRow.createCell(--diabetesCellNumber).setCellValue("No");
//					logger.error("Exception occured while fetching diabetes : "+exception.getMessage()); 
				}
				try {
					String hypertensionExist = userRow.getCell(++hypertensionCellNumber).getStringCellValue();
					if (hypertensionExist != null && !hypertensionExist.isEmpty()) {
						userRow.createCell(--hypertensionCellNumber).setCellValue("Yes");
					} else {
						userRow.createCell(--hypertensionCellNumber).setCellValue("No");
					}
				} catch (Exception exception) {
					userRow.createCell(--hypertensionCellNumber).setCellValue("No");
//					logger.error("Exception occured while fetching Hypertension : "+exception.getMessage()); 					
				}
				try {
					String asthmaExist = userRow.getCell(++asthmaCellNumber).getStringCellValue();
					if (asthmaExist != null && !asthmaExist.isEmpty()) {
						userRow.createCell(--asthmaCellNumber).setCellValue("Yes");
					} else {
						userRow.createCell(--asthmaCellNumber).setCellValue("No");
					}
				} catch (Exception exception) {
					userRow.createCell(--asthmaCellNumber).setCellValue("No");
//					logger.error("Exception occured while fetching Asthma : "+exception.getMessage()); 
				}
				
				cellIndex = covidIndexStart;
				if (healthDeclaration != null && !ObjectUtils.isEmpty(healthDeclaration.getCovid_19Details())) {
					Covid_19Model  covid_19Model = healthDeclaration.getCovid_19Details();
					if(!ObjectUtils.isEmpty(covid_19Model)) {
						userRow.createCell(++cellIndex).setCellValue(covid_19Model.getCovidAnsOne() == null ? "No" : covid_19Model.getCovidAnsOne());
						userRow.createCell(++cellIndex).setCellValue(covid_19Model.getCovidAnsTwo() == null ? "No" : covid_19Model.getCovidAnsTwo());
						userRow.createCell(++cellIndex).setCellValue(covid_19Model.getCovidAnsThree_a() == null ? "No" : covid_19Model.getCovidAnsThree_a());
						userRow.createCell(++cellIndex).setCellValue(covid_19Model.getCovidAnsThree_b() == null ? "No" : covid_19Model.getCovidAnsThree_b());
						userRow.createCell(++cellIndex).setCellValue(covid_19Model.getCovidAnsFour() == null ? "Yes" : covid_19Model.getCovidAnsFour());
						userRow.createCell(++cellIndex).setCellValue(covid_19Model.getCovidAnsFive() == null ? "No" : covid_19Model.getCovidAnsFive());
					}
				}else {
					userRow.createCell(++cellIndex).setCellValue("");
					userRow.createCell(++cellIndex).setCellValue("");
					userRow.createCell(++cellIndex).setCellValue("");
					userRow.createCell(++cellIndex).setCellValue("");
					userRow.createCell(++cellIndex).setCellValue("");
					userRow.createCell(++cellIndex).setCellValue("");
				}
				
				cellIndex= ciRiderIndexStart;
				if (healthDeclaration != null && !ObjectUtils.isEmpty(healthDeclaration.getCiRiderQuestionAns())) {
					List<HealthAnswerModel> ansModel = healthDeclaration.getCiRiderQuestionAns();
					Collections.sort(ansModel, HealthAnswerModel.healthAnswere);
					if (ansModel.size() == 7) {
						for (HealthAnswerModel healthAnswerModel : ansModel) {
							System.out.println("cell index =======" + cellIndex + "=====cell Value======="
									+ healthAnswerModel.getQuestionAns() + "====other desc answere======"
									+ healthDeclaration.getCiHealthDecsAns());
							switch (healthAnswerModel.getQuestionId()) {
							case 1:
								userRow.createCell(++cellIndex).setCellValue(healthAnswerModel.getQuestionAns());
								break;
							case 2:
								userRow.createCell(++cellIndex).setCellValue(healthAnswerModel.getQuestionAns());
								break;
							case 3:
								userRow.createCell(++cellIndex).setCellValue(healthAnswerModel.getQuestionAns());
								break;
							case 4:
								userRow.createCell(++cellIndex).setCellValue(healthAnswerModel.getQuestionAns());
								break;
							case 5:
								userRow.createCell(++cellIndex).setCellValue(healthAnswerModel.getQuestionAns());
								break;
							case 6:
								userRow.createCell(++cellIndex).setCellValue(healthAnswerModel.getQuestionAns());
								break;
							case 7:
								if (healthAnswerModel.getQuestionAns().equals("Yes")) {
									userRow.createCell(++cellIndex).setCellValue(healthAnswerModel.getQuestionAns()
											+ ":" + healthAnswerModel.getCiRiderDsc());
								} else {
									userRow.createCell(++cellIndex).setCellValue(healthAnswerModel.getQuestionAns());
								}
								break;
							default:
								break;
							}
						}

//						userRow.createCell(++cellIndex).setCellValue(ansModel.get(1).getIsQuestionAns());
//						userRow.createCell(++cellIndex).setCellValue(ansModel.get(2).getIsQuestionAns());
//						userRow.createCell(++cellIndex).setCellValue(ansModel.get(3).getIsQuestionAns());
//						userRow.createCell(++cellIndex).setCellValue(ansModel.get(4).getIsQuestionAns());
//						userRow.createCell(++cellIndex).setCellValue(ansModel.get(5).getIsQuestionAns());
//						userRow.createCell(++cellIndex).setCellValue(ansModel.get(6).getIsQuestionAns());
					}

				} else {
					userRow.createCell(++cellIndex).setCellValue("");
					userRow.createCell(++cellIndex).setCellValue("");
					userRow.createCell(++cellIndex).setCellValue("");
					userRow.createCell(++cellIndex).setCellValue("");
					userRow.createCell(++cellIndex).setCellValue("");
					userRow.createCell(++cellIndex).setCellValue("");
					userRow.createCell(++cellIndex).setCellValue("");
				}

				cellIndex = lastIndexOfRQ;
				if (healthDeclaration != null) {
					userRow.createCell(++cellIndex)
							.setCellValue(healthDeclaration.getOtherHealthInfo() != null
									? healthDeclaration.getOtherHealthInfo()
									: "");
					userRow.createCell(++cellIndex).setCellValue(
							healthDeclaration.getTriggerMsg() != null ? healthDeclaration.getTriggerMsg() : "");
				} else {
					userRow.createCell(++cellIndex).setCellValue("");
					userRow.createCell(++cellIndex).setCellValue("");
				}
				cellIndex = nomineeStartCell;
				if (nomineeDetails != null) {
					userRow.createCell(++cellIndex).setCellValue(nomineeDetails.getNomineeFirstName());
					userRow.createCell(++cellIndex).setCellValue(nomineeDetails.getNomineeLastName());

					userRow.createCell(++cellIndex).setCellValue(nomineeDetails.getDateOfBirth());
					userRow.createCell(++cellIndex).setCellValue(nomineeDetails.getGender());
					if (nomineeDetails.getRelationWitHAssured()
							.equalsIgnoreCase(RelationshipWithAssured.OTHERS.getLabel())) {
						userRow.createCell(++cellIndex).setCellValue(nomineeDetails.getRelationWithNominee());
					} else {
						userRow.createCell(++cellIndex).setCellValue(nomineeDetails.getRelationWitHAssured());
					}
					if (nomineeDetails.getAppointeeDetails() != null) {
						userRow.createCell(++cellIndex)
								.setCellValue(nomineeDetails.getAppointeeDetails().getAppointeeFirstName());
						userRow.createCell(++cellIndex)
								.setCellValue(nomineeDetails.getAppointeeDetails().getAppointeeLastName());
						userRow.createCell(++cellIndex)
								.setCellValue(nomineeDetails.getAppointeeDetails().getDateOfBirth());
						userRow.createCell(++cellIndex).setCellValue(nomineeDetails.getAppointeeDetails().getGender());
						if (nomineeDetails.getAppointeeDetails().getRelationWithAssured()
								.equalsIgnoreCase(RelationshipWithAssured.OTHERS.getLabel())) {
							userRow.createCell(++cellIndex)
									.setCellValue(nomineeDetails.getAppointeeDetails().getRelationWithAppointee());
						} else {
							userRow.createCell(++cellIndex)
									.setCellValue(nomineeDetails.getAppointeeDetails().getRelationWithAssured());
						}
					} else {
						userRow.createCell(++cellIndex).setCellValue("");
						userRow.createCell(++cellIndex).setCellValue("");
						userRow.createCell(++cellIndex).setCellValue("");
						userRow.createCell(++cellIndex).setCellValue("");
						userRow.createCell(++cellIndex).setCellValue("");
					}
				}
				cellIndex = proposerStartCell;
				if (customerDetailsModel.getProposerDetails() != null) {
					userRow.createCell(++cellIndex)
							.setCellValue(customerDetailsModel.getProposerDetails().getProposerFirstName());
					userRow.createCell(++cellIndex)
							.setCellValue(customerDetailsModel.getProposerDetails().getProposerLastName());
					userRow.createCell(++cellIndex).setCellValue(customerDetailsModel.getDob());
					userRow.createCell(++cellIndex).setCellValue(customerDetailsModel.getGender());
					if (customerDetailsModel.getProposerDetails().getRelationWithAssured()
							.equalsIgnoreCase(RelationshipWithAssured.OTHERS.getLabel())) {
						userRow.createCell(++cellIndex)
								.setCellValue(customerDetailsModel.getProposerDetails().getRelationWithProposer());
					} else {
						userRow.createCell(++cellIndex)
								.setCellValue(customerDetailsModel.getProposerDetails().getRelationWithAssured());
					}
				} else {
					userRow.createCell(++cellIndex).setCellValue("");
					userRow.createCell(++cellIndex).setCellValue("");
					userRow.createCell(++cellIndex).setCellValue("");
					userRow.createCell(++cellIndex).setCellValue("");
					userRow.createCell(++cellIndex).setCellValue("");
				}
				if (healthDeclaration != null) {
					if (!healthDeclaration.getIsHealthDeclaration()) {
						userRow.createCell(++cellIndex).setCellValue(AGREE);
					} else {
						userRow.createCell(++cellIndex).setCellValue(DISAGREE);
					}
					userRow.createCell(++cellIndex).setCellValue(healthDeclaration.getApplicationNumber());
					userRow.createCell(medatoryTNCCell).setCellValue(healthDeclaration.getNegativeDeclaration());
				} else {
					userRow.createCell(++cellIndex).setCellValue("");
					userRow.createCell(++cellIndex).setCellValue("");
					userRow.createCell(++cellIndex).setCellValue("");
				}
				if (mandatoryDeclaration != null) {
					if (mandatoryDeclaration.getIsMandatoryDeclaration() != null
							&& mandatoryDeclaration.getIsMandatoryDeclaration()) {
						userRow.createCell(++cellIndex).setCellValue(AGREE);
					} else {
						userRow.createCell(++cellIndex).setCellValue(DISAGREE);
					}
					userRow.createCell(++cellIndex).setCellValue(mandatoryDeclaration.getPlace());
					if (mandatoryDeclaration.getSignedDate() != null) {
						userRow.createCell(++cellIndex)
								.setCellValue(DateUtil.extractDateAsString(mandatoryDeclaration.getSignedDate()));
					} else {
						userRow.createCell(++cellIndex).setCellValue("");
					}
				} else {
					userRow.createCell(++cellIndex).setCellValue("");
					userRow.createCell(++cellIndex).setCellValue("");
					userRow.createCell(++cellIndex).setCellValue("");
				}
				cellIndex = startCellOfVerificationData;
				userRow.createCell(++cellIndex)
						.setCellValue(userDetailsModel.getSfvTimeStamp() != null
								? DateUtil.extractDateAsStringSlashFormate(userDetailsModel.getSfvTimeStamp())
								: "");
				userRow.createCell(++cellIndex)
						.setCellValue(userDetailsModel.getSfvTimeStamp() != null
								? DateUtil.extractDateAsStringSlashFormate(userDetailsModel.getSfvTimeStamp())
								: "");
				/*
				 * List<OTPHistoryEntity> otpDetails =
				 * otpDDAO.findByContNoAndStatus(customerDetailsModel.getCustMobileNo(),Status.
				 * USED_OTP); OTPHistoryEntity otpDetail = new OTPHistoryEntity(); // Get last
				 * object of otpDetails which is used to verify if(otpDetails != null &&
				 * !otpDetails.isEmpty()) { otpDetail = otpDetails.get(otpDetails.size()-1); }
				 */
				/* if (otpDetail != null) { */
				/*
				 * logger.info("otpDetail.getOtp() : " + otpDetail.getOtp()); if
				 * (otpDetail.getOtp() != null) {
				 * userRow.createCell(++cellIndex).setCellValue(otpDetail.getOtp()); } else {
				 * userRow.createCell(++cellIndex).setCellValue(""); }
				 */
				if (customerDetailsModel.getVerifiedOtp() != null && !customerDetailsModel.getVerifiedOtp().isEmpty()) {
					logger.info("customerDetailsModel.getVerifiedOtp() : " + customerDetailsModel.getVerifiedOtp());
					logger.info("customerDetailsModel.getModifiedOn() : " + customerDetailsModel.getModifiedOn());
					userRow.createCell(++cellIndex).setCellValue(customerDetailsModel.getVerifiedOtp());
					userRow.createCell(++cellIndex)
							.setCellValue(customerDetailsModel.getModifiedOn() != null
									? DateUtil.extractDateAsStringSlashFormate(customerDetailsModel.getModifiedOn())
									: "");
					userRow.createCell(++cellIndex)
							.setCellValue(customerDetailsModel.getVerifiedDate() != null
									? DateUtil.extractDateAsStringSlashFormate(customerDetailsModel.getVerifiedDate())
									: "");
					userRow.createCell(++cellIndex)
							.setCellValue(customerDetailsModel.getModifiedOn() != null
									? DateUtil.extractDateAsStringSlashFormate(customerDetailsModel.getModifiedOn())
									: "");/// SMS confirmation to seller & Customer
				} else {
					userRow.createCell(++cellIndex).setCellValue("");
					userRow.createCell(++cellIndex).setCellValue("");
					userRow.createCell(++cellIndex).setCellValue("");
					userRow.createCell(++cellIndex).setCellValue("");
				}
				userRow.createCell(++cellIndex).setCellValue(customerDetailsModel.getProposalNumber());
				userRow.createCell(++cellIndex).setCellValue(customerDetailsModel.getRoId());
				userRow.createCell(++cellIndex).setCellValue(customerDetailsModel.getSmId());
				userRow.createCell(++cellIndex).setCellValue(Constant.HEALTH_TYPE_SFQ.equals(customerDetailsModel.getHealthType()) ? Constant.HEALTH_TYPE_SFQ : Constant.HEALTH_TYPE_DOGH);
			    if(userDetailsModel.getSfqHealthDeclaration() != null) {
			    	SFQHealthDeclarationModel sfqHealthDeclarationModel = userDetailsModel.getSfqHealthDeclaration();
			    	userRow.createCell(++cellIndex).setCellValue(sfqHealthDeclarationModel.getHeight());
			    	userRow.createCell(++cellIndex).setCellValue(sfqHealthDeclarationModel.getWeight());
			    	userRow.createCell(++cellIndex).setCellValue(sfqHealthDeclarationModel.getHealthFirstAnswer() == null ? "No" : sfqHealthDeclarationModel.getHealthFirstAnswer());
			    	userRow.createCell(++cellIndex).setCellValue(sfqHealthDeclarationModel.getHealthSecondAnswer() == null ? "No" : sfqHealthDeclarationModel.getHealthSecondAnswer());
			    	userRow.createCell(++cellIndex).setCellValue(sfqHealthDeclarationModel.getHealthThirdAnswer() == null ? "No" : sfqHealthDeclarationModel.getHealthThirdAnswer());
			    	userRow.createCell(++cellIndex).setCellValue(sfqHealthDeclarationModel.getHealthFourthAnswer() == null ? "No" : sfqHealthDeclarationModel.getHealthFourthAnswer());
			    	userRow.createCell(++cellIndex).setCellValue(sfqHealthDeclarationModel.getHealthFifthAnswer() == null ? "No" : sfqHealthDeclarationModel.getHealthFifthAnswer());
			    	userRow.createCell(++cellIndex).setCellValue(sfqHealthDeclarationModel.getHealthSixthAnswer() == null ? "No" : sfqHealthDeclarationModel.getHealthSixthAnswer());
			        
			    	SFQCovidModel  sfqCovidModel = sfqHealthDeclarationModel.getSfqCovidModel();
			    	if(sfqCovidModel != null) {
			    		userRow.createCell(++cellIndex).setCellValue(sfqCovidModel.getCovidFirstAnswer() == null ? "No" : sfqCovidModel.getCovidFirstAnswer());
			    		userRow.createCell(++cellIndex).setCellValue(sfqCovidModel.getCovidSecondAnswer() == null ? "No" : sfqCovidModel.getCovidSecondAnswer());
			    		
			    		SFQCovidTestModel sfqCovidTestModel = sfqCovidModel.getSfqCovidTestModel();
			    		if(sfqCovidTestModel != null) {
			    			userRow.createCell(++cellIndex).setCellValue(sfqCovidTestModel.getCovidFirstAnswer());
			    			userRow.createCell(++cellIndex).setCellValue(sfqCovidTestModel.getCovidSecondAnswer());
			    			userRow.createCell(++cellIndex).setCellValue(sfqCovidTestModel.getCovidThirdAnswer());
			    			userRow.createCell(++cellIndex).setCellValue(sfqCovidTestModel.getCovidFourthAnswer() == null ? "No" : sfqCovidTestModel.getCovidFourthAnswer());
			    		}else {
			    			userRow.createCell(++cellIndex).setCellValue("");
			    			userRow.createCell(++cellIndex).setCellValue("");
			    			userRow.createCell(++cellIndex).setCellValue("");
			    			userRow.createCell(++cellIndex).setCellValue("");
			    		}
			    		
			    		userRow.createCell(++cellIndex).setCellValue(sfqCovidModel.getCovidThirdAnswer_a() != null || sfqCovidModel.getCovidThirdAnswer_b() != null ? "Yes" :"No");
			    		userRow.createCell(++cellIndex).setCellValue(sfqCovidModel.getCovidThirdAnswer_a());
			    		userRow.createCell(++cellIndex).setCellValue(sfqCovidModel.getCovidThirdAnswer_b() == null ? "No" : sfqCovidModel.getCovidThirdAnswer_b());
			    	    
			    		SFQCovidVaccineModel sfqCovidVaccineModel = sfqCovidModel.getSfqCovidVaccineModel();
			    		if(sfqCovidVaccineModel != null) {
			    			userRow.createCell(++cellIndex).setCellValue(sfqCovidVaccineModel.isVaccinated() ? "Yes" : "No");
			    			userRow.createCell(++cellIndex).setCellValue(sfqCovidVaccineModel.getFirstDoseDate());
			    			userRow.createCell(++cellIndex).setCellValue(sfqCovidVaccineModel.getSecondDoseDate());
			    			userRow.createCell(++cellIndex).setCellValue(sfqCovidVaccineModel.getVaccineName());
			    			userRow.createCell(++cellIndex).setCellValue(sfqCovidVaccineModel.getDeclaration());
			    		}else {
			    			userRow.createCell(++cellIndex).setCellValue("");
			    			userRow.createCell(++cellIndex).setCellValue("");
			    			userRow.createCell(++cellIndex).setCellValue("");
			    			userRow.createCell(++cellIndex).setCellValue("");
			    			userRow.createCell(++cellIndex).setCellValue("");
			    		}
			    	}else {
			    		userRow.createCell(++cellIndex).setCellValue("");
		    			userRow.createCell(++cellIndex).setCellValue("");
		    			userRow.createCell(++cellIndex).setCellValue("");
		    			userRow.createCell(++cellIndex).setCellValue("");
		    			userRow.createCell(++cellIndex).setCellValue("");
		    			userRow.createCell(++cellIndex).setCellValue("");
		    			userRow.createCell(++cellIndex).setCellValue("");
		    			userRow.createCell(++cellIndex).setCellValue("");
		    			userRow.createCell(++cellIndex).setCellValue("");
		    			userRow.createCell(++cellIndex).setCellValue("");
		    			userRow.createCell(++cellIndex).setCellValue("");
		    			userRow.createCell(++cellIndex).setCellValue("");
		    			userRow.createCell(++cellIndex).setCellValue("");
		    			userRow.createCell(++cellIndex).setCellValue("");
			    	}
			    	
			    	userRow.createCell(++cellIndex).setCellValue(sfqHealthDeclarationModel.isSmoker() ? "Yes" :"No");
			    	userRow.createCell(++cellIndex).setCellValue(sfqHealthDeclarationModel.getSmokePerDay());
			    }
				
				/*
				 * } else { userRow.createCell(++cellIndex).setCellValue("");
				 * userRow.createCell(++cellIndex).setCellValue(""); }
				 */
				// verifiedDate is AppCompletionDate
			}
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());

//			createDirectory(filePath);
			// if action is null which means this is called for daily report
			if (action == null) {
				encrypt(bis, new FileOutputStream(filePath),
						PasswordGenerateUtil.getExcelPassword(masterPolicyHolderName));
			} else if (action != null && Constant.CUSTOMER_SIMPLE_DUMP.equals(action)) {
				encrypt(bis, new FileOutputStream(filePath), null);
			}
			bis.close();
			bos.close();
			workbook.close();
			logger.info("::::END::::createAndSaveExcelFile:::Excel file has been generated!:::::::::::::::");

			/*
			 * if (action == null) { try { String sourceDownloadPath =
			 * ZipUtils.getDirNameOfVerified(DateUtil.addDaysToCurrentTS(-1),
			 * DocType.PROPOSAL, null); logger.info("sourceDownloadPath (Proposal) : " +
			 * sourceDownloadPath);
			 * awsFileUtility.bulkDownloadFromS3ToLocal(sourceDownloadPath,
			 * tempBulkFilePath, DocType.PROPOSAL);
			 * logger.info("HDF downloaded and zipped successfully to Local"); } catch
			 * (Exception exception) { exception.printStackTrace(); }
			 * 
			 * try { String sourceDownloadPath =
			 * ZipUtils.getDirNameOfVerified(DateUtil.addDaysToCurrentTS(-1),
			 * DocType.PASSPORT, null); logger.info("sourceDownloadPath (Passport) : " +
			 * sourceDownloadPath);
			 * awsFileUtility.bulkDownloadFromS3ToLocal(sourceDownloadPath,
			 * tempBulkFilePath, DocType.PASSPORT);
			 * logger.info("PAssport downloaded and zipped successfully to Local"); } catch
			 * (Exception exception) { exception.printStackTrace(); } }
			 */

		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	private void createDirectory(String fileName) {
		try {
			fileName = fileName.substring(0, fileName.lastIndexOf("/"));
			Path dirPathObj = Paths.get(fileName + "/");
			boolean dirExists = Files.exists(dirPathObj);
			if (dirExists) {
				logger.info("! Directory Already Exists !");
			} else {
				try {
					// Creating The New Directory Structure
					Files.createDirectories(dirPathObj);
					logger.info("! New Directory Successfully Created !");
				} catch (IOException ioExceptionObj) {
					ioExceptionObj.printStackTrace();
					logger.info(
							"Problem Occured While Creating The Directory Structure= " + ioExceptionObj.getMessage());
				}
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	private String getQuestionWithNewLineIfReq(StringBuilder question) {
		StringBuilder nextLine = new StringBuilder("\n");
		int maxLengthOfHeader = 30;
		if (question.toString().length() > maxLengthOfHeader) {
			question.insert(30, nextLine);
		}
		if (question.toString().length() > maxLengthOfHeader * 2) {
			question.insert(60, nextLine);
		}
		if (question.toString().length() > maxLengthOfHeader * 3) {
			question.insert(90, nextLine);
		}
		return question.toString();
	}

	@SuppressWarnings("unchecked")
	public int setAnswer(XSSFRow header, int cellIndex, XSSFRow userRow, CustomerDetailsModel customerDetailsModel,
			String type) {
		try {
			List<Map<String, Object>> answers = (List<Map<String, Object>>) customerDetailsModel.getSavedAnswers();
			String headerValue = header.getCell(++cellIndex).getStringCellValue();
			boolean isCustomerAnswer = false;
			for (Map<String, Object> ans : answers) {
				if (type.equals((String) ans.get("type"))) {
					StringBuilder tempQuestion = new StringBuilder((String) ans.get("question"));
					String question = getQuestionWithNewLineIfReq(tempQuestion);
					if (headerValue.trim().equalsIgnoreCase((question).trim())) {
						isCustomerAnswer = true;
						switch ((String) ans.get("qType")) {
						case Constant.LISTING_TYPE:
							List<Map<String, Object>> selectOption = (List<Map<String, Object>>) ans
									.get("selectedOptions");
							if (selectOption != null && !selectOption.isEmpty()) {
								userRow.createCell(cellIndex).setCellValue("" + selectOption.get(0).get("value"));
							} else {
								userRow.createCell(cellIndex).setCellValue("");
							}
							break;
						case Constant.MULTI_SELECT_TYPE:
							List<Map<String, Object>> multiSelectAns = (List<Map<String, Object>>) ans
									.get("selectedOptions");
							StringBuilder mutiAns = new StringBuilder();
							if (multiSelectAns != null && !multiSelectAns.isEmpty()) {
								for (Map<String, Object> map : multiSelectAns) {
									mutiAns.append((String) map.get("value"));
									mutiAns.append(", ");
								}
							}
							userRow.createCell(cellIndex).setCellValue(mutiAns.toString());
							break;
						case Constant.YES_NO_TYPE:
							String flag = (String) ans.get("flag");
							userRow.createCell(cellIndex).setCellValue(flag);
							break;
						case Constant.REMARK_TYPE:
							String remarkAns = (String) ans.get("flag") + ", " + (String) ans.get("description");
							userRow.createCell(cellIndex).setCellValue(remarkAns);
							break;
						case Constant.DESCRIPTON_TYPE:
							String descAns = (String) ans.get("description");
							userRow.createCell(cellIndex).setCellValue(descAns);
							break;
						case Constant.DATE_TYPE:
							String dateAns = (String) ans.get("description");
							userRow.createCell(cellIndex).setCellValue(dateAns);
							break;
						}
						break;
					}
				}
			}
			if (!isCustomerAnswer) {
//				userRow.createCell(cellIndex).setCellValue("");
			}
			return cellIndex;
		} catch (Exception exception) {
			logger.info("Exception occured while getting answer for Excel dump : " + exception);
			exception.printStackTrace();
		}
		return cellIndex;
	}

	/**
	 * 
	 * @param input
	 * @param output
	 * @param password
	 * @throws IOException
	 * 
	 *                     Encrypting excel with EncryptionMode.standard.
	 */
	private void encrypt(InputStream input, OutputStream output, String password) throws IOException {
		try {
			POIFSFileSystem fs = new POIFSFileSystem();
			EncryptionInfo info = new EncryptionInfo(EncryptionMode.standard);
			Encryptor enc = info.getEncryptor();
			enc.confirmPassword(password);
			OPCPackage opc = OPCPackage.open(input);
			OutputStream os = enc.getDataStream(fs);
			opc.save(os);
			opc.close();
			fs.writeFilesystem(output);
			output.close();
		} catch (GeneralSecurityException e) {
			throw new RuntimeException(e);
		} catch (InvalidFormatException e) {
			throw new RuntimeException(e);
		}

	}

	public void getExcelDataForSeller(List<SellerDetailModel> userDetailModels, String filePath,
			String masterPolicyHolderName) {
		try {
			XSSFWorkbook workbook = new XSSFWorkbook();
			logger.info("::::START::::getExcelData::::::::::::::::::");
			// create excel xls sheet
			XSSFSheet sheet = workbook.createSheet("Seller");
			// create style for header cells
			XSSFCellStyle style = workbook.createCellStyle();
			XSSFCellStyle styleNumber = workbook.createCellStyle();
			XSSFFont font = workbook.createFont();
			font.setFontName("Normal");
			font.setBold(true);
			style.setFont(font);
			style.setBorderLeft(BorderStyle.THIN);
			style.setBorderRight(BorderStyle.THIN);
			style.setBorderBottom(BorderStyle.THIN);
			DataFormat format = workbook.createDataFormat();
			styleNumber.setDataFormat(format.getFormat("0.00"));
			for (int columnCount = 0; columnCount < 20; columnCount++) {
				sheet.setColumnWidth(columnCount, 7000);
			}

			CellStyle backgroundHdrStyle = workbook.createCellStyle();
			backgroundHdrStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
			backgroundHdrStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			backgroundHdrStyle.setBorderLeft(BorderStyle.THIN);
			backgroundHdrStyle.setBorderRight(BorderStyle.THIN);
			backgroundHdrStyle.setBorderBottom(BorderStyle.THIN);
			backgroundHdrStyle.setFont(font);

			XSSFRow header = sheet.createRow(0);
			header.createCell(0).setCellValue("Client Name");
			header.getCell(0).setCellStyle(backgroundHdrStyle);

			header.createCell(1).setCellValue("Seller Mobile No");
			header.getCell(1).setCellStyle(backgroundHdrStyle);

			header.createCell(2).setCellValue("Seller Name");
			header.getCell(2).setCellStyle(backgroundHdrStyle);

			header.createCell(3).setCellValue("Email ID");
			header.getCell(3).setCellStyle(backgroundHdrStyle);

			header.createCell(4).setCellValue("Source Emp Code");
			header.getCell(4).setCellStyle(backgroundHdrStyle);

			header.createCell(5).setCellValue("Creation date");
			header.getCell(5).setCellStyle(backgroundHdrStyle);

			header.createCell(6).setCellValue("Last Modify Date");
			header.getCell(6).setCellStyle(backgroundHdrStyle);

			header.createCell(7).setCellValue("Status");
			header.getCell(7).setCellStyle(backgroundHdrStyle);

			header.createCell(8).setCellValue("RAC Location Mapping");
			header.getCell(8).setCellStyle(backgroundHdrStyle);

			header.createCell(9).setCellValue("MLI Sales Manager");
			header.getCell(9).setCellStyle(backgroundHdrStyle);

			header.createCell(10).setCellValue("MLI SM-Code");
			header.getCell(10).setCellStyle(backgroundHdrStyle);

			header.createCell(11).setCellValue("MLI RM");
			header.getCell(11).setCellStyle(backgroundHdrStyle);

			header.createCell(12).setCellValue("MLI RM-Code");
			header.getCell(12).setCellStyle(backgroundHdrStyle);
			
			header.createCell(13).setCellValue("LOAN TYPE");
			header.getCell(13).setCellStyle(backgroundHdrStyle);

			int rowCount = 1;
			for (SellerDetailModel userDetailsModel : userDetailModels) {
				XSSFRow userRow = sheet.createRow(rowCount++);
				StringBuilder banks = new StringBuilder();
				if (userDetailsModel.getSellerBankDetails() != null
						&& !userDetailsModel.getSellerBankDetails().isEmpty()) {
					for (SellerBankModel tmpBank : userDetailsModel.getSellerBankDetails()) {
						banks.append(tmpBank.getBankNameDesc());
						banks.append(" ");
					}
				}
				
				userRow.createCell(0).setCellValue(banks.toString());
				userRow.createCell(1).setCellValue(userDetailsModel.getContactNo());
				userRow.createCell(2).setCellValue(userDetailsModel.getSellerName());
				userRow.createCell(3).setCellValue(
						userDetailsModel.getSellerEmailId() != null ? userDetailsModel.getSellerEmailId() : "");
				userRow.createCell(4).setCellValue(
						userDetailsModel.getSourceEmpCode() != null ? userDetailsModel.getSourceEmpCode() : "");
				userRow.createCell(5)
						.setCellValue(userDetailsModel.getCreatedOn() != null
								? DateUtil.extractDateWithTSAsStringSlashFormate(userDetailsModel.getCreatedOn())
								: "");
				userRow.createCell(6)
						.setCellValue(userDetailsModel.getModifiedOn() != null
								? DateUtil.extractDateWithTSAsStringSlashFormate(userDetailsModel.getModifiedOn())
								: "");
				userRow.createCell(7).setCellValue(userDetailsModel.getStatus() == null ? Constant.SELLER_ACTIVE_STATUS
						: userDetailsModel.getStatus());
				userRow.createCell(8).setCellValue(userDetailsModel.getRacLocationMapping());

				userRow.createCell(9).setCellValue(userDetailsModel.getMliSalesManager());
				userRow.createCell(10).setCellValue(userDetailsModel.getMliSMCode());
				userRow.createCell(11).setCellValue(userDetailsModel.getMliRM());
				userRow.createCell(12).setCellValue(userDetailsModel.getMliRMCode());
				//userRow.createCell(13).setCellValue(userDetailsModel.getLoanType());
			}
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);

			ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
			bis.close();
			bos.close();

			encrypt(bis, new FileOutputStream(filePath), null);
			bis.close();
			bos.close();

			workbook.close();
			logger.info("::::END::::createAndSaveExcelFile:::Excel file has been generated!:::::::::::::::");
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
	
	public void getYBLCCExcelData(List<CreditCardCustomerEntity> creditCardCustomerEntityList, String filePath, String masterPolicyHolderName,
			String action) {
		try {
			XSSFWorkbook workbook = new XSSFWorkbook();
			logger.info("::::START::::getExcelData::::::::::::::::::");
			// create excel xls sheet
			XSSFSheet sheet = workbook.createSheet("Customer Proposals");
			// create style for header cells
			XSSFCellStyle style = workbook.createCellStyle();
			XSSFCellStyle styleNumber = workbook.createCellStyle();
			XSSFFont font = workbook.createFont();
			font.setFontName("Normal");
			font.setBold(true);
			style.setFont(font);
			style.setBorderLeft(BorderStyle.THIN);
			style.setBorderRight(BorderStyle.THIN);
			style.setBorderBottom(BorderStyle.THIN);
			DataFormat format = workbook.createDataFormat();
			styleNumber.setDataFormat(format.getFormat("0.00"));
			for (int columnCount = 0; columnCount < 200; columnCount++) {
				sheet.setColumnWidth(columnCount, 7000);
			}

			CellStyle backgroundHdrStyle = workbook.createCellStyle();
			backgroundHdrStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
			backgroundHdrStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			backgroundHdrStyle.setBorderLeft(BorderStyle.THIN);
			backgroundHdrStyle.setBorderRight(BorderStyle.THIN);
			backgroundHdrStyle.setBorderBottom(BorderStyle.THIN);
			backgroundHdrStyle.setFont(font);

			XSSFRow header = sheet.createRow(0);
			header.createCell(0).setCellValue("Processing date");
			header.getCell(0).setCellStyle(backgroundHdrStyle);

			header.createCell(1).setCellValue("Seller Name");
			header.getCell(1).setCellStyle(backgroundHdrStyle);

			header.createCell(2).setCellValue("Seller Mobile No");
			header.getCell(2).setCellStyle(backgroundHdrStyle);

			header.createCell(3).setCellValue("Source Emp Code");
			header.getCell(3).setCellStyle(backgroundHdrStyle);

			header.createCell(4).setCellValue("RAC Location Mapping");
			header.getCell(4).setCellStyle(backgroundHdrStyle);

			header.createCell(5).setCellValue("MLI Sales Manager name");
			header.getCell(5).setCellStyle(backgroundHdrStyle);

			header.createCell(6).setCellValue("MLI SM-Code");
			header.getCell(6).setCellStyle(backgroundHdrStyle);

			header.createCell(7).setCellValue("MLI RM");
			header.getCell(7).setCellStyle(backgroundHdrStyle);

			header.createCell(8).setCellValue("MLI RM-Code");
			header.getCell(8).setCellStyle(backgroundHdrStyle);

			header.createCell(9).setCellValue("Client Name");
			header.getCell(9).setCellStyle(backgroundHdrStyle);

			header.createCell(10).setCellValue("Customer Id");
			header.getCell(10).setCellStyle(backgroundHdrStyle);
			
			header.createCell(11).setCellValue("Customer First Name");
			header.getCell(11).setCellStyle(backgroundHdrStyle);

			header.createCell(12).setCellValue("Customer Last Name");
			header.getCell(12).setCellStyle(backgroundHdrStyle);
			
			header.createCell(13).setCellValue("Customer Mobile Number");
			header.getCell(13).setCellStyle(backgroundHdrStyle);
			
			header.createCell(14).setCellValue("Customer Email ID");
			header.getCell(14).setCellStyle(backgroundHdrStyle);

			header.createCell(15).setCellValue("Customer Gender");
			header.getCell(15).setCellStyle(backgroundHdrStyle);

			header.createCell(16).setCellValue("Date of Birth");
			header.getCell(16).setCellStyle(backgroundHdrStyle);

			header.createCell(17).setCellValue("Credit Card Segment");
			header.getCell(17).setCellStyle(backgroundHdrStyle);

			header.createCell(18).setCellValue("Application No.");
			header.getCell(18).setCellStyle(backgroundHdrStyle);

			header.createCell(19).setCellValue("Sum Assured(Rs.)");
			header.getCell(19).setCellStyle(backgroundHdrStyle);

			header.createCell(20).setCellValue("Premium(Incl. of GST)(Rs.)");
			header.getCell(20).setCellStyle(backgroundHdrStyle);

			header.createCell(21).setCellValue("Coverage Start Date");
			header.getCell(21).setCellStyle(backgroundHdrStyle);

			header.createCell(22).setCellValue("Coverage End Date");
			header.getCell(22).setCellStyle(backgroundHdrStyle);
			
			header.createCell(23).setCellValue("Application Status");
			header.getCell(23).setCellStyle(backgroundHdrStyle);
			

			header.createCell(24).setCellValue("Name of Nominee: First Name ");
			header.getCell(24).setCellStyle(backgroundHdrStyle);
			header.createCell(25).setCellValue("Name of Nominee: Last Name");
			header.getCell(25).setCellStyle(backgroundHdrStyle);
			header.createCell(26).setCellValue("Nominee Date of Birth");
			header.getCell(26).setCellStyle(backgroundHdrStyle);
			header.createCell(27).setCellValue("Gender");
			header.getCell(27).setCellStyle(backgroundHdrStyle);
			header.createCell(28).setCellValue("Relationship with the Assured");
			header.getCell(28).setCellStyle(backgroundHdrStyle);
			header.createCell(29).setCellValue("Name of Appointee: First Name ");
			header.getCell(29).setCellStyle(backgroundHdrStyle);
			header.createCell(30).setCellValue("Name of Appointee  Last Name");
			header.getCell(30).setCellStyle(backgroundHdrStyle);
			header.createCell(31).setCellValue("Appointee Date of Birth");
			header.getCell(31).setCellStyle(backgroundHdrStyle);
			header.createCell(32).setCellValue("Gender");
			header.getCell(32).setCellStyle(backgroundHdrStyle);
			header.createCell(33).setCellValue("Relationship with the Nominee");
			header.getCell(33).setCellStyle(backgroundHdrStyle);
			
			//Health question
			header.createCell(34).setCellValue(YBLCCHealthQuestion.QUESTION1);
			header.getCell(34).setCellStyle(backgroundHdrStyle);
			header.createCell(35).setCellValue(YBLCCHealthQuestion.QUESTION2);
			header.getCell(35).setCellStyle(backgroundHdrStyle);
			header.createCell(36).setCellValue(YBLCCHealthQuestion.QUESTION3);
			header.getCell(36).setCellStyle(backgroundHdrStyle);
			header.createCell(37).setCellValue(YBLCCHealthQuestion.QUESTION4);
			header.getCell(37).setCellStyle(backgroundHdrStyle);
			header.createCell(38).setCellValue(YBLCCHealthQuestion.QUESTION5);
			header.getCell(38).setCellStyle(backgroundHdrStyle);
			header.createCell(39).setCellValue(YBLCCHealthQuestion.QUESTION6);
			header.getCell(39).setCellStyle(backgroundHdrStyle);
            //Covid question
			header.createCell(40).setCellValue("Covid declaration");
			header.getCell(40).setCellStyle(backgroundHdrStyle);
			header.createCell(41).setCellValue("Covid comment");
			header.getCell(41).setCellStyle(backgroundHdrStyle);
			
			//mandatory declaration
			header.createCell(42).setCellValue("Mandatory Declaration");
			header.getCell(42).setCellStyle(backgroundHdrStyle);
			header.createCell(43).setCellValue("Place");
			header.getCell(43).setCellStyle(backgroundHdrStyle);
			
			//payment
			header.createCell(44).setCellValue("Premium Paid");
			header.getCell(44).setCellStyle(backgroundHdrStyle);
			header.createCell(45).setCellValue("Date of Payment");
			header.getCell(45).setCellStyle(backgroundHdrStyle);
			header.createCell(46).setCellValue("Application No.");
			header.getCell(46).setCellStyle(backgroundHdrStyle);
			header.createCell(47).setCellValue("Mode(YES Credit/Debit)");
			header.getCell(47).setCellStyle(backgroundHdrStyle);
			header.createCell(48).setCellValue("Transaction Ref.No");
			header.getCell(48).setCellStyle(backgroundHdrStyle);
			header.createCell(49).setCellValue("Proposal Number");
			header.getCell(49).setCellStyle(backgroundHdrStyle);

			
			logger.info(
					"Today date : " + new Date() + ", Total number of column in excel: " + 50);

			int rowCount = 1;
			for (CreditCardCustomerEntity creditCardCustomerEntity : creditCardCustomerEntityList) {
				XSSFRow userRow = sheet.createRow(rowCount++);
				PaymentEntity paymentEntity = paymentDAO.findByCreditCardCustomerIdAndStatus(creditCardCustomerEntity.getCreditCardCustomerId(), "SUCCESS");
				CreditCardNomineeEntity creditCardNomineeEntity = creditCardNomineeDAO.findByCustomerDtlId(creditCardCustomerEntity.getCreditCardCustomerId());
				CreditCardHealthEntity creditCardHealthEntity = creditCardHealthDAO.findByCustomerDtlId(creditCardCustomerEntity.getCreditCardCustomerId());
				CreditCardMandatoryEntity creditCardMandatoryEntity = creditCardMandatoryDAO.findbyCreditCardCustomerId(creditCardCustomerEntity.getCreditCardCustomerId());
				SellerDetailEntity sellerDetailEntity = sellerDAO.findBySellerId(creditCardCustomerEntity.getSlrDtlId().getSellerDtlId());
				//seller details
				if (!ObjectUtils.isEmpty(sellerDetailEntity)) {
					userRow.createCell(0).setCellValue(creditCardCustomerEntity.getCreatedOn() != null
									? DateUtil.extractDateAsStringSlashFormate(creditCardCustomerEntity.getCreatedOn()): "");
					userRow.createCell(1).setCellValue(sellerDetailEntity.getSellerName());
					userRow.createCell(2).setCellValue(sellerDetailEntity.getContactNo());
					userRow.createCell(3).setCellValue(sellerDetailEntity.getSourceEmpCode());// Source Emp Code
					userRow.createCell(4).setCellValue(sellerDetailEntity.getRacLocationMapping());// RAC Location Mapping
					userRow.createCell(5).setCellValue(sellerDetailEntity.getMliSalesManager());// MLI Sales Manager name
					userRow.createCell(6).setCellValue(sellerDetailEntity.getMliSMCode() != null ? sellerDetailEntity.getMliSMCode() : "NA");
					userRow.createCell(7).setCellValue(sellerDetailEntity.getMliRM());// MLI RM
					userRow.createCell(8).setCellValue(sellerDetailEntity.getMliRMCode() != null ? sellerDetailEntity.getMliRMCode() : "NA");
				}
			    //customer details
				userRow.createCell(9).setCellValue(Constant.YBLCC);
				userRow.createCell(10).setCellValue(creditCardCustomerEntity.getCustomerId());
				userRow.createCell(11).setCellValue(creditCardCustomerEntity.getFirstName());
				userRow.createCell(12).setCellValue(creditCardCustomerEntity.getLastName());
				userRow.createCell(13).setCellValue(creditCardCustomerEntity.getPhone());
				userRow.createCell(14).setCellValue(creditCardCustomerEntity.getEmail());
				userRow.createCell(15).setCellValue(creditCardCustomerEntity.getGender());
				userRow.createCell(16).setCellValue(creditCardCustomerEntity.getDob());
				userRow.createCell(17).setCellValue(creditCardCustomerEntity.getCardSegment());
				userRow.createCell(18).setCellValue(creditCardCustomerEntity.getProposalNumber());
				userRow.createCell(19).setCellValue(creditCardCustomerEntity.getCoverage());
				userRow.createCell(20).setCellValue(creditCardCustomerEntity.getPremium());
				userRow.createCell(21).setCellValue(creditCardCustomerEntity.getCreatedOn() != null
						? DateUtil.extractDateAsStringSlashFormate(creditCardCustomerEntity.getCreatedOn()): "");
				if(!ObjectUtils.isEmpty(paymentEntity)) {
					userRow.createCell(22).setCellValue(paymentEntity.getPaymentOn() != null
							? DateUtil.extractDateAsStringSlashFormate(paymentEntity.getPaymentOn()): "");
				}else {
					userRow.createCell(22).setCellValue("");
				}
				userRow.createCell(23).setCellValue(creditCardCustomerEntity.getAppStatus().toString());
				
				//Nominee details
				if(!ObjectUtils.isEmpty(creditCardNomineeEntity)) {
					userRow.createCell(24).setCellValue(creditCardNomineeEntity.getNomineeFirstName());
					userRow.createCell(25).setCellValue(creditCardNomineeEntity.getNomineeLastName());
					userRow.createCell(26).setCellValue(creditCardNomineeEntity.getNomineeDob());
					userRow.createCell(27).setCellValue(creditCardNomineeEntity.getNomineeGender());
					if(creditCardNomineeEntity.getRelationshipWithAssured() != null && !"".equals(creditCardNomineeEntity.getRelationshipWithAssured())) {
						userRow.createCell(28).setCellValue(creditCardNomineeEntity.getRelationshipWithAssured().getLabel());
					}else {
						userRow.createCell(28).setCellValue("");
					}
					if(creditCardNomineeEntity.isAppointee()) {
					   userRow.createCell(29).setCellValue(creditCardNomineeEntity.getAppointeeFirstName());
					   userRow.createCell(30).setCellValue(creditCardNomineeEntity.getAppointeeLastName());
					   userRow.createCell(31).setCellValue(creditCardNomineeEntity.getAppointeeDob());
					   userRow.createCell(32).setCellValue(creditCardNomineeEntity.getAppointeeGender());
					   if(creditCardNomineeEntity.getRelationWithNominee() != null && !"".equals(creditCardNomineeEntity.getRelationWithNominee())) {
						   userRow.createCell(33).setCellValue(creditCardNomineeEntity.getRelationWithNominee().getLabel());
					   }else {
						   userRow.createCell(33).setCellValue("");
					   }
					}else {
						userRow.createCell(29).setCellValue("");
						userRow.createCell(30).setCellValue("");
						userRow.createCell(31).setCellValue("");
						userRow.createCell(32).setCellValue("");
						userRow.createCell(33).setCellValue("");
					}
				}
				
				if(!ObjectUtils.isEmpty(creditCardHealthEntity)) {
					CreditCardCovidEntity creditCardCovidEntity = creditCardCovidDAO.findByCreditCardHealthId(creditCardHealthEntity.getCreditCardHealthId());
					userRow.createCell(34).setCellValue(creditCardHealthEntity.getHealthFirstAnswer());
					userRow.createCell(35).setCellValue(creditCardHealthEntity.getHealthSecondAnswer());
					userRow.createCell(36).setCellValue(creditCardHealthEntity.getHealthThirdAnswer());
					userRow.createCell(37).setCellValue(creditCardHealthEntity.getIsSmoker());
					userRow.createCell(38).setCellValue(creditCardHealthEntity.getSmokePerDay() != null ? creditCardHealthEntity.getSmokePerDay(): "");
					userRow.createCell(39).setCellValue(creditCardHealthEntity.getDeclaration());
					if(!ObjectUtils.isEmpty(creditCardCovidEntity)) {
						userRow.createCell(40).setCellValue(creditCardCovidEntity.getCovidDeclaration());
						userRow.createCell(41).setCellValue(creditCardCovidEntity.getComment());
					}else {
						userRow.createCell(40).setCellValue("");
						userRow.createCell(41).setCellValue("");
					}
				}else {
					userRow.createCell(34).setCellValue("");
					userRow.createCell(35).setCellValue("");
					userRow.createCell(36).setCellValue("");
					userRow.createCell(37).setCellValue("");
					userRow.createCell(38).setCellValue("");
					userRow.createCell(39).setCellValue("");
					userRow.createCell(40).setCellValue("");
					userRow.createCell(41).setCellValue("");
				}
				
				if(!ObjectUtils.isEmpty(creditCardMandatoryEntity)) {
					userRow.createCell(42).setCellValue(creditCardMandatoryEntity.getIsMandatoryDeclaration());
					userRow.createCell(43).setCellValue(creditCardMandatoryEntity.getPlace());
				}
				
				if(!ObjectUtils.isEmpty(paymentEntity)) {
					userRow.createCell(44).setCellValue(paymentEntity.getAmount());
					userRow.createCell(45).setCellValue(paymentEntity.getTxnDate());
					userRow.createCell(46).setCellValue(creditCardCustomerEntity.getProposalNumber());
					if(!StringUtils.isEmpty(paymentEntity.getBankCode())){
						if("1340".equalsIgnoreCase(paymentEntity.getBankCode())) {
							userRow.createCell(47).setCellValue("Yes Bank Credit Card");
						} else if("1350".equalsIgnoreCase(paymentEntity.getBankCode())) {
							userRow.createCell(47).setCellValue("Yes Bank Debit Card");
						} else {
							userRow.createCell(47).setCellValue("");
						}
					}else {
						userRow.createCell(47).setCellValue("");
					}
					userRow.createCell(48).setCellValue(paymentEntity.getTxnId());
				}
				userRow.createCell(49).setCellValue(creditCardCustomerEntity.getProposalNumber());
			

			}
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			workbook.write(bos);
			ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());

//			createDirectory(filePath);
			// if action is null which means this is called for daily report
			if (action == null) {
				encrypt(bis, new FileOutputStream(filePath),
						PasswordGenerateUtil.getExcelPassword(masterPolicyHolderName));
			} else if (action != null && Constant.CUSTOMER_SIMPLE_DUMP.equals(action)) {
				encrypt(bis, new FileOutputStream(filePath), null);
			}
			bis.close();
			bos.close();
			workbook.close();
			logger.info("::::END::::createAndSaveExcelFile:::Excel file has been generated!:::::::::::::::");
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
}
