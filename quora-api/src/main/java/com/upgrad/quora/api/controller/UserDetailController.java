package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.UserDetailsResponse;
import com.upgrad.quora.service.business.UserBusinessService;
import com.upgrad.quora.service.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class UserDetailController {

    @Autowired
    private UserBusinessService userBusinessService;

    public ResponseEntity<UserDetailsResponse> getUser(final String userId){
        final UserEntity userEntity = userBusinessService.getUser(userId);
        UserDetailsResponse userDetailsResponse = new UserDetailsResponse()
                                                    .firstName(userEntity.getFirstName())
                                                    .lastName(userEntity.getLastName())
                                                    .userName(userEntity.getUserName())
                                                    .emailAddress(userEntity.getEmail())
                                                    .country(userEntity.getCountry())
                                                    .aboutMe(userEntity.getAboutMe())
                                                    .dob(userEntity.getDob())
                                                    .contactNumber(userEntity.getContactNumber());
        return new ResponseEntity<UserDetailsResponse>(userDetailsResponse, HttpStatus.OK);
    }
}
