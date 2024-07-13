package io.gic.cinema.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class BookingControllerTest {

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private ByteArrayInputStream inContent;
    private final InputStream originalIn = System.in;

    @BeforeEach
    public void setUp() {
        // Prepare the sequence of inputs for the scenario
        String data = "1\n4\n\n\n2\nGIC0001\n3\n1\n77\n1\n12\n \n1\n2\n \n2\nGIC0001\n2\nGIC0002\n2\nGIC0003\n3\n";
        inContent = new ByteArrayInputStream(data.getBytes());
        System.setOut(new PrintStream(outContent));
        System.setIn(inContent);
    }

    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    @Test
    public void testFullBookingScenario() {
        BookingController controller = new BookingController();
        controller.start();

        // Assert that the final output contains the expected messages
        String output = outContent.toString();
        assertTrue(output.contains("Thanks for using"));
        assertTrue(output.contains("76 tickets available")); // Check for the message after trying to book 77 tickets
        assertTrue(output.contains("GIC0001")); // Check if booking ID GIC0001 is shown in the output
        assertTrue(output.contains("GIC0002")); // Check if booking ID GIC0002 is shown in the output
        assertTrue(output.contains("GIC0003")); // Check if booking ID GIC0003 is shown in the output
    }
}