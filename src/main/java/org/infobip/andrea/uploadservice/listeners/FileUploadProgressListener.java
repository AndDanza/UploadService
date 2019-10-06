package org.infobip.andrea.uploadservice.listeners;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.ProgressListener;
import org.infobip.andrea.uploadservice.dto.FileUploadProgress;
import org.infobip.andrea.uploadservice.utils.Constants;
import org.infobip.andrea.uploadservice.utils.UploadStatistics;

public class FileUploadProgressListener implements ProgressListener
{
    private HttpSession session;
    private String filename;

    @Override
    public void update(final long l, final long l1, final int i)
    {
        if (this.session != null)
        {
            final Map<String, FileUploadProgress> uploads = (Map<String, FileUploadProgress>) this.session.getAttribute(Constants.FILE_UPLOAD_PROGRESS_ATTRIBUTE);
            if (uploads != null && uploads.containsKey(this.filename))
            {
                final FileUploadProgress uploadProgress = uploads.get(this.filename);
                if (l == 4096)
                {
                    uploadProgress.setUploadStarted(new Date());
                }
                else if (l == l1)
                {
                    uploadProgress.setUploadEnded(new Date());

                    uploads.remove(this.filename);
                    UploadStatistics.addUpload(uploadProgress);
                }
                uploadProgress.setUploaded(l);
                uploadProgress.setSize(l1);
            }
        }
    }

    public void initializeProgressListener(final HttpServletRequest request)
    {
        this.session = request.getSession();
        if (this.session != null)
        {
            Map<String, FileUploadProgress> uploads = (Map<String, FileUploadProgress>) this.session.getAttribute(Constants.FILE_UPLOAD_PROGRESS_ATTRIBUTE);
            if (uploads == null)
            {
                uploads = new HashMap<>();
                this.session.setAttribute(Constants.FILE_UPLOAD_PROGRESS_ATTRIBUTE, uploads);
            }

            this.filename = request.getHeader("X-Upload-File");
            uploads.put(this.filename, new FileUploadProgress(this.filename));
        }
    }
}
