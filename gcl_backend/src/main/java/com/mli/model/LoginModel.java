package com.mli.model;

public class LoginModel extends BaseModel {

	private static final long serialVersionUID = -2050950934929700466L;
	
	@Override
	public String toString() {
		return "LoginModel [username=" + username + ", password=" + password + ", contactNo=" + contactNo + "]";
	}

	private String username;
	private String password;
	private String contactNo;

	public LoginModel() {
	}

	public LoginModel(String username, String password) {
		this.setUsername(username);
		this.setPassword(password);
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

}
