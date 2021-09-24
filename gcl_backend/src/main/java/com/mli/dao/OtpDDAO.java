package com.mli.dao;

import java.util.List;

import com.mli.entity.OTPHistoryEntity;
import com.mli.enums.Status;


/**
 * @author Nikhilesh.Tiwari
 *
 */
public interface OtpDDAO extends GenericDAO<OTPHistoryEntity>{

	public OTPHistoryEntity findByContNoAndOtp(Long contNo, Integer otp);

	public OTPHistoryEntity findByContNo(Long contNo);

	public Integer removeOTPHistoryFromTable();
	
	public List<OTPHistoryEntity> findByContNoAndStatus(Long contNo,Status status);

}
