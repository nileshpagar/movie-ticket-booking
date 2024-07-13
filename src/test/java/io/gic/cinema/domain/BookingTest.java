package io.gic.cinema.domain;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BookingTest {

    private Booking booking;

    @BeforeEach
    void setUp() {
        booking = new Booking(5, 5); // 5 rows, 5 seats per row
    }

    @Test
    void reserveSeats_updatesChartSuccessfully() {
        String[][] initialChart = booking.getChart();
        String bookingId = "B100";
        int[] position = {2, 2}; // Third row, third seat
        String[][] updatedChart = booking.reserve(1, initialChart, bookingId, position);
        assertEquals(bookingId, updatedChart[2][2]);
    }

    @Test
    void reserveSeats_failsWhenNoSeatsAvailable() {
        booking = new Booking(1, 1); // 1 row, 1 seat to simulate full chart
        booking.reserve(1, booking.getChart(), "B100", new int[]{0, 0}); // Fill the only seat
        String[][] updatedChart = booking.reserve(1, booking.getChart(), "B101", new int[]{0, 0});
        assertNotEquals("B101", updatedChart[0][0]); // Expect the seat to not be updated
    }

    @Test
    void isBookingPresent_returnsTrueForExistingBooking() {
        String bookingId = "B200";
        booking.reserve(1, booking.getChart(), bookingId, new int[]{0, 0});
        assertTrue(booking.isBookingPresent(bookingId));
    }

    @Test
    void isBookingPresent_returnsFalseForNonExistingBooking() {
        assertFalse(booking.isBookingPresent("B300"));
    }

    @Test
    void getNumberOfTicketsAvailable_returnsCorrectCount() {
        booking.reserve(1, booking.getChart(), "B400", new int[]{0, 0});
        assertEquals(24, booking.getNumberOfTicketsAvailable()); // 25 seats initially, 1 booked
    }

    @Test
    void confirm_updatesChartCorrectly() {
        String[][] newChart = new String[5][5];
        newChart[0][0] = "B500";
        booking.confirm(newChart);
        assertEquals("B500", booking.getChart()[0][0]);
    }
}