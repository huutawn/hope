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

@Document(indexName = "posts")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PostDocument {
    @Id
    private String id;
    
    private Long entityId; // ID from the original Post entity
    
    @Field(type = FieldType.Text, analyzer = "standard")
    private String title;
    
    @Field(type = FieldType.Text, analyzer = "standard") 
    private String content;
    
    @Field(type = FieldType.Boolean)
    private boolean isPublished;
    
    @Field(type = FieldType.Boolean)
    private boolean isPinned;
    
    @Field(type = FieldType.Boolean)
    private boolean isActive;
    
    @Field(type = FieldType.Date)
    private LocalDate createdAt;
    
    @Field(type = FieldType.Date)
    private LocalDate updatedAt;
    
    @Field(type = FieldType.Keyword)
    private String type;
    
    @Field(type = FieldType.Double)
    private BigDecimal capital;
    
    @Field(type = FieldType.Integer)
    private Integer likes;
    
    // User fields for search
    @Field(type = FieldType.Text)
    private String userId;
    
    @Field(type = FieldType.Keyword)
    private String userEmail;
    
    @Field(type = FieldType.Text, analyzer = "standard")
    private String userName;
    
    // Image URLs for display (optional)
    @Field(type = FieldType.Keyword)
    private List<String> imageUrls;
}
