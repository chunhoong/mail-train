package com.chunhoong.mailtrain.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigInteger;

@Data
@EqualsAndHashCode
public class Deliverable {

    @EqualsAndHashCode.Include
    private String name;

    private BigInteger weight;
    private String sendFrom;
    private String sendTo;
    private DeliveryStatus status;

    public static Deliverable fromInput(String input) {
        String[] commaSeparatedValues = input.split(",");
        Deliverable instance = new Deliverable();
        instance.setName(commaSeparatedValues[0]);
        instance.setSendFrom(commaSeparatedValues[1]);
        instance.setSendTo(commaSeparatedValues[2]);
        instance.setWeight(new BigInteger(commaSeparatedValues[3]));
        return instance;
    }

}
