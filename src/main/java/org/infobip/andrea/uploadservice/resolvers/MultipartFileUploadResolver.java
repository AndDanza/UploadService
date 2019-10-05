package org.infobip.andrea.uploadservice.resolvers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.infobip.andrea.uploadservice.listeners.FileUploadProgressListener;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@Component
public class MultipartFileUploadResolver extends CommonsMultipartResolver
{
    @Override
    protected MultipartParsingResult parseRequest(final HttpServletRequest request) throws MultipartException
    {
        final String encoding = determineEncoding(request);
        final FileUpload fileUpload = prepareFileUpload(encoding);

        FileUploadProgressListener uploadProgressListener = new FileUploadProgressListener();
        uploadProgressListener.setSession(request);
        fileUpload.setProgressListener(uploadProgressListener);

        MultipartParsingResult multipartParsingResult = null;
        try
        {
            List<FileItem> fileItems = ((ServletFileUpload) fileUpload).parseRequest(request);
            multipartParsingResult = parseFileItems(fileItems, encoding);
        }
        catch (final FileUploadException e)
        {
            e.printStackTrace(); //TODO log exception
        }

        return multipartParsingResult;
    }
}
