package be.dewolf.hofleverancier.ordertaking.config;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Component
public class CustomUserDetailsService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("username: " + username);

        UserRecord foundUser = new DummyRepository().getUser(username);

        return new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return Arrays.asList((GrantedAuthority) () -> foundUser.getRole());
            }

            @Override
            public String getPassword() {
                return foundUser.getPassword();
            }

            @Override
            public String getUsername() {
                return foundUser.getUsername();
            }

            @Override
            public boolean isAccountNonExpired() {
                return true;
            }

            @Override
            public boolean isAccountNonLocked() {
                return true;
            }

            @Override
            public boolean isCredentialsNonExpired() {
                return true;
            }

            @Override
            public boolean isEnabled() {
                return true;
            }
        };
    }
}


class DummyRepository {

    List<UserRecord> userRecordList;

    public DummyRepository() {
        userRecordList = Arrays.asList(
                new UserRecord("usertestconfig", "{noop}password", "ROLE_USER"),
                new UserRecord("adminuser", "{noop}adminpassword", "ROLE_ADMIN")
        );
    }

    public UserRecord getUser(String userName) {
        return userRecordList.stream().filter(u -> u.getUsername().equals(userName)).findFirst().orElseThrow(() -> new UsernameNotFoundException("not found"));
    }
}

class UserRecord {
    String username;
    String password;
    String role;

    public UserRecord(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }
}