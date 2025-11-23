package gferre.repository;

import gferre.models.Producto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductoRepository {

    public Producto save(Producto producto) {
        String sql = "INSERT INTO productos(nombre, descripcion, precio, cantidad, codigo) VALUES(?, ?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, producto.getNombre());
            pstmt.setString(2, producto.getDescripcion());
            pstmt.setDouble(3, producto.getPrecio());
            pstmt.setInt(4, producto.getCantidad());
            pstmt.setString(5, producto.getCodigo());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        producto.setId(rs.getInt(1));
                        System.out.println("✅ Producto guardado en BD con ID: " + producto.getId());
                    }
                }
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error guardando producto: " + e.getMessage());
        }
        return producto;
    }

    public List<Producto> findAll() {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM productos";
        
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Producto producto = new Producto(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("descripcion"),
                    rs.getDouble("precio"),
                    rs.getInt("cantidad"),
                    rs.getString("codigo")
                );
                productos.add(producto);
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error obteniendo productos: " + e.getMessage());
        }
        return productos;
    }

    public Optional<Producto> findById(int id) {
        String sql = "SELECT * FROM productos WHERE id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Producto producto = new Producto(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("descripcion"),
                    rs.getDouble("precio"),
                    rs.getInt("cantidad"),
                    rs.getString("codigo")
                );
                return Optional.of(producto);
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error buscando producto: " + e.getMessage());
        }
        return Optional.empty();
    }

    public Optional<Producto> findByCodigo(String codigo) {
        String sql = "SELECT * FROM productos WHERE codigo = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, codigo);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Producto producto = new Producto(
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("descripcion"),
                    rs.getDouble("precio"),
                    rs.getInt("cantidad"),
                    rs.getString("codigo")
                );
                return Optional.of(producto);
            }
            
        } catch (SQLException e) {
            System.err.println("❌ Error buscando producto por código: " + e.getMessage());
        }
        return Optional.empty();
    }

    public boolean update(Producto producto) {
        String sql = "UPDATE productos SET nombre = ?, descripcion = ?, precio = ?, cantidad = ?, codigo = ? WHERE id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, producto.getNombre());
            pstmt.setString(2, producto.getDescripcion());
            pstmt.setDouble(3, producto.getPrecio());
            pstmt.setInt(4, producto.getCantidad());
            pstmt.setString(5, producto.getCodigo());
            pstmt.setInt(6, producto.getId());
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("❌ Error actualizando producto: " + e.getMessage());
        }
        return false;
    }

    public boolean deleteById(int id) {
        String sql = "DELETE FROM productos WHERE id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
            
        } catch (SQLException e) {
            System.err.println("❌ Error eliminando producto: " + e.getMessage());
        }
        return false;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection("jdbc:sqlite:data/ferreteria.db");
    }

    public List<Producto> buscarPorNombreOCodigo(String query) {
    List<Producto> productos = new ArrayList<>();
    String sql = "SELECT * FROM productos WHERE nombre LIKE ? OR codigo LIKE ? OR descripcion LIKE ?";
    
    try (Connection conn = getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
        String searchTerm = "%" + query + "%";
        pstmt.setString(1, searchTerm);
        pstmt.setString(2, searchTerm);
        pstmt.setString(3, searchTerm);
        
        ResultSet rs = pstmt.executeQuery();
        
        while (rs.next()) {
            Producto producto = new Producto(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("descripcion"),
                rs.getDouble("precio"),
                rs.getInt("cantidad"),
                rs.getString("codigo")
            );
            productos.add(producto);
        }
        
        System.out.println("🔍 Búsqueda '" + query + "' - Encontrados: " + productos.size());
        
    } catch (SQLException e) {
        System.err.println("❌ Error buscando productos: " + e.getMessage());
    }
    return productos;
}
}