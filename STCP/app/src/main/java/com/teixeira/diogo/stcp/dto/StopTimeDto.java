package com.teixeira.diogo.stcp.dto;

/**
 * Created by teixe on 27/11/2016.
 */

public class StopTimeDto {
    public String arrivalTime;
    public String departureTime;
    public int stopID;

    @Override
    public String toString() {
        return "StopTimeDto{" +
                "arrivalTime='" + arrivalTime + '\'' +
                ", departureTime='" + departureTime + '\'' +
                ", stopID=" + stopID +
                '}';
    }
}
