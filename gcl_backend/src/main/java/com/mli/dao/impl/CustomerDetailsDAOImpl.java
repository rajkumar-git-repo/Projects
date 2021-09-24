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
import org.springframework.util.StringUtils;

import com.mli.constants.Constant;
import com.mli.dao.BaseDAO;
import com.mli.dao.CustomerDetailsDAO;
import com.mli.entity.CustomerDetailsEntity;
import com.mli.entity.SellerDetailEntity;
import com.mli.enums.Status;
import com.mli.utils.DateUtil;

/**
 * @author Nikhilesh.Tiwari
 *
 */
@Repository
@Transactional
public class CustomerDetailsDAOImpl extends BaseDAO<CustomerDetailsEntity> implements CustomerDetailsDAO{

	
	private static final Logger logger = LoggerFactory.getLogger(CustomerDetailsDAOImpl.class);

	public CustomerDetailsDAOImpl() {
		super(CustomerDetailsEntity.class);
	}

	@Override
	public CustomerDetailsEntity findByProposalNumber(String proposalNumber) {
		Criteria criteria = getSession().createCriteria(getPersistentClass());
		criteria.add(Restrictions.eq("proposalNumber", proposalNumber));
		return (CustomerDetailsEntity) criteria.uniqueResult();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<CustomerDetailsEntity> findByDate(Long date) {
		Criteria criteria = getSession().createCriteria(getPersistentClass());
		criteria.add(Restrictions.eq("appCompletionDate", date));
		return (List<CustomerDetailsEntity>) criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<CustomerDetailsEntity> findPendingProposalsBySellerId(Long id, Status status) {
		Criteria criteria = getSession().createCriteria(getPersistentClass());
		criteria.add(Restrictions.eq("slrDtlId.sellerDtlId", id));
		criteria.add(Restrictions.eq("status", status));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return (List<CustomerDetailsEntity>) criteria.list();
	}

	@Override
	public Integer findPendingProposalsCountBySellerId(Long id, Status status) {
		Criteria criteria = getSession().createCriteria(getPersistentClass());
		criteria.add(Restrictions.eq("slrDtlId.sellerDtlId", id));
		// this line commented because of I want to fetch all 
//		criteria.add(Restrictions.eq("status", status));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return  (Integer) criteria.list().size();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<CustomerDetailsEntity> findByDateBankNameNdAppStatus(Long date ,String  masterPolicyHolderName, Status appstatus) {
		Criteria criteria = getSession().createCriteria(getPersistentClass());
		criteria.add(Restrictions.eq("appCompletionDate", date));
		if (masterPolicyHolderName != null) {
			criteria.add(Restrictions.eq("masterPolicyHolderName", masterPolicyHolderName));
		}
		criteria.add(Restrictions.eq("status", appstatus));
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		return (List<CustomerDetailsEntity>) criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CustomerDetailsEntity> findByLoanAppNumber(String param,String status) {
		Criteria criteria = getSession().createCriteria(getPersistentClass());
		Criterion fName = Restrictions.like("customerFirstName", param+"%");
		Criterion lName = Restrictions.like("customerLastName", param+"%");
		Criterion appNo = Restrictions.like("loanAppNumber", param+"%");
		Disjunction orExp = Restrictions.or(fName, lName, appNo);
		criteria.add(orExp);
		if (status != null && status.equals(Constant.VERIFIED)) {
			criteria.add(Restrictions.in("status", Arrays.asList(Status.APP_VERIFIED, Status.PHYSICAL_FORM_VERIFICATION,Status.AFL_PHYSICAL_FORM_VERIFICATION)));
		}
		return (List<CustomerDetailsEntity>) criteria.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CustomerDetailsEntity> findBySellerContactAndAppStatus(SellerDetailEntity sellerDetailEntity,
			Status appStep) {
		Criteria criteria = getSession().createCriteria(getPersistentClass());
		if(sellerDetailEntity != null) {
			criteria.add(Restrictions.eq("slrDtlId", sellerDetailEntity));
		}
		criteria.add(Restrictions.eq("appStep", appStep));
		return (List<CustomerDetailsEntity>) criteria.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> search(Long slrId, String param, String status, Pageable pageable,String isFinance ) {
		Criteria criteria = getSession().createCriteria(CustomerDetailsEntity.class);
		criteria.add(Restrictions.eq("slrDtlId.sellerDtlId", slrId));
		//criteria.addOrder(Order.desc("modifiedOn"));
		criteria.addOrder(Order.desc("modifiedOn"));
		if (param != null && !param.isEmpty()) {
			Criterion fName = Restrictions.like("customerFirstName", param+"%");
			Criterion lName = Restrictions.like("customerLastName", param+"%");
			Criterion appNo = Restrictions.like("loanAppNumber", "%"+param+"%");
			Disjunction orExp = Restrictions.or(fName, lName, appNo);
			criteria.add(orExp);
		} 
		if (status != null && !status.isEmpty()) {
			if (status.equalsIgnoreCase(Constant.APP_SENT)) {
				criteria.add(Restrictions.eq("status", Status.APP_SENT));
			} else if (status.equalsIgnoreCase(Constant.APP_VERIFIED)) {
				criteria.add(Restrictions.eq("status", Status.APP_VERIFIED));
				if(!StringUtils.isEmpty(isFinance)) {
					criteria.add(Restrictions.eq("isFinancePremium",isFinance));
				}
			} else if (status.equalsIgnoreCase(Constant.APP_COMPLETE)) {
				criteria.add(Restrictions.eq("status", Status.APP_COMPLETE));
			} else if (status.equals(Constant.APP_PENDING)) {
				criteria.add(Restrictions.eq("status", Status.APP_PENDING));
			} else if (status.equals(Constant.APP_NOT_INTERESTED)) {
				criteria.add(Restrictions.eq("status", Status.APP_NOT_INTERESTED));
			} else if (status.equals("Physical_Form_Verification")) {
				criteria.add(Restrictions.eq("status", Status.PHYSICAL_FORM_VERIFICATION));
			}else if (status.equals("AFL_Physical_Form_Verification")) {
				criteria.add(Restrictions.eq("status", Status.AFL_PHYSICAL_FORM_VERIFICATION));
			}
		}
		criteria.setFirstResult(pageable.getPageSize() * pageable.getPageNumber());
		criteria.setMaxResults(pageable.getPageSize());
		List<CustomerDetailsEntity> result = (List<CustomerDetailsEntity>) criteria.list();
		Map<String,Object> countResultMap = new HashMap<>(2);
		countResultMap.put(Constant.QUERY_RESULT, result);
		
		criteria.setFirstResult(0);
		Long count = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
		logger.info("Total Count : "+count);
		countResultMap.put(Constant.TOTAL_COUNT, count);
		return countResultMap;
	}

	/**
	 * Get all verified customer's
	 */
	@Override
	public List<CustomerDetailsEntity> getAllCustomerForExcel(Long from,Long to, String masterPlcy, Status appStatus) {
		/*// if current date is 10-10-2018, from 08-10-2018 23:59:59 and to : 09-10-2018 23:59:59
		Long to = DateUtil.addDaysWithUTCLastTS(-1);
		Long from = DateUtil.addDaysWithUTCLastTS(-2);*/
		logger.info("from : " + DateUtil.extractDateWithTSAsStringSlashFormate(from) + ", to : "
				+ DateUtil.extractDateWithTSAsStringSlashFormate(to));
		Criteria criteria = getSession().createCriteria(getPersistentClass());
		if (from != null && to != null) {
			criteria.add(Restrictions.gt("appCompletionDate", from));
			criteria.add(Restrictions.le("appCompletionDate", to));
		}
		if (masterPlcy != null) {
			criteria.add(Restrictions.eq("masterPolicyHolderName", masterPlcy));
		}
		if (appStatus != null) {
			criteria.add(Restrictions.eq("status", appStatus));
		}
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<CustomerDetailsEntity> result = (List<CustomerDetailsEntity>) criteria.list();
		logger.info("Excel data Size : " + (result != null ? result.size() : null));
		if (result != null) {
			return result;
		} else {
			return new ArrayList<>(1);
		}
	}

	/**
	 * Get all customer's
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<CustomerDetailsEntity> getAllCustomerForExcel(Long from, Long to) {
		Criteria criteria = getSession().createCriteria(getPersistentClass());
		if (from != null && to == null) {
			criteria.add(Restrictions.ge("modifiedOn", from));
		} else if (from != null && to != null) {
			criteria.add(Restrictions.ge("modifiedOn", from));
			criteria.add(Restrictions.le("modifiedOn", to));
		}
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		List<CustomerDetailsEntity> result = (List<CustomerDetailsEntity>) criteria.list();
		logger.info("Excel data Size : " + (result != null ? result.size() : null));
		if (result != null) {
			return result;
		} else {
			return new ArrayList<>(1);
		}
	}
	
	@Override
	public CustomerDetailsEntity findByLoanAppNumber(String loanAppNumber) {
		Criteria criteria = getSession().createCriteria(getPersistentClass());
		criteria.add(Restrictions.eq("loanAppNumber", loanAppNumber));
		criteria.addOrder(Order.desc("createdOn"));
		criteria.setFirstResult(0);
		criteria.setMaxResults(1);
		return (CustomerDetailsEntity) criteria.uniqueResult();
	}
	
	@Override
	public CustomerDetailsEntity findByLoanAppNumberAndMphType(String loanAppNumber,String mphType) {
		Criteria criteria = getSession().createCriteria(getPersistentClass());
		criteria.add(Restrictions.eq("loanAppNumber", loanAppNumber));
		criteria.add(Restrictions.eq("masterPolicyHolderName", mphType));
		return (CustomerDetailsEntity) criteria.uniqueResult();
	}
}