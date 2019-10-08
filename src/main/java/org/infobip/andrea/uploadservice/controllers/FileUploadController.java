package org.infobip.andrea.uploadservice.controllers;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.infobip.andrea.uploadservice.dto.FileUploadDurationResponse;
import org.infobip.andrea.uploadservice.dto.FileUploadProgressResponse;
import org.infobip.andrea.uploadservice.services.FileUploadStatisticsService;
import org.infobip.andrea.uploadservice.services.StorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class FileUploadController
{
    private final StorageService storageService;
    private final FileUploadStatisticsService fileUploadStatisticsService;

    public FileUploadController(final StorageService storageService, final FileUploadStatisticsService fileUploadStatisticsService)
    {
        this.storageService = storageService;
        this.fileUploadStatisticsService = fileUploadStatisticsService;
    }

    @GetMapping(value = "/upload")
    public ModelAndView getFileUpload()
    {
        return new ModelAndView("upload");
    }

    @PostMapping(value = "/api/v1/upload")
    public ResponseEntity postFileUpload(final HttpServletRequest request, @RequestParam(required = false) final MultipartFile file)
    {
        final String landingPage = file != null && this.storageService.storeFile(file) ? "/success" : "/error";
        final String path = StringUtils.join("http://", request.getServerName(), ":", request.getServerPort(), landingPage);

        return ResponseEntity.ok(path);
    }

    @GetMapping(value = "/api/v1/upload/progress")
    public ResponseEntity getFileUploadProgress(final HttpServletRequest request)
    {
        final FileUploadProgressResponse response = this.fileUploadStatisticsService.getFileUploadsProgressResponse(request.getSession());

        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/api/v1/upload/duration")
    public ResponseEntity getFileUploadDuration()
    {
        final FileUploadDurationResponse response = this.fileUploadStatisticsService.getAllFileUploadsDurationResponse();

        return ResponseEntity.ok(response);
    }
}
