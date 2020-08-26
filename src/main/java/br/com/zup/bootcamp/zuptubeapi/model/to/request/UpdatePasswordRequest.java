package br.com.zup.bootcamp.zuptubeapi.model.to.request;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class UpdatePasswordRequest {

    @NotBlank(message = "message.account.password.mandatory")
    @Length(min = 6, max = 150, message = "message.account.password.length")
    private String password;


    public interface ValidationUpdate {
    }
}
