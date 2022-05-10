package com.alterra.miniapp.domain.dao;

import com.alterra.miniapp.domain.common.BaseTimestamp;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Getter
@Setter
@Entity
@SuperBuilder
@Table(name = "comments")
@AllArgsConstructor
@NoArgsConstructor
public class Comment extends BaseTimestamp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne
    @JoinColumn(name = "plant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Plant plant;

    @Column
    private String text;
}
