package io.gic.cinema.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BookingIdGeneratorTest {

    private BookingIdGenerator bookingIdGenerator;

    @BeforeEach
    void setUp() {
        bookingIdGenerator = new BookingIdGenerator();
    }

    @Test
    void nextBookingId_generatesSequentialIds() {
        assertEquals("GIC0001", bookingIdGenerator.nextBookingId());
        assertEquals("GIC0002", bookingIdGenerator.nextBookingId());
    }

    @Test
    void nextBookingId_HandlesOnlyTillMaxValue() {
        for (int i = 1; i <= 9999; i++) {
            bookingIdGenerator.nextBookingId(); // Increment to the last possible ID
        }
        assertThrows(IllegalArgumentException.class, () -> bookingIdGenerator.nextBookingId());
    }

}