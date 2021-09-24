package com.mli.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mli.constants.Constant;
import com.mli.dao.MasterOptionDao;
import com.mli.dao.MasterReflexiveQuestionDao;
import com.mli.dao.MasterReflexiveQuestionSubTypeDao;
import com.mli.dao.MasterReflexiveQuestionTypeDao;
import com.mli.dao.ReflexiveAnswerDao;
import com.mli.entity.MasterOptionEntity;
import com.mli.entity.MasterReflexiveQuestionEntity;
import com.mli.entity.MasterReflexiveQuestionSubTypeEntity;
import com.mli.entity.MasterReflexiveQuestionTypeEntity;
import com.mli.entity.ReflexiveAnswerEntity;
import com.mli.entity.SelectOptionEntity;
import com.mli.model.MasterReflexiveQuestionSubTypeModel;
import com.mli.model.RQModel;
import com.mli.model.RQSubTypeModel;
import com.mli.model.RQTypeModel;
import com.mli.service.ReflexiveQuestionService;
import com.mli.utils.CustomParameterizedException;
import com.mli.utils.DateUtil;

/**
 * 
 * @author Devendra.Kumar
 *
 */
@Service
public class ReflexiveQuestionServiceImpl implements ReflexiveQuestionService {

	private static final Logger logger = LoggerFactory.getLogger(ReflexiveQuestionServiceImpl.class);

	@Autowired
	private MasterReflexiveQuestionDao reflexiveQuestionDao;
	
	@Autowired
	private MasterReflexiveQuestionSubTypeDao subTypeDao;

	@Autowired
	private MasterReflexiveQuestionTypeDao masterRQTypeDao;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@Autowired
	private MasterOptionDao masterOptionDao;
	
	@Autowired
	private ReflexiveAnswerDao reflexiveAnswerDao;
	
	private static final String NATIONALITY = "NATIONALITY";
	private static final String OCCUPATION = "OCCUPATION";
	private static final String DISEASE = "DISEASE";
	
	private static final String LISTING_TYPE = "LISTING_TYPE";
	private static final String REMARK_TYPE = "REMARK_TYPE";
	private static final String DESCRIPTON_TYPE = "DESCRIPTON_TYPE";
	private static final String YES_NO_TYPE = "YES_NO_TYPE";
	private static final String MULTI_SELECT_TYPE = "MULTIPLE_SELECTION_TYPE";
	private static final String DATE_TYPE = "DATE_TYPE";
	
	private static final String REFLEXIVE_QUESTION_SERVICE = "ReflexiveQuestionService";
	
//	@Autowired
//	private SessionFactory sessionFactory;
//
//	public Session getSession() {
//		return sessionFactory.getCurrentSession();
//	}

	/**
	 * To save Master Reflexive Question
	 * 
	 * @author Devendra.Kumar
	 * @param rqModel
	 * @return String
	 */
	@Transactional
	@Override
	public String save(RQModel rqModel) {
		try {
			boolean validQuestType = validateQType(rqModel.getqType());
			if (!validQuestType) {
				logger.info("QuestionType : {}",rqModel.getqType());
				throw new CustomParameterizedException(REFLEXIVE_QUESTION_SERVICE,
						"Invalid QuestionType : " + rqModel.getqType());
			}

			MasterReflexiveQuestionEntity entity = new MasterReflexiveQuestionEntity();
			entity.setQuestion(rqModel.getQuestion());
			entity.setCreatedOn(DateUtil.toCurrentUTCTimeStamp());
			entity.setToolTipDescription(rqModel.getToolTipDescription());
			entity.setIsToolTip(rqModel.getIsToolTip());
			MasterReflexiveQuestionSubTypeEntity subType = subTypeDao.findSubTypeId(rqModel.getSubType());
			if (subType != null) {
				if ("false".equalsIgnoreCase(subType.getIsMendatory())) {
					logger.info("Mendatory flag is :{}",subType.getIsMendatory());
					throw new CustomParameterizedException(REFLEXIVE_QUESTION_SERVICE,
							"Mendatory flag is " + subType.getIsMendatory() + " for " + subType.getSubTypeValue());
				}
				entity.setSubTypeEntity(subType);
			} else {
				logger.info("Subtype not found for id : {}",rqModel.getSubType());
				throw new CustomParameterizedException(REFLEXIVE_QUESTION_SERVICE,
						"Subtype not found for id : " + rqModel.getSubType());
			}
			if (rqModel.getqType() != null && !rqModel.getqType().trim().isEmpty()) {
				entity.setqType(rqModel.getqType());
			}
			if (LISTING_TYPE.equalsIgnoreCase(rqModel.getqType())
					|| MULTI_SELECT_TYPE.equalsIgnoreCase(rqModel.getqType())) {
				if (rqModel.getOptions() != null && !rqModel.getOptions().isEmpty()) {
					List<MasterOptionEntity> options = new ArrayList<>(rqModel.getOptions().size());
					for (String temoOption : rqModel.getOptions()) {
						MasterOptionEntity optionEntity = new MasterOptionEntity();
						optionEntity.setValue(temoOption);
						optionEntity.setCreatedOn(DateUtil.toCurrentUTCTimeStamp());
						optionEntity.setQuestionEntity(entity);
						options.add(optionEntity);
					}
					entity.setOptions(options);
				} else {
					logger.info("qType is :{}",rqModel.getqType());
					throw new CustomParameterizedException(REFLEXIVE_QUESTION_SERVICE,
							"Must be enter Options because qType is :" + rqModel.getqType());
				}
			}
			reflexiveQuestionDao.save(entity);
			return "Reflexive Question Saved successfully";
		} catch (CustomParameterizedException exception) {
			logger.error("Exception occured while saving Reflexive Question :{}", exception);
			throw exception;
		} catch (Exception exception) {
			logger.error("Exception occured while saving Reflexive Question :{}", exception);
			throw new CustomParameterizedException(REFLEXIVE_QUESTION_SERVICE,
					"Exception occured while saving Reflexive Question", exception);
		}
	}

	/**
	 * qType :  LISTING_TYPE
	 * 			REMARK_TYPE
	 * 			DESCRIPTON_TYPE
	 * 			LISTING_TYPE
	 * 			MULTI_SELECT_TYPE
	 * 			DATE_TYPE
	 * 
	 * @return
	 */
	private boolean validateQType(String qType) {
		boolean validQTypeFlag = false;
		if (LISTING_TYPE.equals(qType) || REMARK_TYPE.equalsIgnoreCase(qType) || DESCRIPTON_TYPE.equalsIgnoreCase(qType)
				|| YES_NO_TYPE.equalsIgnoreCase(qType) || MULTI_SELECT_TYPE.equalsIgnoreCase(qType) || DATE_TYPE.equalsIgnoreCase(qType)) {
			validQTypeFlag = true;
		}
		return validQTypeFlag;
	}
	
	/**
	 * type : NATIONALITY
	 * 		  OCCUPATION
	 * 		  DISEASE
	 * 
	 * @param type
	 * @return
	 */
	private boolean validateType(String type) {
		boolean validTypeFlag = false;
		if(NATIONALITY.equalsIgnoreCase(type) || OCCUPATION.equalsIgnoreCase(type) || DISEASE.equalsIgnoreCase(type)) {
			validTypeFlag = true;
		}
		return validTypeFlag;
	}
	
	
	@Transactional
	@Override
	public RQModel getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Transactional
	@Override
	public RQModel getById(String id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Save Question type and subType.
	 * 
	 * @author Devendra.Kumar
	 */
	@Transactional
	@Override
	public Object saveQuestionTypes(RQTypeModel rqTypeModel) {
		try {
			boolean validType = validateType(rqTypeModel.getRqType());
			if(!validType) {
				logger.info("rqType : {}",rqTypeModel.getRqType());
				throw new CustomParameterizedException(REFLEXIVE_QUESTION_SERVICE,
						"Invalid rqType"+rqTypeModel.getRqType());
			}
			
			List<MasterReflexiveQuestionSubTypeEntity> subTypeEntities = new ArrayList<>(
					rqTypeModel.getRqSubType().size());
			MasterReflexiveQuestionTypeEntity entity = new MasterReflexiveQuestionTypeEntity();
			MasterReflexiveQuestionTypeEntity rqTypeData = masterRQTypeDao.findTypeByRQType(rqTypeModel.getRqType());
			if (rqTypeData == null) {
				entity.setTypeValue(rqTypeModel.getRqType());
				entity.setCreatedOn(DateUtil.toCurrentUTCTimeStamp());
				rqTypeModel.getRqSubType().stream().forEach(subType -> {
					MasterReflexiveQuestionSubTypeEntity temp = new MasterReflexiveQuestionSubTypeEntity();
					temp.setToolTipDescription(subType.getToolTipDescription());
					temp.setIsToolTip(subType.getIsToolTip());
					temp.setSubTypeValue(subType.getRqSubType());
					temp.setIsMendatory(subType.getIsMendatory());
					temp.setCreatedOn(DateUtil.toCurrentUTCTimeStamp());
					temp.setTypeEntity(entity);
					subTypeEntities.add(temp);
				});
				entity.setSubTypeId(subTypeEntities);
				masterRQTypeDao.saveOrUpdate(entity);
				return "Saved Successfully";
			} else {
				List<MasterReflexiveQuestionSubTypeEntity> subTypes = rqTypeData.getSubTypeId();
				for (RQSubTypeModel subTypeModel : rqTypeModel.getRqSubType()) {
					MasterReflexiveQuestionSubTypeEntity tempSubType = new MasterReflexiveQuestionSubTypeEntity();
					tempSubType.setIsMendatory(subTypeModel.getIsMendatory());
					tempSubType.setToolTipDescription(subTypeModel.getToolTipDescription());
					tempSubType.setIsToolTip(subTypeModel.getIsToolTip());
					tempSubType.setSubTypeValue(subTypeModel.getRqSubType());
					tempSubType.setCreatedOn(DateUtil.toCurrentUTCTimeStamp());
					tempSubType.setTypeEntity(rqTypeData);
					subTypeEntities.add(tempSubType);
				}
				subTypes.addAll(subTypeEntities);
				rqTypeData.setSubTypeId(subTypes);
				rqTypeData.setModifiedOn(DateUtil.toCurrentUTCTimeStamp());
				masterRQTypeDao.saveOrUpdate(rqTypeData);
				return "Updated Successfully";
			}
		} catch (CustomParameterizedException exception) {
			logger.error("Exception occured while saving question type : {}", exception);
			throw exception;
		} catch (Exception exception) {
			logger.error("Exception occured while saving question type : {}", exception);
			throw new CustomParameterizedException(REFLEXIVE_QUESTION_SERVICE,
					"Exception occured while saving question type  ", exception);
		}
	}

	/**
	 * To get Question Type detail
	 * 
	 * @author Devendra.Kumar
	 * @param
	 */
	@Transactional
	@Override
	public Object getQuestionTypes(String type) {
		try {
			if (type == null || type.isEmpty()) {
				List<Map<String, Object>> map = new ArrayList<>(3);
				masterRQTypeDao.getAll().stream().forEach(tempType -> {
					Map<String, Object> temp = new HashMap<>(1);
					temp.put("id", tempType.getId());
					temp.put("value", tempType.getTypeValue());
					map.add(temp);
				});
				return map;
			} else {
				MasterReflexiveQuestionTypeEntity rqTypeData = masterRQTypeDao.findTypeByRQType(type);
				MasterReflexiveQuestionSubTypeEntity otherOccupation = new MasterReflexiveQuestionSubTypeEntity();
				if (rqTypeData != null) {
					if(Constant.OCCUPATION.equalsIgnoreCase(type)) {
						//sort
						for (MasterReflexiveQuestionSubTypeEntity tempSubtype : rqTypeData.getSubTypeId()) {
							if (tempSubtype.getSubTypeValue().equalsIgnoreCase("Others")) {
								otherOccupation = tempSubtype;
								break;
							}
						}
						rqTypeData.getSubTypeId().parallelStream().forEach(s->{});
						List<MasterReflexiveQuestionSubTypeEntity> sortedOccupation = rqTypeData.getSubTypeId().stream().sorted(Comparator.comparing(MasterReflexiveQuestionSubTypeEntity::getSubTypeValue)).filter(s->!s.getSubTypeValue().equals("Others")).collect(Collectors.toList());
						sortedOccupation.add(otherOccupation);
						rqTypeData.setSubTypeId(sortedOccupation);
					}
					
					List<MasterReflexiveQuestionSubTypeEntity> subType = rqTypeData.getSubTypeId();
					MasterReflexiveQuestionSubTypeModel[] subTypeArray = modelMapper.map(subType,
							MasterReflexiveQuestionSubTypeModel[].class);
					return Arrays.asList(subTypeArray);
				} else {
					return new ArrayList<>();
				}
			}
		} catch (Exception exception) {
			logger.error("Exception occured while getting Question Types : {}",exception);
			throw new CustomParameterizedException(REFLEXIVE_QUESTION_SERVICE,
					"Exception occured while getting Question Types ", exception);
		}
	}

	@Transactional
	@Override
	public Object getAllQuestion(String stepNo) {/*
		List<MasterReflexiveQuestionEntity> question = null;
		if (stepNo == null) {
			question = reflexiveQuestionDao.getAll();
		}
		Criteria criteria = getSession().createCriteria(MasterReflexiveQuestionEntity.class);
		Criteria subTypeCriteria = criteria.createCriteria("subTypeEntity");
		Criteria typeCriteria = subTypeCriteria.createCriteria("typeEntity");
		if ("1".equals(stepNo)) {
			typeCriteria.add(Restrictions.eq("typeValue", "NATIONALITY"));
		} else if ("2".equals(stepNo)) {
			typeCriteria.add(Restrictions.eq("typeValue", "OCCUPATION"));
		}
		question = typeCriteria.list();
		MasterReflexiveQuestionModel[] questionArray = modelMapper.map(question, MasterReflexiveQuestionModel[].class);
		return Arrays.asList(questionArray);*/
		return null;
	}

	/**
	 * Get All Customer answer on the basis of given custId
	 * 
	 * @author Devendra.Kumar
	 * @param type Ex : OCCUPATION / NATIONALITY / DISEASE
	 */
	@Override
	@Transactional
	public Object getAllAnswer(String type, Long custId, String proposerNo) {
		try {
			List<ReflexiveAnswerEntity> customerAnswer = null;
			if (custId != null && type != null && !type.isEmpty()) {
				customerAnswer = reflexiveAnswerDao.findByCustomerIdAndType(custId, type);
			} else if (custId != null && (type == null || type.isEmpty())) {
				customerAnswer = reflexiveAnswerDao.findByCustomerId(custId);
			} else {
				customerAnswer = reflexiveAnswerDao.getAll();
			}

			List<Map<String, Object>> result = new ArrayList<>();
			for (ReflexiveAnswerEntity answerEntity : customerAnswer) {
				Map<String, Object> tempMap = new HashMap<>();
				String questIdAndCustId = answerEntity.getCustIdQuestId();
				Long compositeKey[] = splitFromUnderscore(questIdAndCustId);
				MasterReflexiveQuestionEntity questionEntity = reflexiveQuestionDao
						.getEntity(MasterReflexiveQuestionEntity.class, compositeKey[1]);
				if (questionEntity != null) {
					tempMap.put("subType", questionEntity.getSubTypeEntity().getSubTypeValue());
					tempMap.put("type", questionEntity.getSubTypeEntity().getTypeEntity().getTypeValue());
					tempMap.put("questionId", questionEntity.getId());
					tempMap.put("qType", questionEntity.getqType());
					tempMap.put("question", questionEntity.getQuestion());
					tempMap.put("qTootipDescription", questionEntity.getToolTipDescription());
					tempMap.put("isTooltip", questionEntity.getIsToolTip());

					List<MasterOptionEntity> masterOptions = questionEntity.getOptions();
					if (masterOptions != null) {
						List<Map<String, Object>> masterOptionList = new ArrayList<>();
						masterOptions.stream().forEach(selected -> {
							Map<String, Object> tempOption = new HashMap<>();
							tempOption.put("id", selected.getId());
							tempOption.put("value", selected.getValue());
							masterOptionList.add(tempOption);
						});
						tempMap.put("masterOptions", masterOptionList);
					}
				}
				tempMap.put("answerId", answerEntity.getId());
				tempMap.put("flag", answerEntity.getFlag());
				tempMap.put("description", answerEntity.getDescription());
//				tempMap.put("fileUploadPath", answerEntity.getCustomerDetailsEntity().getFileUploadPath());
//				tempMap.put("customerId", answerEntity.getCustomerDetailsEntity().getCustomerDtlId());
				List<SelectOptionEntity> selectOptionEntities = answerEntity.getSelectOptionEntities();
				if (selectOptionEntities != null) {
					List<Map<String, Object>> optionMapList = new ArrayList<>();
					for (SelectOptionEntity selected : selectOptionEntities) {
						compositeKey = splitFromUnderscore(selected.getCustIdMasterOpId());
						MasterOptionEntity tempMasterOption = masterOptionDao.getEntity(MasterOptionEntity.class,
								compositeKey[1]);
						if (tempMasterOption != null) {
							Map<String, Object> tempOption = new HashMap<>();
							tempOption.put("id", tempMasterOption.getId());
							tempOption.put("value", tempMasterOption.getValue());
							tempOption.put("selectedOptionId", selected.getId());
							optionMapList.add(tempOption);
						}
					}
					tempMap.put("selectedOptions", optionMapList);
				}
				result.add(tempMap);
			}
			return result;
		} catch (Exception exception) {
			logger.error("Exception occured while getting answer of customerID :{}", custId);
			throw new CustomParameterizedException(REFLEXIVE_QUESTION_SERVICE,
					"Exception occured while getting answer of customerID ", exception);
		}
	}
	
	/**
	 * convert List<ReflexiveAnswerEntity> to getAllAnswer format
	 */
	@Override
	@Transactional
	public Object convertToModelSavedAnswer(List<ReflexiveAnswerEntity> customerAnswer) {
		try {
			List<Map<String, Object>> result = new ArrayList<>();
			for (ReflexiveAnswerEntity answerEntity : customerAnswer) {
				Map<String, Object> tempMap = new HashMap<>();
				String questIdAndCustId = answerEntity.getCustIdQuestId();
				Long compositeKey[] = splitFromUnderscore(questIdAndCustId);
				MasterReflexiveQuestionEntity questionEntity = reflexiveQuestionDao
						.getEntity(MasterReflexiveQuestionEntity.class, compositeKey[1]);
				if (questionEntity != null) {
					tempMap.put("subType", questionEntity.getSubTypeEntity().getSubTypeValue());
					tempMap.put("type", questionEntity.getSubTypeEntity().getTypeEntity().getTypeValue());
					tempMap.put("questionId", questionEntity.getId());
					tempMap.put("qType", questionEntity.getqType());
					tempMap.put("question", questionEntity.getQuestion());
					tempMap.put("qTootipDescription", questionEntity.getToolTipDescription());
					tempMap.put("isTooltip", questionEntity.getIsToolTip());

					List<MasterOptionEntity> masterOptions = questionEntity.getOptions();
					if (masterOptions != null) {
						List<Map<String, Object>> masterOptionList = new ArrayList<>();
						masterOptions.stream().forEach(selected -> {
							Map<String, Object> tempOption = new HashMap<>();
							tempOption.put("id", selected.getId());
							tempOption.put("value", selected.getValue());
							masterOptionList.add(tempOption);
						});
						tempMap.put("masterOptions", masterOptionList);
					}
				}
				tempMap.put("answerId", answerEntity.getId());
				tempMap.put("flag", answerEntity.getFlag());
				tempMap.put("description", answerEntity.getDescription());
//				tempMap.put("fileUploadPath", answerEntity.getCustomerDetailsEntity().getFileUploadPath());
//				tempMap.put("customerId", answerEntity.getCustomerDetailsEntity().getCustomerDtlId());
				List<SelectOptionEntity> selectOptionEntities = answerEntity.getSelectOptionEntities();
				if (selectOptionEntities != null) {
					List<Map<String, Object>> optionMapList = new ArrayList<>();
					for (SelectOptionEntity selected : selectOptionEntities) {
						compositeKey = splitFromUnderscore(selected.getCustIdMasterOpId());
						MasterOptionEntity tempMasterOption = masterOptionDao.getEntity(MasterOptionEntity.class,
								compositeKey[1]);
						Map<String, Object> tempOption = new HashMap<>();
						if (tempMasterOption != null) {
							tempOption.put("id", tempMasterOption.getId());
							tempOption.put("value", tempMasterOption.getValue());
							tempOption.put("selectedOptionId", selected.getId());
							optionMapList.add(tempOption);
						}
					}
					tempMap.put("selectedOptions", optionMapList);
				}
				result.add(tempMap);
			}
			return result;
		} catch (Exception exception) {
			throw new CustomParameterizedException(REFLEXIVE_QUESTION_SERVICE,
					"Exception occured while getting answer of customerID ", exception);
		}
	}
	
	private Long[] splitFromUnderscore(String compositeKey) {
		String tempKeys[] =  compositeKey.split("_");
		Long []keys = new Long[2];
		keys[0] = Long.valueOf(tempKeys[0]);
		keys[1] = Long.valueOf(tempKeys[1]);
		return keys;
	}
}
