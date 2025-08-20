package dk.cphbusiness.flightdemo;

import dk.cphbusiness.flightdemo.dtos.AirlineDTO;
import dk.cphbusiness.flightdemo.dtos.FlightDTO;
import dk.cphbusiness.flightdemo.dtos.FlightInfoDTO;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static dk.cphbusiness.flightdemo.FlightReader.getFlightInfoDetails;
import static dk.cphbusiness.flightdemo.FlightReader.getFlightsFromFile;

public class services {

    public static void printAllData(){
        //get everything and print all
        try {
            List<FlightDTO> flightList = getFlightsFromFile("flights.json");
            List<FlightInfoDTO> flightInfoDTOList = getFlightInfoDetails(flightList);
            flightInfoDTOList.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static double sumOfFlightHours(String airline){
        try {
            List<FlightDTO> flightList = getFlightsFromFile("flights.json").stream().
                    filter(flight -> flight.getAirline().getName() != null).
                    filter(flight -> flight.getAirline().
                            getName().
                            equals(airline)).
                    toList();
            return ((double)getFlightInfoDetails(flightList).stream().
                    mapToLong(flightInfo -> flightInfo.
                            getDuration().
                            toSeconds()).
                    sum())/3600;

        } catch (IOException e) {
            e.printStackTrace();

        }
        return 0.0;
    }

    public static double averageOfFlightHours(String airline){
        try {
            List<FlightDTO> flightList = getFlightsFromFile("flights.json").stream().
                    filter(flight -> flight.getAirline().getName() != null).
                    filter(flight -> flight.getAirline().
                            getName().
                            equals(airline)).
                    toList();
            return (getFlightInfoDetails(flightList).stream().
                    mapToLong(flightInfo -> flightInfo.
                            getDuration().
                            toSeconds()).
                    average().orElse(0.0)/3600);

        } catch (IOException e) {
            e.printStackTrace();

        }
        return 0.0;
    }

    //SLOOOWWWW
    public static Map<AirlineDTO, Double> averageFlightHoursAll(){
        HashMap<AirlineDTO,Double> map = new HashMap<>();
        try {

            getFlightsFromFile("flights.json").stream().
                    map(FlightDTO::getAirline).distinct().
                    filter(flightDTO -> flightDTO.getName() != null).
                    forEach(airlineDTO -> map.put(airlineDTO,services.averageOfFlightHours(airlineDTO.getName())));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }


    public static List<FlightDTO> flightsBetweenTwoAirports(String airport1, String airport2){
        try {
            return getFlightsFromFile("flights.json").stream().
                    filter(flight -> flight.getDeparture() != null).
                    filter(flight -> flight.getArrival() != null).
                    filter(flight -> flight.getDeparture().getAirport() != null).
                    filter(flight -> flight.getArrival().getAirport() != null).
                    filter(flight -> flight.getDeparture().getAirport().contains(airport1) || flight.getArrival().getAirport().equals(airport1)).
                    filter(flight -> flight.getDeparture().getAirport().contains(airport2) || flight.getArrival().getAirport().equals(airport2)).
                    toList();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static List<FlightDTO> sortedByArrival(){
        try {
            return getFlightsFromFile("flights.json").stream().
                    filter(flight -> flight.getArrival().getScheduled() != null).
                    sorted((a, b) -> a.getArrival().getScheduled().compareTo(b.getArrival().getScheduled())).toList();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static List<FlightDTO> sortedByDuration(){
        try {
            return getFlightsFromFile("flights.json").stream().
                    filter(flight -> flight.getArrival().getScheduled() != null).
                    filter(flight -> flight.getDeparture().getScheduled() != null).
                    sorted((a, b) -> Duration.between(a.getArrival().getScheduled(), a.getDeparture().getScheduled()).compareTo(Duration.between(b.getArrival().getScheduled(), b.getDeparture().getScheduled()))).toList();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static List<FlightDTO> flightsBefore(LocalDateTime time){
        try {
            return getFlightsFromFile("flights.json").stream().
                    filter(flight -> flight.getDeparture().getScheduled() != null).
                    filter(flightDTO -> flightDTO.getDeparture().getScheduled().isBefore(time)).toList();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
