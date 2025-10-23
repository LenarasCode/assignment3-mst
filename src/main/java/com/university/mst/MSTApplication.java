package com.university.mst;

import com.university.mst.GraphData.*;
import com.university.mst.PrimsAlgorithm.MSTResult;
import com.university.mst.KruskalsAlgorithm.MSTResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Main application class for running MST algorithms on city transportation networks.
 * Processes input JSON files and generates output with performance comparisons.
 */
public class MSTApplication {
    
    public static void main(String[] args) {
        try {
            // Default file paths
            String inputFile = "data/input.json";
            String outputFile = "results/output.json";
            
            // Override with command line arguments if provided
            if (args.length >= 1) {
                inputFile = args[0];
            }
            if (args.length >= 2) {
                outputFile = args[1];
            }
            
            System.out.println("=== Minimum Spanning Tree Analysis ===");
            System.out.println("Input file: " + inputFile);
            System.out.println("Output file: " + outputFile);
            System.out.println();
            
            // Read input data
            InputData inputData = GraphData.JsonUtils.readInputData(inputFile);
            System.out.println("Loaded " + inputData.getGraphs().size() + " graphs from input file.");
            
            // Process each graph
            OutputData outputData = new OutputData();
            
            for (GraphInput graphInput : inputData.getGraphs()) {
                System.out.println("\nProcessing Graph " + graphInput.getId() + "...");
                
                // Convert input to Graph object
                Graph graph = GraphData.JsonUtils.inputToGraph(graphInput);
                
                // Validate graph
                if (!graph.isConnected()) {
                    System.err.println("Warning: Graph " + graphInput.getId() + " is not connected. Skipping.");
                    continue;
                }
                
                // Run Prim's algorithm
                System.out.println("Running Prim's algorithm...");
                PrimsAlgorithm primsAlgorithm = new PrimsAlgorithm();
                PrimsAlgorithm.MSTResult primResult = primsAlgorithm.findMST(graph);
                
                // Run Kruskal's algorithm
                System.out.println("Running Kruskal's algorithm...");
                KruskalsAlgorithm kruskalsAlgorithm = new KruskalsAlgorithm();
                KruskalsAlgorithm.MSTResult kruskalResult = kruskalsAlgorithm.findMST(graph);
                
                // Verify results match
                if (primResult.getTotalCost() != kruskalResult.getTotalCost()) {
                    System.err.println("ERROR: MST costs don't match!");
                    System.err.println("Prim: " + primResult.getTotalCost());
                    System.err.println("Kruskal: " + kruskalResult.getTotalCost());
                }
                
                // Create result data
                ResultData result = new ResultData();
                result.setGraphId(graphInput.getId());
                result.setInputStats(new InputStats(graph.getVertexCount(), graph.getEdgeCount()));
                
                // Convert Prim's result
                AlgorithmResult primAlgorithmResult = new AlgorithmResult();
                primAlgorithmResult.setTotalCost(primResult.getTotalCost());
                primAlgorithmResult.setOperationsCount(primResult.getOperationsCount());
                primAlgorithmResult.setExecutionTimeMs(primResult.getExecutionTimeMs());
                
                List<EdgeInput> primEdges = new ArrayList<>();
                for (Edge edge : primResult.getMstEdges()) {
                    primEdges.add(new EdgeInput(edge.getFrom(), edge.getTo(), edge.getWeight()));
                }
                primAlgorithmResult.setMstEdges(primEdges);
                result.setPrim(primAlgorithmResult);
                
                // Convert Kruskal's result
                AlgorithmResult kruskalAlgorithmResult = new AlgorithmResult();
                kruskalAlgorithmResult.setTotalCost(kruskalResult.getTotalCost());
                kruskalAlgorithmResult.setOperationsCount(kruskalResult.getOperationsCount());
                kruskalAlgorithmResult.setExecutionTimeMs(kruskalResult.getExecutionTimeMs());
                
                List<EdgeInput> kruskalEdges = new ArrayList<>();
                for (Edge edge : kruskalResult.getMstEdges()) {
                    kruskalEdges.add(new EdgeInput(edge.getFrom(), edge.getTo(), edge.getWeight()));
                }
                kruskalAlgorithmResult.setMstEdges(kruskalEdges);
                result.setKruskal(kruskalAlgorithmResult);
                
                outputData.getResults().add(result);
                
                // Print summary
                System.out.println("Graph " + graphInput.getId() + " Results:");
                System.out.println("  Vertices: " + graph.getVertexCount() + ", Edges: " + graph.getEdgeCount());
                System.out.println("  MST Cost: " + primResult.getTotalCost());
                System.out.println("  Prim's - Time: " + primResult.getExecutionTimeMs() + "ms, Operations: " + primResult.getOperationsCount());
                System.out.println("  Kruskal's - Time: " + kruskalResult.getExecutionTimeMs() + "ms, Operations: " + kruskalResult.getOperationsCount());
            }
            
            // Write output data
            GraphData.JsonUtils.writeOutputData(outputData, outputFile);
            System.out.println("\nResults written to: " + outputFile);
            
            // Print overall summary
            printSummary(outputData);
            
        } catch (IOException e) {
            System.err.println("Error processing files: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Prints a summary of all results.
     * 
     * @param outputData the output data to summarize
     */
    private static void printSummary(OutputData outputData) {
        System.out.println("\n=== SUMMARY ===");
        System.out.printf("%-8s %-8s %-8s %-12s %-12s %-12s %-12s%n",
                "Graph", "Vertices", "Edges", "MST Cost", "Prim Time", "Kruskal Time", "Prim Ops");
        System.out.println("------------------------------------------------------------------------");
        
        for (ResultData result : outputData.getResults()) {
            System.out.printf("%-8d %-8d %-8d %-12d %-12.2f %-12.2f %-12d%n",
                    result.getGraphId(),
                    result.getInputStats().getVertices(),
                    result.getInputStats().getEdges(),
                    result.getPrim().getTotalCost(),
                    result.getPrim().getExecutionTimeMs(),
                    result.getKruskal().getExecutionTimeMs(),
                    result.getPrim().getOperationsCount());
        }
        
        // Calculate averages
        double avgPrimTime = outputData.getResults().stream()
                .mapToDouble(r -> r.getPrim().getExecutionTimeMs())
                .average().orElse(0.0);
        
        double avgKruskalTime = outputData.getResults().stream()
                .mapToDouble(r -> r.getKruskal().getExecutionTimeMs())
                .average().orElse(0.0);
        
        int avgPrimOps = (int) outputData.getResults().stream()
                .mapToInt(r -> r.getPrim().getOperationsCount())
                .average().orElse(0.0);
        
        int avgKruskalOps = (int) outputData.getResults().stream()
                .mapToInt(r -> r.getKruskal().getOperationsCount())
                .average().orElse(0.0);
        
        System.out.println("------------------------------------------------------------------------");
        System.out.printf("%-8s %-8s %-8s %-12s %-12.2f %-12.2f %-12d%n",
                "AVG", "", "", "", avgPrimTime, avgKruskalTime, avgPrimOps);
        
        System.out.println("\nAverage Kruskal Operations: " + avgKruskalOps);
        
        // Performance comparison
        System.out.println("\n=== PERFORMANCE COMPARISON ===");
        if (avgPrimTime < avgKruskalTime) {
            System.out.println("Prim's algorithm is faster on average by " + 
                    String.format("%.2f", avgKruskalTime - avgPrimTime) + "ms");
        } else {
            System.out.println("Kruskal's algorithm is faster on average by " + 
                    String.format("%.2f", avgPrimTime - avgKruskalTime) + "ms");
        }
        
        if (avgPrimOps < avgKruskalOps) {
            System.out.println("Prim's algorithm performs fewer operations on average by " + 
                    (avgKruskalOps - avgPrimOps));
        } else {
            System.out.println("Kruskal's algorithm performs fewer operations on average by " + 
                    (avgPrimOps - avgKruskalOps));
        }
    }
}
