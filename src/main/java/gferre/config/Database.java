package gferre.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    private static final String URL = "jdbc:sqlite:data/ferreteria.db";

    public static void initialize() {
        try {
            Class.forName("org.sqlite.JDBC");
            System.out.println("✅ SQLite JDBC Driver cargado correctamente");
            
        } catch (ClassNotFoundException e) {
            System.err.println("❌ Error: SQLite JDBC Driver no encontrado");
            e.printStackTrace();
            return; // Salir si no hay driver
        }

        try (Connection conn = DriverManager.getConnection(URL)) {
            if (conn != null) {
                System.out.println("✅ Base de datos conectada correctamente");

                Statement stmt = conn.createStatement();

                // ELIMINAR TABLAS INNECESARIAS PRIMERO
                try {
                    stmt.executeUpdate("DROP TABLE IF EXISTS movimientos");
                    System.out.println("✅ Tabla 'movimientos' eliminada");
                } catch (SQLException e) {
                    System.out.println("ℹ️ Tabla 'movimientos' no existía: " + e.getMessage());
                }

                try {
                    stmt.executeUpdate("DROP TABLE IF EXISTS stock");
                    System.out.println("✅ Tabla 'stock' eliminada");
                } catch (SQLException e) {
                    System.out.println("ℹ️ Tabla 'stock' no existía: " + e.getMessage());
                }

                try {
                    stmt.executeUpdate("DROP TABLE IF EXISTS almacenes");
                    System.out.println("✅ Tabla 'almacenes' eliminada");
                } catch (SQLException e) {
                    System.out.println("ℹ️ Tabla 'almacenes' no existía: " + e.getMessage());
                }

                //  CREAR SOLO LA TABLA DE PRODUCTOS
                stmt.executeUpdate(
                    "CREATE TABLE IF NOT EXISTS productos (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "nombre TEXT NOT NULL," +
                    "descripcion TEXT," +
                    "precio REAL," +
                    "cantidad INTEGER," +  // ← Este campo será nuestro stock
                    "codigo TEXT UNIQUE" +
                    ")"
                );

                System.out.println("✅ Tabla 'productos' creada o ya existente");

                // Crear índices para mejor performance
                stmt.executeUpdate("CREATE INDEX IF NOT EXISTS idx_productos_codigo ON productos(codigo)");
                stmt.executeUpdate("CREATE INDEX IF NOT EXISTS idx_productos_nombre ON productos(nombre)");
                
                System.out.println("✅ Índices creados para mejor performance");
            }

        } catch (SQLException e) {
            System.err.println("❌ Error inicializando base de datos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Método para obtener conexión
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}