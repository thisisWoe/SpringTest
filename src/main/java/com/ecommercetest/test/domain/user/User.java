package com.ecommercetest.test.domain.user;

import com.ecommercetest.test.domain.device.Device;
import com.ecommercetest.test.domain.provider.Provider;
import com.ecommercetest.test.domain.role.Role;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "users")
@Builder
@ToString
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = true)
    private Long id;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @Column(nullable = true)
    private String fullName;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = true, unique = true)
    private String email;

    /**
     * Questo campo viene utilizzato SOLTANTO per gli utenti
     * che fanno registrazione/login locale.
     * Se l'utente è OAuth2, può essere lasciato null o stringa vuota.
     */
    @Column(nullable = true)
    private String password;

    @ManyToOne
    @JoinColumn(name = "provider_id", nullable = true)
    private Provider provider;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    private List<Device> devices = new ArrayList<>();

    public User() {
    }

    public User(Long id,
                Set<Role> roles,
                String fullName,
                String username,
                String email,
                String password,
                Provider provider,
                List<Device> devices) {
        this.id = id;
        this.roles = roles;
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.provider = provider;
        this.devices = devices;
    }

    @PrePersist
    @PreUpdate
    private void validateUser() {
        boolean passwordPresent = password != null && !password.isEmpty();
        boolean providerPresent = (provider != null);

        // Controllo che non siano presenti entrambi
        if (passwordPresent && providerPresent) {
            throw new IllegalArgumentException(
                    "Users can't authenticate with BOTH username/password and an OAuth2 provider."
            );
        }

        // Controllo che almeno uno sia presente
        if (!passwordPresent && !providerPresent) {
            throw new IllegalArgumentException(
                    "Users must authenticate with EITHER username/password OR an OAuth2 provider."
            );
        }
    }
}