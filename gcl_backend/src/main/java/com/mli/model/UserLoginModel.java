package com.mli.model;

/**
 * @author Nikhilesh.Tiwari
 *
 */
public class UserLoginModel {

	private Long contNumber;
	private Integer otp;
	private boolean regenerateOtp;
	private String userType;
	private String loginType;
	private TokenModel tokenDetails;
	private SellerDetailModel sellerDetailModel;

	public Long getContNumber() {
		return contNumber;
	}

	public void setContNumber(Long contNumber) {
		this.contNumber = contNumber;
	}

	public Integer getOtp() {
		return otp;
	}

	public void setOtp(Integer otp) {
		this.otp = otp;
	}

	public boolean isRegenerateOtp() {
		return regenerateOtp;
	}

	public void setRegenerateOtp(boolean regenerateOtp) {
		this.regenerateOtp = regenerateOtp;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getLoginType() {
		return loginType;
	}

	public void setLoginType(String loginType) {
		this.loginType = loginType;
	}

	public TokenModel getTokenDetails() {
		return tokenDetails;
	}

	public void setTokenDetails(TokenModel tokenDetails) {
		this.tokenDetails = tokenDetails;
	}

	public SellerDetailModel getSellerDetailModel() {
		return sellerDetailModel;
	}

	public void setSellerDetailModel(SellerDetailModel sellerDetailModel) {
		this.sellerDetailModel = sellerDetailModel;
	}

}
