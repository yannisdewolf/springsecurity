package be.dewolf.hofleverancier.ordertaking.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserAuthenticationProvider dummyAuthenticationProvider;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth)
            throws Exception {

        //werkt!!
        //configureCustomAuthenticationProviders(auth);

        //werkt!!
        //configureCustomUserDetailsService(auth);

        //werkt!!
        configureInMemory(auth);
    }

    private void configureCustomAuthenticationProviders(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(new UserAuthenticationProvider())
                .authenticationProvider(new AdminAuthenticationProvider());
    }

    private void configureCustomUserDetailsService(AuthenticationManagerBuilder auth) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(customUserDetailsService);
        auth.authenticationProvider(authenticationProvider);
    }

    private void configureInMemory(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                    .withUser("yannis")
                    .password("{noop}password")
                    .roles("USER")
                .and()
                    .withUser("amke")
                    .password("{noop}password")
                    .roles("USER")
                .and()
                    .withUser("adminuser")
                    .password("{noop}adminpassword")
                    .roles("ADMIN");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //.antMatcher("/api/**")
                .authorizeRequests()
                .antMatchers("/basicinfo").permitAll()
                .antMatchers("/publicdisclaimer").permitAll()
                .anyRequest().authenticated()
                //.antMatchers("/api/admin/**").hasRole("ADMIN")
                //.antMatchers("/api/user/**").hasRole("USER")
                .and().httpBasic();
    }
}
