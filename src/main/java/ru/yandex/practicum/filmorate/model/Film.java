package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
@Builder
public class Film {

    private Long id;
    @NotNull
    private String name;
    @Size(min = 1, max = 200)
    private String description;
    @Min(1)
    private Long duration;
    private LocalDate releaseDate;

}
