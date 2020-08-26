package br.com.zup.bootcamp.zuptubeapi.model.entity;

import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Account {

    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String password;
    private String locale;
    private String timezone;
    private boolean removed;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
