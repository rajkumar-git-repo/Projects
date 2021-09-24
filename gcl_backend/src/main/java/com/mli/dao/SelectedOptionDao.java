package com.mli.dao;

import java.util.List;

import com.mli.entity.SelectOptionEntity;

/**
 * 
 * @author Devendra.Kumar
 *
 */
public interface SelectedOptionDao extends GenericDAO<SelectOptionEntity> {

	public List<SelectOptionEntity> findByReflexiveAnswerId(Long ansId);
	public SelectOptionEntity findByMasterOptionIdAndAnswerId(Long masterOptionId,Long answerId);
}
