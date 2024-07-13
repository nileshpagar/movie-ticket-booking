package io.gic.cinema.ui;

import static java.lang.Integer.parseInt;

public class BookingView {

    public static int acceptNumberOfTickets(int numberOfTicketsAvailable) {
        int numberOfTickets = 0;
        while(true) {
            try {
                numberOfTickets = parseInt(UserInterface.getUserInput("Enter number of tickets to book, or enter blank to go back to main menu:"));
                if (numberOfTicketsAvailable < numberOfTickets || numberOfTickets < 1) {
                    UserInterface.promptError(String.format("Sorry, there are only %d tickets available.", numberOfTicketsAvailable));
                    continue;
                }
                break;
            } catch (Exception e) {
                UserInterface.promptError("Invalid input. Please try again.");
            }
        }
        return numberOfTickets;
    }

}
