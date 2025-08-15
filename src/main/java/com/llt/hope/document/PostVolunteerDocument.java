package com.llt.hope.document;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.*;

@Document(indexName = "post_volunteers")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PostVolunteerDocument {
    @Id
    private String id;
    
    private Long entityId; // ID from the original PostVolunteer entity
    
    @Field(type = FieldType.Text, analyzer = "standard")
    private String title;
    
    @Field(type = FieldType.Keyword)
    private String location;
    
    @Field(type = FieldType.Text, analyzer = "standard")
    private String content;
    
    @Field(type = FieldType.Keyword)
    private String stk;
    
    @Field(type = FieldType.Keyword)
    private String bankName;
    
    @Field(type = FieldType.Double)
    private BigDecimal fund;
    
    @Field(type = FieldType.Double)
    private BigDecimal totalAmount;
    
    @Field(type = FieldType.Date)
    private LocalDate createAt;
    
    @Field(type = FieldType.Date)
    private LocalDate updateAt;
    
    @Field(type = FieldType.Keyword)
    private String status;
    
    @Field(type = FieldType.Boolean)
    private boolean isActive;
    
    // User fields for search
    @Field(type = FieldType.Text)
    private String userId;
    
    @Field(type = FieldType.Keyword)
    private String userEmail;
    
    @Field(type = FieldType.Text, analyzer = "standard")
    private String userName;
    
    // File URLs for display (optional)
    @Field(type = FieldType.Keyword)
    private List<String> fileUrls;
}
