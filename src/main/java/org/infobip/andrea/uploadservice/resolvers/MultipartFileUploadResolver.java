package org.infobip.andrea.uploadservice.resolvers;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang3.StringUtils;
import org.infobip.andrea.uploadservice.dto.FileUploadProgress;
import org.infobip.andrea.uploadservice.listeners.FileUploadProgressListener;
import org.infobip.andrea.uploadservice.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@Component
public class MultipartFileUploadResolver extends CommonsMultipartResolver
{
    private static final Logger LOG = LoggerFactory.getLogger(MultipartFileUploadResolver.class);

    @Override
    protected MultipartParsingResult parseRequest(final HttpServletRequest request)
    {
        final Map<String, FileUploadProgress> uploads = (Map<String, FileUploadProgress>) request.getSession().getAttribute(Constants.FILE_UPLOAD_PROGRESS_ATTRIBUTE);
        final String filename = request.getHeader("X-Upload-File");
        MultipartParsingResult multipartParsingResult = null;

        if (uploads != null && uploads.containsKey(filename))
        {
            //TODO prekini da ne vrišti server
        }

        final String encoding = determineEncoding(request);
        final FileUpload fileUpload = prepareFileUpload(encoding);

        final FileUploadProgressListener uploadProgressListener = new FileUploadProgressListener();
        uploadProgressListener.initializeProgressListener(request);
        fileUpload.setProgressListener(uploadProgressListener);

        try
        {
            final List<FileItem> fileItems = ((ServletFileUpload) fileUpload).parseRequest(request);
            multipartParsingResult = parseFileItems(fileItems, encoding);
        }
        catch (final FileUploadException e)
        {
            LOG.error(filename + " could not be processed", e);
        }

        return multipartParsingResult;
    }
}
