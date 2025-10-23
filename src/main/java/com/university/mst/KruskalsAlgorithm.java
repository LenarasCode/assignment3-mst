package com.university.mst;

import java.util.*;

/**
 * Implementation of Kruskal's algorithm for finding Minimum Spanning Tree.
 * Uses Union-Find data structure to efficiently detect cycles.
 */
public class KruskalsAlgorithm {
    private int operationsCount;
    private long executionTimeMs;
    
    /**
     * Creates a new instance of Kruskal's algorithm.
     */
    public KruskalsAlgorithm() {
        this.operationsCount = 0;
        this.executionTimeMs = 0;
    }
    
    /**
     * Finds the Minimum Spanning Tree using Kruskal's algorithm.
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
        List<Edge> sortedEdges = new ArrayList<>(graph.getEdges());
        
        // Sort edges by weight (ascending order)
        Collections.sort(sortedEdges);
        operationsCount += sortedEdges.size() * (int) Math.log(sortedEdges.size()); // Sorting complexity
        
        // Initialize Union-Find data structure
        UnionFind uf = new UnionFind(graph.getVertices());
        operationsCount += graph.getVertexCount(); // Union-Find initialization
        
        // Process edges in order of increasing weight
        for (Edge edge : sortedEdges) {
            operationsCount++; // Edge processing
            
            String from = edge.getFrom();
            String to = edge.getTo();
            
            // Check if adding this edge would create a cycle
            if (!uf.connected(from, to)) {
                // Add edge to MST
                mstEdges.add(edge);
                uf.union(from, to);
                operationsCount += 2; // Adding edge and union operation
                
                // Stop when we have V-1 edges
                if (mstEdges.size() == graph.getVertexCount() - 1) {
                    break;
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
     * Union-Find data structure for cycle detection in Kruskal's algorithm.
     */
    private static class UnionFind {
        private final Map<String, String> parent;
        private final Map<String, Integer> rank;
        private int operationsCount;
        
        public UnionFind(Set<String> vertices) {
            this.parent = new HashMap<>();
            this.rank = new HashMap<>();
            this.operationsCount = 0;
            
            // Initialize each vertex as its own parent
            for (String vertex : vertices) {
                parent.put(vertex, vertex);
                rank.put(vertex, 0);
            }
        }
        
        /**
         * Finds the root of the set containing the given vertex.
         * Uses path compression for optimization.
         * 
         * @param vertex the vertex to find
         * @return the root of the set
         */
        public String find(String vertex) {
            operationsCount++;
            if (!parent.get(vertex).equals(vertex)) {
                // Path compression: make parent point directly to root
                parent.put(vertex, find(parent.get(vertex)));
                operationsCount++;
            }
            return parent.get(vertex);
        }
        
        /**
         * Unions two sets containing the given vertices.
         * Uses union by rank for optimization.
         * 
         * @param a first vertex
         * @param b second vertex
         */
        public void union(String a, String b) {
            String rootA = find(a);
            String rootB = find(b);
            operationsCount += 2; // Two find operations
            
            if (rootA.equals(rootB)) {
                return; // Already in the same set
            }
            
            // Union by rank
            if (rank.get(rootA) < rank.get(rootB)) {
                parent.put(rootA, rootB);
                operationsCount++;
            } else if (rank.get(rootA) > rank.get(rootB)) {
                parent.put(rootB, rootA);
                operationsCount++;
            } else {
                parent.put(rootB, rootA);
                rank.put(rootA, rank.get(rootA) + 1);
                operationsCount += 2;
            }
        }
        
        /**
         * Checks if two vertices are in the same connected component.
         * 
         * @param a first vertex
         * @param b second vertex
         * @return true if vertices are connected, false otherwise
         */
        public boolean connected(String a, String b) {
            return find(a).equals(find(b));
        }
        
        /**
         * Gets the number of operations performed by this Union-Find structure.
         * 
         * @return the operations count
         */
        public int getOperationsCount() {
            return operationsCount;
        }
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
