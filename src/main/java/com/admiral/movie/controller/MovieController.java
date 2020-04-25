package com.admiral.movie.controller;

import com.admiral.movie.beans.entity.Movie;
import com.admiral.movie.beans.model.MovieRequest;
import com.admiral.movie.beans.model.MovieUpdateRequest;
import com.admiral.movie.beans.response.ResponseWrapper;
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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseWrapper("Movie Rating should be between 0.5 and 5","fail",HttpStatus.BAD_REQUEST.value()));
        Movie savedResponse = movieService.createOneMovie(movieRequest);
        if(savedResponse!=null)
            return ResponseEntity.ok(new ResponseWrapper(savedResponse,"success",HttpStatus.OK.value()));
        else
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseWrapper("Unable to save, try again later.","fail",HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }

    @GetMapping
    public ResponseEntity<?> getAllMovies(){
        List<Movie> allMoviesList = movieService.getAllData();
        if(allMoviesList.size()>0)
            return ResponseEntity.ok(new ResponseWrapper(allMoviesList,"success",HttpStatus.OK.value()));
        else
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(new ResponseWrapper("No movies stored currently","success",HttpStatus.NO_CONTENT.value()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMovieById(@PathVariable("id") UUID id){
        Movie movie = movieService.getOneMovie(id);
        if(movie!=null)
            return ResponseEntity.ok(new ResponseWrapper(movie,"success",HttpStatus.OK.value()));
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseWrapper("No movie found with the given id","fail",HttpStatus.BAD_REQUEST.value()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMovie(@PathVariable("id") UUID id){
        String result = movieService.deleteMovie(id);
        if(result.equalsIgnoreCase("deleted"))
            return ResponseEntity.ok().body(new ResponseWrapper(result,"success",HttpStatus.OK.value()));
        else
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseWrapper(result,"fail",HttpStatus.BAD_REQUEST.value()));
    }

    @PutMapping
    public ResponseEntity<?> updateMovie(@RequestBody MovieUpdateRequest movieUpdateRequest){
        if(movieUpdateRequest.getRating()<0.5||movieUpdateRequest.getRating()>5)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseWrapper("Movie Rating should be between 0.5 and 5","fail",HttpStatus.BAD_REQUEST.value()));
        Movie updatedMovie = movieService.updateOneMovie(movieUpdateRequest);
        if(updatedMovie!=null)
            return ResponseEntity.ok(new ResponseWrapper(updatedMovie,"success",HttpStatus.OK.value()));
        else
            return ResponseEntity.badRequest().body(new ResponseWrapper("Movie with given id does not exist","fail",HttpStatus.BAD_REQUEST.value()));
    }
}
