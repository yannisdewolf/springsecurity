package be.dewolf.hofleverancier.ordertaking.config;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class DummyAuthenticationProvider implements AuthenticationProvider {
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        System.out.println("username: " + username);

        String role = username.equals("usertestconfig") && password.equals("password") ? "ROLE_USER" :
                username.equals("adminuser") && password.equals("adminpassword") ? "ROLE_ADMIN" :
                        "";

        return new UsernamePasswordAuthenticationToken(username, password, Collections.singleton((GrantedAuthority) () -> role));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
