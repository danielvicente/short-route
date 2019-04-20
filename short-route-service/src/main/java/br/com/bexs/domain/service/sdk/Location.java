package br.com.bexs.domain.service.sdk;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Location {

    Map<Location, Integer> adjacentNodes = new HashMap<>();
    private String name;
    private List<Location> shortestPath = new LinkedList<>();
    private Integer distance = Integer.MAX_VALUE;

    public Location(String origin) {
        this.name = origin;
    }

    public void addDestination(Location destination, int distance) {
        adjacentNodes.put(destination, distance);
    }

}
