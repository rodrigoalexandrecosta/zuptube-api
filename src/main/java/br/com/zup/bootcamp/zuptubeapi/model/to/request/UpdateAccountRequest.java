package br.com.zup.bootcamp.zuptubeapi.model.to.request;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdateAccountRequest {

    @NotBlank(message = "message.account.first-name.mandatory")
    @Length(min = 1, max = 255, message = "message.account.first-name.length")
    private String firstName;

    @NotBlank(message = "message.account.last-name.mandatory")
    @Length(min = 1, max = 255, message = "message.account.last-name.length")
    private String lastName;

    @Email(message = "message.account.email.invalid-format")
    @NotBlank(message = "message.account.email.mandatory")
    @Length(min = 1, max = 255, message = "message.account.email.length")
    private String email;

    @NotBlank(message = "message.account.phone.mandatory")
    @Pattern(regexp = "(\\+55|0)[0-9]{11}", message = "message.account.phone.invalid-format")
    private String phone;

    @NotBlank(message = "message.account.locale.mandatory")
    @Pattern(regexp = "([a-z]){2}-([A-Z]){2}", message = "message.account.locale.invalid-format")
    private String locale;

    @NotNull(message = "message.account.timezone.mandatory")
    @Pattern(regexp = "([A-Za-z])+/([A-Za-z_-])+", message = "message.account.timezone.invalid-format")
    private String timezone;


    public interface ValidationCreate {
    }
    public interface ValidationUpdate {
    }
}
