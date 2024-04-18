package org.choon.crudprc.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;


@Component
public class JwtProvider {
    private String SECRET_KEY;
    private long ACCESS_TOKEN_EXPIRATION_TIME;
    public JwtProvider(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.expiration_time.access_token}") long accessTokenExprTime
    ) {
        this.SECRET_KEY = secretKey;
        this.ACCESS_TOKEN_EXPIRATION_TIME = accessTokenExprTime;
    }

    /***
     * @param secretKey : yml 에 저장되어 있는 secret key
     * @return : secret key 를 인코딩 하여 Key 객체로 리턴
     */
    private Key getSigningKey(String secretKey) {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    /***
     * @param token : 요청이 들어온 토큰
     * @return : token을 파싱. 즉, 디코딩 하여 리턴
     */
    public Claims parseToken(String token) {
        return Jwts.parserBuilder().setSigningKey(getSigningKey(SECRET_KEY)).build().parseClaimsJws(token).getBody();
    }

    /***
     * @param token : 요청이 들어온 토큰
     * @return : 토큰을 파싱하여 토큰에 들어있는 Claim을 리턴
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey(SECRET_KEY))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /***
     * @param token : 요청이 들어온 토큰
     * @return : 토큰속(claim)에 있는 클라이언트의 name 리턴
     */
    public String getNameInToken(String token) {
        return extractAllClaims(token).get("name", String.class);
    }

    /***
     * @param token : 요청이 들어온 토큰
     * @return : 토큰을 이용하여 로그인 된 UPA 객체를 가져옴 -> UPA 객체 안에 유저의 권한들이 담겨 있음
     */
//    public Authentication getAuthentication(String token) {
//        UserDetails userDetails = userDetailsService.loadUserByUsername(getEmailInToken(token));
//        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
//    }

    /***
     *
     * @param name : claim 에 넣기 위한 클라이언트의 이메일
     * @return : 토큰 타입에 맞는 토큰을 생성하여 리턴
     */
    public String createToken(String name) {
        Claims claims = Jwts.claims().setSubject(name);
        claims.put("email", name);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION_TIME))
                .signWith(getSigningKey(SECRET_KEY), SignatureAlgorithm.HS256)
                .compact();
    }

    /***
     * @param token : 요청이 들어온 토큰
     * @return : 토큰 문자열 앞의 Bearer 을 제거하고 토큰 문자열만 리턴
     */
    public String resolveToken(String token) {
        if (token != null) {
            return token.substring("Bearer ".length());
        } else {
            return "";
        }
    }

    /***
     * @param token : 요청이 들어온 토큰
     * @return : 토큰의 유효기간이 얼마나 남았는지 리턴
     */
    public Long getExpiration(String token) {
        Date expiration = Jwts.parserBuilder().setSigningKey(getSigningKey(SECRET_KEY)).build()
                .parseClaimsJws(token).getBody().getExpiration();
        long now = System.currentTimeMillis();
        return expiration.getTime() - now;
    }

    public boolean validateToken(String token) {
        return extractAllClaims(token).getExpiration().after(new Date());
    }
}
