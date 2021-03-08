package com.meowlomo.vmc.util;

import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JWTUtils {

	@Value("${meowlomo.security.jwt.key}")
	private String KEY;

	@Value("${meowlomo.config.vmc.connection.authentication.enable}")
	private boolean enableAuthentication;

	public String generateJWS(String input) {
		byte[] decodedKey = Base64.getDecoder().decode(KEY);
		SecretKey secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "SHA-512");

		String compactJws = Jwts.builder()
				.setSubject(input)
				.signWith(SignatureAlgorithm.HS512, secretKey)
				.compact();
		return compactJws;
	}

	public boolean isTokenValid(String jwt, String subject) {
	    if(!this.enableAuthentication) {
	        return true;
	    }
		if(jwt== null || subject == null) {
			return false;
		}
		else {
			byte[] decodedKey = Base64.getDecoder().decode(KEY);
			SecretKey secretKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "SHA-512");
			return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwt).getBody().getSubject().equals(subject);
		}
	}

}
