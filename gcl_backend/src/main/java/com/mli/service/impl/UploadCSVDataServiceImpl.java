package com.mli.service.impl;


import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import com.mli.constants.Constant;
import com.mli.constants.MLIMessageConstants;
import com.mli.controller.UploadCSVDataController;
import com.mli.dao.SellerDAO;
import com.mli.entity.SellerDetailEntity;
import com.mli.exception.StorageException;
import com.mli.filter.FileTypeTest;
import com.mli.helper.SellerHelper;
import com.mli.model.SellerBankModel;
import com.mli.model.SellerDetailModel;
import com.mli.model.response.ResponseModel;
import com.mli.model.response.SaveUpdateResponse;
import com.mli.service.SellerService;
import com.mli.service.UploadCSVDataService;
import com.mli.utils.DateUtil;
import com.mli.utils.EmailValidationUtil;
import com.mli.utils.ObjectsUtil;

/**
 * @author Nikhilesh.Tiwari
 *
 */
@Service
public class UploadCSVDataServiceImpl implements UploadCSVDataService {

	private static final Logger logger = Logger.getLogger(UploadCSVDataController.class);

	@Autowired
	private SellerService sellerService;
	
	@Autowired
	private SellerDAO sellerDAO;
	
	@Autowired
	private SellerHelper sellerHelper;

	
	/**
	 *  Upload seller data from xls file into database 
	 */
	@Override
	@Transactional()
	public ResponseModel<?> uploadSellerInBulk(MultipartFile csvFile) throws Exception {
		ResponseModel<?> response = new ResponseModel<>();
		try {
			
			List<SellerDetailModel> sellerDetailList = new ArrayList<SellerDetailModel>();
			XSSFWorkbook myWorkBook = new XSSFWorkbook(csvFile.getInputStream());
			XSSFSheet worksheet = myWorkBook.getSheetAt(0);
			Iterator<Row> rowIterator = worksheet.iterator();
			Row header = null;
			
			
			// validate excel format and checked for duplicate Mobile Number in excel
			try {
				List<Long> mobileNumbers = new ArrayList<>(100);
				while (rowIterator.hasNext()) {
					Row row = rowIterator.next();
					if (row.getRowNum() == 0) {
						boolean fileFormat = checkFileFormat(row);
						if (!fileFormat) {
							response.setMessage(MLIMessageConstants.FILE_FORMAT);
							response.setStatus(SaveUpdateResponse.FAILURE);
							return response;
						}
						if (row.getRowNum() == 0) {
							header = row;
							continue;
						}
					}
					if (row.getCell(1) != null) {
						Double tmpMob = row.getCell(1).getNumericCellValue();
						mobileNumbers.add(tmpMob.longValue());
					}
				}
				Set<Long> uniqueMobileNumbers = new HashSet<>(mobileNumbers);
				logger.info(uniqueMobileNumbers.size() + " : " + mobileNumbers.size());
				if (uniqueMobileNumbers.size() != mobileNumbers.size()) {
					response.setMessage(MLIMessageConstants.EXCEP_DUPLI_MOBILE_NUMBER);
					response.setStatus(SaveUpdateResponse.FAILURE);
					return response;
				}
			} catch (Exception exception) {
				exception.printStackTrace();
				response.setMessage(Constant.FAILURE_MSG);
				response.setStatus(Constant.FAILURE);
				return response;
			}
			
			// Read Excel
			rowIterator = worksheet.iterator();
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				if (row.getRowNum() == 0) {
				  if(row.getRowNum() == 0){
					  continue;
				  }
				}
				SellerDetailModel selrDtl = new SellerDetailModel();
				for (int cellIndex = 0; cellIndex < header.getLastCellNum(); cellIndex++) {
					Cell cell = null;
					String headerValue = header.getCell(cellIndex).getStringCellValue();
					cell = row.getCell(cellIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
					String stringColumn = null;
					Long longValue = 0l;
					Integer columnValue = cell.getColumnIndex();
					Set<SellerBankModel> sellerBankDetailList = new HashSet<>();
					switch (cell.getCellType()) {
					case Cell.CELL_TYPE_STRING:
						if (columnValue == 3) {
							String[] column = cell.getStringCellValue().split(",");
							for (String string : column) {
								if (string != null) {
									string = string.trim();
									if (!(string.equalsIgnoreCase(Constant.AXIS)
											|| string.equalsIgnoreCase(Constant.YES) || string.equalsIgnoreCase(Constant.YESBANKCC))) {
										response.setMessage(String.format(MLIMessageConstants.EXCEL_INVALID_BANK_NAME,
												headerValue, row.getRowNum(), columnValue + 1));
										response.setStatus(Constant.FAILURE);
										return response;
									}
								}
								SellerBankModel selBankDtl = new SellerBankModel();
								selBankDtl.setBankName(string);
								sellerBankDetailList.add(selBankDtl);
							}

						} else {
							stringColumn = cell.getStringCellValue();
						}

						break;
					case Cell.CELL_TYPE_NUMERIC:
						Double value = cell.getNumericCellValue();
						longValue = value.longValue();
						stringColumn = longValue.toString();
						break;
					case Cell.CELL_TYPE_BLANK:
						if (!("Seller Employee Code".equalsIgnoreCase(headerValue)
								|| "Email ID".equalsIgnoreCase(headerValue) || "Role Id".equalsIgnoreCase(headerValue))) {
							response.setStatus(SaveUpdateResponse.FAILURE);
							response.setMessage(
									String.format(MLIMessageConstants.CELL_NOT_EMPTY, row.getRowNum(), columnValue + 1));
							return response;
						}
					}
					switch (columnValue) {
					case 0:
						if (!ObjectsUtil.isNull(stringColumn)) {
							selrDtl.setSellerName(stringColumn);
						} else {
							response.setMessage(String.format(MLIMessageConstants.EXCEL_NOT_NULL_SELLER_NAME,row.getRowNum(), columnValue+1 ));
							response.setStatus(SaveUpdateResponse.FAILURE);
							return response;
						}

						break;
					case 1:
						if (!ObjectsUtil.isNull(stringColumn) && stringColumn.length() == 10) {
							selrDtl.setContactNo(Long.parseLong(stringColumn));
							if(sellerService.checkSellerIfExist(selrDtl.getContactNo())){								
								response.setStatus(SaveUpdateResponse.FAILURE);
								response.setMessage("Seller already exists with mobile number: "+selrDtl.getContactNo()+", at row no." + row.getRowNum());
								return response;
							}
						} else {
							response.setMessage(String.format(MLIMessageConstants.TEN_DIGIT_CONTACT_NO,row.getRowNum(), columnValue+1));
							response.setStatus(SaveUpdateResponse.FAILURE);
							return response;
						}
						break;
					case 2:
						if (!ObjectsUtil.isNull(stringColumn)) {
							 if(EmailValidationUtil.isEmailValidation(stringColumn)){
								 selrDtl.setSellerEmailId(stringColumn);
							 }else{
									response.setMessage(String.format(MLIMessageConstants.EXCEL_NOT_NULL_SELLER_EMAIL_FORMAT,row.getRowNum(), columnValue+1));
									response.setStatus(SaveUpdateResponse.FAILURE);
									return response;
							 }
						}/* else {
							response.setMessage(String.format(MLIMessageConstants.EXCEL_NOT_NULL_SELLER_EMAIL,row.getRowNum(), columnValue+1));
							response.setStatus(SaveUpdateResponse.FAILURE);
							return response;
						}*/
						break;
					case 3:
						if (!ObjectsUtil.isNull(sellerBankDetailList) && !sellerBankDetailList.isEmpty()) {
							selrDtl.setSellerBankDetails(sellerBankDetailList);
							selrDtl.setSellerBankName(sellerBankDetailList.iterator().next().getBankName());
						} else {
							response.setMessage(String.format(MLIMessageConstants.EXCEL_NOT_NULL_SELLER_BANK,
									row.getRowNum(), columnValue + 1));
							response.setStatus(SaveUpdateResponse.FAILURE);
							return response;
						}
						break;
					case 4:
						if (!ObjectsUtil.isNull(stringColumn)) {
							selrDtl.setSourceEmpCode(stringColumn);
						} /*else {
							response.setMessage(String.format(MLIMessageConstants.EXCEL_SELLER_EMP_CODE ,row.getRowNum(), columnValue+1));
							response.setStatus(SaveUpdateResponse.FAILURE);
							return response;
						}*/
						break;
					case 5:
						if (!ObjectsUtil.isNull(stringColumn)) {
							selrDtl.setRacLocationMapping(stringColumn);
						} else {
							response.setMessage(String.format(MLIMessageConstants.EXCEL_RAC_LOCATION_MAPPING ,row.getRowNum(), columnValue+1));
							response.setStatus(SaveUpdateResponse.FAILURE);
							return response;
						}
						break;
					case 6:
						if (!ObjectsUtil.isNull(stringColumn)) {
							selrDtl.setMliSalesManager(stringColumn);
						} else {
							response.setMessage(String.format(MLIMessageConstants.EXCEL_MLI_SALES_MANAGER ,row.getRowNum(), columnValue+1));
							response.setStatus(SaveUpdateResponse.FAILURE);
							return response;
						}
						break;
					case 7:
						if (!ObjectsUtil.isNull(stringColumn)) {
							selrDtl.setMliSMCode(stringColumn);
						} else {
							response.setMessage(String.format(MLIMessageConstants.EXCEL_SM_CODE ,row.getRowNum(), columnValue+1));
							response.setStatus(SaveUpdateResponse.FAILURE);
							return response;
						}
						break;
					case 8:
						if (!ObjectsUtil.isNull(stringColumn)) {
							selrDtl.setMliRM(stringColumn);
						} else {
							response.setMessage(String.format(MLIMessageConstants.EXCEL_RM ,row.getRowNum(), columnValue+1));
							response.setStatus(SaveUpdateResponse.FAILURE);
							return response;
						}
						break;
					case 9:
						if (!ObjectsUtil.isNull(stringColumn)) {
							selrDtl.setMliRMCode(stringColumn);
						} else {
							response.setMessage(String.format(MLIMessageConstants.EXCEL_RM_CODE ,row.getRowNum(), columnValue+1));
							response.setStatus(SaveUpdateResponse.FAILURE);
							return response;
						}
						break;
						
					case 10:
						if (Constant.YES.equalsIgnoreCase(selrDtl.getSellerBankName())) {
							if (!ObjectsUtil.isNull(stringColumn)) {
								Integer value = Integer.parseInt(stringColumn);
								if(!(1 <= value && value <= 5)) {
									response.setMessage(String.format(MLIMessageConstants.ROLE_VALIDATION));
									response.setStatus(SaveUpdateResponse.FAILURE);
									return response;
								}
								String role = MLIMessageConstants.ROLE +" "+stringColumn;
								selrDtl.setRole(role);
							    selrDtl.setLoanTypes(sellerService.findLonaTypeById(role));
							} else {
								response.setMessage(String.format(MLIMessageConstants.ROLE_ID, row.getRowNum(), columnValue + 1));
								response.setStatus(SaveUpdateResponse.FAILURE);
								return response;
							}
						}
						break;
						
					}
				}
				selrDtl.setStatus(Constant.SELLER_ACTIVE_STATUS);
				sellerDetailList.add(selrDtl);
			}
			
			int i = 0;
			for (SellerDetailModel sellerDlt : sellerDetailList) {
				try {
					i++;
					sellerService.saveOrUpdateSallerDetails(sellerDlt);
				}catch (Exception e) {
					logger.error("::::::::::::::::: Error in uploadSellerInBulk  save or update saller details:::::::::::::::" + e);
				} 
			}
			response.setMessage(MLIMessageConstants.CSV_UPLOAD_SUCCESS);
			response.setStatus(SaveUpdateResponse.SUCCESS);
			return response; 
		}catch (StorageException e) {
			e.printStackTrace();
			logger.error("::::::::::::::::: Error in uploadSellerInBulk  :::::::::::::::" + e);
			throw new StorageException(MLIMessageConstants.FILE_FORMAT);
		}catch(Exception e){
			e.printStackTrace();
			throw new StorageException(Constant.FAILURE_MSG);
		}
	}

	/**
	 *  Checking file's correct format 
	 */
	
	private boolean checkFileFormat(Row row) {
		Iterator<Cell> cellIterator = row.cellIterator();
		boolean csvformat = false;
		while (cellIterator.hasNext()) {
			Cell cell = cellIterator.next();
			String stringColumn = null;

			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_STRING:
					stringColumn = cell.getStringCellValue();
				if (!ObjectsUtil.isNull(stringColumn) && (stringColumn.equals("User Name")
						|| stringColumn.equals("Mobile Number") || stringColumn.equals("Email ID")
						|| stringColumn.equals("Master Policy Holder") || stringColumn.equals("Seller Employee Code")
						|| stringColumn.equals("RAC Location Mapping") || stringColumn.equals("MLI Sales Manager")
						|| stringColumn.equals("MLI SM-Code") || stringColumn.equals("MLI RM") || stringColumn.equals("Status")
						|| stringColumn.equals("MLI RM Code") || stringColumn.equals("Role Id") )) {
						 csvformat = true;
					}else{
						logger.info("File format is invalid :"+stringColumn);
						return csvformat = false;
					}
					break;
		case Cell.CELL_TYPE_NUMERIC:
			return csvformat = false;
			}
		}
		return csvformat;
	}

	/**
	 * upload seller in bulk using excel file
	 * @param csvFile
	 * @return
	 * @throws IOException
	 * @throws Exception
	 * @author rajkumar
	 */
	@Override
	@Transactional
	public ResponseModel<?> updateSellerInBulk(MultipartFile csvFile) throws IOException, Exception {
		ResponseModel<?> response = new ResponseModel<>();
		try {
			List<Map<String, Object>> sellerContStatusMaps = new ArrayList<>();
			XSSFWorkbook myWorkBook = new XSSFWorkbook(csvFile.getInputStream());
			XSSFSheet worksheet = myWorkBook.getSheetAt(0);
			Iterator<Row> rowIterator = worksheet.iterator();
			Row header = null;
			
			// validate excel format and checked for duplicate Mobile Number in excel
			try {
				List<Long> mobileNumbers = new ArrayList<>(100);
				while (rowIterator.hasNext()) {
					Row row = rowIterator.next();
					if (row.getRowNum() == 0) {
						boolean fileFormat = checkFileFormat(row);
						if (!fileFormat) {
							response.setMessage(MLIMessageConstants.FILE_FORMAT);
							response.setStatus(SaveUpdateResponse.FAILURE);
							return response;
						}
						if (row.getRowNum() == 0) {
							header = row;
							continue;
						}
					}
					if (row.getCell(0) != null) {
						Double tmpMob = row.getCell(0).getNumericCellValue();
						mobileNumbers.add(tmpMob.longValue());
					}
				}
				Set<Long> uniqueMobileNumbers = new HashSet<>(mobileNumbers);
				logger.info(uniqueMobileNumbers.size() + " : " + mobileNumbers.size());
				if (uniqueMobileNumbers.size() != mobileNumbers.size()) {
					response.setMessage(MLIMessageConstants.EXCEP_DUPLI_MOBILE_NUMBER);
					response.setStatus(SaveUpdateResponse.FAILURE);
					return response;
				}
			} catch (Exception exception) {
				exception.printStackTrace();
				response.setMessage(Constant.FAILURE_MSG);
				response.setStatus(Constant.FAILURE);
				return response;
			}

			rowIterator = worksheet.iterator();
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				if (row.getRowNum() == 0) {
					// this is Header
					if (row.getRowNum() == 0) {
						continue;
					}
				}
				Map<String, Object> sellerContStatusMap = new HashMap<>();
				for (int cellIndex = 0; cellIndex < header.getLastCellNum(); cellIndex++) {
					Cell cell = row.getCell(cellIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
					String stringColumn = null;
					Integer columnValue = cell.getColumnIndex();
					Long longValue = 0l;
					switch (cell.getCellType()) {
					case Cell.CELL_TYPE_STRING:
						stringColumn = cell.getStringCellValue();
						break;
					case Cell.CELL_TYPE_NUMERIC:
						Double value = cell.getNumericCellValue();
						longValue = value.longValue();
						stringColumn = longValue.toString();
						break;
					case Cell.CELL_TYPE_BLANK:
						response.setStatus(SaveUpdateResponse.FAILURE);
						response.setMessage(
								String.format(MLIMessageConstants.CELL_NOT_EMPTY, row.getRowNum(), columnValue + 1));
						return response;
					}

					switch (columnValue) {
					case 0:
						if (!ObjectsUtil.isNull(stringColumn)) {
							if(!sellerService.checkSellerIfExist(Long.parseLong(stringColumn))){								
								response.setStatus(SaveUpdateResponse.FAILURE);
								response.setMessage("Seller does not exists with mobile number: "+Long.parseLong(stringColumn)+", at row no." + row.getRowNum());
								return response;
							}
							sellerContStatusMap.put(Constant.SELLER_CONTACT_NO,  Long.parseLong(stringColumn));
						} else {
							response.setMessage(String.format(MLIMessageConstants.TEN_DIGIT_CONTACT_NO, row.getRowNum(),
									columnValue + 1));
							response.setStatus(SaveUpdateResponse.FAILURE);
							return response;
						}
						break;
					case 1:
						if (!ObjectsUtil.isNull(stringColumn)) {
							sellerContStatusMap.put(Constant.SELLER_STATUS, getSellerStatus(stringColumn));
						} else {
							response.setMessage(
									String.format(MLIMessageConstants.EXCEL_STATUS, row.getRowNum(), columnValue + 1));
							response.setStatus(SaveUpdateResponse.FAILURE);
							return response;
						}
						break;
					}
				
				}
				sellerContStatusMaps.add(sellerContStatusMap);
			}
			
			for(Map<String, Object> tempSlrCont : sellerContStatusMaps) {
				SellerDetailEntity seller = sellerDAO
						.findByContactNo((Long) tempSlrCont.get(Constant.SELLER_CONTACT_NO));
				if(seller != null) {
				seller.setStatus((String) tempSlrCont.get(Constant.SELLER_STATUS));
				seller.setModifiedOn(DateUtil.toCurrentUTCTimeStamp());
				sellerDAO.saveOrUpdate(seller);
				} else {
					response.setMessage(
							"Seller with " + (Long) tempSlrCont.get(Constant.SELLER_CONTACT_NO) + " is not found");
					response.setStatus(SaveUpdateResponse.FAILURE);
					return response;
				}
			}
			response.setMessage(MLIMessageConstants.SELLET_BULK_UPFATED_SUCCESS);
			response.setStatus(SaveUpdateResponse.SUCCESS);
			return response; 
		} catch (StorageException e) {
			e.printStackTrace();
			logger.error("::::::::::::::::: Error in update SellerInBulk  :::::::::::::::" + e);
			throw new StorageException(MLIMessageConstants.FILE_FORMAT);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("::::::::::::::::: Error in update SellerInBulk  :::::::::::::::" + e);
			throw new StorageException(Constant.FAILURE_MSG);
		}
	}
	
	/**
	 * get status
	 * @param status
	 * @return
	 * @author rajkumar
	 */
	private String getSellerStatus(String status) {
		if (status.equalsIgnoreCase(Constant.SELLER_ACTIVE_STATUS)) {
			return Constant.SELLER_ACTIVE_STATUS;
		} else if (status.equalsIgnoreCase(Constant.SELLER_DEACTIVE_STATUS)) {
			return Constant.SELLER_DEACTIVE_STATUS;
		}
		return null;
	}
	
	
	/**
	 * update seller-status ,RAC Location mapping ,MLI Sales Manager ,MLI SM-Code, MLI RM ,MLI RM-Code in bulk
	 */
	@Override
	@Transactional
	public ResponseModel<?> editSellerInBulk(MultipartFile csvFile) throws IOException, Exception {
		ResponseModel<?> response = new ResponseModel<>();
		try {
			List<Map<String, Object>> sellerContStatusMaps = new ArrayList<>();
			XSSFWorkbook myWorkBook = new XSSFWorkbook(csvFile.getInputStream());
			XSSFSheet worksheet = myWorkBook.getSheetAt(0);
			Iterator<Row> rowIterator = worksheet.iterator();
			Row header = null;
			
			// validate excel format and checked for duplicate Mobile Number in excel
			try {
				List<Long> mobileNumbers = new ArrayList<>(100);
				while (rowIterator.hasNext()) {
					Row row = rowIterator.next();
					if (row.getRowNum() == 0) {
						boolean fileFormat = checkFileFormat(row);
						if (!fileFormat) {
							response.setMessage(MLIMessageConstants.FILE_FORMAT);
							response.setStatus(SaveUpdateResponse.FAILURE);
							return response;
						}
						if (row.getRowNum() == 0) {
							header = row;
							continue;
						}
					}
					if (row.getCell(0) != null) {
						Double tmpMob = row.getCell(0).getNumericCellValue();
						mobileNumbers.add(tmpMob.longValue());
					}
				}
				Set<Long> uniqueMobileNumbers = new HashSet<>(mobileNumbers);
				logger.info(uniqueMobileNumbers.size() + " : " + mobileNumbers.size());
				if (uniqueMobileNumbers.size() != mobileNumbers.size()) {
					response.setMessage(MLIMessageConstants.EXCEP_DUPLI_MOBILE_NUMBER);
					response.setStatus(SaveUpdateResponse.FAILURE);
					return response;
				}
			} catch (Exception exception) {
				exception.printStackTrace();
				response.setMessage(Constant.FAILURE_MSG);
				response.setStatus(Constant.FAILURE);
				return response;
			}

			rowIterator = worksheet.iterator();
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				if (row.getRowNum() == 0) {
					// this is Header
					if (row.getRowNum() == 0) {
						continue;
					}
				}
				Map<String, Object> sellerContStatusMap = new HashMap<>();
				for (int cellIndex = 0; cellIndex < header.getLastCellNum(); cellIndex++) {
					Cell cell = row.getCell(cellIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
					String stringColumn = null;
					Integer columnValue = cell.getColumnIndex();
					Long longValue = 0l;
					switch (cell.getCellType()) {
					case Cell.CELL_TYPE_STRING:
						stringColumn = cell.getStringCellValue();
						break;
					case Cell.CELL_TYPE_NUMERIC:
						Double value = cell.getNumericCellValue();
						longValue = value.longValue();
						stringColumn = longValue.toString();
						break;
					case Cell.CELL_TYPE_BLANK:
						response.setStatus(SaveUpdateResponse.FAILURE);
						response.setMessage(
								String.format(MLIMessageConstants.CELL_NOT_EMPTY, row.getRowNum(), columnValue + 1));
						return response;
					}

					switch (columnValue) {
					case 0:
						if (!ObjectsUtil.isNull(stringColumn)) {
							if(!sellerService.checkSellerIfExist(Long.parseLong(stringColumn))){								
								response.setStatus(SaveUpdateResponse.FAILURE);
								response.setMessage("Seller does not exists with mobile number: "+Long.parseLong(stringColumn)+", at row no." + row.getRowNum());
								return response;
							}
							sellerContStatusMap.put(Constant.SELLER_CONTACT_NO,  Long.parseLong(stringColumn));
						} else {
							response.setMessage(String.format(MLIMessageConstants.TEN_DIGIT_CONTACT_NO, row.getRowNum(),
									columnValue + 1));
							response.setStatus(SaveUpdateResponse.FAILURE);
							return response;
						}
						break;
					case 1:
						if (!ObjectsUtil.isNull(stringColumn)) {
							sellerContStatusMap.put(Constant.SELLER_STATUS, getSellerStatus(stringColumn));
						} else {
							response.setMessage(
									String.format(MLIMessageConstants.EXCEL_STATUS, row.getRowNum(), columnValue + 1));
							response.setStatus(SaveUpdateResponse.FAILURE);
							return response;
						}
						break;
					case 2:
						if (!ObjectsUtil.isNull(stringColumn)) {
							sellerContStatusMap.put(Constant.RAC_LOCATION_MAPPING,stringColumn);
						} else {
							response.setMessage(String.format(MLIMessageConstants.EXCEL_RAC_LOCATION_MAPPING ,row.getRowNum(), columnValue+1));
							response.setStatus(SaveUpdateResponse.FAILURE);
							return response;
						}
						break;
					case 3:
						if (!ObjectsUtil.isNull(stringColumn)) {
							sellerContStatusMap.put(Constant.MLI_SALES_MANAGER,stringColumn);
						} else {
							response.setMessage(String.format(MLIMessageConstants.EXCEL_MLI_SALES_MANAGER ,row.getRowNum(), columnValue+1));
							response.setStatus(SaveUpdateResponse.FAILURE);
							return response;
						}
						break;
					case 4:
						if (!ObjectsUtil.isNull(stringColumn)) {
							sellerContStatusMap.put(Constant.SM_CODE,stringColumn);
						} else {
							response.setMessage(String.format(MLIMessageConstants.EXCEL_SM_CODE ,row.getRowNum(), columnValue+1));
							response.setStatus(SaveUpdateResponse.FAILURE);
							return response;
						}
						break;
					case 5:
						if (!ObjectsUtil.isNull(stringColumn)) {
							sellerContStatusMap.put(Constant.MLI_RM,stringColumn);
						} else {
							response.setMessage(String.format(MLIMessageConstants.EXCEL_RM ,row.getRowNum(), columnValue+1));
							response.setStatus(SaveUpdateResponse.FAILURE);
							return response;
						}
						break;
					case 6:
						if (!ObjectsUtil.isNull(stringColumn)) {
							sellerContStatusMap.put(Constant.RM_CODE,stringColumn);
						} else {
							response.setMessage(String.format(MLIMessageConstants.EXCEL_RM_CODE ,row.getRowNum(), columnValue+1));
							response.setStatus(SaveUpdateResponse.FAILURE);
							return response;
						}
						break;
					}
				
				}
				sellerContStatusMaps.add(sellerContStatusMap);
			}
			int inactiveSeller = 0;
			int updatedSeller = 0;
			for(Map<String, Object> tempSlrCont : sellerContStatusMaps) {
				SellerDetailEntity seller = sellerDAO.findByContactNo((Long) tempSlrCont.get(Constant.SELLER_CONTACT_NO));
				if (!ObjectUtils.isEmpty(seller)) {
					String status = (String) tempSlrCont.get(Constant.SELLER_STATUS);
					if(Constant.SELLER_DEACTIVE_STATUS.equalsIgnoreCase(status)) {
						inactiveSeller++;
					}
					seller.setStatus(status);
					seller.setRacLocationMapping((String) tempSlrCont.get(Constant.RAC_LOCATION_MAPPING));
					seller.setMliSalesManager((String) tempSlrCont.get(Constant.MLI_SALES_MANAGER));
					seller.setMliSMCode((String) tempSlrCont.get(Constant.SM_CODE));
					seller.setMliRM((String) tempSlrCont.get(Constant.MLI_RM));
					seller.setMliRMCode((String) tempSlrCont.get(Constant.RM_CODE));
					seller.setModifiedOn(DateUtil.toCurrentUTCTimeStamp());
					sellerDAO.saveOrUpdate(seller);
					updatedSeller++;
				} else {
					response.setMessage("Seller with " + (Long) tempSlrCont.get(Constant.SELLER_CONTACT_NO) + " is not found");
					response.setStatus(SaveUpdateResponse.FAILURE);
					return response;
				}
			}
			List<SellerDetailEntity> activeSellerList = sellerDAO.findAllActiveSeller();
			int count = activeSellerList != null ? activeSellerList.size() : 0;
			StringBuilder builder = new StringBuilder();
			builder.append("Total number of active sellers present at backend = ")
			       .append(count+"\n")
			       .append("Total number of active sellers updated = ")
			       .append(updatedSeller);
			if(inactiveSeller > 1) {
				builder.append("\n Total number of sellers de-activated = ")
			           .append(inactiveSeller);
			}
			response.setMessage(builder.toString());
			response.setStatus(SaveUpdateResponse.SUCCESS);
			return response; 
		} catch (StorageException e) {
			e.printStackTrace();
			logger.error("::::::::::::::::: Error in update SellerInBulk  :::::::::::::::" + e);
			throw new StorageException(MLIMessageConstants.FILE_FORMAT);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("::::::::::::::::: Error in update SellerInBulk  :::::::::::::::" + e);
			throw new StorageException(Constant.FAILURE_MSG);
		}
	}
	
}
