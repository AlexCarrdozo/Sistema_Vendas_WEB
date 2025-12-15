package pweb.aula1509.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pweb.aula1509.model.entity.Produto;

import java.util.List;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    @Query("FROM Produto p WHERE LOWER(p.descricao) LIKE LOWER(CONCAT('%', :descricao, '%'))")
    List<Produto> findByDescricaoContainingIgnoreCase(@Param("descricao") String descricao);
}