package com.mli.dao;

import com.mli.entity.UserEntity;
import com.mli.enums.UserType;

public interface UserDAO extends GenericDAO<UserEntity> {

    UserEntity findByUserId(Long id);

    UserEntity findByUserName(String name);
    
    public UserEntity findByUserNameNdUserType(String userName , UserType userType);
}