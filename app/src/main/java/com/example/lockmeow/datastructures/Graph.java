package com.example.lockmeow.datastructures;

import java.util.*;

/**
 * Implementación de un Grafo usando lista de adyacencia
 */
public class Graph<T> {
    private Map<T, List<T>> adjacencyList;
    private boolean isDirected;
    
    public Graph(boolean isDirected) {
        this.adjacencyList = new HashMap<>();
        this.isDirected = isDirected;
    }
    
    /**
     * Añade un vértice al grafo
     */
    public void addVertex(T vertex) {
        adjacencyList.putIfAbsent(vertex, new ArrayList<>());
    }
    
    /**
     * Añade una arista entre dos vértices
     */
    public void addEdge(T source, T destination) {
        addVertex(source);
        addVertex(destination);
        
        adjacencyList.get(source).add(destination);
        
        // Si el grafo no es dirigido, añadir la arista en ambas direcciones
        if (!isDirected) {
            adjacencyList.get(destination).add(source);
        }
    }
    
    /**
     * Remueve una arista entre dos vértices
     */
    public void removeEdge(T source, T destination) {
        List<T> sourceNeighbors = adjacencyList.get(source);
        if (sourceNeighbors != null) {
            sourceNeighbors.remove(destination);
        }
        
        if (!isDirected) {
            List<T> destNeighbors = adjacencyList.get(destination);
            if (destNeighbors != null) {
                destNeighbors.remove(source);
            }
        }
    }
    
    /**
     * Remueve un vértice del grafo
     */
    public void removeVertex(T vertex) {
        // Remover todas las aristas que apuntan a este vértice
        for (List<T> neighbors : adjacencyList.values()) {
            neighbors.remove(vertex);
        }
        
        // Remover el vértice y sus aristas
        adjacencyList.remove(vertex);
    }
    
    /**
     * Obtiene los vecinos de un vértice
     */
    public List<T> getNeighbors(T vertex) {
        return adjacencyList.getOrDefault(vertex, new ArrayList<>());
    }
    
    /**
     * Verifica si existe una arista entre dos vértices
     */
    public boolean hasEdge(T source, T destination) {
        List<T> neighbors = adjacencyList.get(source);
        return neighbors != null && neighbors.contains(destination);
    }
    
    /**
     * Obtiene todos los vértices del grafo
     */
    public Set<T> getVertices() {
        return adjacencyList.keySet();
    }
    
    /**
     * Búsqueda en profundidad (DFS)
     */
    public List<T> depthFirstSearch(T startVertex) {
        List<T> result = new ArrayList<>();
        Set<T> visited = new HashSet<>();
        dfsHelper(startVertex, visited, result);
        return result;
    }
    
    private void dfsHelper(T vertex, Set<T> visited, List<T> result) {
        visited.add(vertex);
        result.add(vertex);
        
        for (T neighbor : getNeighbors(vertex)) {
            if (!visited.contains(neighbor)) {
                dfsHelper(neighbor, visited, result);
            }
        }
    }
    
    /**
     * Búsqueda en anchura (BFS)
     */
    public List<T> breadthFirstSearch(T startVertex) {
        List<T> result = new ArrayList<>();
        Set<T> visited = new HashSet<>();
        Queue<T> queue = new LinkedList<>();
        
        queue.offer(startVertex);
        visited.add(startVertex);
        
        while (!queue.isEmpty()) {
            T current = queue.poll();
            result.add(current);
            
            for (T neighbor : getNeighbors(current)) {
                if (!visited.contains(neighbor)) {
                    visited.add(neighbor);
                    queue.offer(neighbor);
                }
            }
        }
        
        return result;
    }
    
    /**
     * Verifica si el grafo tiene ciclos (solo para grafos dirigidos)
     */
    public boolean hasCycle() {
        if (!isDirected) {
            return hasCycleUndirected();
        }
        
        Set<T> visited = new HashSet<>();
        Set<T> recursionStack = new HashSet<>();
        
        for (T vertex : getVertices()) {
            if (!visited.contains(vertex)) {
                if (hasCycleDirectedHelper(vertex, visited, recursionStack)) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    private boolean hasCycleDirectedHelper(T vertex, Set<T> visited, Set<T> recursionStack) {
        visited.add(vertex);
        recursionStack.add(vertex);
        
        for (T neighbor : getNeighbors(vertex)) {
            if (!visited.contains(neighbor)) {
                if (hasCycleDirectedHelper(neighbor, visited, recursionStack)) {
                    return true;
                }
            } else if (recursionStack.contains(neighbor)) {
                return true;
            }
        }
        
        recursionStack.remove(vertex);
        return false;
    }
    
    private boolean hasCycleUndirected() {
        Set<T> visited = new HashSet<>();
        
        for (T vertex : getVertices()) {
            if (!visited.contains(vertex)) {
                if (hasCycleUndirectedHelper(vertex, visited, null)) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    private boolean hasCycleUndirectedHelper(T vertex, Set<T> visited, T parent) {
        visited.add(vertex);
        
        for (T neighbor : getNeighbors(vertex)) {
            if (!visited.contains(neighbor)) {
                if (hasCycleUndirectedHelper(neighbor, visited, vertex)) {
                    return true;
                }
            } else if (!neighbor.equals(parent)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Obtiene el número de vértices
     */
    public int getVertexCount() {
        return adjacencyList.size();
    }
    
    /**
     * Obtiene el número de aristas
     */
    public int getEdgeCount() {
        int count = 0;
        for (List<T> neighbors : adjacencyList.values()) {
            count += neighbors.size();
        }
        
        return isDirected ? count : count / 2;
    }
    
    /**
     * Verifica si el grafo está vacío
     */
    public boolean isEmpty() {
        return adjacencyList.isEmpty();
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Graph (").append(isDirected ? "Directed" : "Undirected").append("):\n");
        
        for (Map.Entry<T, List<T>> entry : adjacencyList.entrySet()) {
            sb.append(entry.getKey()).append(" -> ").append(entry.getValue()).append("\n");
        }
        
        return sb.toString();
    }
}
