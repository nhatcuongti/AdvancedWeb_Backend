package com.example.webadvanced_backend.requestentities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateSlideRequest {
    int preId;
    int slideType;
    String title; // your question
}
