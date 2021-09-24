package com.mli.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import com.mli.enums.UserType;

@Entity
@Table(name = "users")
public class UserEntity extends BaseEntity {

	private static final long serialVersionUID = -4324573381389970662L;

	@Id
	@Column(name = "user_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long userId;

	@Column(name = "user_name", unique = true, length = 55)
	private String userName;

	@Column(name = "password")
	private String password;

	@Column(name = "first_name", length = 55)
	private String firstName;

	@Column(name = "last_name", length = 55)
	private String lastName;

	@Column(name = "user_type", length = 50)
	@Enumerated(EnumType.STRING)
	private UserType userType;

	@Column(name = "email")
	private String email;

	@Column(name = "mobile", length = 20)
	private String mobile;

	@Column(name = "status")
	private Boolean status;

	@Column(name = "last_login")
	private Long lastLogin;
	
	@Column(name = "lock_count", columnDefinition = "int default 0")
	private int lockCount;
	
	@Column(name = "lock_timestamp")
	private Long lockTimestamp;
	
	@Column(name = "is_locked",columnDefinition = "boolean default false")
	private boolean locked;

	@Version
	@Column(name = "version")
	private long version;

	public long getVersion() {
		return version;
	}

	public void setVersion(long version) {
		this.version = version;
	}

	public UserEntity() {
	}

	public UserEntity(Long userId) {
		this.userId = userId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Long getLastLogin() {
		return lastLogin;
	}

	public void setLastLogin(Long lastLogin) {
		this.lastLogin = lastLogin;
	}

	public int getLockCount() {
		return lockCount;
	}

	public void setLockCount(int lockCount) {
		this.lockCount = lockCount;
	}

	public Long getLockTimestamp() {
		return lockTimestamp;
	}

	public void setLockTimestamp(Long lockTimestamp) {
		this.lockTimestamp = lockTimestamp;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}
}
