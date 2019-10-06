package org.infobip.andrea.uploadservice.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.infobip.andrea.uploadservice.dto.FileUploadProgress;

public class MapUtils
{
    public static List<FileUploadProgress> mapValuesToList(final Map<String, FileUploadProgress> uploads)
    {
        return new ArrayList<>(uploads.values());
    }
}
