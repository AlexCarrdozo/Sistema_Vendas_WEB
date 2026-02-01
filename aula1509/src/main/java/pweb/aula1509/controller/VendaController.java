package pweb.aula1509.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import pweb.aula1509.model.entity.Pessoa;
import pweb.aula1509.model.entity.Venda;
import pweb.aula1509.model.repository.PessoaRepository;
import pweb.aula1509.model.repository.VendaRepository;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/venda")
public class VendaController {

    @Autowired
    VendaRepository repository;

    @Autowired
    PessoaRepository pessoaRepository;

    @GetMapping("/list")
    public ModelAndView listar(@RequestParam(value = "data", required = false) LocalDate data, ModelMap model) {
        if (data != null) {
            model.addAttribute("vendas", repository.vendas(data));
            model.addAttribute("dataFiltro", data);
        } else {
            model.addAttribute("vendas", repository.findAll());
        }
        return new ModelAndView("venda/list", model);
    }

    @GetMapping("/detalhes/{id}")
    public ModelAndView detalhes(@PathVariable("id") Long id, ModelMap model) {
        model.addAttribute("venda", repository.venda(id));
        return new ModelAndView("venda/detalhes", model);
    }

    @GetMapping("/porCliente/{id}")
    public ModelAndView porCliente(@PathVariable("id") Long id,
                                   @RequestParam(value = "data", required = false) LocalDate data,
                                   ModelMap model) {

        Pessoa cliente = pessoaRepository.findById(id).orElse(null);

        if (cliente != null) {
            model.addAttribute("cliente", cliente);
            if (data != null) {
                model.addAttribute("vendas", repository.vendasPorCliente(id, data));
                model.addAttribute("dataFiltro", data);
            } else {
                model.addAttribute("vendas", repository.vendasPorCliente(id, null));
            }
        }

        return new ModelAndView("venda/listPorCliente", model);
    }

    @GetMapping("/minhasCompras")
    public ModelAndView minhasCompras() {
        // 1. Pega o login do usuário autenticado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        // Se não estiver logado, manda pro login
        if (auth == null || !auth.isAuthenticated() || "anonymousUser".equals(auth.getPrincipal())) {
            return new ModelAndView("redirect:/login");
        }

        String login = auth.getName();

        // 2. Busca as vendas desse usuário
        List<Venda> vendas = repository.findByPessoa_Usuario_Login(login);

        // 3. Retorna para a view
        ModelMap model = new ModelMap();
        model.addAttribute("vendas", vendas);

        return new ModelAndView("/venda/minhasCompras", model);
    }
}