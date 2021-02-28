package com.chunhoong.mailtrain;

import com.chunhoong.mailtrain.domain.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Supplier;

public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);
    private static final Scanner scanner = new Scanner(System.in);
    private final TrainMap trainMap = TrainMap.getInstance();
    private final List<Deliverable> deliverables = Collections.synchronizedList(new ArrayList<>());
    private final List<Train> trains = new ArrayList<>();

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
        while (trainMap.getStations().size() < numberOfStation) {
            System.out.print("Add a station [<stationName>]: ");
            String input = scanner.next();
            trainMap.addStation(Station.fromInput(input));
        }
    }

    void setupRoutes() {
        System.out.print("Number of routes: ");
        int numberOfRoutes = scanner.nextInt();
        while (trainMap.getRoutes().size() < numberOfRoutes) {
            System.out.print("Add a route [<routeName>,<stationName>,<anotherStationName>,<duration>]: ");
            String input = scanner.next();
            trainMap.addRoute(Route.fromInput(input));
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
        Supplier<String> randomStation = () -> {
            Random r = new Random();
            return String.valueOf((char)(r.nextInt(5) + 'A'));
        };

        trains.parallelStream().forEach(train -> {
            Station origin = new Station(randomStation.get());
            Station destination = new Station(randomStation.get());
            logger.info("Train {} from {} to {}", train.getName(), origin.getName(), destination.getName());
            train.travel(origin, destination);
        });
    }

}
