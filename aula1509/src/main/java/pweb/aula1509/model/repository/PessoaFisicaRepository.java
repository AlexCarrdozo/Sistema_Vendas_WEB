package pweb.aula1509.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pweb.aula1509.model.entity.PessoaFisica;

import java.util.List;

public interface PessoaFisicaRepository extends JpaRepository<PessoaFisica, Long> {

    @Query("FROM PessoaFisica pf WHERE LOWER(pf.nome) LIKE LOWER(CONCAT('%', :nome, '%'))")
    List<PessoaFisica> findByNomeContainingIgnoreCase(@Param("nome") String nome);
}