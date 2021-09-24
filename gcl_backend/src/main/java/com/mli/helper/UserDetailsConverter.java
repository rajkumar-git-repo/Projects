package com.mli.helper;

import org.springframework.util.CollectionUtils;

import com.mli.entity.AppointeeDetailsEntity;
import com.mli.entity.CustomerDetailsEntity;
import com.mli.entity.HealthDeclarationEntity;
import com.mli.entity.MandatoryDeclarationEntity;
import com.mli.entity.NomineeDetailsEntity;
import com.mli.enums.LoanType;
import com.mli.enums.MasterPolicyHolderName;
import com.mli.enums.RelationshipGpPolicyHolder;
import com.mli.enums.RelationshipWithAssured;
import com.mli.enums.SchemeType;
import com.mli.enums.Status;
import com.mli.model.AppointeeModel;
import com.mli.model.CustomerDetailsModel;
import com.mli.model.HealthAnswerModel;
import com.mli.model.HealthDeclarationModel;
import com.mli.model.MandatoryDeclarationModel;
import com.mli.model.NomineeDetailsModel;
import com.mli.utils.DateUtil;
import com.mli.utils.ObjectsUtil;

public class UserDetailsConverter {

	public static NomineeDetailsEntity toUpdateNomineeDetails(final NomineeDetailsEntity entity,
			final NomineeDetailsModel source) {
		entity.setNomineeFirstName(source.getNomineeFirstName());
		entity.setNomineeLastName(source.getNomineeLastName());
		entity.setDateOfBirth(source.getDateOfBirth());
		entity.setAppointee(source.getIsAppointee());
		entity.setGender(source.getGender());
		entity.setModifiedOn(DateUtil.toCurrentUTCTimeStamp());
		entity.setRelationshipWithAssured(
				RelationshipWithAssured.getRelationshipWithAssured(source.getRelationWitHAssured()));
		if (RelationshipWithAssured.OTHERS.getLabel().equals(source.getRelationWitHAssured())) {
			entity.setRelationWithNominee(source.getRelationWithNominee());
		}

		return entity;
	}

	public static AppointeeDetailsEntity toUpdateAppointeedetails(AppointeeDetailsEntity entity,
			AppointeeModel source) {
		entity.setAppointeeFirstName(source.getAppointeeFirstName());
		entity.setAppointeeLastName(source.getAppointeeLastName());
		entity.setGender(source.getGender());
		entity.setDateOfBirth(source.getDateOfBirth());
		entity.setModifiedOn(DateUtil.toCurrentUTCTimeStamp());
		entity.setRelationshipWithAssured(
				RelationshipWithAssured.getRelationshipWithAssured(source.getRelationWithAssured()));
		if (RelationshipWithAssured.OTHERS.getLabel().equals(source.getRelationWithAssured())) {
			entity.setRelationWithAppointee(source.getRelationWithAppointee());
		}
		return entity;
	}

	public static HealthDeclarationEntity toUpdateHealthDetails(HealthDeclarationEntity entity,
			HealthDeclarationModel source) {
		entity.setNegativeDeclaration(source.getNegativeDeclaration());
		entity.setHealthDeclaration(source.getIsHealthDeclaration());
		entity.setNegativeDeclaration(source.getNegativeDeclaration());
		entity.setIsApplication(source.getIsApplication());
		entity.setApplicationNumber(source.getApplicationNumber());
		entity.setOtherInsurance(source.getOtherInsurance());
		entity.setOtherHealthInfo(source.getOtherHealthInfo());
		entity.setModifiedOn(DateUtil.toCurrentUTCTimeStamp());
		if (!CollectionUtils.isEmpty(source.getCiRiderQuestionAns())) {
			String anslist = "";
			for (HealthAnswerModel healthAnswerModel : source.getCiRiderQuestionAns()) {
				if ("Yes".equalsIgnoreCase(healthAnswerModel.getQuestionAns())) {
					anslist = anslist + healthAnswerModel.getQuestionId() + ",";
				}
			}
			
			if(source.getCiHealthDecsAns() != null && source.getCiHealthDecsAns() !="") {
				entity.setCiHealthDecsAns(source.getCiHealthDecsAns());
			}
			entity.setHealthDeclarationForCIRider(anslist);
		}
		return entity;
	}

	public static MandatoryDeclarationEntity toUpdateMandatoryDetails(MandatoryDeclarationEntity entity,
			MandatoryDeclarationModel source) {
		entity.setModifiedOn(DateUtil.toCurrentUTCTimeStamp());
		entity.setPolicyHolderName(MasterPolicyHolderName.getMasterPolicyHolder(source.getPolicyHolderName()));
		entity.setIsMandatoryDeclaration(source.getIsMandatoryDeclaration());
		entity.setSignedDate(DateUtil.toCurrentUTCTimeStamp());
		entity.setPlace(source.getPlace());
		return entity;
	}

	public static CustomerDetailsEntity convertIntoCustomerDetailsEntity(CustomerDetailsModel source,
			CustomerDetailsEntity entity, String proposalNumber) {
		entity.setSchemeType(SchemeType.getSchemeType(source.getSchemeType()));
		entity.setMasterPolicyHolderName(source.getMasterPolicyholderName());
		entity.setLoanAppNumber(source.getLoanAppNumber());
		entity.setCustMobileNo(source.getCustMobileNo());
		entity.setCustEmailId(source.getCustEmailId());
		entity.sethDFSignedDate(DateUtil.toMilliSecond());
		if (source.getSumAssured() != null) {
			entity.setSumAssured(source.getSumAssured());
		}
		entity.setRelationshipGpPolicyHolder(RelationshipGpPolicyHolder.getRelationshipGpPolicyHolder(source.getRelationshipGpPolicyHolder()));
		entity.setStatus(Status.APP_PENDING);
		entity.setAppStep(Status.STEP1);
		entity.setCustomerFirstName(source.getCustomerFirstName());
		entity.setCustomerLastName(source.getCustomerLastName());
		entity.setProposalNumber(!ObjectsUtil.isNull(source.getProposalNumber()) ? source.getProposalNumber() : null);
		entity.setDob(DateUtil.dateFormater(source.getDob()));
		if (source.getLoanTenure() != null) {
			entity.setLoanTenure(source.getLoanTenure());
		}
		entity.setLoanType(LoanType.getLoanType(source.getLoanType()));
		entity.setAdhaarNumber(source.getAdhaarNumber());
		entity.setVerifiedOtp(source.getVerifiedOtp());
		if(!ObjectsUtil.isNull(proposalNumber)){
			entity.setProposalNumber(proposalNumber);
		}
	
		entity.setGender(source.getGender());
		
		entity.setStreet(source.getStreet());
		entity.setAddress1(source.getAddress1());
		entity.setAddress2(source.getAddress2());
		entity.setAddress3(source.getAddress3());
		entity.setPinCode(source.getPinCode());
		entity.setTown(source.getTown());
		entity.setState(source.getState());
		entity.setCountry(source.getCountry());
		entity.setHealthType(source.getHealthType());
		return entity;
	}

}