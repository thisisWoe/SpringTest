package com.ecommercetest.test.domain.provider;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "provider")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Provider {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;
}