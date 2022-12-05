package com.example.webadvanced_backend.requestBodyEntities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


// create option in multichoice
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateMChoiceOptionRequest {
    int contentId;
    String optionName;
}
