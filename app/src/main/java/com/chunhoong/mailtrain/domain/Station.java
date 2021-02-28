package com.chunhoong.mailtrain.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
public class Station {

    @EqualsAndHashCode.Include
    private final String name;

    public static Station fromInput(String input) {
        return new Station(input);
    }
}
