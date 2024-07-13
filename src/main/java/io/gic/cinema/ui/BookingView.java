package io.gic.cinema.ui;

import static io.gic.cinema.ui.Console.*;
import static java.lang.Integer.parseInt;

public class BookingView {

    static int acceptNumberOfTickets(int numberOfTicketsAvailable) {
        int numberOfTickets;
        while(true) {
            try {
                numberOfTickets = parseInt(getUserInput("Enter number of tickets to book, or enter blank to go back to main menu:"));
                if (numberOfTickets < 1) {
                    promptError(String.format("Sorry, please enter value between 1 to %d.", numberOfTicketsAvailable));
                    continue;
                }
                if (numberOfTicketsAvailable < numberOfTickets && numberOfTickets > 1) {
                    promptError(String.format("Sorry, there are only %d tickets available.", numberOfTicketsAvailable));
                    continue;
                }
                break;
            } catch (Exception e) {
                promptError("Invalid input. Please try again.");
            }
        }
        return numberOfTickets;
    }


    static String printBookings(String[][] booking, String bookingId) {
        StringBuilder output = new StringBuilder();

        printHeader(output, booking);
        printSeatingChart(output, booking, bookingId);
        printFooter(output, booking);

        //print full booking details in BLUE color
        prompt(BLUE + output + RESET);
        return output.toString();
    }

    private static void printHeader(StringBuilder output, String[][] booking) {
        int seatsPerRow = booking[0].length;
        String cinemaTheatre = "SCREEN";
        int headerPaddingSize = (seatsPerRow * 3 + 2)/2 - cinemaTheatre.length()/2;  // 3 characters per seat + 2 spaces
        String padding = String.format("%" + headerPaddingSize + "s", "");
        output.append(lineBreak);
        output.append(padding).append(cinemaTheatre).append(padding);   // center align cinema name
        output.append(lineBreak);
        output.append(String.format("%" + ((headerPaddingSize * 2) + cinemaTheatre.length()) + "s", "-").replace(' ', '-'));  // underline cinema name
        output.append(lineBreak);
    }

    private static void printSeatingChart(StringBuilder output, String[][] booking, String bookingId) {
        int rows = booking.length;
        int seatsPerRow = booking[0].length;
        char A = 'A';
        A = (char) (A + rows - 1);  //convert 0-indexed rows to A-indexed rows
        for (int i = 1; i <= rows; i++) {
            output.append(A).append(" ");
            for (int j = 1; j <= seatsPerRow; j++) {
                if(booking[i-1][j-1] != null && booking[i-1][j-1].equals(bookingId)) {
                    output.append(" o ");  // o for occupied
                } else if(booking[i-1][j-1] != null && !booking[i-1][j-1].equals(bookingId)) {
                    output.append(" # ");  // # for occupied by someone else
                } else {
                    output.append(" . ");  // . for available
                }
            }
            output.append(lineBreak);
            A--;
        }
        output.append(" ");
    }

    private static void printFooter(StringBuilder output, String[][] booking) {
        int seatsPerRow = booking[0].length;
        output.append(" ");
        for (int i = 1; i <= seatsPerRow; i++) {
            output.append(String.format("%3d", i));   // print seat numbers starting from 1
        }
        output.append(lineBreak).append(lineBreak);
    }
}
