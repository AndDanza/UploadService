package org.infobip.andrea.uploadservice.services.impl;

import java.util.List;

import org.infobip.andrea.uploadservice.dto.FileUploadProgress;
import org.infobip.andrea.uploadservice.services.FileUploadDurationService;
import org.infobip.andrea.uploadservice.utils.UploadStatistics;
import org.springframework.stereotype.Service;

@Service
public class FileUploadDurationServiceImpl implements FileUploadDurationService
{
    @Override
    public List<FileUploadProgress> getAllFileProgresses()
    {
        return UploadStatistics.getUploads();
    }

    @Override
    public void addFileUploadsProgress(final FileUploadProgress fileUploadProgress)
    {
        UploadStatistics.getUploads().add(fileUploadProgress);
    }
}
