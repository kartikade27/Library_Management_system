package com.librart.managament.utility;

import com.librart.managament.model.Role;
import com.librart.managament.model.User;
import com.librart.managament.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AdminInitializer  implements CommandLineRunner {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;



    @Override
    public void run(String... args) throws Exception {
        Optional<User> admin = this.userRepository.findByEmail("kartikade27@gmail.com");

        if(admin.isEmpty()){
            User kartikAde = User.builder().fullName("kartik ade")
                    .email("kartikade27@gmail.com")
                    .phoneNumber("9076543521").password(this
                            .passwordEncoder.encode("Kartik$@&123"))
                    .role(Role.ADMIN).build();
            this.userRepository.save(kartikAde);
        }
    }
}
