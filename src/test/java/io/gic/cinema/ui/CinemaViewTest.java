package io.gic.cinema.ui;

import io.gic.cinema.domain.Cinema;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.io.*;
import java.util.concurrent.atomic.AtomicReference;

import static java.lang.System.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CinemaViewTest {

    ByteArrayOutputStream outputStream;
    private InputStream mockInputStream;
    private PrintStream mockOutputStream;

    public void setUp(String simulatedUserInput) {
        mockInputStream = new ByteArrayInputStream(simulatedUserInput.getBytes());
        outputStream = new ByteArrayOutputStream();
        mockOutputStream = new PrintStream(outputStream);
        setOut(mockOutputStream);
        setIn(mockInputStream);
    }

    public void tearDown()  {
        try {
            mockOutputStream.flush();
            mockOutputStream.close();
            outputStream.close();
            mockInputStream.close();
            setIn(in);
            setOut(out);
        } catch (Exception e) {
            //do nothing
        }
    }


    @Test
    @Order(1)
    public void test_readCinemaDetails() {
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
        tearDown();
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

        mockOutputStream.flush();
        // Then
        assertEquals(null, cinema.get());
        assertTrue(outputStream.toString().trim().contains("Invalid input. Please try again."));
        setUp("A 1 2");
        tearDown();
    }


}