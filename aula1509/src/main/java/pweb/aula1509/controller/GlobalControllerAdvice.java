package pweb.aula1509.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import pweb.aula1509.model.entity.Venda;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute("itensNoCarrinho")
    public int itensNoCarrinho(HttpSession session) {
        Venda venda = (Venda) session.getAttribute("vendaSession");

        if (venda == null || venda.getItens() == null) {
            return 0;
        }

        return venda.getItens().size();
    }
}