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
public class TodoNoTypesDto implements ITodo {
    private Object id;
    private Object text;
    private Object completed;

    public static TodoNoTypesDto newTodo() {
        Random random = new Random();
        return TodoNoTypesDto.builder()
                .id(Math.abs(random.nextLong()))
                .text("")
                .completed(false)
                .build();
    }

    @Override
    public String toString() {
        return "{id: " + id + "; text: " + text + "; completed: " + completed + "}";
    }
}
