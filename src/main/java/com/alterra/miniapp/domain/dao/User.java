package com.alterra.miniapp.domain.dao;

import com.alterra.miniapp.domain.common.BaseCreatedAt;
import com.alterra.miniapp.domain.common.BaseIsDeleted;
import com.alterra.miniapp.domain.common.BaseUpdatedAt;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@SuperBuilder
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = "username")})
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE users SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted = false")
public class User extends BaseIsDeleted {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    @JsonIgnore
    private String password;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(  name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Comment> comments;

    @JsonIgnore
    @OneToMany(mappedBy = "user")
    private List<Favourite> favourites;
}
