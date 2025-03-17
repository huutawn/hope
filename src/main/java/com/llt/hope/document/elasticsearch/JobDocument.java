package com.llt.hope.document.elasticsearch;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Id;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.*;

@Document(indexName = "jobs")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobDocument {
    @Id
    private String id;

    @Field(type = FieldType.Text)
    private String title;

    @Field(type = FieldType.Text)
    private String description;

    @Field(type = FieldType.Keyword)
    private String jobCategory;

    @Field(type = FieldType.Keyword)
    private String employerId;

    @Field(type = FieldType.Keyword)
    private String location;

    @Field(type = FieldType.Keyword)
    private String suitableForDisability;

    @Field(type = FieldType.Keyword)
    private String requirements;

    @Field(type = FieldType.Double)
    private BigDecimal salaryMin;

    @Field(type = FieldType.Double)
    private BigDecimal salaryMax;

    @Field(type = FieldType.Keyword)
    private String responsibilities;

    @Field(type = FieldType.Keyword)
    private String benefits;

    @Field(type = FieldType.Date)
    private LocalDateTime applicationDeadline;

    @Field(type = FieldType.Boolean)
    private Boolean isActive;

    @Field(type = FieldType.Date)
    private LocalDateTime createdAt;

    @Field(type = FieldType.Integer)
    private Integer views;
}
