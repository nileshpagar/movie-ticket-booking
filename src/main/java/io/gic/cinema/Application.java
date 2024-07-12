package io.gic.cinema;

import io.gic.cinema.model.Booking;
import io.gic.cinema.model.Cinema;
import java.util.Arrays;

import static java.lang.Integer.parseInt;

public class Application {

    public static void main(String[] args) {

        Cinema cinema = new Cinema("GIC Cinemas");
        cinema.readCinemaDetails();

        String[][] booking = new String[cinema.getRows()][cinema.getSeatsPerRow()];
        int numberOfSeats = cinema.getRows() * cinema.getSeatsPerRow();

        while(true) {
            int choice = acceptChoice(cinema, numberOfSeats);
            switch (choice) {
                case 1:
                    booking = acceptBookingDetails(booking);
                    break;
                case 2:
                    String bookingId = IOUtils.getUserInput("Enter booking ID, or enter blank to go back to main menu:");
                    if (bookingId.isEmpty()) {
                        break;
                    } else if (!isBookingPresent(booking, bookingId)) {
                        IOUtils.promptError("Booking ID " + bookingId + " is NOT present. Please try again.");
                        break;
                    }
                    printBookings(booking, bookingId);
                    break;
                case 3:
                    printBookings(booking, null);
                    IOUtils.prompt(IOUtils.CYAN+"Thanks for using " + cinema.getName() + " system. Bye!"+ IOUtils.RESET);
                    System.exit(0);
                    break;
                default:
                    IOUtils.promptError("Invalid choice. Please try again.");
            }
        }
    }

    private static boolean isBookingPresent(String[][] booking, String bookingId) {
        for (String[] row : booking) {
            for (String seat : row) {
                if (seat != null && seat.equals(bookingId)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static String[][] acceptBookingDetails(String[][] booking) {
        int numberOfTickets = acceptNumberOfTickets(booking);
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

    private static int[] resolvePosition(String position, int maxRows) {
        if (position == null || position.length() < 3) {
            return null;
        }
        int row = maxRows - (position.charAt(0) - 'A') - 1; // Convert 'A' to m-1, 'B' to m-2, etc.
        int seat = Integer.parseInt(position.substring(1)) - 1; // Convert seat number to 0-based index
        return new int[]{row, seat};
    }

    private static int acceptNumberOfTickets(String[][] booking) {
        int numberOfTickets = 0;
        while(true) {
            try {
                int numberOfTicketsAvailable = getNumberOfTicketsAvailable(booking);
                numberOfTickets = parseInt(IOUtils.getUserInput("Enter number of tickets to book, or enter blank to go back to main menu:"));
                if (numberOfTicketsAvailable < numberOfTickets || numberOfTickets < 1) {
                    IOUtils.promptError(String.format("Sorry, there are only %d tickets available.", numberOfTicketsAvailable));
                    continue;
                }
                break;
            } catch (Exception e) {
                IOUtils.promptError("Invalid input. Please try again.");
            }
        }
        return numberOfTickets;
    }

    private static int getNumberOfTicketsAvailable(String[][] booking) {
        int count = 0;
        for (String[] row : booking) {
            for (String seat : row) {
                if (seat == null) {
                    count++;
                }
            }
        }
        return count;
    }

    public static String[][] reserve(int numberOfTickets, String[][] booking, String bookingId, int[] position) {
        if(position == null) {
            return reserveDefault(numberOfTickets, booking, bookingId, 0);
        } else {
            return reservePreferred(numberOfTickets, booking, bookingId, position);
        }
    }

    private static String[][] reserveDefault(int numberOfTickets, String[][] booking, String bookingId, int rowToStart) {
        int rows = rowToStart == 0 ? booking.length: rowToStart;
        int seatsPerRow = booking[0].length;
        int middle = seatsPerRow / 2;
        int ticketsToAllocate = numberOfTickets;

        for (int i = rows - 1; i >= 0 && ticketsToAllocate > 0; i--) {
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
                leftIndex--;
                rightIndex++;
            }
        }
        return booking;
    }

    private static String[][] reservePreferred(int numberOfTickets, String[][] booking, String bookingId, int[] position) {
        int rows = booking.length;
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
            booking = reserveDefault(ticketsToAllocate, booking, bookingId, startRow);
        }

        return booking;    }

    private static String[][] shallowCopyOf(String[][] booking) {
        String[][] copy = new String[booking.length][];
        for (int i = 0; i < booking.length; i++) {
            copy[i] = Arrays.copyOf(booking[i], booking[i].length);
        }
        return copy;
    }


    private static int acceptChoice(Cinema cinema, int numberOfSeats) {
        int choice = 0;
        while(true) {
            IOUtils.prompt(" ");
            IOUtils.prompt("Welcome to " + cinema.getName());
            IOUtils.prompt("[1] Book tickets for " + cinema.getMovieName() + " (" + numberOfSeats + " seats available)");
            IOUtils.prompt("[2] Check Bookings");
            IOUtils.prompt("[3] Exit");
            try {
                choice = parseInt(IOUtils.getUserInput("Please enter your selection:"));
                if (choice < 1 || choice > 3) {
                    throw new RuntimeException("Invalid choice");
                }
                break;
            } catch (Exception e) {
                IOUtils.promptError("Invalid choice. Please try again.");
            }
        }
        return choice;
    }

    private static void printBookings( String[][] bookings, String BookingId) {

        int rows = bookings.length;
        int seatsPerRow = bookings[0].length;

        String lineBreak = System.lineSeparator();
        StringBuilder output = new StringBuilder();

        String cinemaTheatre = "SCREEN";
        output.append(lineBreak);
        int paddingSize = (seatsPerRow * 3 + 2)/2 - cinemaTheatre.length()/2;  // 3 characters per seat + 2 spaces

        String padding = String.format("%" + paddingSize + "s", "");
        output.append(padding).append(cinemaTheatre).append(padding);   // center align cinema name

        output.append(lineBreak);
        output.append(String.format("%" + ((paddingSize * 2) + cinemaTheatre.length()) + "s", "-").replace(' ', '-'));  // underline cinema name
        output.append(lineBreak);
        char A = 'A';
        A = (char) (A + rows - 1);  //convert 0-indexed rows to A-indexed rows
        for (int i = 1; i <= rows; i++) {
            output.append(A).append(" ");
            for (int j = 1; j <= seatsPerRow; j++) {
                if(bookings[i-1][j-1] != null && bookings[i-1][j-1].equals(BookingId)) {
                    output.append(" o ");  // o for occupied
                } else if(bookings[i-1][j-1] != null && !bookings[i-1][j-1].equals(BookingId)) {
                    output.append(" # ");  // # for occupied by someone else
                } else {
                    output.append(" . ");  // . for available
                }
            }
            output.append(lineBreak);
            A--;
        }
        output.append(" ");
        for (int i = 1; i <= seatsPerRow; i++) {
            output.append(String.format("%3d", i));   // print seat numbers starting from 1
        }
        output.append(lineBreak).append(lineBreak);

        IOUtils.prompt(IOUtils.BLUE+output.toString()+ IOUtils.RESET);
    }

}