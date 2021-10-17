package algorithms;

import simulator.NeighborInfo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implements a flooding routing algorithm that converges.
 */
public class FloodingAlgorithm extends Algorithm {

    // IMPORTANT: You can maintain a state, e.g., a flag.
    private boolean routed = false;

    @Override
    public List<NeighborInfo> selectNeighbors(String origin, String destination, String previousHop,
                                              List<NeighborInfo> neighbors) {
        // Your code goes here.
        List<NeighborInfo> chosen = new ArrayList<>();
        if (!routed) {
            chosen = neighbors.stream()
                    // Make sure that we do not route back to the previous hop.
                    .filter(n -> !n.address.equals(previousHop))
                    .collect(Collectors.toList());
            // once the algorithm is called on the node, set router true to avoid further executions
            routed = true;
        }
        ///////////////////////
        return chosen;
    }

    @Override
    public Algorithm copy() {
        return new FloodingAlgorithm();
    }

    @Override
    public String getName() {
        return "Flooding";
    }
}
