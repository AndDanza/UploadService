package org.infobip.andrea.uploadservice.listeners;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.ProgressListener;
import org.infobip.andrea.uploadservice.dto.FileUploadProgress;
import org.infobip.andrea.uploadservice.utils.Constants;
import org.infobip.andrea.uploadservice.utils.UploadStatistics;

public class FileUploadProgressListener implements ProgressListener
{
    private HttpSession session;

    @Override
    public void update(final long l, final long l1, final int i)
    {
        if (this.session != null)
        {
            final List<FileUploadProgress> uploads = (List<FileUploadProgress>) this.session.getAttribute(Constants.FILE_UPLOAD_PROGRESS_ATTRIBUTE);
            if (uploads != null)
            {
                final Optional<FileUploadProgress> uploadProgressOpt = uploads.stream().filter(progress -> progress.getContentLength() == l1).findFirst();
                final FileUploadProgress uploadProgress = uploadProgressOpt.orElseGet(FileUploadProgress::new);

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
                uploadProgress.setBytesRead(l);
                uploadProgress.setContentLength(l1);
                uploadProgress.setItems(i);

                if (!uploadProgressOpt.isPresent())
                {
                    uploads.add(uploadProgress);
                }
            }
        }
    }

    public void setSession(final HttpServletRequest request)
    {
        this.session = request.getSession();
        List<FileUploadProgress> uploads = (List<FileUploadProgress>) this.session.getAttribute(Constants.FILE_UPLOAD_PROGRESS_ATTRIBUTE);
        if (uploads == null)
        {
            uploads = new ArrayList<>();
            this.session.setAttribute(Constants.FILE_UPLOAD_PROGRESS_ATTRIBUTE, uploads);
        }
    }
}