package org.infobip.andrea.uploadservice.services.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import javax.annotation.PostConstruct;

import org.infobip.andrea.uploadservice.services.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class StorageServiceImpl implements StorageService
{
    private static final Logger LOG = LoggerFactory.getLogger(StorageServiceImpl.class);

    @Value("${uploadservice.upload.location}")
    private String uploadPath;

    @PostConstruct
    private void init()
    {
        final File uploadDirectory = new File(this.uploadPath);
        if (!uploadDirectory.exists())
        {
            uploadDirectory.mkdirs();
        }
    }

    @Override
    public boolean storeFile(final MultipartFile file)
    {
        boolean isStored = false;

        if (!file.isEmpty())
        {
            try
            {
                final Path filePath = Paths.get(this.uploadPath + "/" + file.getOriginalFilename());
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                isStored = true;
            }
            catch (final IOException e)
            {
                isStored = false;
                LOG.warn(file.getOriginalFilename() + " could not be stored", e);
            }
        }

        return isStored;
    }
}
