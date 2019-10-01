package org.infobip.andrea.uploadservice.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class FileUploadController
{
    @GetMapping(value = "/upload")
    public ModelAndView getUpload()
    {
        return new ModelAndView("upload");
    }
}
