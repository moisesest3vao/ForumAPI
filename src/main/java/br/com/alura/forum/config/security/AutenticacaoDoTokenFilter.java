package br.com.alura.forum.config.security;

import java.io.IOException;
import java.util.Optional;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import br.com.alura.forum.modelo.Usuario;
import br.com.alura.forum.repository.UsuarioRepository;

public class AutenticacaoDoTokenFilter extends OncePerRequestFilter {

	TokenService tokenService;
	
	UsuarioRepository usuarioRepository;
	
	public AutenticacaoDoTokenFilter(TokenService tokenService, UsuarioRepository usuarioRepository) {
		super();
		this.tokenService = tokenService;
		this.usuarioRepository = usuarioRepository;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String token = recuperarToken(request);
		
		if(tokenService.isTokenValid(token)) {
			autenticarUsuario(token);
		}
		filterChain.doFilter(request, response);
	}

	private void autenticarUsuario(String token) {
		Long id = tokenService.getIdUsuario(token);
		Optional<Usuario> opUsuario = usuarioRepository.findById(id);
		if(opUsuario.isPresent()) {
			Usuario usuario = opUsuario.get();
			UsernamePasswordAuthenticationToken authentication =  new UsernamePasswordAuthenticationToken(
					usuario, null, usuario.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		
		
	}

	private String recuperarToken(HttpServletRequest request) {
		String token = request.getHeader("Authorization");
		if(token==null || token.isEmpty() || !token.startsWith("Bearer ")) {
			return null;
		}
		
		return token.replace("Bearer ", "");
	}

}
