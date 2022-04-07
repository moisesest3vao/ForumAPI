package br.com.alura.forum.config.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import br.com.alura.forum.modelo.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class TokenService {
	
	@Value("${forum.jwt.expiration}")
	String expiracao;
	@Value("${forum.jwt.secret}")
	String segredo;
	
	public String gerarToken(Authentication authentication) {
		Usuario logado = (Usuario)authentication.getPrincipal();
		Date now = new Date();
		Date expiration = new Date(now.getTime()+Long.parseLong(expiracao));
		
		return Jwts.builder()
				.setIssuer("API Fórum Alura")
				.setSubject(logado.getId().toString())
				.setIssuedAt(now)	
				.setExpiration(expiration)
				.signWith(SignatureAlgorithm.HS256, segredo)
				.compact();			
	}

	public boolean isTokenValid(String token) {
		//o método paseClaimsJws retorna Exceptions, por isso está dentro do try
		try {
			Jwts.parser().setSigningKey(this.segredo).parseClaimsJws(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public Long getIdUsuario(String token) {
		Claims body = Jwts.parser().setSigningKey(this.segredo).parseClaimsJws(token).getBody();
		return Long.parseLong(body.getSubject());

	}

}
