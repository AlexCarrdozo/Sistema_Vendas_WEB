package pweb.aula1509.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pweb.aula1509.model.entity.PessoaJuridica;

import java.util.List;

public interface PessoaJuridicaRepository  extends JpaRepository<PessoaJuridica, Long> {

    @Query("FROM PessoaJuridica pj WHERE LOWER(pj.razaoSocial) LIKE LOWER(CONCAT('%', :razaoSocial, '%'))")
    List<PessoaJuridica> findByRazaoSocialContainingIgnoreCase(@Param("razaoSocial") String razaoSocial);
}