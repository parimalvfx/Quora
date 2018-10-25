package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class QuestionBusinessService {

    @Autowired
    private QuestionDao questionDao;
    @Autowired
    private UserDao userDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity createQuestion(QuestionEntity questionEntity, final String authorizationToken) throws AuthorizationFailedException {
        UserAuthEntity userAuthEntity = questionDao.getUserAuthToken(authorizationToken);
        if (userAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "'User has not signed in");
        }
        if (userAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to post a question");
        }

        questionEntity.setUser(userAuthEntity.getUser());
        return questionDao.createQuestion(questionEntity);
    }

    public List<QuestionEntity> getAllQuestionsByUser(final String uuid, final String authorizationToken) throws AuthorizationFailedException, UserNotFoundException{

        UserAuthEntity userAuthEntity = questionDao.getUserAuthToken(authorizationToken);
        if (userAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "'User has not signed in");
        }
        if (userAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to post a question");
        }
        if(userAuthEntity.getUser().getRole().equals("nonadmin") || !userAuthEntity.getUser().equals(questionDao.getQuestionByUuid(uuid).getUser())){
            throw new AuthorizationFailedException("ATHR-003", "Only the question owner or admin can delete the question");

        }
        if (userDao.getUserByUuid(uuid)==null) {
            throw new UserNotFoundException("USR-001", "User with entered uuid to be deleted does not exist");
        }

        return questionDao.getAllQuestionsByUser(uuid);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void userQuestionDelete(final String questionId, final String authorization) throws InvalidQuestionException, AuthorizationFailedException {


        UserAuthEntity userAuthEntity = userDao.getUserAuthToken(authorization);
        if (userAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "'User has not signed in");

        }
        if (userAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out");
        }
        if(userAuthEntity.getUser().getRole().equals("nonadmin")){
            throw new AuthorizationFailedException("ATHR-003", "Unauthorized Access, Entered user is not an admin");
        }
        if (questionDao.getQuestionByUuid(questionId)==null) {
            throw new InvalidQuestionException("QUES-001","Entered question uuid does not exist.");
        }

        questionDao.userQuestionDelete(questionId);


    }
}
