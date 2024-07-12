package io.gic.cinema.model;

import io.gic.cinema.IOUtils;

import java.util.Arrays;

import static java.lang.Integer.parseInt;

public class Cinema {

    private String name = "GIC Cinemas";
    private String movieName;
    private Integer rows;
    private Integer seatsPerRow;

    public Cinema(String name) {
        this.name = name;
    }

    public Cinema(String movieName, Integer rows, Integer seatsPerRow) {
        this.movieName = movieName;
        this.rows = rows;
        this.seatsPerRow = seatsPerRow;
    }

    public void readCinemaDetails() {
        while(true) {
            try {
                String bookingRequest = IOUtils.getUserInput("Please define movie title and seating map in [Title] [Row] [SeatsPerRow] format:");

                String[] bookingRequestArray = bookingRequest.split("\\s");
                String _movieName = String.join(" ", Arrays.copyOfRange(bookingRequestArray, 0, bookingRequestArray.length - 2)).trim();

                int _rows = parseInt(bookingRequestArray[bookingRequestArray.length - 2]);
                int _seatsPerRow = parseInt(bookingRequestArray[bookingRequestArray.length - 1]);
                if (_rows < 1 || _seatsPerRow < 1 || _rows > 26 || _seatsPerRow > 50) {
                    IOUtils.promptError("Rows should be between 1 and 26, and seats per row should be between 1 and 50.");
                    throw new RuntimeException("Invalid input");
                }
                this.movieName = _movieName;
                this.rows = _rows;
                this.seatsPerRow = _seatsPerRow;
                break;
            } catch (Exception e) {
                IOUtils.promptError("Invalid input. Please try again.");
            }
        }
    }

    public String getMovieName() {
          return movieName;
     }

    public Integer getRows() {
        return rows;
    }

    public Integer getSeatsPerRow() {
        return seatsPerRow;
    }

    public String getName() {
        return name;
    }
}
