package com.mli.dao;

import com.mli.entity.CreditCardMandatoryEntity;

public interface CreditCardMandatoryDAO extends GenericDAO<CreditCardMandatoryEntity>{

	CreditCardMandatoryEntity findbyCreditCardCustomerId(Long creditCardCustomerId);

}
