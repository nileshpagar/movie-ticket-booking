package io.gic.cinema.controller;

import io.gic.cinema.domain.Booking;
import io.gic.cinema.domain.Cinema;
import io.gic.cinema.ui.UserInterface;

import static io.gic.cinema.ui.UserInterface.*;

public class BookingController {

    private Cinema cinema;
    private Booking booking;

    public void start() {
        cinema = readCinemaDetails("GIC Cinemas");
        booking = new Booking(cinema.getRows(), cinema.getSeatsPerRow());
        bookingWorkflow(cinema, booking);
    }

    private void bookingWorkflow(Cinema cinema, Booking booking) {
        do {
            int choice = acceptChoice(cinema.getName(), cinema.getMovieName(), booking.getNumberOfTicketsAvailable());
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

    private void exit() {
        printBookings(booking.getBookingChart(), null);
        prompt(CYAN+"Thanks for using " + cinema.getName() + " system. Bye!"+ RESET);
        System.exit(0);
    }

    private void checkBooking() {
        String bookingId = getUserInput("Enter booking ID, or enter blank to go back to main menu:");
        if (bookingId.isEmpty()) {
            return;
        } else if (!booking.isBookingPresent(bookingId)) {
            promptError("Booking ID " + bookingId + " is NOT present. Please try again.");
            return;
        }
        UserInterface.printBookings(booking.getBookingChart(), bookingId);
    }

    private void acceptBooking() {
        if (booking.getNumberOfTicketsAvailable() == 0) {
            promptError("Sorry, no tickets available for booking.");
            return;
        }
        int numberOfTickets = acceptNumberOfTickets(booking.getNumberOfTicketsAvailable());
        booking.acceptBookingDetails(numberOfTickets);
    }

}