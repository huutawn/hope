package com.llt.hope.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.FieldDefaults;

@NoArgsConstructor
@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class SellerCreationRequest {
	@Pattern(regexp = "^\\d{10,11}$", message = "PHONE_INVALID")
	String phone;
	@Email(message = "EMAIL_INCORRECT")
	String email;
	String storeName;
	String storeDescription;
}
