package com.chunhoong.mailtrain.domain;

import lombok.Data;

@Data
public class Station {

    private String name;

    public static Station fromInput(String input) {
        Station instance = new Station();
        instance.setName(input);
        return instance;
    }
}
