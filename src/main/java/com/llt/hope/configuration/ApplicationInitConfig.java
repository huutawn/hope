package com.llt.hope.configuration;

import java.util.HashSet;

import com.llt.hope.entity.Profile;
import com.llt.hope.repository.jpa.ProfileRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.llt.hope.constant.PredefindRole;
import com.llt.hope.entity.Role;
import com.llt.hope.entity.User;
import com.llt.hope.repository.jpa.RoleRepository;
import com.llt.hope.repository.jpa.UserRepository;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;

    @NonFinal
    static final String ADMIN_USER_NAME = "admin";

    @NonFinal
    static final String ADMIN_PASSWORD = "admin";

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository, RoleRepository roleRepository, ProfileRepository profileRepository) {
        return args -> {
            if (userRepository.findByEmail(ADMIN_USER_NAME).isEmpty()) {
                roleRepository.save(Role.builder()
                        .name(PredefindRole.USER_ROLE)
                        .description("User role")
                        .build());

                Role admin = roleRepository.save(Role.builder()
                        .name(PredefindRole.ADMIN_ROLE)
                        .description("Admin role")
                        .build());

                var roles = new HashSet<Role>();
                roles.add(admin);





                User user = User.builder()
                        .email("admin")
                        .password(passwordEncoder.encode(ADMIN_PASSWORD))
                        .roles(roles)
                        .build();

                userRepository.save(user);

                Profile profile =Profile.builder()
                        .user(user)
                        .fullName("ADMIN")
                        .build();
                profileRepository.save(profile);

                user.setProfile(profile);
                userRepository.save(user);
                log.warn("admin user has been created with default password: admin, please change the password");
            }
            if (roleRepository.findById(PredefindRole.EMPLOYER_ROLE).isEmpty()) {
                Role employer = roleRepository.save(Role.builder()
                        .name(PredefindRole.EMPLOYER_ROLE)
                        .description("Employee role")
                        .build());
            }
            if (roleRepository.findById(PredefindRole.SELLER_ROLE).isEmpty()) {
                Role employer = roleRepository.save(Role.builder()
                        .name(PredefindRole.SELLER_ROLE)
                        .description("Selller role")
                        .build());
            }
        };
    }
}
