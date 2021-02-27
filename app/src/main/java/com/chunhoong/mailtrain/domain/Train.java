package com.chunhoong.mailtrain.domain;

import lombok.Data;

import java.math.BigInteger;

@Data
public class Train {

    private String name;
    private BigInteger capacity;
    private String startedFrom;

    public void travel(Station destination) {

    }

    public static Train fromInput(String input) {
        String[] commaSeparatedValues = input.split(",");
        Train train = new Train();
        train.setName(commaSeparatedValues[0]);
        train.setStartedFrom(commaSeparatedValues[1]);
        train.setCapacity(new BigInteger(commaSeparatedValues[2]));
        return train;
    }

}
