package com.admiral.movie.service;

import com.admiral.movie.beans.entity.Movie;
import com.admiral.movie.beans.model.MovieRequest;
import com.admiral.movie.beans.model.MovieUpdateRequest;
import com.admiral.movie.repository.MovieRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MovieService {

    @Autowired
    MovieRepository movieRepository;

    public Movie createOneMovie(MovieRequest movieRequest){
        Movie movie = new Movie();
        BeanUtils.copyProperties(movieRequest,movie);
        return movieRepository.save(movie);
    }

    public List<Movie> getAllData(){
        return movieRepository.findAll();
    }

    public Movie getOneMovie(UUID id){
        Optional<Movie> movie = movieRepository.findById(id.toString());
        return movie.orElse(null);
    }

    public String deleteMovie(UUID id){
        Optional<Movie> movie = movieRepository.findById(id.toString());
        if(movie.isPresent()) {
            movieRepository.deleteById(id.toString());
            return "deleted";
        }else
            return "Invalid Movie Id";
    }

    public Movie updateOneMovie(MovieUpdateRequest movieUpdateRequest){
        Optional<Movie> movieOptionalData = movieRepository.findById(movieUpdateRequest.getId().toString());
        if(movieOptionalData.isPresent()){
            Movie movie = movieOptionalData.get();
            if(movieUpdateRequest.getCategory()!=null)
                movie.setCategory(movieUpdateRequest.getCategory());
            if(movieUpdateRequest.getRating()!=null)
                movie.setRating(movieUpdateRequest.getRating());
            if(movieUpdateRequest.getTitle()!=null)
                movie.setTitle(movieUpdateRequest.getTitle());
            return movieRepository.save(movie);
        }else return null;
    }
}
