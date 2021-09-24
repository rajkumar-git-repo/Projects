package com.mli.dao;

import java.util.List;

import com.mli.entity.CovidReportEntity;

public interface CovidReportDAO extends GenericDAO<CovidReportEntity> {
	
	public List<CovidReportEntity> findAllReport(String proposalNumber);
	
	public int deleteByFileUrl(String proposalNumber, String url);
	
	public List<CovidReportEntity> findByFileType(String proposalNumber,String fileType);

}
