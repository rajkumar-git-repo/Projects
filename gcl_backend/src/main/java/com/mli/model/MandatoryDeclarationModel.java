package com.mli.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author Nikhilesh.Tiwari
 *
 */

/**
 * @author nikhilesh
 *
 */
/**
 * @author nikhilesh
 *
 */
@JsonInclude(Include.NON_NULL)
public class MandatoryDeclarationModel extends BaseModel {

	private static final long serialVersionUID = 7086812207466745408L;

	private String policyHolderName;
	private Boolean isMandatoryDeclaration;
	private Long SignedDate;
	private String place;

	public String getPolicyHolderName() {
		return policyHolderName;
	}

	public void setPolicyHolderName(String policyHolderName) {
		this.policyHolderName = policyHolderName;
	}

	public Boolean getIsMandatoryDeclaration() {
		return isMandatoryDeclaration;
	}

	public void setIsMandatoryDeclaration(Boolean isMandatoryDeclaration) {
		this.isMandatoryDeclaration = isMandatoryDeclaration;
	}

	public Long getSignedDate() {
		return SignedDate;
	}

	public void setSignedDate(Long signedDate) {
		SignedDate = signedDate;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}
}
