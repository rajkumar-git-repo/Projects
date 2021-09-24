package com.mli.helper;

import java.util.Collection;

import org.springframework.stereotype.Component;

import com.mli.entity.MandatoryDeclarationEntity;
import com.mli.enums.MasterPolicyHolderName;
import com.mli.model.MandatoryDeclarationModel;
import com.mli.utils.DateUtil;

@Component
public class MandatoryDeclarationHelper
		implements BaseConverter<MandatoryDeclarationEntity, MandatoryDeclarationModel> {

	
	@Override
	public MandatoryDeclarationModel convertToModel(MandatoryDeclarationEntity source) {
		if(source == null)
			return null;
		MandatoryDeclarationModel mandatoryDeclarationModel = new MandatoryDeclarationModel();
		mandatoryDeclarationModel.setCreatedOn(DateUtil.toCurrentUTCTimeStamp());
		mandatoryDeclarationModel.setPolicyHolderName(source.getPolicyHolderName().getLabel());
		mandatoryDeclarationModel.setIsMandatoryDeclaration(source.getIsMandatoryDeclaration());
		mandatoryDeclarationModel.setSignedDate(DateUtil.toCurrentUTCTimeStamp());
		mandatoryDeclarationModel.setPlace(source.getPlace());
		return mandatoryDeclarationModel;
	}

	@Override
	public MandatoryDeclarationEntity convertToEntity(MandatoryDeclarationModel source) {
		MandatoryDeclarationEntity entity = new MandatoryDeclarationEntity();
		entity.setCreatedOn(DateUtil.toCurrentUTCTimeStamp());
		entity.setPolicyHolderName(MasterPolicyHolderName.getMasterPolicyHolder(source.getPolicyHolderName()));
		entity.setIsMandatoryDeclaration(source.getIsMandatoryDeclaration());
		entity.setSignedDate(DateUtil.toCurrentUTCTimeStamp());
		entity.setPlace(source.getPlace());
		return entity;
	}

	@Override
	public Collection<MandatoryDeclarationModel> convertToModelList(Collection<MandatoryDeclarationEntity> source) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<MandatoryDeclarationEntity> convertToEntityList(Collection<MandatoryDeclarationModel> source) {
		// TODO Auto-generated method stub
		return null;
	}

}
