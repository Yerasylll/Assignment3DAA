package com.company.algorithmsProcessor;

import com.company.algorithms.KruskalAlgorithm;
import com.company.algorithms.PrimAlgorithm;
import com.company.io.JSONReader;
import com.company.io.JSONWriter;
import com.company.models.MSTResult;
import com.company.models.graphData.GraphData;
import com.company.models.graphData.ResultData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MSTProcessor {

    public static void processGraphs(String inputFile, String outputFile) throws IOException {
        List<GraphData> graphs = JSONReader.readGraphs(inputFile);
        List<ResultData> results = new ArrayList<>();

        System.out.println("Processing " + graphs.size() + " graphs..\n");
        for(GraphData graph : graphs) {
            System.out.println("Processing Graph " + graph.getId() + ": " +
                    graph.getNodes().size() + " nodes, " +
                    graph.getNodes().size() + " edges");

            MSTResult primResult = PrimAlgorithm.findMST(graph.getNodes(), graph.getEdges());
            System.out.println("Prim: Cost=" + primResult.getTotalCost() +
                    ", Ops=" + primResult.getMetrics().getOperationsCount() +
                    ", Time=" + primResult.getMetrics().getExecutionTimeMs() + "ms");

            MSTResult kruskalResult = KruskalAlgorithm.findMST(graph.getNodes(), graph.getEdges());
            System.out.println("Kruskal: Cost=" + kruskalResult.getTotalCost() +
                    ", Ops=" + kruskalResult.getMetrics().getOperationsCount() +
                    ", Time=" + kruskalResult.getMetrics().getExecutionTimeMs() + "ms");

            results.add(new ResultData(graph.getId(), graph.getNodes().size(), graph.getEdges().size(),
                    primResult, kruskalResult));
            System.out.println();
        }

        JSONWriter.writeResults(results, outputFile);
        System.out.println("Results written to " + outputFile);
    }

}
