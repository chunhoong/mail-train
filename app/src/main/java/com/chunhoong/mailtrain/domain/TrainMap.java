package com.chunhoong.mailtrain.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrainMap {

    private static final TrainMap instance = new TrainMap();
    private final List<Station> stations = new ArrayList<>();
    private final List<Route> routes = new ArrayList<>();
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

        adjacentStations.get(station).add(adjacentStation.withDuration(route.getDuration()));
        adjacentStations.get(adjacentStation).add(station.withDuration(route.getDuration()));

        routes.add(route);
    }

    public List<Station> getStations() {
        return stations;
    }

    public List<Route> getRoutes() {
        return routes;
    }

    public Map<Station, List<Station>> getAdjacentStations() {
        return adjacentStations;
    }

}
