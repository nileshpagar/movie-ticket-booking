package io.gic.cinema.ui;

import io.gic.cinema.domain.Cinema;

import java.util.Arrays;
import java.util.Scanner;

import static java.lang.Integer.parseInt;

public class UserInterface {

    public static final String RESET = "\033[0m";  // Text Reset
    public static final String RED = "\033[0;31m";    // RED
    public static final String BLUE = "\033[0;34m";   // BLUE
    public static final String CYAN = "\033[0;36m";   // CYAN
    public static final Scanner scanner = new Scanner(System.in);
    public static String lineBreak = System.lineSeparator();



    public static Cinema readCinemaDetails(String cinemaName) {
        Cinema cinema = null;
        while(true) {
            try {
                String bookingRequest = getUserInput("Please define movie title and seating map in [Title] [Row] [SeatsPerRow] format:");

                String[] bookingRequestArray = bookingRequest.split("\\s");
                String movieName = String.join(" ", Arrays.copyOfRange(bookingRequestArray, 0, bookingRequestArray.length - 2)).trim();

                int _rows = parseInt(bookingRequestArray[bookingRequestArray.length - 2]);
                int _seatsPerRow = parseInt(bookingRequestArray[bookingRequestArray.length - 1]);
                if (_rows < 1 || _seatsPerRow < 1 || _rows > 26 || _seatsPerRow > 50) {
                    UserInterface.promptError("Rows should be between 1 and 26, and seats per row should be between 1 and 50.");
                    throw new RuntimeException("Invalid input");
                }
                cinema = new Cinema(cinemaName, movieName, _rows, _seatsPerRow);
                break;
            } catch (Exception e) {
                UserInterface.promptError("Invalid input. Please try again.");
            }
        }
        return cinema;
    }

    public static int acceptChoice(String cinemaName, String movieName, int numberOfSeats) {
        int choice = 0;
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

    public static String getUserInput(String message) {
        prompt(message);
        return scanner.nextLine().trim();
    }

    public static void prompt(String message) {
        System.out.println(message);
    }

    public static void promptError(String message) {
        System.out.println(RED +message+ RESET);
        System.out.println(" ");
    }
}
