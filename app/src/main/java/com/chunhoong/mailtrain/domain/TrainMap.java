package com.chunhoong.mailtrain.domain;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrainMap {

    private static final TrainMap instance = new TrainMap();

    @Getter
    private final List<Station> stations = new ArrayList<>();

    @Getter
    private final Map<Station, List<Station>> adjacentStations = new HashMap<>();

    private TrainMap() {
    }

    public static TrainMap getInstance() {
        return instance;
    }

    public void addStation(Station station) {
        stations.add(station);
        adjacentStations.put(station, new ArrayList<>());
    }

    public void addRoute(Route route) {
        Station station = stations.stream()
                .filter(s -> s.getName().equals(route.getStationName()))
                .findFirst()
                .orElseThrow();

        Station adjacentStation = stations.stream()
                .filter(s -> s.getName().equals(route.getAdjacentStationName()))
                .findFirst()
                .orElseThrow();

        adjacentStations.get(station).add(adjacentStation
                .withDuration(route.getDuration())
                .withRouteName(route.getName())
        );
        adjacentStations.get(adjacentStation).add(station
                .withDuration(route.getDuration())
                .withRouteName(route.getName())
        );
    }

}
