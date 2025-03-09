package com.llt.hope.dto.response;

import java.time.LocalDate;
import java.util.Set;

import com.llt.hope.entity.Role;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {
    String id;
    String username;
    String firstName;
    String lastName;
    LocalDate dob;
    String email;
    Set<Role> roles;
}
