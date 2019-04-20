package br.com.bexs.domain.service;

import br.com.bexs.api.ApplicationMessages;
import br.com.bexs.api.exception.EntityNotFoundException;
import br.com.bexs.api.exception.ResourceNotModifiedException;
import br.com.bexs.domain.RouteDTO;
import br.com.bexs.domain.ShortRouteDTO;
import br.com.bexs.domain.service.sdk.Location;
import br.com.bexs.domain.service.sdk.LocationGraph;
import br.com.bexs.domain.service.utils.DijkstraUtils;
import br.com.bexs.domain.service.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RouteServiceFacade {

    @Autowired
    private ApplicationMessages messages;

    private List<RouteDTO> routeCsvs;

    private LocationGraph locationGraph;

    private File inputFile;

    private String fileLocation;

    public List<RouteDTO> findAllRoutes() {
        if (this.inputFile == null || this.routeCsvs == null) {
            throw new EntityNotFoundException(messages.getNoRoutesFound());
        }
        return this.routeCsvs;
    }

    public Optional<ShortRouteDTO> findShortestRoute(String origin, String destination) {

        Optional<Location> originConditional = this.locationGraph.getNodes().stream().filter(location -> location.getName().equals(origin)).findFirst();

        if (!originConditional.isPresent()) {
            throw new EntityNotFoundException(messages.getOriginNotFound());
        }

        try {
            LocationGraph shortestRoute = DijkstraUtils.calculateShortestPathFromSource(locationGraph, originConditional.get());
            Location destinationShorted = shortestRoute.getNodes().stream().filter(node -> node.getName().equals(destination)).findFirst().get();
            String fullShortRoute = destinationShorted.getShortestPath().stream().map(Location::getName).collect(Collectors.joining(" - "));
            fullShortRoute = fullShortRoute.concat(" - ").concat(destination);
            Integer distanceBetween = destinationShorted.getDistance();
            return Optional.of(new ShortRouteDTO(origin, destination, fullShortRoute, distanceBetween));
        } catch (Exception exception) {
            throw new EntityNotFoundException(messages.getRouteNotDefinedForDestination());
        }

    }

    public RouteDTO addRoute(String origin, String destination, Integer price) {
        if (this.inputFile == null) {
            throw new ResourceNotModifiedException(messages.getInputFileNotFound());
        }

        try {
            FileWriter pw = new FileWriter(this.inputFile, true);
            pw.append("\r\n").append(origin).append(",").append(destination).append(",").append(String.valueOf(price));
            pw.flush();
            pw.close();
            this.initRoutes();
            return new RouteDTO(origin, destination, price);
        } catch (IOException e) {
            throw new ResourceNotModifiedException(messages.getErrorWritingFile());
        }

    }

    public String getFileLocation() {
        return this.fileLocation;
    }

    public void setFileLocation(String fileLocation) {
        this.fileLocation = fileLocation;
    }

    public void initRoutes() throws IOException {
        this.inputFile = FileUtils.getInputFile(getFileLocation());
        this.routeCsvs = FileUtils.processInputFile(this.inputFile);

        List<Location> locations = getLocations(this.routeCsvs);
        locations.forEach(location -> {
            this.routeCsvs.forEach(routeDTO -> {
                if (location.getName().equals(routeDTO.getOrigin())) {
                    Location destination = locations.stream().filter(location1 -> location1.getName().equals(routeDTO.getDestination())).findFirst().get();
                    location.addDestination(destination, routeDTO.getPrice());
                }
            });
        });

        this.locationGraph = new LocationGraph();
        locations.forEach(this.locationGraph::addNode);

    }

    private List<Location> getLocations(List<RouteDTO> routeCsvs) {
        List<Location> locations = new ArrayList<>();
        routeCsvs.forEach(routeDTO -> {

            Optional<Location> findedOrigin = locations.stream().filter(location -> location.getName().equals(routeDTO.getOrigin())).findFirst();
            Optional<Location> findedDestination = locations.stream().filter(location -> location.getName().equals(routeDTO.getDestination())).findFirst();

            if (!findedOrigin.isPresent()) {
                Location location = new Location(routeDTO.getOrigin());
                locations.add(location);
            }
            if (!findedDestination.isPresent()) {
                Location location = new Location(routeDTO.getDestination());
                locations.add(location);
            }

        });
        return locations;
    }

}
