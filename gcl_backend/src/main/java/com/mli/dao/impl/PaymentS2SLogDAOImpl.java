package com.mli.dao.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mli.dao.BaseDAO;
import com.mli.dao.PaymentS2SLogDAO;
import com.mli.entity.PaymentS2SLog;

@Repository
@Transactional
public class PaymentS2SLogDAOImpl extends BaseDAO<PaymentS2SLog> implements PaymentS2SLogDAO{

	public PaymentS2SLogDAOImpl() {
		super(PaymentS2SLog.class);
	}

}
