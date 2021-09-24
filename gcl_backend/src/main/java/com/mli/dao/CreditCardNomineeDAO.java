package com.mli.dao;


import com.mli.entity.CreditCardNomineeEntity;

public interface CreditCardNomineeDAO extends GenericDAO<CreditCardNomineeEntity>{

	CreditCardNomineeEntity findByCustomerDtlId(Long customerId);

}
