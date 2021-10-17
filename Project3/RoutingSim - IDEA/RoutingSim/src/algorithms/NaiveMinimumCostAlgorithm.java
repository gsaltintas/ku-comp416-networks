package algorithms;

import simulator.NeighborInfo;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.min;

public class NaiveMinimumCostAlgorithm extends Algorithm {

    @Override
    public List<NeighborInfo> selectNeighbors(String origin, String destination, String previousHop,
                                              List<NeighborInfo> neighbors) {
        // Your code goes here.
        List<NeighborInfo> chosen = neighbors.stream()
                // Make sure that we do not route back to the previous hop.
                .filter(n -> !n.address.equals(previousHop))
                .collect(Collectors.toList());
        NeighborInfo minNeighbor;
        // if the chosen list is non empty, pick minimum element
        if (!chosen.isEmpty()) {
            minNeighbor = chosen.stream().min(Comparator.comparingInt(n -> n.cost)).get();
            chosen.clear();
            chosen.add(minNeighbor);
        }
        ///////////////////////
        return chosen;
    }

    @Override
    public Algorithm copy() {
        return new NaiveMinimumCostAlgorithm();
    }

    @Override
    public String getName() {
        return "NaiveMinimumCost";
    }
}
