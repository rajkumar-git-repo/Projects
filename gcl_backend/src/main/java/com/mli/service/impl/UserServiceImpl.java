package com.mli.service.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mli.dao.SellerDAO;
import com.mli.dao.UserDAO;
import com.mli.dao.UserRepository;
import com.mli.entity.SellerDetailEntity;
import com.mli.entity.UserEntity;
import com.mli.enums.UserType;
import com.mli.model.UserModel;
import com.mli.model.response.SaveUpdateResponse;
import com.mli.service.UserService;
import com.mli.utils.DateUtil;
import com.mli.utils.ObjectsUtil;

/**
 * @author Nikhilesh.Tiwari
 *
 */
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private SellerDAO sellerDAO;

	@Autowired
	private UserRepository userRepo;

	@Override
	public UserModel findByUserId(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public UserModel findByUserName(String userName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserModel> findAllUsers(String userType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<UserType> getUserRoles(String userDept) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 *  Change seller & other role login password
	 */
	
	@Transactional
	@Override
	public SaveUpdateResponse changePswd(String userId, String oldPswd, String newPswd) {
		UserEntity userEntity = userDAO.findByUserName(userId);
		SaveUpdateResponse response = new SaveUpdateResponse();
		if (!ObjectsUtil.isNull(userEntity)) {
			if (passwordEncoder.matches(oldPswd, userEntity.getPassword())) {
				userEntity.setPassword(passwordEncoder.encode(newPswd));
				userEntity.setModifiedOn(DateUtil.toCurrentUTCTimeStamp());
				userDAO.save(userEntity);
				response.setStatus(SaveUpdateResponse.SUCCESS);
				response.setMessage("Password changed successfully.");
			} else {
				throw new IllegalArgumentException("Invalid UserId or Old Password !!");
			}
		} else {
			throw new IllegalArgumentException("Invalid UserId !!");
		}
		return response;
	}

	@Override
	public SaveUpdateResponse registerUser(UserModel userModel, String userName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isUserExists(String aadharNumber, UserType userType) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<String> bcryptPasswordForUsers(String userType, String userName) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 *  Set new password for seller  
	 */
	
	@Override
	@Transactional
	public SaveUpdateResponse setPassword(String userName, String newPswd) {
		UserEntity userEntity = userDAO.findByUserName(userName);
		SaveUpdateResponse response = new SaveUpdateResponse();
		if (!ObjectsUtil.isNull(userEntity)) {
			userEntity.setPassword(passwordEncoder.encode(newPswd));
			userDAO.saveOrUpdate(userEntity);
			if (userEntity.getUserType().equals(UserType.ROLE_SELLER)) {
				SellerDetailEntity sellerEntity = new SellerDetailEntity();
				sellerEntity = sellerDAO.findByContactNo(Long.parseLong(userName));
				sellerEntity.setIsPasswordSet(true);
				sellerDAO.saveOrUpdate(sellerEntity);
			}
			response.setStatus(SaveUpdateResponse.SUCCESS);
			response.setMessage("Password set successfully.");

		} else {
			throw new IllegalArgumentException("Invalid UserId !!");
		}
		return response;
	}

}
