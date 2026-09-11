package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.example.repository.Identifiable;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class Client implements Identifiable<UUID> {
    private UUID id;
    private String name;
    private int age;
}
