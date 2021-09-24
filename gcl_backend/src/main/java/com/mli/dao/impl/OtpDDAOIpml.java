package com.mli.dao.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.SQLQuery;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mli.dao.BaseDAO;
import com.mli.dao.OtpDDAO;
import com.mli.entity.OTPHistoryEntity;
import com.mli.enums.Status;


/**
 * @author Nikhilesh.Tiwari
 *
 */
@Repository
@Transactional
public class OtpDDAOIpml extends BaseDAO<OTPHistoryEntity> implements OtpDDAO {

	public OtpDDAOIpml() {
		super(OTPHistoryEntity.class);
	}

	@Override
	public OTPHistoryEntity findByContNoAndOtp(Long contNo, Integer otp) {
		Criteria criteria = getSession().createCriteria(getPersistentClass());
		criteria.add(Restrictions.eq("contNo", contNo));
		criteria.add(Restrictions.eq("otp", otp));
		criteria.add(Restrictions.eq("otpStatus", Status.CURRENT_OTP));
		return (OTPHistoryEntity) criteria.uniqueResult();
	}

	@Override
	public OTPHistoryEntity findByContNo(Long contNo) {
		Criteria criteria = getSession().createCriteria(getPersistentClass());
		criteria.add(Restrictions.eq("contNo", contNo));
		criteria.add(Restrictions.eq("otpStatus", Status.CURRENT_OTP));
		return (OTPHistoryEntity) criteria.uniqueResult();
	}

	@Override
	public Integer removeOTPHistoryFromTable() {
		StringBuilder queryString = new StringBuilder("delete from otp_history");
		SQLQuery query = getSession().createSQLQuery(queryString.toString());
		return  query.executeUpdate();
	}

	@Override
	public List<OTPHistoryEntity> findByContNoAndStatus(Long contNo, Status status) {
		Criteria criteria = getSession().createCriteria(getPersistentClass());
		criteria.add(Restrictions.eq("contNo", contNo));
		criteria.add(Restrictions.eq("otpStatus", status));
		return (List<OTPHistoryEntity>) criteria.list();
	}

}
