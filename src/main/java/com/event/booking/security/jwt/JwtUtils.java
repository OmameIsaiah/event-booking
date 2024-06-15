package com.event.booking.security.jwt;

import com.event.booking.exceptions.AccessDeniedException;
import com.event.booking.repository.UserRepository;
import com.event.booking.security.SecurityUtils;
import com.event.booking.security.user.UserDetailsImpl;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.event.booking.security.user.UserDetailsImpl.getListOfPermissionsFromRoles;
import static com.event.booking.util.AppMessages.INVALID_ACCESS_TOKEN;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);
    @Value("${app.jwt.signing.key}")
    private String JWT_SECRET;
    @Value("${app.jwt.expirationDateInMs}")
    private int JWT_EXPIRATION_TIME_IN_MINUTES;
    @Value("${app.jwt.token.validity}")
    public long TOKEN_VALIDITY;
    private String ROLES_KEY = "roles";
    private String TOKEN_ISSUER = "Event Booking Ltd.";
    @Autowired
    private UserRepository userRepository;

    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        Map<String, Object> claims = new HashMap<>();
        claims.put("user-type", userPrincipal.getUserType());
        claims.put("sub", userPrincipal.getUsername());
        claims.put("name", userPrincipal.getName());
        claims.put("email", userPrincipal.getUsername());
        claims.put("issuer", TOKEN_ISSUER);
        claims.put(ROLES_KEY, getPermissionsData(userPrincipal));
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plus(TOKEN_VALIDITY, ChronoUnit.MINUTES)))
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();
    }

    public String generateJwtTokenWithOnlyRoleClaim(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        return Jwts.builder()
                .setSubject(userPrincipal.getUsername())
                .claim(ROLES_KEY, getAuthorityData(userPrincipal))
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plus(TOKEN_VALIDITY, ChronoUnit.MINUTES)))
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();
    }

    private String getAuthorityData(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }

    private String getAuthorityData(UserDetailsImpl userPrincipal) {
        return userPrincipal.getRoles().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }

    private String getRoles(String username) {
        return userRepository.findUserByEmail(username).get().getUserRoles().stream().filter(Objects::nonNull)
                .map(rs -> rs.getRoleid().getName()).collect(Collectors.joining(","));
    }

    private String getPermissionsData(UserDetailsImpl userPrincipal) {
        return userPrincipal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }

    private String getPermissions(String username) {
        return userRepository.findUserByEmail(username).get().getUserRoles().stream().filter(Objects::nonNull)
                .map(rs -> getListOfPermissionsFromRoles(rs.getRoleid())).collect(Collectors.joining(","));
    }

    public String getUserNameFromJwtToken(String token) {
        try {
            return Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(token).getBody().getSubject();
        } catch (Exception e) {
            throw new AccessDeniedException(INVALID_ACCESS_TOKEN);
        }
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        return (getUserNameFromJwtToken(token).equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private Boolean isTokenExpired(String token) {
        return getExpirationDateFromToken(token).before(new Date());
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(JWT_SECRET)
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(JWT_SECRET).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Invalid JWT signature: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }

    UsernamePasswordAuthenticationToken getAuthenticationToken(final String token, final Authentication existingAuth, final UserDetails userDetails) {
        final JwtParser jwtParser = Jwts.parser().setSigningKey(JWT_SECRET);
        final Jws<Claims> claimsJws = jwtParser.parseClaimsJws(token);
        final Claims claims = claimsJws.getBody();
        final Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(ROLES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
        logger.info("USER: {}, HAS THE FOLLOWING ROLES: {}", userDetails.getUsername(), claims.get(ROLES_KEY).toString());
        return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
    }
}
