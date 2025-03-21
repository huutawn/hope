package com.llt.hope.dto.response;

import java.util.Set;

import com.llt.hope.entity.Profile;
import com.llt.hope.entity.Role;

//import com.llt.hope.entity.SellerProfile;
import com.llt.hope.entity.Seller;
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
    Seller seller;
    Set<Role> roles;
}
