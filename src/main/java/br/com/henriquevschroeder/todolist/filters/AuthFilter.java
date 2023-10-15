package br.com.henriquevschroeder.todolist.filters;

import java.io.IOException;
import java.util.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import at.favre.lib.crypto.bcrypt.BCrypt;
import br.com.henriquevschroeder.todolist.repositories.IUserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthFilter extends OncePerRequestFilter {
    
    @Autowired
    private IUserRepository userRepository;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {
        
        String servletPath = request.getServletPath();
        
        if (!servletPath.startsWith("/tasks")) {
            filterChain.doFilter(request, response);
            return;
        }
        
        String authHeader = request.getHeader("Authorization");
        
        String basicAuthEncoded = authHeader.substring("Basic".length()).trim();
        byte[] basicAuthDecoded = Base64.getDecoder().decode(basicAuthEncoded);
        
        String authString = new String(basicAuthDecoded);
        
        String[] credentials = authString.split(":");
        
        String username = credentials[0];
        String password = credentials[1];
        
        var user = this.userRepository.findByUsername(username);
        
        if (user == null) {
            response.sendError(401, "User is not authenticated");
            return;
        }
        
        var passwordVerify = BCrypt.verifyer().verify(password.toCharArray(), user.getPassword());
        
        if (!passwordVerify.verified) {
            response.sendError(401, "User is not authenticated");
            return;
        }
        
        request.setAttribute("userId", user.getId());

        filterChain.doFilter(request, response);
        
    }
    
}
