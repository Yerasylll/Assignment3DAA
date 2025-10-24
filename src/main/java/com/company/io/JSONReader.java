package com.company.io;

import com.company.models.EdgeInput;
import com.company.models.graphData.GraphData;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JSONReader {

    public static List<GraphData> readGraphs(String filename) throws IOException {
        List<GraphData> graphs = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        StringBuilder json = new StringBuilder();
        String line;

        while((line = reader.readLine()) != null) {
            json.append(line);
        }
        reader.close();

        String content = json.toString();
        String[] graphBlocks = content.split("\\{\\s*\"id\":");

        for(int i = 1; i < graphBlocks.length; i++) {
            String block = graphBlocks[i];

            int id = extractInt(block, "");

            String description = extractString(block, "description");

            List<String> nodes = extractNodes(block);

            List<EdgeInput> edges = extractEdges(block);

            graphs.add(new GraphData(id, description, nodes, edges));
        }
        return graphs;
    }

    private static int extractInt(String text, String key) {
        int start = 0;
        int end = text.indexOf(',');
        if(end == -1) end = text.indexOf('\n');
        String numStr = text.substring(start, end).trim();
        return Integer.parseInt(numStr);
    }

    private static String extractString(String text, String key) {
        String pattern = "\"" + key + "\":\\s*\"";
        int start = text.indexOf(pattern) + pattern.length();
        int end = text.indexOf("\"", start);
        return text.substring(start, end);
    }

    private static List<String> extractNodes(String text) {
        List<String> nodes = new ArrayList<>();
        int nodesStart = text.indexOf("\"nodes\":") + 8;
        int nodesEnd = text.indexOf("]", nodesStart);
        String nodesStr = text.substring(nodesStart, nodesEnd);

        String[] parts = nodesStr.split("\"");
        for(String part : parts) {
            if(part.matches("N\\d+")) {
                nodes.add(part);
            }
        }
        return nodes;
    }

    private static List<EdgeInput> extractEdges(String text) {
        List<EdgeInput> edges = new ArrayList<>();
        int edgesStart = text.indexOf("\"edges\":");
        if(edgesStart == -1) return edges;

        String edgesSection = text.substring(edgesStart);
        String[] edgeBlocks = edgesSection.split("\\{\"from\":");

        for(int i = 1; i < edgeBlocks.length; i++) {
            String block = edgeBlocks[i];

            int fromStart = block.indexOf("\"") + 1;
            int fromEnd = block.indexOf("\"", fromStart);
            String from = block.substring(fromStart, fromEnd);

            int toStart = block.indexOf("\"to\":", fromEnd) + 6;
            toStart = block.indexOf("\"", toStart) + 1;
            int toEnd = block.indexOf("\"", toStart);
            String to = block.substring(toStart, toEnd);

            int weightStart = block.indexOf("\"weight\":", toEnd) + 9;
            int weightEnd = block.indexOf("}", weightStart);
            if(weightEnd == -1) weightEnd = block.indexOf(",", weightStart);
            if(weightEnd == -1) weightEnd = block.indexOf("\n", weightStart);
            String weightStr = block.substring(weightStart, weightEnd).trim();
            int weight = Integer.parseInt(weightStr);

            edges.add(new EdgeInput(from, to, weight));
        }

        return edges;
    }
}
