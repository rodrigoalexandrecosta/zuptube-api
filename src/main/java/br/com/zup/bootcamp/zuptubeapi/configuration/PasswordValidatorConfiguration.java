package br.com.zup.bootcamp.zuptubeapi.configuration;

import org.passay.LengthRule;
import org.passay.PasswordValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class PasswordValidatorConfiguration {

    @Bean
    public PasswordValidator passwordValidator() {

        // length between 6 and 150 characters
        LengthRule lengthRule = new LengthRule(6, 150);

        // at least one lower-case character
        //CharacterRule atLeastOneLowerCaseRule = new CharacterRule(EnglishCharacterData.LowerCase, 1);

        // at least one digit character
        //CharacterRule atLeastOneDigit = new CharacterRule(EnglishCharacterData.Digit, 1);

        return new PasswordValidator(lengthRule);
    }
}