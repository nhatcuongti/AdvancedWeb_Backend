package com.example.webadvanced_backend.requestentities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpgradeRoleRequest {
    String username;
    int groupId;
}
