package dev.mutti.simplepaymentsapi.domain;

import dev.mutti.simplepaymentsapi.enums.UserType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "users")
@AllArgsConstructor
@Getter
public class User {

    @Id
    private long id;

    @Indexed(unique = true)
    private String document;

    @Size(min = 3, max = 50, message = "Name must have between 3 and 50 characters")
    private String name;

    @Indexed(unique = true)
    private String email;

    @NotNull(message = "Password is required")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{8,}$", message = "Password must have at least 8 characters, one uppercase letter, one lowercase letter and one number")
    private String password;

    @NotNull(message = "Type is required")
    private UserType type;

    @Field("balance")
    private double balance;
}
