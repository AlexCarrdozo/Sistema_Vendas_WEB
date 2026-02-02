package pweb.aula1509.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pweb.aula1509.model.entity.PessoaFisica;
import pweb.aula1509.model.entity.PessoaJuridica;
import pweb.aula1509.model.entity.Usuario;
import pweb.aula1509.model.repository.PessoaFisicaRepository;
import pweb.aula1509.model.repository.PessoaJuridicaRepository;
import pweb.aula1509.model.repository.VendaRepository;

import java.util.Optional;

@Controller
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    PessoaFisicaRepository pessoaFisicaRepository;

    @Autowired
    PessoaJuridicaRepository pessoaJuridicaRepository;

    @Autowired
    VendaRepository vendaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/listClientes")
    public ModelAndView listarClientes(
            @RequestParam(name = "filtroNome", required = false) String filtroNome,
            ModelMap model) {

        if (filtroNome != null && !filtroNome.isEmpty()) {
            model.addAttribute("pessoasFisicas", pessoaFisicaRepository.findByNomeContainingIgnoreCase(filtroNome));
            model.addAttribute("pessoasJuridicas", pessoaJuridicaRepository.findByRazaoSocialContainingIgnoreCase(filtroNome));
        } else {
            model.addAttribute("pessoasFisicas", pessoaFisicaRepository.findAll());
            model.addAttribute("pessoasJuridicas", pessoaJuridicaRepository.findAll());
        }

        model.addAttribute("filtroNome", filtroNome);
        return new ModelAndView("/cliente/listClientes", model);
    }

    @GetMapping(value = {"/formFisico", "/formFisico/{id}"})
    public ModelAndView formPessoaFisica(
            @PathVariable(name = "id", required = false) Optional<Long> id,
            ModelMap model) {

        PessoaFisica pessoaFisica;
        if (id.isPresent()) {
            pessoaFisica = pessoaFisicaRepository.findById(id.get()).orElse(new PessoaFisica());
        } else {
            pessoaFisica = new PessoaFisica();
            pessoaFisica.setUsuario(new Usuario());
        }

        model.addAttribute("pessoaFisica", pessoaFisica);
        return new ModelAndView("/cliente/formPessoaFisica", model);
    }

    @PostMapping("/saveFisico")
    public String savePessoaFisica(@Valid PessoaFisica pessoaFisica, BindingResult result, Authentication authentication) {

        if (result.hasErrors()) {
            result.getAllErrors().forEach(error -> {
                System.out.println("Erro: " + error.getDefaultMessage());
                System.out.println("Campo: " + error.toString());
            });
            return "/cliente/formPessoaFisica";
        }

        pessoaFisica.getUsuario().setPassword(
                passwordEncoder.encode(pessoaFisica.getUsuario().getPassword())
        );

        pessoaFisicaRepository.save(pessoaFisica);

        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/cliente/listClientes";
        }

        return "redirect:/login";
    }

    @GetMapping("/deleteFisico/{id}")
    public String deletePessoaFisica(
            @PathVariable("id") Long id,
            RedirectAttributes redirectAttributes) {

        if (vendaRepository.vendasPorCliente(id, null).isEmpty()) {
            pessoaFisicaRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Cliente excluído com sucesso!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Não é possível excluir: Este cliente possui vendas associadas.");
        }
        return "redirect:/cliente/listClientes";
    }

    @GetMapping(value = {"/formJuridico", "/formJuridico/{id}"})
    public ModelAndView formPessoaJuridica(
            @PathVariable(name = "id", required = false) Optional<Long> id,
            ModelMap model) {

        PessoaJuridica pessoaJuridica;
        if (id.isPresent()) {
            pessoaJuridica = pessoaJuridicaRepository.findById(id.get()).orElse(new PessoaJuridica());
        } else {
            pessoaJuridica = new PessoaJuridica();
            pessoaJuridica.setUsuario(new Usuario());
        }

        model.addAttribute("pessoaJuridica", pessoaJuridica);
        return new ModelAndView("/cliente/formPessoaJuridica", model);
    }

    @PostMapping("/saveJuridico")
    public String savePessoaJuridica(@Valid PessoaJuridica pessoaJuridica, BindingResult result, Authentication authentication) {

        if (result.hasErrors()) {
            return "/cliente/formPessoaJuridica";
        }

        String senhaPura = pessoaJuridica.getUsuario().getPassword();
        String senhaCriptografada = passwordEncoder.encode(senhaPura);
        pessoaJuridica.getUsuario().setPassword(senhaCriptografada);

        pessoaJuridicaRepository.save(pessoaJuridica);

        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/cliente/listClientes";
        }

        return "redirect:/login";
    }

    @GetMapping("/deleteJuridico/{id}")
    public String deletePessoaJuridica(
            @PathVariable("id") Long id,
            RedirectAttributes redirectAttributes) {

        if (vendaRepository.vendasPorCliente(id, null).isEmpty()) {
            pessoaJuridicaRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Cliente excluído com sucesso!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Não é possível excluir: Este cliente possui vendas associadas.");
        }
        return "redirect:/cliente/listClientes";
    }
}