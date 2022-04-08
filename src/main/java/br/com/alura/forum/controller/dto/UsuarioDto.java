package br.com.alura.forum.controller.dto;

import java.util.ArrayList;
import java.util.List;

import br.com.alura.forum.modelo.Usuario;

public class UsuarioDto {
	
	private Long id;
	private String nome;
	private String email;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public UsuarioDto(Usuario usuario) {
		this.email = usuario.getEmail();
		this.id = usuario.getId();
		this.nome = usuario.getNome();
	}

	public static List<UsuarioDto> converter(List<Usuario> usuarios) {
		List<UsuarioDto> usuariosDto = new ArrayList<>();
		
		for(int i=0; i < usuarios.size(); i++ ) {
			Usuario usuario = usuarios.get(i);
			usuariosDto.add(new UsuarioDto(usuario));
		}
		
		return usuariosDto;
	}

	public static UsuarioDto converter(Usuario usuario) {
		return new UsuarioDto(usuario);
	}

}
