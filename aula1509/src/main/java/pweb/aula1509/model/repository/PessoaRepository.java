package pweb.aula1509.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pweb.aula1509.model.entity.Pessoa;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {
    Pessoa findByUsuarioLogin(String login);
}