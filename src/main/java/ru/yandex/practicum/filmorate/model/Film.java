package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class Film {

    private Integer id;
    @NotBlank
    @NonNull
    private final String name;
    @Size(max = 200)
    private final String description;
    @Min(1)
    private final Long duration;
    private final LocalDate releaseDate;

}
