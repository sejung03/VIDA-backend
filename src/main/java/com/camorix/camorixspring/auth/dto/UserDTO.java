package com.camorix.camorixspring.auth.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private String email;

    private String name;

    private UserRole role;
}
