package io.gic.cinema.ui;

import static io.gic.cinema.ui.UserInterface.prompt;
import static io.gic.cinema.ui.UserInterface.promptError;
import static java.lang.Integer.parseInt;

public class MainView {

    static int acceptChoice(String cinemaName, String movieName, int numberOfSeats) {
        int choice;
        while(true) {
            prompt(" ");
            prompt("Welcome to " + cinemaName);
            prompt("[1] Book tickets for " + movieName + " (" + numberOfSeats + " seats available)");
            prompt("[2] Check Bookings");
            prompt("[3] Exit");
            try {
                choice = parseInt(UserInterface.getUserInput("Please enter your selection:"));
                if (choice < 1 || choice > 3) {
                    throw new RuntimeException("Invalid choice");
                }
                break;
            } catch (Exception e) {
                promptError("Invalid choice. Please try again.");
            }
        }
        return choice;
    }

}
