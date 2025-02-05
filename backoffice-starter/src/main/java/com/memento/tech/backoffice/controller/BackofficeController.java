package com.memento.tech.backoffice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/backoffice")
public class BackofficeController {

    @GetMapping
    public String redirectToConsole() {
        return "redirect:/backoffice/console";
    }

    @GetMapping("/console")
    public ModelAndView getReactMapping() {
        return new ModelAndView("/backoffice/index");
    }

    @GetMapping("/login")
    public ModelAndView getLoginMapping() {
        return new ModelAndView("index");
    }
}