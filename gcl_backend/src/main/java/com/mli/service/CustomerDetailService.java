package com.mli.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.itextpdf.text.DocumentException;
import com.mli.entity.CustomerDetailsEntity;
import com.mli.enums.Status;
import com.mli.modal.CamResponseModel;
import com.mli.modal.FileUploadModel;
import com.mli.model.CSModel;
import com.mli.model.CovidReportModel;
import com.mli.model.UserDetailsModel;
import com.mli.model.response.ResponseModel;

/**
 * @author Nikhilesh.Tiwari
 *
 */
public interface CustomerDetailService {

	ResponseModel<UserDetailsModel> saveCustomerDetails(UserDetailsModel userDetails, Integer steps,
			String proposalNumber) throws Exception;

	public UserDetailsModel getByProposalNumber(String proposalNumber);

	public List<UserDetailsModel> getProposalsByDate(Long Date) throws Exception;

	public List<UserDetailsModel> getPendingProposalsBySellerContNo(String contNo) throws Exception;

	public ResponseModel<UserDetailsModel> saveUploadFile(MultipartFile file, String proposalNumber)
			throws DocumentException, Exception;

	public ResponseModel<UserDetailsModel> saveFinalSubmit(String proposalNumber) throws Exception;

	public List<UserDetailsModel> getSubmitedProposalsByBankNdDate(Long date,
			String masterPolicyHolderName, Status appstatus) throws Exception;

	public List<Map<String, Object>> getByLoanAppNo(String loanAppNo);

	public UserDetailsModel getUSerDetailModelByCustomerDetail(CustomerDetailsEntity customerDetailsEntity);

	public Map<String,Object> uploadPasport(String proposalNumber,MultipartFile file1,MultipartFile file2);

	public Map<String,Object> search(Long slrContNo,String param, String status, Pageable pageable,String isFinance);

	public Map<String,Object> getStatus(String data);
	
	public List<UserDetailsModel> getAllCustomerForExcel(Long fron,Long to,String masterPlcy,Status appStatus);

	public ResponseModel<UserDetailsModel> getCustomerDetails(String data);

	public ResponseModel<?> editReflexiveQuestion(CSModel csModel);
	
	public boolean getCustomerInBulk(String from,String to);

	public ResponseModel<List<CamResponseModel>> saveCamFiles(List<MultipartFile> files, String proposalNumber,String sellerId);

	public List<CamResponseModel> deleteAdditionalFile(String fileUrl,String propsalNo);
	
	public ResponseModel<List<FileUploadModel>> savePhysicalFormFile(List<MultipartFile> files, String proposalNumber,String sellerId,String roId, String smId);

	public List<FileUploadModel> deletePhysicalFormFile(String fileUrl,String propsalNo);

	boolean checkAgeGreaterThan18(String age);

	String genearteProposalNumber() throws Exception;

	List<Map<String, Object>> searchYBLCCCustomer(String param);

	ResponseModel<List<CovidReportModel>> uploadCovidReport(List<MultipartFile> files, String proposalNumber, String fileType);
	

}
