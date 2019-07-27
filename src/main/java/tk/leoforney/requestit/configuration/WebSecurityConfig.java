package tk.leoforney.requestit.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import tk.leoforney.requestit.service.CustomUserDetailsService;

@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    CustomizeAuthenticationSuccessHandler customizeAuthenticationSuccessHandler;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/VAADIN/**", "/HEARTBEAT/**", "/UIDL/**", "/resources/**"
                        , "/login", "/login**", "/login/**", "/manifest.json", "/icons/**", "/images/**",
                        // (development mode) static resources
                        "/frontend/**",
                        // (development mode) webjars
                        "/webjars/**",
                        // (development mode) H2 debugging console
                        "/h2-console/**",
                        // (production mode) static resources
                        "/frontend-es5/**", "/frontend-es6/**").permitAll()
                .antMatchers("/").permitAll()
                .antMatchers("/login").permitAll()
                .antMatchers("/signup").permitAll()
                .antMatchers("/party/**").permitAll()
                .antMatchers("/r/**").permitAll()
                .antMatchers("/RequestIt-1/**").permitAll()
                .antMatchers("/.well-known/**").permitAll()
                .antMatchers("/sessionSocket/**").permitAll()
                .antMatchers("/sessionSocketJs").permitAll()
                .antMatchers("/app/**").permitAll()
                .antMatchers("/user").permitAll()
                .anyRequest().authenticated()

                    .and()
                .formLogin().successHandler(customizeAuthenticationSuccessHandler)
                .loginPage("/login").failureUrl("/login?error=true")
                .usernameParameter("email")
                .passwordParameter("password")

                    .and()
                .httpBasic()

                    .and()
                .csrf().disable()

                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/").and().exceptionHandling();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/resources/**",
                        "/static/**",
                        "/css/**",
                        "/js/**",
                        "/images/**",
// Vaadin Flow static resources
                        "/VAADIN/**",

                        // the standard favicon URI
                        "/favicon.ico",

                        // web application manifest
                        "/manifest.json",

                        // icons and images
                        "/icons/**",
                        "/images/**",

                        // (development mode) static resources
                        "/frontend/**",

                        // (development mode) webjars
                        "/webjars/**",

                        // (development mode) H2 debugging console
                        "/h2-console/**",

                        // (production mode) static resources
                        "/frontend-es5/**", "/frontend-es6/**");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        UserDetailsService userDetailsService = mongoUserDetails();
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());

    }

    @Bean
    public UserDetailsService mongoUserDetails() {
        return new CustomUserDetailsService();
    }

}
