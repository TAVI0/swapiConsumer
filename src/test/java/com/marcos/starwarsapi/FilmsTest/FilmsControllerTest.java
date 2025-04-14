package com.marcos.starwarsapi.FilmsTest;

import com.marcos.starwarsapi.controller.FilmsController;
import com.marcos.starwarsapi.dto.FilmDTO;
import com.marcos.starwarsapi.service.FilmsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FilmsController.class)
class FilmsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FilmsService filmsService;

    @Test
    void testGetFilmById_ReturnsFilmDTO() throws Exception {
        FilmDTO filmDTO = new FilmDTO();
        filmDTO.setUid("1");
        filmDTO.setTitle("A New Hope");
        filmDTO.setEpisodeId(4);

        when(filmsService.getFilmById("1")).thenReturn(filmDTO);

        mockMvc.perform(get("/api/films/1").with(httpBasic("admin", "admin")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("A New Hope"))
                .andExpect(jsonPath("$.episodeId").value(4));

    }

    @Test
    void testGetFilmsByTitle_ReturnsList() throws Exception {
        FilmDTO dto = new FilmDTO();
        dto.setTitle("A New Hope");
        List<FilmDTO> list = Arrays.asList(dto);

        when(filmsService.getFilmsByTitle("Hope")).thenReturn(list);

        mockMvc.perform(get("/api/films/name/")
                .param("title", "Hope")
                .with(httpBasic("admin", "admin")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("A New Hope"));
    }

    @Test
    void testGetFilms_ReturnsEmpty() throws Exception {
        when(filmsService.getFilms(0, 0)).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/films")
                .with(httpBasic("admin", "admin")))
                .andExpect(status().isNoContent());
    }
}
