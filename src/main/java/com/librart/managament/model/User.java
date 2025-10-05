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
@Table(name = "users")
public class User {

    @Id
    private String userId;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false , unique = true)
    private String email;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user" , cascade = CascadeType.ALL , orphanRemoval = true)
    private List<IssuedBook>issuedBooks;

    @PrePersist
    void onCreate(){
        this.userId = UUID.randomUUID().toString();
    }
}
