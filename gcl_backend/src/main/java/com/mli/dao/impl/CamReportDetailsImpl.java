package com.mli.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mli.dao.BaseDAO;
import com.mli.dao.CamReportDetailsDao;
import com.mli.entity.CamReportDetailsEntity;
import com.mli.modal.CamResponseModel;

@Repository
@Transactional
public class CamReportDetailsImpl extends BaseDAO<CamReportDetailsEntity> implements CamReportDetailsDao {


	public CamReportDetailsImpl() {
		super(CamReportDetailsEntity.class);
	}

	/**
	 * fetch list of CamReportDetailsEntity based on proposal number
	 * @param proposalNumber
	 * @return
	 * @author rajkumar
	 */
	@SuppressWarnings("unchecked")
	@Transactional
	@Override
	public List<CamReportDetailsEntity> getProposals(String proposalNumber) {
		Criteria criteria = getSession().createCriteria(getPersistentClass());
		criteria.add(Restrictions.eq("proposalNumber", proposalNumber));
		criteria.setResultTransformer(Criteria.ROOT_ENTITY);
		return (List<CamReportDetailsEntity>) criteria.list();
	}
	
	/**
	 * delete cam report from db based on url and proposal number
	 * @param proposalNumber
	 * @param url
	 * @return
	 * @author rajkumar
	 */
	@Transactional
	@Override
	public List<CamResponseModel> deleteAdditonalDocument(String proposalNumber, String url) {
		try {
			Query q = getSession().createQuery(
					"delete from CamReportDetailsEntity where proposalNumber = :proposalNumber and camReportUrls=:url");
			q.setParameter("proposalNumber", proposalNumber);
			q.setParameter("url", url);
			q.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	/**
	 * fetch cam report based on proposal number
	 * @param proposalNumber
	 * @return
	 * @author rajkumar
	 */
	@Override
	public List<CamResponseModel> getProposalsNumber(String proposalNumber) {
		Criteria criteria = getSession().createCriteria(getPersistentClass());
		criteria.add(Restrictions.eq("proposalNumber", proposalNumber));
		criteria.setResultTransformer(Criteria.ROOT_ENTITY);
		return (List<CamResponseModel>) criteria.list();
	}


	  /**
	   * fetch list of CamReportDetailsEntity based on list of createdOn date
	   * @param list
	   * @return
	   * @author rajkumar
	   */
	  @Transactional
	  @Override 
	  public List<CamReportDetailsEntity> getAllRecords(Long list) {
	  Criteria criteria = getSession().createCriteria(getPersistentClass());
	  criteria.add(Restrictions.eq("createdOn", list));
	  criteria.setResultTransformer(Criteria.ROOT_ENTITY); return
	  (List<CamReportDetailsEntity>) criteria.list(); }
	 


}
