package pweb.aula1509.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pweb.aula1509.model.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    public Usuario findByLogin(String login);

}
