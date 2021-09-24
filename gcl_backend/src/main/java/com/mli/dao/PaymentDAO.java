package com.mli.dao;

import java.util.List;

import com.mli.entity.PaymentEntity;

public interface PaymentDAO extends GenericDAO<PaymentEntity>{

	PaymentEntity findByTxnId(String txnId);

	List<PaymentEntity> findByProposalNumber(String proposalNumber);

	PaymentEntity findByCreditCardCustomerIdAndStatus(Long customerId, String paymentStatus);

}
