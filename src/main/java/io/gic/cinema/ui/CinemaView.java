package io.gic.cinema.ui;

import io.gic.cinema.domain.Cinema;

import java.util.Arrays;

import static io.gic.cinema.ui.UserInterface.getUserInput;
import static java.lang.Integer.parseInt;

public class CinemaView {

    static Cinema readCinemaDetails(String cinemaName) {
        Cinema cinema;
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

}
