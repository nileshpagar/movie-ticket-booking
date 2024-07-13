package io.gic.cinema;

import io.gic.cinema.controller.BookingController;

public class Application {

    public static void main(String[] args) {
        BookingController bookingController = new BookingController();
        bookingController.start();
    }

}