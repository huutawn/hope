package com.llt.hope.dto.response;

import java.math.BigDecimal;
import java.util.Set;

import com.llt.hope.entity.Profile;
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
    String email;
    String phone;
    Profile profile;
    Set<Role> roles;
    BigDecimal fund;
    Boolean accepted;
}
