package pweb.aula1509.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pweb.aula1509.model.entity.*;
import pweb.aula1509.model.repository.PessoaRepository;
import pweb.aula1509.model.repository.ProdutoRepository;
import pweb.aula1509.model.repository.VendaRepository;

import java.time.LocalDate;

@Controller
@RequestMapping("/carrinho")
public class CarrinhoController {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private PessoaRepository pessoaRepository;

    @Autowired
    private VendaRepository vendaRepository;

    @GetMapping("/loja")
    public ModelAndView loja(ModelMap model) {
        model.addAttribute("produtos", produtoRepository.findAll());
        return new ModelAndView("/venda/loja", model);
    }

    @GetMapping("/listar")
    public ModelAndView listar(HttpSession session, ModelMap model) {
        Venda venda = obterVendaDaSessao(session);

        model.addAttribute("itens", venda.getItens());
        model.addAttribute("total", venda.getTotal());
        model.addAttribute("clientes", pessoaRepository.findAll()); // Para o select de clientes na finalização

        return new ModelAndView("/venda/carrinho", model);
    }

    /**
     * Adiciona um item à Venda na sessão
     */
    @GetMapping("/adicionar/{id}")
    public String adicionar(@PathVariable("id") Long id, HttpSession session) {
        Produto produto = produtoRepository.findById(id).orElse(null);

        if (produto != null) {
            Venda venda = obterVendaDaSessao(session);

            ItemVenda item = new ItemVenda();
            item.setProduto(produto);
            item.setQuantidade(1.0);
            item.setValorUnitario(produto.getValor());

            venda.adicionarItem(item);
        }
        return "redirect:/carrinho/loja";
    }

    @GetMapping("/remover/{id}")
    public String remover(@PathVariable("id") Long id, HttpSession session) {
        Venda venda = obterVendaDaSessao(session);
        venda.removerItem(id);
        return "redirect:/carrinho/listar";
    }

    @PostMapping("/atualizar/{id}")
    public String atualizar(@PathVariable("id") Long id, @RequestParam("quantidade") Double quantidade, HttpSession session) {
        Venda venda = obterVendaDaSessao(session);
        venda.alterarQuantidade(id, quantidade);
        return "redirect:/carrinho/listar";
    }

    /**
     * Finaliza a venda: Pega da sessão -> Salva no Banco -> Limpa a sessão
     */
    @PostMapping("/finalizar")
    public String finalizar(HttpSession session, RedirectAttributes redirectAttributes) {

        Venda venda = obterVendaDaSessao(session);

        // 1. Pergunta ao Spring Security quem está logado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // Verifica se é um usuário anônimo (não logado)
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Você precisa estar logado para finalizar uma compra.");
            return "redirect:/login";
        }

        // 2. Pega o login (ex: "admin", "Alex")
        String login = auth.getName();

        // 3. Busca a pessoa no banco usando o login
        Pessoa cliente = pessoaRepository.findByUsuarioLogin(login);

        // 4. Valida se encontrou a pessoa
        if (cliente == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Erro: O usuário logado (" + login + ") não tem um cadastro de Pessoa associado.");
            return "redirect:/venda/minhasCompras";
        }

        // 5. Configura a venda
        venda.setData(LocalDate.now());
        venda.setPessoa(cliente);

        // 6. Amarra os itens à venda (para evitar erro de chave estrangeira)
        if (venda.getItens() != null) {
            for (ItemVenda item : venda.getItens()) {
                item.setVenda(venda);
            }
        }

        // 7. Salva tudo
        vendaRepository.save(venda);

        // 8. Limpa o carrinho da sessão
        session.removeAttribute("vendaSession");

        redirectAttributes.addFlashAttribute("successMessage", "Venda realizada com sucesso!");

        return "redirect:/venda/minhasCompras";
    }

    /**
     * Método auxiliar para gerenciar a sessão
     */
    private Venda obterVendaDaSessao(HttpSession session) {
        Venda venda = (Venda) session.getAttribute("vendaSession");
        if (venda == null) {
            venda = new Venda();
            session.setAttribute("vendaSession", venda);
        }
        return venda;
    }

    @GetMapping("/aumentar/{id}")
    public String aumentar(@PathVariable("id") Long id, HttpSession session) {
        Venda venda = obterVendaDaSessao(session);

        // Procura o item
        for (ItemVenda item : venda.getItens()) {
            if (item.getProduto().getId().equals(id)) {
                item.setQuantidade(item.getQuantidade() + 1.0);
                break;
            }
        }

        return "redirect:/carrinho/listar";
    }

    @GetMapping("/diminuir/{id}")
    public String diminuir(@PathVariable("id") Long id, HttpSession session) {
        Venda venda = obterVendaDaSessao(session);

        for (ItemVenda item : venda.getItens()) {
            if (item.getProduto().getId().equals(id)) {
                double novaQtd = item.getQuantidade() - 1.0;

                if (novaQtd <= 0) {
                    // Se ficar zero ou menos, remove o item
                    venda.removerItem(id);
                } else {
                    item.setQuantidade(novaQtd);
                }
                break;
            }
        }

        return "redirect:/carrinho/listar";
    }
}