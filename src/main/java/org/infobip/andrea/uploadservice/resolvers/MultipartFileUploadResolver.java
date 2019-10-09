package org.infobip.andrea.uploadservice.resolvers;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.lang3.StringUtils;
import org.infobip.andrea.uploadservice.dto.FileUploadProgress;
import org.infobip.andrea.uploadservice.listeners.FileUploadProgressListener;
import org.infobip.andrea.uploadservice.services.FileUploadFacade;
import org.infobip.andrea.uploadservice.utils.Constants;
import org.infobip.andrea.uploadservice.utils.RequestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@Component
public class MultipartFileUploadResolver extends CommonsMultipartResolver
{
    private static final Logger LOG = LoggerFactory.getLogger(MultipartFileUploadResolver.class);
    private static final long CONVERSION_TO_BYTES_MULTIPLICATOR = 1024 * 1024;
    private static final long DEFAULT_MAX_FILE_SIZE = 50 * CONVERSION_TO_BYTES_MULTIPLICATOR;

    @Value("${uploadservice.upload.max-size}")
    private String fileMaxSize;

    private final FileUploadFacade fileUploadFacade;

    public MultipartFileUploadResolver(final FileUploadFacade fileUploadFacade)
    {
        this.fileUploadFacade = fileUploadFacade;
    }

    @PostConstruct
    public void init()
    {
        if (StringUtils.isNoneEmpty(this.fileMaxSize) && StringUtils.contains(this.fileMaxSize, "MB"))
        {
            try
            {
                final int maxFileSizeMB = Integer.parseInt(StringUtils.replace(this.fileMaxSize, "MB", ""));
                this.setMaxUploadSize(maxFileSizeMB * CONVERSION_TO_BYTES_MULTIPLICATOR);
            }
            catch (final NumberFormatException e)
            {
                LOG.error("Max upload size is not valid number", e);
                this.setMaxUploadSize(DEFAULT_MAX_FILE_SIZE);
            }
        }
    }

    @Override
    protected MultipartParsingResult parseRequest(final HttpServletRequest request)
    {
        final Map<String, FileUploadProgress> uploads = (Map<String, FileUploadProgress>) RequestUtils.getFileUploadsProgressForSession(request.getSession(), Constants.FILE_UPLOAD_PROGRESS_ATTRIBUTE);
        final String filename = request.getHeader(Constants.X_UPLOAD_FILE);
        MultipartParsingResult multipartParsingResult = null;
        final String encoding = determineEncoding(request);

        if (uploads == null || (uploads != null && StringUtils.isNotEmpty(filename) && !uploads.containsKey(filename)))
        {
            final FileUpload fileUpload = prepareFileUpload(encoding);

            final FileUploadProgressListener uploadProgressListener = new FileUploadProgressListener();
            uploadProgressListener.initializeProgressListener(request, fileUploadFacade);
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
        }

        if (multipartParsingResult == null)
        {
            multipartParsingResult = parseFileItems(Collections.emptyList(), encoding);
        }

        return multipartParsingResult;
    }
}
