package com.university.mst;

import java.util.*;

/**
 * Represents a weighted undirected graph for modeling city transportation networks.
 * Vertices represent city districts, edges represent potential roads with construction costs.
 */
public class Graph {
    private final Map<String, List<Edge>> adjacencyList;
    private final List<Edge> edges;
    private final Set<String> vertices;
    
    /**
     * Creates an empty graph.
     */
    public Graph() {
        this.adjacencyList = new HashMap<>();
        this.edges = new ArrayList<>();
        this.vertices = new HashSet<>();
    }
    
    /**
     * Creates a graph with the given vertices and edges.
     * 
     * @param vertices the set of vertices
     * @param edges the list of edges
     */
    public Graph(Set<String> vertices, List<Edge> edges) {
        this.vertices = new HashSet<>(vertices);
        this.edges = new ArrayList<>(edges);
        this.adjacencyList = new HashMap<>();
        
        // Build adjacency list
        for (String vertex : vertices) {
            adjacencyList.put(vertex, new ArrayList<>());
        }
        
        for (Edge edge : edges) {
            addEdge(edge);
        }
    }
    
    /**
     * Adds a vertex to the graph.
     * 
     * @param vertex the vertex to add
     */
    public void addVertex(String vertex) {
        if (!vertices.contains(vertex)) {
            vertices.add(vertex);
            adjacencyList.put(vertex, new ArrayList<>());
        }
    }
    
    /**
     * Adds an edge to the graph.
     * 
     * @param edge the edge to add
     */
    public void addEdge(Edge edge) {
        if (!edges.contains(edge)) {
            edges.add(edge);
            
            // Add vertices if they don't exist
            addVertex(edge.getFrom());
            addVertex(edge.getTo());
            
            // Add to adjacency list
            adjacencyList.get(edge.getFrom()).add(edge);
            adjacencyList.get(edge.getTo()).add(edge);
        }
    }
    
    /**
     * Adds an edge between two vertices with a given weight.
     * 
     * @param from the source vertex
     * @param to the destination vertex
     * @param weight the edge weight
     */
    public void addEdge(String from, String to, int weight) {
        addEdge(new Edge(from, to, weight));
    }
    
    /**
     * Gets all vertices in the graph.
     * 
     * @return a set of all vertices
     */
    public Set<String> getVertices() {
        return new HashSet<>(vertices);
    }
    
    /**
     * Gets all edges in the graph.
     * 
     * @return a list of all edges
     */
    public List<Edge> getEdges() {
        return new ArrayList<>(edges);
    }
    
    /**
     * Gets the number of vertices in the graph.
     * 
     * @return the number of vertices
     */
    public int getVertexCount() {
        return vertices.size();
    }
    
    /**
     * Gets the number of edges in the graph.
     * 
     * @return the number of edges
     */
    public int getEdgeCount() {
        return edges.size();
    }
    
    /**
     * Gets all edges incident to a given vertex.
     * 
     * @param vertex the vertex
     * @return a list of edges incident to the vertex
     */
    public List<Edge> getAdjacentEdges(String vertex) {
        return new ArrayList<>(adjacencyList.getOrDefault(vertex, new ArrayList<>()));
    }
    
    /**
     * Gets all vertices adjacent to a given vertex.
     * 
     * @param vertex the vertex
     * @return a set of adjacent vertices
     */
    public Set<String> getAdjacentVertices(String vertex) {
        Set<String> adjacent = new HashSet<>();
        for (Edge edge : getAdjacentEdges(vertex)) {
            adjacent.add(edge.getOther(vertex));
        }
        return adjacent;
    }
    
    /**
     * Checks if the graph is connected.
     * 
     * @return true if the graph is connected, false otherwise
     */
    public boolean isConnected() {
        if (vertices.isEmpty()) {
            return true;
        }
        
        Set<String> visited = new HashSet<>();
        String startVertex = vertices.iterator().next();
        dfs(startVertex, visited);
        
        return visited.size() == vertices.size();
    }
    
    /**
     * Performs depth-first search to check connectivity.
     * 
     * @param vertex the current vertex
     * @param visited set of visited vertices
     */
    private void dfs(String vertex, Set<String> visited) {
        visited.add(vertex);
        for (String adjacent : getAdjacentVertices(vertex)) {
            if (!visited.contains(adjacent)) {
                dfs(adjacent, visited);
            }
        }
    }
    
    /**
     * Gets the total weight of all edges in the graph.
     * 
     * @return the total weight
     */
    public int getTotalWeight() {
        return edges.stream().mapToInt(Edge::getWeight).sum();
    }
    
    /**
     * Creates a subgraph containing only the given vertices and their connecting edges.
     * 
     * @param vertexSubset the subset of vertices to include
     * @return a new graph containing only the specified vertices and their edges
     */
    public Graph createSubgraph(Set<String> vertexSubset) {
        Graph subgraph = new Graph();
        
        for (String vertex : vertexSubset) {
            if (vertices.contains(vertex)) {
                subgraph.addVertex(vertex);
            }
        }
        
        for (Edge edge : edges) {
            if (vertexSubset.contains(edge.getFrom()) && vertexSubset.contains(edge.getTo())) {
                subgraph.addEdge(edge);
            }
        }
        
        return subgraph;
    }
    
    /**
     * Returns a string representation of the graph.
     * 
     * @return string representation showing vertices and edges
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Graph with ").append(vertices.size()).append(" vertices and ")
          .append(edges.size()).append(" edges:\n");
        
        sb.append("Vertices: ").append(vertices).append("\n");
        sb.append("Edges: ").append(edges);
        
        return sb.toString();
    }
    
    /**
     * Creates a copy of this graph.
     * 
     * @return a new graph with the same vertices and edges
     */
    public Graph copy() {
        return new Graph(vertices, edges);
    }
}
