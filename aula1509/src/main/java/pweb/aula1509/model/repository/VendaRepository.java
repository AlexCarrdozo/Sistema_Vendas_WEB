package pweb.aula1509.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pweb.aula1509.model.entity.Venda;

import java.time.LocalDate;
import java.util.List;

public interface VendaRepository extends JpaRepository<Venda, Long> {

    @Query("FROM Venda v WHERE :data IS NULL OR v.data = :data")
    List<Venda> vendas(@Param("data") LocalDate data);

    @Query("FROM Venda v WHERE v.id = :id")
    Venda venda(@Param("id") Long id);

    @Query("FROM Venda v WHERE v.pessoa.id = :id AND (:data IS NULL OR v.data = :data)")
    List<Venda> vendasPorCliente(@Param("id") Long id, @Param("data") LocalDate data);
}