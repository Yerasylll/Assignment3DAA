package com.company.models.graphData;

import com.company.models.EdgeInput;

import java.util.List;

public class GraphData {
    private int id;
    private String description;
    private List<String> nodes;
    private List<EdgeInput> edges;

    public GraphData(int id, String description, List<String> nodes, List<EdgeInput> edges) {
        this.id = id;
        this.description = description;
        this.nodes = nodes;
        this.edges = edges;
    }

    public int getId() {return id;}

    public String getDescription() {
        return description;
    }

    public List<String> getNodes() {
        return nodes;
    }

    public List<EdgeInput> getEdges() {
        return edges;
    }
}
