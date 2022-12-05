package com.example.webadvanced_backend.requestBodyEntities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreatePresentationRequest {
    private String presentationName;
    private String createdTime;
}
