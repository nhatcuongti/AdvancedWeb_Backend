package com.example.webadvanced_backend.requestBodyEntities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeleteSlideTypeRequest {
    private int slideTypeId;
    private int slideType;
}
