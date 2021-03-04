package com.chunhoong.mailtrain.service;

import com.chunhoong.mailtrain.domain.Deliverable;
import com.chunhoong.mailtrain.domain.DeliveryStatus;
import com.chunhoong.mailtrain.domain.Navigate;
import com.chunhoong.mailtrain.domain.Train;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;

public class DeliveryService {

    private static final Logger logger = LoggerFactory.getLogger(DeliveryService.class);
    private static final DeliveryService instance = new DeliveryService();

    @Getter
    private final List<Deliverable> deliverables = new ArrayList<>();

    private DeliveryService() {
    }

    public static DeliveryService getInstance() {
        return instance;
    }

    public void addDeliverable(Deliverable deliverable) {
        deliverables.add(deliverable);
    }

    public synchronized boolean hasDeliverable(Train train) {
        Predicate<Deliverable> notBooked = deliverable -> deliverable.getStatus() == DeliveryStatus.NOT_BOOKED;
        Predicate<Deliverable> loadable = deliverable -> deliverable.getWeight().compareTo(train.getCapacity()) <= 0;

        AtomicBoolean hasDeliverable = new AtomicBoolean(false);

        deliverables.stream().filter(notBooked.and(loadable))
                .min(Comparator.comparingInt(deliverable -> computeTotalTravelDuration(train, deliverable)))
                .ifPresent(deliverable -> {
                    hasDeliverable.set(true);
                    deliverable.setStatus(DeliveryStatus.BOOKED);
                    train.getDeliverables().add(deliverable);
                });

        return hasDeliverable.get();
    }

    int computeTotalTravelDuration(Train train, Deliverable deliverable) {
        int pickupDuration = train.getCurrentStation().equals(deliverable.getSendFrom()) ? 0 :
                new Navigate().apply(train.getCurrentStation(), deliverable.getSendFrom())
                        .stream()
                        .filter(station -> station.getDuration() != null)
                        .map(station -> station.getDuration().intValue())
                        .reduce(0, Integer::sum);

        int deliveryDuration = new Navigate().apply(deliverable.getSendFrom(), deliverable.getSendTo())
                .stream()
                .filter(station -> station.getDuration() != null)
                .map(station -> station.getDuration().intValue())
                .reduce(0, Integer::sum);

        return pickupDuration + deliveryDuration;
    }

}
