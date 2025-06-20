package com.dailycodebuffer.security.service;

import com.dailycodebuffer.security.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    // Token imzalamada kullanılacak gizli anahtar (Base64 formatında)
    private String secretKey = null;

    // Kullanıcı nesnesine göre JWT token üretir
    public String generateToken(User user) {

        // Token içine eklenecek ek bilgiler burada tutulur. Şu anda boş.
        Map<String, Object> claims = new HashMap<>();

        // JWT token üretimi başlıyor
        return Jwts
                .builder() // JWT builder nesnesi oluşturuluyor
                .claims() // token içine gömülecek ek bilgiler (payload).
                .add(claims) // boş claim'ler ekleniyor
                .issuer("Poyraz") // Bu token’ı oluşturan kim? Cevap: Poyraz
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 60 * 100 * 100000))
                .and() // claim bloğundan çıkılıyor
                .signWith(generateKey(), SignatureAlgorithm.HS256) // Algoritma belirtin
                .compact(); // Token string’e çevrilip döndürülüyor
    }

    // Gizli anahtarı üreten yardımcı metot
    private SecretKey generateKey() {
        // Gizli anahtar Base64 formatından byte dizisine çözülüyor
        byte[] decode = Decoders.BASE64.decode(getSecretKey());

        // JWT için HMAC algoritmasına uygun güvenli anahtar oluşturuluyor
        return Keys.hmacShaKeyFor(decode);
    }

    // Gizli anahtarı döndüren metot (şu an sabit yazılmış durumda)
    public String getSecretKey() {
        // Bu sabit secret key'i doğrudan burada tanımlamak yerine environment ya da application.properties'e almak daha güvenlidir
        return secretKey = "xI8e9NNh47kBSDdn2hPVJXIYJVV0HMo6njQnRSH9+nk=";
    }

    public String extractUserName(String token) {
    return extractClaims(token, Claims::getSubject);
    }

    private <T> T extractClaims(String token, Function<Claims,T> claimResolver) {
        Claims claims = extractClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractClaims(String token) {
        return Jwts
                .parser()
                .verifyWith(generateKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token){
        return extractClaims(token, Claims::getExpiration);
    }
}
