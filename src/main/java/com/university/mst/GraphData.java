package com.university.mst;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data classes for JSON serialization/deserialization of graph data.
 */
public class GraphData {
    
    /**
     * Represents the complete input data structure.
     */
    public static class InputData {
        @JsonProperty("graphs")
        private List<GraphInput> graphs;
        
        public InputData() {
            this.graphs = new ArrayList<>();
        }
        
        public List<GraphInput> getGraphs() {
            return graphs;
        }
        
        public void setGraphs(List<GraphInput> graphs) {
            this.graphs = graphs;
        }
    }
    
    /**
     * Represents a single graph in the input data.
     */
    public static class GraphInput {
        @JsonProperty("id")
        private int id;
        
        @JsonProperty("nodes")
        private List<String> nodes;
        
        @JsonProperty("edges")
        private List<EdgeInput> edges;
        
        public GraphInput() {
            this.nodes = new ArrayList<>();
            this.edges = new ArrayList<>();
        }
        
        public int getId() {
            return id;
        }
        
        public void setId(int id) {
            this.id = id;
        }
        
        public List<String> getNodes() {
            return nodes;
        }
        
        public void setNodes(List<String> nodes) {
            this.nodes = nodes;
        }
        
        public List<EdgeInput> getEdges() {
            return edges;
        }
        
        public void setEdges(List<EdgeInput> edges) {
            this.edges = edges;
        }
    }
    
    /**
     * Represents an edge in the input data.
     */
    public static class EdgeInput {
        @JsonProperty("from")
        private String from;
        
        @JsonProperty("to")
        private String to;
        
        @JsonProperty("weight")
        private int weight;
        
        public EdgeInput() {}
        
        public EdgeInput(String from, String to, int weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }
        
        public String getFrom() {
            return from;
        }
        
        public void setFrom(String from) {
            this.from = from;
        }
        
        public String getTo() {
            return to;
        }
        
        public void setTo(String to) {
            this.to = to;
        }
        
        public int getWeight() {
            return weight;
        }
        
        public void setWeight(int weight) {
            this.weight = weight;
        }
    }
    
    /**
     * Represents the complete output data structure.
     */
    public static class OutputData {
        @JsonProperty("results")
        private List<ResultData> results;
        
        public OutputData() {
            this.results = new ArrayList<>();
        }
        
        public List<ResultData> getResults() {
            return results;
        }
        
        public void setResults(List<ResultData> results) {
            this.results = results;
        }
    }
    
    /**
     * Represents the result for a single graph.
     */
    public static class ResultData {
        @JsonProperty("graph_id")
        private int graphId;
        
        @JsonProperty("input_stats")
        private InputStats inputStats;
        
        @JsonProperty("prim")
        private AlgorithmResult prim;
        
        @JsonProperty("kruskal")
        private AlgorithmResult kruskal;
        
        public ResultData() {}
        
        public int getGraphId() {
            return graphId;
        }
        
        public void setGraphId(int graphId) {
            this.graphId = graphId;
        }
        
        public InputStats getInputStats() {
            return inputStats;
        }
        
        public void setInputStats(InputStats inputStats) {
            this.inputStats = inputStats;
        }
        
        public AlgorithmResult getPrim() {
            return prim;
        }
        
        public void setPrim(AlgorithmResult prim) {
            this.prim = prim;
        }
        
        public AlgorithmResult getKruskal() {
            return kruskal;
        }
        
        public void setKruskal(AlgorithmResult kruskal) {
            this.kruskal = kruskal;
        }
    }
    
    /**
     * Represents input statistics.
     */
    public static class InputStats {
        @JsonProperty("vertices")
        private int vertices;
        
        @JsonProperty("edges")
        private int edges;
        
        public InputStats() {}
        
        public InputStats(int vertices, int edges) {
            this.vertices = vertices;
            this.edges = edges;
        }
        
        public int getVertices() {
            return vertices;
        }
        
        public void setVertices(int vertices) {
            this.vertices = vertices;
        }
        
        public int getEdges() {
            return edges;
        }
        
        public void setEdges(int edges) {
            this.edges = edges;
        }
    }
    
    /**
     * Represents the result of an algorithm execution.
     */
    public static class AlgorithmResult {
        @JsonProperty("mst_edges")
        private List<EdgeInput> mstEdges;
        
        @JsonProperty("total_cost")
        private int totalCost;
        
        @JsonProperty("operations_count")
        private int operationsCount;
        
        @JsonProperty("execution_time_ms")
        private double executionTimeMs;
        
        public AlgorithmResult() {
            this.mstEdges = new ArrayList<>();
        }
        
        public List<EdgeInput> getMstEdges() {
            return mstEdges;
        }
        
        public void setMstEdges(List<EdgeInput> mstEdges) {
            this.mstEdges = mstEdges;
        }
        
        public int getTotalCost() {
            return totalCost;
        }
        
        public void setTotalCost(int totalCost) {
            this.totalCost = totalCost;
        }
        
        public int getOperationsCount() {
            return operationsCount;
        }
        
        public void setOperationsCount(int operationsCount) {
            this.operationsCount = operationsCount;
        }
        
        public double getExecutionTimeMs() {
            return executionTimeMs;
        }
        
        public void setExecutionTimeMs(double executionTimeMs) {
            this.executionTimeMs = executionTimeMs;
        }
    }
    
    /**
     * Utility class for JSON operations.
     */
    public static class JsonUtils {
        private static final ObjectMapper objectMapper = new ObjectMapper();
        
        static {
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        }
        
        /**
         * Reads input data from a JSON file.
         * 
         * @param filePath the path to the JSON file
         * @return the parsed input data
         * @throws IOException if there's an error reading the file
         */
        public static InputData readInputData(String filePath) throws IOException {
            return objectMapper.readValue(new File(filePath), InputData.class);
        }
        
        /**
         * Writes output data to a JSON file.
         * 
         * @param outputData the data to write
         * @param filePath the path to the output file
         * @throws IOException if there's an error writing the file
         */
        public static void writeOutputData(OutputData outputData, String filePath) throws IOException {
            objectMapper.writeValue(new File(filePath), outputData);
        }
        
        /**
         * Converts a Graph object to GraphInput.
         * 
         * @param graph the graph to convert
         * @param id the graph ID
         * @return the GraphInput representation
         */
        public static GraphInput graphToInput(Graph graph, int id) {
            GraphInput input = new GraphInput();
            input.setId(id);
            input.setNodes(new ArrayList<>(graph.getVertices()));
            
            List<EdgeInput> edgeInputs = new ArrayList<>();
            for (Edge edge : graph.getEdges()) {
                edgeInputs.add(new EdgeInput(edge.getFrom(), edge.getTo(), edge.getWeight()));
            }
            input.setEdges(edgeInputs);
            
            return input;
        }
        
        /**
         * Converts a GraphInput to a Graph object.
         * 
         * @param graphInput the input data
         * @return the Graph object
         */
        public static Graph inputToGraph(GraphInput graphInput) {
            Graph graph = new Graph();
            
            // Add vertices
            for (String node : graphInput.getNodes()) {
                graph.addVertex(node);
            }
            
            // Add edges
            for (EdgeInput edgeInput : graphInput.getEdges()) {
                graph.addEdge(edgeInput.getFrom(), edgeInput.getTo(), edgeInput.getWeight());
            }
            
            return graph;
        }
    }
}
