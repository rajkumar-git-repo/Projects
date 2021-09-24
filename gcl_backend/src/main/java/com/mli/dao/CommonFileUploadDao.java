package com.mli.dao;

import java.util.List;

import com.mli.entity.CommonFileUploadEntity;
import com.mli.modal.FileUploadModel;

public interface CommonFileUploadDao extends GenericDAO<CommonFileUploadEntity> {
	public List<CommonFileUploadEntity> getProposals(String proposalNumber);

	public List<FileUploadModel> deleteFileDocument(String proposalNumber, String url);

	public List<FileUploadModel> getProposalsNumber(String proposalNumber);

	public List<CommonFileUploadEntity> getAllRecords(Long list);

}
