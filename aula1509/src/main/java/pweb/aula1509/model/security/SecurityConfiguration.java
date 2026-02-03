package pweb.aula1509.model.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    // 1. Injeção da sua classe de configuração de usuário (Banco de Dados)
    @Autowired
    UsuarioDetailsConfig usuarioDetailsConfig;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(
                        customizer ->
                                customizer
                                        .requestMatchers("/venda/minhasCompras",
                                                "/carrinho/loja", "/carrinho/adicionar/**", "/carrinho/listar",
                                                "/cliente/formFisico", "/cliente/formJuridico",
                                                "/cliente/saveFisico", "/cliente/saveJuridico").permitAll()

                                        .requestMatchers("/produto/**", "/venda/**", "/cliente/**").hasAnyRole("ADMIN")
                                        .anyRequest()
                                        .authenticated()
                )
                .formLogin(customizer ->
                        customizer
                                .loginPage("/login")
                                .defaultSuccessUrl("/carrinho/loja", true)
                                .permitAll()
                )
                .httpBasic(withDefaults())
                .logout(LogoutConfigurer::permitAll)
                .rememberMe(withDefaults());
        return http.build();
    }

    // 3. Método para configurar o AuthenticationManagerBuilder com sua classe personalizada
    @Autowired
    public void configureUserDetails(final AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(usuarioDetailsConfig).passwordEncoder(new BCryptPasswordEncoder());
    }

    // 4. O InMemoryUserDetailsManager foi removido pois agora usamos o método acima.

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}