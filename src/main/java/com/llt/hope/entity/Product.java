package com.llt.hope.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller;

    @NotBlank
    @Size(max = 100)
    private String name;

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal price;

    @NotBlank
    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private ProductCategory productCategory;

    private String creationProcess;
    private String materialsUsed;
    private String productionTime;

    @NotNull
    @Min(1)
    private Integer inventory;

    private BigDecimal weight;
    private String dimensions;
    private Boolean isCustomizable = false;
    private Boolean isFeatured = false;

    @Column(updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    private LocalDateTime updatedAt;
    private String imageUrl;
}
