package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode(of = "id")
public class Genre {
    private long id;
    private String name;
}