package com.mli.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.mli.constants.Constant;
import com.mli.entity.LoanTypeSellerEntity;
import com.mli.entity.SellerBankEntity;
import com.mli.entity.SellerDetailEntity;
import com.mli.enums.MasterPolicyHolderName;
import com.mli.enums.Status;
import com.mli.model.LoanTypeSellerModal;
import com.mli.model.SellerBankModel;
import com.mli.model.SellerDetailModel;
import com.mli.utils.DateUtil;
import com.mli.utils.ObjectsUtil;

/**
 * @author Haripal.Chauhan
 */

@Component
public class SellerHelper implements BaseConverter<SellerDetailEntity, SellerDetailModel> {
	
	@Override
	public SellerDetailModel convertToModel(SellerDetailEntity source) {
		SellerDetailModel sellerDetailModel = new SellerDetailModel();
		sellerDetailModel.setSellerDtlId(source.getSellerDtlId());
		sellerDetailModel.setContactNo(source.getContactNo());
		sellerDetailModel.setSellerEmailId(source.getSellerEmailId());
		sellerDetailModel.setSellerName(source.getSellerName());
		sellerDetailModel.setConnector(source.getConnector());
		sellerDetailModel.setGroupPolicyNumber(source.getGroupPolicyNumber());
		sellerDetailModel.setSellerBankDetails(bankSellerEntityTobankSellerModel(source.getSellerBankEntity()));
		sellerDetailModel.setLoanTypeSellerModal(bankLaonTypeEntityTobankLoanSellerModel(source.getLoanTypeSellerEntity()));
		
		sellerDetailModel.setLoanTypes(getLoanTypeOfSeller(sellerDetailModel, source));
		/*if (source.getStatus() == null) {
			source.setStatus(Constant.SELLER_ACTIVE_STATUS);
		}*/
		if(source.getLastLogIn() != null) {
		  sellerDetailModel.setLastLogIn(source.getLastLogIn().toString());
		} else {
		  sellerDetailModel.setLastLogIn(DateUtil.toCurrentUTCTimeStamp().toString());
		}
		sellerDetailModel.setStatus(source.getStatus());
		sellerDetailModel.setSourceEmpCode(source.getSourceEmpCode());
		sellerDetailModel.setRacLocationMapping(source.getRacLocationMapping());
		sellerDetailModel.setMliSalesManager(source.getMliSalesManager());
		sellerDetailModel.setMliSMCode(source.getMliSMCode());
		sellerDetailModel.setMliRM(source.getMliRM());
		sellerDetailModel.setMliRMCode(source.getMliRMCode());
		sellerDetailModel.setCreatedOn(source.getCreatedOn());
		sellerDetailModel.setModifiedOn(source.getModifiedOn());
		sellerDetailModel.setRole(source.getRole());
		return sellerDetailModel;
	}

	@Override
	public SellerDetailEntity convertToEntity(SellerDetailModel source) {
		SellerDetailEntity entity = new SellerDetailEntity();
		entity.setSellerDtlId(source.getSellerDtlId());
		entity.setContactNo(source.getContactNo());
		entity.setSellerEmailId(source.getSellerEmailId());
		entity.setSellerName(source.getSellerName());
		entity.setConnector(source.getConnector());
		entity.setGroupPolicyNumber(source.getGroupPolicyNumber());
		entity.setSellerBankEntity(bankSellerModelToBankSellerEntity(source.getSellerBankDetails(), entity));	
		if(ObjectsUtil.isNull(source.getSellerDtlId())){
			entity.setIsPasswordSet(false);
		}
		if (ObjectsUtil.isNull(source.getSellerDtlId())) {
			entity.setCreatedOn(DateUtil.toCurrentUTCTimeStamp());
		} else {
			entity.setModifiedOn(DateUtil.toCurrentUTCTimeStamp());
		}
		/*entity.setStatus(source.getStatus());
		entity.setSourceEmpCode(source.getSourceEmpCode());
		entity.setRacLocationMapping(source.getRacLocationMapping());
		entity.setMliSalesManager(source.getMliSalesManager());
		entity.setMliSMCode(source.getMliSMCode());
		entity.setMliRM(source.getMliRM());
		entity.setMliRMCode(source.getMliRMCode());*/
		return entity;
	}

	public SellerDetailEntity convertToEntity(SellerDetailModel sellerDetailModel, SellerDetailEntity source) {
		if(source == null){
			source = new SellerDetailEntity();	
			}
		source.setSellerDtlId(source.getSellerDtlId());
		source.setContactNo(sellerDetailModel.getContactNo());
		source.setSellerEmailId(sellerDetailModel.getSellerEmailId());
		source.setSellerName(sellerDetailModel.getSellerName());
		source.setConnector(sellerDetailModel.getConnector());
		source.setGroupPolicyNumber(sellerDetailModel.getGroupPolicyNumber());
		source.setSellerBankEntity(bankSellerModelToBankSellerEntity(sellerDetailModel.getSellerBankDetails(), source));
		if(ObjectsUtil.isNull(source.getSellerDtlId())){
			source.setIsPasswordSet(false);
		}
		if (ObjectsUtil.isNull(source.getSellerDtlId())) {
			source.setCreatedOn(DateUtil.toCurrentUTCTimeStamp());
		} else {
			source.setModifiedOn(DateUtil.toCurrentUTCTimeStamp());
		}
//		source.setClientName(source.getClientName());
		source.setStatus(sellerDetailModel.getStatus());
		source.setSourceEmpCode(sellerDetailModel.getSourceEmpCode());
		source.setRacLocationMapping(sellerDetailModel.getRacLocationMapping());
		source.setMliSalesManager(sellerDetailModel.getMliSalesManager());
		source.setMliSMCode(sellerDetailModel.getMliSMCode());
		source.setMliRM(sellerDetailModel.getMliRM());
		source.setMliRMCode(sellerDetailModel.getMliRMCode());
		return source;
	}
	
	@Override
	public Collection<SellerDetailModel> convertToModelList(Collection<SellerDetailEntity> source) {
		List<SellerDetailModel> sellerDetailModels = new ArrayList<SellerDetailModel>();
		Set<SellerDetailEntity> sellerDetailEntities =  new HashSet<SellerDetailEntity>(source);
		for(SellerDetailEntity sellerDetailEntity : sellerDetailEntities){
			sellerDetailModels.add(this.convertToModel(sellerDetailEntity));			
		}
		return sellerDetailModels;
	}

	@Override
	public Collection<SellerDetailEntity> convertToEntityList(Collection<SellerDetailModel> source) {
		// TODO Auto-generated method stub
		return null;
	}

	//Convert BankSellerModel to BankSeller Set.
    //Currently only YES bank is active if bank is adding, we need to update MAsterPolicyHolder enum as well
	public Set<SellerBankEntity> bankSellerModelToBankSellerEntity(Set<SellerBankModel> sellerBankDetails, SellerDetailEntity sellerDetailEntity){
		Set<SellerBankEntity> sellerBanks= new HashSet<SellerBankEntity>();
		SellerBankEntity sellerBankEntity = null;
		if(sellerDetailEntity.getSellerBankEntity() != null){
		for (SellerBankEntity se : sellerDetailEntity.getSellerBankEntity()) {
			sellerBankEntity = se;
			break;
			}
		}
		if(sellerBankDetails != null) {
		for (SellerBankModel sellerBankModel : sellerBankDetails) {
			if (sellerBankEntity == null) {
				sellerBankEntity = new SellerBankEntity();
			}
			sellerBankEntity.setBankName(MasterPolicyHolderName.getMasterPolicyHolder(sellerBankModel.getBankName()));
			sellerBankEntity.setStatus(Status.ACTIVE);
			if (ObjectsUtil.isNull(sellerBankEntity.getSellerBankId())) {
				sellerBankEntity.setCreatedOn(DateUtil.toCurrentUTCTimeStamp());
			} else {
				sellerBankEntity.setModifiedOn(DateUtil.toCurrentUTCTimeStamp());
			}
			sellerBankEntity.setSellerDetailEntity(sellerDetailEntity);
			sellerBanks.add(sellerBankEntity);
			break;
		}
		}
		return sellerBanks;

	}

	public Set<SellerBankModel> bankSellerEntityTobankSellerModel(Set<SellerBankEntity> sellerBankEntities){
		Set<SellerBankModel> sellerBankDetails = new HashSet<SellerBankModel>();		
		for ( SellerBankEntity sellerBankEntity: sellerBankEntities) {
			SellerBankModel sellerBankModel = new SellerBankModel();
			sellerBankModel.setBankName(sellerBankEntity.getBankName().getLabel());
			sellerBankModel.setBankNameDesc(sellerBankEntity.getBankName().getDescription());
			sellerBankDetails.add(sellerBankModel);
		}		
		return sellerBankDetails;

	}
	
	
	public Set<LoanTypeSellerModal> bankLaonTypeEntityTobankLoanSellerModel(Set<LoanTypeSellerEntity> sellerLoanEntity){
		Set<LoanTypeSellerModal> loanTypeSellerModal = new HashSet<LoanTypeSellerModal>();	
		for ( LoanTypeSellerEntity loanEntity: sellerLoanEntity) {
			LoanTypeSellerModal loanTypeSeller = new LoanTypeSellerModal();
			if(loanEntity.getInterest()!=null) {
				String[] interst=loanEntity.getInterest().split(",");
				loanTypeSeller.setInterest(interst);
			}
			loanTypeSeller.setCiRider(loanEntity.getCiRider());
			loanTypeSeller.setCiRiderType(loanEntity.getCiRiderType());
			loanTypeSeller.setCoverType(loanEntity.getCoverType());
			loanTypeSeller.setLoanType(loanEntity.getLoanType());
			loanTypeSeller.setMph(loanEntity.getMph());
			loanTypeSeller.setPercentage(loanEntity.getPercentage());
			loanTypeSeller.setBalicPremimum(loanEntity.getBalicPremimum());
			loanTypeSeller.setLevelRates(loanEntity.getLevelRates());
			loanTypeSeller.setReducingCoverInterestRates(loanEntity.getReducingCoverInterestRates());
			loanTypeSeller.setReducingMaxRates(loanEntity.getReducingMaxRates());
			loanTypeSellerModal.add(loanTypeSeller);
		}		
		return loanTypeSellerModal;

	}
	
	
	
	public SellerDetailModel selletDtlWithBanks(SellerDetailEntity source) {
		SellerDetailModel sellerDetailModel = new SellerDetailModel();
		sellerDetailModel.setSellerName(source.getSellerName());
		sellerDetailModel.setSellerBankDetails(bankSellerDetails(source.getSellerBankEntity()));
		if(source.getLastLogIn()!=null) {
			sellerDetailModel.setLastLogIn(DateUtil.extractDateWithTSAsStringSlashFormate(source.getLastLogIn()));
		}
		return sellerDetailModel;
	}
	
	public Set<SellerBankModel> bankSellerDetails(Set<SellerBankEntity> sellerBankEntities){
		Set<SellerBankModel> sellerBankDetails = new HashSet<SellerBankModel>();		
		for ( SellerBankEntity sellerBankEntity: sellerBankEntities) {
			SellerBankModel sellerBankModel = new SellerBankModel();
			sellerBankModel.setBankName(sellerBankEntity.getBankName().getLabel());
			sellerBankModel.setBankNameDesc(sellerBankEntity.getBankName().getDescription());
			sellerBankDetails.add(sellerBankModel);
		}		
		return sellerBankDetails;

	}
	
	public String getMaskedContact(String contact) {
		return contact.substring(0, 3)+"XXXX"+contact.substring(7,10);
	}
	
	public void bankSellerLoanDetails(SellerBankEntity sellerBankEntities,String loanType){
		LoanTypeSellerEntity  loanTypeSellerEntity=new LoanTypeSellerEntity();
		
		sellerBankEntities.getBankName();
		
	}
	
	public Set<String> getLoanTypeOfSeller(SellerDetailModel sellerDetailModel,SellerDetailEntity source) {
		Set<String> set = new HashSet<String>();
		for(LoanTypeSellerEntity obj:source.getLoanTypeSellerEntity()) {
			set.add(obj.getLoanType());
		}
		
		return set;
		
	} 
	
	
	
	

}
