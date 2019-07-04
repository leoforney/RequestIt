package tk.leoforney.requestit.rest;

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

    @RequestMapping(value = "/RequestIt-1/{wildcard}", method = RequestMethod.GET)
    void handleRedirect(@PathVariable("wildcard") String route, HttpServletRequest request, HttpServletResponse response) throws IOException {
        //System.out.println(request.getRequestURI());
        //String[] split = request.getRequestURL().toString().split("/RequestIt-1/");
        //System.out.println(request.getScheme() + "://" + request.getServerName() + ":" + request.);
        response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);
        response.setHeader("Location", getURL(request));
    }

    public static String getURL(HttpServletRequest req) {

        String scheme = req.getScheme();             // http
        String serverName = req.getServerName();     // hostname.com
        int serverPort = req.getServerPort();        // 80
        //String contextPath = req.getContextPath();   // /mywebapp
        String servletPath = req.getServletPath().replace("/RequestIt-1", "");   // /servlet/MyServlet
        String pathInfo = req.getPathInfo();         // /a/b;c=123
        String queryString = req.getQueryString();          // d=789

        // Reconstruct original requesting URL
        StringBuilder url = new StringBuilder();
        url.append(scheme).append("://").append(serverName);

        if (serverPort != 80 && serverPort != 443) {
            url.append(":").append(serverPort);
        }

        url.append(servletPath);

        if (pathInfo != null) {
            url.append(pathInfo);
        }
        if (queryString != null) {
            url.append("?").append(queryString);
        }
        return url.toString();
    }

}
