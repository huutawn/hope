package com.llt.hope.entity;

import java.util.Set;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "ProductCategories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@JsonIgnoreProperties("productCategory")
public class ProductCategory extends AbstractEntity<Long> {
    //    @Id
    //    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //    @Column(name = "category_id", nullable = false)
    //    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    private String description;

    @OneToMany(mappedBy = "productCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private Set<Product> products;
}
