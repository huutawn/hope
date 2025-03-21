package com.llt.hope.dto.response;


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
	Boolean isActive;
	LocalDateTime createdAt;
	LocalDateTime updatedAt;
}
