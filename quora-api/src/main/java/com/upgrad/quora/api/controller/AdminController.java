package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.UserDeleteResponse;
import com.upgrad.quora.service.business.AdminBusinessService;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/")
public class AdminController {

    @Autowired
    private AdminBusinessService adminBusinessService;

    @RequestMapping(method = RequestMethod.DELETE, value="/admin/user/{userId}")
    public ResponseEntity<UserDeleteResponse> userDelete(@PathVariable("userId") final String userId,@RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, UserNotFoundException {


        adminBusinessService.deleteUser(userId,authorization);

        UserDeleteResponse deleteResponse = new UserDeleteResponse().id(userId).status("USER SUCCESSFULLY DELETED");
        return new ResponseEntity<UserDeleteResponse>(deleteResponse,HttpStatus.NO_CONTENT);
    }

}

