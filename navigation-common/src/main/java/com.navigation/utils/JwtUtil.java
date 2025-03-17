package com.navigation.utils;

import io.jsonwebtoken.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 处理 JSON Web Token（JWT）相关的操作，比如生成、验证 JWT
 */
public class JwtUtil {

    private static final String SECRET_KEY = "navigationSystem"; // 你的密钥，请保持私密
    private static final long JWT_EXPIRATION = 86400000; // 24小时

    public static String createToken(String userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);

        Date expiration = new Date(System.currentTimeMillis() + JWT_EXPIRATION);

        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    public static Claims parseToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
            System.out.println(claims);
            return claims;
        } catch (ExpiredJwtException e) {
            // JWT已过期
            e.printStackTrace();
        } catch (UnsupportedJwtException e) {
            // 不支持的JWT
            e.printStackTrace();
        } catch (MalformedJwtException e) {
            // JWT格式错误
            e.printStackTrace();
        } catch (SignatureException e) {
            // JWT签名错误
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // JWT字符串为空
            e.printStackTrace();
        }
        return null;
    }
}
