package com.mli.service;


import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.domain.Pageable;

import com.mli.entity.MasterLoanTypeBankEntity;
import com.mli.model.CustomerDetailsModel;
import com.mli.model.CustomerPhysicalFormModel;
import com.mli.model.PremiumCalculator;
import com.mli.model.SellerBankModel;
import com.mli.model.SellerDetailModel;
import com.mli.model.UserLoginModel;
import com.mli.model.response.ResponseModel;

/**
 * @author Nikhilesh.Tiwari
 *
 */
public interface SellerService {

	ResponseModel<UserLoginModel> sellerLogin(Long contNo) throws Exception;

	ResponseModel<UserLoginModel> verifyLoginOPT(Long contNo, Integer otp, String userType,String proposalNumber,String triggerScreen) throws Exception;

	ResponseModel<UserLoginModel> generateOTP(Long contNo, String userType, Boolean regenerateOtp, String proposalNumber,String triggerScreen) throws Exception;
	
	
	void saveOrUpdateSallerDetails(SellerDetailModel sellerDetailModel) throws Exception;
	
	SellerDetailModel getSellerDetailsById(Long id) throws Exception;

	List<SellerDetailModel> getAllSellerDetails() throws Exception;

	ResponseModel<UserLoginModel> sellerDtlAndDraftCount(Long contNo) throws Exception;
	
	List<SellerBankModel> getAllMasterPolicyHolders();
	boolean checkSellerIfExist (Long contactNo);

	Map<String, Object> getAllSellerDetailsByPageNumber(Pageable pageable , Long pattern) throws Exception;

	boolean checkOtherSellerIfExistForMobileUpdate(Long contactNo, Long sellerId);

	public Map<String, Object> saveByPremiumCalc(PremiumCalculator premiumCalculator);

	public List<CustomerDetailsModel> getDraftByContactNo(Long contactNo);
	
	public Boolean isSellerActive(Long contactPhone);
	
	public boolean getSellerInBulk();
	
	public Map<String, Object> savePremiumCalc(PremiumCalculator premiumCalculator);
	
	public Map<String, Object> savePhysicalForm(CustomerPhysicalFormModel customerPhysicalFormModel);
	
	public void updateSellerLastLogIn(Long contactNo);
	
	SellerDetailModel findByContantNumber (Long contactNo);
	
	Set<String> getAllSellerLoanType();

	Map<String, Object> getSellerRoles();

	Set<String> findLonaTypeById(String roleId);

	List<MasterLoanTypeBankEntity> getAllMasterLoanTypeList();

	
}
