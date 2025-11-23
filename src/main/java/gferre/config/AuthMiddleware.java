package gferre.config;

public class AuthMiddleware {

    public static boolean isAuthorized(String tokenHeader) {

        System.out.println("🔍 DEBUG - Header completo recibido: '" + tokenHeader + "'");

        // Si no envía header
        if (tokenHeader == null || tokenHeader.isBlank()) {
            System.out.println("❌ No se envió token header");
            return false;
        }

        // Verificar EXACTAMENTE qué contiene
        System.out.println("🔍 DEBUG - ¿Empieza con 'Bearer '? " + tokenHeader.startsWith("Bearer "));
        System.out.println("🔍 DEBUG - Longitud del header: " + tokenHeader.length());
        
        // Mostrar primeros y últimos caracteres
        if (tokenHeader.length() > 20) {
            System.out.println("🔍 DEBUG - Primeros 20 chars: '" + tokenHeader.substring(0, 20) + "'");
            System.out.println("🔍 DEBUG - Últimos 20 chars: '" + tokenHeader.substring(tokenHeader.length() - 20) + "'");
        }

        // Debe venir así: "Bearer xxx"
        if (!tokenHeader.startsWith("Bearer ")) {
            System.out.println("❌ Formato de token incorrecto, debe empezar con 'Bearer '");
            System.out.println("🔍 DEBUG - Los primeros 10 caracteres son: '" + 
                (tokenHeader.length() > 10 ? tokenHeader.substring(0, 10) : tokenHeader) + "'");
            return false;
        }

        // Extraer token real
        String token = tokenHeader.substring(7).trim();
        
        System.out.println("🔑 Token extraído: " + token);
        System.out.println("🔑 Longitud del token: " + token.length());

        // ✅ ACEPTAR CUALQUIER TOKEN TEMPORALMENTE
        System.out.println("✅ TOKEN ACEPTADO (modo depuración)");
        return true;
    }

    public static String getSecretToken() {
        return "DEBUG_MODE";
    }
}