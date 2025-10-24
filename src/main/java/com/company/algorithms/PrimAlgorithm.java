package com.company.algorithms;

import com.company.metrics.Metrics;
import com.company.models.EdgeInput;
import com.company.models.EdgeResult;
import com.company.models.MSTResult;

import java.util.*;

public class PrimAlgorithm {
    public static MSTResult findMST(List<String> nodes, List<EdgeInput> edges) {
        Metrics metrics = new Metrics();
        long startTime = System.nanoTime();

        // Adjacency List
        Map<String, List<EdgeInput>> adjacencyList = new HashMap<>();
        for(String node : nodes) {
            adjacencyList.put(node, new ArrayList<>());
        }

        for(EdgeInput edge : edges) {
            adjacencyList.get(edge.getFrom()).add(edge);
            adjacencyList.get(edge.getTo()).add(new EdgeInput(edge.getTo(), edge.getFrom(), edge.getWeight()));
            metrics.incrementOperationsCount();
        }

        // Prim's algorithm
        Set<String> visited = new HashSet<>();
        PriorityQueue<EdgeInput> pq = new PriorityQueue<>((a, b) -> {
            metrics.incrementOperationsCount();
            return Integer.compare(a.getWeight(), b.getWeight());
        });

        List<EdgeResult> mstEdges = new ArrayList<>();
        int totalCost = 0;

        String startNode = nodes.get(0);
        visited.add(startNode);
        pq.addAll(adjacencyList.get(startNode));

        while(!pq.isEmpty() && visited.size() < nodes.size()) {
            EdgeInput edge = pq.poll();
            metrics.incrementOperationsCount();

            String nextNode = visited.contains(edge.getFrom()) ? edge.getTo() : edge.getFrom();

            if(visited.contains(nextNode)) {
                continue;
            }

            visited.add(nextNode);
            mstEdges.add(new EdgeResult(edge.getFrom(), edge.getTo(), edge.getWeight()));
            totalCost += edge.getWeight();

            for(EdgeInput neighbor : adjacencyList.get(nextNode)) {
                if(!visited.contains(neighbor.getTo())) {
                    pq.add(neighbor);
                }
                metrics.incrementOperationsCount();
            }
        }

        long endTime = System.nanoTime();
        metrics.setExecutionTimeMs((endTime - startTime) / 1_000_000);

        return new MSTResult(mstEdges, totalCost, metrics);
    }
}
