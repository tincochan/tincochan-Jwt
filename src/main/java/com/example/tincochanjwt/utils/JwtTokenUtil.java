package com.example.tincochanjwt.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtTokenUtil {
    /**
     * 请求头
     */
    private static final String TOKEN_HEADER = "Authorization";
    /**
     * TOKEN 前缀
     */
    public static final String TOKEN_PREFIX = "Bearer";
    /**
     * 私钥
     */
    private static final String TOKEN_SECRET = "secret";
    /**
     * 令牌过期时间 : one day
     */
    private static final long TOKEN_EXPIRATION = 1000 * 60 * 60 * 24;
    /**
     * 角色权限定义
     */
    private static final String ROLE_CLAIMS = "role";

    /**
     * 创建令牌
     *
     * @param username
     * @param role
     */
    public static String createToken(String username, String role) {
        Map<String, Object> map = new HashMap<>();
        map.put(ROLE_CLAIMS, role);
        return Jwts.builder()
                .setClaims(map)
                .setSubject(username)
                .claim("username", username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION))
                .signWith(SignatureAlgorithm.HS256, TOKEN_SECRET)
                .compact();
    }

    /**
     * 获取解析后的token信息
     * @param token
     * @return
     */
    public static Claims getTokenBody(String token) {
        return parseToken(token).getBody();
    }

    /**
     * 检查token是否存在
     * @param token
     * @return
     */
    public static Claims checkToken(String token) {

        try {
            Claims claims = getTokenBody(token);
            return claims;
        } catch (Exception e) {
            return null;
        }
    }
    /**
     * 判断令牌是否过期
     * @param token 令牌
     * @return boolean
     * @describe getExpiration()获取令牌过期时间
     */
    public static boolean isExpiration(String token) {
        return getTokenBody(token).getExpiration().before(new Date());
    }

    /**
     * 解析令牌
     * @param token
     * @return
     */
    public static Jws<Claims> parseToken(String token) {
        return Jwts.parser().setSigningKey(TOKEN_SECRET).parseClaimsJws(token);
    }
    /**
     * 解析token，获取用户名
     */
    public static String parseTokenToUsername(String token) {
        String username = getTokenBody(token).getSubject();
        return username;
    }
}
