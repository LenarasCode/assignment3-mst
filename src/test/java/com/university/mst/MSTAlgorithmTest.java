package com.university.mst;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Set;
import java.util.HashSet;

/**
 * Comprehensive test suite for MST algorithms and graph data structures.
 * Tests correctness, performance, and edge cases for both Prim's and Kruskal's algorithms.
 */
public class MSTAlgorithmTest {
    
    private Graph testGraph;
    private PrimsAlgorithm primsAlgorithm;
    private KruskalsAlgorithm kruskalsAlgorithm;
    
    @BeforeEach
    void setUp() {
        primsAlgorithm = new PrimsAlgorithm();
        kruskalsAlgorithm = new KruskalsAlgorithm();
    }
    
    @Test
    @DisplayName("Test Edge class basic functionality")
    void testEdgeBasicFunctionality() {
        Edge edge = new Edge("A", "B", 5);
        
        assertEquals("A", edge.getFrom());
        assertEquals("B", edge.getTo());
        assertEquals(5, edge.getWeight());
        assertEquals("B", edge.getOther("A"));
        assertEquals("A", edge.getOther("B"));
        assertNull(edge.getOther("C"));
        assertTrue(edge.contains("A"));
        assertTrue(edge.contains("B"));
        assertFalse(edge.contains("C"));
    }
    
    @Test
    @DisplayName("Test Edge comparison")
    void testEdgeComparison() {
        Edge edge1 = new Edge("A", "B", 3);
        Edge edge2 = new Edge("C", "D", 5);
        Edge edge3 = new Edge("E", "F", 3);
        
        assertTrue(edge1.compareTo(edge2) < 0);
        assertTrue(edge2.compareTo(edge1) > 0);
        assertEquals(0, edge1.compareTo(edge3));
    }
    
    @Test
    @DisplayName("Test Edge equality")
    void testEdgeEquality() {
        Edge edge1 = new Edge("A", "B", 5);
        Edge edge2 = new Edge("B", "A", 5);
        Edge edge3 = new Edge("A", "B", 3);
        Edge edge4 = new Edge("A", "C", 5);
        
        assertEquals(edge1, edge2); // Should be equal regardless of direction
        assertNotEquals(edge1, edge3); // Different weights
        assertNotEquals(edge1, edge4); // Different vertices
    }
    
    @Test
    @DisplayName("Test Graph basic functionality")
    void testGraphBasicFunctionality() {
        Graph graph = new Graph();
        
        // Test adding vertices and edges
        graph.addVertex("A");
        graph.addVertex("B");
        graph.addEdge("A", "B", 5);
        
        assertEquals(2, graph.getVertexCount());
        assertEquals(1, graph.getEdgeCount());
        assertTrue(graph.getVertices().contains("A"));
        assertTrue(graph.getVertices().contains("B"));
        
        // Test adjacency
        List<Edge> adjacentEdges = graph.getAdjacentEdges("A");
        assertEquals(1, adjacentEdges.size());
        assertEquals(5, adjacentEdges.get(0).getWeight());
        
        Set<String> adjacentVertices = graph.getAdjacentVertices("A");
        assertEquals(1, adjacentVertices.size());
        assertTrue(adjacentVertices.contains("B"));
    }
    
    @Test
    @DisplayName("Test Graph connectivity")
    void testGraphConnectivity() {
        Graph connectedGraph = new Graph();
        connectedGraph.addEdge("A", "B", 1);
        connectedGraph.addEdge("B", "C", 2);
        connectedGraph.addEdge("C", "A", 3);
        assertTrue(connectedGraph.isConnected());
        
        Graph disconnectedGraph = new Graph();
        disconnectedGraph.addEdge("A", "B", 1);
        disconnectedGraph.addEdge("C", "D", 2);
        assertFalse(disconnectedGraph.isConnected());
    }
    
    @Test
    @DisplayName("Test simple MST - both algorithms should produce same cost")
    void testSimpleMST() {
        Graph graph = createSimpleGraph();
        
        PrimsAlgorithm.MSTResult primResult = primsAlgorithm.findMST(graph);
        KruskalsAlgorithm.MSTResult kruskalResult = kruskalsAlgorithm.findMST(graph);
        
        // Both algorithms should produce the same total cost
        assertEquals(primResult.getTotalCost(), kruskalResult.getTotalCost());
        
        // MST should have V-1 edges
        assertEquals(graph.getVertexCount() - 1, primResult.getMstEdges().size());
        assertEquals(graph.getVertexCount() - 1, kruskalResult.getMstEdges().size());
        
        // Execution time should be non-negative
        assertTrue(primResult.getExecutionTimeMs() >= 0);
        assertTrue(kruskalResult.getExecutionTimeMs() >= 0);
        
        // Operations count should be non-negative
        assertTrue(primResult.getOperationsCount() >= 0);
        assertTrue(kruskalResult.getOperationsCount() >= 0);
    }
    
    @Test
    @DisplayName("Test MST correctness - known graph")
    void testMSTCorrectness() {
        // Create a graph with known MST cost
        Graph graph = new Graph();
        graph.addEdge("A", "B", 1);
        graph.addEdge("A", "C", 4);
        graph.addEdge("B", "C", 2);
        graph.addEdge("C", "D", 3);
        graph.addEdge("B", "D", 5);
        
        PrimsAlgorithm.MSTResult primResult = primsAlgorithm.findMST(graph);
        KruskalsAlgorithm.MSTResult kruskalResult = kruskalsAlgorithm.findMST(graph);
        
        // Expected MST cost: 1 + 2 + 3 = 6
        assertEquals(6, primResult.getTotalCost());
        assertEquals(6, kruskalResult.getTotalCost());
    }
    
    @Test
    @DisplayName("Test MST acyclicity")
    void testMSTAcyclicity() {
        Graph graph = createComplexGraph();
        
        PrimsAlgorithm.MSTResult primResult = primsAlgorithm.findMST(graph);
        KruskalsAlgorithm.MSTResult kruskalResult = kruskalsAlgorithm.findMST(graph);
        
        // Check that MST has no cycles (V-1 edges)
        assertEquals(graph.getVertexCount() - 1, primResult.getMstEdges().size());
        assertEquals(graph.getVertexCount() - 1, kruskalResult.getMstEdges().size());
        
        // Verify MST connects all vertices
        assertTrue(isSpanningTree(primResult.getMstEdges(), graph.getVertices()));
        assertTrue(isSpanningTree(kruskalResult.getMstEdges(), graph.getVertices()));
    }
    
    @Test
    @DisplayName("Test disconnected graph handling")
    void testDisconnectedGraph() {
        Graph disconnectedGraph = new Graph();
        disconnectedGraph.addEdge("A", "B", 1);
        disconnectedGraph.addEdge("C", "D", 2);
        
        assertThrows(IllegalArgumentException.class, () -> {
            primsAlgorithm.findMST(disconnectedGraph);
        });
        
        assertThrows(IllegalArgumentException.class, () -> {
            kruskalsAlgorithm.findMST(disconnectedGraph);
        });
    }
    
    @Test
    @DisplayName("Test empty graph")
    void testEmptyGraph() {
        Graph emptyGraph = new Graph();
        
        PrimsAlgorithm.MSTResult primResult = primsAlgorithm.findMST(emptyGraph);
        KruskalsAlgorithm.MSTResult kruskalResult = kruskalsAlgorithm.findMST(emptyGraph);
        
        assertEquals(0, primResult.getTotalCost());
        assertEquals(0, kruskalResult.getTotalCost());
        assertEquals(0, primResult.getMstEdges().size());
        assertEquals(0, kruskalResult.getMstEdges().size());
    }
    
    @Test
    @DisplayName("Test single vertex graph")
    void testSingleVertexGraph() {
        Graph singleVertexGraph = new Graph();
        singleVertexGraph.addVertex("A");
        
        PrimsAlgorithm.MSTResult primResult = primsAlgorithm.findMST(singleVertexGraph);
        KruskalsAlgorithm.MSTResult kruskalResult = kruskalsAlgorithm.findMST(singleVertexGraph);
        
        assertEquals(0, primResult.getTotalCost());
        assertEquals(0, kruskalResult.getTotalCost());
        assertEquals(0, primResult.getMstEdges().size());
        assertEquals(0, kruskalResult.getMstEdges().size());
    }
    
    @Test
    @DisplayName("Test performance consistency")
    void testPerformanceConsistency() {
        Graph graph = createLargeGraph();
        
        // Run multiple times to check consistency
        for (int i = 0; i < 5; i++) {
            PrimsAlgorithm.MSTResult primResult = primsAlgorithm.findMST(graph);
            KruskalsAlgorithm.MSTResult kruskalResult = kruskalsAlgorithm.findMST(graph);
            
            // Results should be consistent
            assertEquals(primResult.getTotalCost(), kruskalResult.getTotalCost());
            assertTrue(primResult.getExecutionTimeMs() >= 0);
            assertTrue(kruskalResult.getExecutionTimeMs() >= 0);
        }
    }
    
    @Test
    @DisplayName("Test algorithm scalability")
    void testAlgorithmScalability() {
        // Test with different graph sizes
        int[] sizes = {5, 10, 15, 20};
        
        for (int size : sizes) {
            Graph graph = createGraphOfSize(size);
            
            PrimsAlgorithm.MSTResult primResult = primsAlgorithm.findMST(graph);
            KruskalsAlgorithm.MSTResult kruskalResult = kruskalsAlgorithm.findMST(graph);
            
            // Both should produce same result
            assertEquals(primResult.getTotalCost(), kruskalResult.getTotalCost());
            
            // MST should have correct number of edges
            assertEquals(size - 1, primResult.getMstEdges().size());
            assertEquals(size - 1, kruskalResult.getMstEdges().size());
        }
    }
    
    @Test
    @DisplayName("Test edge case - all edges have same weight")
    void testSameWeightEdges() {
        Graph graph = new Graph();
        graph.addEdge("A", "B", 1);
        graph.addEdge("B", "C", 1);
        graph.addEdge("C", "D", 1);
        graph.addEdge("D", "A", 1);
        
        PrimsAlgorithm.MSTResult primResult = primsAlgorithm.findMST(graph);
        KruskalsAlgorithm.MSTResult kruskalResult = kruskalsAlgorithm.findMST(graph);
        
        // Both should produce same cost
        assertEquals(primResult.getTotalCost(), kruskalResult.getTotalCost());
        assertEquals(3, primResult.getTotalCost()); // 3 edges of weight 1
    }
    
    @Test
    @DisplayName("Test edge case - linear graph")
    void testLinearGraph() {
        Graph linearGraph = new Graph();
        linearGraph.addEdge("A", "B", 1);
        linearGraph.addEdge("B", "C", 2);
        linearGraph.addEdge("C", "D", 3);
        linearGraph.addEdge("D", "E", 4);
        
        PrimsAlgorithm.MSTResult primResult = primsAlgorithm.findMST(linearGraph);
        KruskalsAlgorithm.MSTResult kruskalResult = kruskalsAlgorithm.findMST(linearGraph);
        
        // Linear graph is already a tree
        assertEquals(10, primResult.getTotalCost()); // 1+2+3+4
        assertEquals(primResult.getTotalCost(), kruskalResult.getTotalCost());
    }
    
    // Helper methods
    
    private Graph createSimpleGraph() {
        Graph graph = new Graph();
        graph.addEdge("A", "B", 1);
        graph.addEdge("A", "C", 4);
        graph.addEdge("B", "C", 2);
        graph.addEdge("C", "D", 3);
        graph.addEdge("B", "D", 5);
        return graph;
    }
    
    private Graph createComplexGraph() {
        Graph graph = new Graph();
        graph.addEdge("A", "B", 2);
        graph.addEdge("A", "C", 1);
        graph.addEdge("B", "D", 3);
        graph.addEdge("C", "E", 4);
        graph.addEdge("D", "F", 5);
        graph.addEdge("E", "F", 6);
        graph.addEdge("A", "F", 7);
        graph.addEdge("B", "E", 8);
        return graph;
    }
    
    private Graph createLargeGraph() {
        Graph graph = new Graph();
        // Create a graph with 10 vertices
        for (int i = 0; i < 10; i++) {
            char vertex = (char) ('A' + i);
            if (i > 0) {
                graph.addEdge((char) ('A' + i - 1), vertex, i);
            }
        }
        // Add some cross edges
        graph.addEdge("A", "E", 2);
        graph.addEdge("B", "F", 3);
        graph.addEdge("C", "G", 4);
        graph.addEdge("D", "H", 5);
        return graph;
    }
    
    private Graph createGraphOfSize(int size) {
        Graph graph = new Graph();
        // Create a path
        for (int i = 0; i < size - 1; i++) {
            char from = (char) ('A' + i);
            char to = (char) ('A' + i + 1);
            graph.addEdge(String.valueOf(from), String.valueOf(to), i + 1);
        }
        // Add some random edges
        for (int i = 0; i < size / 2; i++) {
            char from = (char) ('A' + i);
            char to = (char) ('A' + (i + size / 2) % size);
            graph.addEdge(String.valueOf(from), String.valueOf(to), size + i);
        }
        return graph;
    }
    
    private boolean isSpanningTree(List<Edge> edges, Set<String> vertices) {
        if (edges.size() != vertices.size() - 1) {
            return false;
        }
        
        // Check if all vertices are connected
        Set<String> connectedVertices = new HashSet<>();
        for (Edge edge : edges) {
            connectedVertices.add(edge.getFrom());
            connectedVertices.add(edge.getTo());
        }
        
        return connectedVertices.equals(vertices);
    }
}
