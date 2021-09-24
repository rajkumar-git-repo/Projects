package com.mli.dao;


import org.springframework.data.repository.CrudRepository;

import com.mli.entity.UserEntity;

public interface UserRepository extends CrudRepository<UserEntity, Long>
{
    UserEntity findByUserName(String userName);
    UserEntity findByUserId(Long id);

}
