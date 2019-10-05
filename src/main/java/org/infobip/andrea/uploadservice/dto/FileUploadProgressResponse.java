package org.infobip.andrea.uploadservice.dto;

import java.util.ArrayList;
import java.util.List;

public class FileUploadProgressResponse
{
    List<FileUploadProgress> uploads = new ArrayList<>();

    public List<FileUploadProgress> getUploads()
    {
        return this.uploads;
    }

    public void setUploads(final List<FileUploadProgress> uploads)
    {
        this.uploads = uploads;
    }

    public void addProgress(final FileUploadProgress progress)
    {
        if (!this.uploads.contains(progress))
        {
            this.uploads.add(progress);
        }
    }
}
