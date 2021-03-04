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

    public static Station fromInput(String input) {
        return new Station(input);
    }
}
