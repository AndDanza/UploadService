package org.infobip.andrea.uploadservice.services;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.infobip.andrea.uploadservice.dto.FileUploadDurationResponse;
import org.infobip.andrea.uploadservice.dto.FileUploadProgress;
import org.infobip.andrea.uploadservice.dto.FileUploadProgressResponse;

public interface FileUploadStatisticsService
{
    FileUploadDurationResponse getAllFileUploadsDurationResponse();

    FileUploadProgressResponse getFileUploadsProgressResponse(HttpSession session);

    Map<String, FileUploadProgress> getFileUploadsProgressForSession(HttpSession session);
}
