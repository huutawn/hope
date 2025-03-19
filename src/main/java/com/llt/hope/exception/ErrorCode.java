package com.llt.hope.exception;

import org.apache.http.protocol.HTTP;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ErrorCode {
    USER_NOT_FOUND(404, "User not found", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(406, "User not existed", HttpStatus.NOT_FOUND),
    USERNAME_INVALID(407, "User name must have at least 3 characters", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(408, "Password must have at least 8 characters", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(409, "User is not authenticated", HttpStatus.FORBIDDEN),
    UNCATEGORIZED_EXCEPTION(410, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    UNAUTHORIZED_(411, "You do not has permission", HttpStatus.UNAUTHORIZED),
    USER_ALREADY_EXISTED(412, "User already exists", HttpStatus.CONFLICT),
    EMAIL_INVALID(414, "Email is invalid", HttpStatus.BAD_REQUEST),
    EMAIL_EXISTED(415, "Email already exists", HttpStatus.CONFLICT),
    TITLE_INVALID(416, "Title is invalid", HttpStatus.BAD_REQUEST),
    CONTENT_INVALID(417, "Content is invalid", HttpStatus.BAD_REQUEST),
    UPLOAD_FILE_ERROR(418, "Failed to upload file", HttpStatus.INTERNAL_SERVER_ERROR),
    REQUEST_INVALID(422, "Request is invalid", HttpStatus.BAD_REQUEST),
    CATEGORY_NOT_FOUND(423, "Category not found", HttpStatus.BAD_REQUEST),
    PROFILE_NOT_FOUND(424, "Profile not found", HttpStatus.BAD_REQUEST),
    CATEGORY_HAS_EXISTED(425, "Category has already existed", HttpStatus.CONFLICT),
    COMPANY_NOT_FOUND(427, "Company not found", HttpStatus.BAD_REQUEST),
    COMPANY_ALREADY_ACTIVE(428, "company is active", HttpStatus.CONFLICT),
    PRODUCT_HAS_EXISTED(429, "Product has already existed", HttpStatus.CONFLICT),
    COMPANY_IS_NOT_ACTIVE(430, "company is not active", HttpStatus.FORBIDDEN),
    PRODUCT_NOT_EXISTED(431, "Product not existed", HttpStatus.NOT_FOUND),
    POST_NOT_EXISTED(432, "Post not found", HttpStatus.BAD_REQUEST),
    SELLER_PROFILE_ALREADY_EXISTS(433, "", HttpStatus.CONFLICT),
    USER_NOT_ACTIVE(434,"" , HttpStatus.MULTI_STATUS),
    ORDER_HAS_EXISTED(435," " ,HttpStatus.CONFLICT )
    , ORDER_NOT_EXISTED(436, " ",HttpStatus.NOT_FOUND );
    int code;
    String message;
    HttpStatusCode httpStatusCode;
}
