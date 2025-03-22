package com.llt.hope.dto.response;


import com.llt.hope.entity.Profile;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SellerResponse {
	Long id;
	String phone;
	String email;
	String storeName;
	String storeDescription;
	boolean isActive;
	Profile profile;
	LocalDateTime createdAt;
	LocalDateTime updatedAt;
}
