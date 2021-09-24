package com.mli.dao;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;

import com.mli.entity.CreditCardCustomerEntity;
import com.mli.enums.Status;

public interface CreditCardCustomerDAO extends GenericDAO<CreditCardCustomerEntity>{

	CreditCardCustomerEntity findByProposalNumber(String proposalNumber);

	Map<String, Object> search(Pageable pageable, Long sellerId, String param, String status, String paymentStatus);

	Integer findProposalsCountBySellerId(Long sellerId);

	List<CreditCardCustomerEntity> findByCustomerIdAndName(String param, String status);

	List<CreditCardCustomerEntity> getAllCustomerForExcel(Long from, Long to);

	List<CreditCardCustomerEntity> getAllCustomerForExcelCron(Long from, Long to, Status appStatus);

	List<CreditCardCustomerEntity> findByCustAppAndPaymentStatus();

}
