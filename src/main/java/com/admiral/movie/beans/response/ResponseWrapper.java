package com.admiral.movie.beans.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseWrapper {
    private Object data;
    private String status;
    private int statusCode;
}
