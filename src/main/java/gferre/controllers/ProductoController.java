package gferre.controllers;

import static spark.Spark.*;
import gferre.models.Producto;
import gferre.repository.ProductoRepository;
import java.util.List;

public class ProductoController {

    private ProductoRepository productoRepo = new ProductoRepository();

    public ProductoController() {

        path("/productos", () -> {

            // 📋 OBTENER TODOS LOS PRODUCTOS
            get("", (req, res) -> {
                res.type("application/json");
                try {
                    List<Producto> productos = productoRepo.findAll();
                    System.out.println("📦 Productos encontrados: " + productos.size());
                    
                    if (productos.isEmpty()) {
                        return "[]";
                    }
                    
                    // Convertir manualmente a JSON
                    StringBuilder json = new StringBuilder("[");
                    for (int i = 0; i < productos.size(); i++) {
                        Producto p = productos.get(i);
                        json.append(String.format(
                            "{\"id\":%d,\"nombre\":\"%s\",\"descripcion\":\"%s\",\"precio\":%.2f,\"cantidad\":%d,\"codigo\":\"%s\"}",
                            p.getId(), 
                            escapeJson(p.getNombre()), 
                            escapeJson(p.getDescripcion()), 
                            p.getPrecio(), 
                            p.getCantidad(), 
                            escapeJson(p.getCodigo())
                        ));
                        if (i < productos.size() - 1) json.append(",");
                    }
                    json.append("]");
                    
                    return json.toString();
                } catch (Exception e) {
                    res.status(500);
                    return "{\"error\": \"Error al obtener productos: " + e.getMessage() + "\"}";
                }
            });

            // 🔍 OBTENER PRODUCTO POR ID
            get("/:id", (req, res) -> {
                res.type("application/json");
                try {
                    int id = Integer.parseInt(req.params(":id"));
                    var productoOpt = productoRepo.findById(id);
                    
                    if (productoOpt.isPresent()) {
                        Producto p = productoOpt.get();
                        return String.format(
                            "{\"id\":%d,\"nombre\":\"%s\",\"descripcion\":\"%s\",\"precio\":%.2f,\"cantidad\":%d,\"codigo\":\"%s\"}",
                            p.getId(), 
                            escapeJson(p.getNombre()), 
                            escapeJson(p.getDescripcion()), 
                            p.getPrecio(), 
                            p.getCantidad(), 
                            escapeJson(p.getCodigo())
                        );
                    } else {
                        res.status(404);
                        return "{\"error\": \"Producto no encontrado\"}";
                    }
                } catch (NumberFormatException e) {
                    res.status(400);
                    return "{\"error\": \"ID inválido\"}";
                } catch (Exception e) {
                    res.status(500);
                    return "{\"error\": \"Error del servidor: " + e.getMessage() + "\"}";
                }
            });

            // ➕ CREAR NUEVO PRODUCTO
            post("", (req, res) -> {
                res.type("application/json");
                try {
                    System.out.println("📥 JSON recibido: " + req.body());
                    
                    // Parsear manualmente el JSON
                    Producto producto = parseProductoFromJson(req.body());
                    System.out.println("📦 Producto parseado: " + producto.getNombre());
                    
                    Producto productoCreado = productoRepo.save(producto);
                    System.out.println("✅ Producto guardado con ID: " + productoCreado.getId());
                    
                    return String.format(
                        "{\"id\":%d,\"nombre\":\"%s\",\"descripcion\":\"%s\",\"precio\":%.2f,\"cantidad\":%d,\"codigo\":\"%s\"}",
                        productoCreado.getId(), 
                        escapeJson(productoCreado.getNombre()), 
                        escapeJson(productoCreado.getDescripcion()), 
                        productoCreado.getPrecio(), 
                        productoCreado.getCantidad(), 
                        escapeJson(productoCreado.getCodigo())
                    );
                    
                } catch (Exception e) {
                    res.status(400);
                    System.err.println("❌ Error creando producto: " + e.getMessage());
                    return "{\"error\": \"Error creando producto: " + e.getMessage() + "\"}";
                }
            });

            // ✏️ ACTUALIZAR PRODUCTO
            put("/:id", (req, res) -> {
                res.type("application/json");
                try {
                    int id = Integer.parseInt(req.params(":id"));
                    Producto producto = parseProductoFromJson(req.body());
                    producto.setId(id);
                    
                    boolean actualizado = productoRepo.update(producto);
                    if (actualizado) {
                        return "{\"msg\": \"Producto actualizado correctamente\"}";
                    } else {
                        res.status(404);
                        return "{\"error\": \"Producto no encontrado\"}";
                    }
                } catch (NumberFormatException e) {
                    res.status(400);
                    return "{\"error\": \"ID inválido\"}";
                } catch (Exception e) {
                    res.status(400);
                    return "{\"error\": \"Error al actualizar: " + e.getMessage() + "\"}";
                }
            });

            // 🗑️ ELIMINAR PRODUCTO
            delete("/:id", (req, res) -> {
                res.type("application/json");
                try {
                    int id = Integer.parseInt(req.params(":id"));
                    boolean eliminado = productoRepo.deleteById(id);
                    if (eliminado) {
                        return "{\"msg\": \"Producto eliminado correctamente\"}";
                    } else {
                        res.status(404);
                        return "{\"error\": \"Producto no encontrado\"}";
                    }
                } catch (NumberFormatException e) {
                    res.status(400);
                    return "{\"error\": \"ID inválido\"}";
                } catch (Exception e) {
                    res.status(500);
                    return "{\"error\": \"Error del servidor: " + e.getMessage() + "\"}";
                }
            });

        });

        System.out.println("➡️ ProductoController cargado (JSON MANUAL)");
    }

    // 📝 MÉTODO PARA PARSEAR JSON MANUALMENTE
    private Producto parseProductoFromJson(String json) {
        try {
            // Remover llaves y espacios
            String cleanJson = json.replace("{", "").replace("}", "").replace("\"", "").replace(" ", "");
            
            String nombre = extractValue(cleanJson, "nombre");
            String descripcion = extractValue(cleanJson, "descripcion");
            double precio = Double.parseDouble(extractValue(cleanJson, "precio"));
            int cantidad = Integer.parseInt(extractValue(cleanJson, "cantidad"));
            String codigo = extractValue(cleanJson, "codigo");
            
            return new Producto(0, nombre, descripcion, precio, cantidad, codigo);
            
        } catch (Exception e) {
            throw new RuntimeException("Error parseando JSON: " + e.getMessage());
        }
    }

    // 🔍 MÉTODO PARA EXTRAER VALORES DEL JSON
    private String extractValue(String json, String key) {
        String search = key + ":";
        int start = json.indexOf(search);
        if (start == -1) return "";
        
        start += search.length();
        int end = json.indexOf(",", start);
        if (end == -1) end = json.length();
        
        return json.substring(start, end);
    }

    // 🛡️ MÉTODO PARA ESCAPAR CARACTERES ESPECIALES EN JSON
    private String escapeJson(String text) {
        if (text == null) return "";
        return text.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }
}