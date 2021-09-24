package com.mli.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mli.dao.BaseDAO;
import com.mli.dao.CommonFileUploadDao;
import com.mli.entity.CommonFileUploadEntity;
import com.mli.modal.FileUploadModel;


@Repository
@Transactional
public class CommonFileUploadImpl extends BaseDAO<CommonFileUploadEntity> implements CommonFileUploadDao {

	public CommonFileUploadImpl() {
		super(CommonFileUploadEntity.class);
	}

	@Override
	@SuppressWarnings("unchecked")
	@Transactional
	public List<CommonFileUploadEntity> getProposals(String proposalNumber) {
		Criteria criteria = getSession().createCriteria(getPersistentClass());
		criteria.add(Restrictions.eq("proposalNumber", proposalNumber));
		criteria.setResultTransformer(Criteria.ROOT_ENTITY);
		return (List<CommonFileUploadEntity>) criteria.list();
	}

	@Override
	public List<FileUploadModel> deleteFileDocument(String proposalNumber, String url) {
		try {
			Query q = getSession().createQuery(
					"delete from CommonFileUploadEntity where proposalNumber = :proposalNumber and fileUrl=:url");
			q.setParameter("proposalNumber", proposalNumber);
			q.setParameter("url", url);
			q.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<FileUploadModel> getProposalsNumber(String proposalNumber) {
		Criteria criteria = getSession().createCriteria(getPersistentClass());
		criteria.add(Restrictions.eq("proposalNumber", proposalNumber));
		criteria.setResultTransformer(Criteria.ROOT_ENTITY);
		return (List<FileUploadModel>) criteria.list();
	}

	@Override
	public List<CommonFileUploadEntity> getAllRecords(Long list) {
		 Criteria criteria = getSession().createCriteria(getPersistentClass());
		  criteria.add(Restrictions.eq("createdOn", list));
		  criteria.setResultTransformer(Criteria.ROOT_ENTITY); return
		  (List<CommonFileUploadEntity>) criteria.list();
	}

}
