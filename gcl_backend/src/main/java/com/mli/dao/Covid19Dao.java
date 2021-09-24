package com.mli.dao;

import com.mli.entity.Covid19Entity;

public interface Covid19Dao extends GenericDAO<Covid19Entity>{

	Covid19Entity findByHealthDtlId(Long healthDtlId);

}
