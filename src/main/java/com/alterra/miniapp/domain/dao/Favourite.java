package com.alterra.miniapp.domain.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Builder
@Table(name = "favourites")
@AllArgsConstructor
@NoArgsConstructor
public class Favourite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "plant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Plant plant;
}
