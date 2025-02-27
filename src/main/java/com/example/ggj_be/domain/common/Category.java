package com.example.ggj_be.domain.common;


import jakarta.persistence.*;
import lombok.*;


@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long categoryId;

    @Column(nullable = false)
    private String categoryName;
    
    
}
