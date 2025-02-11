package com.ecommercetest.test.domain.device;

import com.ecommercetest.test.domain.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "device")
@Builder
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = true)
    private Long id;

    @Column(nullable = false)
    private DeviceType type;

    @Column(nullable = false)
    private DeviceState state;

    @Column(nullable = false)
    private String model;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;


    public Device(Long id, DeviceType type, DeviceState state, String model, User user) {
        this.id = id;
        this.type = type;
        this.state = state;
        this.model = model;
        this.user = user;
    }

    public Device() {
    }
}
