package gferre.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

import java.util.Date;

public class TokenService {

    private static final String SECRET_KEY = "miSecretoSuperSeguro"; // Cambiar por variable de entorno
    private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hora

    private static final Algorithm algorithm = Algorithm.HMAC256(SECRET_KEY);

    // Generar token para un usuario (por ejemplo usando su email o id)
    public static String generarToken(String usuario) {
        return JWT.create()
                .withSubject(usuario)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(algorithm);
    }

    // Validar token
    public static boolean validarToken(String token) {
        try {
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(token);
            return true;
        } catch (JWTVerificationException e) {
            return false;
        }
    }

    // Obtener usuario del token
    public static String obtenerUsuario(String token) {
        try {
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt.getSubject();
        } catch (JWTVerificationException e) {
            return null;
        }
    }
}
