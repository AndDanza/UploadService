package org.infobip.andrea.uploadservice.services;

import java.util.List;

import org.infobip.andrea.uploadservice.dto.FileUploadProgress;

public interface FileUploadDurationService
{
    List<FileUploadProgress> getAllFileProgresses();

    void addFileUploadsProgress(FileUploadProgress fileUploadProgress);
}
