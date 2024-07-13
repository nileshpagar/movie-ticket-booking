package io.gic.cinema.ui;

import io.gic.cinema.domain.Cinema;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class CinemaViewTest {

    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;

    private InputStream mockInputStream;
    private PrintStream mockOutputStream;

    @BeforeEach
    public void setUp() {
        System.setOut(mockOutputStream);
        System.setIn(mockInputStream);
    }

    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }


    @Test
    public void readCinemaDetails() {

        // Given
        String cinemaName = "GIC Cinemas";
        String movieName = "The Matrix";
        int rows = 5;
        int seatsPerRow = 10;

        // When
        Cinema cinema = CinemaView.readCinemaDetails(cinemaName);

        // Then
        assertEquals(cinemaName, cinema.getName());
        assertEquals(movieName, cinema.getMovieName());
        assertEquals(rows, cinema.getRows());
        assertEquals(seatsPerRow, cinema.getSeatsPerRow());

    }
}