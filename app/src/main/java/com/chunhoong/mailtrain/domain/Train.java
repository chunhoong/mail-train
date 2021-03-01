package com.chunhoong.mailtrain.domain;

import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
public class Train {

    private static Logger logger = LoggerFactory.getLogger(Train.class);

    private String name;
    private BigInteger capacity;
    private Station currentStation;
    private List<Deliverable> deliverables = new ArrayList<>();

    public static Train fromInput(String input) {
        String[] commaSeparatedValues = input.split(",");
        Train train = new Train();
        train.setName(commaSeparatedValues[0]);
        train.setCurrentStation(new Station(commaSeparatedValues[1]));
        train.setCapacity(new BigInteger(commaSeparatedValues[2]));
        return train;
    }

    public void performDelivery() {
        Deliverable deliverable = deliverables.get(0);
        travel(currentStation, deliverable.getSendFrom());
        loadDeliverable();
        currentStation = deliverable.getSendFrom();
        travel(currentStation, deliverable.getSendTo());
        dropDeliverable();
    }

    void loadDeliverable() {
        deliverables.get(0).setStatus(DeliveryStatus.IN_DELIVERY);
    }

    void dropDeliverable() {
        deliverables.get(0).setStatus(DeliveryStatus.DISPATCHED);
        deliverables.clear();
    }

    public void travel(Station origin, Station destination) {
        List<Station> route = new Navigate().apply(origin, destination);

        for (int i = 0; i < route.size(); i++) {
            Station currentStation = route.get(i);

            Optional.ofNullable(currentStation.getDuration())
                    .ifPresent(v -> {
                        int delay = v.multiply(BigInteger.valueOf(300)).intValue();
                        try {
                            Thread.sleep(delay);
                        } catch (InterruptedException e) {
                            logger.error(e.getMessage(), e);
                        }
                    });

            boolean hasArrived = i == route.size() - 1;

            if (hasArrived) {
                logger.info("n: {}, q: {}", currentStation.getName(), getName());
            } else {
                Station nextStation = route.get(i + 1);
                logger.info(
                        "n: {}, q: {}, moving {}->{}",
                        currentStation.getName(),
                        getName(),
                        currentStation.getName(),
                        nextStation.getName()
                );
            }
        }
    }

}
