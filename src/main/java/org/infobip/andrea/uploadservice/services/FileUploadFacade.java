package org.infobip.andrea.uploadservice.services;

import javax.servlet.http.HttpSession;

import org.infobip.andrea.uploadservice.dto.FileUploadDurationResponse;
import org.infobip.andrea.uploadservice.dto.FileUploadProgress;
import org.infobip.andrea.uploadservice.dto.FileUploadProgressResponse;

public interface FileUploadFacade
{
    FileUploadDurationResponse getAllFileUploadsDurationResponse();

    FileUploadProgressResponse getFileUploadsProgressResponse(HttpSession session);

    void addFileProgressToDurations(FileUploadProgress fileUploadProgress);
}
