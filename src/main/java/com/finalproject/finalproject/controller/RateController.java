package com.finalproject.finalproject.controller;

import com.finalproject.finalproject.model.dto.RateDTO;
import com.finalproject.finalproject.model.dto.RateResponseDTO;
import com.finalproject.finalproject.service.RateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
public class RateController {
    @Autowired
    RateService rateService;
    @PostMapping("/rate/{raterId}/{ratedId}")
    public ResponseEntity<RateResponseDTO> rate(@PathVariable int raterId, @PathVariable int ratedId, @RequestBody RateDTO rating, HttpSession session, HttpServletRequest request){
        return ResponseEntity.ok(rateService.createRate(raterId,ratedId,rating,session,request));
    }

}
