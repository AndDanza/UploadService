package org.infobip.andrea.uploadservice.dto;

public class FileUploadDuration
{
    private final String id;
    private final long duration;

    public FileUploadDuration(final String id, final long duration)
    {
        this.id = id;
        this.duration = duration;
    }

    public String getId()
    {
        return this.id;
    }

    public long getDuration()
    {
        return this.duration;
    }
}
