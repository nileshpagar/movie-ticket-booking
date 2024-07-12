package io.gic.cinema.model;

import io.gic.cinema.IOUtils;

public class Booking {

    private Integer bookingCounter;
    private String[][] bookingMap;
    private Integer rows;
    private Integer seatsPerRow;


    public Booking(Integer rows, Integer seatsPerRow) {
        bookingCounter = 1;
        this.rows = rows;
        this.seatsPerRow = seatsPerRow;
        this.bookingMap = new String[rows][seatsPerRow];
    }

    private void acceptBookingDetails() {
        String bookingId = Booking.getNextBookingId();
        String newPosition = "";
        String previousPosition = "";
        String[][] _bookingCopy = shallowCopyOf(booking);
        while(true) {
            try {
                _bookingCopy = reserve(numberOfTickets, _bookingCopy, bookingId, resolvePosition(newPosition, booking.length));
                printBookings(_bookingCopy, bookingId);
                newPosition = IOUtils.getUserInput("Enter blank to accept seat selection, or enter new seating position:");
                if (newPosition.isEmpty()) {
                    booking = _bookingCopy;
                    IOUtils.prompt(String.format(IOUtils.BLUE+"Booking ID: %s confirmed." + IOUtils.RESET, bookingId));
                    break;
                } else if(!newPosition.matches("^[A-Z][0-9]{2}$")){
                    IOUtils.promptError(String.format("New position: %s is wrong. Please enter in [row][seat-no] format. e.g. B03,C10 etc.", newPosition));
                    newPosition = previousPosition;
                    _bookingCopy = shallowCopyOf(booking);
                }
                else {
                    previousPosition = newPosition;
                    _bookingCopy = shallowCopyOf(booking);
                }
            } catch (Exception e) {
                IOUtils.promptError("Invalid input. Please try again.");
            }
        }
        return booking;
    }

    public String getNextBookingId() {
        return "GIC" + String.format("%04d", bookingCounter++);
    }

    public String[][] getBookingMap() {
        return bookingMap;
    }
}
