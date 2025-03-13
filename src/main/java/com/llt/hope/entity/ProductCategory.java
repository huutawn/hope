package com.llt.hope.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Table(name = "ProductCategories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ProductCategory extends AbstractEntity<Long>{
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "category_id", nullable = false)
//    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    private String description;
    @OneToMany(mappedBy = "productCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Product> products;
}
