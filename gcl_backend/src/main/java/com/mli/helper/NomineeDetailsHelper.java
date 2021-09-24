package com.mli.helper;

import java.util.Collection;

import org.springframework.stereotype.Component;

import com.mli.entity.NomineeDetailsEntity;
import com.mli.enums.RelationshipWithAssured;
import com.mli.model.NomineeDetailsModel;
import com.mli.utils.DateUtil;

@Component
public class NomineeDetailsHelper implements BaseConverter<NomineeDetailsEntity, NomineeDetailsModel> {

	@Override
	public NomineeDetailsModel convertToModel(NomineeDetailsEntity source) {
		if(source == null)
			return null;
		NomineeDetailsModel nomineeDetailsModel = new NomineeDetailsModel();
		nomineeDetailsModel.setNomineeFirstName(source.getNomineeFirstName());
		nomineeDetailsModel.setNomineeLastName(source.getNomineeLastName());
		//nomineeDetailsModel.setIsAppointee(source.get);
		nomineeDetailsModel.setDateOfBirth(source.getDateOfBirth());
		nomineeDetailsModel.setGender(source.getGender());
		nomineeDetailsModel.setRelationWitHAssured(source.getRelationshipWithAssured().getLabel());		
		if (RelationshipWithAssured.OTHERS.getLabel().equals(source.getRelationshipWithAssured().getLabel())) {
			nomineeDetailsModel.setRelationWithNominee(source.getRelationWithNominee());
		}
		return nomineeDetailsModel;
	}

	@Override
	public NomineeDetailsEntity convertToEntity(NomineeDetailsModel source) {
		NomineeDetailsEntity entity = new NomineeDetailsEntity();
		entity.setNomineeFirstName(source.getNomineeFirstName());
		entity.setNomineeLastName(source.getNomineeLastName());
		entity.setDateOfBirth(source.getDateOfBirth());
		entity.setGender(source.getGender());
		entity.setAppointee(source.getIsAppointee());
		entity.setCreatedOn(DateUtil.toCurrentUTCTimeStamp());
		entity.setRelationshipWithAssured(
				RelationshipWithAssured.getRelationshipWithAssured(source.getRelationWitHAssured()));
		
		if (RelationshipWithAssured.OTHERS.getLabel().equals(source.getRelationWitHAssured())) {
			entity.setRelationWithNominee(source.getRelationWithNominee());
		}
		return entity;
	}

	@Override
	public Collection<NomineeDetailsModel> convertToModelList(Collection<NomineeDetailsEntity> source) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<NomineeDetailsEntity> convertToEntityList(Collection<NomineeDetailsModel> source) {
		// TODO Auto-generated method stub
		return null;
	}

}
