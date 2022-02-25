package com.finalproject.finalproject.controller;

import com.finalproject.finalproject.exceptions.BadRequestException;
import com.finalproject.finalproject.exceptions.ForbiddenException;
import com.finalproject.finalproject.exceptions.NotFoundException;
import com.finalproject.finalproject.exceptions.UnauthorizedException;
import com.finalproject.finalproject.model.dto.ExceptionDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ExceptionDTO badRequestExceptionHandler(BadRequestException e){
        ExceptionDTO dto = new ExceptionDTO();
        dto.setMsg(e.getMessage());
        dto.setStatus(HttpStatus.BAD_REQUEST.value());
        e.printStackTrace();
        return dto;
    }

    @ExceptionHandler(value = {UnauthorizedException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public ExceptionDTO handleUnauthorized(Exception e){
        ExceptionDTO dto = new ExceptionDTO();
        dto.setMsg(e.getMessage());
        dto.setStatus(HttpStatus.UNAUTHORIZED.value());
        e.printStackTrace();
        return dto;
    }

    @ExceptionHandler(value = {NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ExceptionDTO handleNotFound(Exception e){
        ExceptionDTO dto = new ExceptionDTO();
        dto.setMsg(e.getMessage());
        dto.setStatus(HttpStatus.NOT_FOUND.value());
        e.printStackTrace();
        return dto;
    }

    @ExceptionHandler(value = {ForbiddenException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ExceptionDTO handleForbiddenException(Exception e){
        ExceptionDTO dto = new ExceptionDTO();
        dto.setMsg(e.getMessage());
        dto.setStatus(HttpStatus.FORBIDDEN.value());
        System.out.println(e.getClass().getName());
        e.printStackTrace();
        return dto;
    }

    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionDTO handleAllOthers(Exception e){
        ExceptionDTO dto = new ExceptionDTO();
        dto.setMsg(e.getMessage());
        dto.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        System.out.println(e.getClass().getSimpleName());
        e.printStackTrace();
        return dto;
    }



}
