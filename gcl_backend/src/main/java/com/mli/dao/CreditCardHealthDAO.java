package com.mli.dao;

import com.mli.entity.CreditCardHealthEntity;

public interface CreditCardHealthDAO extends GenericDAO<CreditCardHealthEntity>{

	CreditCardHealthEntity findByCustomerDtlId(Long customerId);

}
