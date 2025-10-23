# Assignment 3: Optimization of a City Transportation Network (Minimum Spanning Tree)

## Overview

This project implements and compares **Prim's** and **Kruskal's** algorithms for finding Minimum Spanning Trees (MST) in the context of optimizing a city's transportation network. The goal is to determine the minimum set of roads that connect all city districts with the lowest possible total construction cost.

## Problem Statement

The city administration plans to construct roads connecting all districts such that:
- Each district is reachable from any other district
- The total cost of construction is minimized

This scenario is modeled as a weighted undirected graph where:
- **Vertices** represent city districts
- **Edges** represent potential roads
- **Edge weights** represent the cost of constructing the road

## Algorithm Implementations

### 1. Prim's Algorithm
- **Time Complexity**: O(E log V) using binary heap
- **Space Complexity**: O(V)
- **Approach**: Grows the MST by adding the minimum weight edge that connects a vertex in the MST to a vertex outside the MST
- **Data Structure**: Priority queue (min-heap) for efficient edge selection

### 2. Kruskal's Algorithm
- **Time Complexity**: O(E log E) for sorting + O(E α(V)) for Union-Find
- **Space Complexity**: O(V)
- **Approach**: Sorts all edges by weight and adds them to the MST if they don't create a cycle
- **Data Structure**: Union-Find (Disjoint Set Union) for cycle detection

### 3. Custom Graph Data Structure (Bonus)
- **Graph.java**: Comprehensive graph implementation with adjacency list representation
- **Edge.java**: Edge class with proper comparison and equality methods
- **Features**: Connectivity checking, subgraph creation, and efficient edge/vertex operations

## Project Structure

```
assignment3-mst/
├── src/
│   ├── main/java/com/university/mst/
│   │   ├── MSTApplication.java          # Main application class
│   │   ├── PrimsAlgorithm.java          # Prim's algorithm implementation
│   │   ├── KruskalsAlgorithm.java       # Kruskal's algorithm implementation
│   │   ├── Graph.java                   # Custom graph data structure
│   │   ├── Edge.java                    # Edge representation
│   │   └── GraphData.java               # JSON serialization classes
│   └── test/java/com/university/mst/
│       └── MSTAlgorithmTest.java        # Comprehensive test suite
├── data/
│   ├── input.json                       # Combined test datasets
│   ├── small_graphs.json                # 10 small graphs (4-6 vertices)
│   ├── medium_graphs.json               # 10 medium graphs (10-15 vertices)
│   └── large_graphs.json                # 15 large graphs (20-34 vertices)
├── results/
│   └── output.json                       # Algorithm results and metrics
├── pom.xml                               # Maven configuration
└── README.md                             # This file
```

## Test Datasets

### Small Graphs (4-6 vertices)
- **Purpose**: Correctness verification and debugging
- **Count**: 10 graphs
- **Characteristics**: Simple structures for algorithm validation

### Medium Graphs (10-15 vertices)
- **Purpose**: Performance observation on moderately sized networks
- **Count**: 10 graphs
- **Characteristics**: Balanced complexity for algorithm comparison

### Large Graphs (20-34 vertices)
- **Purpose**: Scalability testing and efficiency analysis
- **Count**: 15 graphs
- **Characteristics**: Complex networks to test algorithm performance differences

## Testing Framework

### Automated Tests (JUnit 5)
The test suite includes comprehensive tests for:

#### Correctness Tests
- ✅ MST total cost identical for both algorithms
- ✅ MST contains exactly V-1 edges
- ✅ MST is acyclic (no cycles)
- ✅ MST connects all vertices (single connected component)
- ✅ Disconnected graphs handled gracefully

#### Performance Tests
- ✅ Execution time non-negative and measured in milliseconds
- ✅ Operation counts non-negative and consistent
- ✅ Results reproducible for same dataset
- ✅ Algorithm scalability across different graph sizes

#### Edge Cases
- ✅ Empty graphs
- ✅ Single vertex graphs
- ✅ Linear graphs
- ✅ Graphs with equal edge weights
- ✅ Performance consistency across multiple runs

## Usage

### Prerequisites
- Java 11 or higher
- Maven 3.6 or higher

### Building the Project
```bash
mvn clean compile
```

### Running Tests
```bash
mvn test
```

### Running the Application
```bash
mvn exec:java -Dexec.mainClass="com.university.mst.MSTApplication"
```

### Custom Input
```bash
mvn exec:java -Dexec.mainClass="com.university.mst.MSTApplication" -Dexec.args="path/to/input.json path/to/output.json"
```

## Results and Analysis

### Sample Output Format
```json
{
  "results": [
    {
      "graph_id": 1,
      "input_stats": {
        "vertices": 5,
        "edges": 7
      },
      "prim": {
        "mst_edges": [
          {"from": "B", "to": "C", "weight": 2},
          {"from": "A", "to": "C", "weight": 3},
          {"from": "B", "to": "D", "weight": 5},
          {"from": "D", "to": "E", "weight": 6}
        ],
        "total_cost": 16,
        "operations_count": 42,
        "execution_time_ms": 1.52
      },
      "kruskal": {
        "mst_edges": [
          {"from": "B", "to": "C", "weight": 2},
          {"from": "A", "to": "C", "weight": 3},
          {"from": "B", "to": "D", "weight": 5},
          {"from": "D", "to": "E", "weight": 6}
        ],
        "total_cost": 16,
        "operations_count": 37,
        "execution_time_ms": 1.28
      }
    }
  ]
}
```

## Performance Comparison

### Theoretical Analysis

| Algorithm | Time Complexity | Space Complexity | Best Case | Worst Case |
|-----------|----------------|-----------------|-----------|------------|
| **Prim's** | O(E log V) | O(V) | Dense graphs | Sparse graphs |
| **Kruskal's** | O(E log E) | O(V) | Sparse graphs | Dense graphs |

### Practical Performance Factors

#### Prim's Algorithm Advantages:
- **Dense Graphs**: More efficient when E ≈ V²
- **Memory Efficient**: Only stores vertices in MST, not all edges
- **Online Algorithm**: Can start with any vertex

#### Kruskal's Algorithm Advantages:
- **Sparse Graphs**: More efficient when E << V²
- **Parallelizable**: Edge sorting can be parallelized
- **Simple Implementation**: Easier to understand and implement

### Performance Metrics Tracked:
1. **Execution Time**: Measured in milliseconds
2. **Operation Count**: Number of key algorithmic operations
3. **Memory Usage**: Space complexity analysis
4. **Scalability**: Performance across different graph sizes

## Conclusions

### Algorithm Selection Guidelines

#### Choose Prim's Algorithm when:
- **Dense graphs** (E ≈ V²)
- **Memory is limited**
- **Starting from a specific vertex** is important
- **Real-time applications** where you can start building MST immediately

#### Choose Kruskal's Algorithm when:
- **Sparse graphs** (E << V²)
- **Parallel processing** is available
- **Simple implementation** is preferred
- **All edges are known upfront**

### Key Findings:
1. **Correctness**: Both algorithms produce identical MST costs, confirming implementation correctness
2. **Performance**: Performance varies based on graph density and size
3. **Scalability**: Both algorithms scale well, but with different characteristics
4. **Memory**: Prim's is more memory-efficient for dense graphs
5. **Implementation**: Kruskal's is simpler to implement and understand

### Real-World Applications:
- **Network Design**: Computer networks, telecommunications
- **Transportation**: Road networks, airline routes
- **Infrastructure**: Power grids, water distribution
- **Clustering**: Machine learning applications
- **Image Processing**: Region growing algorithms

## Technical Implementation Details

### Data Structures Used:
- **Priority Queue**: Min-heap for Prim's algorithm
- **Union-Find**: Disjoint Set Union for Kruskal's algorithm
- **Adjacency List**: Efficient graph representation
- **Hash Maps**: Fast vertex and edge lookups

### Optimization Techniques:
- **Path Compression**: In Union-Find for faster lookups
- **Union by Rank**: In Union-Find for balanced trees
- **Lazy Evaluation**: In Prim's algorithm for efficiency
- **Early Termination**: When MST is complete

## Future Enhancements

1. **Parallel Implementation**: Multi-threaded versions of both algorithms
2. **Dynamic Graphs**: Support for adding/removing edges
3. **Visualization**: Graph and MST visualization tools
4. **Benchmarking**: More comprehensive performance analysis
5. **Advanced Algorithms**: Borůvka's algorithm implementation

## References

1. Cormen, T. H., Leiserson, C. E., Rivest, R. L., & Stein, C. (2009). *Introduction to Algorithms* (3rd ed.). MIT Press.
2. Sedgewick, R., & Wayne, K. (2011). *Algorithms* (4th ed.). Addison-Wesley.
3. Tarjan, R. E. (1983). *Data Structures and Network Algorithms*. SIAM.
4. Kruskal, J. B. (1956). On the shortest spanning subtree of a graph and the traveling salesman problem. *Proceedings of the American Mathematical Society*, 7(1), 48-50.
5. Prim, R. C. (1957). Shortest connection networks and some generalizations. *Bell System Technical Journal*, 36(6), 1389-1401.

## License

This project is created for educational purposes as part of a university assignment.

---

**Author**: [Your Name]  
**Course**: [Course Name]  
**Institution**: [University Name]  
**Date**: [Current Date]
