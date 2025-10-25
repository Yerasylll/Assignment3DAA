package com.company.graphGenerator;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.Array;
import java.util.*;

public class GraphGenerator {

    private static class Edge {
        private String from;
        private String to;
        private int weight;

        Edge(String from, String to, int weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }

        public String getFrom() { return from; }
        public String getTo() { return to; }
        public int getWeight() { return weight; }
    }

    private static class Graph {
        private int id;
        private String description;
        private List<String> nodes;
        private List<Edge> edges;

        Graph(int id, String description, List<String> nodes, List<Edge> edges) {
            this.id = id;
            this.description = description;
            this.nodes = nodes;
            this.edges = edges;
        }

        public int getId() { return id; }
        public String getDescription() { return description; }
        public List<String> getNodes() { return nodes; }
        public List<Edge> getEdges() { return edges; }
    }

    public static Graph generateGraph(int graphId, int numNodes, double density, long seed) {
        Random random = new Random(seed);

        // Generate node names
        List<String> nodes = new ArrayList<>();
        for (int i = 0; i < numNodes; i++) {
            nodes.add("N" + i);
        }

        List<Edge> edges = new ArrayList<>();
        Set<String> edgeSet = new HashSet<>();

        // Create a spanning tree to ensure connectivity
        List<Integer> connected = new ArrayList<>();
        List<Integer> unconnected = new ArrayList<>();

        connected.add(0);
        for (int i = 1; i < numNodes; i++) {
            unconnected.add(i);
        }
        Collections.shuffle(unconnected, random);

        // Connect all nodes
        for (int node : unconnected) {
            int parent = connected.get(random.nextInt(connected.size()));
            int weight = random.nextInt(100) + 1;

            String fromNode = nodes.get(parent);
            String toNode = nodes.get(node);

            // Ensure consistent edge representation
            if (fromNode.compareTo(toNode) > 0) {
                String temp = fromNode;
                fromNode = toNode;
                toNode = temp;
            }

            edges.add(new Edge(fromNode, toNode, weight));
            edgeSet.add(fromNode + "-" + toNode);
            connected.add(node);
        }

        // Add additional edges based on density
        int maxEdges = (numNodes * (numNodes - 1)) / 2;
        int targetEdges = Math.min(maxEdges, (int)(maxEdges * density));

        int attempts = 0;
        int maxAttempts = targetEdges * 10;

        while (edges.size() < targetEdges && attempts < maxAttempts) {
            int i = random.nextInt(numNodes);
            int j = random.nextInt(numNodes);

            if (i == j) {
                attempts++;
                continue;
            }

            String fromNode = nodes.get(Math.min(i, j));
            String toNode = nodes.get(Math.max(i, j));
            String edgeKey = fromNode + "-" + toNode;

            if (!edgeSet.contains(edgeKey)) {
                int weight = random.nextInt(100) + 1;
                edges.add(new Edge(fromNode, toNode, weight));
                edgeSet.add(edgeKey);
            }

            attempts++;
        }

        String description = String.format("Graph with %d nodes, %d edges (density: %.2f)",
                numNodes, edges.size(), density);

        return new Graph(graphId, description, nodes, edges);
    }

    public static void generateAllVariants(String filename) throws IOException {
        List<Graph> graphs = new ArrayList<>();
        int graphId = 1;

        // Small variants (5 graphs, up to 30 nodes)
        System.out.println("Generating small variants (5-30 nodes)...");
        int[] smallSizes = {5, 10, 15, 20, 30};
        double[] smallDensities = {0.3, 0.5, 0.4, 0.6, 0.5};

        for (int i = 0; i < smallSizes.length; i++) {
            Graph graph = generateGraph(graphId, smallSizes[i], smallDensities[i], graphId * 100L);
            graphs.add(graph);
            System.out.printf("  Graph %d: %d nodes, %d edges%n",
                    graphId, graph.getNodes().size(), graph.getEdges().size());
            graphId++;
        }

        // Medium variants (10 graphs, up to 300 nodes)
        System.out.println("\nGenerating medium variants (50-300 nodes)...");
        int[] mediumSizes = {50, 75, 100, 125, 150, 175, 200, 225, 275, 300};
        double[] mediumDensities = {0.2, 0.3, 0.25, 0.35, 0.3, 0.4, 0.35, 0.3, 0.25, 0.4};

        for (int i = 0; i < mediumSizes.length; i++) {
            Graph graph = generateGraph(graphId, mediumSizes[i], mediumDensities[i], graphId * 100L);
            graphs.add(graph);
            System.out.printf("  Graph %d: %d nodes, %d edges%n",
                    graphId, graph.getNodes().size(), graph.getEdges().size());
            graphId++;
        }

        // Large variants (10 graphs, up to 1000 nodes)
        System.out.println("\nGenerating large variants (350-1000 nodes)...");
        int[] largeSizes = {350, 400, 450, 500, 600, 700, 800, 900, 950, 1000};
        double[] largeDensities = {0.15, 0.2, 0.18, 0.22, 0.2, 0.25, 0.2, 0.18, 0.15, 0.3};

        for (int i = 0; i < largeSizes.length; i++) {
            Graph graph = generateGraph(graphId, largeSizes[i], largeDensities[i], graphId * 100L);
            graphs.add(graph);
            System.out.printf("  Graph %d: %d nodes, %d edges%n",
                    graphId, graph.getNodes().size(), graph.getEdges().size());
            graphId++;
        }

        // Extra large variants (5 graphs, up to 3000 nodes)
        System.out.println("\nGenerating extra large variants (1500-3000 nodes)...");
        int[] extraSizes = {1500, 2000, 2250, 2750, 3000};
        double[] extraDensities = {0.1, 0.12, 0.15, 0.1, 0.2};

        for (int i = 0; i < extraSizes.length; i++) {
            Graph graph = generateGraph(graphId, extraSizes[i], extraDensities[i], graphId * 100L);
            graphs.add(graph);
            System.out.printf("  Graph %d: %d nodes, %d edges%n",
                    graphId, graph.getNodes().size(), graph.getEdges().size());
            graphId++;
        }

        // Write to JSON file
        writeToJSON(graphs, filename);
        System.out.println("\nSuccessfully generated " + graphs.size() + " graphs!");
        System.out.println("Output file: " + filename);
    }

    public static void writeToJSON(List<Graph> graphs, String filename) throws IOException {
        FileWriter writer = new FileWriter(filename);
        writer.write("{\n");
        writer.write("  \"graphs\": [\n");

        for (int i = 0; i < graphs.size(); i++) {
            Graph graph = graphs.get(i);
            writer.write("    {\n");
            writer.write("      \"id\": " + graph.getId() + ",\n");
            writer.write("      \"description\": \"" + graph.getDescription() + "\",\n");

            // Write nodes
            writer.write("      \"nodes\": [");
            for (int j = 0; j < graph.getNodes().size(); j++) {
                writer.write("\"" + graph.getNodes().get(j) + "\"");
                if (j < graph.getNodes().size() - 1) writer.write(", ");
            }
            writer.write("],\n");

            // Write edges
            writer.write("      \"edges\": [\n");
            for (int j = 0; j < graph.getEdges().size(); j++) {
                Edge edge = graph.getEdges().get(j);
                writer.write("        {\"from\": \"" + edge.getFrom() + "\", " +
                        "\"to\": \"" + edge.getTo() + "\", " +
                        "\"weight\": " + edge.getWeight() + "}");
                if (j < graph.getEdges().size() - 1) writer.write(",");
                writer.write("\n");
            }
            writer.write("      ]\n");
            writer.write("    }");
            if (i < graphs.size() - 1) writer.write(",");
            writer.write("\n");
        }

        writer.write("  ]\n");
        writer.write("}\n");
        writer.close();
    }

    public static void main(String[] args) {
        try {
            generateAllVariants("assign_3_input.json");
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}