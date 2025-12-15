package pweb.aula1509.model.entity;

import jakarta.persistence.*;
import java.math.BigDecimal; // Importante
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Entity
public class Venda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate data = LocalDate.now();

    @OneToMany(mappedBy = "venda", cascade = CascadeType.ALL)
    private List<ItemVenda> itens = new ArrayList<>();

    @ManyToOne
    private Pessoa pessoa;

    // --- CÁLCULO TOTAL DA VENDA ---
    public BigDecimal getTotal() {
        if (itens == null || itens.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return itens.stream()
                .map(ItemVenda::getTotal) // Pega o total de cada item
                .filter(Objects::nonNull) // Evita nulos
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void adicionarItem(ItemVenda item) {
        Optional<ItemVenda> itemExistente = itens.stream()
                .filter(i -> i.getProduto().getId().equals(item.getProduto().getId()))
                .findFirst();

        if (itemExistente.isPresent()) {
            ItemVenda existente = itemExistente.get();
            existente.setQuantidade(existente.getQuantidade() + item.getQuantidade());
        } else {
            item.setVenda(this);
            itens.add(item);
        }
    }

    public void removerItem(Long produtoId) {
        itens.removeIf(i -> i.getProduto().getId().equals(produtoId));
    }

    public void alterarQuantidade(Long produtoId, Double novaQuantidade) {
        Optional<ItemVenda> item = itens.stream()
                .filter(i -> i.getProduto().getId().equals(produtoId))
                .findFirst();
        if(item.isPresent()) {
            if(novaQuantidade <= 0) {
                removerItem(produtoId);
            } else {
                item.get().setQuantidade(novaQuantidade);
            }
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public List<ItemVenda> getItens() {
        return itens;
    }

    public void setItens(List<ItemVenda> itens) {
        this.itens = itens;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }
}