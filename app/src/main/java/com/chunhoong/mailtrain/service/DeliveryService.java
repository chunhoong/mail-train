package com.chunhoong.mailtrain.service;

import com.chunhoong.mailtrain.domain.Deliverable;
import com.chunhoong.mailtrain.domain.DeliveryStatus;
import com.chunhoong.mailtrain.domain.Train;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;

public class DeliveryService {

    private static final DeliveryService instance = new DeliveryService();

    @Getter
    private final List<Deliverable> deliverables = new ArrayList<>();

    private DeliveryService() {}

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
                .findAny()
                .ifPresent(deliverable -> {
                    hasDeliverable.set(true);
                    deliverable.setStatus(DeliveryStatus.BOOKED);
                    train.getDeliverables().add(deliverable);
                });

        return hasDeliverable.get();
    }

}
