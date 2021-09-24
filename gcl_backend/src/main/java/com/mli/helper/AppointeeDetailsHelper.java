package com.mli.helper;

import java.util.Collection;

import org.springframework.stereotype.Component;

import com.mli.entity.AppointeeDetailsEntity;
import com.mli.enums.RelationshipWithAssured;
import com.mli.model.AppointeeModel;
import com.mli.utils.DateUtil;

@Component
public class AppointeeDetailsHelper implements BaseConverter<AppointeeDetailsEntity, AppointeeModel> {

	@Override
	public AppointeeModel convertToModel(AppointeeDetailsEntity source) {
		if(source == null)
			return null;
		AppointeeModel appointeeModel = new AppointeeModel();
		appointeeModel.setAppointeeFirstName(source.getAppointeeFirstName());
		appointeeModel.setAppointeeLastName(source.getAppointeeLastName());
		appointeeModel.setGender(source.getGender());
		appointeeModel.setDateOfBirth(source.getDateOfBirth());
		appointeeModel.setRelationWithAssured(source.getRelationshipWithAssured().getLabel());
		if (RelationshipWithAssured.OTHERS.getLabel().equals(source.getRelationshipWithAssured().getLabel())) {
			appointeeModel.setRelationWithAppointee(source.getRelationWithAppointee());
		}
		return appointeeModel;
	}

	@Override
	public AppointeeDetailsEntity convertToEntity(AppointeeModel source) {
		AppointeeDetailsEntity entity = new AppointeeDetailsEntity();
		entity.setIsrelationWithAppointeeOthers(false);
		entity.setAppointeeFirstName(source.getAppointeeFirstName());
		entity.setAppointeeLastName(source.getAppointeeLastName());
		entity.setGender(source.getGender());
		entity.setDateOfBirth(source.getDateOfBirth());
		entity.setCreatedOn(DateUtil.toCurrentUTCTimeStamp());
		entity.setRelationshipWithAssured(
				RelationshipWithAssured.getRelationshipWithAssured(source.getRelationWithAssured()));
		if (RelationshipWithAssured.OTHERS.getLabel().equals(source.getRelationWithAssured())) {
			entity.setRelationWithAppointee(source.getRelationWithAppointee());
		}
		return entity;
	}

	@Override
	public Collection<AppointeeModel> convertToModelList(Collection<AppointeeDetailsEntity> source) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<AppointeeDetailsEntity> convertToEntityList(Collection<AppointeeModel> source) {
		// TODO Auto-generated method stub
		return null;
	}

}
