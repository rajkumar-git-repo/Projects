package com.mli.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.mli.constants.Constant;
import com.mli.constants.MLIMessageConstants;
import com.mli.dao.AdminProductConfigDao;
import com.mli.dao.MISRecipientDAO;
import com.mli.dao.SellerDAO;
import com.mli.entity.AdminProductConfigEntity;
import com.mli.entity.MISRecipientEntity;
import com.mli.entity.SellerBankEntity;
import com.mli.entity.SellerDetailEntity;
import com.mli.enums.MasterPolicyHolderName;
import com.mli.model.AdminProductConfigModel;
import com.mli.model.MISRecipientModel;
import com.mli.model.response.ResponseModel;
import com.mli.model.response.SaveDetailsResponse;
import com.mli.model.response.SaveUpdateResponse;
import com.mli.security.JwtTokenUtil;
import com.mli.service.AdminProductConfigService;
import com.mli.utils.DateUtil;
import com.mli.utils.EmailValidationUtil;

@Service
public class AdminProductConfigServiceImpl implements AdminProductConfigService{
	
	private static final Logger logger = Logger.getLogger(AdminProductConfigServiceImpl.class);
	@Autowired
	private AdminProductConfigDao  adminProductConfigDao;
	
	@Autowired 
	private SellerDAO sellerDao;
	
	@Autowired
	JwtTokenUtil jwtTokenUtil;

	@Autowired
	private MISRecipientDAO misRecipientDao;
	
	@Autowired
	private ModelMapper modelMapper;
	/**
	 * create or update admin config
	 * @return 
	 */
	@Transactional
	@Override
	public ResponseModel<?> createAdminConfig(List<AdminProductConfigModel> adminProductConfigModelList) {
		ResponseModel<?> responseModel = new ResponseModel<>();
		logger.info("Enter into AdminProductConfigServiceImpl to create or update configuration...");
		try {
			if (!CollectionUtils.isEmpty(adminProductConfigModelList)) {
				List<AdminProductConfigEntity> adminProductConfigEntityList = new ArrayList<AdminProductConfigEntity>();
				List<String> configValue = Arrays.asList("Yes","No");
				for(AdminProductConfigModel adminProductConfigModel: adminProductConfigModelList){
					String bankName = MasterPolicyHolderName.getDescription(adminProductConfigModel.getBankName().toUpperCase());
					if (!StringUtils.isEmpty(bankName) && configValue.contains(adminProductConfigModel.getIsCovidEnable())
							&& configValue.contains(adminProductConfigModel.getIsRiderEnable())) {
						AdminProductConfigEntity adminProductConfigEntity = adminProductConfigDao.findByBankName(bankName);
						if (!ObjectUtils.isEmpty(adminProductConfigEntity)) {
							adminProductConfigEntity.setBankName(bankName);
							adminProductConfigEntity.setIsRiderEnable(adminProductConfigModel.getIsRiderEnable());
							adminProductConfigEntity.setIsCovidEnable(adminProductConfigModel.getIsCovidEnable());
							adminProductConfigEntity.setModifiedBy("Admin");
							adminProductConfigEntity.setModifiedOn(DateUtil.toCurrentUTCTimeStamp());
							adminProductConfigEntityList.add(adminProductConfigEntity);
						} else {
							adminProductConfigEntity = new AdminProductConfigEntity();
							adminProductConfigEntity.setBankName(bankName);
							adminProductConfigEntity.setIsRiderEnable(adminProductConfigModel.getIsRiderEnable());
							adminProductConfigEntity.setIsCovidEnable(adminProductConfigModel.getIsCovidEnable());
							adminProductConfigEntity.setCreatedBy("Admin");
							adminProductConfigEntity.setCreatedOn(DateUtil.toCurrentUTCTimeStamp());
							adminProductConfigEntityList.add(adminProductConfigEntity);
						}
					}else {
						logger.info("Please provide valid bank name..........");
						responseModel.setMessage(MLIMessageConstants.ADMIN_CONFIG_FAILURE);
						responseModel.setStatus(SaveDetailsResponse.FAILURE);
						return responseModel;
					}
				};
				adminProductConfigEntityList.forEach(adminProductConfigEntity ->{
					adminProductConfigDao.saveOrUpdate(adminProductConfigEntity);
				});
				responseModel.setMessage(MLIMessageConstants.ADMIN_CONFIG_SUCCESS);
				responseModel.setStatus(SaveDetailsResponse.SUCCESS);
			}
			return responseModel;
		}catch (Exception e) {
			logger.error("Exception occurred while creating or updating admin config : {}");
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * fetch admin config using bank name
	 */
	@Transactional
	@Override
	public AdminProductConfigEntity getAdminConfigDetails(String bankName) {
		logger.info("Enter into AdminProductConfigServiceImpl to fetch admin config using bankName....");
		try {
			AdminProductConfigEntity adminProductConfigEntity = adminProductConfigDao.findByBankName(MasterPolicyHolderName.getDescription(bankName.toUpperCase()));
			return adminProductConfigEntity;
		} catch (Exception e) {
			logger.error("Exception occurred while fetching admin config by bank name: {}");
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * fetch all admin config
	 */
	@Transactional
	@Override
	public List<AdminProductConfigEntity> getAllAdminConfig(){
		logger.info("Enter into AdminProductConfigServiceImpl to fetch all admin config....");
		try {
			return adminProductConfigDao.getAll();
		}catch (Exception e) {
			logger.error("Exception occurred while fetching admin config : {}");
			e.printStackTrace();
		}
		return null;
		
	}
	
	/**
	 * fetch admin detail for seller using token
	 */
	@Transactional
	@Override
	public AdminProductConfigEntity getByCurrentSellerBankName(String token) {
		logger.info("Enter into AdminProductConfigServiceImpl to fetch admin config by token....");
		try {
			String userName = jwtTokenUtil.getUsernameFromToken(token);
			SellerDetailEntity bankEntity = sellerDao.findByContactNo(Long.parseLong(userName));
			if (!ObjectUtils.isEmpty(bankEntity) && !CollectionUtils.isEmpty(bankEntity.getSellerBankEntity())) {
				SellerBankEntity sellerBankEntity = bankEntity.getSellerBankEntity().iterator().next();
				if (!ObjectUtils.isEmpty(sellerBankEntity)) {
					AdminProductConfigEntity adminProductConfigEntity = adminProductConfigDao.findByBankName(MasterPolicyHolderName.getDescription(sellerBankEntity.getBankName().getLabel().toUpperCase()));
					return adminProductConfigEntity;
				}
			}
			return null;
		} catch (Exception e) {
			logger.error("Exception occurred while fetching admin config using token : {}");
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * get {@link AdminProductConfigEntity} based in contact number
	 * @param contactNumber
	 * @return
	 * @author rajkumar
	 */
	@Override
	public AdminProductConfigEntity getBySellerContactNumber(Long contactNumber) {
		logger.info("Enter into AdminProductConfigServiceImpl to fetch admin config by contactNumber....");
		try {
			SellerDetailEntity bankEntity = sellerDao.findByContactNo(contactNumber);
			if (!ObjectUtils.isEmpty(bankEntity) && !CollectionUtils.isEmpty(bankEntity.getSellerBankEntity())) {
				SellerBankEntity sellerBankEntity = bankEntity.getSellerBankEntity().iterator().next();
				if (!ObjectUtils.isEmpty(sellerBankEntity)) {
					AdminProductConfigEntity adminProductConfigEntity = adminProductConfigDao.findByBankName(MasterPolicyHolderName.getDescription(sellerBankEntity.getBankName().getLabel().toUpperCase()));
					return adminProductConfigEntity;
				}
			}
			return null;
		} catch (Exception e) {
			logger.error("Exception occurred while fetching admin config using contactNumber : {}");
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * add MISRecipients from admin panal
	 */
	@Override
	@Transactional
	public ResponseModel<?> saveMisRecipients(List<MISRecipientModel> misRecipientModelList) {
		ResponseModel<?> responseModel = new ResponseModel<>();
		logger.info("Enter into AdminProductConfigServiceImpl to add MIS Recipients...");
		try {
			responseModel = validateMISRecipient(misRecipientModelList);
			if(responseModel.getStatus() != null) {
				return responseModel;
			}
			List<MISRecipientEntity>  misRecipientEntities = getMappedMISRecipients(misRecipientModelList);
			if(!CollectionUtils.isEmpty(misRecipientEntities)) {
				for(MISRecipientEntity misRecipientEntity : misRecipientEntities) {
					MISRecipientEntity misRecipient = misRecipientDao.findByMPHTypeAndMailType(misRecipientEntity.getMphType(), misRecipientEntity.getMailType());
					if(!ObjectUtils.isEmpty(misRecipient)) {
						misRecipient.setMailId(misRecipientEntity.getMailId());
						misRecipientDao.saveOrUpdate(misRecipient);
					}else {
					    misRecipientDao.save(misRecipientEntity);
					}
				}
				logger.info("MIS Recipients add successfully..........");
				responseModel.setMessage(MLIMessageConstants.MIS_RECIPIENT_ADD);
				responseModel.setStatus(SaveDetailsResponse.SUCCESS);
				return responseModel;
			}
		}catch (Exception e) {
			logger.error("Exception occurred while adding MIS recipients : {}");
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * validate mail using regex and check duplicate recipients
	 * @param misRecipientModelList
	 * @return
	 * @author rajkumar
	 * @throws Exception 
	 */
	public ResponseModel<?> validateMISRecipient(List<MISRecipientModel> misRecipientModelList) throws Exception {
		ResponseModel<?> responseModel = new ResponseModel<>();
		StringBuilder axisMails = new StringBuilder();
		StringBuilder yesMails = new StringBuilder();
		for (MISRecipientModel misRecipientModel : misRecipientModelList) {
			if (Constant.AXIS.equals(misRecipientModel.getMphType())) {
				if (Constant.TO.equals(misRecipientModel.getMailType()) && !StringUtils.isEmpty(misRecipientModel.getMailId())) {
					axisMails.append(misRecipientModel.getMailId()).append(Constant.SEMICOLON);
				}
				if (Constant.CC.equals(misRecipientModel.getMailType()) && !StringUtils.isEmpty(misRecipientModel.getMailId())) {
					axisMails.append(misRecipientModel.getMailId()).append(Constant.SEMICOLON);
				}
				if (Constant.BCC.equals(misRecipientModel.getMailType()) && !StringUtils.isEmpty(misRecipientModel.getMailId())) {
					axisMails.append(misRecipientModel.getMailId()).append(Constant.SEMICOLON);
				}
			}
			if (Constant.YES.equals(misRecipientModel.getMphType())) {
				if (Constant.TO.equals(misRecipientModel.getMailType()) && !StringUtils.isEmpty(misRecipientModel.getMailId())) {
					yesMails.append(misRecipientModel.getMailId()).append(Constant.SEMICOLON);
				}
				if (Constant.CC.equals(misRecipientModel.getMailType()) && !StringUtils.isEmpty(misRecipientModel.getMailId())) {
					yesMails.append(misRecipientModel.getMailId()).append(Constant.SEMICOLON);
				}
				if (Constant.BCC.equals(misRecipientModel.getMailType()) && !StringUtils.isEmpty(misRecipientModel.getMailId())) {
					yesMails.append(misRecipientModel.getMailId()).append(Constant.SEMICOLON);
				}
			}
		}
		String allAxisMail = "";
		String allYesMail = "";
		if(axisMails.length() > 1) {
		   allAxisMail = axisMails.toString().substring(0, axisMails.toString().length() - 1);;
		}
		if(yesMails.length() > 1) {
		   allYesMail = yesMails.toString().substring(0, yesMails.toString().length() - 1);;
		}
		String[] axisMailList = allAxisMail.split(Constant.SEMICOLON);
		HashSet<String> axisSet = new HashSet<String>();
		HashSet<String> yesSet = new HashSet<String>();
		for(String axisMail : axisMailList) {
			 if(!EmailValidationUtil.isEmailValidation(axisMail.trim())){
				 responseModel.setMessage(MLIMessageConstants.MIS_EMAIL_VALIDATION);
				 responseModel.setStatus(SaveUpdateResponse.FAILURE);
				 return responseModel;
			 }
			if(!axisSet.contains(axisMail))
			    axisSet.add(axisMail);
			else {
				responseModel.setMessage(String.format(MLIMessageConstants.MIS_RECIPIENT_DUPLICATE, axisMail));
				responseModel.setStatus(Constant.FAILURE);
				return responseModel;
			}
		}
		String[] yesMailList = allYesMail.split(Constant.SEMICOLON);
		for(String yesMail : yesMailList) {
			if(!EmailValidationUtil.isEmailValidation(yesMail.trim())){
				 responseModel.setMessage(MLIMessageConstants.MIS_EMAIL_VALIDATION);
				 responseModel.setStatus(SaveUpdateResponse.FAILURE);
				 return responseModel;
			 }
			if(!yesSet.contains(yesMail))
			    yesSet.add(yesMail);
			else {
				responseModel.setMessage(String.format(MLIMessageConstants.MIS_RECIPIENT_DUPLICATE, yesMail));
				responseModel.setStatus(Constant.FAILURE);
				return responseModel;
			}
		}
		return responseModel;
	}
	
	
	/**
	 * mapped model into entity
	 * @param misRecipientModelList
	 * @return
	 * @author rajkumar
	 */
	public List<MISRecipientEntity> getMappedMISRecipients(List<MISRecipientModel> misRecipientModelList) {
		List<MISRecipientEntity> misRecipientEntities = new ArrayList<MISRecipientEntity>();
		if (!CollectionUtils.isEmpty(misRecipientModelList)) {
			for (MISRecipientModel misRecipientModel : misRecipientModelList) {
				MISRecipientEntity misRecipientEntity = new MISRecipientEntity();
				misRecipientEntity.setMphType(misRecipientModel.getMphType());
				misRecipientEntity.setMailType(misRecipientModel.getMailType());
				misRecipientEntity.setMailId(misRecipientModel.getMailId());
				misRecipientEntities.add(misRecipientEntity);
			}
		}
		return misRecipientEntities;
	}
	
	/**
	 * get all mis recipients based on mphType
	 * @param mphType
	 * @return
	 * @author rajkumar
	 */
	@Override
	@Transactional
	public ResponseModel<?> getAllRecipients(String mphType) {
		logger.info("Enter into AdminProductConfigServiceImpl to fetch MIS Recipients...");
		ResponseModel<List<MISRecipientModel>> responseModel = new ResponseModel<>();
		List<MISRecipientEntity> misRecipientEntities = new ArrayList<MISRecipientEntity>();
		try {
			if (!StringUtils.isEmpty(mphType)) {
				misRecipientEntities = misRecipientDao.findByMphType(mphType);
			} else {
				misRecipientEntities = misRecipientDao.getAll();
			}
			List<MISRecipientModel> misRecipientModel = Arrays.asList(modelMapper.map(misRecipientEntities, MISRecipientModel[].class));
			responseModel.setData(misRecipientModel);
			responseModel.setStatus(SaveUpdateResponse.SUCCESS);
			responseModel.setMessage("Fetched successfully");
			return responseModel;
		} catch (Exception e) {
			logger.error("Exception occurred while fetching MIS recipients : {}");
			e.printStackTrace();
		}
		return null;
	}
}
