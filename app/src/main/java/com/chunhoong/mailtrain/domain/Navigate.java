package com.chunhoong.mailtrain.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;

public class Navigate implements BiFunction<Station, Station, List<Station>> {

    private List<Station> route = new ArrayList<>();
    private final TrainMap trainMap = TrainMap.getInstance();
    private final Set<Station> isVisited = new HashSet<>();

    @Override
    public List<Station> apply(Station origin, Station destination) {
        List<Station> searchRoute = new ArrayList<>();
        exploreRoute(origin, destination, searchRoute);
        return route;
    }

    private void exploreRoute(Station origin, Station destination, List<Station> searchRoute) {
        if (!searchRoute.contains(origin)) {
            searchRoute.add(origin);
        }

        if (origin.equals(destination)) {
            route = new ArrayList<>(searchRoute);
        }

        isVisited.add(origin);

        if (!isVisited.containsAll(trainMap.getAdjacentStations().get(origin))) {
            for (Station station : trainMap.getAdjacentStations().get(origin)) {
                if (!isVisited.contains(station)) {
                    searchRoute.add(station);
                    exploreRoute(station, destination, searchRoute);
                    searchRoute.remove(station);
                }
            }
        }

        isVisited.remove(origin);
    }

}
