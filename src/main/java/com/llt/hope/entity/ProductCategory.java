package com.llt.hope.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@JsonIgnoreProperties("productCategory")
public class ProductCategory extends AbstractEntity<Long>{
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
