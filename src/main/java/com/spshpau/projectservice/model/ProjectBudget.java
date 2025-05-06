package com.spshpau.projectservice.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "projectbudgets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProjectBudget {
    @Id
    @Column(nullable = false, unique = true, updatable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String currency;

    @Column(nullable = false)
    private float totalAmount;
}
