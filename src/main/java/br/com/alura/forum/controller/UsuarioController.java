package br.com.alura.forum.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.Validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.alura.forum.controller.dto.UsuarioDto;
import br.com.alura.forum.controller.form.UsuarioForm;
import br.com.alura.forum.modelo.Usuario;
import br.com.alura.forum.repository.UsuarioRepository;

//Dev: Moisés Estevão
//Data: 07/04/2022
//CRUD Usuario

@RestController
@RequestMapping("/usuario")
public class UsuarioController {
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	@GetMapping
	public ResponseEntity<?> getUsuario(@RequestParam(required = false) Long id){
	
		if(id != null) {
			Optional<Usuario> optional = usuarioRepository.findById(id);
			if(optional.isPresent()) {
				
				return ResponseEntity.ok(UsuarioDto.converter(optional.get()));
			}	
			return ResponseEntity.badRequest().build();
		} 
		List<Usuario> usuarios = usuarioRepository.findAll();
		return ResponseEntity.ok(UsuarioDto.converter(usuarios));
	}
	
	@PostMapping
	public ResponseEntity<?> addUsuario(@RequestBody(required = true) @Valid UsuarioForm form){
		
		Usuario usuario = form.toUsuario();
		usuarioRepository.save(usuario);
		
		return ResponseEntity.ok(new UsuarioDto(usuario));
	}
	
	
	

}
