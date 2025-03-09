package com.llt.hope.exception;

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
    COURSE_ALREADY_SIGNUP(413, "course already signup", HttpStatus.BAD_REQUEST),
    EMAIL_INVALID(414, "Email is invalid", HttpStatus.BAD_REQUEST),
    EMAIL_EXISTED(415, "Email already exists", HttpStatus.CONFLICT),
    COURSE_NOT_FOUND(418, "Course not found", HttpStatus.NOT_FOUND),
    LESSON_NOT_FOUND(422, "Course not found", HttpStatus.NOT_FOUND),
    EXERCISE_NOT_FOUND(423, "Exercises not found", HttpStatus.NOT_FOUND),
    EXERCISE_NOT_ENOUGH_QUESTIONS(424, "Not enough questions in exercise", HttpStatus.BAD_REQUEST),
    EXERCISE_ANSWER_NOT_CORRECT(425, "Incorrect answer", HttpStatus.BAD_REQUEST),
    QUESTION_NOT_FOUND(426, "Question not found", HttpStatus.NOT_FOUND),
    ANSWER_OPTION_NOT_FOUND(427, "Answer option not found", HttpStatus.NOT_FOUND);
    int code;
    String message;
    HttpStatusCode httpStatusCode;
}
