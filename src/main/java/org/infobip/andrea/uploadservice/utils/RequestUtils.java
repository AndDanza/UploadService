package org.infobip.andrea.uploadservice.utils;

import javax.servlet.http.HttpSession;

public class RequestUtils
{
    private RequestUtils()  //SONAR
    {
    }

    public static Object getFileUploadsProgressForSession(final HttpSession session, final String attributeKey)
    {
        return session.getAttribute(attributeKey);
    }
}
