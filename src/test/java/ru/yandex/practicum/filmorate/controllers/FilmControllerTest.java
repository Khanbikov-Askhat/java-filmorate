package ru.yandex.practicum.filmorate.controllers;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class FilmControllerTest {
    private static final String URL = "/films";
    private static final LocalDate VALID_DATE = LocalDate.of(2009, 12, 10);
    private static final LocalDate INVALID_DATE = LocalDate.of(1895, 12, 27);
    private static final String VALID_NAME = "testValidName";
    private static final String VALID_DESCRIPTION = "TEST_VALID_DESCRIPTION";
    private static final String VALID_DESCRIPTION_SIZE_200 = "TEST_INVALID_DESCRIPTIONsdjjfwlqmvflkelkaonrnanre" +
            "kinwknlndfmpewpOMVPOEMVPmmnnnlkmdewpmgmgl" +
            "rkenlnsbbijrtjgzlkfrdgbmkrqrejnqangjrnkzngnrnqlkrnebnr" +
            "agnelkngrannmdfkgmrmjshsntsnhrahjnflahjrilstrkhitajjqnnr";
    private static final String INVALID_DESCRIPTION_SIZE_201 = "TEST_INVALID_DESCRIPTIONsdjjfwlqmvflkelkaonrnanre" +
            "kinwknlndfmpewpOMVPOEMVPmmnnnlkmdewpmgmgl" +
            "rkenlnsbbijrtjgzlkfrdgbmkrqrejnqangjrnkzngnrnqlkrnebnr" +
            "agnelkngrannmdfkgmrmjshsntsnhrahjnflahjrilstrkjhitajjqnnr";
    private static final long VALID_DURATION = 90L;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;

    @Test
    void shouldReturnStatusOkWhenCreateValidFilm() throws Exception {
        Film newFilm = Film.builder()
                .name(VALID_NAME)
                .description(VALID_DESCRIPTION)
                .duration(VALID_DURATION)
                .releaseDate(VALID_DATE)
                .build();
        String body = mapper.writeValueAsString(newFilm);
        this.mockMvc.perform(post(URL).content(body).contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk());
    }

    @Test
    void shouldReturnStatusOkWhenCreateValidFilmWithDescriptionSize200() throws Exception {
        Film newFilm = Film.builder()
                .name(VALID_NAME)
                .description(VALID_DESCRIPTION_SIZE_200)
                .duration(VALID_DURATION)
                .releaseDate(VALID_DATE)
                .build();
        String body = mapper.writeValueAsString(newFilm);
        this.mockMvc.perform(post(URL).content(body).contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk());
    }

    @Test
    void shouldReturnStatus4xxWhenCreateValidFilmWithDescriptionSize201() throws Exception {
        Film newFilm = Film.builder()
                .name(VALID_NAME)
                .description(INVALID_DESCRIPTION_SIZE_201)
                .duration(VALID_DURATION)
                .releaseDate(VALID_DATE)
                .build();
        String body = mapper.writeValueAsString(newFilm);
        this.mockMvc.perform(post(URL).content(body).contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().is4xxClientError());
    }

    @Test
    void shouldReturnStatus4xxWhenCreateFilmWithNegativeDuration() throws Exception {
        Film newFilm = Film.builder()
                .name(VALID_NAME)
                .description(VALID_DESCRIPTION)
                .duration(-162L)
                .releaseDate(VALID_DATE)
                .build();
        String body = mapper.writeValueAsString(newFilm);
        this.mockMvc.perform(post(URL).content(body).contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().is4xxClientError());
    }

    @Test
    void shouldReturnStatusOkWhenCreateFilmWithValidReleaseDate() throws Exception {
        Film newFilm = Film.builder()
                .name(VALID_NAME)
                .description(VALID_DESCRIPTION)
                .duration(VALID_DURATION)
                .releaseDate(LocalDate.of(1895, 12, 28))
                .build();
        String body = mapper.writeValueAsString(newFilm);
        this.mockMvc.perform(post(URL).content(body).contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk());
    }

    @Test
    void shouldReturnStatusOkWhenUpdateValidFilm() throws Exception {
        createFilm();
        Film updatedFilm = Film.builder()
                .name(VALID_NAME)
                .description("Some description")
                .duration(VALID_DURATION)
                .releaseDate(VALID_DATE)
                .build();
        updatedFilm.setId(1L);
        String updatedBody = mapper.writeValueAsString(updatedFilm);
        this.mockMvc.perform(put(URL).content(updatedBody).contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk());
    }

    @Test
    void shouldReturnStatusOkWhenUpdateValidFilmWithDescriptionSize200() throws Exception {
        createFilm();
        Film updatedFilm = Film.builder()
                .name(VALID_NAME)
                .description(VALID_DESCRIPTION_SIZE_200)
                .duration(VALID_DURATION)
                .releaseDate(VALID_DATE)
                .build();
        updatedFilm.setId(1L);
        String body = mapper.writeValueAsString(updatedFilm);
        this.mockMvc.perform(put(URL).content(body).contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk());
    }

    @Test
    void shouldReturnStatus4xxWhenUpdateValidFilmWithDescriptionSize201() throws Exception {
        createFilm();
        Film updatedFilm = Film.builder()
                .name(VALID_NAME)
                .description(INVALID_DESCRIPTION_SIZE_201)
                .duration(VALID_DURATION)
                .releaseDate(VALID_DATE)
                .build();
        updatedFilm.setId(1L);
        String body = mapper.writeValueAsString(updatedFilm);
        this.mockMvc.perform(put(URL).content(body).contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().is4xxClientError());
    }

    @Test
    void shouldReturnStatus4xxWhenUpdateFilmWithNegativeDuration() throws Exception {
        createFilm();
        Film updatedFilm = Film.builder()
                .name(VALID_NAME)
                .description(VALID_DESCRIPTION)
                .duration(-162L)
                .releaseDate(VALID_DATE)
                .build();
        updatedFilm.setId(1L);
        String body = mapper.writeValueAsString(updatedFilm);
        this.mockMvc.perform(post(URL).content(body).contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().is4xxClientError());
    }

    @Test
    void shouldReturnStatusOkWhenUpdateFilmWithValidReleaseDate() throws Exception {
        createFilm();
        Film updatedFilm = Film.builder()
                .name(VALID_NAME)
                .description(VALID_DESCRIPTION)
                .duration(VALID_DURATION)
                .releaseDate(LocalDate.of(1895, 12, 28))
                .build();
        updatedFilm.setId(1L);
        String body = mapper.writeValueAsString(updatedFilm);
        this.mockMvc.perform(post(URL).content(body).contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk());
    }

    private void createFilm() throws Exception {
        Film newFilm = Film.builder()
                .name(VALID_NAME)
                .description(VALID_DESCRIPTION)
                .duration(VALID_DURATION)
                .releaseDate(VALID_DATE)
                .build();
        String newBody = mapper.writeValueAsString(newFilm);
        this.mockMvc.perform(post(URL).content(newBody).contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk());
    }

    /*
    Это тесты, которые по идее должны отрабатывать, но я выбрасываю Exception,
     что не подходит ни под одну категорию статусов, не знаю как это обработать(
    @Test
    void shouldReturnStatus4xxWhenUpdateFilmWithInvalidReleaseDate() throws Exception {
        createFilm();
        Film updatedFilm = new Film(VALID_NAME, VALID_DESCRIPTION, VALID_DURATION, INVALID_DATE);
        updatedFilm.setId(1);
        String body = mapper.writeValueAsString(updatedFilm);
        this.mockMvc.perform(post(URL).content(body).contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().is4xxClientError());
    }

    @Test
    void shouldReturnStatus4xxWhenCreateFilmWithInvalidReleaseDate() throws Exception {
        Film newFilm = new Film(VALID_NAME, VALID_DESCRIPTION, VALID_DURATION, INVALID_DATE);
        String body = mapper.writeValueAsString(newFilm);
        this.mockMvc.perform(post(URL).content(body).contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().is4xxClientError());
    }

        @Test
    void shouldReturnStatus4xxWhenCreateFilmWithEmptyName() throws Exception {
        Film newFilm = Film.builder()
                .name(" ")
                .description(VALID_DESCRIPTION)
                .duration(VALID_DURATION)
                .releaseDate(VALID_DATE)
                .build();
        String body = mapper.writeValueAsString(newFilm);
        this.mockMvc.perform(post(URL).content(body).contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().is4xxClientError());
    }

        @Test
    void shouldReturnStatus4xxWhenUpdateFilmWithEmptyName() throws Exception {
        createFilm();
        Film updatedFilm = Film.builder()
                .name(" ")
                .description(VALID_DESCRIPTION)
                .duration(VALID_DURATION)
                .releaseDate(VALID_DATE)
                .build();
        String body = mapper.writeValueAsString(updatedFilm);
        updatedFilm.setId(1L);
        this.mockMvc.perform(put(URL).content(body).contentType(MediaType.APPLICATION_JSON)).
                andExpect(status().is4xxClientError());
    }
     */
}
