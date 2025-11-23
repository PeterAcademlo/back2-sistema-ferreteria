package gferre;

import gferre.config.Database;
import gferre.controllers.ProductoController;
import static spark.Spark.*;

public class Main {

    public static void main(String[] args) {
        System.out.println(" Iniciando BACK2 (Sistema de Ventas Simplificado)...");
        System.out.println(" Iniciando base de datos...");
        Database.initialize();

        // Configurar puerto
        port(8081);

        // Configurar CORS
        options("/*", (request, response) -> {
            String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
            if (accessControlRequestHeaders != null) {
                response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
            }
            String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
            if (accessControlRequestMethod != null) {
                response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
            }
            return "OK";
        });

        before((request, response) -> {
            response.header("Access-Control-Allow-Origin", "*");
            response.header("Access-Control-Request-Method", "*");
            response.header("Access-Control-Allow-Headers", "*");
            response.type("application/json");
        });

        // ✅ SOLO ProductoController - Eliminados los otros
        new ProductoController();

        System.out.println(" Servidor iniciado en http://localhost:8081");
        System.out.println("► Rutas disponibles:");
        System.out.println("   GET    /api/productos                 # Listar stock");
        System.out.println("   GET    /api/productos/:id             # Obtener producto");
        System.out.println("   POST   /api/productos                 # Crear producto");
        System.out.println("   PUT    /api/productos/:id             # Actualizar producto");
        System.out.println("   DELETE /api/productos/:id             # Eliminar producto");
        System.out.println("   GET    /api/productos/buscar/:query   # Buscar para ventas");
        System.out.println("   PUT    /api/productos/:id/stock       # Actualizar stock");
        System.out.println("----------------------------------------");
        System.out.println(" Todas las rutas requieren token de autenticación");
    }
}