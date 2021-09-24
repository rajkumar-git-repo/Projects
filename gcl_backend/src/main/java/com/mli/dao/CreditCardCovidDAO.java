package com.mli.dao;

import com.mli.entity.CreditCardCovidEntity;

public interface CreditCardCovidDAO extends GenericDAO<CreditCardCovidEntity>{

	CreditCardCovidEntity findByCreditCardHealthId(Long creditCardHealthId);

}
