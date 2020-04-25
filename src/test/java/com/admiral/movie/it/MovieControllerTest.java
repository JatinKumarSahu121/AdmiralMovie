package com.admiral.movie.it;

import com.admiral.movie.beans.entity.Movie;
import com.admiral.movie.beans.model.MovieRequest;
import com.admiral.movie.beans.model.MovieUpdateRequest;
import com.admiral.movie.controller.MovieController;
import com.admiral.movie.service.MovieService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(MovieController.class)
public class MovieControllerTest {

    @MockBean
    private MovieService movieService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testCreateMovie() throws Exception{
        MovieRequest movieRequest = new MovieRequest();
        movieRequest.setCategory("Drama");
        movieRequest.setRating(4.9f);
        movieRequest.setTitle("Pursuit Of Happiness");
        Mockito.when(movieService.createOneMovie(ArgumentMatchers.any(MovieRequest.class))).thenReturn(getDummyMovieData());
        mockMvc.perform(post("/v1/movie")
                .content(new ObjectMapper().writeValueAsString(movieRequest).getBytes(StandardCharsets.UTF_8))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @Test
    public void testCreateMovieWithInvalidRatingAbove5() throws Exception{
        MovieRequest movieRequest = new MovieRequest();
        movieRequest.setCategory("Action");
        movieRequest.setRating(6.1f);
        movieRequest.setTitle("Rambo");
        Mockito.when(movieService.createOneMovie(ArgumentMatchers.any(MovieRequest.class))).thenReturn(getDummyMovieData());
        mockMvc.perform(post("/v1/movie")
                .content(new ObjectMapper().writeValueAsString(movieRequest).getBytes(StandardCharsets.UTF_8))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateMovieWithInvalidRatingBelow() throws Exception{
        MovieRequest movieRequest = new MovieRequest();
        movieRequest.setCategory("Action");
        movieRequest.setRating(0.1f);
        movieRequest.setTitle("Rambo");
        Mockito.when(movieService.createOneMovie(ArgumentMatchers.any(MovieRequest.class))).thenReturn(getDummyMovieData());
        mockMvc.perform(post("/v1/movie")
                .content(new ObjectMapper().writeValueAsString(movieRequest).getBytes(StandardCharsets.UTF_8))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetAllMovies() throws Exception{
        List<Movie> resultList = new ArrayList<>();
        resultList.add(getDummyMovieData());
        Mockito.when(movieService.getAllData()).thenReturn(resultList);
        mockMvc.perform(get("/v1/movie"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @Test
    public void testGetAllMoviesWithNoData() throws Exception{
        List<Movie> resultList = new ArrayList<>();
        Mockito.when(movieService.getAllData()).thenReturn(resultList);
        mockMvc.perform(get("/v1/movie"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is(204));
    }

    @Test
    public void testGetMovieById() throws Exception{
        Mockito.when(movieService.getOneMovie(ArgumentMatchers.any(UUID.class))).thenReturn(getDummyMovieData());
        mockMvc.perform(get("/v1/movie/"+UUID.randomUUID()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @Test
    public void testGetMovieByIdWithInvalidId() throws Exception{
        Mockito.when(movieService.getOneMovie(ArgumentMatchers.any(UUID.class))).thenReturn(null);
        mockMvc.perform(get("/v1/movie/"+UUID.randomUUID()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().is(204));
    }

    @Test
    public void testDeleteMovie() throws Exception{
        Mockito.when(movieService.deleteMovie(ArgumentMatchers.any(UUID.class))).thenReturn("deleted");
        mockMvc.perform(delete("/v1/movie/"+UUID.randomUUID()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteMovieWithInvalidId() throws Exception{
        Mockito.when(movieService.deleteMovie(ArgumentMatchers.any(UUID.class))).thenReturn(" ");
        mockMvc.perform(delete("/v1/movie/"+UUID.randomUUID()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateMovie() throws Exception{
        MovieUpdateRequest movieRequest = new MovieUpdateRequest();
        movieRequest.setCategory("Drama");
        movieRequest.setRating(4.9f);
        movieRequest.setTitle("Pursuit Of Happiness");
        Mockito.when(movieService.updateOneMovie(ArgumentMatchers.any(MovieUpdateRequest.class))).thenReturn(getDummyMovieData());
        mockMvc.perform(put("/v1/movie")
                .content(new ObjectMapper().writeValueAsString(movieRequest).getBytes(StandardCharsets.UTF_8))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk());

    }

    @Test
    public void testUpdateMovieWithInvalidId() throws Exception{
        MovieUpdateRequest movieRequest = new MovieUpdateRequest();
        movieRequest.setCategory("Drama");
        movieRequest.setRating(4.9f);
        movieRequest.setTitle("Pursuit Of Happiness");
        Mockito.when(movieService.updateOneMovie(ArgumentMatchers.any(MovieUpdateRequest.class))).thenReturn(null);
        mockMvc.perform(put("/v1/movie")
                .content(new ObjectMapper().writeValueAsString(movieRequest).getBytes(StandardCharsets.UTF_8))
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isBadRequest());
    }

    public static Movie getDummyMovieData(){
        Movie movie = new Movie();
        movie.setTitle("Pursuit Of Happiness");
        movie.setRating(4.9f);
        movie.setCategory("Drama");
        movie.setId(UUID.randomUUID().toString());
        return movie;
    }
}
