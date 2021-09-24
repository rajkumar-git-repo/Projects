package com.mli.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mli.constants.Constant;
import com.mli.dao.BaseDAO;
import com.mli.dao.SellerDAO;
import com.mli.entity.SellerDetailEntity;
import com.mli.utils.ObjectsUtil;

/**
 * @author Nikhilesh.Tiwari
 *
 */
@Repository
@Transactional
public class SellerDAOImpl extends BaseDAO<SellerDetailEntity> implements SellerDAO {
	
	private static final Logger logger = LoggerFactory.getLogger(SellerDAOImpl.class);

	public SellerDAOImpl() {
		super(SellerDetailEntity.class);
	}

	@Override
	public SellerDetailEntity findByContactNo(Long contNo) {
		Criteria criteria = getSession().createCriteria(getPersistentClass());
		criteria.add(Restrictions.eq("contactNo", contNo));
		return (SellerDetailEntity) criteria.uniqueResult();
	}

	@Override
	public Map<String, Object> ApplicationStatusSearchBasis(Integer pageNumber, Integer perPageRecords , Long pattern) {
		Map<String, Object> mapObject = new HashMap<String, Object>();
		Criteria criteria = getSession().createCriteria(getPersistentClass());
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.addOrder((Order.asc("sellerDtlId")));
		Integer totalRecords = 0;
		if (!ObjectsUtil.isNull(pattern)){
			 criteria.add(Restrictions.sqlRestriction("contact_no LIKE '"+pattern.toString()+"%'"));
			 totalRecords = criteria.list().size();
		}else{
			totalRecords = criteria.list().size();
		}
		if (perPageRecords != null && pageNumber != null && perPageRecords >0 && pageNumber>0) {
			criteria.setFirstResult((pageNumber - 1) * perPageRecords);
			criteria.setMaxResults(perPageRecords);
		}
		mapObject.put("sellerDetailEntities", criteria.list());
		mapObject.put("totalRecords", totalRecords);

		return mapObject;
	}

	@Override
	public SellerDetailEntity checkOtherSellerIfExistForMobileUpdate(Long contactNo, Long sellerId) {
		Criteria criteria = getSession().createCriteria(getPersistentClass());
		criteria.add(Restrictions.eq("contactNo", contactNo));
		criteria.add(Restrictions.ne("sellerDtlId", sellerId));
		return (SellerDetailEntity) criteria.uniqueResult();
	}

	@Override
	public Map<String, Object> getSellerSearch(Pageable pageable, Long pattern) {
		Criteria criteria = getSession().createCriteria(getPersistentClass());
		criteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
		criteria.addOrder((Order.asc("sellerDtlId")));
		if (!ObjectsUtil.isNull(pattern)){
			 criteria.add(Restrictions.sqlRestriction("contact_no LIKE '"+pattern.toString()+"%'"));
		}
		criteria.setFirstResult(pageable.getPageSize() * pageable.getPageNumber());
		criteria.setMaxResults(pageable.getPageSize());
		List<SellerDetailEntity> result = (List<SellerDetailEntity>) criteria.list();
		Map<String,Object> countResultMap = new HashMap<>(2);
		countResultMap.put(Constant.QUERY_RESULT, result);
		criteria.setFirstResult(0);
		Long count = (Long) criteria.setProjection(Projections.rowCount()).uniqueResult();
		logger.info("Total Count : "+count);
		countResultMap.put(Constant.TOTAL_COUNT, count);
		return countResultMap;
	}

	@Override
	public List<SellerDetailEntity> findBySellerDtlIds(List<Long> ids) {
		Criteria criteria = getSession().createCriteria(getPersistentClass());
		criteria.add(Restrictions.in("sellerDtlId", ids));
		if (ids != null && !ids.isEmpty()) {
			return (List<SellerDetailEntity>) criteria.list();
		} else {
			return new ArrayList<>(1);
		}
	}
	
	/**
	 * 
	 */
	@Override
	public SellerDetailEntity findBySellerId(Long id) {
		Criteria criteria = getSession().createCriteria(getPersistentClass());
		criteria.add(Restrictions.eq("sellerDtlId", id));
		return (SellerDetailEntity) criteria.uniqueResult();
	}

	@Override
	public List<SellerDetailEntity> findAllActiveSeller() {
		Criteria criteria = getSession().createCriteria(getPersistentClass());
		criteria.add(Restrictions.eq("status", Constant.SELLER_ACTIVE_STATUS));
		return (List<SellerDetailEntity>) criteria.list();
	}
}
