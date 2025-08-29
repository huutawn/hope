package com.llt.hope.entity;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name; // Tên công ty

    @Column(columnDefinition = "TEXT")
    private String description; // Giới thiệu công ty


    private String industry; // Ngành nghề chính

    private String website; // Trang web công ty

    private String phoneNumber; // Số điện thoại liên hệ

    private String email; // Email liên hệ

    private String address; // Địa chỉ công ty

    private String size; // Quy mô công ty (Small, Medium, Large)

    private boolean isActive;

    @OneToOne
    @JsonBackReference
    private Profile profile; //

    @OneToOne
    @JoinColumn(name = "file_id")
    private MediaFile logo; // Ảnh logo công ty (Lưu trên Cloudinary)

    private String taxCode; // Mã số thuế (nếu có)

    @OneToMany(mappedBy = "company")
    @JsonManagedReference
    private List<Job> jobs; // Danh sách việc làm công ty đã đăng

    // Danh sách nhà tuyển dụng thuộc công ty này

    @Column(nullable = false)
    private LocalDate createdAt; // Ngày tạo công ty
}
