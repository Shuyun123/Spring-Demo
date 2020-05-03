package net.anumbrella.spring.jwt.util;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.util.Assert;

import java.time.LocalDateTime;

public class JwtUtil {

    public static final String AUTH_HEADER_KEY = "Authorization";


    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * JWT秘钥
     */
    public static final String DEFAULT_SECRET = "secret";

    public static final String USER_ID = "userId";


    /**
     * 过期时间，一小时有效期
     */
    public static final LocalDateTime EXPIRE_TIME = LocalDateTime.now().plusHours(1);


    /**
     * 签发JWT
     */
    public static String generateToken(String userId, String authInfo) {
        return generateToken(userId, authInfo, DEFAULT_SECRET);
    }

    /**
     * 签发JWT
     */
    public static String generateToken(String userId, String authInfo, String secret) {

        return Jwts.builder()
                // 角色权限相关信息
                .claim(USER_ID, userId)
                .setIssuedAt(new java.util.Date())
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    /**
     * 验证JWT
     */
    public static Boolean validateToken(String token) {
        return validateToken(token, DEFAULT_SECRET);
    }


    /**
     * 验证JWT
     */
    public static Boolean validateToken(String token, String secret) {
        try {
            return getClaimsFromToken(token, secret) != null;
        } catch (Exception e) {
            throw new IllegalStateException("Invalid Token. " + e.getMessage());
        }
    }

    /**
     * 从token中获取用户ID
     */
    public static String getUserId(String token) {
        return getUserId(token, DEFAULT_SECRET);
    }

    /**
     * 从token中获取用户ID
     */
    public static String getUserId(String token, String secret) {
        Claims claims = getClaimsFromToken(token, secret);
        return claims.get(USER_ID, String.class);
    }

    /**
     * 解析JWT
     */
    private static Claims getClaimsFromToken(String token, String secret) {
        Claims claims = Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
        return claims;
    }


}
