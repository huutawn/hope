package com.llt.hope.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Products")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@JsonIgnoreProperties("products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    User seller;

    @NotBlank
    @Size(max = 100)
    String name;

    @NotNull
    @DecimalMin("0.0")
    BigDecimal price;

    @NotBlank
    String description;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    @JsonBackReference
    ProductCategory productCategory;

    String creationProcess;
    String materialsUsed;
    String productionTime;

    @NotNull
    @Min(1)
    Integer inventory;

    BigDecimal weight;
    String dimensions;
    Boolean isCustomizable = false;
    Boolean isFeatured = false;

    @Column(updatable = false)
     LocalDateTime createdAt = LocalDateTime.now();

     LocalDateTime updatedAt;
     String imageUrl;
}
