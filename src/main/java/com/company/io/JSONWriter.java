package com.company.io;

import com.company.models.EdgeResult;
import com.company.models.graphData.ResultData;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class JSONWriter {
    public static void writeResults(List<ResultData> results, String filename) throws IOException {
        FileWriter writer = new FileWriter(filename);
        writer.write("{\n");
        writer.write("  \"results\": [\n");

        for(int i = 0; i < results.size(); i++) {
            ResultData result = results.get(i);
            writer.write("   {\n");
            writer.write("      \"graph_id\": " + result.getGraphId() + ",\n");
            writer.write("      \"input_stats\": {\n");
            writer.write("          \"vertices\": " + result.getVertices() + ",\n");
            writer.write("          \"edges\": " + result.getInputEdges() + "\n");
            writer.write("      },\n");

            // Prim results
            writer.write("      \"prim\": {\n");
            writer.write("          \"mst_edges\": [\n");
            writeEdges(writer, result.getPrimResult().getMstEdges());
            writer.write("          ],\n");
            writer.write("          \"total_cost\": " + result.getPrimResult().getTotalCost() + ",\n");
            writer.write("          \"operations_count\": " + result.getPrimResult().getMetrics().getOperationsCount() + ",\n");
            writer.write("          \"execution_time_ms\": " + result.getPrimResult().getMetrics().getExecutionTimeMs() + "\n");
            writer.write("      },\n");

            // Kruskal results
            writer.write("      \"kruskal\": {\n");
            writer.write("          \"mst_edges\": [\n");
            writeEdges(writer, result.getKruskalResult().getMstEdges());
            writer.write("      ],\n");
            writer.write("      \"total_cost\": " + result.getKruskalResult().getTotalCost() + ",\n");
            writer.write("          \"operations_count\": " + result.getKruskalResult().getMetrics().getOperationsCount() + ",\n");
            writer.write("          \"execution_time_ms\": " + result.getKruskalResult().getMetrics().getExecutionTimeMs() + "\n");
            writer.write("      }\n");

            writer.write("    }");
            if(i < results.size() - 1) writer.write(",");
            writer.write("\n");
        }

        writer.write("  ]\n");
        writer.write("}\n");
        writer.close();
    }

    private static void writeEdges(FileWriter writer, List<EdgeResult> edges) throws IOException {
        for(int i = 0; i < edges.size(); i++) {
            EdgeResult edge = edges.get(i);
            writer.write("          {\"from\": \"" + edge.getFrom() + "\", " +
                    "\"to\": \"" + edge.getTo() + "\", " +
                    "\"weight\": " + edge.getWeight() + "}");
            if(i < edges.size() - 1) writer.write(",");
            writer.write("\n");
        }
    }
}
