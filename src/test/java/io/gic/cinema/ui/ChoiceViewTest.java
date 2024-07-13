package io.gic.cinema.ui;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ChoiceViewTest {

    ByteArrayOutputStream outputStream;
    private InputStream testIn;
    private PrintStream testOut;

    public void setUp(String simulatedUserInput) {
        testIn = new ByteArrayInputStream(simulatedUserInput.getBytes());
        outputStream = new ByteArrayOutputStream();
        testOut = new PrintStream(outputStream);
        System.setOut(testOut);
        System.setIn(testIn);
    }

    public void tearDown()  {
        try {
            System.setIn(System.in);
            System.setOut(System.out);
        } catch (Exception e) {
            //do nothing
        }
    }

    @Test
    public void testDisplayMainMenu() {
        ChoiceView.acceptChoice("GIC Cinema", "The Matrix", 10);
        setUp("1\n");
        String output = testOut.toString();
        assertTrue(output.contains("Welcome to GIC Cinema"));
        assertTrue(output.contains("1. Show Movies"));
        assertTrue(output.contains("2. Book Ticket"));
        assertTrue(output.contains("Please select an option:"));
//        setUp();
    }
}