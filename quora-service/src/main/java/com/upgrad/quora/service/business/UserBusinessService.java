package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserBusinessService {

      @Autowired
    private UserDao userDao;


    /**
     * This method helps to get details of a user
     *
     * @param userId the UserEntity object from which user details will be retrieved
     *
     * @return UserEntity object
     *
     * @throws AuthorizationFailedException if validation for signed in user failed
     *
     * @throws UserNotFoundException if get details for user is not found in database
     */
    public UserEntity getUser(final long userId, final String authorizationToken) throws AuthorizationFailedException, UserNotFoundException{

        UserAuthEntity userAuthEntity = userDao.getUserAuthToken(authorizationToken);
        UserAuthEntity userLogoutAt = userDao.getLogoutAt(authorizationToken);
        UserEntity userEntity = userDao.getUser(userId);

        if(userAuthEntity == null){
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }

        if(userLogoutAt != null && userAuthEntity == null){
            throw  new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to get user details");
        }

        if(userEntity == null){
            throw new UserNotFoundException("USR-001","User with entered uuid does not exist");
        }
        return  userEntity;

    }
}
