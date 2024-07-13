package io.gic.cinema.domain;

import io.gic.cinema.core.ReservationHelper;

import java.util.Arrays;
import java.util.Objects;

import static java.util.Arrays.stream;

public class Booking {

    private final ReservationHelper reservationHelper;
    private String[][] chart;

    public Booking(Integer rows, Integer seatsPerRow) {
        this.chart = new String[rows][seatsPerRow];
        this.reservationHelper = new ReservationHelper();
    }

    public String[][] getChart() {
        return this.chart;
    }

    public String[][] reserve(int numberOfTickets, String[][] booking, String bookingId, int[] position) {
        return reservationHelper.reserve(numberOfTickets, booking, bookingId, position);
    }

    public void confirm(String[][] bookingCopy) {
        this.chart = bookingCopy;
    }

    public boolean isBookingPresent(String bookingId) {
        return stream(chart).flatMap(Arrays::stream).anyMatch(seat -> seat != null && seat.equals(bookingId));
    }

    public int getNumberOfTicketsAvailable() {
        return (int) stream(chart).flatMap(Arrays::stream).filter(Objects::isNull).count();
    }

}
