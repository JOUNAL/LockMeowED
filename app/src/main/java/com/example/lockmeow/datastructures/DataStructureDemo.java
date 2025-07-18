package com.example.lockmeow.datastructures;

import java.util.List;

/**
 * Clase de demostración para mostrar el uso de las estructuras de datos implementadas
 */
public class DataStructureDemo {
    
    public static void demonstrateAll() {
        System.out.println("=== DEMOSTRACIÓN DE ESTRUCTURAS DE DATOS ===\n");
        
        demonstrateStack();
        demonstrateBinaryTree();
        demonstrateGraph();
        demonstrateHashTable();
    }
    
    /**
     * Demuestra el uso de la Pila (Stack) - Estructura lineal
     */
    public static void demonstrateStack() {
        System.out.println("1. PILA (STACK) - Estructura Lineal LIFO");
        System.out.println("----------------------------------------");
        
        Stack<String> stack = new Stack<>();
        
        // Añadir elementos
        stack.push("Primer elemento");
        stack.push("Segundo elemento");
        stack.push("Tercer elemento");
        
        System.out.println("Stack después de push: " + stack);
        System.out.println("Tamaño: " + stack.size());
        System.out.println("Elemento en el tope: " + stack.peek());
        
        // Remover elementos
        System.out.println("Pop: " + stack.pop());
        System.out.println("Pop: " + stack.pop());
        System.out.println("Stack después de pop: " + stack);
        System.out.println("¿Está vacía? " + stack.isEmpty());
        
        System.out.println();
    }
    
    /**
     * Demuestra el uso del Árbol Binario - Estructura no lineal
     */
    public static void demonstrateBinaryTree() {
        System.out.println("2. ÁRBOL BINARIO - Estructura No Lineal");
        System.out.println("---------------------------------------");
        
        BinaryTree<Integer> tree = new BinaryTree<>();
        
        // Insertar elementos
        tree.insert(50);
        tree.insert(30);
        tree.insert(70);
        tree.insert(20);
        tree.insert(40);
        tree.insert(60);
        tree.insert(80);
        
        System.out.println("Árbol creado con elementos: 50, 30, 70, 20, 40, 60, 80");
        System.out.println("Altura del árbol: " + tree.height());
        
        // Recorridos
        System.out.println("Recorrido en orden: " + tree.inorderTraversal());
        System.out.println("Recorrido en preorden: " + tree.preorderTraversal());
        System.out.println("Recorrido en postorden: " + tree.postorderTraversal());
        
        // Búsquedas
        System.out.println("¿Existe 40? " + tree.search(40));
        System.out.println("¿Existe 90? " + tree.search(90));
        
        // Eliminar elemento
        tree.delete(30);
        System.out.println("Después de eliminar 30: " + tree.inorderTraversal());
        
        System.out.println();
    }
    
    /**
     * Demuestra el uso del Grafo
     */
    public static void demonstrateGraph() {
        System.out.println("3. GRAFO");
        System.out.println("--------");
        
        // Grafo no dirigido
        Graph<String> undirectedGraph = new Graph<>(false);
        
        undirectedGraph.addVertex("A");
        undirectedGraph.addVertex("B");
        undirectedGraph.addVertex("C");
        undirectedGraph.addVertex("D");
        
        undirectedGraph.addEdge("A", "B");
        undirectedGraph.addEdge("B", "C");
        undirectedGraph.addEdge("C", "D");
        undirectedGraph.addEdge("D", "A");
        
        System.out.println("Grafo no dirigido:");
        System.out.println(undirectedGraph);
        
        System.out.println("Vecinos de A: " + undirectedGraph.getNeighbors("A"));
        System.out.println("¿Existe arista A-B? " + undirectedGraph.hasEdge("A", "B"));
        
        // Recorridos
        System.out.println("DFS desde A: " + undirectedGraph.depthFirstSearch("A"));
        System.out.println("BFS desde A: " + undirectedGraph.breadthFirstSearch("A"));
        
        System.out.println("¿Tiene ciclos? " + undirectedGraph.hasCycle());
        System.out.println("Número de vértices: " + undirectedGraph.getVertexCount());
        System.out.println("Número de aristas: " + undirectedGraph.getEdgeCount());
        
        System.out.println();
    }
    
    /**
     * Demuestra el uso de la Tabla Hash
     */
    public static void demonstrateHashTable() {
        System.out.println("4. TABLA HASH");
        System.out.println("-------------");
        
        HashTable<String, Integer> hashTable = new HashTable<>();
        
        // Insertar elementos
        hashTable.put("manzana", 5);
        hashTable.put("banana", 3);
        hashTable.put("naranja", 8);
        hashTable.put("uva", 12);
        hashTable.put("pera", 6);
        
        System.out.println("Tabla hash: " + hashTable);
        System.out.println("Tamaño: " + hashTable.size());
        
        // Búsquedas
        System.out.println("Valor de 'manzana': " + hashTable.get("manzana"));
        System.out.println("Valor de 'kiwi': " + hashTable.get("kiwi"));
        
        System.out.println("¿Contiene clave 'banana'? " + hashTable.containsKey("banana"));
        System.out.println("¿Contiene valor 8? " + hashTable.containsValue(8));
        
        // Obtener colecciones
        System.out.println("Todas las claves: " + hashTable.keys());
        System.out.println("Todos los valores: " + hashTable.values());
        
        // Actualizar valor
        hashTable.put("manzana", 10);
        System.out.println("Después de actualizar 'manzana': " + hashTable.get("manzana"));
        
        // Remover elemento
        Integer removedValue = hashTable.remove("banana");
        System.out.println("Valor removido de 'banana': " + removedValue);
        System.out.println("Tabla después de remover: " + hashTable);
        
        System.out.println("Factor de carga: " + String.format("%.2f", hashTable.getLoadFactor()));
        System.out.println("Capacidad: " + hashTable.getCapacity());
        
        System.out.println();
    }
}
