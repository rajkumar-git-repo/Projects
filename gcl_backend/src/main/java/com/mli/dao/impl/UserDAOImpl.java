package com.mli.dao.impl;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mli.dao.BaseDAO;
import com.mli.dao.UserDAO;
import com.mli.entity.UserEntity;
import com.mli.enums.UserType;

@Repository
@Transactional
public class UserDAOImpl extends BaseDAO<UserEntity> implements UserDAO {

    public UserDAOImpl() {
        super(UserEntity.class);
    }

    @Override
    public UserEntity findByUserId(Long id) {
        Criteria criteria = getSession().createCriteria(getPersistentClass());
        criteria.add(Restrictions.eq("userId", id));
        return (UserEntity) criteria.uniqueResult();
    }

    @Override
    public UserEntity findByUserName(String userName) {
        Criteria criteria = getSession().createCriteria(getPersistentClass());
        criteria.add(Restrictions.eq("userName", userName));
        return (UserEntity) criteria.uniqueResult();
    }

   
    @Override
    public UserEntity findByUserNameNdUserType(String userName , UserType userType) {
        Criteria criteria = getSession().createCriteria(getPersistentClass());
        criteria.add(Restrictions.eq("userName", userName));
        criteria.add(Restrictions.eq("userType", userType));
        return (UserEntity) criteria.uniqueResult();
    }
    
}