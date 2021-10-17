package algorithms;

import simulator.NeighborInfo;

import java.util.*;
import java.util.stream.Collectors;

public class MinimumCostAlgorithm extends Algorithm {

    // IMPORTANT: Use this random number generator.
    Random rand = new Random(6391238);

    // IMPORTANT: You can maintain a state, e.g., a set of neighbors.
    Set<String> exclusionSet = new HashSet<>();

    @Override
    public List<NeighborInfo> selectNeighbors(String origin, String destination, String previousHop,
                                              List<NeighborInfo> neighbors) {
        // Your code goes here.
        List<NeighborInfo> chosen = neighbors.stream()
                // Make sure that we do not route back to the previous hop.
                .filter(n -> !n.address.equals(previousHop) && !exclusionSet.contains(n.address))
                .collect(Collectors.toList());
        NeighborInfo minNeighbor;
        // if every neighbor is in the exclusion set, pick a random neighbor
        if (chosen.isEmpty()) {
            // if all neighbors are in the exclusion set, select randomly
            int randomInd = rand.nextInt(neighbors.size());
            minNeighbor = neighbors.get(randomInd);
        } else {
            // chose the minimum neighbor according to their costs
            minNeighbor = chosen.stream().min(Comparator.comparingInt(n -> n.cost)).get();
        }
        // empty chosen array list
        chosen.clear();
        chosen.add(minNeighbor);
        // add the next and previous hops to the exclusion set
        exclusionSet.add(minNeighbor.address);
        exclusionSet.add(previousHop);
        ///////////////////////
        return chosen;
    }

    @Override
    public Algorithm copy() {
        return new MinimumCostAlgorithm();
    }

    @Override
    public String getName() {
        return "MinimumCost";
    }
}
