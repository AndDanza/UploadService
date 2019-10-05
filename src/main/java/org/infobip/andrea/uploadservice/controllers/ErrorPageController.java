package org.infobip.andrea.uploadservice.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ErrorPageController
{
    @GetMapping(value = "/error")
    public ModelAndView getSuccessPage()
    {
        return new ModelAndView("error");
    }
}
