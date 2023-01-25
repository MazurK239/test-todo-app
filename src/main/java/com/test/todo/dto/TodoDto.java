package com.test.todo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Random;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TodoDto {
    private Long id;
    private String text;
    private Boolean completed;

    public static TodoDto newTodo() {
        Random random = new Random();
        return TodoDto.builder()
                .id(Math.abs(random.nextLong()))
                .text("")
                .completed(false)
                .build();
    }
}
