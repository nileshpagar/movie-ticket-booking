package io.gic.cinema.core;

public class ReservationHelper {

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


}
