package pweb.aula1509.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pweb.aula1509.model.entity.Produto;
import pweb.aula1509.model.repository.ItemVendaRepository;
import pweb.aula1509.model.repository.ProdutoRepository;
import java.util.Optional;

@Controller
@RequestMapping("/produto")
public class ProdutoController {

    @Autowired
    ProdutoRepository produtoRepository;

    @Autowired
    ItemVendaRepository itemVendaRepository;

    @GetMapping("/list")
    public ModelAndView listar(
            @RequestParam(name = "filtroDescricao", required = false) String filtroDescricao,
            ModelMap model) {

        if (filtroDescricao != null && !filtroDescricao.isEmpty()) {
            model.addAttribute("produtos", produtoRepository.findByDescricaoContainingIgnoreCase(filtroDescricao));
        } else {
            model.addAttribute("produtos", produtoRepository.findAll());
        }

        model.addAttribute("filtroDescricao", filtroDescricao);
        return new ModelAndView("/produto/list", model);
    }

    @GetMapping(value = {"/form", "/form/{id}"})
    public ModelAndView form(
            @PathVariable(name = "id", required = false) Optional<Long> id,
            ModelMap model) {

        Produto produto;
        if (id.isPresent()) {
            produto = produtoRepository.findById(id.get()).orElse(new Produto());
        } else {
            produto = new Produto();
        }

        model.addAttribute("produto", produto);
        return new ModelAndView("/produto/form", model);
    }

    @PostMapping("/save")
    public String save(@Valid Produto produto, BindingResult result) {
        if (result.hasErrors()) {
            return "/produto/form";
        }

        produtoRepository.save(produto);
        return "redirect:/produto/list";
    }

    @GetMapping("/delete/{id}")
    public String delete(
            @PathVariable("id") Long id,
            RedirectAttributes redirectAttributes) {

        if (itemVendaRepository.existsByProdutoId(id)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Não é possível excluir: Este produto está associado a vendas.");
        } else {
            produtoRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Produto excluído com sucesso!");
        }
        return "redirect:/produto/list";
    }
}