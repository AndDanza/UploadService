package org.infobip.andrea.uploadservice.controllers;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.infobip.andrea.uploadservice.dto.FileUploadProgress;
import org.infobip.andrea.uploadservice.dto.FileUploadProgressResponse;
import org.infobip.andrea.uploadservice.utils.Constants;
import org.infobip.andrea.uploadservice.utils.UploadStatistics;
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

    @GetMapping(value = "/success")
    public ModelAndView getSuccessPage()
    {
        return new ModelAndView("success");
    }

    @PostMapping(value = "/api/v1/upload")
    public ResponseEntity postFileUpload(final HttpServletRequest request)
    {
        String path = StringUtils.join("http://" + request.getServerName() + ":" + request.getServerPort() + "/success");
        return ResponseEntity.ok(path);
    }

    @GetMapping(value = "/api/v1/upload/progress")
    public ResponseEntity getFileUploadProgress(final HttpServletRequest request)
    {
        final FileUploadProgressResponse response = new FileUploadProgressResponse();

        final HttpSession session = request.getSession();
        if (session != null)
        {
            final List<FileUploadProgress> uploads = (List<FileUploadProgress>) session.getAttribute(Constants.FILE_UPLOAD_PROGRESS_ATTRIBUTE);
            if (uploads != null)
            {
                response.setUploads(uploads);
            }
        }

        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/api/v1/upload/duration")
    public ResponseEntity getFileUploadDuration(final HttpServletRequest request)
    {
        final String response_format = "upload_duration{id=\"%s\"} %d";
        String response = "No data";

        final HttpSession session = request.getSession();
        if (session != null)
        {
            final List<FileUploadProgress> uploads = UploadStatistics.getUploads();
            if (uploads != null)
            {
                response = uploads.stream().map(uploadProgress -> String.format(response_format, uploadProgress.getFilename(), uploadProgress.getDuration())).collect(Collectors.joining(", \n"));
            }
        }

        return ResponseEntity.ok(response);
    }
}
