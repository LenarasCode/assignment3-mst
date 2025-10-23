package com.university.mst;

import java.util.*;

/**
 * Implementation of Prim's algorithm for finding Minimum Spanning Tree.
 * Uses a priority queue to efficiently select the minimum weight edge at each step.
 */
public class PrimsAlgorithm {
    private int operationsCount;
    private long executionTimeMs;
    
    /**
     * Creates a new instance of Prim's algorithm.
     */
    public PrimsAlgorithm() {
        this.operationsCount = 0;
        this.executionTimeMs = 0;
    }
    
    /**
     * Finds the Minimum Spanning Tree using Prim's algorithm.
     * 
     * @param graph the input graph
     * @return the MST result containing edges, cost, and performance metrics
     */
    public MSTResult findMST(Graph graph) {
        long startTime = System.nanoTime();
        operationsCount = 0;
        
        if (graph.getVertexCount() == 0) {
            executionTimeMs = (System.nanoTime() - startTime) / 1_000_000;
            return new MSTResult(new ArrayList<>(), 0, operationsCount, executionTimeMs);
        }
        
        if (!graph.isConnected()) {
            executionTimeMs = (System.nanoTime() - startTime) / 1_000_000;
            throw new IllegalArgumentException("Graph is not connected - no MST exists");
        }
        
        List<Edge> mstEdges = new ArrayList<>();
        Set<String> mstVertices = new HashSet<>();
        PriorityQueue<Edge> minHeap = new PriorityQueue<>();
        
        // Start with the first vertex
        String startVertex = graph.getVertices().iterator().next();
        mstVertices.add(startVertex);
        operationsCount++; // Adding vertex to MST
        
        // Add all edges from the starting vertex to the priority queue
        for (Edge edge : graph.getAdjacentEdges(startVertex)) {
            minHeap.offer(edge);
            operationsCount++; // Heap insertion
        }
        
        // Continue until we have V-1 edges (complete MST)
        while (mstEdges.size() < graph.getVertexCount() - 1 && !minHeap.isEmpty()) {
            Edge minEdge = minHeap.poll();
            operationsCount++; // Heap extraction
            
            String newVertex = null;
            
            // Find which vertex is not yet in MST
            if (!mstVertices.contains(minEdge.getFrom())) {
                newVertex = minEdge.getFrom();
            } else if (!mstVertices.contains(minEdge.getTo())) {
                newVertex = minEdge.getTo();
            }
            
            // If we found a new vertex, add the edge to MST
            if (newVertex != null) {
                mstEdges.add(minEdge);
                mstVertices.add(newVertex);
                operationsCount += 2; // Adding edge and vertex to MST
                
                // Add all edges from the new vertex to the priority queue
                for (Edge edge : graph.getAdjacentEdges(newVertex)) {
                    // Only add edges that connect to vertices not yet in MST
                    String otherVertex = edge.getOther(newVertex);
                    if (!mstVertices.contains(otherVertex)) {
                        minHeap.offer(edge);
                        operationsCount++; // Heap insertion
                    }
                }
            }
        }
        
        // Calculate total cost
        int totalCost = mstEdges.stream().mapToInt(Edge::getWeight).sum();
        operationsCount += mstEdges.size(); // Summing weights
        
        executionTimeMs = (System.nanoTime() - startTime) / 1_000_000;
        
        return new MSTResult(mstEdges, totalCost, operationsCount, executionTimeMs);
    }
    
    /**
     * Gets the number of operations performed in the last MST computation.
     * 
     * @return the operations count
     */
    public int getOperationsCount() {
        return operationsCount;
    }
    
    /**
     * Gets the execution time of the last MST computation in milliseconds.
     * 
     * @return the execution time in milliseconds
     */
    public long getExecutionTimeMs() {
        return executionTimeMs;
    }
    
    /**
     * Result class containing MST information and performance metrics.
     */
    public static class MSTResult {
        private final List<Edge> mstEdges;
        private final int totalCost;
        private final int operationsCount;
        private final long executionTimeMs;
        
        public MSTResult(List<Edge> mstEdges, int totalCost, int operationsCount, long executionTimeMs) {
            this.mstEdges = new ArrayList<>(mstEdges);
            this.totalCost = totalCost;
            this.operationsCount = operationsCount;
            this.executionTimeMs = executionTimeMs;
        }
        
        public List<Edge> getMstEdges() {
            return new ArrayList<>(mstEdges);
        }
        
        public int getTotalCost() {
            return totalCost;
        }
        
        public int getOperationsCount() {
            return operationsCount;
        }
        
        public long getExecutionTimeMs() {
            return executionTimeMs;
        }
        
        @Override
        public String toString() {
            return String.format("MST Cost: %d, Edges: %d, Operations: %d, Time: %d ms",
                    totalCost, mstEdges.size(), operationsCount, executionTimeMs);
        }
    }
}
