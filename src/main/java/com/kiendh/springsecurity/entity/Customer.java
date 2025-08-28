package com.kiendh.springsecurity.entity;

import com.kiendh.springsecurity.dto.enums.Gender;
import com.kiendh.springsecurity.dto.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "customer")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String fullName;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", columnDefinition = "varchar(20)")
    private Gender gender;

    @Column(name = "date_of_birth")
    private LocalDate dob;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "varchar(20)")
    private Status status;

    private String email;

    private String phoneNumber;
}
