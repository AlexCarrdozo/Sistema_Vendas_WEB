package pweb.aula1509.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pweb.aula1509.model.entity.ItemVenda;
import pweb.aula1509.model.entity.Pessoa;
import pweb.aula1509.model.entity.Venda;
import pweb.aula1509.model.repository.PessoaRepository;
import pweb.aula1509.model.repository.VendaRepository;

import java.time.LocalDate;

@Service
public class FinalizacaoVenda {

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private VendaRepository vendaRepository;

    @Transactional
    public void processarFinalizacao(Venda venda, String login) {

        Pessoa cliente = pessoaRepository.findByUsuarioLogin(login);

        if (cliente == null) {
            throw new RuntimeException("O usuário logado (" + login + ") não tem um cadastro de Pessoa associado.");
        }

        venda.setData(LocalDate.now());
        venda.setPessoa(cliente);

        if (venda.getItens() != null) {
            for (ItemVenda item : venda.getItens()) {
                item.setVenda(venda);
            }
        }

        vendaRepository.save(venda);
    }
}
