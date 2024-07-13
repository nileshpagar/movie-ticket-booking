package io.gic.cinema.controller;

import io.gic.cinema.core.BookingManager;
import io.gic.cinema.domain.Cinema;

import static io.gic.cinema.ui.Console.*;

public class BookingController {

    private Cinema cinema;
    private BookingManager bookingManager;

    public void start() {
        cinema = readCinemaDetails("GIC Cinemas");
        bookingManager = new BookingManager(cinema.getRows(), cinema.getSeatsPerRow());
        bookingWorkflow(cinema, bookingManager);
    }

    private void bookingWorkflow(Cinema cinema, BookingManager bookingManager) {
        do {
            int choice = acceptChoice(cinema.getName(), cinema.getMovieName(), bookingManager.getNumberOfTicketsAvailable());
            switch (choice) {
                case 1:
                    acceptBooking();
                    break;
                case 2:
                    checkBooking();
                    break;
                case 3:
                    exit();
                    break;
                default:
                    promptError("Invalid choice. Please try again.");
            }
        } while (true);
    }

    private void acceptBooking() {
        if (bookingManager.getNumberOfTicketsAvailable() == 0) {
            promptError("Sorry, no tickets available for booking.");
            return;
        }
        int numberOfTickets = acceptNumberOfTickets(bookingManager.getNumberOfTicketsAvailable());
        bookingManager.acceptBookingDetails(numberOfTickets);
    }

    private void checkBooking() {
        String bookingId = getUserInput("Enter booking ID, or enter blank to go back to main menu:");
        if (bookingId.isEmpty()) {
            return;
        } else if (!bookingManager.isBookingPresent(bookingId)) {
            promptError("Booking ID " + bookingId + " is NOT present. Please try again.");
            return;
        }
        printBookings(bookingManager.getBookingChart(), bookingId);
    }

    private void exit() {
        printBookings(bookingManager.getBookingChart(), null);
        prompt(CYAN+"Thanks for using " + cinema.getName() + " system. Bye!"+ RESET);
        System.exit(0);
    }

}