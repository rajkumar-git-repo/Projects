package com.mli.dao;

import java.util.List;

import com.mli.entity.MISRecipientEntity;

public interface MISRecipientDAO extends GenericDAO<MISRecipientEntity>{

	MISRecipientEntity findByMPHTypeAndMailType(String mphType, String mailType);

	List<MISRecipientEntity> findByMphType(String mphType);

	void saveData(MISRecipientEntity misRecipientEntity);

}
