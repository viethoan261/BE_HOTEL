package com.example.hotel.security.jwt;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.example.hotel.model.UserModel;
import com.example.hotel.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

@Component
public class JwtHelper {
    private String key = "test";
    private String prefix = "Bearer ";

    @Autowired
    private UserRepository userRepository;

    private static final Logger log = LoggerFactory.getLogger(JwtHelper.class);

    public String generateJwtToken(String username) {
        UserModel user = userRepository.findByUserName(username).get();
        Map<String, Object> claims = new HashMap<>();
        Set<String> userRoles = new HashSet<>();
        userRoles.add(user.getPosition().toString());

        claims.put("role",userRoles.toArray());

        Date currentDate = new Date();

        return Jwts.builder().setClaims(claims).setSubject(username).setIssuedAt(currentDate)
                .setExpiration(new Date(currentDate.getTime() + 86400000))
                .signWith(SignatureAlgorithm.HS512, "test").compact();
    }

    public boolean validate(String token) {
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(token);
            return true;

        } catch (UnsupportedJwtException e1) {
            log.error("JWT token is not supported: {}", e1);
        } catch (MalformedJwtException e2) {
            log.error("Invalid Token: {}", e2);
        } catch (SignatureException e3) {
            log.error("Invalid signature: {}", e3);
        } catch (ExpiredJwtException e4) {
            log.error("JWT is expired: {}", e4);
        } catch (IllegalArgumentException e5) {
            log.error("JWT Claims is empty: {}", e5);
        }

        return false;
    }

    public String getToken(HttpServletRequest request) {

        String jwt = request.getHeader("Authorization");
        if (jwt == null)
            return jwt;
        return jwt.substring(prefix.length(), jwt.length());

    }

    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody().getSubject();
    }
}

