package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
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
     * This method helps find existing user by email id
     *
     * @param uid the user id which will be searched in database for existing user
     *
     * @return UserEntity object if user with requested email id exists in database
     */
    public UserEntity getUserById(String uid){

        try{
            return entityManager.createNamedQuery("userById",UserEntity.class).setParameter("uuid",uid).getSingleResult();
        } catch (NoResultException nre){
            return null;
        }


    }


    public UserEntity deleteUser(String uuid) {

        try{
            return entityManager.createNamedQuery("deleteUserById",UserEntity.class).setParameter("uuid",uuid).getSingleResult();
        } catch (NoResultException nre){
            return null;
        }
    }

    public UserAuthEntity createAuthToken(final UserAuthEntity userAuthEntity) {
        entityManager.persist(userAuthEntity);
        return userAuthEntity;
    }

    public void updateUser(final UserEntity updatedUserEntity) {
        entityManager.merge(updatedUserEntity);
    }

    public UserAuthEntity getUserAuthToken(final String accessToken) {
        try {
            return entityManager.createNamedQuery("userAuthTokenByAccessToken", UserAuthEntity.class).setParameter("accessToken", accessToken).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public boolean isAdmin(String uid){
        try{
            String value = entityManager.createNamedQuery("getUserRole",UserEntity.class).setParameter("uuid",uid).toString();
            if(value == "admin"){
                return true;
            }
        } catch (NoResultException nre){
            return false;
        }
        return false;
    }
}
