package com.mli.dao;

import com.mli.entity.CronJobEntity;
import com.mli.enums.CronJobType;

public interface CronJobDAO extends GenericDAO<CronJobEntity> {

   public CronJobEntity getCronJobByType( CronJobType cronJobType, boolean status);

}