package com.mli.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.mli.model.response.ResponseModel;


public interface UploadCSVDataService {

	ResponseModel<?> uploadSellerInBulk(MultipartFile file) throws  IOException, Exception;
	
	ResponseModel<?> updateSellerInBulk(MultipartFile file) throws  IOException, Exception;

	ResponseModel<?> editSellerInBulk(MultipartFile csvFile) throws IOException, Exception;

}
