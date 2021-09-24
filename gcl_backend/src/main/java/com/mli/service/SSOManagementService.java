package com.mli.service;

import java.util.List;

import com.mli.entity.SSOManagementEntity;
import com.mli.model.SSOManagementModel;
import com.mli.model.response.ResponseModel;

public interface SSOManagementService {

	ResponseModel<?> create(SSOManagementModel ssoManagementModel);

	ResponseModel<?> update(SSOManagementModel ssoManagementModel);

	ResponseModel<?> delete(Long id);

	List<SSOManagementEntity> getAll();

	boolean findByRoId(String roId);

	boolean findBySmId(String smId);

}
