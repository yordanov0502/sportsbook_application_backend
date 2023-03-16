package com.example.sportsbook_application_backend.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class StatusResponse implements Response {
    private int statusCode;
    private String message;
    private String path;
}
