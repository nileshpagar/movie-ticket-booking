package io.gic.cinema.domain;

import io.gic.cinema.core.ReservationHelper;

import java.util.Arrays;

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

    public boolean isBookingPresent(String bookingId) {
        return stream(chart).flatMap(Arrays::stream).anyMatch(seat -> seat != null && seat.equals(bookingId));
    }

    public int getNumberOfTicketsAvailable() {
        int count = 0;
        for (String[] row : chart) {
            for (String seat : row) {
                if (seat == null) {
                    count++;
                }
            }
        }
        return count;
    }

    public void confirm(String[][] bookingCopy) {
        this.chart = bookingCopy;
    }
}
