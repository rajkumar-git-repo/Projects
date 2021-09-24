package com.mli.dao;

import com.mli.entity.ProposalNumberEntity;

/**
 * @author Nikhilesh.Tiwari
 *
 */
public interface ProposalNumberDAO extends GenericDAO<ProposalNumberEntity> {

	public ProposalNumberEntity findByProposalNumberId(Long i);

	
}
