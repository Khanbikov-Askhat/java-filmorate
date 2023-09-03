package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
public class User {
    public User(Integer id, String email, String login, @NonNull String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    private Integer id;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String login;
    @NonNull
    private String name;
    @Past
    private LocalDate birthday;
}
