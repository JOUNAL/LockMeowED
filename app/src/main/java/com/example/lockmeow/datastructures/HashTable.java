package com.example.lockmeow.datastructures;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementación de una Tabla Hash con manejo de colisiones por encadenamiento
 */
public class HashTable<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double LOAD_FACTOR_THRESHOLD = 0.75;
    
    private List<Entry<K, V>>[] buckets;
    private int size;
    private int capacity;
    
    /**
     * Clase interna para representar una entrada clave-valor
     */
    private static class Entry<K, V> {
        K key;
        V value;
        
        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
        
        @Override
        public String toString() {
            return key + "=" + value;
        }
    }
    
    @SuppressWarnings("unchecked")
    public HashTable() {
        this.capacity = DEFAULT_CAPACITY;
        this.buckets = new List[capacity];
        this.size = 0;
        
        // Inicializar cada bucket como una lista vacía
        for (int i = 0; i < capacity; i++) {
            buckets[i] = new ArrayList<>();
        }
    }
    
    @SuppressWarnings("unchecked")
    public HashTable(int initialCapacity) {
        this.capacity = initialCapacity;
        this.buckets = new List[capacity];
        this.size = 0;
        
        for (int i = 0; i < capacity; i++) {
            buckets[i] = new ArrayList<>();
        }
    }
    
    /**
     * Función hash para obtener el índice del bucket
     */
    private int hash(K key) {
        if (key == null) {
            return 0;
        }
        return Math.abs(key.hashCode()) % capacity;
    }
    
    /**
     * Inserta o actualiza una entrada clave-valor
     */
    public void put(K key, V value) {
        int index = hash(key);
        List<Entry<K, V>> bucket = buckets[index];
        
        // Buscar si la clave ya existe
        for (Entry<K, V> entry : bucket) {
            if (entry.key != null && entry.key.equals(key)) {
                entry.value = value; // Actualizar valor existente
                return;
            }
        }
        
        // Añadir nueva entrada
        bucket.add(new Entry<>(key, value));
        size++;
        
        // Verificar si necesitamos redimensionar
        if ((double) size / capacity > LOAD_FACTOR_THRESHOLD) {
            resize();
        }
    }
    
    /**
     * Obtiene el valor asociado a una clave
     */
    public V get(K key) {
        int index = hash(key);
        List<Entry<K, V>> bucket = buckets[index];
        
        for (Entry<K, V> entry : bucket) {
            if (entry.key != null && entry.key.equals(key)) {
                return entry.value;
            }
        }
        
        return null; // Clave no encontrada
    }
    
    /**
     * Remueve una entrada por su clave
     */
    public V remove(K key) {
        int index = hash(key);
        List<Entry<K, V>> bucket = buckets[index];
        
        for (int i = 0; i < bucket.size(); i++) {
            Entry<K, V> entry = bucket.get(i);
            if (entry.key != null && entry.key.equals(key)) {
                V removedValue = entry.value;
                bucket.remove(i);
                size--;
                return removedValue;
            }
        }
        
        return null; // Clave no encontrada
    }
    
    /**
     * Verifica si la tabla contiene una clave
     */
    public boolean containsKey(K key) {
        return get(key) != null;
    }
    
    /**
     * Verifica si la tabla contiene un valor
     */
    public boolean containsValue(V value) {
        for (List<Entry<K, V>> bucket : buckets) {
            for (Entry<K, V> entry : bucket) {
                if (entry.value != null && entry.value.equals(value)) {
                    return true;
                }
            }
        }
        return false;
    }
    
    /**
     * Obtiene todas las claves
     */
    public List<K> keys() {
        List<K> keyList = new ArrayList<>();
        for (List<Entry<K, V>> bucket : buckets) {
            for (Entry<K, V> entry : bucket) {
                keyList.add(entry.key);
            }
        }
        return keyList;
    }
    
    /**
     * Obtiene todos los valores
     */
    public List<V> values() {
        List<V> valueList = new ArrayList<>();
        for (List<Entry<K, V>> bucket : buckets) {
            for (Entry<K, V> entry : bucket) {
                valueList.add(entry.value);
            }
        }
        return valueList;
    }
    
    /**
     * Obtiene todas las entradas
     */
    public List<Entry<K, V>> entries() {
        List<Entry<K, V>> entryList = new ArrayList<>();
        for (List<Entry<K, V>> bucket : buckets) {
            entryList.addAll(bucket);
        }
        return entryList;
    }
    
    /**
     * Redimensiona la tabla hash cuando el factor de carga es alto
     */
    @SuppressWarnings("unchecked")
    private void resize() {
        List<Entry<K, V>>[] oldBuckets = buckets;
        int oldCapacity = capacity;
        
        capacity *= 2;
        buckets = new List[capacity];
        size = 0;
        
        // Inicializar nuevos buckets
        for (int i = 0; i < capacity; i++) {
            buckets[i] = new ArrayList<>();
        }
        
        // Reinsertar todas las entradas
        for (int i = 0; i < oldCapacity; i++) {
            for (Entry<K, V> entry : oldBuckets[i]) {
                put(entry.key, entry.value);
            }
        }
    }
    
    /**
     * Obtiene el número de entradas
     */
    public int size() {
        return size;
    }
    
    /**
     * Verifica si la tabla está vacía
     */
    public boolean isEmpty() {
        return size == 0;
    }
    
    /**
     * Limpia todas las entradas
     */
    public void clear() {
        for (List<Entry<K, V>> bucket : buckets) {
            bucket.clear();
        }
        size = 0;
    }
    
    /**
     * Obtiene el factor de carga actual
     */
    public double getLoadFactor() {
        return (double) size / capacity;
    }
    
    /**
     * Obtiene la capacidad actual
     */
    public int getCapacity() {
        return capacity;
    }
    
    /**
     * Obtiene estadísticas de distribución de buckets
     */
    public String getBucketStats() {
        StringBuilder sb = new StringBuilder();
        sb.append("Bucket Distribution:\n");
        
        int emptyBuckets = 0;
        int maxBucketSize = 0;
        
        for (int i = 0; i < capacity; i++) {
            int bucketSize = buckets[i].size();
            if (bucketSize == 0) {
                emptyBuckets++;
            }
            maxBucketSize = Math.max(maxBucketSize, bucketSize);
            sb.append("Bucket ").append(i).append(": ").append(bucketSize).append(" entries\n");
        }
        
        sb.append("\nStatistics:\n");
        sb.append("Empty buckets: ").append(emptyBuckets).append("/").append(capacity).append("\n");
        sb.append("Max bucket size: ").append(maxBucketSize).append("\n");
        sb.append("Load factor: ").append(String.format("%.2f", getLoadFactor())).append("\n");
        
        return sb.toString();
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("HashTable {");
        
        boolean first = true;
        for (List<Entry<K, V>> bucket : buckets) {
            for (Entry<K, V> entry : bucket) {
                if (!first) {
                    sb.append(", ");
                }
                sb.append(entry.toString());
                first = false;
            }
        }
        
        sb.append("}");
        return sb.toString();
    }
}
