package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NamedQuery;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

@Repository
public class UserDao {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * This methods creates new user from given UserEntity object
     *
     * @param userEntity the UserEntity object from which new user will be created
     *
     * @return UserEntity object
     */
    public UserEntity createUser(UserEntity userEntity) {
        entityManager.persist(userEntity);
        return userEntity;
    }

    /**
     * This method helps find existing user by user name
     *
     * @param userName the user name which will be searched in database for existing user
     *
     * @return UserEntity object if user with requested user name exists in database
     */
    public UserEntity getUserByUserName(final String userName) {
        try {
            return entityManager.createNamedQuery("userByUserName", UserEntity.class).setParameter("userName", userName).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    /**
     * This method helps find existing user by email id
     *
     * @param email the email id which will be searched in database for existing user
     *
     * @return UserEntity object if user with requested email id exists in database
     */
    public UserEntity getUserByEmail(final String email) {
        try {
            return entityManager.createNamedQuery("userByEmail", UserEntity.class).setParameter("email", email).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    /**
     * This method helps find existing user by User ID
     *
     * @param userId the  id which will be searched in database for existing user
     *
     * @return UserEntity object if user with requested id exists in database
     */
    public UserEntity getUser(final String userId){
        try {
            return entityManager.createNamedQuery("userById", UserEntity.class).setParameter("id", userId).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }
}
