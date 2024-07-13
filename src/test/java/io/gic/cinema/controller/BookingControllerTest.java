package io.gic.cinema.controller;

import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertTrue;


public class BookingControllerTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private ByteArrayInputStream inContent;
    private final PrintStream originalOut = System.out;
    private final InputStream originalIn = System.in;

    public void setUp() {
        // Prepare the sequence of inputs for the scenario
        String data = "Inception 8 10\n";
        inContent = new ByteArrayInputStream(data.getBytes());
        System.setOut(new PrintStream(outContent));
        System.setIn(inContent);
    }

    public void tearDown() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

//    @Test
    public void testFullBookingScenario() throws IOException {
        BookingController controller = new BookingController();
        controller.start();
        String data = "Inception 8 10\n";
        inContent.read(data.getBytes());

        // Assert that the final output contains the expected messages
        String output = outContent.toString();
        assertTrue(output.contains("Thanks for using"));
        assertTrue(output.contains("76 tickets available")); // Check for the message after trying to book 77 tickets
        assertTrue(output.contains("GIC0001")); // Check if booking ID GIC0001 is shown in the output
        assertTrue(output.contains("GIC0002")); // Check if booking ID GIC0002 is shown in the output
        assertTrue(output.contains("GIC0003")); // Check if booking ID GIC0003 is shown in the output
    }
}