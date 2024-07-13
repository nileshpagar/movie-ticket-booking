package io.gic.cinema.ui;

import org.junit.jupiter.api.*;

import java.io.*;

import static io.gic.cinema.ui.ChoiceView.acceptChoice;
import static java.lang.System.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ChoiceViewTest {

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
    public  void tearDown()  {
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

    @Test
    @Order(1)
    public void testDisplayMainMenu() throws IOException {
        //Given
        setUp("1\n");
        String cinemaName = "GIC Cinema";
        String movieName = "The Matrix";
        int numberOfSeats = 10;

        //When
        int choice = acceptChoice(cinemaName, movieName, numberOfSeats);

        //Then
        outputStream.flush();
        String output = outputStream.toString();
        assertTrue(choice == 1);
        assertTrue(output.contains("Welcome to " + cinemaName));
    }

    @Test
    @Order(2)
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
    }


}