package com.test.todo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Random;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class TodoDto implements ITodo {
    private Long id;
    private String text;
    private Boolean completed;

    public static TodoDto newTodo() {
        Random random = new Random();
        return TodoDto.builder()
                .id(Math.abs(random.nextLong()))
                .text("Randomly generated todo " + random.nextInt())
                .completed(false)
                .build();
    }

    @Override
    public String toString() {
        return "{id: " + id + "; text: " + text + "; completed: " + completed + "}";
    }
}
