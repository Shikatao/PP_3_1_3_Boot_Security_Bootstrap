package ru.kata.spring.boot_security.demo.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.kata.spring.boot_security.demo.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private PasswordEncoder bCryptPasswordEncoder;
    public CustomUserDetailsService customUserDetailsService;
    private final SuccessUserHandler successUserHandler;

    public WebSecurityConfig(SuccessUserHandler successUserHandler, PasswordEncoder bCryptPasswordEncoder,
                             CustomUserDetailsService customUserDetailsService) {
        this.successUserHandler = successUserHandler;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    //Для того, чтобы разграничить доступ мы реализуем кастомную конфигурацию нашего http
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //Первое что я говорю, что мне необходимо авторизовать запрос следующим образом
                .authorizeRequests()

                .antMatchers("/user").hasAnyRole("USER", "ADMIN")
                .antMatchers("/admin/**").hasRole("ADMIN")

                .anyRequest().authenticated()

                .and()

                .formLogin()

                .successHandler(successUserHandler)
                .permitAll()
                .and()
                //с помощью logout() мы можем перенаправить  пользователей куда-то
                .logout()
                .permitAll();
    }

    // аутентификация inMemory
//    @Bean
//    @Override
//    public UserDetailsService userDetailsService() {
//        UserDetails user =
//                //PasswordEncoder — в нем задается, как шифровать пароль.
//                User.builder()
//                        .username("user")
//                        .password(passwordEncoder().encode("user"))
//                        .roles("USER")
//                        .build();
//
//        return new InMemoryUserDetailsManager(user);
//    }


}