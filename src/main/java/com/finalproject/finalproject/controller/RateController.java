package com.finalproject.finalproject.controller;

import com.finalproject.finalproject.model.dto.RateDTO;
import com.finalproject.finalproject.model.dto.RateResponseDTO;
import com.finalproject.finalproject.service.RateService;
import com.finalproject.finalproject.utility.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class RateController {
    @Autowired
    RateService rateService;
    @Autowired
    SessionManager sessionManager;

    @PostMapping("/rates")
    public ResponseEntity<RateResponseDTO> rate(@RequestParam(name = "ratedId") int ratedId,  @RequestBody RateDTO rating, HttpServletRequest request){
        sessionManager.verifyUser(request);
        return ResponseEntity.ok(rateService.createRate((Integer) request.getSession().getAttribute(SessionManager.USER_ID),  ratedId,rating));
    }

    @PutMapping("/rates/{ratedId}")
    public ResponseEntity<RateResponseDTO> editRate(@PathVariable int ratedId, @RequestBody RateDTO rating, HttpServletRequest request){
        sessionManager.verifyUser(request);
        return ResponseEntity.ok(rateService.editRate((Integer) request.getSession().getAttribute(SessionManager.USER_ID),  ratedId,rating));
    }

    @DeleteMapping("/rates/{ratedId}")
    public ResponseEntity<RateResponseDTO> unrate(@PathVariable int ratedId, HttpServletRequest request){
        sessionManager.verifyUser(request);
        return ResponseEntity.ok(rateService.unrate((Integer) request.getSession().getAttribute(SessionManager.USER_ID),ratedId));
    }
}
