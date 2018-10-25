package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.AnswerEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public class AnswerDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private AnswerEntity answerEntity;

    public AnswerEntity createAnswer(AnswerEntity answerEntity) {
        entityManager.persist(answerEntity);
        return answerEntity;
    }

    public AnswerEntity getAnswerByUuid(final String id) {
        try {
            return entityManager.createNamedQuery("questionEntityByUuid", AnswerEntity.class).setParameter("uuid",id).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public void userQuestionDelete(final String id) {
        answerEntity = getAnswerByUuid(id);
        entityManager.remove(answerEntity);
    }


    public List<AnswerEntity> getAllAnswersToQuestion(final String id) {
        try {
            return entityManager.createNamedQuery("answerEntityByQuestionId", AnswerEntity.class).setParameter("uuid", id).getResultList();
        } catch (NoResultException nre) {
            return null;
        }
    }
}
