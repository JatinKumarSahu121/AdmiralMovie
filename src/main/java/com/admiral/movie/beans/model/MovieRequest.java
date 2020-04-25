package com.admiral.movie.beans.model;

import lombok.Data;

//creating a separate request model since entities should not be used directly as request body
@Data
public class MovieRequest {
    private String title;
    private String category;
    private float rating;
}
