package com.mli.dao;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;

import com.mli.entity.CustomerDetailsEntity;
import com.mli.entity.SellerDetailEntity;
import com.mli.enums.Status;

/**
 * @author Nikhilesh.Tiwari
 *
 */
public interface CustomerDetailsDAO extends GenericDAO<CustomerDetailsEntity>{

	public CustomerDetailsEntity findByProposalNumber(String proposalNumber);
	public List<CustomerDetailsEntity> findByDate(Long date);
	public List<CustomerDetailsEntity> findPendingProposalsBySellerId(Long id, Status status);
	public Integer findPendingProposalsCountBySellerId(Long id, Status status);
	List<CustomerDetailsEntity> findByDateBankNameNdAppStatus(Long date, String masterPolicyHolderName,
			Status appstatus);
	public List<CustomerDetailsEntity> findByLoanAppNumber(String loanAppNumber,String status);
	
	/**
	 * @author Devendra.Kumar
	 */
	public List<CustomerDetailsEntity> findBySellerContactAndAppStatus(SellerDetailEntity sellerDetailEntity,Status appStep);
	public Map<String, Object> search(Long slrId,String param, String status, Pageable pageable,String isFinance);
	public List<CustomerDetailsEntity> getAllCustomerForExcel(Long from,Long to,String masterPlcy, Status appStatus);
	
	public List<CustomerDetailsEntity> getAllCustomerForExcel(Long from,Long to);
	CustomerDetailsEntity findByLoanAppNumber(String loanAppNumber);
	CustomerDetailsEntity findByLoanAppNumberAndMphType(String loanAppNumber,String mphType);
}
