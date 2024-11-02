package com.memento.tech.backoffice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/backoffice")
public class BackofficeViewController {

    @GetMapping
    public ModelAndView getTest() {
        return new ModelAndView("backoffice");
    }
}