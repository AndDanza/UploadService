package org.infobip.andrea.uploadservice.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class FileUploadController
{
    @GetMapping(value = "/upload")
    public ModelAndView getFileUpload()
    {
        return new ModelAndView("upload");
    }

    @PostMapping(value = "/api/v1/upload")
    public ModelAndView postFileUpload(final HttpServletRequest request, final HttpServletResponse response)
    {
        return new ModelAndView("upload");
    }

    @GetMapping(value = "/api/v1/upload/progress")
    public ResponseEntity getFileUploadProgress(final HttpServletRequest request)
    {
        final String response = "No data";

        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/api/v1/upload/duration")
    public ModelAndView getFileUploadDuration()
    {
        return new ModelAndView("upload");
    }
}
