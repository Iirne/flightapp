package dk.cphbusiness.flightdemo;

import dk.cphbusiness.flightdemo.dtos.FlightDTO;
import dk.cphbusiness.flightdemo.dtos.FlightInfoDTO;

import java.io.IOException;
import java.util.List;

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
                    filter(flight -> flight != null).
                    filter(flight -> flight.getAirline().getName() != null).
                    filter(flight -> flight.getAirline().
                            getName().
                            equals(airline)).
                    toList();
            return ((double)getFlightInfoDetails(flightList).stream().
                    mapToLong(flightInfo -> flightInfo.
                            getDuration().
                            toSeconds()).
                    sum());

        } catch (IOException e) {
            e.printStackTrace();

        }
        return 0.0;
    }
}
