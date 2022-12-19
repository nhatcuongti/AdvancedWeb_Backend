package com.example.webadvanced_backend.requestentities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EditHeadingRequest {
    int slideTypeId;
    String heading;
    String subHeading;
}
