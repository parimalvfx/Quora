package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.AnswerRequest;
import com.upgrad.quora.api.model.AnswerResponse;
import com.upgrad.quora.service.business.AnswerBusinessService;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/")
public class AnswerController {

    @Autowired
    private AnswerBusinessService answerBusinessService;

    @RequestMapping(method = RequestMethod.POST, path = "/question/{questionId}answer/create", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerResponse> createAnswer(final AnswerRequest answerRequest, @PathVariable("questionId") final String questionId, @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, InvalidQuestionException {
        final AnswerEntity answerEntity = new AnswerEntity();
        answerEntity.setAnswer(answerRequest.getAnswer());

        final AnswerEntity createdAnswerEntity = answerBusinessService.createAnswer(answerEntity, questionId, authorization);
        AnswerResponse answerResponse = new AnswerResponse().id(createdAnswerEntity.getUuid()).status("ANSWER CREATED");
        return new ResponseEntity<AnswerResponse>(answerResponse, HttpStatus.CREATED);
    }

    @RequestMapping(method = RequestMethod.POST, path = "/answer/delete/{answerId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<AnswerResponse> deleteAnswer(@PathVariable("questionId") final String questionId, @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, AnswerNotFoundException {


        answerBusinessService.deleteAnswer(questionId, authorization);
        AnswerResponse answerResponse = new AnswerResponse().id(questionId).status("ANSWER DELETED");
        return new ResponseEntity<AnswerResponse>(answerResponse, HttpStatus.NO_CONTENT);
    }

    @RequestMapping("/all/{questionId}")
    public ResponseEntity<List<AnswerResponse>> getAllAnswersToQuestion (@PathVariable("questionId") final String questionId, @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, InvalidQuestionException {


        List<AnswerEntity> allAnswers = answerBusinessService.getAllAnswersToQuestion(questionId, authorization);

        List<AnswerResponse> allAnswersResponse = new ArrayList<AnswerResponse>();

        for(int i=0;i<allAnswers.size();i++){
            AnswerResponse ar = new AnswerResponse().id(allAnswers.get(i).getUuid()+allAnswers.get(i).getAnswer()).status("ANSWER TO QUESTION");
            allAnswersResponse.add(ar);
        }
        return new ResponseEntity<List<AnswerResponse>>(allAnswersResponse,HttpStatus.FOUND);
    }
}
