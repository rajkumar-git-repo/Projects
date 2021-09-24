package com.mli.service;

import java.util.List;

import com.mli.entity.AdminProductConfigEntity;
import com.mli.model.AdminProductConfigModel;
import com.mli.model.MISRecipientModel;
import com.mli.model.response.ResponseModel;

public interface AdminProductConfigService {


	public AdminProductConfigEntity getAdminConfigDetails(String bankName);

	ResponseModel<?> createAdminConfig(List<AdminProductConfigModel> adminProductConfigModelList);

	List<AdminProductConfigEntity> getAllAdminConfig();

	AdminProductConfigEntity getByCurrentSellerBankName(String token);

	AdminProductConfigEntity getBySellerContactNumber(Long contactNumber);

	public ResponseModel<?> saveMisRecipients(List<MISRecipientModel> misRecipientModel);

	ResponseModel<?> getAllRecipients(String mphType);

}
