package com.mli.helper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.mli.dao.CovidReportDAO;
import com.mli.entity.CovidReportEntity;
import com.mli.entity.SFQCovidEntity;
import com.mli.entity.SFQCovidTestEntity;
import com.mli.entity.SFQCovidVaccineEntity;
import com.mli.entity.SFQHealthDeclarationEntity;
import com.mli.model.CovidReportModel;
import com.mli.model.SFQCovidModel;
import com.mli.model.SFQCovidTestModel;
import com.mli.model.SFQCovidVaccineModel;
import com.mli.model.SFQHealthDeclarationModel;

@Component
public class SFQHealthDeclarationHelper {
	
	
	public SFQHealthDeclarationEntity updateSFQHealthEntity(SFQHealthDeclarationEntity sfqHealthDeclarationEntity ,SFQHealthDeclarationModel model) {
		if(model == null) {
			return null;
		}
		sfqHealthDeclarationEntity.setHeight(model.getHeight());
		sfqHealthDeclarationEntity.setWeight(model.getWeight());
		sfqHealthDeclarationEntity.setHealthFirstAnswer(model.getHealthFirstAnswer());
		sfqHealthDeclarationEntity.setHealthSecondAnswer(model.getHealthSecondAnswer());
		sfqHealthDeclarationEntity.setHealthThirdAnswer(model.getHealthThirdAnswer());
		sfqHealthDeclarationEntity.setHealthFourthAnswer(model.getHealthFourthAnswer());
		sfqHealthDeclarationEntity.setHealthFifthAnswer(model.getHealthFifthAnswer());
		sfqHealthDeclarationEntity.setHealthSixthAnswer(model.getHealthSixthAnswer());
		sfqHealthDeclarationEntity.setSmoker(model.isSmoker());
		sfqHealthDeclarationEntity.setSmokePerDay(model.getSmokePerDay());
		return sfqHealthDeclarationEntity;
	}

	public SFQHealthDeclarationEntity convertToEntity(SFQHealthDeclarationModel model) {
		if(model == null) {
			return null;
		}
		SFQHealthDeclarationEntity sfqHealthDeclarationEntity = new SFQHealthDeclarationEntity();
		sfqHealthDeclarationEntity.setHeight(model.getHeight());
		sfqHealthDeclarationEntity.setWeight(model.getWeight());
		sfqHealthDeclarationEntity.setHealthFirstAnswer(model.getHealthFirstAnswer());
		sfqHealthDeclarationEntity.setHealthSecondAnswer(model.getHealthSecondAnswer());
		sfqHealthDeclarationEntity.setHealthThirdAnswer(model.getHealthThirdAnswer());
		sfqHealthDeclarationEntity.setHealthFourthAnswer(model.getHealthFourthAnswer());
		sfqHealthDeclarationEntity.setHealthFifthAnswer(model.getHealthFifthAnswer());
		sfqHealthDeclarationEntity.setHealthSixthAnswer(model.getHealthSixthAnswer());
		sfqHealthDeclarationEntity.setSmoker(model.isSmoker());
		sfqHealthDeclarationEntity.setSmokePerDay(model.getSmokePerDay());
		return sfqHealthDeclarationEntity;
	}
	
	public SFQHealthDeclarationModel convertToModel(SFQHealthDeclarationEntity sfqHealthDeclarationEntity) {
		if(sfqHealthDeclarationEntity == null) {
			return null;
		}
		SFQHealthDeclarationModel model = new SFQHealthDeclarationModel();
		model.setHeight(sfqHealthDeclarationEntity.getHeight());
		model.setWeight(sfqHealthDeclarationEntity.getWeight());
		model.setHealthFirstAnswer(sfqHealthDeclarationEntity.getHealthFirstAnswer());
		model.setHealthSecondAnswer(sfqHealthDeclarationEntity.getHealthSecondAnswer());
		model.setHealthThirdAnswer(sfqHealthDeclarationEntity.getHealthThirdAnswer());
		model.setHealthFourthAnswer(sfqHealthDeclarationEntity.getHealthFourthAnswer());
		model.setHealthFifthAnswer(sfqHealthDeclarationEntity.getHealthFifthAnswer());
		model.setHealthSixthAnswer(sfqHealthDeclarationEntity.getHealthSixthAnswer());
		model.setSmoker(sfqHealthDeclarationEntity.isSmoker());
		model.setSmokePerDay(sfqHealthDeclarationEntity.getSmokePerDay());
		return model;
	}

	public SFQCovidEntity updateSFQCovidEntity(SFQCovidEntity sfqCovidEntity, SFQCovidModel sfqCovidModel) {
		if(sfqCovidModel == null) {
			return null;
		}
		sfqCovidEntity.setCovidFirstAnswer(sfqCovidModel.getCovidFirstAnswer());
		sfqCovidEntity.setCovidSecondAnswer(sfqCovidModel.getCovidSecondAnswer());
		sfqCovidEntity.setCovidThirdAnswer_a(sfqCovidModel.getCovidThirdAnswer_a());
		sfqCovidEntity.setCovidThirdAnswer_b(sfqCovidModel.getCovidThirdAnswer_b());
		sfqCovidEntity.getSfqCovidTestEntity().setCovidFirstAnswer(sfqCovidModel.getSfqCovidTestModel().getCovidFirstAnswer());
		sfqCovidEntity.getSfqCovidTestEntity().setCovidSecondAnswer(sfqCovidModel.getSfqCovidTestModel().getCovidSecondAnswer());
		sfqCovidEntity.getSfqCovidTestEntity().setCovidThirdAnswer(sfqCovidModel.getSfqCovidTestModel().getCovidThirdAnswer());
		sfqCovidEntity.getSfqCovidTestEntity().setCovidFourthAnswer(sfqCovidModel.getSfqCovidTestModel().getCovidFourthAnswer());
		sfqCovidEntity.getSfqCovidVaccineEntity().setVaccinated(sfqCovidModel.getSfqCovidVaccineModel().isVaccinated());
		sfqCovidEntity.getSfqCovidVaccineEntity().setFirstDoseDate(sfqCovidModel.getSfqCovidVaccineModel().getFirstDoseDate());
		sfqCovidEntity.getSfqCovidVaccineEntity().setSecondDoseDate(sfqCovidModel.getSfqCovidVaccineModel().getSecondDoseDate());
		sfqCovidEntity.getSfqCovidVaccineEntity().setVaccineName(sfqCovidModel.getSfqCovidVaccineModel().getVaccineName());
		sfqCovidEntity.getSfqCovidVaccineEntity().setDeclaration(sfqCovidModel.getSfqCovidVaccineModel().getDeclaration());
		return sfqCovidEntity;
	}

	public SFQCovidEntity getSFQCovidEntity(SFQCovidModel sfqCovidModel) {
		if(sfqCovidModel == null) {
			return null;
		}
		SFQCovidEntity sfqCovidEntity = new SFQCovidEntity();
		SFQCovidTestEntity sfqCovidTestEntity = new SFQCovidTestEntity();
		SFQCovidVaccineEntity sfqCovidVaccineEntity = new SFQCovidVaccineEntity();
		sfqCovidEntity.setCovidFirstAnswer(sfqCovidModel.getCovidFirstAnswer());
		sfqCovidEntity.setCovidSecondAnswer(sfqCovidModel.getCovidSecondAnswer());
		sfqCovidEntity.setCovidThirdAnswer_a(sfqCovidModel.getCovidThirdAnswer_a());
		sfqCovidEntity.setCovidThirdAnswer_b(sfqCovidModel.getCovidThirdAnswer_b());
		
		if (sfqCovidModel.getSfqCovidTestModel() != null) {
			sfqCovidTestEntity.setCovidFirstAnswer(sfqCovidModel.getSfqCovidTestModel().getCovidFirstAnswer());
			sfqCovidTestEntity.setCovidSecondAnswer(sfqCovidModel.getSfqCovidTestModel().getCovidSecondAnswer());
			sfqCovidTestEntity.setCovidThirdAnswer(sfqCovidModel.getSfqCovidTestModel().getCovidThirdAnswer());
			sfqCovidTestEntity.setCovidFourthAnswer(sfqCovidModel.getSfqCovidTestModel().getCovidFourthAnswer());
			sfqCovidEntity.setSfqCovidTestEntity(sfqCovidTestEntity);
		}
		
		if (sfqCovidModel.getSfqCovidVaccineModel() != null) {
			sfqCovidVaccineEntity.setVaccinated(sfqCovidModel.getSfqCovidVaccineModel().isVaccinated());
			sfqCovidVaccineEntity.setFirstDoseDate(sfqCovidModel.getSfqCovidVaccineModel().getFirstDoseDate());
			sfqCovidVaccineEntity.setSecondDoseDate(sfqCovidModel.getSfqCovidVaccineModel().getSecondDoseDate());
			sfqCovidVaccineEntity.setVaccineName(sfqCovidModel.getSfqCovidVaccineModel().getVaccineName());
			sfqCovidVaccineEntity.setDeclaration(sfqCovidModel.getSfqCovidVaccineModel().getDeclaration());
			sfqCovidEntity.setSfqCovidVaccineEntity(sfqCovidVaccineEntity);
		}
		return sfqCovidEntity;
	}
	
	public SFQCovidModel convertToModel(SFQCovidEntity sfqCovidEntity) {
		if(sfqCovidEntity == null) {
			return null;
		}
		SFQCovidModel sfqCovidModel = new SFQCovidModel();
		SFQCovidTestModel sfqCovidTestModel = new SFQCovidTestModel();
		SFQCovidVaccineModel sfqCovidVaccineModel = new SFQCovidVaccineModel();
		
		sfqCovidModel.setCovidFirstAnswer(sfqCovidEntity.getCovidFirstAnswer());
		sfqCovidModel.setCovidSecondAnswer(sfqCovidEntity.getCovidSecondAnswer());
		sfqCovidModel.setCovidThirdAnswer_a(sfqCovidEntity.getCovidThirdAnswer_a());
		sfqCovidModel.setCovidThirdAnswer_b(sfqCovidEntity.getCovidThirdAnswer_b());
		
		SFQCovidTestEntity sfqCovidTestEntity = sfqCovidEntity.getSfqCovidTestEntity();
		if (sfqCovidTestEntity != null) {
			sfqCovidTestModel.setCovidFirstAnswer(sfqCovidTestEntity.getCovidFirstAnswer());
			sfqCovidTestModel.setCovidSecondAnswer(sfqCovidTestEntity.getCovidSecondAnswer());
			sfqCovidTestModel.setCovidThirdAnswer(sfqCovidTestEntity.getCovidThirdAnswer());
			sfqCovidTestModel.setCovidFourthAnswer(sfqCovidTestEntity.getCovidFourthAnswer());
			sfqCovidModel.setSfqCovidTestModel(sfqCovidTestModel);
		}
		
		SFQCovidVaccineEntity sfqCovidVaccineEntity = sfqCovidEntity.getSfqCovidVaccineEntity();
		if (sfqCovidVaccineEntity != null) {
			sfqCovidVaccineModel.setVaccinated(sfqCovidVaccineEntity.isVaccinated());
			sfqCovidVaccineModel.setFirstDoseDate(sfqCovidVaccineEntity.getFirstDoseDate());
			sfqCovidVaccineModel.setSecondDoseDate(sfqCovidVaccineEntity.getSecondDoseDate());
			sfqCovidVaccineModel.setVaccineName(sfqCovidVaccineEntity.getVaccineName());
			sfqCovidVaccineModel.setDeclaration(sfqCovidVaccineEntity.getDeclaration());
			sfqCovidModel.setSfqCovidVaccineModel(sfqCovidVaccineModel);
		}
		return sfqCovidModel;
	}
	
	public List<CovidReportModel> convertToCovidReportModelList(List<CovidReportEntity> covidReportEntityList) {
		List<CovidReportModel> covidReportModelList = null;
		if(CollectionUtils.isEmpty(covidReportEntityList)) {
			covidReportModelList = new ArrayList<CovidReportModel>(0);
		}else {
			covidReportModelList = new ArrayList<CovidReportModel>(covidReportEntityList.size());
			for(CovidReportEntity covidReportEntity: covidReportEntityList) {
				CovidReportModel covidReportModel = new CovidReportModel();
				covidReportModel.setFileFolderPath(covidReportEntity.getFileFolderPath());
				covidReportModel.setFileName(covidReportEntity.getFileName());
				covidReportModel.setFileType(covidReportEntity.getFileType());
				covidReportModel.setFileUrl(covidReportEntity.getFileUrl());
				covidReportModel.setProposalNumber(covidReportEntity.getProposalNumber());
				covidReportModelList.add(covidReportModel);
			}
		}
		return covidReportModelList;
	}
}
