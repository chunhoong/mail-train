package com.chunhoong.mailtrain.domain;

import lombok.*;

import java.math.BigInteger;

@With
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
public class Station {

    private final String name;

    @EqualsAndHashCode.Exclude
    private String routeName;

    @EqualsAndHashCode.Exclude
    private BigInteger duration;

    // TODO: Extract to other layer as domain should not be aware of user input
    public static Station fromInput(String input) {
        return new Station(input);
    }
}
