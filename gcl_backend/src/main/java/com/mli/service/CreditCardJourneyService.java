package com.mli.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;

import com.mli.entity.CreditCardCustomerEntity;
import com.mli.model.CardTypeModel;
import com.mli.model.CreditCardCustomerModel;
import com.mli.model.CreditCardJourneyModel;
import com.mli.model.CustomerDeclarationModel;
import com.mli.model.CustomerUpdateModel;
import com.mli.model.response.ResponseModel;

public interface CreditCardJourneyService {

	ResponseModel<CreditCardJourneyModel> saveCreditCardJourneyDetails(CreditCardJourneyModel creditCardJourneyModel);

	List<CardTypeModel> getLookup();

	public Map<String, Object> search(Pageable pageable,Long slrContNo, String param, String status, String paymentStatus);

	Map<String, Object> getStatus(String data);

	ResponseModel<?> updateCustomer(CustomerUpdateModel customerUpdateModel);

	String generateAndEmailPDF(CreditCardCustomerEntity creditCardCustomerEntity, String modifyTS);

	void sendEmailWithPDF(CreditCardCustomerEntity creditCardCustomerEntity,String filePath);

	void generatePDF(CreditCardCustomerEntity creditCardCustomerEntity);

	boolean getYBLCCCustomerInBulk(String fromDate, String toDate);

	ResponseModel<?> saveCustDeclaration(CustomerDeclarationModel customerDeclarationModel);

	List<CreditCardCustomerModel> executeCron();

	void sendEmailToBank();

}
