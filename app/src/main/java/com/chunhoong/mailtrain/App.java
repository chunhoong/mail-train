package com.chunhoong.mailtrain;

import com.chunhoong.mailtrain.domain.*;
import com.chunhoong.mailtrain.service.DeliveryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);
    private static final Scanner scanner = new Scanner(System.in);
    private final TrainMap trainMap = TrainMap.getInstance();
    private final DeliveryService deliveryService = DeliveryService.getInstance();
    private final List<Train> trains = new ArrayList<>();

    public static void main(String[] args) throws ExecutionException, InterruptedException {
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
        int counter = 0;
        while (counter < numberOfRoutes) {
            System.out.print("Add a route [<routeName>,<stationName>,<anotherStationName>,<duration>]: ");
            String input = scanner.next();
            trainMap.addRoute(Route.fromInput(input));
            counter++;
        }
    }

    void prepareDeliverables() {
        System.out.print("Number of deliveries: ");
        int numberOfDeliveries = scanner.nextInt();
        while (deliveryService.getDeliverables().size() < numberOfDeliveries) {
            System.out.print("Add a delivery [<packageName>,<sendFrom>,<sendTo>,<weight>]: ");
            String input = scanner.next();
            deliveryService.addDeliverable(Deliverable.fromInput(input));
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

    void run() throws ExecutionException, InterruptedException {
        ForkJoinPool deliveryProcessPool = new ForkJoinPool(trains.size());
        deliveryProcessPool.submit(() -> trains.parallelStream().forEach(train -> {
            while (deliveryService.hasDeliverable(train)) {
                logger.info(
                        "Train {} delivers {} from {} to {}",
                        train.getName(),
                        train.getDeliverables().get(0).getName(),
                        train.getDeliverables().get(0).getSendFrom().getName(),
                        train.getDeliverables().get(0).getSendTo().getName()
                );
                train.performDelivery();
            }
        })).get();
    }

}
