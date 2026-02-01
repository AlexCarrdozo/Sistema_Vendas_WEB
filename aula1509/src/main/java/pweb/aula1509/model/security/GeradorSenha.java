package pweb.aula1509.model.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *
 * @author fagno
 */
public class GeradorSenha {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println("----------------------------------------------------");
        System.out.println("Senha '1': " + encoder.encode("1"));
        System.out.println("Senha '123': " + encoder.encode("123"));
        System.out.println("----------------------------------------------------");
    }
}
