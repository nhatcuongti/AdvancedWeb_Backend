package com.example.webadvanced_backend.responseentities;

import com.example.webadvanced_backend.models.UserGroup;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseListUserInGroup {
    List<UserGroup> userGroupList;
    UserGroup myAccountInGroup;
}
