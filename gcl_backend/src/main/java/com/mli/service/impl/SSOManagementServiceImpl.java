package com.mli.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.mli.constants.MLIMessageConstants;
import com.mli.dao.SSOManagementDAO;
import com.mli.entity.SSOManagementEntity;
import com.mli.model.SSOManagementModel;
import com.mli.model.response.ResponseModel;
import com.mli.model.response.SaveDetailsResponse;
import com.mli.service.SSOManagementService;

@Service
public class SSOManagementServiceImpl implements SSOManagementService{
	
	private static final Logger logger = Logger.getLogger(SSOManagementServiceImpl.class);
	
	@Autowired
	private SSOManagementDAO ssoManagementDAO;

    /**
     * method is used to create SSOManagementEntity
     * @param ssoManagementModel
     * @return
     * @author rajkumar
     */
	@Override
	@Transactional
	public ResponseModel<?> create(SSOManagementModel ssoManagementModel) {
		ResponseModel<?> responseModel = new ResponseModel<>();
		logger.info("Enter into SSOManagementServiceImpl to create sso management...");
		try {
			if(!ObjectUtils.isEmpty(ssoManagementModel)) {
				if(findByRoId(ssoManagementModel.getRoId())) {
					responseModel.setMessage(MLIMessageConstants.RO_SSO_EXIST);
					responseModel.setStatus(SaveDetailsResponse.SUCCESS);
					return responseModel;
				}
				if(findBySmId(ssoManagementModel.getSmId())) {
					responseModel.setMessage(MLIMessageConstants.SM_SSO_EXIST);
					responseModel.setStatus(SaveDetailsResponse.SUCCESS);
					return responseModel;
				}
				SSOManagementEntity ssoManagementEntity = new SSOManagementEntity();
				ssoManagementEntity.setRoId(ssoManagementModel.getRoId());
				ssoManagementEntity.setRoName(ssoManagementModel.getRoName());
				ssoManagementEntity.setSmId(ssoManagementModel.getSmId());
				ssoManagementEntity.setSmName(ssoManagementModel.getSmName());
				ssoManagementDAO.save(ssoManagementEntity);
				responseModel.setMessage(MLIMessageConstants.SSO_SAVE_SUCCESS);
				responseModel.setStatus(SaveDetailsResponse.SUCCESS);
			}
			return responseModel;
		}catch(Exception exception) {
			logger.error("Exception occurred while creating sso management : {}");
			exception.printStackTrace();
		}
		return null;
	}

	/**
	 * method is used to update SSOManagementEntity
	 * @param ssoManagementModel
	 * @return
	 * @author rajkumar
	 */
	@Override
	@Transactional
	public ResponseModel<?> update(SSOManagementModel ssoManagementModel) {
		ResponseModel<?> responseModel = new ResponseModel<>();
		logger.info("Enter into SSOManagementServiceImpl to update sso management...");
		try {
			if(!ObjectUtils.isEmpty(ssoManagementModel.getId())) {
				SSOManagementEntity ssoManagementEntity = ssoManagementDAO.getEntity(SSOManagementEntity.class, ssoManagementModel.getId());
				if (!ObjectUtils.isEmpty(ssoManagementEntity)) {
					ssoManagementEntity.setRoId(ssoManagementModel.getRoId());
					ssoManagementEntity.setRoName(ssoManagementModel.getRoName());
					ssoManagementEntity.setSmId(ssoManagementModel.getSmId());
					ssoManagementEntity.setSmName(ssoManagementModel.getSmName());
					ssoManagementDAO.saveOrUpdate(ssoManagementEntity);
					responseModel.setMessage(MLIMessageConstants.SSO_UPDATE_SUCCESS);
					responseModel.setStatus(SaveDetailsResponse.SUCCESS);
				}else {
					responseModel.setMessage(MLIMessageConstants.SSO_NOT_EXIST);
					responseModel.setStatus(SaveDetailsResponse.FAILURE);
				}
			}else {
				responseModel.setMessage(MLIMessageConstants.ID_NOT_NULL);
				responseModel.setStatus(SaveDetailsResponse.FAILURE);
			}
			return responseModel;
		}catch(Exception exception) {
			logger.error("Exception occurred while updating sso management : {}");
			exception.printStackTrace();
		}
		return null;
	}

	/**
	 * method is used to delete SSOManagementEntity for given primary key
	 * @param id
	 * @return
	 * @author rajkumar
	 */
	@Override
	@Transactional
	public ResponseModel<?> delete(Long id) {
		ResponseModel<?> responseModel = new ResponseModel<>();
		logger.info("Enter into SSOManagementServiceImpl to update sso management...");
		try {
			if(!ObjectUtils.isEmpty(id)) {
				SSOManagementEntity ssoManagementEntity = ssoManagementDAO.getEntity(SSOManagementEntity.class, id);
				if (!ObjectUtils.isEmpty(ssoManagementEntity)) {
					ssoManagementDAO.delete(ssoManagementEntity);
					responseModel.setMessage(MLIMessageConstants.SSO_DELETE_SUCCESS);
					responseModel.setStatus(SaveDetailsResponse.SUCCESS);
				}else {
					responseModel.setMessage(MLIMessageConstants.SSO_NOT_EXIST);
					responseModel.setStatus(SaveDetailsResponse.FAILURE);
				}
			}else {
				responseModel.setMessage(MLIMessageConstants.ID_NOT_NULL);
				responseModel.setStatus(SaveDetailsResponse.FAILURE);
			}
			return responseModel;
		}catch(Exception exception) {
			logger.error("Exception occurred while deleting sso management : {}");
			exception.printStackTrace();
		}
		return null;
	}

	/**
	 * fetch all SSOManagementEntity object
	 * @return
	 * @author rajkumar
	 */
	@Override
	@Transactional
	public List<SSOManagementEntity> getAll() {
		logger.info("Enter into SSOManagementServiceImpl to fetching sso management...");
		try {
			return ssoManagementDAO.getAll();
		}catch (Exception exception) {
			logger.error("Exception occurred while fetching sso management : {}");
			exception.printStackTrace();
		}
		return null;
	}

	/**
	 * check whether SSOManagementEntity is exist or not for given ro id 
	 * @return
	 * @author rajkumar
	 */
	@Override
	@Transactional
	public boolean findByRoId(String roId) {
		try {
			SSOManagementEntity ssoManagementEntity = ssoManagementDAO.findByRoId(roId);
			if(!ObjectUtils.isEmpty(ssoManagementEntity)) {
				return true;
			}
		}catch (Exception exception) {
			logger.error("Exception occurred while fetching sso management by ro id: {}");
			exception.printStackTrace();
		}
		return false;
	}

	/**
	 * check whether SSOManagementEntity is exist or not for given sm id 
	 * @return
	 * @author rajkumar
	 */
	@Override
	@Transactional
	public boolean findBySmId(String smId) {
		try {
			SSOManagementEntity ssoManagementEntity = ssoManagementDAO.findBySmId(smId);
			if(!ObjectUtils.isEmpty(ssoManagementEntity)) {
				return true;
			}
		}catch (Exception exception) {
			logger.error("Exception occurred while fetching sso management by sm id: {}");
			exception.printStackTrace();
		}
		return false;
	}

}
