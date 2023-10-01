package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class User {

    private Long id;
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String login;
    private String name;
    @Past
    private LocalDate birthday;
    @JsonIgnore
    private Set<Long> friendsId;

    public boolean setFriendsId (Long friendId) {
        if (friendsId == null) {
            friendsId = new HashSet<Long>();
            return friendsId.add(friendId);
        }
        return friendsId.add(friendId);
    }

    public void removeFriend (Long id) {
        friendsId.remove(id);
    }

    public Set<Long> getFriendsId () {
        if (friendsId == null) {
            friendsId = new HashSet<Long>();
            return friendsId;
        }
        return friendsId;
    }
}
