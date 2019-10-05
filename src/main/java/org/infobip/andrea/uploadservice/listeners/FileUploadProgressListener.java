package org.infobip.andrea.uploadservice.listeners;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.ProgressListener;
import org.apache.commons.lang3.StringUtils;
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
            final List<FileUploadProgress> uploads = (List<FileUploadProgress>) this.session.getAttribute(Constants.FILE_UPLOAD_PROGRESS_ATTRIBUTE);
            if (uploads != null)
            {
                final Optional<FileUploadProgress> uploadProgressOpt = uploads.stream().filter(progress -> StringUtils.equals(this.filename, progress.getId())).findFirst();
                if (uploadProgressOpt.isPresent())
                {
                    final FileUploadProgress uploadProgress = uploadProgressOpt.get();
                    if (l == 4096)
                    {
                        uploadProgress.setUploadStarted(new Date());
                    }
                    else if (l == l1)
                    {
                        uploadProgress.setUploadEnded(new Date());

                        uploads.remove(uploadProgress);
                        UploadStatistics.addUpload(uploadProgress);
                    }
                    uploadProgress.setUploaded(l);
                    uploadProgress.setSize(l1);
                }
            }
        }
    }

    public void initializeProgressListener(final HttpServletRequest request)
    {
        this.session = request.getSession();
        if (this.session != null)
        {
            List<FileUploadProgress> uploads = (List<FileUploadProgress>) this.session.getAttribute(Constants.FILE_UPLOAD_PROGRESS_ATTRIBUTE);
            if (uploads == null)
            {
                uploads = new ArrayList<>();
                this.session.setAttribute(Constants.FILE_UPLOAD_PROGRESS_ATTRIBUTE, uploads);
            }

            this.filename = request.getHeader("X-Upload-File");
            uploads.add(new FileUploadProgress(this.filename));
        }
    }
}
