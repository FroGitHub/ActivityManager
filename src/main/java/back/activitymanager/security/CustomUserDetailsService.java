package back.activitymanager.security;

import back.activitymanager.exception.EntityNotFoundException;
import back.activitymanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        return userRepository.findByEmailWithRoles(email)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can`t find user by email: " + email
                ));
    }
}
