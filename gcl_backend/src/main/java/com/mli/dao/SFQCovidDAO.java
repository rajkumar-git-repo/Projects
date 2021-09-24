package com.mli.dao;

import com.mli.entity.SFQCovidEntity;

public interface SFQCovidDAO extends GenericDAO<SFQCovidEntity>{

	SFQCovidEntity findBySFQHealthDtlId(Long healthDtlId);
}
