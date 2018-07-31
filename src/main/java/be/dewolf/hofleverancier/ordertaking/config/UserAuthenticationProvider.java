package be.dewolf.hofleverancier.ordertaking.config;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class UserAuthenticationProvider implements AuthenticationProvider {
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        System.out.println("username: " + username);

        if (username.equals("usertestconfig") && password.equals("password")) {
            System.out.println("user is logged in!");
            return new UsernamePasswordAuthenticationToken(username, password, Collections.singleton((GrantedAuthority) () -> "ROLE_USER"));
        }
        System.out.println("user is NOT logged in!");

        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
