package br.com.alura.forum.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.alura.forum.controller.dto.DetalhesDoTopicoDto;
import br.com.alura.forum.controller.dto.TopicoDto;
import br.com.alura.forum.controller.form.AtualizacaoTopicoForm;
import br.com.alura.forum.controller.form.TopicoForm;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repository.CursoRepository;
import br.com.alura.forum.repository.TopicoRepository;

@RestController
@RequestMapping("/topicos")
public class TopicosController {
	
	@Autowired
	private TopicoRepository topicoRepository;
	
	@Autowired
	private CursoRepository cursoRepository;
	
	@GetMapping
	public List<TopicoDto> lista(String nomeCurso){
		if (nomeCurso == null) {
			List<Topico> topicos = topicoRepository.findAll();
			return TopicoDto.converter(topicos);
		} else {
			List<Topico> topicos = topicoRepository.findByCursoNome(nomeCurso);
			return TopicoDto.converter(topicos);
		}
	}
	
	@PostMapping
	@Transactional
	public ResponseEntity<TopicoDto> cadastrar(
			@RequestBody //definindo que topicoForm será recebido como corpo da requisição
			@Valid //definindo que topicoForm será validado pelo BeanValidation
			TopicoForm topicoForm, 
			UriComponentsBuilder uriBuilder //Objeto que carrega o caminho da URI até antes do recurso (injetável automaticamente)
		){
		Topico topico =  topicoForm.converter(cursoRepository);
		topicoRepository.save(topico);
		
		URI uri = uriBuilder.path("/topicos/{id}") //Especificando o caminho do recurso 
							.buildAndExpand(topico.getId()) //passa o parâmetro que será identificado como id do path
							.toUri(); //converte em uma URI completa.
		
		/* ResponseEntity.created 
		 * Necessita que seja retornado o status, 
		 * O cabeçalho HTTP "location" com a URL do novo recurso 
		 * E no corpo (body) da requisição deve conter a representação do recurso */
		return ResponseEntity.created(uri) //cria a resposta com status code 201 (recebe a uri como parâmetro) e re
							 .body(new TopicoDto(topico)); //devolvendo um corpo na resposta contendo o novo topicoDto (recebendo o topico como parametro)
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<DetalhesDoTopicoDto> detalhar(@PathVariable Long id) {
		Optional<Topico> topico = topicoRepository.findById(id);
		if(topico.isPresent()) {
			return ResponseEntity.ok(new DetalhesDoTopicoDto(topico.get()));
		}
		return ResponseEntity.notFound().build();
	}
	
	@PutMapping("/{id}")
	@Transactional //Necessário para disparar o commit após finalizar a atualização.
	public ResponseEntity<TopicoDto> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizacaoTopicoForm atualizaTopicoForm){
		Optional<Topico> optional = topicoRepository.findById(id);
		if(optional.isPresent()) {
			Topico topico = atualizaTopicoForm.atualizar(id, topicoRepository); //Essa linha já atualiza no banco de dados automaticamente
			return ResponseEntity.ok(new TopicoDto(topico));
		}
		return ResponseEntity.notFound().build();
	}
	
	@DeleteMapping("/{id}")
	@Transactional
	public ResponseEntity<?> remover(@PathVariable Long id){ //Retorno ResponseEntity não tem retorno, por isso foi especificado genérico
		Optional<Topico> optional = topicoRepository.findById(id);
		if(optional.isPresent()) {
			topicoRepository.deleteById(id); //Deletando do banco de dados
			return ResponseEntity.ok().build(); //Retornando uma resposta (status code 200) sem corpo da requisição e mandando buildar (executar)
		}
		return ResponseEntity.notFound().build();
	}
}
