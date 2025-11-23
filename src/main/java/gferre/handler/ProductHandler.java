package gferre.handler;

import gferre.repository.ProductoRepository;
import java.io.PrintWriter;

public class ProductHandler {

    private ProductoRepository productoRepo = new ProductoRepository();

    public void handle(String method, String path, String body, String token, PrintWriter out) {
        switch (method) {
            case "GET":
                // Ejemplo simple: devolver todos los productos
                sendResponse(out, 200, productoRepo.findAll().toString());
                break;

            case "POST":
                // Aquí parsearías `body` para crear un Producto
                // Por ahora solo placeholder
                sendResponse(out, 200, "{\"msg\": \"Producto creado (placeholder)\"}");
                break;

            case "PUT":
                sendResponse(out, 200, "{\"msg\": \"Producto actualizado (placeholder)\"}");
                break;

            case "DELETE":
                sendResponse(out, 200, "{\"msg\": \"Producto eliminado (placeholder)\"}");
                break;

            default:
                sendResponse(out, 405, "{\"error\":\"Método no permitido\"}");
        }
    }

    private void sendResponse(PrintWriter out, int code, String json) {
        out.println("HTTP/1.1 " + code + " OK");
        out.println("Content-Type: application/json");
        out.println();
        out.println(json);
        out.flush();
    }
}
