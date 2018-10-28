package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class QuestionDao {

    @PersistenceContext
    private EntityManager entityManager;

    public QuestionEntity createQuestion(QuestionEntity questionEntity) {
        entityManager.persist(questionEntity);
        return questionEntity;
    }

    public QuestionEntity getQuestionById(final long questionId) {
        try {
            return entityManager.createNamedQuery("questionEntityById", QuestionEntity.class).setParameter("uuid", questionId).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public List<QuestionEntity> getAllQuestionsByUser(final String uuid){
        try {
            return entityManager.createNamedQuery("questionByUserId", QuestionEntity.class).setParameter("uuid", uuid).getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public QuestionEntity getQuestionByUuid(final String uuid) {
        try {
            return entityManager.createNamedQuery("questionEntityByUuid", QuestionEntity.class).setParameter("uuid",uuid).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public void userQuestionDelete(final String uuid) {
        QuestionEntity questionEntity = getQuestionByUuid(uuid);
        entityManager.remove(questionEntity);
    }

    public QuestionEntity editQuestionContent(final QuestionEntity questionEntity) {
        return entityManager.merge(questionEntity);
    }

    public List<QuestionEntity> getAllQuestions() {
        try {
            return entityManager.createNamedQuery("allQuestions", QuestionEntity.class).getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }
}
