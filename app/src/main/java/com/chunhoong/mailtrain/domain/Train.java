package com.chunhoong.mailtrain.domain;

import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.List;

@Data
public class Train {

    private static Logger logger = LoggerFactory.getLogger(Train.class);

    private String name;
    private BigInteger capacity;
    private String startedFrom;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private Navigate navigate = new Navigate();

    public static Train fromInput(String input) {
        String[] commaSeparatedValues = input.split(",");
        Train train = new Train();
        train.setName(commaSeparatedValues[0]);
        train.setStartedFrom(commaSeparatedValues[1]);
        train.setCapacity(new BigInteger(commaSeparatedValues[2]));
        return train;
    }

    public void travel(Station origin, Station destination) {
        List<Station> route = navigate.apply(origin, destination);

        for (int i = 0; i < route.size(); i++) {
            Station currentStation = route.get(i);

            if (i != route.size() - 1) {
                Station nextStation = route.get(i + 1);
                logger.info(
                        "n: {}, q: {}, moving {}->{}",
                        currentStation.getName(),
                        getName(),
                        currentStation.getName(),
                        nextStation.getName()
                );
            } else {
                logger.info("n: {}, q: {}", currentStation.getName(), getName());
            }
        }
    }

}
