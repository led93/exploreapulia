package com.example.ep.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "security_role")
@Getter
@Setter
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "authority")
    private String authority;

    @Column(name = "description")
    private String description;

    @Override
    public String getAuthority() {
        return authority;
    }

}
