package com.sparta.write.jwt;



import com.sparta.write.entity.UserRoleEnum;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {


    //토큰 생성에 필요한 값을 생성
    //jwt구현하기 2(from gradle-properties)//이후 같은 JwtUtil에 Token 가져오기부분 만들기
    public static final String AUTHORIZATION_HEADER = "Authorization";// Header KEY 값
//    public static final String AUTHORIZATION_KEY = "auth";// 사용자 권한 값의 KEY
    private static final String BEARER_PREFIX = "Bearer ";// Token 식별자
    private static final long TOKEN_TIME = 60 * 60 * 1000L;// 토큰 만료시간

    @Value("${jwt.secret.key}")//application.properties에 넣은 키값을 ${jwt.secret.key}로 작성하면 가져올 수 있음(?)
    private String secretKey;
    private Key key;//토큰을 만들때 넣어줄 Key값
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct//처음 객체를 생성할때 초기화 하는 함수관련
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }



    // header에서 토큰을 가져오기
    //jwt구현하기 3(from JwtUtil)//이후 같은 JwtUtil에 JWT생성 부분 만들기
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }


    // 토큰 생성
    //jwt구현하기 4(from JwtUtil)//이후 같은 JwtUtil에 토큰 검증 부분 만들기
    public String createToken(String username) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(username)//어떠한 공간에 username을 넣어준다(?)
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME))//토큰을 언제까지 유효하게 칠건지 지정(현재시간+TOKEN_TIME(?))
                        .setIssuedAt(date)//토큰이 언제 만들어 졌는지 넣어줌
                        .signWith(key, signatureAlgorithm)//Secret key를 사용해서 만든 key객체와 그 key객체를 어떤 알고리즘을 사용해서 암호화 할건지를 지정해주는 부분
                        .compact();
    }//string형식의 jwt토큰으로 반환(?)



    // 토큰 검증
    //jwt구현하기 5(from JwtUtil)//이후 같은 JwtUtil에 토큰에서 사용자 정보 가져오기 부분 만들기
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);//내부적으로 토큰을 검증해줌
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    // 토큰에서 사용자 정보 가져오기
    //jwt구현하기 6(from JwtUtil)//이후 ShopController로 이동
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }//검증과 비슷한데 getBody()를 통해 그안에 들어있는 정보들을 가지고 올 수 있음

}