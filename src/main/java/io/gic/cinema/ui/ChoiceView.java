package io.gic.cinema.ui;

import static io.gic.cinema.ui.UserInterface.getUserInput;
import static io.gic.cinema.ui.UserInterface.prompt;
import static java.lang.Integer.parseInt;

public class ChoiceView {

    static int acceptChoice(String cinemaName, String movieName, int numberOfSeats) {
        int choice = 0;
        prompt(" ");
        prompt("Welcome to " + cinemaName);
        prompt("[1] Book tickets for " + movieName + " (" + numberOfSeats + " seats available)");
        prompt("[2] Check Bookings");
        prompt("[3] Exit");
        try {
            choice = parseInt(getUserInput("Please enter your selection:"));
            if (choice < 1 || choice > 3) {
                throw new RuntimeException("Invalid choice");
            }
        } catch (Exception e) {
            //do nothing
        }
        return choice;
    }

}
