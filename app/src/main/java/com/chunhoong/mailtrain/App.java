package com.chunhoong.mailtrain;

import com.chunhoong.mailtrain.domain.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.function.Predicate;

public class App {

    private static final Scanner scanner = new Scanner(System.in);
    private List<Station> stations = new ArrayList<>();
    private List<Route> routes = new ArrayList<>();
    private List<Deliverable> deliverables = Collections.synchronizedList(new ArrayList<>());
    private List<Train> trains = new ArrayList<>();

    public static void main(String[] args) {
        App instance = new App();
        instance.setupStations();
        instance.setupRoutes();
        instance.prepareDeliverables();
        instance.deployTrain();
        instance.run();
    }

    void setupStations() {
        System.out.print("Number of stations: ");
        int numberOfStation = scanner.nextInt();
        while (stations.size() < numberOfStation) {
            System.out.print("Add a station [<stationName>]: ");
            String input = scanner.next();
            stations.add(Station.fromInput(input));
        }
    }

    void setupRoutes() {
        System.out.print("Number of routes: ");
        int numberOfRoutes = scanner.nextInt();
        while (routes.size() < numberOfRoutes) {
            System.out.print("Add a route [<routeName>,<stationName>,<anotherStationName>,<duration>]: ");
            String input = scanner.next();
            routes.add(Route.fromInput(input));
        }
    }

    void prepareDeliverables() {
        System.out.print("Number of deliveries: ");
        int numberOfDeliveries = scanner.nextInt();
        while (deliverables.size() < numberOfDeliveries) {
            System.out.print("Add a delivery [<packageName>,<sendFrom>,<sendTo>,<weight>]: ");
            String input = scanner.next();
            deliverables.add(Deliverable.fromInput(input));
        }
    }

    void deployTrain() {
        System.out.print("Number of trains: ");
        int numberOfTrain = scanner.nextInt();
        while (trains.size() < numberOfTrain) {
            System.out.print("Add a train [<trainName>,<initialStation>,<capacity>]: ");
            String input = scanner.next();
            trains.add(Train.fromInput(input));
        }
    }

    void run() {

    }

}
