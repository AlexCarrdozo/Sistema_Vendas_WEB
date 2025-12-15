package pweb.aula1509.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pweb.aula1509.model.entity.ItemVenda;

public interface ItemVendaRepository extends JpaRepository<ItemVenda, Long> {
    boolean existsByProdutoId(Long produtoId);
}