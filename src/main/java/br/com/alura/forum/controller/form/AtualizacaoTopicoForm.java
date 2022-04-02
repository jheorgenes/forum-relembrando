package br.com.alura.forum.controller.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repository.TopicoRepository;

public class AtualizacaoTopicoForm {
	
	@NotNull @NotEmpty @Length(min = 5)
	private String titulo;
	
	@NotNull @NotEmpty @Length(min = 10)
	private String mensagem;

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public Topico atualizar(Long id, TopicoRepository topicoRepository) { //Recebe como serviço o TopicoRepository para não precisar efetuar injeção de dependência
		Topico topico = topicoRepository.getById(id); //Consulta no banco de dados
		topico.setTitulo(this.titulo); //Sobrescrevendo o titulo
		topico.setMensagem(this.mensagem); //Sobrescrevendo a mensagem
		return topico;
	}
}
