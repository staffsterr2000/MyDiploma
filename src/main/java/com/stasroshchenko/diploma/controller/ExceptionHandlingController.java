package com.stasroshchenko.diploma.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

//@ControllerAdvice
public class ExceptionHandlingController {

//    @ExceptionHandler(Exception.class)
//    public ModelAndView handleException(HttpServletRequest req, Exception ex) {
//        ModelAndView mav = new ModelAndView();
//        mav.setViewName("error");
//        mav.addObject("exception", ex);
//        mav.addObject("url", req.getRequestURL());
//        return mav;
//    }

}
