package com.admiral.movie.ut;

import com.admiral.movie.beans.entity.Movie;
import com.admiral.movie.beans.model.MovieRequest;
import com.admiral.movie.beans.model.MovieUpdateRequest;
import com.admiral.movie.repository.MovieRepository;
import com.admiral.movie.service.MovieService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MovieServiceTest {

    @InjectMocks
    MovieService movieService;

    @Mock
    MovieRepository movieRepository;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testCreateOneMovie(){
        MovieRequest movieRequest = new MovieRequest();
        movieRequest.setTitle("Kill Bill");
        movieRequest.setRating(4.8f);
        movieRequest.setCategory("Action");
        Mockito.when(movieRepository.save(ArgumentMatchers.any(Movie.class))).thenReturn(getDummyMovieData());
        Movie movie = movieService.createOneMovie(movieRequest);
        Assert.assertNotNull(movie);
    }

    @Test
    public void testGetAllData(){
        List<Movie> allMoviesList = new ArrayList<>();
        Mockito.when(movieService.getAllData()).thenReturn(allMoviesList);
        allMoviesList.add(getDummyMovieData());
        List<Movie> resultList = movieService.getAllData();
        Assert.assertNotEquals(0,resultList.size());
    }

    @Test
    public void testGetOneMovie(){
        Mockito.when(movieRepository.findById(ArgumentMatchers.anyString())).thenReturn(Optional.of(getDummyMovieData()));
        Movie movie = movieService.getOneMovie(UUID.randomUUID());
        Assert.assertNotNull(movie);
    }

    @Test
    public void testGetOneMovieWithInvalidId(){
        Mockito.when(movieRepository.findById(ArgumentMatchers.anyString())).thenReturn(Optional.empty());
        Movie movie = movieService.getOneMovie(UUID.randomUUID());
        Assert.assertNull(movie);
    }

    @Test
    public void testDeleteMovie(){
        Mockito.when(movieRepository.findById(ArgumentMatchers.anyString())).thenReturn(Optional.of(getDummyMovieData()));
        String result = movieService.deleteMovie(UUID.randomUUID());
        Assert.assertEquals("deleted",result);
    }

    @Test
    public void testDeleteMovieWithInvalidId(){
        Mockito.when(movieRepository.findById(ArgumentMatchers.anyString())).thenReturn(Optional.empty());
        String result = movieService.deleteMovie(UUID.randomUUID());
        Assert.assertEquals("Invalid Movie Id",result);
    }

    @Test
    public void testUpdateOneMovie(){
        MovieUpdateRequest movieUpdateRequest = new MovieUpdateRequest();
        movieUpdateRequest.setId(UUID.randomUUID());
        movieUpdateRequest.setCategory("Action1");
        movieUpdateRequest.setRating(0.8f);
        movieUpdateRequest.setTitle("Green Lantern");

        Movie movieResult = new Movie();
        movieResult.setId(movieUpdateRequest.getId().toString());
        movieResult.setCategory(movieUpdateRequest.getCategory());
        movieResult.setRating(movieUpdateRequest.getRating());
        movieResult.setTitle(movieUpdateRequest.getTitle());

        Mockito.when(movieRepository.findById(ArgumentMatchers.anyString())).thenReturn(Optional.of(getDummyMovieData()));
        Mockito.when(movieRepository.save(ArgumentMatchers.any(Movie.class))).thenReturn(movieResult);

        Movie movie = movieService.updateOneMovie(movieUpdateRequest);
        Assert.assertEquals(movieResult.getTitle(),movie.getTitle());
    }

    @Test
    public void testUpdateOneMovieWithInvalidId(){
        MovieUpdateRequest movieUpdateRequest = new MovieUpdateRequest();
        movieUpdateRequest.setId(UUID.randomUUID());
        movieUpdateRequest.setCategory("Action1");
        movieUpdateRequest.setRating(0.8f);
        movieUpdateRequest.setTitle("Green Lantern");

        Mockito.when(movieRepository.findById(ArgumentMatchers.anyString())).thenReturn(Optional.empty());

        Movie movie = movieService.updateOneMovie(movieUpdateRequest);
        Assert.assertNull(movie);
    }

    public static Movie getDummyMovieData(){
        Movie movie = new Movie();
        movie.setTitle("Kill Bill");
        movie.setRating(4.8f);
        movie.setCategory("Action");
        movie.setId(UUID.randomUUID().toString());
        return movie;
    }
}
