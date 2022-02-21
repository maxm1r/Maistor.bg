package com.finalproject.finalproject.controller;

import com.finalproject.finalproject.model.dto.OfferDTOS.OfferCreateDTO;
import com.finalproject.finalproject.model.dto.OfferDTOS.OfferDTO;
import com.finalproject.finalproject.model.dto.OfferEditDTO;
import com.finalproject.finalproject.service.OfferService;
import com.finalproject.finalproject.utility.UserUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
public class OfferControler {

    @Autowired
    OfferService offerService;

    @PostMapping("offer/{id}")
    public ResponseEntity<OfferDTO> createOffer(HttpSession session, HttpServletRequest request, @RequestBody OfferCreateDTO offer){
        UserUtility.validateLogin(session,request);
        return ResponseEntity.ok(offerService.createOffer(offer, (Integer) session.getAttribute(UserController.USER_ID)));
    }
    @PostMapping("offer/{id}")
    public ResponseEntity<OfferDTO> editOffer(HttpSession session, HttpServletRequest request, @RequestBody OfferEditDTO offerEditDTO){
        UserUtility.validateLogin(session,request);
        return ResponseEntity.ok(offerService.editOffer(offerEditDTO,(Integer) session.getAttribute(UserController.USER_ID)));
    }
}
