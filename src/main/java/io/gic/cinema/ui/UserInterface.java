package io.gic.cinema.ui;

import io.gic.cinema.domain.Cinema;

import java.util.Scanner;

public class UserInterface {

    public static final String RESET = "\033[0m";  // Text Reset
    public static final String RED = "\033[0;31m";    // RED
    public static final String BLUE = "\033[0;34m";   // BLUE
    public static final String CYAN = "\033[0;36m";   // CYAN
    public static final Scanner scanner = new Scanner(System.in);
    public static String lineBreak = System.lineSeparator();

    public static Cinema readCinemaDetails(String cinemaName) {
        return CinemaView.readCinemaDetails(cinemaName);
    }

    public static int acceptChoice(String cinemaName, String movieName, int numberOfSeats) {
        return MainView.acceptChoice(cinemaName, movieName, numberOfSeats);
    }

    public static int acceptNumberOfTickets(int numberOfTicketsAvailable) {
        return BookingView.acceptNumberOfTickets(numberOfTicketsAvailable);
    }

    public static void printBookings(String[][] booking, String bookingId) {
        BookingView.printBookings(booking, bookingId);
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
