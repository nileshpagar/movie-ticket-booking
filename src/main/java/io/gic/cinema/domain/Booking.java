package io.gic.cinema.domain;

import java.util.Arrays;

import static io.gic.cinema.ui.UserInterface.*;
import static java.lang.Integer.parseInt;
import static java.util.Arrays.stream;

public class Booking {

    private Integer bookingCounter;
    private String[][] bookingMap;

    public Booking(Integer rows, Integer seatsPerRow) {
        bookingCounter = 1;
        this.bookingMap = new String[rows][seatsPerRow];
    }

    public String[][] getBookingChart() {
        return bookingMap;
    }

    public void acceptBookingDetails(Integer numberOfTickets) {
        String bookingId = getNextBookingId();
        String newPosition = "";
        String previousPosition = "";
        String[][] bookingCopy = shallowCopyOf(bookingMap);
        while(true) {
            try {
                bookingCopy = reserve(numberOfTickets, bookingCopy, bookingId, resolvePosition(newPosition, bookingMap.length));
                printBookings(bookingCopy, bookingId);
                newPosition = getUserInput("Enter blank to accept seat selection, or enter new seating position:");
                if (newPosition.isEmpty()) {
                    bookingMap = bookingCopy;
                    prompt(String.format(BLUE+"Booking ID: %s confirmed." + RESET, bookingId));
                    break;
                } else if(!newPosition.matches("^[A-Z](0[1-9]|[1-9][0-9])$")){
                    promptError(String.format("New position: %s is wrong. Please enter in [row][seat-no] format. e.g. B03,C10 etc.", newPosition));
                    newPosition = previousPosition;
                    bookingCopy = shallowCopyOf(bookingMap);
                }
                else {
                    previousPosition = newPosition;
                    bookingCopy = shallowCopyOf(bookingMap);
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
        return stream(bookingMap).flatMap(Arrays::stream).anyMatch(seat -> seat != null && seat.equals(bookingId));
    }

    public int getNumberOfTicketsAvailable() {
        int count = 0;
        for (String[] row : bookingMap) {
            for (String seat : row) {
                if (seat == null) {
                    count++;
                }
            }
        }
        return count;
    }

    public String[][] reserve(int numberOfTickets, String[][] booking, String bookingId, int[] position) {
        if(position == null) {
            return reserveDefault(numberOfTickets, booking, bookingId, 0);
        } else {
            return reservePreferred(numberOfTickets, booking, bookingId, position);
        }
    }

    private String[][] reserveDefault(int numberOfTickets, String[][] booking, String bookingId, int rowToStart) {
        int rows = rowToStart == 0 ? booking.length: rowToStart;
        int seatsPerRow = booking[0].length;
        int middle = seatsPerRow / 2;
        int ticketsToAllocate = numberOfTickets;

        for (int i = rows - 1; i >= 0 && ticketsToAllocate > 0; i--) {  //start with the furthest row from the screen
            int leftIndex = middle;
            int rightIndex = middle + ((seatsPerRow % 2 == 0) ? -1 : 0); // Adjust for even number of seats per row
            while (ticketsToAllocate > 0 && (leftIndex >= 0 || rightIndex < seatsPerRow)) {
                if (leftIndex >= 0 && booking[i][leftIndex] == null) {
                    booking[i][leftIndex] = bookingId;
                    ticketsToAllocate--;
                }
                if (ticketsToAllocate > 0 && rightIndex < seatsPerRow && booking[i][rightIndex] == null) {
                    booking[i][rightIndex] = bookingId;
                    ticketsToAllocate--;
                }
                leftIndex--;   //move to left
                rightIndex++;  //also move to right
            }
        }
        return booking;
    }

    private String[][] reservePreferred(int numberOfTickets, String[][] booking, String bookingId, int[] position) {
        int seatsPerRow = booking[0].length;
        int startRow = position[0];
        int startSeat = position[1];
        int ticketsToAllocate = numberOfTickets;

        // Fill up empty seats from the specified position to the right
        for (int j = startSeat; j < seatsPerRow && ticketsToAllocate > 0; j++) {
            if (booking[startRow][j] == null) {
                booking[startRow][j] = bookingId;
                ticketsToAllocate--;
            }
        }

        // If tickets still need to be allocated, use the default allocation method for the remaining tickets
        if (ticketsToAllocate > 0) {
            reserveDefault(ticketsToAllocate, booking, bookingId, startRow);
        }

        return booking;
    }


    public String getNextBookingId() {
        return "GIC" + String.format("%04d", bookingCounter++);
    }

}
