package org.infobip.andrea.uploadservice.utils;

import java.util.ArrayList;
import java.util.List;

import org.infobip.andrea.uploadservice.dto.FileUploadProgress;

public class UploadStatistics
{
    private static final List<FileUploadProgress> uploads = new ArrayList<>();

    public static synchronized void addUpload(final FileUploadProgress progress)
    {
        uploads.add(progress);
    }

    public static synchronized List<FileUploadProgress> getUploads()
    {
        return uploads;
    }
}
