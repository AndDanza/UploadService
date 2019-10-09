package org.infobip.andrea.uploadservice.services.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.infobip.andrea.uploadservice.dto.FileUploadDuration;
import org.infobip.andrea.uploadservice.dto.FileUploadDurationResponse;
import org.infobip.andrea.uploadservice.dto.FileUploadProgress;
import org.infobip.andrea.uploadservice.dto.FileUploadProgressResponse;
import org.infobip.andrea.uploadservice.services.FileUploadDurationService;
import org.infobip.andrea.uploadservice.services.FileUploadFacade;
import org.infobip.andrea.uploadservice.utils.Constants;
import org.infobip.andrea.uploadservice.utils.MapUtils;
import org.infobip.andrea.uploadservice.utils.RequestUtils;
import org.springframework.stereotype.Service;

@Service
public class FileUploadFacadeImpl implements FileUploadFacade
{
    private final FileUploadDurationService fileUploadDurationService;

    public FileUploadFacadeImpl(final FileUploadDurationService fileUploadDurationService)
    {
        this.fileUploadDurationService = fileUploadDurationService;
    }

    @Override
    public FileUploadDurationResponse getAllFileUploadsDurationResponse()
    {
        final FileUploadDurationResponse response = new FileUploadDurationResponse();

        final List<FileUploadProgress> uploads = this.fileUploadDurationService.getAllFileProgresses();
        final List<FileUploadDuration> uploadDurations = uploads.stream().map(this::createUploadDurationInfo).collect(Collectors.toList());

        response.setUploadDurations(uploadDurations);

        return response;
    }

    private FileUploadDuration createUploadDurationInfo(final FileUploadProgress progress)
    {
        return new FileUploadDuration(progress.getIdWithTimestamp(), progress.getDuration());
    }

    @Override
    public FileUploadProgressResponse getFileUploadsProgressResponse(final HttpSession session)
    {
        final FileUploadProgressResponse response = new FileUploadProgressResponse();

        if (session != null)
        {
            final Map<String, FileUploadProgress> uploads = (Map<String, FileUploadProgress>)RequestUtils.getFileUploadsProgressForSession(session, Constants.FILE_UPLOAD_PROGRESS_ATTRIBUTE);
            if (uploads != null)
            {
                response.setUploads(MapUtils.mapValuesToList(uploads));
            }
        }

        return response;
    }

    @Override
    public void addFileProgressToDurations(final FileUploadProgress fileUploadProgress)
    {
        this.fileUploadDurationService.addFileUploadsProgress(fileUploadProgress);
    }
}
