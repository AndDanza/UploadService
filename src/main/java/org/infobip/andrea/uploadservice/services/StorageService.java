package org.infobip.andrea.uploadservice.services;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService
{
    boolean storeFile(MultipartFile file);
}
