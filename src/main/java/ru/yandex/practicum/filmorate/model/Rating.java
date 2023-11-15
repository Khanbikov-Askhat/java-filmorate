package ru.yandex.practicum.filmorate.model;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Builder
public
class Rating {
    private final long id;
    private String name;
}