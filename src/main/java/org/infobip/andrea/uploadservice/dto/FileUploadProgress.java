package org.infobip.andrea.uploadservice.dto;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class FileUploadProgress implements Serializable
{
    private static final long serialVersionUID = 7906730624000106797L;

    private long uploaded;
    private long size;
    private String id;
    private Date uploadStarted;
    private Date uploadEnded;

    public FileUploadProgress(final String id)
    {
        this.id = id;
    }

    public long getUploaded()
    {
        return this.uploaded;
    }

    public void setUploaded(final long uploaded)
    {
        this.uploaded = uploaded;
    }

    public long getSize()
    {
        return this.size;
    }

    public void setSize(final long size)
    {
        this.size = size;
    }

    @JsonIgnore
    public void setUploadStarted(final Date uploadStarted)
    {
        this.uploadStarted = uploadStarted;
    }

    public void setUploadEnded(final Date uploadEnded)
    {
        this.uploadEnded = uploadEnded;
    }

    @JsonIgnore
    public String getId()
    {
        return this.id;
    }

    @JsonProperty(value = "id")
    public String getIdWithTimestamp()
    {
        return StringUtils.join(this.id, "-", this.uploadStarted.getTime());
    }

    public void setId(final String id)
    {
        this.id = id;
    }

    @JsonIgnore
    public Date getUploadStarted()
    {
        return this.uploadStarted;
    }

    @JsonIgnore
    public Date getUploadEnded()
    {
        return this.uploadEnded;
    }

    @JsonIgnore
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
