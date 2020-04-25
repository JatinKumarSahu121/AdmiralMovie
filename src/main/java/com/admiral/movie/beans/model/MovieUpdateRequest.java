package com.admiral.movie.beans.model;

import lombok.Data;

import java.util.UUID;

//creating a separate request model since entities should not be used directly as request body
@Data
public class MovieUpdateRequest {
    private UUID id;
    private String title;
    private String category;
    private Float rating;
}
