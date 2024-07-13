package io.gic.cinema.domain;

public class Cinema {

    private String name = "GIC Cinemas";
    private String movieName;
    private Integer rows;
    private Integer seatsPerRow;

    public Cinema(String name, String movieName, Integer rows, Integer seatsPerRow) {
        this.name = name;
        this.movieName = movieName;
        this.rows = rows;
        this.seatsPerRow = seatsPerRow;
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
