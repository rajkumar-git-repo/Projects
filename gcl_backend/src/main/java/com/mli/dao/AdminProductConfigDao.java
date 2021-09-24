package com.mli.dao;

import java.util.List;

import com.mli.entity.AdminProductConfigEntity;
import com.mli.model.AdminProductConfigModel;

public interface AdminProductConfigDao extends GenericDAO<AdminProductConfigEntity> {

	AdminProductConfigEntity findByBankName(String bankName);

}
