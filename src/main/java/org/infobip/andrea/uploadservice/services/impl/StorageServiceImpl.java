package org.infobip.andrea.uploadservice.services.impl;

import org.infobip.andrea.uploadservice.services.StorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class StorageServiceImpl implements StorageService
{
    @Value("${upload.path}")
    private String uploadPath;

    @Override
    public boolean storeFile(MultipartFile file)
    {
        return true;
    }
}
