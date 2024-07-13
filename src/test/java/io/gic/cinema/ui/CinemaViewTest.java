package io.gic.cinema.ui;

import io.gic.cinema.domain.Cinema;
import org.junit.jupiter.api.*;

import java.io.*;
import java.util.concurrent.atomic.AtomicReference;

import static java.lang.System.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CinemaViewTest {

    ByteArrayOutputStream outputStream;
    InputStream mockInputStream;
    PrintStream mockOutputStream;

    public void setUp(String simulatedUserInput) {
        mockInputStream = new ByteArrayInputStream(simulatedUserInput.getBytes());
        outputStream = new ByteArrayOutputStream();
        mockOutputStream = new PrintStream(outputStream);
        setOut(mockOutputStream);
        setIn(mockInputStream);
    }

    @AfterEach
    public void tearDown()  {
        try {
            outputStream.flush();
            mockOutputStream.flush();
            outputStream.close();
            mockOutputStream.close();
            mockInputStream.close();
            setIn(in);
            setOut(out);
        } catch (Exception e) {
            //do nothing
        }
    }


    void test_readCinemaDetails() {
        // Given
        String cinemaName = "GIC Cinemas";
        int rows = 5;
        int seatsPerRow = 10;
        String movieName = "The Matrix";
        setUp(movieName + " " + rows + " " + seatsPerRow+ "\n");

        // When
        Cinema cinema = CinemaView.readCinemaDetails(cinemaName);

        // Then
        assertEquals(cinemaName, cinema.getName());
        assertEquals(movieName, cinema.getMovieName());
        assertEquals(rows, cinema.getRows());
        assertEquals(seatsPerRow, cinema.getSeatsPerRow());
    }

    @Test
    @Order(2)
    public void test_readCinemaDetails_forIncorrectInput() throws InterruptedException {
        // Given
        setUp("The Matrix a b\n");
        String cinemaName = "GIC Cinemas";

        // When
        AtomicReference<Cinema> cinema = new AtomicReference<>();
        new Thread(() -> cinema.set(CinemaView.readCinemaDetails(cinemaName))).start();  //executing ASYNC as the method is blocking till we get the correct input from the user.
        Thread.sleep(50);

        // Then
        mockOutputStream.flush();
        assertEquals(null, cinema.get());
        assertTrue(outputStream.toString().trim().contains("Invalid input. Please try again."));
        setUp("A 1 2");
    }


}