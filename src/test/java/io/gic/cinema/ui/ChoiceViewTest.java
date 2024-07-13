package io.gic.cinema.ui;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static io.gic.cinema.ui.ChoiceView.acceptChoice;
import static java.lang.System.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ChoiceViewTest {

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
    public void testDisplayMainMenu() {
        //Given
        setUp("1\n");
        String cinemaName = "GIC Cinema";
        String movieName = "The Matrix";
        int numberOfSeats = 10;

        //When
        int choice = acceptChoice(cinemaName, movieName, numberOfSeats);

        //Then
        String output = outputStream.toString();
        assertTrue(choice == 1);
        assertTrue(output.contains("Welcome to " + cinemaName));
        tearDown();
    }

    @Test
    public void testDisplayMainMenu_wrongChoice() {
        //Given
        setUp("4\n");
        String cinemaName = "GIC Cinema";
        String movieName = "The Matrix";
        int numberOfSeats = 10;

        //When
        int choice = acceptChoice(cinemaName, movieName, numberOfSeats);

        //Then
        String output = outputStream.toString();
        assertTrue(choice == 0);
        assertTrue(output.contains(""));
        tearDown();
    }


}