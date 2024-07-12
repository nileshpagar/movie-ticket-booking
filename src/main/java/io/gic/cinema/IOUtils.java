package io.gic.cinema;

import java.util.Scanner;

public class IOUtils {
    public static final String RESET = "\033[0m";  // Text Reset
    public static final String RED = "\033[0;31m";    // RED
    public static final String BLUE = "\033[0;34m";   // BLUE
    public static final String CYAN = "\033[0;36m";   // CYAN

    public static final Scanner scanner = new Scanner(System.in);

    private IOUtils() {
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
