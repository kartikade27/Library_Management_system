package com.librart.managament.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "categories")
public class Category {
    @Id
    private String categoryId;

    private  String categoryName;
    @OneToMany(mappedBy = "category" ,cascade = CascadeType.ALL , orphanRemoval = true)
    private List<Book> book;

    @PrePersist
    void onCreate(){
        this.categoryId = UUID.randomUUID().toString();
    }

}
