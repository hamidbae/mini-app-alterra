package com.alterra.miniapp.domain.common;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class BaseIsDeleted extends BaseUpdatedAt{
    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @PrePersist
    void onCreate(){
        this.isDeleted = Boolean.FALSE;
        this.setCreatedAt(LocalDateTime.now());
    }
}
