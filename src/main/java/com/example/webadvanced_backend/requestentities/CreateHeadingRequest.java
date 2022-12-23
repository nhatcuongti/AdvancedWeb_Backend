package com.example.webadvanced_backend.requestentities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


// create option in multichoice
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateHeadingRequest {
    int contentId;
    String heading;
    String subHeading;
}
