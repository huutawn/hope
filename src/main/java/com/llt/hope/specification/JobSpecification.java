package com.llt.hope.specification;

import java.math.BigDecimal;

import org.springframework.data.jpa.domain.Specification;

import com.llt.hope.entity.Job;

public class JobSpecification {

    public static Specification<Job> hasCategory(String categoryName) {
        return (root, query, criteriaBuilder) -> {
            if (categoryName == null || categoryName.isEmpty()) {
                return null;
            }
            return criteriaBuilder.equal(root.get("jobCategory").get("name"), categoryName);
        };
    }

    public static Specification<Job> hasRequirements(String requirement) {
        return (root, query, criteriaBuilder) -> {
            if (requirement == null || requirement.isEmpty()) {
                return null;
            }
            return criteriaBuilder.like(root.get("requirements"), "%" + requirement + "%");
        };
    }

    public static Specification<Job> salaryBetween(BigDecimal min, BigDecimal max) {
        return (root, query, criteriaBuilder) -> {
            if (min == null && max == null) {
                return null;
            }
            if (min == null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("salaryMin"), max);
            }
            if (max == null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("salaryMax"), min);
            }
            return criteriaBuilder.between(root.get("salaryMin"), min, max);
        };
    }
}
