package com.mli.dao.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.mli.constants.Constant;
import com.mli.dao.BaseDAO;
import com.mli.dao.CreditCardCustomerDAO;
import com.mli.entity.CreditCardCustomerEntity;
import com.mli.entity.CustomerDetailsEntity;
import com.mli.enums.Status;
import com.mli.utils.DateUtil;

@Repository
@Transactional
public class CreditCardCustomerDAOImpl extends BaseDAO<CreditCardCustomerEntity> implements CreditCardCustomerDAO{
	
	private static final Logger logger = LoggerFactory.getLogger(CreditCardCustomerDAOImpl.class);

	public CreditCardCustomerDAOImpl() {
		super(CreditCardCustomerEntity.class);
	}
	
	
	@Override
	public CreditCardCustomerEntity findByProposalNumber(String proposalNumber) {
		Criteria criteria = getSession().createCriteria(getPersistentClass());
		criteria.add(Restrictions.eq("proposalNumber", proposalNumber));
		return (CreditCardCustomerEntity) criteria.uniqueResult();
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> search(Pageable pageable,Long sellerId, String param, String status, String paymentStatus) {
		Criteria criteria = getSession().createCriteria(CreditCardCustomerEntity.class);
		criteria.add(Restrictions.eq("slrDtlId.sellerDtlId", sellerId));
		criteria.addOrder(Order.desc("modifiedOn"));
		if (!StringUtils.isEmpty(param)) {
			Criterion fName = Restrictions.like("firstName", param+"%");
			Criterion lName = Restrictions.like("lastName", param+"%");
			Criterion appNo = Restrictions.like("proposalNumber", "%"+param+"%");
			Disjunction orExp = Restrictions.or(fName, lName, appNo);
			criteria.add(orExp);
		} 
		if (!StringUtils.isEmpty(status)) {
			if (status.equals(Constant.APP_PENDING)) {
				criteria.add(Restrictions.eq("appStatus", Status.APP_PENDING));
			} else if (status.equalsIgnoreCase(Constant.APP_SENT)) {
				criteria.add(Restrictions.eq("appStatus", Status.APP_SENT));
			} else if (status.equalsIgnoreCase(Constant.APP_VERIFIED)) {
				criteria.add(Restrictions.eq("appStatus", Status.APP_VERIFIED));
				if(!StringUtils.isEmpty(paymentStatus)) {
					if (Constant.PAYMENT_PENDING.equals(paymentStatus)) {
						criteria.add(Restrictions.eq("paymentStatus", Status.PAYMENT_PENDING));
					} else if (Constant.PAYMENT_SUCCESS.equals(paymentStatus)) {
						criteria.add(Restrictions.eq("paymentStatus", Status.PAYMENT_SUCCESS));
					} else if (Constant.PAYMENT_FAILED.equals(paymentStatus)) {
						criteria.add(Restrictions.eq("paymentStatus", Status.PAYMENT_FAILED));
					} 
				}
			} else if (status.equalsIgnoreCase(Constant.APP_COMPLETE)) {
				criteria.add(Restrictions.eq("appStatus", Status.APP_COMPLETE));
			} 
		}
		criteria.setFirstResult(pageable.getPageSize() * pageable.getPageNumber());
		criteria.setMaxResults(pageable.getPageSize());
		List<CreditCardCustomerEntity> result = criteria.list();
		Map<String,Object> countResultMap = new HashMap<>(2);
		countResultMap.put(Constant.QUERY_RESULT, result);	
		criteria.setFirstResult(0);
		Long count = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
		logger.info("Total Count : "+count);
		countResultMap.put(Constant.TOTAL_COUNT, count);
		return countResultMap;
	}
	
	@Override
	public Integer findProposalsCountBySellerId(Long sellerId) {
		Criteria criteria = getSession().createCriteria(getPersistentClass());
		criteria.add(Restrictions.eq("slrDtlId.sellerDtlId", sellerId));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return  (Integer) criteria.list().size();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<CreditCardCustomerEntity> findByCustomerIdAndName(String param,String status) {
		Criteria criteria = getSession().createCriteria(getPersistentClass());
		Criterion fName = Restrictions.like("firstName", param+"%");
		Criterion lName = Restrictions.like("lastName", param+"%");
		Criterion appNo = Restrictions.like("customerId", param+"%");
		Disjunction orExp = Restrictions.or(fName, lName, appNo);
		criteria.add(orExp);
		if (status != null && status.equals(Constant.VERIFIED)) {
			criteria.add(Restrictions.eq("appStatus", Status.APP_VERIFIED));
			criteria.add(Restrictions.eq("paymentStatus", Status.PAYMENT_SUCCESS));
		}
		return (List<CreditCardCustomerEntity>) criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<CreditCardCustomerEntity> getAllCustomerForExcel(Long from, Long to) {
		Criteria criteria = getSession().createCriteria(getPersistentClass());
		if (from != null && to == null) {
			criteria.add(Restrictions.ge("modifiedOn", from));
		} else if (from != null && to != null) {
			criteria.add(Restrictions.ge("modifiedOn", from));
			criteria.add(Restrictions.le("modifiedOn", to));
		}
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<CreditCardCustomerEntity> result = (List<CreditCardCustomerEntity>) criteria.list();
		logger.info("Excel data Size : " + (result != null ? result.size() : null));
		if (result != null) {
			return result;
		} else {
			return new ArrayList<>(1);
		}
	}
	
	/**
	 * return {@link CreditCardCustomerEntity} list based on date range and app status
	 */
	@Override
	public List<CreditCardCustomerEntity> getAllCustomerForExcelCron(Long from,Long to, Status appStatus) {
		logger.info("from : " + DateUtil.extractDateWithTSAsStringSlashFormate(from) + ", to : "+ DateUtil.extractDateWithTSAsStringSlashFormate(to));
		Criteria criteria = getSession().createCriteria(getPersistentClass());
		if (!ObjectUtils.isEmpty(from) && !ObjectUtils.isEmpty(to)) {
			criteria.add(Restrictions.gt("appCompletionDate", from));
			criteria.add(Restrictions.le("appCompletionDate", to));
		}
		if (!StringUtils.isEmpty(appStatus)) {
			criteria.add(Restrictions.eq("appStatus", appStatus));
		}
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<CreditCardCustomerEntity> result = (List<CreditCardCustomerEntity>) criteria.list();
		logger.info("Excel data Size : " + (result != null ? result.size() : null));
		if (result != null) {
			return result;
		} else {
			return new ArrayList<>(1);
		}
	}

	/**
	 * get customer list based on app and payment status
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<CreditCardCustomerEntity> findByCustAppAndPaymentStatus() {
		Criteria criteria = getSession().createCriteria(getPersistentClass());
		criteria.add(Restrictions.in("appStatus", Arrays.asList(Status.APP_SENT,Status.APP_VERIFIED)));
		criteria.add(Restrictions.ne("paymentStatus", Status.PAYMENT_SUCCESS));
		criteria.add(Restrictions.ge("modifiedOn", DateUtil.addDaysWithUTCLastTS(-15)));
		criteria.add(Restrictions.le("modifiedOn", DateUtil.addDaysWithUTCLastTS(-1)));
		List<CreditCardCustomerEntity> result = (List<CreditCardCustomerEntity>) criteria.list();
		logger.info("customer data Size : " + (result != null ? result.size() : null));
		if (result != null) {
			return result;
		} else {
			return new ArrayList<>(1);
		}
	}
}
