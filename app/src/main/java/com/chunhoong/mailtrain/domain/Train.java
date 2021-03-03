package com.chunhoong.mailtrain.domain;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@Data
public class Train {

    private static final String LOGGING_TEMPLATE_PRE_ARRIVAL = "@{}, n =  {}, q = {}, load = { {} }, drop = { {} }, " +
            "moving {}->{}" +
            " arr {}";
    private static final String LOGGING_TEMPLATE_ARRIVAL = "@{}, n =  {}, q = {}, load = { {} }, drop = { {} }";
    private static Logger logger = LoggerFactory.getLogger(Train.class);
    private String name;
    private BigInteger capacity;
    private Station currentStation;
    private List<Deliverable> deliverables = new ArrayList<>();
    private AtomicReference<BigInteger> time = new AtomicReference<>(BigInteger.ZERO);

    public static Train fromInput(String input) {
        String[] commaSeparatedValues = input.split(",");
        Train train = new Train();
        train.setName(commaSeparatedValues[0]);
        train.setCurrentStation(new Station(commaSeparatedValues[1]));
        train.setCapacity(new BigInteger(commaSeparatedValues[2]));
        return train;
    }

    public void performDelivery() {
        if (!currentStation.equals(deliverables.get(0).getSendFrom())) {
            travelToPickupPoint();
        }
        loadDeliverable();
        travelToDeliver();
        dropDeliverable();
    }

    void loadDeliverable() {
        deliverables.get(0).setStatus(DeliveryStatus.IN_DELIVERY);
    }

    void dropDeliverable() {
        deliverables.get(0).setStatus(DeliveryStatus.DISPATCHED);
        deliverables.clear();
    }

    void travelToPickupPoint() {
        Deliverable deliverable = deliverables.get(0);

        BiConsumer<Station, Station> handleDepartureAndPassThrough = (currentStation, nextStation) -> logger.info(
                LOGGING_TEMPLATE_PRE_ARRIVAL,
                time.getAndAccumulate(nextStation.getDuration(), BigInteger::add),
                currentStation.getName(),
                getName(),
                "",
                "",
                currentStation.getName(),
                nextStation.getName(),
                time.get()
        );

        travel(currentStation, deliverable.getSendFrom(), handleDepartureAndPassThrough, null,
                handleDepartureAndPassThrough);
    }

    void travelToDeliver() {
        Deliverable deliverable = deliverables.get(0);

        currentStation = deliverable.getSendFrom();

        BiConsumer<Station, Station> handleDeparture = (currentStation, nextStation) -> logger.info(
                LOGGING_TEMPLATE_PRE_ARRIVAL,
                time.getAndAccumulate(nextStation.getDuration(), BigInteger::add),
                currentStation.getName(),
                getName(),
                deliverable.getName(),
                "",
                currentStation.getName(),
                nextStation.getName(),
                time.get()
        );

        Consumer<Station> handleArrival = (currentStation) -> logger.info(
                LOGGING_TEMPLATE_ARRIVAL,
                time.get(),
                currentStation.getName(),
                getName(),
                "",
                deliverable.getName()
        );

        BiConsumer<Station, Station> handlePassThrough = (currentStation, nextStation) -> logger.info(
                LOGGING_TEMPLATE_PRE_ARRIVAL,
                time.getAndAccumulate(nextStation.getDuration(), BigInteger::add),
                currentStation.getName(),
                getName(),
                "",
                "",
                currentStation.getName(),
                nextStation.getName(),
                time.get()
        );

        travel(currentStation, deliverable.getSendTo(), handleDeparture, handleArrival, handlePassThrough);
    }

    void travel(Station origin, Station destination, BiConsumer<Station, Station> onDeparture,
                Consumer<Station> onArrival,
                BiConsumer<Station, Station> onPassThrough) {
        if (origin.equals(destination)) {
            return;
        }

        List<Station> route = new Navigate().apply(origin, destination);

        for (int i = 0; i < route.size(); i++) {
            Station currentStation = route.get(i);

            if (currentStation.getDuration() != null) {
                int delay = currentStation.getDuration().multiply(BigInteger.valueOf(300)).intValue();
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    logger.error(e.getMessage(), e);
                }
            }

            boolean hasDeparted = i == 0;
            boolean hasArrived = i == route.size() - 1;
            boolean hasPassedThroughIntermediateStations = i != 0 && i != route.size() - 1;

            if (hasDeparted && onDeparture != null) {
                Station nextStation = route.get(i + 1);
                onDeparture.accept(currentStation, nextStation);
                continue;
            }

            if (hasArrived && onArrival != null) {
                onArrival.accept(currentStation);
                continue;
            }

            if (hasPassedThroughIntermediateStations && onPassThrough != null) {
                Station nextStation = route.get(i + 1);
                onPassThrough.accept(currentStation, nextStation);
            }
        }
    }

}
