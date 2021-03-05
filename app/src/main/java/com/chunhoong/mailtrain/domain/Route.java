package com.chunhoong.mailtrain.domain;

import lombok.Data;

import java.math.BigInteger;

@Data
public class Route {

    private String name;
    private String stationName;
    private String adjacentStationName;
    private BigInteger duration;

    // TODO: Extract to other layer as domain should not be aware of user input
    public static Route fromInput(String input) {
        String[] commaSeparatedValues = input.split(",");
        Route instance = new Route();
        instance.setName(commaSeparatedValues[0]);
        instance.setStationName(commaSeparatedValues[1]);
        instance.setAdjacentStationName(commaSeparatedValues[2]);
        instance.setDuration(new BigInteger(commaSeparatedValues[3]));
        return instance;
    }

}
