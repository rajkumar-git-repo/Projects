package com.mli.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.mli.dao.BaseDAO;
import com.mli.dao.PaymentDAO;
import com.mli.entity.CreditCardHealthEntity;
import com.mli.entity.NomineeDetailsEntity;
import com.mli.entity.PaymentEntity;

@Repository
@Transactional
public class PaymentDAOImpl extends BaseDAO<PaymentEntity> implements PaymentDAO{

	public PaymentDAOImpl() {
		super(PaymentEntity.class);
	}

	@Override
	public PaymentEntity findByTxnId(String txnId) {
		Criteria criteria = getSession().createCriteria(getPersistentClass());
		criteria.add(Restrictions.eq("txnId", txnId));
		return (PaymentEntity) criteria.uniqueResult();
	}
	
	@Override
	public List<PaymentEntity> findByProposalNumber(String proposalNumber) {
		Criteria criteria = getSession().createCriteria(getPersistentClass());
		criteria.add(Restrictions.eq("proposalNumber", proposalNumber));
		if (!StringUtils.isEmpty(proposalNumber)) {
			return (List<PaymentEntity>) criteria.list();
		} else {
			return new ArrayList<>(1);
		}
	}
	
	@Override
	public PaymentEntity findByCreditCardCustomerIdAndStatus(Long customerId, String paymentStatus) {
		Criteria criteria = getSession().createCriteria(getPersistentClass());
		criteria.add(Restrictions.eq("creditCardCustomerId.creditCardCustomerId", customerId));
		criteria.add(Restrictions.eq("paymentStatus", paymentStatus));
		return (PaymentEntity) criteria.uniqueResult();
	}
}
