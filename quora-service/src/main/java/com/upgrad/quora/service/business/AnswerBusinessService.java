package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.AnswerDao;
import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class AnswerBusinessService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private QuestionDao questionDao;

    @Autowired
    private AnswerDao answerDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity createAnswer(final AnswerEntity answerEntity, final String questionId, final String authorization) throws AuthorizationFailedException, InvalidQuestionException {
        UserAuthEntity userAuthEntity = userDao.getUserAuthToken(authorization);
        if (userAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }
        if (userAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to post an answer");
        }
        QuestionEntity questionEntity = questionDao.getQuestionByUuid(questionId);
        if (questionEntity == null) {
            throw new InvalidQuestionException("QUES-001", "The question entered is invalid");
        }

        answerEntity.setUuid(UUID.randomUUID().toString());
        answerEntity.setDate(ZonedDateTime.now());
        answerEntity.setUser(userAuthEntity.getUser());
        answerEntity.setQuestion(questionEntity);
        return answerDao.createAnswer(answerEntity);
    }

    public void deleteAnswer(final String uuid, final String authorization) throws AuthorizationFailedException, AnswerNotFoundException {

        UserAuthEntity userAuthEntity = userDao.getUserAuthToken(authorization);
        if (userAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "'User has not signed in");

        }
        if (userAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out");
        }
        if(userAuthEntity.getUser().getRole().equals("nonadmin") || !userAuthEntity.getUser().equals(answerDao.getAnswerByUuid(uuid).getUser())){
            throw new AuthorizationFailedException("ATHR-003", "Only the answer owner or admin can delete the answer");

        }
        if (answerDao.getAnswerByUuid(uuid)==null) {
            throw new AnswerNotFoundException("AND-001","Entered answer uuid does not exist.");
        }

        answerDao.userQuestionDelete(uuid);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public List<AnswerEntity> getAllAnswersToQuestion(final String questionId, final String authorization) throws AuthorizationFailedException, InvalidQuestionException {
        UserAuthEntity userAuthEntity = questionDao.getUserAuthToken(authorization);

        // Validate if user is signed in or not
        if (userAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }

        // Validate if user has signed out
        if (userAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to get the answers");
        }

        // Validate if requested question exist or not
        if (questionDao.getQuestionByUuid(questionId) == null) {
            throw new InvalidQuestionException("QUES-001", "The question with entered uuid whose details are to be seen does not exist");
        }

        return answerDao.getAllAnswersToQuestion(questionId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity editAnswerContent(final AnswerEntity answerEntity, final String authorization) throws AuthorizationFailedException, AnswerNotFoundException {
        UserAuthEntity userAuthEntity = userDao.getUserAuthToken(authorization);

        // Validate if user is signed in or not
        if (userAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }

        // Validate if user has signed out
        if (userAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to edit an answer");
        }

        // Validate if requested answer exist or not
        AnswerEntity existingAnswerEntity = answerDao.getAnswerByUuid(answerEntity.getUuid());
        if (existingAnswerEntity == null) {
            throw new AnswerNotFoundException("ANS-001", "Entered answer uuid does not exist");
        }

        // Validate if current user is the owner of requested answer
        UserEntity currentUser = userAuthEntity.getUser();
        UserEntity answerOwner = answerDao.getAnswerByUuid(answerEntity.getUuid()).getUser();
        if (currentUser.getId() != answerOwner.getId()) {
            throw new AuthorizationFailedException("ATHR-003", "Only the answer owner can edit the answer");
        }

        answerEntity.setId(existingAnswerEntity.getId());
        answerEntity.setDate(existingAnswerEntity.getDate());
        answerEntity.setUser(existingAnswerEntity.getUser());
        answerEntity.setQuestion(existingAnswerEntity.getQuestion());
        return answerDao.editAnswerContent(answerEntity);
    }
}
