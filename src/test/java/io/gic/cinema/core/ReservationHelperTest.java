package io.gic.cinema.core;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ReservationHelperTest {

    private ReservationHelper helper;
    private String[][] bookingChart;

    @BeforeEach
    void setUp() {
        helper = new ReservationHelper();
        bookingChart = new String[3][5]; // 3 rows, 5 seats per row
    }

    @Test
    void testReserveDefault() {
        String bookingId = "GIC0001";
        helper.reserve(3, bookingChart, bookingId, null);
        // Expect the last row, center outwards to be reserved
        assertEquals(bookingId, bookingChart[2][2]);
        assertEquals(bookingId, bookingChart[2][1]);
        assertEquals(bookingId, bookingChart[2][3]);
    }

    @Test
    void testReserveDefaultWithOverflow() {
        String bookingId = "GIC0002";
        helper.reserve(6, bookingChart, bookingId, null);
        // Expect overflow handling: last row, center outwards, and first two seats of the second row
        assertEquals(bookingId, bookingChart[2][0]);
        assertEquals(bookingId, bookingChart[2][1]);
        assertEquals(bookingId, bookingChart[2][2]);
        assertEquals(bookingId, bookingChart[2][3]);
        assertEquals(bookingId, bookingChart[2][4]);
        assertEquals(bookingId, bookingChart[1][2]);
    }

    @Test
    void testReservePreferred() {
        String bookingId = "GIC0002";
        int[] startPosition = {1, 2}; // Second row, third seat
        helper.reserve(2, bookingChart, bookingId, startPosition);
        // Expect the specified seats to be reserved
        assertEquals(bookingId, bookingChart[1][2]);
        assertEquals(bookingId, bookingChart[1][3]);
    }

    @Test
    void testReservePreferredWithOverflow() {
        String bookingId = "GIC0003";
        int[] startPosition = {1, 4}; // Second row, last seat
        helper.reserve(3, bookingChart, bookingId, startPosition);
        // Expect overflow handling: last seat of the specified row and first two of the next row
        assertEquals(bookingId, bookingChart[1][4]);
        assertEquals(bookingId, bookingChart[0][1]);
        assertEquals(bookingId, bookingChart[0][2]);
    }



}