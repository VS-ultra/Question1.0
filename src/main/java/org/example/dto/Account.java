package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.example.repository.Identifiable;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class Account implements Identifiable<UUID> {
    @NonNull
    private UUID id;
    @NonNull
    private UUID clientId;
    @NonNull
    private final String currency;
    @Setter
    @NonNull
    private volatile BigDecimal balance;


}
