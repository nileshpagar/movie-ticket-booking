package io.gic.cinema.core;

import io.gic.cinema.domain.Booking;

import java.util.Arrays;

import static io.gic.cinema.ui.Console.*;
import static java.lang.Integer.parseInt;

public class BookingManager {

    private final BookingIdGenerator bookingIdGenerator;
    private final Booking booking;

    public BookingManager(Integer rows, Integer seatsPerRow) {
        this.booking = new Booking(rows, seatsPerRow);
        this.bookingIdGenerator = new BookingIdGenerator();
    }

    public String[][] getBookingChart() {
        return booking.getChart();
    }

    public void acceptBookingDetails(Integer numberOfTickets) {
        String bookingId = bookingIdGenerator.nextBookingId();
        String newPosition = "";
        String previousPosition = "";
        String[][] bookingCopy = shallowCopyOf(booking.getChart());
        while(true) {
            try {
                bookingCopy = booking.reserve(numberOfTickets, bookingCopy, bookingId, resolvePosition(newPosition, booking.getChart().length));
                printBookings(bookingCopy, bookingId);
                newPosition = getUserInput("Enter blank to accept seat selection, or enter new seating position:");
                if (newPosition.isEmpty()) {
                    booking.confirm(bookingCopy);
                    prompt(String.format(BLUE+"Booking ID: %s confirmed." + RESET, bookingId));
                    break;
                } else if(!newPosition.matches("^[A-Z](0[1-9]|[1-9][0-9])$")){
                    promptError(String.format("New position: %s is wrong. Please enter in [row][seat-no] format. e.g. B03,C10 etc.", newPosition));
                    newPosition = previousPosition;
                    bookingCopy = shallowCopyOf(booking.getChart());
                }
                else {
                    previousPosition = newPosition;
                    bookingCopy = shallowCopyOf(booking.getChart());
                }
            } catch (Exception e) {
                promptError("Invalid input. Please try again.");
            }
        }
    }

    private static String[][] shallowCopyOf(String[][] booking) {
        String[][] copy = new String[booking.length][];
        for (int i = 0; i < booking.length; i++) {
            copy[i] = Arrays.copyOf(booking[i], booking[i].length);
        }
        return copy;
    }

    private static int[] resolvePosition(String position, int maxRows) {
        if (position == null || position.length() < 3) {
            return null;
        }
        int row = maxRows - (position.charAt(0) - 'A') - 1; // Convert 'A' to m-1, 'B' to m-2, etc.
        int seat = parseInt(position.substring(1)) - 1; // Convert seat number to 0-based index
        return new int[]{row, seat};
    }

    public boolean isBookingPresent(String bookingId) {
        return booking.isBookingPresent(bookingId);
    }

    public int getNumberOfTicketsAvailable() {
       return booking.getNumberOfTicketsAvailable();
    }

}
