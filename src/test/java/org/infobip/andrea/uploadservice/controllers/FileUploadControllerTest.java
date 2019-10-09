package org.infobip.andrea.uploadservice.controllers;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.infobip.andrea.uploadservice.resolvers.MultipartFileUploadResolver;
import org.infobip.andrea.uploadservice.services.StorageService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class FileUploadControllerTest
{
    private static final String FILE_TO_POST_NAME = "test-%d.pdf";
    private static final String FILE_TO_UPLOAD_NAME = "test.pdf";

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private StorageService storageService;

    @Mock
    private MultipartFileUploadResolver multipartFileUploadResolver;

    @Test
    public void postFileUpload()
    {
        List<Future<MvcResult>> futures = new ArrayList<>();
        try
        {
            final URL fileToUploadPath = getClass().getClassLoader().getResource(FILE_TO_UPLOAD_NAME);
            final byte[] fileToUploadBytes = Files.readAllBytes(Paths.get(fileToUploadPath.toURI()));
            final MockMultipartFile mockMultipartFile = new MockMultipartFile(FILE_TO_UPLOAD_NAME, fileToUploadBytes);

            final ExecutorService executor = Executors.newFixedThreadPool(101);

            final List<PostWorkerThread> threads = new ArrayList<>();
            for (int i = 0; i < 101; i++)
            {
                threads.add(new PostWorkerThread(this.mockMvc, mockMultipartFile, i));
            }

            futures = executor.invokeAll(threads);
        }
        catch (final InterruptedException | URISyntaxException | IOException e)
        {
            e.printStackTrace();
        }

        futures.isEmpty();
    }

    private class PostWorkerThread implements Callable<MvcResult>
    {
        MockMvc mockMvc;
        MockHttpServletRequestBuilder mockBuilder;

        public PostWorkerThread(final MockMvc mockMvc, final MockMultipartFile mockMultipartFile, final int i)
        {
            this.mockMvc = mockMvc;
            this.mockBuilder = multipart("/api/v1/upload")
                                 .file(mockMultipartFile)
                                 .contentType("multipart/form-data")
                                 .header("X-Content-File", String.format(FileUploadControllerTest.FILE_TO_POST_NAME, i));
        }

        @Override
        public MvcResult call() throws Exception
        {
            return this.mockMvc.perform(this.mockBuilder).andReturn();
        }
    }
}