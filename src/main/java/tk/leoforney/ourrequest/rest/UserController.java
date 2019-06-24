package tk.leoforney.ourrequest.rest;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;

@RestController
public class UserController {

    @RequestMapping("/user")
    public Principal user(Principal principal) {
        return principal;
    }

    @RequestMapping(value = "/OurRequest-1/{wildcard}", method = RequestMethod.GET)
    String handleFoo(@PathVariable("wildcard") String route, HttpServletRequest request) throws IOException {
        System.out.println(route);
        return request.getContextPath();
    }

}
