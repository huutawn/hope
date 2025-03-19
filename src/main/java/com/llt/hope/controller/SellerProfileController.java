/*
package com.llt.hope.controller;


@RestController
@RequestMapping("/sellerProfile")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class SellerProfileController {
	SellerProfileService sellerProfileService;

	@PostMapping
	public ApiResponse<SellerProfileResponse> createSellerProfile(
			@ModelAttribute SellerProfileCreationRequest request) {
		return ApiResponse.<SellerProfileResponse>builder()
				.result(sellerProfileService.createSeller(request))
				.build();
	}
}
*/
