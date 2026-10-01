package org.example.ENUM;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TaskPriority {
    HIGH(0),
    LOW(1);
    private final int order;
}