package com.teixeira.diogo.stcp.dto;

import java.util.List;

/**
 * Created by teixe on 26/11/2016.
 */

public class StopDto {
    public long id;
    public String stopName;
    public List<RouteDto> routes;

    @Override
    public String toString() {
        return id + " - " + stopName;
    }
}
