package com.finalproject.finalproject.controller;

import com.finalproject.finalproject.model.dto.OfferDTOS.OfferCreateDTO;
import com.finalproject.finalproject.model.dto.OfferDTOS.OfferDTO;
import com.finalproject.finalproject.model.dto.OfferEditDTO;
import com.finalproject.finalproject.service.OfferService;
import com.finalproject.finalproject.utility.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class OfferControler {

    @Autowired
    OfferService offerService;
    @Autowired
    SessionManager sessionManager;
    @PostMapping("/offers")
    public ResponseEntity<OfferDTO> createOffer(HttpServletRequest request, @RequestBody OfferCreateDTO offer){
        sessionManager.verifyUser(request);
        return ResponseEntity.ok(offerService.createOffer(offer, (Integer) request.getSession().getAttribute(SessionManager.USER_ID)));
    }

    @PutMapping("/offers")
    public ResponseEntity<OfferDTO> editOffer(HttpServletRequest request, @RequestBody OfferEditDTO offerEditDTO){
        sessionManager.verifyUser(request);
        return ResponseEntity.ok(offerService.editOffer(offerEditDTO,(Integer) request.getSession().getAttribute(SessionManager.USER_ID)));
    }

    @DeleteMapping("/offers")
    public ResponseEntity<OfferDTO> deleteOffer(@RequestParam (name = "id") int postId, HttpServletRequest request){
        sessionManager.verifyUser(request);
        return ResponseEntity.ok(offerService.deleteById(postId, (Integer) request.getSession().getAttribute(SessionManager.USER_ID)));
    }

    @GetMapping("/offers")
    public ResponseEntity<List<OfferDTO>> getAllOffersForUser(HttpServletRequest request){
        sessionManager.verifyUser(request);
        return ResponseEntity.ok(offerService.getAllOffersForUser((Integer) request.getSession().getAttribute(SessionManager.USER_ID)));
    }

    @GetMapping("{id}/offers")
    public ResponseEntity<List<OfferDTO>> getAllOffersForPost(@PathVariable int id,HttpServletRequest request){
        sessionManager.verifyUser(request);
        List<OfferDTO> offers = offerService.findAllByPost(id, (Integer) request.getSession().getAttribute(SessionManager.USER_ID));
        return ResponseEntity.ok(offers);
    }
}
