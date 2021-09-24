package com.mli.dao;

import java.util.List;

import com.mli.entity.CIRiderQuestionsMaster;


/**
 * @author nikhilesh
 *
 */
public interface CIRiderQuestionsMasterDAO extends GenericDAO<CIRiderQuestionsMaster>{

	public List<CIRiderQuestionsMaster> getAllQuestions();
}
