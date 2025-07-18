package com.example.lockmeow;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

/**
 * Actividad para demostrar y gestionar las estructuras de datos implementadas
 */
public class DataStructuresActivity extends AppCompatActivity {
    
    private AppDataManager dataManager;
    private TextView tvHistorial, tvAppsOrdenadas, tvEstadisticas, tvCache;
    private Button btnDeshacerAccion, btnLimpiarHistorial, btnMostrarStats, btnLimpiarCache;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_structures);
        
        // Inicializar gestor de datos
        dataManager = AppDataManager.getInstance();
        
        // Inicializar vistas
        initViews();
        
        // Configurar listeners
        setupListeners();
        
        // Cargar datos iniciales
        loadInitialData();
    }
    
    private void initViews() {
        tvHistorial = findViewById(R.id.tvHistorial);
        tvAppsOrdenadas = findViewById(R.id.tvAppsOrdenadas);
        tvEstadisticas = findViewById(R.id.tvEstadisticas);
        tvCache = findViewById(R.id.tvCache);
        
        btnDeshacerAccion = findViewById(R.id.btnDeshacerAccion);
        btnLimpiarHistorial = findViewById(R.id.btnLimpiarHistorial);
        btnMostrarStats = findViewById(R.id.btnMostrarStats);
        btnLimpiarCache = findViewById(R.id.btnLimpiarCache);
    }
    
    private void setupListeners() {
        // Botón para deshacer última acción (usando Stack)
        btnDeshacerAccion.setOnClickListener(v -> {
            AppDataManager.AppAction lastAction = dataManager.undoLastAction();
            if (lastAction != null) {
                Toast.makeText(this, "Acción deshecha: " + lastAction.getAppName(), 
                    Toast.LENGTH_SHORT).show();
                updateHistorialView();
            } else {
                Toast.makeText(this, "No hay acciones para deshacer", Toast.LENGTH_SHORT).show();
            }
        });
        
        // Botón para limpiar historial
        btnLimpiarHistorial.setOnClickListener(v -> {
            dataManager.clearHistory();
            Toast.makeText(this, "Historial limpiado", Toast.LENGTH_SHORT).show();
            updateHistorialView();
        });
        
        // Botón para mostrar estadísticas generales
        btnMostrarStats.setOnClickListener(v -> {
            updateEstadisticasView();
            Toast.makeText(this, "Estadísticas actualizadas", Toast.LENGTH_SHORT).show();
        });
        
        // Botón para limpiar cache
        btnLimpiarCache.setOnClickListener(v -> {
            dataManager.clearCache();
            Toast.makeText(this, "Cache limpiado", Toast.LENGTH_SHORT).show();
            updateCacheView();
            // Recargar apps para repoblar el cache
            dataManager.loadInstalledApps(this);
        });
    }
    
    private void loadInitialData() {
        updateHistorialView();
        updateAppsOrdenadasView();
        updateEstadisticasView();
        updateCacheView();
    }
    
    /**
     * Actualiza la vista del historial (Stack)
     */
    private void updateHistorialView() {
        List<AppDataManager.AppAction> history = dataManager.getActionHistory();
        StringBuilder historialText = new StringBuilder();
        historialText.append("HISTORIAL DE ACCIONES (Stack - LIFO):\n");
        historialText.append("Total de acciones: ").append(history.size()).append("\n\n");
        
        if (history.isEmpty()) {
            historialText.append("No hay acciones registradas");
        } else {
            // Mostrar las últimas 10 acciones
            int limit = Math.min(10, history.size());
            for (int i = 0; i < limit; i++) {
                AppDataManager.AppAction action = history.get(i);
                historialText.append("• ").append(action.toString()).append("\n");
            }
            
            if (history.size() > 10) {
                historialText.append("... y ").append(history.size() - 10).append(" más");
            }
        }
        
        tvHistorial.setText(historialText.toString());
    }
    
    /**
     * Actualiza la vista de aplicaciones ordenadas (BinaryTree)
     */
    private void updateAppsOrdenadasView() {
        List<String> sortedApps = dataManager.getSortedAppList();
        StringBuilder appsText = new StringBuilder();
        appsText.append("APLICACIONES ORDENADAS (BinaryTree):\n");
        appsText.append("Total de apps: ").append(sortedApps.size()).append("\n\n");
        
        if (sortedApps.isEmpty()) {
            appsText.append("No hay aplicaciones cargadas");
        } else {
            // Mostrar las primeras 15 apps ordenadas alfabéticamente
            int limit = Math.min(15, sortedApps.size());
            for (int i = 0; i < limit; i++) {
                String packageName = sortedApps.get(i);
                AppDataManager.AppCacheData cacheData = dataManager.getCachedAppData(packageName);
                String appName = cacheData != null ? cacheData.getAppName() : packageName;
                String status = cacheData != null && cacheData.isBlocked() ? " [BLOQUEADA]" : " [LIBRE]";
                appsText.append("• ").append(appName).append(status).append("\n");
            }
            
            if (sortedApps.size() > 15) {
                appsText.append("... y ").append(sortedApps.size() - 15).append(" más");
            }
        }
        
        tvAppsOrdenadas.setText(appsText.toString());
    }
    
    /**
     * Actualiza la vista de estadísticas generales
     */
    private void updateEstadisticasView() {
        String stats = dataManager.getGeneralStats();
        
        // Añadir información adicional del grafo
        StringBuilder statsText = new StringBuilder(stats);
        statsText.append("\n=== INFORMACIÓN DEL GRAFO ===\n");
        statsText.append("¿Tiene dependencias circulares? ");
        statsText.append(dataManager.hasCircularDependencies() ? "SÍ" : "NO").append("\n");
        
        // Ejemplo de búsqueda en el árbol
        statsText.append("\n=== PRUEBA DE BÚSQUEDA ===\n");
        String testPackage = "com.android.settings";
        boolean found = dataManager.isAppInSortedList(testPackage);
        statsText.append("¿Está 'Settings' en el árbol? ").append(found ? "SÍ" : "NO").append("\n");
        
        tvEstadisticas.setText(statsText.toString());
    }
    
    /**
     * Actualiza la vista del cache (HashTable)
     */
    private void updateCacheView() {
        StringBuilder cacheText = new StringBuilder();
        cacheText.append("CACHE DE APLICACIONES (HashTable):\n");
        cacheText.append(dataManager.getCacheStats()).append("\n\n");
        
        List<String> cachedApps = dataManager.getCachedAppPackages();
        if (cachedApps.isEmpty()) {
            cacheText.append("No hay aplicaciones en cache");
        } else {
            cacheText.append("Aplicaciones en cache:\n");
            int limit = Math.min(10, cachedApps.size());
            int blockedCount = 0;
            
            for (int i = 0; i < limit; i++) {
                String packageName = cachedApps.get(i);
                AppDataManager.AppCacheData data = dataManager.getCachedAppData(packageName);
                if (data != null) {
                    String status = data.isBlocked() ? "[BLOQUEADA]" : "[LIBRE]";
                    if (data.isBlocked()) blockedCount++;
                    cacheText.append("• ").append(data.getAppName()).append(" ").append(status).append("\n");
                }
            }
            
            if (cachedApps.size() > 10) {
                cacheText.append("... y ").append(cachedApps.size() - 10).append(" más\n");
            }
            
            cacheText.append("\nApps bloqueadas: ").append(blockedCount);
            cacheText.append(" de ").append(cachedApps.size());
        }
        
        tvCache.setText(cacheText.toString());
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        // Actualizar datos cuando se regresa a la actividad
        loadInitialData();
    }
}
