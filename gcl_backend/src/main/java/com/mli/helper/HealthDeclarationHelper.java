package com.mli.helper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import com.mli.entity.Covid19Entity;
import com.mli.entity.HealthDeclarationEntity;
import com.mli.model.Covid_19Model;
import com.mli.model.HealthAnswerModel;
import com.mli.model.HealthDeclarationModel;
import com.mli.service.ReflexiveQuestionService;
import com.mli.utils.DateUtil;

@Component
public class HealthDeclarationHelper implements BaseConverter<HealthDeclarationEntity, HealthDeclarationModel> {

	private static final Logger logger = Logger.getLogger(HealthDeclarationHelper.class);
	@Autowired
	private ReflexiveQuestionService reflexiveQuestionService;
	
	@Override
	public HealthDeclarationModel convertToModel(HealthDeclarationEntity source) {
		if(source == null)
			return null;
		HealthDeclarationModel healthDeclarationModel = new HealthDeclarationModel();
		healthDeclarationModel.setNegativeDeclaration(source.getNegativeDeclaration());
		healthDeclarationModel.setIsHealthDeclaration(source.isHealthDeclaration());
		healthDeclarationModel.setNegativeDeclaration(source.getNegativeDeclaration());
		healthDeclarationModel.setOtherInsurance(source.getOtherInsurance());
		healthDeclarationModel.setCreatedOn(source.getCreatedOn());
		healthDeclarationModel.setIsApplication(source.getIsApplication());
		healthDeclarationModel.setApplicationNumber(source.getApplicationNumber());
		healthDeclarationModel.setSavedAnswers(
				reflexiveQuestionService.getAllAnswer(null, source.getCustomerDtlId().getCustomerDtlId(), null));
		healthDeclarationModel.setOtherHealthInfo(source.getOtherHealthInfo());
		StringBuilder triggerMsg = new StringBuilder();
		if (source.getDiabetesTriggerMsg() != null && source.getHypertensionTriggerMsg() == null) {
			triggerMsg.append(source.getDiabetesTriggerMsg());
		} else if (source.getDiabetesTriggerMsg() == null && source.getHypertensionTriggerMsg() != null) {
			triggerMsg.append(source.getHypertensionTriggerMsg());
		} else if (source.getDiabetesTriggerMsg() != null && source.getHypertensionTriggerMsg() != null) {
			triggerMsg.append(source.getDiabetesTriggerMsg());
			triggerMsg.append(", ");
			triggerMsg.append(source.getHypertensionTriggerMsg());
		}
		healthDeclarationModel.setTriggerMsg(triggerMsg.toString());
		
		if (source.getHealthDeclarationForCIRider() != null && source.getHealthDeclarationForCIRider() !="" ) {
			//logger.info("==============start MandatoryDeclarationModel convertToModel=============");
			List<String> savedAns = new ArrayList<>();
			List<HealthAnswerModel> healthAnswerModelList = new ArrayList<>();
		//	logger.info("===source.getHealthDeclarationForCIRider()=======" +source.getHealthDeclarationForCIRider());
			String[] getList = source.getHealthDeclarationForCIRider().split(",");
		//	logger.info("=====getList  ======" +  getList);
			
			for (int i = 0; i < getList.length; i++) {
				HealthAnswerModel healthAnswerModel = new HealthAnswerModel();
				healthAnswerModel.setQuestionAns("Yes");
				if(!getList[i].equals("")) {
					if(getList[i].equals("7")) {
						healthAnswerModel.setCiRiderDsc(source.getCiHealthDecsAns());
					}
					healthAnswerModel.setQuestionId(Integer.parseInt(getList[i]));
					healthAnswerModelList.add(healthAnswerModel);
					savedAns.add(getList[i]);
				}
			}
			
			for (int i = 1; i < 8; i++) {
				if (!savedAns.contains(String.valueOf(i))) {
					HealthAnswerModel healthAnswerModel = new HealthAnswerModel();
					healthAnswerModel.setQuestionAns("No");
					healthAnswerModel.setQuestionId(i);
					healthAnswerModelList.add(healthAnswerModel);
				}
			}
			if(source.getCiHealthDecsAns() != null && source.getCiHealthDecsAns() != "") {
				healthDeclarationModel.setCiHealthDecsAns(source.getCiHealthDecsAns());
			}
			healthDeclarationModel.setCiRiderQuestionAns(healthAnswerModelList);
		}
		
		return healthDeclarationModel;
	}

	@Override
	public HealthDeclarationEntity convertToEntity(HealthDeclarationModel source) {
		if(source == null)
			return null;
		HealthDeclarationEntity entity = new HealthDeclarationEntity();
		entity.setNegativeDeclaration(source.getNegativeDeclaration());
		entity.setIsApplication(source.getIsApplication());
		entity.setApplicationNumber(source.getApplicationNumber());
		entity.setHealthDeclaration(source.getIsHealthDeclaration());
		entity.setNegativeDeclaration(source.getNegativeDeclaration());
		entity.setOtherInsurance(source.getOtherInsurance());
		entity.setOtherHealthInfo(source.getOtherHealthInfo());
		entity.setCreatedOn(DateUtil.toCurrentUTCTimeStamp());
		if (!CollectionUtils.isEmpty(source.getCiRiderQuestionAns())) {
			String anslist = "";
			for (HealthAnswerModel healthAnswerModel : source.getCiRiderQuestionAns()) {
				if ("yes".equalsIgnoreCase(healthAnswerModel.getQuestionAns())) {
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

	@Override
	public Collection<HealthDeclarationModel> convertToModelList(Collection<HealthDeclarationEntity> source) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<HealthDeclarationEntity> convertToEntityList(Collection<HealthDeclarationModel> source) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * convert Covid_19Model to Covid19Entity object
	 * @param covid_19Model
	 * @return
	 * @author rajkumar
	 */
	public Covid19Entity getCovid19Entity(Covid_19Model covid_19Model) {
		logger.info("Set covid model data into covid entity...");
		if(!ObjectUtils.isEmpty(covid_19Model)) {
			Covid19Entity covid19Entity = new Covid19Entity();
			covid19Entity.setCovidAnsOne(covid_19Model.getCovidAnsOne());
			covid19Entity.setCovidAnsTwo(covid_19Model.getCovidAnsTwo());
			covid19Entity.setCovidAnsThree_a(covid_19Model.getCovidAnsThree_a());
			covid19Entity.setCovidAnsThree_b(covid_19Model.getCovidAnsThree_b());
			covid19Entity.setCovidAnsFour(covid_19Model.getCovidAnsFour());
			covid19Entity.setCovidAnsFive(covid_19Model.getCovidAnsFive());
			covid19Entity.setCovidDeclaration(covid_19Model.getCovidDeclaration());
			return covid19Entity;
		}
		return null;
	}
	
	/**
	 * update {@link Covid19Entity} 
	 * @param covid19Entity
	 * @param covid_19Model
	 * @return
	 * @author rajkumar
	 */
	public Covid19Entity updateCovid19Entity(Covid19Entity covid19Entity,Covid_19Model covid_19Model) {
		logger.info("Set covid model data into covid entity...");
		if(!ObjectUtils.isEmpty(covid19Entity) && !ObjectUtils.isEmpty(covid_19Model)) {
			covid19Entity.setCovidAnsOne(covid_19Model.getCovidAnsOne());
			covid19Entity.setCovidAnsTwo(covid_19Model.getCovidAnsTwo());
			covid19Entity.setCovidAnsThree_a(covid_19Model.getCovidAnsThree_a());
			covid19Entity.setCovidAnsThree_b(covid_19Model.getCovidAnsThree_b());
			covid19Entity.setCovidAnsFour(covid_19Model.getCovidAnsFour());
			covid19Entity.setCovidAnsFive(covid_19Model.getCovidAnsFive());
			covid19Entity.setCovidDeclaration(covid_19Model.getCovidDeclaration());
		}
		return covid19Entity;
	}

	/**
	 * convert {@link Covid19Entity} into {@link Covid_19Model} object
	 * @param covid19Entity
	 * @return
	 * @author rajkumar
	 */
	public Covid_19Model getCovid_19Model(Covid19Entity covid19Entity) {
		logger.info("Set covid entity data into covid model...");
		if(!ObjectUtils.isEmpty(covid19Entity)) {
			Covid_19Model covid_19Model = new Covid_19Model();
			covid_19Model.setCovidAnsOne(covid19Entity.getCovidAnsOne());
			covid_19Model.setCovidAnsTwo(covid19Entity.getCovidAnsTwo());
			covid_19Model.setCovidAnsThree_a(covid19Entity.getCovidAnsThree_a());
			covid_19Model.setCovidAnsThree_b(covid19Entity.getCovidAnsThree_b());
			covid_19Model.setCovidAnsFour(covid19Entity.getCovidAnsFour());
			covid_19Model.setCovidAnsFive(covid19Entity.getCovidAnsFive());
			covid_19Model.setCovidDeclaration(covid19Entity.getCovidDeclaration());
			return covid_19Model;
		}
		return null;
	}
}
