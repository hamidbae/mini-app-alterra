package com.alterra.miniapp.domain.dao;


import com.alterra.miniapp.domain.common.BaseTimestamp;
import com.alterra.miniapp.domain.common.BaseUpdatedAt;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@SuperBuilder
@Table(name = "plants")
@AllArgsConstructor
@NoArgsConstructor
public class Plant extends BaseUpdatedAt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String speciesName;

    @Column
    private String content;
}
