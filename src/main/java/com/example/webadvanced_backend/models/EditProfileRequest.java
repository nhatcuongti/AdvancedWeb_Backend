package com.example.webadvanced_backend.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EditProfileRequest {
    private String img;
    private String fullname;
    private String oldPassword;
    private String newPassword;
    private String confirmNewPassword;
}
