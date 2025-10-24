package com.company.models.graphData;

import com.company.models.MSTResult;

public class ResultData {
    private int graphId;
    private int vertices;
    private int inputEdges;
    private MSTResult primResult;
    private MSTResult kruskalResult;

    public ResultData(int graphId, int vertices, int inputEdges, MSTResult primResult, MSTResult kruskalResult) {
        this.graphId = graphId;
        this.vertices = vertices;
        this.inputEdges = inputEdges;
        this.primResult = primResult;
        this.kruskalResult = kruskalResult;
    }

    public int getGraphId() {
        return graphId;
    }

    public int getVertices() {
        return vertices;
    }

    public int getInputEdges() {
        return inputEdges;
    }

    public MSTResult getPrimResult() {
        return primResult;
    }

    public MSTResult getKruskalResult() {
        return kruskalResult;
    }
}
