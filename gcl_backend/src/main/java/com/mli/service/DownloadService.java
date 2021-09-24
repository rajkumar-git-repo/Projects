package com.mli.service;

import java.util.Map;

import com.mli.model.response.ResponseModel;

public interface DownloadService {

	public Map<String, Object> findByProposalNoNdDocType(String proposalNumber,String docType,String mph);
	ResponseModel<String> downloadBase64CovidReport(String proposalNumber,String fileName, String fileType);
}
