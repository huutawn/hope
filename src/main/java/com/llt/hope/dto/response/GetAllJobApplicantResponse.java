package com.llt.hope.dto.response;

import java.util.List;

import com.llt.hope.entity.Job;
import com.llt.hope.entity.User;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetAllJobApplicantResponse {

    Job job;
    List<User> applicant;
}
