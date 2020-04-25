package com.admiral.movie.controller;

import com.admiral.movie.beans.entity.Movie;
import com.admiral.movie.beans.model.MovieRequest;
import com.admiral.movie.beans.model.MovieUpdateRequest;
import com.admiral.movie.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("v1/movie")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @PostMapping
    public ResponseEntity<?> createMovie(@RequestBody MovieRequest movieRequest){
        //adding validation here to give desired http status code with the message
        if(movieRequest.getRating()<0.5||movieRequest.getRating()>5)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Movie Rating should be between 0.5 and 5");
        Movie savedResponse = movieService.createOneMovie(movieRequest);
        if(savedResponse!=null)
            return ResponseEntity.ok(savedResponse);
        else
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unable to save, try again later.");
    }

    @GetMapping
    public ResponseEntity<?> getAllMovies(){
        List<Movie> allMoviesList = movieService.getAllData();
        if(allMoviesList.size()>0)
            return ResponseEntity.ok(allMoviesList);
        else
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No movies stored currently");

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMovieById(@PathVariable("id") UUID id){
        Movie movie = movieService.getOneMovie(id);
        if(movie!=null)
            return ResponseEntity.ok(movie);
        else
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No movie found with the given id");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMovie(@PathVariable("id") UUID id){
        String result = movieService.deleteMovie(id);
        if(result.equalsIgnoreCase("deleted"))
            return ResponseEntity.ok().body(result);
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
    }

    @PutMapping
    public ResponseEntity<?> updateMovie(@RequestBody MovieUpdateRequest movieUpdateRequest){
        Movie updatedMovie = movieService.updateOneMovie(movieUpdateRequest);
        if(updatedMovie!=null)
            return ResponseEntity.ok(updatedMovie);
        else
            return ResponseEntity.badRequest().body("Movie with given id does not exist");
    }
}
