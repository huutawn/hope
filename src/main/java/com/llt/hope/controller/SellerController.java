package com.llt.hope.controller;


import com.llt.hope.dto.request.SellerCreationRequest;
import com.llt.hope.dto.response.ActiveCompanyResponse;
import com.llt.hope.dto.response.ApiResponse;
import com.llt.hope.dto.response.PageResponse;
import com.llt.hope.dto.response.SellerResponse;
import com.llt.hope.entity.Seller;
import com.llt.hope.service.SellerService;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sellerProfile")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class SellerController {
	SellerService sellerService;

	@PostMapping
	public ApiResponse<SellerResponse> createSellerProfile(
			@Valid @RequestBody SellerCreationRequest request) {
		return ApiResponse.<SellerResponse>builder()
				.result(sellerService.createSeller(request))
				.build();
	}
	@GetMapping("/nonactive")
    public ApiResponse<PageResponse<SellerResponse>> getAllSellerProfileNonActive(
            @Filter Specification<Seller> spec,

            @RequestParam(value = "page", required = false, defaultValue = "1") int page,

            @RequestParam(value = "size", required = false, defaultValue = "10") int size
    ){
        return ApiResponse.<PageResponse<SellerResponse>>builder()
                .result(sellerService.getAllSellerProfiles(false,page,size))
                .build();
    }
	@GetMapping("/active")
	public ApiResponse<PageResponse<SellerResponse>> getAllSellerProfileActive(
			@Filter Specification<Seller> spec,

			@RequestParam(value = "page", required = false, defaultValue = "1") int page,

			@RequestParam(value = "size", required = false, defaultValue = "10") int size
	){
		return ApiResponse.<PageResponse<SellerResponse>>builder()
				.result(sellerService.getAllSellerProfiles(true,page,size))
				.build();
	}
	@DeleteMapping("/{sellerId}")
	public ApiResponse<String> deleteSellerProfile(@PathVariable Long Id) {
		sellerService.deleteSellerProfile(Id);
		return ApiResponse.<String>builder()
				.result("Profile deleted successfully")
				.build();
	}
	@PatchMapping("/{sellerId}/activate")
		public ApiResponse<ActiveCompanyResponse> activateSellerProfile(@PathVariable Long sellerId) {
			return ApiResponse.<ActiveCompanyResponse>builder()
					.result(sellerService.activeSeller(sellerId))
					.build();
		}

}
