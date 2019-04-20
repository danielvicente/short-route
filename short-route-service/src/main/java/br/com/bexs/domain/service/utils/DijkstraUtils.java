package br.com.bexs.domain.service.utils;

import br.com.bexs.domain.service.sdk.Location;
import br.com.bexs.domain.service.sdk.LocationGraph;
import lombok.experimental.UtilityClass;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

@UtilityClass
public class DijkstraUtils {

    public LocationGraph calculateShortestPathFromSource(LocationGraph graph, Location source) {
        source.setDistance(0);

        Set<Location> settledNodes = new HashSet<>();
        Set<Location> unsettledNodes = new HashSet<>();

        unsettledNodes.add(source);

        while (unsettledNodes.size() != 0) {
            Location currentNode = getLowestDistanceNode(unsettledNodes);
            unsettledNodes.remove(currentNode);
            for (Map.Entry<Location, Integer> adjacencyPair : currentNode.getAdjacentNodes().entrySet()) {
                Location adjacentNode = adjacencyPair.getKey();
                Integer edgeWeight = adjacencyPair.getValue();
                if (!settledNodes.contains(adjacentNode)) {
                    calculateMinimumDistance(adjacentNode, edgeWeight, currentNode);
                    unsettledNodes.add(adjacentNode);
                }
            }
            settledNodes.add(currentNode);
        }
        return graph;
    }

    private static Location getLowestDistanceNode(Set<Location> unsettledNodes) {
        Location lowestDistanceNode = null;
        int lowestDistance = Integer.MAX_VALUE;
        for (Location node : unsettledNodes) {
            int nodeDistance = node.getDistance();
            if (nodeDistance < lowestDistance) {
                lowestDistance = nodeDistance;
                lowestDistanceNode = node;
            }
        }
        return lowestDistanceNode;
    }

    private static void calculateMinimumDistance(Location evaluationNode,
                                                 Integer edgeWeigh, Location sourceNode) {
        Integer sourceDistance = sourceNode.getDistance();
        if (sourceDistance + edgeWeigh < evaluationNode.getDistance()) {
            evaluationNode.setDistance(sourceDistance + edgeWeigh);
            LinkedList<Location> shortestPath = new LinkedList<>(sourceNode.getShortestPath());
            shortestPath.add(sourceNode);
            evaluationNode.setShortestPath(shortestPath);
        }
    }
}
