package com.finalproject.finalproject.controller;

import com.finalproject.finalproject.model.dto.OfferDTOS.OfferCreateDTO;
import com.finalproject.finalproject.model.dto.OfferDTOS.OfferDTO;
import com.finalproject.finalproject.model.dto.OfferEditDTO;
import com.finalproject.finalproject.service.OfferService;
import com.finalproject.finalproject.utility.UserUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Set;

@RestController
public class OfferControler {

    @Autowired
    OfferService offerService;
    @Autowired
    SessionManager sessionManager;
    @PostMapping("/offer")
    public ResponseEntity<OfferDTO> createOffer(HttpServletRequest request, @RequestBody OfferCreateDTO offer){
        sessionManager.verifyUser(request);
        return ResponseEntity.ok(offerService.createOffer(offer, (Integer) request.getSession().getAttribute(UserController.USER_ID)));
    }

    @PutMapping("/offer")
    public ResponseEntity<OfferDTO> editOffer(HttpServletRequest request, @RequestBody OfferEditDTO offerEditDTO){
        sessionManager.verifyUser(request);
        return ResponseEntity.ok(offerService.editOffer(offerEditDTO,(Integer) request.getSession().getAttribute(UserController.USER_ID)));
    }

    @DeleteMapping("/offer")
    public ResponseEntity<OfferDTO> deleteOffer(@RequestParam (name = "id") int postId, HttpServletRequest request){
        sessionManager.verifyUser(request);
        return ResponseEntity.ok(offerService.deleteById(postId, (Integer) request.getSession().getAttribute(UserController.USER_ID)));
    }

    @GetMapping("/offer")
    public ResponseEntity<List<OfferDTO>> getAllOffersForUser(HttpServletRequest request){
        sessionManager.verifyUser(request);
        return ResponseEntity.ok(offerService.getAllOffersForUser((Integer) request.getSession().getAttribute(UserController.USER_ID)));
    }

}
