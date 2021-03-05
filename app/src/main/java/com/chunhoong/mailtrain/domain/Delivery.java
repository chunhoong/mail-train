package com.chunhoong.mailtrain.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigInteger;
import java.util.Objects;

@Data
@RequiredArgsConstructor
public class Delivery {

    private final Deliverable deliverable;
    private final Train train;

    public BigInteger getPickupDuration() {
        return train.getCurrentStation().equals(deliverable.getSendFrom()) ? BigInteger.ZERO :
                new Navigation().apply(train.getCurrentStation(), deliverable.getSendFrom())
                        .stream()
                        .map(Station::getDuration)
                        .filter(Objects::nonNull)
                        .reduce(BigInteger.ZERO, BigInteger::add);
    }

    public BigInteger getDeliveryDuration() {
        return new Navigation().apply(deliverable.getSendFrom(), deliverable.getSendTo())
                .stream()
                .map(Station::getDuration)
                .filter(Objects::nonNull)
                .reduce(BigInteger.ZERO, BigInteger::add);
    }

    public BigInteger getTotalTravelDuration() {
        return getPickupDuration().add(getDeliveryDuration());
    }

}
