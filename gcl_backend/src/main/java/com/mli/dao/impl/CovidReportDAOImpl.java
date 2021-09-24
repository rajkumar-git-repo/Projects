package com.mli.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mli.dao.BaseDAO;
import com.mli.dao.CovidReportDAO;
import com.mli.entity.CovidReportEntity;

@Repository
@Transactional
public class CovidReportDAOImpl extends BaseDAO<CovidReportEntity> implements CovidReportDAO {


	public CovidReportDAOImpl() {
		super(CovidReportEntity.class);
	}

	@Override
	public List<CovidReportEntity> findAllReport(String proposalNumber) {
		Criteria criteria = getSession().createCriteria(getPersistentClass());
		criteria.add(Restrictions.eq("proposalNumber", proposalNumber));
		criteria.setResultTransformer(Criteria.ROOT_ENTITY);
		return (List<CovidReportEntity>) criteria.list();
	}

	@Override
	public int deleteByFileUrl(String proposalNumber, String url) {
		try {
			Query q = getSession().createQuery("delete from CovidReportEntity where proposalNumber = :proposalNumber and fileUrl=:url");
			q.setParameter("proposalNumber", proposalNumber);
			q.setParameter("url", url);
			return q.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public List<CovidReportEntity> findByFileType(String proposalNumber, String fileType) {
		Criteria criteria = getSession().createCriteria(getPersistentClass());
		criteria.add(Restrictions.eq("proposalNumber", proposalNumber));
		criteria.add(Restrictions.eq("fileType", fileType));
		criteria.setResultTransformer(Criteria.ROOT_ENTITY);
		return (List<CovidReportEntity>) criteria.list();
	}

}
