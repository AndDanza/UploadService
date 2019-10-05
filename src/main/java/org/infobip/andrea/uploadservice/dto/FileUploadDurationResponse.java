package org.infobip.andrea.uploadservice.dto;

import java.util.ArrayList;
import java.util.List;

public class FileUploadDurationResponse
{
    private List<FileUploadDuration> uploadDurations = new ArrayList<>();

    public List<FileUploadDuration> getUploadDurations()
    {
        return this.uploadDurations;
    }

    public void setUploadDurations(final List<FileUploadDuration> uploadDurations)
    {
        this.uploadDurations = uploadDurations;
    }
}
