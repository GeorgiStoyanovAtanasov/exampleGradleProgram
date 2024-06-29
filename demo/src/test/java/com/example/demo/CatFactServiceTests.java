package com.example.demo;

import com.example.demo.Clients.CatFactClient;
import com.example.demo.Entities.CatFact;
import com.example.demo.Repositories.CatFactRepository;
import com.example.demo.Services.CatFactService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;


import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.*;

class CatFactServiceTests {

    @Mock
    private CatFactRepository catFactRepository;

    @Mock
    private CloseableHttpClient httpClient;

    @Mock
    private CloseableHttpResponse httpResponse;
    @Mock
    private CatFactClient catFactClient;
    @Mock
    private HttpEntity httpEntity;
    @InjectMocks
    private CatFactService catFactService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        catFactService = new CatFactService(catFactRepository, catFactClient);
    }

    @Test
    void testGetCatFact() throws IOException {
        String jsonResponse = "{\"fact\": \"Test cat fact\"}";
        InputStream inputStream = IOUtils.toInputStream(jsonResponse, "UTF-8");
        when(httpEntity.getContent()).thenReturn(inputStream);
        when(httpResponse.getEntity()).thenReturn(httpEntity);
        when(httpClient.execute(any(HttpGet.class))).thenReturn(httpResponse);
        when(httpResponse.getStatusLine()).thenReturn(mock(StatusLine.class));
        when(httpResponse.getStatusLine().getStatusCode()).thenReturn(200);

        catFactService.getCatFact();
        verify(catFactRepository, times(1)).save(ArgumentMatchers.any(CatFact.class));
    }

    @Test
    void testGetCatFactException() throws IOException {
        // Arrange
        when(httpClient.execute(any(HttpGet.class))).thenThrow(new IOException("HTTP request failed"));

        // Act & Assert
        try {
            catFactService.getCatFact();
        } catch (IOException e) {
            assertEquals("HTTP request failed", e.getMessage());
        }
    }
    @Test
    void testPostCatFactSuccess() throws IOException {
        CatFact catFact = new CatFact("Test cat fact", 14);
        String jsonResponse = "{\"fact\": \"Test cat fact\"}";
        InputStream inputStream = IOUtils.toInputStream(jsonResponse, "UTF-8");
        when(httpResponse.getStatusLine()).thenReturn(mock(StatusLine.class));
        when(httpResponse.getStatusLine().getStatusCode()).thenReturn(200);
        when(httpResponse.getEntity()).thenReturn(httpEntity);
        when(httpEntity.getContent()).thenReturn(inputStream);
        when(httpClient.execute(any(HttpPost.class))).thenReturn(httpResponse);

        String result = catFactService.postCatFact(catFact);
        assertEquals("Your post request sent a message:201 Created", result);
    }

    @Test
    void testPostCatFactFailure() throws IOException {
        CatFact catFact = new CatFact("Test cat fact", 14);

        when(httpResponse.getStatusLine()).thenReturn(mock(StatusLine.class));
        when(httpResponse.getStatusLine().getStatusCode()).thenReturn(500);
        when(httpResponse.getStatusLine().getReasonPhrase()).thenReturn("Internal Server Error");
        when(httpClient.execute(any(HttpPost.class))).thenReturn(httpResponse);

        String result = catFactService.postCatFact(catFact);
        assertNotEquals("You have sent your post request successfully!", result);
    }
    @Test
    void testGetCatFactSecondMethod() {
        // Arrange
        CatFact mockCatFact = new CatFact("Test cat fact", 14);
        when(catFactClient.getCatFact()).thenReturn(mockCatFact);

        // Act
        ResponseEntity<CatFact> response = catFactService.getCatFactSecondMethod();

        // Assert
        verify(catFactClient, times(1)).getCatFact();
        verify(catFactRepository, times(1)).save(mockCatFact);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(mockCatFact, response.getBody());
    }
    @Test
    void testGetCatFactSecondMethod_ClientReturnsNull() {
        when(catFactClient.getCatFact()).thenReturn(null);

        ResponseEntity<CatFact> response = catFactService.getCatFactSecondMethod();

        verify(catFactClient, times(1)).getCatFact();
        verify(catFactRepository, times(0)).save(any(CatFact.class));

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(null, response.getBody());
    }
}
