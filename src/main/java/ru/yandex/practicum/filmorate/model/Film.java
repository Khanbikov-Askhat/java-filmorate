package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

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
    @JsonIgnore
    private Set<Long> filmLikes;

    public boolean setFilmLikes (Long friendId) {
        if (filmLikes == null) {
            filmLikes = new HashSet<Long>();
            return filmLikes.add(friendId);
        }
        return filmLikes.add(friendId);
    }

    public void removeLike (Long id) {
        filmLikes.remove(id);
    }

    public Set<Long> getFilmLikes () {
        if (filmLikes == null) {
            filmLikes = new HashSet<Long>();
            return filmLikes;
        }
        return filmLikes;
    }

}
