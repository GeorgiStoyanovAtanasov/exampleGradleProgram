package com.example.demo;

import com.example.demo.Controller.CatFactController;
import com.example.demo.Entities.CatFact;
import com.example.demo.Repositories.CatFactRepository;
import com.example.demo.Services.CatFactService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CatFactControllerTest {

    @Mock
    private CatFactRepository catFactRepository;

    @Mock
    private CatFactService catFactService;

    @Autowired
    private CatFactController catFactController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        catFactController = new CatFactController(catFactRepository, catFactService);
    }

    @Test
    void testControllerConstructor() {
        assertNotNull(catFactController);
    }

    @Test
    void testGetCatFact() throws IOException {
        // Arrange
        String expectedFact = "Cats are awesome!";
        when(catFactService.getCatFact()).thenReturn(expectedFact);

        // Act
        String actualFact = catFactController.getCatFact();

        // Assert
        assertEquals(expectedFact, actualFact);
    }

    @Test
    void testPostCatFact() throws IOException {
        // Arrange
        CatFact catFact = new CatFact("New Cat Fact", 14);
        String expectedMessage = "You have sent your post request successfully!";
        when(catFactService.postCatFact(catFact)).thenReturn(expectedMessage);

        // Act
        String actualMessage = catFactController.postCatFact(catFact);

        // Assert
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    void testPostCatFactToDatabase() {
        // Arrange
        CatFact catFact = new CatFact("New Cat Fact", 14);

        // Act
        String response = catFactController.postCatFactToDatabase(catFact);

        // Assert
        verify(catFactRepository, times(1)).save(catFact);
        assertEquals("Successfully posted to the database!", response);
    }

    @Test
    void testGetCatFact2() {
        // Arrange
        CatFact catFact = new CatFact("Another Cat Fact", 16);
        when(catFactService.getCatFactSecondMethod()).thenReturn(ResponseEntity.ok(catFact));

        // Act
        ResponseEntity<CatFact> response = catFactController.getCatFact2();

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(catFact, response.getBody());
    }

    @Test
    void testGetCatFact_ExceptionHandling() throws IOException {
        // Arrange
        when(catFactService.getCatFact()).thenThrow(new IOException("Service Exception"));

        // Act & Assert
        assertThrows(IOException.class, () -> catFactController.getCatFact());
    }

    @Test
    void testPostCatFact_ExceptionHandling() throws IOException {
        // Arrange
        CatFact catFact = new CatFact("Test Cat Fact", 14);
        when(catFactService.postCatFact(catFact)).thenThrow(new IOException("Service Exception"));

        // Act & Assert
        assertThrows(IOException.class, () -> catFactController.postCatFact(catFact));
    }
}
