package org.infobip.andrea.uploadservice.controllers;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.infobip.andrea.uploadservice.dto.FileUploadDuration;
import org.infobip.andrea.uploadservice.dto.FileUploadDurationResponse;
import org.infobip.andrea.uploadservice.dto.FileUploadProgress;
import org.infobip.andrea.uploadservice.dto.FileUploadProgressResponse;
import org.infobip.andrea.uploadservice.services.StorageService;
import org.infobip.andrea.uploadservice.utils.Constants;
import org.infobip.andrea.uploadservice.utils.MapUtils;
import org.infobip.andrea.uploadservice.utils.UploadStatistics;
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

    public FileUploadController(final StorageService storageService)
    {
        this.storageService = storageService;
    }

    @GetMapping(value = "/upload")
    public ModelAndView getFileUpload()
    {
        return new ModelAndView("upload");
    }

    @PostMapping(value = "/api/v1/upload")
    public ResponseEntity postFileUpload(final HttpServletRequest request, @RequestParam(required=false) final MultipartFile file)
    {
        final String landingPage = file != null && this.storageService.storeFile(file) ? "/success" : "/error";
        final String path = StringUtils.join("http://", request.getServerName(), ":", request.getServerPort(), landingPage);

        return ResponseEntity.ok(path);
    }

    @GetMapping(value = "/api/v1/upload/progress")
    public ResponseEntity getFileUploadProgress(final HttpServletRequest request)
    {
        final FileUploadProgressResponse response = new FileUploadProgressResponse();

        final HttpSession session = request.getSession();
        if (session != null)
        {
            final Map<String, FileUploadProgress> uploads = (Map<String, FileUploadProgress>) session.getAttribute(Constants.FILE_UPLOAD_PROGRESS_ATTRIBUTE);
            if (uploads != null)
            {
                response.setUploads(MapUtils.mapValuesToList(uploads));
            }
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/api/v1/upload/duration")
    public ResponseEntity getFileUploadDuration(final HttpServletRequest request)
    {
        final FileUploadDurationResponse response = new FileUploadDurationResponse();

        final HttpSession session = request.getSession();
        if (session != null)
        {
            final List<FileUploadProgress> uploads = UploadStatistics.getUploads();
            final List<FileUploadDuration> uploadDurations = uploads.stream().map(this::createUploadDurationInfo).collect(Collectors.toList());

            response.setUploadDurations(uploadDurations);
        }

        return ResponseEntity.ok(response);
    }

    private FileUploadDuration createUploadDurationInfo(final FileUploadProgress progress)
    {
        return new FileUploadDuration(progress.getIdWithTimestamp(), progress.getDuration());
    }
}
