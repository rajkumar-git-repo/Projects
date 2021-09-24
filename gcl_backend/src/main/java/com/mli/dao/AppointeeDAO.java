package com.mli.dao;

import java.util.List;

import com.mli.entity.AppointeeDetailsEntity;

/**
 * @author Nikhilesh.Tiwari
 *
 */
public interface AppointeeDAO extends GenericDAO<AppointeeDetailsEntity> {

	public AppointeeDetailsEntity findByNomineeDtlId(Long nomineeDtlId);
	
	public List<AppointeeDetailsEntity> findByNomineeDtlIds(List<Long> nomineeDtlId);

}
