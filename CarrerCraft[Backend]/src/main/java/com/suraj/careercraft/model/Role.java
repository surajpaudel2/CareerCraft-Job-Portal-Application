package com.suraj.careercraft.model;

import com.suraj.careercraft.model.enums.RoleName;
import lombok.*;
import jakarta.persistence.*;

@Entity
@Table(name = "roles")
@Data
@Builder
@AllArgsConstructor
public class Role {
    public Role() {
    }

    public Role(RoleName roleName) {
        this.name = roleName;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private RoleName name;  // Enum to represent role name
}
