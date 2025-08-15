package com.llt.hope.document;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.*;

@Document(indexName = "jobs")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class JobDocument {
    @Id
    private String id;
    
    private Long entityId; // ID from the original Job entity
    
    @Field(type = FieldType.Text, analyzer = "standard")
    private String title;
    
    @Field(type = FieldType.Text, analyzer = "standard") 
    private String description;
    
    @Field(type = FieldType.Text, analyzer = "standard")
    private String requirements;
    
    @Field(type = FieldType.Text, analyzer = "standard")
    private String responsibilities;
    
    @Field(type = FieldType.Text, analyzer = "standard")
    private String benefits;
    
    @Field(type = FieldType.Text, analyzer = "standard")
    private String suitableForDisability;
    
    @Field(type = FieldType.Keyword)
    private String location;
    
    @Field(type = FieldType.Double)
    private BigDecimal salaryMin;
    
    @Field(type = FieldType.Double)
    private BigDecimal salaryMax;
    
    @Field(type = FieldType.Date)
    private LocalDate applicationDeadline;
    
    @Field(type = FieldType.Boolean)
    private Boolean isActive;
    
    @Field(type = FieldType.Keyword)
    private String jobType;
    
    @Field(type = FieldType.Date)
    private LocalDate createdAt;
    
    @Field(type = FieldType.Date)
    private LocalDate updatedAt;
    
    @Field(type = FieldType.Integer)
    private Integer views;
    
    // Company fields for search
    @Field(type = FieldType.Long)
    private Long companyId;
    
    @Field(type = FieldType.Text, analyzer = "standard")
    private String companyName;
    
    @Field(type = FieldType.Text, analyzer = "standard")
    private String companyDescription;
    
    @Field(type = FieldType.Keyword)
    private String companyIndustry;
    
    @Field(type = FieldType.Keyword)
    private String companySize;
    
    @Field(type = FieldType.Keyword)
    private String companyLocation;
    
    // Employer fields
    @Field(type = FieldType.Text)
    private String employerId;
    
    @Field(type = FieldType.Keyword)
    private String employerEmail;
    
    // Job Category fields
    @Field(type = FieldType.Long)
    private Long jobCategoryId;
    
    @Field(type = FieldType.Keyword)
    private String jobCategoryName;
}
