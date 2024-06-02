package dev.mutti.simplepaymentsapi.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "transactions")
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {

    @Id
    private long id;

    @Min(value = 1, message = "The value must be positive")
    @JsonProperty("payer")
    private long payerId;

    @Min(value = 1, message = "The value must be positive")
    @JsonProperty("payee")
    private long payeeId;

    @Min(value = 0, message = "The value must be positive")
    private double value;
}
