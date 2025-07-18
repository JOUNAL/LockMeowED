package com.example.lockmeow;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.example.lockmeow.datastructures.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Gestor de datos de la aplicación que utiliza las estructuras de datos implementadas
 * para optimizar el manejo de aplicaciones, historial y configuraciones
 */
public class AppDataManager {
    private static AppDataManager instance;
    private static final String TAG = "AppDataManager";
    
    // Estructuras de datos implementadas
    private Stack<AppAction> actionHistory;           // Historial de acciones (Stack - LIFO)
    private BinaryTree<String> sortedApps;          // Aplicaciones ordenadas (BinaryTree)
    private Graph<String> appDependencies;          // Relaciones entre apps (Graph)
    private HashTable<String, AppCacheData> appCache; // Cache de datos de apps (HashTable)
    
    // Singleton pattern
    private AppDataManager() {
        initializeDataStructures();
    }
    
    public static synchronized AppDataManager getInstance() {
        if (instance == null) {
            instance = new AppDataManager();
        }
        return instance;
    }
    
    /**
     * Inicializa todas las estructuras de datos
     */
    private void initializeDataStructures() {
        actionHistory = new Stack<>();
        sortedApps = new BinaryTree<>();
        appDependencies = new Graph<>(true); // Grafo dirigido para dependencias
        appCache = new HashTable<>();
        
        Log.d(TAG, "Estructuras de datos inicializadas");
    }
    
    /**
     * Clase interna para representar una acción en el historial
     */
    public static class AppAction {
        public enum ActionType { BLOCK, UNBLOCK, INSTALL, UNINSTALL }
        
        private String packageName;
        private String appName;
        private ActionType action;
        private long timestamp;
        
        public AppAction(String packageName, String appName, ActionType action) {
            this.packageName = packageName;
            this.appName = appName;
            this.action = action;
            this.timestamp = System.currentTimeMillis();
        }
        
        // Getters
        public String getPackageName() { return packageName; }
        public String getAppName() { return appName; }
        public ActionType getAction() { return action; }
        public long getTimestamp() { return timestamp; }
        
        @Override
        public String toString() {
            return action + ": " + appName + " (" + packageName + ")";
        }
    }
    
    /**
     * Clase interna para datos en cache
     */
    public static class AppCacheData {
        private String appName;
        private Drawable icon;
        private boolean isBlocked;
        private long usageTime;
        private long lastAccessed;
        
        public AppCacheData(String appName, Drawable icon, boolean isBlocked, long usageTime) {
            this.appName = appName;
            this.icon = icon;
            this.isBlocked = isBlocked;
            this.usageTime = usageTime;
            this.lastAccessed = System.currentTimeMillis();
        }
        
        // Getters y Setters
        public String getAppName() { return appName; }
        public Drawable getIcon() { return icon; }
        public boolean isBlocked() { return isBlocked; }
        public void setBlocked(boolean blocked) { this.isBlocked = blocked; }
        public long getUsageTime() { return usageTime; }
        public void setUsageTime(long usageTime) { this.usageTime = usageTime; }
        public long getLastAccessed() { return lastAccessed; }
        public void updateLastAccessed() { this.lastAccessed = System.currentTimeMillis(); }
    }
    
    // ==================== MÉTODOS PARA STACK (HISTORIAL) ====================
    
    /**
     * Registra una acción en el historial usando Stack
     */
    public void recordAction(String packageName, String appName, AppAction.ActionType action) {
        AppAction newAction = new AppAction(packageName, appName, action);
        actionHistory.push(newAction);
        Log.d(TAG, "Acción registrada: " + newAction);
    }
    
    /**
     * Obtiene la última acción del historial
     */
    public AppAction getLastAction() {
        if (!actionHistory.isEmpty()) {
            return actionHistory.peek();
        }
        return null;
    }
    
    /**
     * Deshace la última acción
     */
    public AppAction undoLastAction() {
        if (!actionHistory.isEmpty()) {
            AppAction lastAction = actionHistory.pop();
            Log.d(TAG, "Acción deshecha: " + lastAction);
            return lastAction;
        }
        return null;
    }
    
    /**
     * Obtiene el historial completo de acciones
     */
    public List<AppAction> getActionHistory() {
        List<AppAction> history = new ArrayList<>();
        Stack<AppAction> tempStack = new Stack<>();
        
        // Vaciar el stack principal al temporal para mantener orden
        while (!actionHistory.isEmpty()) {
            AppAction action = actionHistory.pop();
            tempStack.push(action);
            history.add(action);
        }
        
        // Restaurar el stack principal
        while (!tempStack.isEmpty()) {
            actionHistory.push(tempStack.pop());
        }
        
        return history;
    }
    
    /**
     * Limpia el historial de acciones
     */
    public void clearHistory() {
        actionHistory.clear();
        Log.d(TAG, "Historial limpiado");
    }
    
    // ==================== MÉTODOS PARA BINARY TREE (APPS ORDENADAS) ====================
    
    /**
     * Añade una aplicación al árbol ordenado
     */
    public void addAppToSortedList(String packageName) {
        sortedApps.insert(packageName);
        Log.d(TAG, "App añadida al árbol: " + packageName);
    }
    
    /**
     * Busca una aplicación en el árbol
     */
    public boolean isAppInSortedList(String packageName) {
        return sortedApps.search(packageName);
    }
    
    /**
     * Obtiene todas las aplicaciones ordenadas alfabéticamente
     */
    public List<String> getSortedAppList() {
        return sortedApps.inorderTraversal();
    }
    
    /**
     * Remueve una aplicación del árbol
     */
    public void removeAppFromSortedList(String packageName) {
        sortedApps.delete(packageName);
        Log.d(TAG, "App removida del árbol: " + packageName);
    }
    
    // ==================== MÉTODOS PARA GRAPH (DEPENDENCIAS) ====================
    
    /**
     * Añade una dependencia entre aplicaciones
     */
    public void addAppDependency(String parentApp, String dependentApp) {
        appDependencies.addEdge(parentApp, dependentApp);
        Log.d(TAG, "Dependencia añadida: " + parentApp + " -> " + dependentApp);
    }
    
    /**
     * Obtiene las aplicaciones que dependen de una app específica
     */
    public List<String> getAppDependents(String packageName) {
        return appDependencies.getNeighbors(packageName);
    }
    
    /**
     * Verifica si existe dependencia entre dos apps
     */
    public boolean hasDependency(String parentApp, String dependentApp) {
        return appDependencies.hasEdge(parentApp, dependentApp);
    }
    
    /**
     * Obtiene todas las apps relacionadas usando BFS
     */
    public List<String> getRelatedApps(String packageName) {
        if (appDependencies.getVertices().contains(packageName)) {
            return appDependencies.breadthFirstSearch(packageName);
        }
        return new ArrayList<>();
    }
    
    /**
     * Detecta ciclos en las dependencias
     */
    public boolean hasCircularDependencies() {
        return appDependencies.hasCycle();
    }
    
    // ==================== MÉTODOS PARA HASH TABLE (CACHE) ====================
    
    /**
     * Añade datos de una app al cache
     */
    public void cacheAppData(String packageName, String appName, Drawable icon, 
                           boolean isBlocked, long usageTime) {
        AppCacheData cacheData = new AppCacheData(appName, icon, isBlocked, usageTime);
        appCache.put(packageName, cacheData);
        Log.d(TAG, "App cacheada: " + packageName);
    }
    
    /**
     * Obtiene datos de una app del cache
     */
    public AppCacheData getCachedAppData(String packageName) {
        AppCacheData data = appCache.get(packageName);
        if (data != null) {
            data.updateLastAccessed();
        }
        return data;
    }
    
    /**
     * Actualiza el estado de bloqueo de una app en cache
     */
    public void updateAppBlockStatus(String packageName, boolean isBlocked) {
        AppCacheData data = appCache.get(packageName);
        if (data != null) {
            data.setBlocked(isBlocked);
            data.updateLastAccessed();
            Log.d(TAG, "Estado de bloqueo actualizado: " + packageName + " -> " + isBlocked);
        }
    }
    
    /**
     * Verifica si una app está en cache
     */
    public boolean isAppCached(String packageName) {
        return appCache.containsKey(packageName);
    }
    
    /**
     * Obtiene todas las apps cacheadas
     */
    public List<String> getCachedAppPackages() {
        return appCache.keys();
    }
    
    /**
     * Limpia el cache
     */
    public void clearCache() {
        appCache.clear();
        Log.d(TAG, "Cache limpiado");
    }
    
    /**
     * Obtiene estadísticas del cache
     */
    public String getCacheStats() {
        return "Apps en cache: " + appCache.size() + 
               "\nFactor de carga: " + String.format("%.2f", appCache.getLoadFactor()) +
               "\nCapacidad: " + appCache.getCapacity();
    }
    
    // ==================== MÉTODOS UTILITARIOS ====================
    
    /**
     * Carga todas las aplicaciones instaladas y las organiza en las estructuras
     */
    public void loadInstalledApps(Context context) {
        PackageManager packageManager = context.getPackageManager();
        List<ApplicationInfo> installedApps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
        
        for (ApplicationInfo appInfo : installedApps) {
            // Solo procesar apps que no son del sistema o que el usuario puede ver
            if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                String packageName = appInfo.packageName;
                String appName = appInfo.loadLabel(packageManager).toString();
                Drawable icon = appInfo.loadIcon(packageManager);
                
                // Añadir al árbol ordenado
                addAppToSortedList(packageName);
                
                // Añadir al cache
                boolean isBlocked = SharedPreferencies.getInstance(context).isAppBloqueada(packageName, context);
                cacheAppData(packageName, appName, icon, isBlocked, 0);
                
                // Añadir al grafo (cada app es un vértice)
                appDependencies.addVertex(packageName);
            }
        }
        
        Log.d(TAG, "Aplicaciones cargadas: " + sortedApps.inorderTraversal().size());
    }
    
    /**
     * Obtiene estadísticas generales de todas las estructuras
     */
    public String getGeneralStats() {
        StringBuilder stats = new StringBuilder();
        stats.append("=== ESTADÍSTICAS DEL GESTOR DE DATOS ===\n");
        stats.append("Historial de acciones: ").append(actionHistory.size()).append("\n");
        stats.append("Apps en árbol ordenado: ").append(sortedApps.inorderTraversal().size()).append("\n");
        stats.append("Vértices en grafo: ").append(appDependencies.getVertexCount()).append("\n");
        stats.append("Aristas en grafo: ").append(appDependencies.getEdgeCount()).append("\n");
        stats.append("Apps en cache: ").append(appCache.size()).append("\n");
        stats.append("Factor de carga cache: ").append(String.format("%.2f", appCache.getLoadFactor())).append("\n");
        
        return stats.toString();
    }
}
