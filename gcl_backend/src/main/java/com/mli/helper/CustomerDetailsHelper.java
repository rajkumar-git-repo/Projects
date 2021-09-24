package com.mli.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.mli.constants.Constant;
import com.mli.dao.CamReportDetailsDao;
import com.mli.dao.CommonFileUploadDao;
import com.mli.dao.CovidReportDAO;
import com.mli.dao.CustomerDetailsDAO;
import com.mli.dao.ProposerDetailDao;
import com.mli.entity.CamReportDetailsEntity;
import com.mli.entity.CommonFileUploadEntity;
import com.mli.entity.CovidReportEntity;
import com.mli.entity.CustomerDetailsEntity;
import com.mli.entity.ProposerDetailEntity;
import com.mli.enums.LoanType;
import com.mli.enums.RelationshipGpPolicyHolder;
import com.mli.enums.SchemeType;
import com.mli.enums.Status;
import com.mli.modal.FileUploadModel;
import com.mli.model.CustomerDetailsModel;
import com.mli.model.ProposerDetailModel;
import com.mli.service.ReflexiveQuestionService;
import com.mli.utils.DateUtil;
import com.mli.utils.ObjectsUtil;

/**
 * @author Nikhilesh.Tiwari
 */
@Component
public class CustomerDetailsHelper implements BaseConverter<CustomerDetailsEntity, CustomerDetailsModel> {

	@Autowired
	CustomerDetailsDAO customerDetailsDAO;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private ProposerDetailDao proposerDetailDao;

	@Autowired
	private ReflexiveQuestionService reflexiveQuestionService;
	
	@Autowired
	private CamReportDetailsDao camReportDetailsDao;
	
	@Autowired
	private CommonFileUploadDao commonFileUploadDao;
	
	@Autowired
	private CovidReportDAO covidReportDAO;
	
	@Autowired
	private SFQHealthDeclarationHelper sfqHealthDeclarationHelper;

	@Override
	public CustomerDetailsModel convertToModel(CustomerDetailsEntity source) {
		if (source == null)
			return null;

		CustomerDetailsModel customerDetailsModel = new CustomerDetailsModel();
		customerDetailsModel.setCustomerDtlId(source.getCustomerDtlId());
		if (source.getLoanType() != null) {
			customerDetailsModel.setLoanType(source.getLoanType().getCode());
			customerDetailsModel.setLoanTypeDesc(source.getLoanType().getText());
		}
		if (source.getSchemeType() != null) {
			customerDetailsModel.setSchemeType(source.getSchemeType().getLabel());
		}
		if (source.getMasterPolicyHolderName() != null) {
			customerDetailsModel.setMasterPolicyholderName(source.getMasterPolicyHolderName());
		}
		customerDetailsModel.setLoanAmount(source.getLoanAmount());
		customerDetailsModel.setGender(source.getGender());
		customerDetailsModel.setCustomerFirstName(source.getCustomerFirstName());
		customerDetailsModel.setCustomerLastName(source.getCustomerLastName());
		customerDetailsModel.setLoanAppNumber(source.getLoanAppNumber());
		customerDetailsModel.setCustMobileNo(source.getCustMobileNo());
		customerDetailsModel.setCustEmailId(source.getCustEmailId());
		customerDetailsModel.setSumAssured(source.getSumAssured());
		if (source.getRelationshipGpPolicyHolder() != null) {
			customerDetailsModel.setRelationshipGpPolicyHolder(source.getRelationshipGpPolicyHolder().getLabel());
		}
		customerDetailsModel
				.setProposalNumber(!ObjectsUtil.isNull(source.getProposalNumber()) ? source.getProposalNumber() : null);
		customerDetailsModel.setLoanTenure(source.getLoanTenure());
		customerDetailsModel.setAppCompletionDate(source.getAppCompletionDate());
		customerDetailsModel.setDob(DateUtil.dateInFormatddmmyyyy(source.getDob()));
		customerDetailsModel.setAdhaarNumber(source.getAdhaarNumber());
		customerDetailsModel.setVerifiedOtp(source.getVerifiedOtp());
		if (source.getProposerEntity() != null) {
			customerDetailsModel
					.setProposerDetails(modelMapper.map(source.getProposerEntity(), ProposerDetailModel.class));
		}
		ProposerDetailEntity proposerDetailEntity = proposerDetailDao.findByCustomerDtlId(source.getCustomerDtlId());
		if (proposerDetailEntity != null) {
			customerDetailsModel.setIsProposer(Boolean.TRUE);
		} else {
			customerDetailsModel.setIsProposer(Boolean.FALSE);
		}
		/*String status = "";
		if (source.getStatus().equals(Status.APP_PENDING)) {
			status = Constant.WIP;
		} else if (source.getStatus().equals(Status.APP_SENT)) {
			status = Constant.SFV;
		} else if (source.getStatus().equals(Status.APP_VERIFIED)) {
			status = Constant.V;
		} else if (source.getStatus().equals(Status.APP_COMPLETE)) {
			status = "Completed";
		} else if (source.getStatus().equals(Status.APP_NOT_INTERESTED)) {
			status = Constant.NI;
		}*/
		
		customerDetailsModel.setStatus(getPrintedStatus(source.getStatus()));
		customerDetailsModel.setOtherPlace(source.getOtherPalce());
		customerDetailsModel.setNationality(source.getNationality());
		customerDetailsModel.setOccupation(source.getOccupation());
//		customerDetailsModel.setSavedAnswers(
//				reflexiveQuestionService.getAllAnswer(Constant.OCCUPATION, source.getCustomerDtlId(), null));
		customerDetailsModel.setSavedAnswers(
				reflexiveQuestionService.getAllAnswer(null, source.getCustomerDtlId(), null));
		
		customerDetailsModel.setSellerMobileNumber(source.getSlrDtlId().getContactNo());
		customerDetailsModel.setSelletName(source.getSlrDtlId().getSellerName());
		customerDetailsModel.setInterested(source.getIsInterested());
		customerDetailsModel.setIncrementalEMI(source.getIncrementalEMI());
		customerDetailsModel.setInterestRate(source.getInterestRate());
		customerDetailsModel.setReasonForNotInterested(source.getReseanForInterested());
		customerDetailsModel.setFinancePremium(source.getIsFinancePremium());
		customerDetailsModel.setTenureEligible(""+source.getTenureEligible());
		customerDetailsModel.setTentativeEMI(source.getTentativeEMI());
		customerDetailsModel.setMedicalRequired(source.getIsMedicalUnderWritingRequired());
		customerDetailsModel.setFinancialUWRequired(source.getIsFinancialUnderWritingRequired());
		customerDetailsModel.setVerifiedDate(source.getAppCompletionDate());
		customerDetailsModel.setCreatedOn(source.getCreatedOn());
		customerDetailsModel.setTotalPremium(""+source.getTotalPremium());
		customerDetailsModel.setModifiedOn(source.getModifiedOn());
		if (source.getSlrDtlId() != null) {
			customerDetailsModel.setSourceEmpCode(source.getSlrDtlId().getSourceEmpCode());
			customerDetailsModel.setMliSalesManager(source.getSlrDtlId().getMliSalesManager());
			customerDetailsModel.setMliRM(source.getSlrDtlId().getMliRM());
			customerDetailsModel.setRacLocationMapping(source.getSlrDtlId().getRacLocationMapping());
		}
		
		customerDetailsModel.setBaseOrBaseWithCI(ObjectsUtil.isNotNull(source.getBaseOrBaseWithCI()) ?  source.getBaseOrBaseWithCI() : null);
		customerDetailsModel.setPercentageOfSumAssured(ObjectsUtil.isNotNull(source.getPercentageOfSumAssured()) ?  source.getPercentageOfSumAssured() : null);
		customerDetailsModel.setCiOption(ObjectsUtil.isNotNull(source.getCiOption()) ?  source.getCiOption() : null);
		customerDetailsModel.setCiTenureYears(ObjectsUtil.isNotNull(source.getCiTenureYears()) ?  source.getCiTenureYears() : null);
		customerDetailsModel.setCiRiderSumAssured(source.getCiRiderSumAssured());
		customerDetailsModel.setCiRiderPermium(source.getCiRiderPermium());
		customerDetailsModel.setBaseWithCIPremium(source.getBaseWithCIPremium());
		customerDetailsModel.setMedicalUWCI(source.getMedicalUWCI());
		customerDetailsModel.setFinancialUWCI(source.getFinancialUWCI());
		customerDetailsModel.setOverallUWFinancial(source.getOverallUWFinancial());
		customerDetailsModel.setOverallUWMedical(source.getOverallUWMedical());
		customerDetailsModel.setTentativeEMIBaseWithCI(source.getTentativeEMIBaseWithCI());
		customerDetailsModel.setIncrementalEMIBaseWithCI(source.getIncrementalEMIBaseWithCI());
		
		 List<CamReportDetailsEntity> camReportList =camReportDetailsDao.getProposals(source.getProposalNumber());
		  if(camReportList!=null) {
			  customerDetailsModel.setCamReportUrls(camReportList);
		  }
		  customerDetailsModel.setStreet(source.getStreet());
		  customerDetailsModel.setAddress1(source.getAddress1());
		  customerDetailsModel.setAddress2(source.getAddress2());
		  customerDetailsModel.setAddress3(source.getAddress3());
		  customerDetailsModel.setPinCode(source.getPinCode());
		  customerDetailsModel.setTown(source.getTown());
		  customerDetailsModel.setState(source.getState());
		  customerDetailsModel.setCountry(source.getCountry());
		  customerDetailsModel.setFormStatus(source.getFormStatus());
		  customerDetailsModel.setHealthType(source.getHealthType() == null ? Constant.HEALTH_TYPE_DOGH:Constant.HEALTH_TYPE_SFQ);
		  customerDetailsModel.setRoId(source.getRoId());
		  customerDetailsModel.setSmId(source.getSmId());
		  
		  List<FileUploadModel>commonfileUpload=commonFileUploadDao.getProposalsNumber(source.getProposalNumber());
			if(!commonfileUpload.isEmpty())
				customerDetailsModel.setPhysicalFileList(commonfileUpload);
			
		  List<CovidReportEntity> covidReportList = covidReportDAO.findAllReport(source.getProposalNumber());
			if(!covidReportList.isEmpty())
				customerDetailsModel.setCovidReportList(sfqHealthDeclarationHelper.convertToCovidReportModelList(covidReportList));
		
		return customerDetailsModel;
	}

	@Override
	public CustomerDetailsEntity convertToEntity(CustomerDetailsModel source) {
		if (source == null)
			return null;

		CustomerDetailsEntity entity = new CustomerDetailsEntity();

		entity.setLoanType(LoanType.getLoanType(source.getLoanType()));
		entity.setSchemeType(SchemeType.getSchemeType(source.getSchemeType()));
		entity.setMasterPolicyHolderName(source.getMasterPolicyholderName());
		entity.setLoanAppNumber(source.getLoanAppNumber());
		entity.setCustMobileNo(source.getCustMobileNo());
		if (source.getCustEmailId() == null) {
			entity.setCustEmailId("");
		} else {
			entity.setCustEmailId(source.getCustEmailId());
		}
		entity.sethDFSignedDate(DateUtil.toMilliSecond());
		entity.setSumAssured(source.getSumAssured());
		entity.setRelationshipGpPolicyHolder(
				RelationshipGpPolicyHolder.getRelationshipGpPolicyHolder(source.getRelationshipGpPolicyHolder()));
		entity.setStatus(Status.APP_PENDING);
		entity.setAppStep(Status.STEP1);
		entity.setCustomerFirstName(source.getCustomerFirstName());
		entity.setCustomerLastName(source.getCustomerLastName());
		entity.setProposalNumber(!ObjectsUtil.isNull(source.getProposalNumber()) ? source.getProposalNumber() : null);
		entity.setDob(DateUtil.dateFormater(source.getDob()));
		entity.setLoanTenure(source.getLoanTenure());
		entity.setAdhaarNumber(source.getAdhaarNumber());
		entity.setVerifiedOtp(source.getVerifiedOtp());
		entity.setGender(source.getGender());
		entity.setBaseOrBaseWithCI(!ObjectsUtil.isNotNull(source.getBaseOrBaseWithCI()) ? source.getBaseOrBaseWithCI() : null);
		entity.setPercentageOfSumAssured(!ObjectsUtil.isNotNull(source.getPercentageOfSumAssured()) ? source.getPercentageOfSumAssured() : null);
		entity.setCiOption(!ObjectsUtil.isNotNull(source.getCiOption()) ?  source.getCiOption() : null);
		entity.setCiTenureYears(!ObjectsUtil.isNotNull(source.getCiTenureYears()) ?  source.getCiTenureYears() : null);
		if (ObjectsUtil.isNull(source.getProposalNumber())) {
			entity.setCreatedOn(DateUtil.toCurrentUTCTimeStamp());
		} else {
			entity.setModifiedOn(DateUtil.toCurrentUTCTimeStamp());
		}
		
		entity.setStreet(source.getStreet());
		entity.setAddress1(source.getAddress1());
		entity.setAddress2(source.getAddress2());
		entity.setAddress3(source.getAddress3());
		entity.setPinCode(source.getPinCode());
		entity.setTown(source.getTown());
		entity.setState(source.getState());
		entity.setCountry(source.getCountry());
		entity.setHealthType(source.getHealthType());
		/*
		 * if(!CollectionUtils.isEmpty(source.getCamReportUrls())) { String camUrls="";
		 * for (String camUrl : source.getCamReportUrls()) { camUrls = camUrls + ","+
		 * camUrl; } entity.setCamReportUrls(camUrls); }
		 */
		return entity;
	}

	/**
	 * @author Devendra.Kumar
	 */
	@Override
	public List<CustomerDetailsModel> convertToModelList(Collection<CustomerDetailsEntity> sources) {
		List<CustomerDetailsModel> models = new ArrayList<>(sources.size());
		sources.stream().forEach(source -> {
			CustomerDetailsModel customerDetailsModel = new CustomerDetailsModel();
			customerDetailsModel.setCustomerDtlId(source.getCustomerDtlId());
			customerDetailsModel.setLoanType(source.getLoanType().getCode());
			customerDetailsModel.setLoanTypeDesc(source.getLoanType().getText());
			customerDetailsModel.setSchemeType(source.getSchemeType().getLabel());
			if (source.getMasterPolicyHolderName() != null) {
				customerDetailsModel.setMasterPolicyholderName(source.getMasterPolicyHolderName());
			}
			customerDetailsModel.setCustomerFirstName(source.getCustomerFirstName());
			customerDetailsModel.setCustomerLastName(source.getCustomerLastName());
			customerDetailsModel.setLoanAppNumber(source.getLoanAppNumber());
			customerDetailsModel.setCustMobileNo(source.getCustMobileNo());
			customerDetailsModel.setCustEmailId(source.getCustEmailId());
			customerDetailsModel.setSumAssured(source.getSumAssured());
			if (source.getRelationshipGpPolicyHolder() != null) {
				customerDetailsModel.setRelationshipGpPolicyHolder(source.getRelationshipGpPolicyHolder().getLabel());
			}
			customerDetailsModel.setProposalNumber(
					!ObjectsUtil.isNull(source.getProposalNumber()) ? source.getProposalNumber() : null);
			customerDetailsModel.setLoanTenure(source.getLoanTenure());
			customerDetailsModel.setAppCompletionDate(source.getAppCompletionDate());
			customerDetailsModel.setDob(DateUtil.dateInFormatddmmyyyy(source.getDob()));
			customerDetailsModel.setAdhaarNumber(source.getAdhaarNumber());
			customerDetailsModel.setVerifiedOtp(source.getVerifiedOtp());
			
			  customerDetailsModel.setStreet(source.getStreet());
			  customerDetailsModel.setAddress1(source.getAddress1());
			  customerDetailsModel.setAddress2(source.getAddress2());
			  customerDetailsModel.setAddress3(source.getAddress3());
			  customerDetailsModel.setPinCode(source.getPinCode());
			  customerDetailsModel.setTown(source.getTown());
			  customerDetailsModel.setState(source.getState());
			  customerDetailsModel.setCountry(source.getCountry());
			  customerDetailsModel.setFormStatus(source.getFormStatus());
			  customerDetailsModel.setHealthType(source.getHealthType() == null ? Constant.HEALTH_TYPE_DOGH:Constant.HEALTH_TYPE_SFQ);
			  customerDetailsModel.setRoId(source.getRoId());
			  customerDetailsModel.setSmId(source.getSmId());
			  List<FileUploadModel>commonfileUpload=commonFileUploadDao.getProposalsNumber(source.getProposalNumber());
				if(!commonfileUpload.isEmpty())
					customerDetailsModel.setPhysicalFileList(commonfileUpload);
			  
			  List<CovidReportEntity> covidReportList = covidReportDAO.findAllReport(source.getProposalNumber());
			  if(!covidReportList.isEmpty())
				 customerDetailsModel.setCovidReportList(sfqHealthDeclarationHelper.convertToCovidReportModelList(covidReportList));
			
			
			/*
			 * if (source.getCamReportUrls() != null && source.getCamReportUrls() != "") {
			 * String[] camUrlList = source.getCamReportUrls().split(","); List<String>
			 * urlsList = new ArrayList<>(); for (String singleUrl : camUrlList) { if
			 * (singleUrl != null && singleUrl != "") { urlsList.add(singleUrl); } }
			 * customerDetailsModel.setCamReportUrls(urlsList); }
			 */
			
			models.add(customerDetailsModel);
		});
		return models;
	}

	@Override
	public Collection<CustomerDetailsEntity> convertToEntityList(Collection<CustomerDetailsModel> source) {
		// TODO Auto-generated method stub
		return null;
	}

	public String getPrintedStatus(Status status) {
		String printedStatus = "";
		if (status.equals(Status.APP_PENDING)) {
			printedStatus = Constant.WIP;
		} else if (status.equals(Status.APP_SENT)) {
			printedStatus = Constant.SFV;
		} else if (status.equals(Status.APP_VERIFIED)) {
			printedStatus = Constant.V;
		} else if (status.equals(Status.APP_COMPLETE)) {
			printedStatus = "Completed";
		} else if (status.equals(Status.APP_NOT_INTERESTED)) {
			printedStatus = Constant.NI;
		}else if (status.equals(Status.PHYSICAL_FORM_VERIFICATION)) {
			printedStatus = Constant.PHYSICAL_FORM_VERIFICATION;
		}else if (status.equals(Status.AFL_PHYSICAL_FORM_VERIFICATION)) {
			printedStatus = Constant.AFL_PHYSICAL_FORM_VERIFICATION;
		}
		return printedStatus;
	}
}
