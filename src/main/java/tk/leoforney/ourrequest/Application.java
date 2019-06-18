package tk.leoforney.ourrequest;

import org.springframework.beans.BeansException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import tk.leoforney.ourrequest.model.Role;
import tk.leoforney.ourrequest.model.User;
import tk.leoforney.ourrequest.repository.RoleRepository;
import tk.leoforney.ourrequest.service.CustomUserDetailsService;

@SpringBootApplication
public class Application extends SpringBootServletInitializer implements ApplicationContextAware {

    public static ApplicationContext appContext;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    CommandLineRunner init(RoleRepository roleRepository, CustomUserDetailsService customUserDetailsService) {

        return args -> {

            if (customUserDetailsService.findUserByEmail("loforney@gmail.com") == null) {
                User jumpstart = new User();
                jumpstart.setEmail("loforney@gmail.com");
                jumpstart.setEnabled(true);
                jumpstart.setFullname("Leo Forney");
                jumpstart.setPassword("daryleo1");
                customUserDetailsService.saveUser(jumpstart);
            }

            Role adminRole = roleRepository.findByRole("ADMIN");
            if (adminRole == null) {
                Role newAdminRole = new Role();
                newAdminRole.setRole("ADMIN");
                roleRepository.save(newAdminRole);
            }

            Role ownerRole = roleRepository.findByRole("OWNER");
            if (adminRole == null) {
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
