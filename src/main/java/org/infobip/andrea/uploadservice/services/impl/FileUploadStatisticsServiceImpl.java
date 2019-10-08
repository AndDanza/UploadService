package org.infobip.andrea.uploadservice.services.impl;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;

import org.infobip.andrea.uploadservice.dto.FileUploadDuration;
import org.infobip.andrea.uploadservice.dto.FileUploadDurationResponse;
import org.infobip.andrea.uploadservice.dto.FileUploadProgress;
import org.infobip.andrea.uploadservice.dto.FileUploadProgressResponse;
import org.infobip.andrea.uploadservice.services.FileUploadStatisticsService;
import org.infobip.andrea.uploadservice.utils.Constants;
import org.infobip.andrea.uploadservice.utils.MapUtils;
import org.infobip.andrea.uploadservice.utils.UploadStatistics;
import org.springframework.stereotype.Service;

@Service
public class FileUploadStatisticsServiceImpl implements FileUploadStatisticsService
{
    @Override
    public FileUploadDurationResponse getAllFileUploadsDurationResponse()
    {
        final FileUploadDurationResponse response = new FileUploadDurationResponse();

        final List<FileUploadProgress> uploads = UploadStatistics.getUploads();
        final List<FileUploadDuration> uploadDurations = uploads.stream().map(this::createUploadDurationInfo).collect(Collectors.toList());

        response.setUploadDurations(uploadDurations);

        return response;
    }

    @Override
    public FileUploadProgressResponse getFileUploadsProgressResponse(HttpSession session)
    {
        final FileUploadProgressResponse response = new FileUploadProgressResponse();

        if (session != null)
        {
            final Map<String, FileUploadProgress> uploads = getFileUploadsProgressForSession(session);
            if (uploads != null)
            {
                response.setUploads(MapUtils.mapValuesToList(uploads));
            }
        }

        return response;
    }

    @Override
    public Map<String, FileUploadProgress> getFileUploadsProgressForSession(HttpSession session)
    {
        return (Map<String, FileUploadProgress>) session.getAttribute(Constants.FILE_UPLOAD_PROGRESS_ATTRIBUTE);
    }

    private FileUploadDuration createUploadDurationInfo(final FileUploadProgress progress)
    {
        return new FileUploadDuration(progress.getIdWithTimestamp(), progress.getDuration());
    }
}
