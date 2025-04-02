package com.navigation.utils;

import io.jsonwebtoken.*;

import java.util.Date;

public class JwtUtils {
    private static final String SECRET_KEY = "mySecretKey"; // 秘钥
    private static final long EXPIRATION_TIME = 86400000; // 1天有效期

    // 生成 Token
    public static String generateToken(String userId, String role) {
        return Jwts.builder()
                .setSubject(userId.toString())  // 主题：用户ID
                .claim("role", role)            // 存储角色（user / admin）
                .setIssuedAt(new Date())        // 签发时间
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // 过期时间
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY) // 签名算法
                .compact();
    }

    // 解析 Token（增加异常捕获）
    public static Claims parseToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
            System.out.println("解析 JWT 成功：" + claims);
            return claims;
        } catch (ExpiredJwtException e) {
            System.err.println("JWT 已过期");
            e.printStackTrace();
        } catch (UnsupportedJwtException e) {
            System.err.println("不支持的 JWT");
            e.printStackTrace();
        } catch (MalformedJwtException e) {
            System.err.println("JWT 格式错误");
            e.printStackTrace();
        } catch (SignatureException e) {
            System.err.println("JWT 签名错误");
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.err.println("JWT 为空或解析错误");
            e.printStackTrace();
        }
        return null; // 解析失败返回 null
    }

    // 获取用户ID
    public static Integer getUserId(String token) {
        Claims claims = parseToken(token);
        return claims != null ? Integer.parseInt(claims.getSubject()) : null;
    }

    // 获取角色
    public static String getUserRole(String token) {
        Claims claims = parseToken(token);
        return claims != null ? claims.get("role", String.class) : null;
    }
}
