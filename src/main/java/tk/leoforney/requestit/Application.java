package tk.leoforney.requestit;

import org.springframework.beans.BeansException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import tk.leoforney.requestit.model.Role;
import tk.leoforney.requestit.model.User;
import tk.leoforney.requestit.repository.RoleRepository;
import tk.leoforney.requestit.service.CustomUserDetailsService;

import java.util.Arrays;
import java.util.HashSet;

@SpringBootApplication
public class Application extends SpringBootServletInitializer implements ApplicationContextAware {

    public static ApplicationContext appContext;

    public static void main(String[] args) {
        System.setProperty("server.servlet.context-path", "");
        SpringApplication.run(Application.class, args);
    }

    @Bean
    CommandLineRunner init(RoleRepository roleRepository, CustomUserDetailsService customUserDetailsService) {

        return args -> {

            if (customUserDetailsService.findUserByEmail("loforney@gmail.com") == null) {
                User jumpstart = new User();
                jumpstart.setEmail("loforney@gmail.com");
                jumpstart.setFullname("Leo Forney");
                jumpstart.setPassword("daryleo1");
                Role userRole = roleRepository.findByRole("OWNER");
                jumpstart.setRoles(new HashSet<>(Arrays.asList(userRole)));
                customUserDetailsService.saveUser(jumpstart);
            }

            Role adminRole = roleRepository.findByRole("ADMIN");
            if (adminRole == null) {
                Role newAdminRole = new Role();
                newAdminRole.setRole("ADMIN");
                roleRepository.save(newAdminRole);
            }

            Role ownerRole = roleRepository.findByRole("OWNER");
            if (ownerRole == null) {
                Role newOwnerRole = new Role();
                newOwnerRole.setRole("OWNER");
                roleRepository.save(newOwnerRole);
            }

            Role userRole = roleRepository.findByRole("USER");
            if (userRole == null) {
                Role newUserRole = new Role();
                newUserRole.setRole("USER");
                roleRepository.save(newUserRole);
            }
        };

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        appContext = applicationContext;
    }
}
