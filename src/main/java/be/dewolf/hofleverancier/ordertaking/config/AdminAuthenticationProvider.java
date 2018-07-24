package be.dewolf.hofleverancier.ordertaking.config;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collections;

public class AdminAuthenticationProvider implements AuthenticationProvider {
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        System.out.println("username: " + username);

        if (username.equals("adminuser") && password.equals("adminpassword")) {
            return new UsernamePasswordAuthenticationToken(username, password, Collections.singleton((GrantedAuthority) () -> "ROLE_ADMIN"));
        }

        return null;

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}