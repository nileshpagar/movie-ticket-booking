package io.gic.cinema.ui;

import org.junit.jupiter.api.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.concurrent.atomic.AtomicReference;

import static io.gic.cinema.ui.BookingView.acceptNumberOfTickets;
import static java.lang.System.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class BookingViewTest {

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


    @Test
    @Order(1)
    void testAcceptNumberOfTickets() throws InterruptedException {
        int numberOfTicketsAvailable = 5;
        setUp("5\n");

        AtomicReference<Integer> tickets = new AtomicReference<>();
        new Thread(() -> tickets.set(acceptNumberOfTickets(numberOfTicketsAvailable))).start();  //executing ASYNC as the method is blocking till we get the correct input from the user.
        Thread.sleep(50);

        mockOutputStream.flush();
        assertEquals(null, tickets.get());
    }


    @Test
    @Order(2)
     void testAcceptNumberOfTickets_notValid() throws InterruptedException {
        //given
        int numberOfTicketsAvailable = 5;
        setUp("6\n");

        //when
        AtomicReference<Integer> tickets = new AtomicReference<>();
        new Thread(() -> tickets.set(acceptNumberOfTickets(numberOfTicketsAvailable))).start();  //executing ASYNC as the method is blocking till we get the correct input from the user.
        Thread.sleep(50);


        //then
        tickets.get();
        mockOutputStream.flush();
        assertTrue(outputStream.toString().contains("Invalid input. Please try again."));
        setUp("5\n");
    }


    @Test
    void testPrintBookings_forAllTickets() {
        String[][] booking = {
                {"GIC0003", "GIC0003", "GIC0003"},
                {"GIC0002", "GIC0002", "GIC0001"},
                {"GIC0001", "GIC0001", "GIC0001"}
        };
        String bookingId = null;
        String output = BookingView.printBookings(booking, bookingId);
        assertEquals("\n" +
                "  SCREEN  \n" +
                "----------\n" +
                "C  #  #  # \n" +
                "B  #  #  # \n" +
                "A  #  #  # \n" +
                "    1  2  3\n" +
                "\n", output);
    }

    @Test
    void testPrintBookings_forATicket() {
        String[][] booking = {
                {"GIC0003", "GIC0003", "GIC0003"},
                {"GIC0002", "GIC0002", "GIC0001"},
                {"GIC0001", "GIC0001", "GIC0001"}
        };
        String bookingId = "GIC0001";
        String output = BookingView.printBookings(booking, bookingId);
        assertEquals("\n" +
                "  SCREEN  \n" +
                "----------\n" +
                "C  #  #  # \n" +
                "B  #  #  o \n" +
                "A  o  o  o \n" +
                "    1  2  3\n" +
                "\n", output);
    }

    @Test
    void testPrintBookings_forATicket_forUnbookedSeats() {
        String[][] booking = {
                {null, null, null},
                {"GIC0002", "GIC0002", "GIC0001"},
                {"GIC0001", "GIC0001", "GIC0001"}
        };
        String bookingId = "GIC0001";
        String output = BookingView.printBookings(booking, bookingId);
        assertEquals("\n" +
                "  SCREEN  \n" +
                "----------\n" +
                "C  .  .  . \n" +
                "B  #  #  o \n" +
                "A  o  o  o \n" +
                "    1  2  3\n" +
                "\n", output);
    }

}