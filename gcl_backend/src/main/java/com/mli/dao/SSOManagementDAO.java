package com.mli.dao;

import com.mli.entity.SSOManagementEntity;

public interface SSOManagementDAO extends GenericDAO<SSOManagementEntity> {

	SSOManagementEntity findByRoId(String roId);

	SSOManagementEntity findBySmId(String smId);

}
