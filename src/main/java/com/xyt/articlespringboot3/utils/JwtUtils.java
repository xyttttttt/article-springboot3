package com.xyt.articlespringboot3.utils;

import com.xyt.articlespringboot3.entity.LoginUser;
import io.jsonwebtoken.*;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT工具类
 */
public class JwtUtils {


    public static  long TOKEN_TIME_OUT = 1000 * 60 * 60 * 2;

    public static  long REFRESH_TOKEN_TIME_OUT = 1000 * 60 * 60 * 4;
    //密钥 现在的密钥是随便写的，实际中公司会提供
    public static  String TOKEN_SECRET = "abcdefghigklmn";



    private static Key getSecretKey() {
        return new SecretKeySpec(JwtUtils.TOKEN_SECRET.getBytes(), SignatureAlgorithm.HS256.getJcaName());
    }

    /**
     * 解析
     *
     * @param jwt
     * @return
     * @throws Exception
     */
    public static Claims parseJWT(String jwt) {
        try {
            Key key = getSecretKey(); // 根据签名算法获取密钥
            return Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(jwt)
                    .getBody();
        } catch (Exception e) {
            // 其他异常情况处理
            System.out.println("token过期: " + e.getMessage());
            return null;
        }
    }

    /**
     * 生成jtw
     * @param claims token中要存放的数据（json格式）
     * @return
     */
    public static String createToken(Map<String, Object> claims, String subject) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + TOKEN_TIME_OUT);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject) // 主题，可以是用户ID或其他标识符
                .setIssuedAt(now) // 签发时间
                .setExpiration(expiryDate) // 过期时间
                .signWith(SignatureAlgorithm.HS256, TOKEN_SECRET.getBytes()) // 使用密钥和指定的签名算法
                .compact();
    }






    public static String createRefreshToken(Map<String, Object> claims, String subject) {
        Date now = new Date(System.currentTimeMillis());
        Date expiryDate = new Date(System.currentTimeMillis() + REFRESH_TOKEN_TIME_OUT);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject) // 主题，可以是用户ID或其他标识符
                .setIssuedAt(now) // 签发时间
                .setExpiration(expiryDate) // 过期时间
                .signWith(SignatureAlgorithm.HS256, TOKEN_SECRET.getBytes()) // 使用密钥和指定的签名算法
                .compact();
    }

    public static boolean isValidToken(String jwtToken) {
        try {
            Key key = getSecretKey(); // 根据签名算法获取密钥
            Claims claims = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(jwtToken)
                    .getBody();
            // 进行进一步的验证，比如检查过期时间等...
            Date expiration = claims.getExpiration();
            return !expiration.before(new Date());
        } catch (ExpiredJwtException e) {
            // 如果是因为过期抛出的异常，明确打印错误信息并返回false
            System.out.println("Token已过期: " + e.getMessage());
            return false;

        } catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            // 其他与验证相关的异常处理
            System.out.println("Token验证失败: " + e.getMessage());
            return false;
        }
    }
}
