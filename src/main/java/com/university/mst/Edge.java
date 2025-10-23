package com.university.mst;

/**
 * Represents an edge in a weighted undirected graph.
 * Used for modeling potential roads between city districts.
 */
public class Edge implements Comparable<Edge> {
    private final String from;
    private final String to;
    private final int weight;
    
    /**
     * Creates a new edge between two vertices with a given weight.
     * 
     * @param from the source vertex
     * @param to the destination vertex
     * @param weight the cost/weight of the edge
     */
    public Edge(String from, String to, int weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }
    
    /**
     * Gets the source vertex of the edge.
     * 
     * @return the source vertex
     */
    public String getFrom() {
        return from;
    }
    
    /**
     * Gets the destination vertex of the edge.
     * 
     * @return the destination vertex
     */
    public String getTo() {
        return to;
    }
    
    /**
     * Gets the weight/cost of the edge.
     * 
     * @return the edge weight
     */
    public int getWeight() {
        return weight;
    }
    
    /**
     * Gets the other vertex of this edge given one vertex.
     * 
     * @param vertex one vertex of the edge
     * @return the other vertex, or null if the given vertex is not part of this edge
     */
    public String getOther(String vertex) {
        if (vertex.equals(from)) {
            return to;
        } else if (vertex.equals(to)) {
            return from;
        }
        return null;
    }
    
    /**
     * Checks if this edge contains the given vertex.
     * 
     * @param vertex the vertex to check
     * @return true if the edge contains the vertex, false otherwise
     */
    public boolean contains(String vertex) {
        return vertex.equals(from) || vertex.equals(to);
    }
    
    /**
     * Compares this edge with another edge based on weight.
     * Used for sorting edges by weight in Kruskal's algorithm.
     * 
     * @param other the other edge to compare with
     * @return negative if this edge has smaller weight, positive if larger, 0 if equal
     */
    @Override
    public int compareTo(Edge other) {
        return Integer.compare(this.weight, other.weight);
    }
    
    /**
     * Checks if this edge is equal to another object.
     * Two edges are equal if they connect the same vertices (regardless of direction).
     * 
     * @param obj the object to compare with
     * @return true if the edges are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        
        Edge edge = (Edge) obj;
        return (from.equals(edge.from) && to.equals(edge.to)) ||
               (from.equals(edge.to) && to.equals(edge.from));
    }
    
    /**
     * Generates a hash code for this edge.
     * 
     * @return the hash code
     */
    @Override
    public int hashCode() {
        // Use commutative hash to ensure A-B and B-A have same hash
        return from.hashCode() + to.hashCode();
    }
    
    /**
     * Returns a string representation of this edge.
     * 
     * @return string representation in format "from-to:weight"
     */
    @Override
    public String toString() {
        return from + "-" + to + ":" + weight;
    }
}
