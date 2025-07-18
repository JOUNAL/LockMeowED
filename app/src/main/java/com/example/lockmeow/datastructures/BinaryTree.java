package com.example.lockmeow.datastructures;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementación de un Árbol Binario - Estructura de datos no lineal
 */
public class BinaryTree<T extends Comparable<T>> {
    
    private Node<T> root;
    
    /**
     * Clase interna para representar un nodo del árbol
     */
    private static class Node<T> {
        T data;
        Node<T> left;
        Node<T> right;
        
        Node(T data) {
            this.data = data;
            this.left = null;
            this.right = null;
        }
    }
    
    public BinaryTree() {
        root = null;
    }
    
    /**
     * Inserta un elemento en el árbol
     */
    public void insert(T data) {
        root = insertRec(root, data);
    }
    
    private Node<T> insertRec(Node<T> root, T data) {
        if (root == null) {
            root = new Node<>(data);
            return root;
        }
        
        if (data.compareTo(root.data) < 0) {
            root.left = insertRec(root.left, data);
        } else if (data.compareTo(root.data) > 0) {
            root.right = insertRec(root.right, data);
        }
        
        return root;
    }
    
    /**
     * Busca un elemento en el árbol
     */
    public boolean search(T data) {
        return searchRec(root, data);
    }
    
    private boolean searchRec(Node<T> root, T data) {
        if (root == null) {
            return false;
        }
        
        if (data.compareTo(root.data) == 0) {
            return true;
        }
        
        if (data.compareTo(root.data) < 0) {
            return searchRec(root.left, data);
        }
        
        return searchRec(root.right, data);
    }
    
    /**
     * Elimina un elemento del árbol
     */
    public void delete(T data) {
        root = deleteRec(root, data);
    }
    
    private Node<T> deleteRec(Node<T> root, T data) {
        if (root == null) {
            return root;
        }
        
        if (data.compareTo(root.data) < 0) {
            root.left = deleteRec(root.left, data);
        } else if (data.compareTo(root.data) > 0) {
            root.right = deleteRec(root.right, data);
        } else {
            // Nodo a eliminar encontrado
            if (root.left == null) {
                return root.right;
            } else if (root.right == null) {
                return root.left;
            }
            
            // Nodo con dos hijos
            root.data = minValue(root.right);
            root.right = deleteRec(root.right, root.data);
        }
        
        return root;
    }
    
    private T minValue(Node<T> root) {
        T minValue = root.data;
        while (root.left != null) {
            minValue = root.left.data;
            root = root.left;
        }
        return minValue;
    }
    
    /**
     * Recorrido en orden (inorder traversal)
     */
    public List<T> inorderTraversal() {
        List<T> result = new ArrayList<>();
        inorderRec(root, result);
        return result;
    }
    
    private void inorderRec(Node<T> root, List<T> result) {
        if (root != null) {
            inorderRec(root.left, result);
            result.add(root.data);
            inorderRec(root.right, result);
        }
    }
    
    /**
     * Recorrido en preorden (preorder traversal)
     */
    public List<T> preorderTraversal() {
        List<T> result = new ArrayList<>();
        preorderRec(root, result);
        return result;
    }
    
    private void preorderRec(Node<T> root, List<T> result) {
        if (root != null) {
            result.add(root.data);
            preorderRec(root.left, result);
            preorderRec(root.right, result);
        }
    }
    
    /**
     * Recorrido en postorden (postorder traversal)
     */
    public List<T> postorderTraversal() {
        List<T> result = new ArrayList<>();
        postorderRec(root, result);
        return result;
    }
    
    private void postorderRec(Node<T> root, List<T> result) {
        if (root != null) {
            postorderRec(root.left, result);
            postorderRec(root.right, result);
            result.add(root.data);
        }
    }
    
    /**
     * Verifica si el árbol está vacío
     */
    public boolean isEmpty() {
        return root == null;
    }
    
    /**
     * Obtiene la altura del árbol
     */
    public int height() {
        return heightRec(root);
    }
    
    private int heightRec(Node<T> root) {
        if (root == null) {
            return 0;
        }
        
        int leftHeight = heightRec(root.left);
        int rightHeight = heightRec(root.right);
        
        return Math.max(leftHeight, rightHeight) + 1;
    }
}
