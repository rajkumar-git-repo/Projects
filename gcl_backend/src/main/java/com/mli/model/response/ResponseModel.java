
package com.mli.model.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.mli.model.SellerDetailModel;

/**
 * @author Nikhilesh.Tiwari
 *
 * @param <D>
 */
@JsonInclude(Include.NON_NULL)
public class ResponseModel<D> {

	public static final String SUCCESS = "SUCCESS";
	public static final String FAILED = "FAILURE";
	public static final String PENDING = "PENDING";

	private String status;
	private String message;
	private String loginType;
	private Integer draftCount;
	private D data;
	private String code;
	private Integer totalNoOfSeller;
	private Integer totalNoOfPage;
	private Integer itemPerPage;
	private Integer totalRecords;
	private SellerDetailModel sellerDetailModel;
	
	private String mph;

	public ResponseModel() {
	}

	public ResponseModel(String status) {
		this.status = status;
	}

	public ResponseModel(String status, String message, Integer statusCode, String loginType) {
		this.status = status;
		this.message = message;
		this.loginType = loginType;

	}

	public ResponseModel(String status, D data) {
		this.status = status;
		this.data = data;
	}

	public ResponseModel(String status, D data, String message) {
		this.status = status;
		this.data = data;
		this.message = message;
	}

	public ResponseModel(String status, D data, String message, Integer statusCode) {
		this.status = status;
		this.data = data;
		this.message = message;
	}
	
	public ResponseModel(String status, String message, Integer statusCode, String loginType,SellerDetailModel sellerDetailModel) {
		this.status = status;
		this.message = message;
		this.loginType = loginType;
		this.sellerDetailModel=sellerDetailModel;

	}
	
	

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public D getData() {
		return data;
	}

	public void setData(D data) {
		this.data = data;
	}

	public String getLoginType() {
		return loginType;
	}

	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getDraftCount() {
		return draftCount;
	}

	public void setDraftCount(Integer draftCount) {
		this.draftCount = draftCount;
	}

	public Integer getTotalNoOfSeller() {
		return totalNoOfSeller;
	}

	public void setTotalNoOfSeller(Integer totalNoOfSeller) {
		this.totalNoOfSeller = totalNoOfSeller;
	}

	public Integer getTotalNoOfPage() {
		return totalNoOfPage;
	}

	public void setTotalNoOfPage(Integer totalNoOfPage) {
		this.totalNoOfPage = totalNoOfPage;
	}

	public Integer getItemPerPage() {
		return itemPerPage;
	}

	public void setItemPerPage(Integer itemPerPage) {
		this.itemPerPage = itemPerPage;
	}

	public Integer getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(Integer totalRecords) {
		this.totalRecords = totalRecords;
	}

	public String getMph() {
		return mph;
	}

	public void setMph(String mph) {
		this.mph = mph;
	}

	public SellerDetailModel getSellerDetailModel() {
		return sellerDetailModel;
	}

	public void setSellerDetailModel(SellerDetailModel sellerDetailModel) {
		this.sellerDetailModel = sellerDetailModel;
	}
	
	

}
