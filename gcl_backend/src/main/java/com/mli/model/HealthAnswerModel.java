package com.mli.model;

import java.util.Comparator;

/**
 * @author nikhilesh
 *
 */
public class HealthAnswerModel {

	private Integer questionId;
	private String questionAns;
	private String ciRiderDsc;
	
	

	public Integer getQuestionId() {
		return questionId;
	}

	public void setQuestionId(Integer questionId) {
		this.questionId = questionId;
	}

	public String getQuestionAns() {
		return questionAns;
	}

	public void setQuestionAns(String questionAns) {
		this.questionAns = questionAns;
	}

	public String getCiRiderDsc() {
		return ciRiderDsc;
	}

	public void setCiRiderDsc(String ciRiderDsc) {
		this.ciRiderDsc = ciRiderDsc;
	}
	
	  public static Comparator<HealthAnswerModel> healthAnswere = new Comparator<HealthAnswerModel>() {
			public int compare(HealthAnswerModel s1, HealthAnswerModel s2) {
			   int no1 = s1.getQuestionId();
			   int no2 = s2.getQuestionId();
			   /*For ascending order*/
			   return no1-no2;
			  
		   }};



	@Override
	public String toString() {
		return "HealthAnswerModel [questionId=" + questionId + ", questionAns=" + questionAns + ", ciRiderDsc="
				+ ciRiderDsc + "]";
	}


}
