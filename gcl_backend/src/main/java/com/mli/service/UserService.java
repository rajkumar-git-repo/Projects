package com.mli.service;

import java.util.List;

import com.mli.enums.UserType;
import com.mli.model.UserModel;
import com.mli.model.response.SaveUpdateResponse;

public interface UserService {
	
	    public UserModel findByUserId(Long id);

	    public UserModel findByUserName(String userName);

	    public List<UserModel> findAllUsers(String userType);
	    
	    public List<UserType> getUserRoles(String userDept); 
	    
	    public SaveUpdateResponse changePswd(String id, String oldPswd, String newPswd);
	        
	    public SaveUpdateResponse registerUser(UserModel userModel, String userName);


	    boolean isUserExists(String aadharNumber, UserType userType);

	    public List<String> bcryptPasswordForUsers(String userType, String userName);
	    
	    public SaveUpdateResponse setPassword(String userName, String newPswd);
}
