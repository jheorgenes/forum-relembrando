package br.com.alura.forum.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.alura.forum.modelo.Topico;

public interface TopicoRepository extends JpaRepository<Topico, Long>{
	
	// Curso é a entidade de relacionamento, e o nome é a entidade dentro do relacionamento.
	List<Topico> findByCursoNome(String nomeCurso);

	// Utilizando o padrão que quiser, inserindo o JPQL para consulta
//	@Query("SELECT t FROM Topico t WHERE t.curso.nome = :nomeCurso")
//	List<Topico> carregarPorNomeDoCurso(@Param("nomeCurso") String nomeCurso);
}
