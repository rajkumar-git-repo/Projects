package com.mli.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mli.dao.UserRepository;
import com.mli.entity.UserEntity;
import com.mli.model.CustomUserDetail;

/**
 * @author Nikhilesh.Tiwari
 *
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;
	
	/**
	 * check login user is valid user or not
	 * @param username
	 * @return
	 * @throws UsernameNotFoundException
	 * @author rajkumar
	 */
    @Override
	public CustomUserDetail loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity user = userRepository.findByUserName(username);

		if (user == null) {
			//throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
			return null;
		} else {
			List<String> userRoles = new ArrayList<String>();
			userRoles.add(user.getUserType().toString());
			return new CustomUserDetail(user.getUserId(), user.getPassword(), user.getUserName(), user.getStatus(),
					userRoles);
		}
	}
}
