package br.com.zup.bootcamp.zuptubeapi.features.account;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomPasswordEncoder implements PasswordEncoder {

    private final PasswordEncoder passwordEncoder;

    boolean matches(String plainPassword, String accountPassword) {
        return this.passwordEncoder.matches(plainPassword, accountPassword);
    }

    @Override
    public String encode(CharSequence rawPassword) {
        return this.passwordEncoder.encode(rawPassword);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return this.passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
