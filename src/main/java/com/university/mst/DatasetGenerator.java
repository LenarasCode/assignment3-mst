package com.university.mst;

import com.university.mst.GraphData.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class to generate test datasets of various sizes.
 */
public class DatasetGenerator {
    
    public static void main(String[] args) {
        try {
            generateLargeGraphs();
            generateExtraGraphs();
            System.out.println("Dataset generation completed successfully!");
        } catch (IOException e) {
            System.err.println("Error generating datasets: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Generates large graphs (10 graphs with < 1000 vertices)
     */
    public static void generateLargeGraphs() throws IOException {
        InputData largeData = new InputData();
        
        // Generate 10 large graphs with varying sizes (100-900 vertices)
        int[] sizes = {100, 200, 300, 400, 500, 600, 700, 800, 900, 950};
        
        for (int i = 0; i < sizes.length; i++) {
            GraphInput graph = generateGraphOfSize(sizes[i], 40 + i);
            largeData.getGraphs().add(graph);
        }
        
        GraphData.JsonUtils.writeOutputData(convertToOutputData(largeData), "data/large_graphs.json");
    }
    
    /**
     * Generates extra large graphs (10 graphs with < 1300, 1600, 2000 vertices)
     */
    public static void generateExtraGraphs() throws IOException {
        InputData extraData = new InputData();
        
        // Generate 10 extra large graphs with varying sizes (1000-1900 vertices)
        int[] sizes = {1000, 1100, 1200, 1300, 1400, 1500, 1600, 1700, 1800, 1900};
        
        for (int i = 0; i < sizes.length; i++) {
            GraphInput graph = generateGraphOfSize(sizes[i], 50 + i);
            extraData.getGraphs().add(graph);
        }
        
        GraphData.JsonUtils.writeOutputData(convertToOutputData(extraData), "data/extra_graphs.json");
    }
    
    /**
     * Generates a graph of specified size with a given ID
     */
    private static GraphInput generateGraphOfSize(int vertexCount, int graphId) {
        GraphInput graph = new GraphInput();
        graph.setId(graphId);
        
        List<String> nodes = new ArrayList<>();
        List<EdgeInput> edges = new ArrayList<>();
        
        // Generate vertex names
        for (int i = 0; i < vertexCount; i++) {
            nodes.add(generateVertexName(i));
        }
        graph.setNodes(nodes);
        
        // Generate edges - create a spanning tree first, then add extra edges
        // Spanning tree edges (V-1 edges)
        for (int i = 1; i < vertexCount; i++) {
            int parent = (i - 1) / 2; // Tree structure
            edges.add(new EdgeInput(nodes.get(parent), nodes.get(i), (i % 50) + 1));
        }
        
        // Add extra edges for complexity (about 20% more edges)
        int extraEdges = vertexCount / 5;
        for (int i = 0; i < extraEdges; i++) {
            int from = i % vertexCount;
            int to = (i * 7) % vertexCount;
            if (from != to) {
                edges.add(new EdgeInput(nodes.get(from), nodes.get(to), (i % 100) + 1));
            }
        }
        
        graph.setEdges(edges);
        return graph;
    }
    
    /**
     * Generates vertex names for large graphs
     */
    private static String generateVertexName(int index) {
        if (index < 26) {
            return String.valueOf((char) ('A' + index));
        } else if (index < 702) { // 26 + 26*26
            int first = index / 26;
            int second = index % 26;
            return String.valueOf((char) ('A' + first)) + String.valueOf((char) ('A' + second));
        } else {
            int first = (index - 702) / 676;
            int second = ((index - 702) % 676) / 26;
            int third = (index - 702) % 26;
            return String.valueOf((char) ('A' + first)) + 
                   String.valueOf((char) ('A' + second)) + 
                   String.valueOf((char) ('A' + third));
        }
    }
    
    /**
     * Converts InputData to OutputData format for JSON writing
     */
    private static OutputData convertToOutputData(InputData inputData) {
        OutputData outputData = new OutputData();
        // This is a placeholder - in practice, you'd run the algorithms and populate results
        return outputData;
    }
}
