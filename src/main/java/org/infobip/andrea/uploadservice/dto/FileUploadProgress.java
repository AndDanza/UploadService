package org.infobip.andrea.uploadservice.dto;

import java.io.Serializable;
import java.util.Date;

public class FileUploadProgress implements Serializable
{
    private static final long serialVersionUID = 7906730624000106797L;

    private long bytesRead;
    private long contentLength;
    private int items;
    private String filename;
    private Date uploadStarted;
    private Date uploadEnded;

    public FileUploadProgress(String filename)
    {
        this.filename = filename;
    }

    public long getBytesRead()
    {
        return this.bytesRead;
    }

    public void setBytesRead(final long bytesRead)
    {
        this.bytesRead = bytesRead;
    }

    public long getContentLength()
    {
        return this.contentLength;
    }

    public void setContentLength(final long contentLength)
    {
        this.contentLength = contentLength;
    }

    public int getItems()
    {
        return this.items;
    }

    public void setItems(final int items)
    {
        this.items = items;
    }

    public void setUploadStarted(final Date uploadStarted)
    {
        this.uploadStarted = uploadStarted;
    }

    public void setUploadEnded(final Date uploadEnded)
    {
        this.uploadEnded = uploadEnded;
    }

    public String getFilename()
    {
        return this.filename;
    }

    public void setFilename(final String filename)
    {
        this.filename = filename;
    }

    public Date getUploadEnded()
    {
        return this.uploadEnded;
    }

    public long getDuration()
    {
        long duration = 0L;

        if (this.uploadStarted != null && this.uploadEnded != null)
        {
            duration = this.uploadEnded.getTime() - this.uploadStarted.getTime();
        }

        return duration;
    }
}
