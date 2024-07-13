package io.gic.cinema.core;

public class BookingIdGenerator {

    private Integer bookingCounter;

    public BookingIdGenerator(){
        this.bookingCounter = 1;
    }

    public String nextBookingId() {
        if (bookingCounter > 9999) {
            throw new IllegalArgumentException("Booking ID has reached the maximum value of 9999");
        }
        return "GIC" + String.format("%04d", bookingCounter++);
    }

}
