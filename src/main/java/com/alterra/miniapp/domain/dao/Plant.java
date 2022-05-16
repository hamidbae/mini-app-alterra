package com.alterra.miniapp.domain.dao;


import com.alterra.miniapp.domain.common.BaseTimestamp;
import com.alterra.miniapp.domain.common.BaseUpdatedAt;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.List;

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

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "plant", targetEntity = Comment.class, cascade = CascadeType.ALL)
    private List<Comment> comments;

    @LazyCollection(LazyCollectionOption.FALSE)
    @OneToMany(mappedBy = "plant", targetEntity = Favourite.class, cascade = CascadeType.ALL)
    private List<Favourite> favourites;
}
