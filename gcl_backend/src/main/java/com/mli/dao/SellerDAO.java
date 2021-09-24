package com.mli.dao;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;

import com.mli.entity.SellerDetailEntity;

/**
 * @author Nikhilesh.Tiwari
 *
 */
public interface SellerDAO extends GenericDAO<SellerDetailEntity> {


	public SellerDetailEntity findByContactNo(Long contNo);

	public Map<String, Object> ApplicationStatusSearchBasis(Integer pageNumber, Integer perPageRecords, Long pattern);
	
	public SellerDetailEntity checkOtherSellerIfExistForMobileUpdate(Long contactNo, Long sellerId);
	
	public Map<String, Object> getSellerSearch(Pageable pageable, Long pattern);
	
	public List<SellerDetailEntity> findBySellerDtlIds(List<Long> ids);

	SellerDetailEntity findBySellerId(Long id);

    List<SellerDetailEntity> findAllActiveSeller();
}
