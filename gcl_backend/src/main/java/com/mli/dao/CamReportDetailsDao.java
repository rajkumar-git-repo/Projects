package com.mli.dao;

import java.util.List;

import com.mli.entity.CamReportDetailsEntity;
import com.mli.modal.CamResponseModel;

public interface CamReportDetailsDao extends GenericDAO<CamReportDetailsEntity> {
	public List<CamReportDetailsEntity> getProposals(String proposalNumber);
	
	public List<CamResponseModel> deleteAdditonalDocument(String proposalNumber, String url);
	
	public List<CamResponseModel> getProposalsNumber(String proposalNumber);
	
	public List<CamReportDetailsEntity> getAllRecords(Long list);

}
