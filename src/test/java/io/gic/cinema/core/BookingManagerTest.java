package io.gic.cinema.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BookingManagerTest {

    private BookingManager bookingManager;
    private BookingIdGenerator bookingIdGenerator;

    @BeforeEach
    void setUp() {
        bookingIdGenerator = new BookingIdGenerator();
        bookingManager = new BookingManager(5, 5);
    }

//    @Test
    void acceptBookingDetailsSuccessfullyProcessesValidInput() {
        int initialAvailableTickets = bookingManager.getNumberOfTicketsAvailable();
        bookingManager.acceptBookingDetails(3);
        assertEquals(initialAvailableTickets - 3, bookingManager.getNumberOfTicketsAvailable());
    }

//    @Test
    void bookingIdIsGeneratedAndUsedCorrectly() {
        String bookingIdBefore = bookingIdGenerator.nextBookingId();
        bookingManager.acceptBookingDetails(1);
        String bookingIdAfter = bookingIdGenerator.nextBookingId();
        assertNotEquals(bookingIdBefore, bookingIdAfter);
        assertTrue(bookingManager.isBookingPresent(bookingIdBefore));
    }

//    @Test
    void acceptBookingDetailsHandlesInvalidSeatPositionsGracefully() {
        assertDoesNotThrow(() -> bookingManager.acceptBookingDetails(100)); // Assuming 100 exceeds the number of available seats
    }
}