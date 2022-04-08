package br.com.alura.forum.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import br.com.alura.forum.controller.dto.UsuarioDto;
import br.com.alura.forum.controller.form.UsuarioForm;
import br.com.alura.forum.modelo.Usuario;
import br.com.alura.forum.repository.TopicoRepository;
import br.com.alura.forum.repository.UsuarioRepository;

/* 
 * Implementação CRUD Usuário
 * Data: 07/04/2022 - 08/04/2022
 * Dev: Moisés Estevãos
 */

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

	@Autowired
	UsuarioRepository usuarioRepository;

	@Autowired
	TopicoRepository topicoRepository;

	@GetMapping
	public ResponseEntity<?> getUsuario(@RequestParam(required = false) Long id,
			@PageableDefault(sort = "id", direction = Direction.DESC, page = 0, size = 10) Pageable paginacao) {

		if (id != null) {
			Optional<Usuario> optional = usuarioRepository.findById(id);
			if (optional.isPresent()) {
				return ResponseEntity.ok(UsuarioDto.converter(optional.get()));
			}
			return ResponseEntity.notFound().build();
		}
		Page<Usuario> usuarios = usuarioRepository.findAll(paginacao);
		return ResponseEntity.ok(UsuarioDto.converter(usuarios));
	}

	@GetMapping("{id}")
	public ResponseEntity<?> detalhaUsuario(@PathVariable(required = false) Long id) {

		if (id != null) {
			Optional<Usuario> optional = usuarioRepository.findById(id);
			if (optional.isPresent()) {
				return ResponseEntity.ok(UsuarioDto.converter(optional.get()));
			}
		}
		
		return ResponseEntity.notFound().build();
	}
			
	@PostMapping
	@Transactional
	public ResponseEntity<?> addUsuario(@RequestBody(required = true) @Valid UsuarioForm form,
			UriComponentsBuilder uriBuilder) {
		if (!form.validar(usuarioRepository)) {
			return ResponseEntity.badRequest().body("email já registrado em nosso banco de dados");
		}
		Usuario usuario = form.toUsuario();
		usuarioRepository.save(usuario);

		URI uri = uriBuilder.path("/topicos/{id}").buildAndExpand(usuario.getId()).toUri();
		return ResponseEntity.created(uri).body(new UsuarioDto(usuario));
	}

	@PutMapping("/{id}")
	@Transactional
	public ResponseEntity<?> editaUsuario(@PathVariable Long id,
			@RequestBody(required = true) @Valid UsuarioForm form) {
		if (!form.validar(usuarioRepository)) {
			return ResponseEntity.badRequest().body("email já registrado em nosso banco de dados");
		}
		Optional<Usuario> optional = usuarioRepository.findById(id);
		if (optional.isPresent()) {
			Usuario usuario = optional.get();
			usuario.atualizar(form);

			return ResponseEntity.ok(new UsuarioDto(usuario));
		}
		return ResponseEntity.notFound().build();
	}

	/*
	 * Moisés: Método retorna Exception se o usuario de id = 1 tentar ser excluído
	 * Isso ocorre devido as chaves estrangeiras que este usuário possui atrelado
	 * aos tópicos que criou. Para excluir o usuário é necessário excluir todos os
	 * registros atrelados a ele utilizando HttpMethod.DELETE em /topicos/1,
	 * /topicos/2, /topicos/3
	 */
	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<?> deletaUsuario(@PathVariable Long id) {
		Optional<Usuario> optional = usuarioRepository.findById(id);
		if (optional.isPresent()) {
			usuarioRepository.delete(optional.get());
			return ResponseEntity.ok().build();
		}
		return ResponseEntity.notFound().build();
	}

}
