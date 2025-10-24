package com.company.algorithms;

import com.company.metrics.Metrics;
import com.company.models.EdgeInput;
import com.company.models.EdgeResult;
import com.company.models.MSTResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KruskalAlgorithm {

    private static class UnionFind {
        private Map<String, String> parent;
        private Map<String, Integer> rank;
        private Metrics metrics;

        public UnionFind(List<String> nodes, Metrics metrics) {
            this.parent = new HashMap<>();
            this.rank = new HashMap<>();
            this.metrics = metrics;

            for(String node : nodes) {
                parent.put(node, node);
                rank.put(node, 0);
            }
        }

        public String find(String node) {
            metrics.incrementOperationsCount();
            if(!parent.get(node).equals(node)) {
                parent.put(node, find(parent.get(node)));
            }
            return parent.get(node);
        }

        public boolean union(String node1, String node2) {
            String root1 = find(node1);
            String root2 = find(node2);

            metrics.incrementOperationsCount();

            if(root1.equals(root2)) {
                return false;
            }

            if (rank.get(root1) < rank.get(root2)) {
                parent.put(root1, root2);
            } else if (rank.get(root1) > rank.get(root2)) {
                parent.put(root2, root1);
            } else {
                parent.put(root2, root1);
                rank.put(root1, rank.get(root1) + 1);
            }

            return true;
        }
    }

    public static MSTResult findMST(List<String> nodes, List<EdgeInput> edges) {
        Metrics metrics = new Metrics();
        long startTime = System.nanoTime();

        List<EdgeInput> sortedEdges = new ArrayList<>(edges);
        sortedEdges.sort((a, b) -> {
            metrics.incrementOperationsCount();
            return Integer.compare(a.getWeight(), b.getWeight());
        });

        UnionFind uf = new UnionFind(nodes, metrics);
        List<EdgeResult> mstEdges = new ArrayList<>();
        int totalCost = 0;

        for(EdgeInput edge : sortedEdges) {
            metrics.incrementOperationsCount();

            if(uf.union(edge.getFrom(), edge.getTo())) {
                mstEdges.add(new EdgeResult(edge.getFrom(), edge.getTo(), edge.getWeight()));
                totalCost += edge.getWeight();

                if (mstEdges.size() == nodes.size() - 1) {
                    break;
                }
            }
        }

        long endTime = System.nanoTime();
        metrics.setExecutionTimeMs((endTime - startTime) / 1_000_000);

        return new MSTResult(mstEdges, totalCost, metrics);
    }
}
