package com.alterra.miniapp.repository;

import com.alterra.miniapp.domain.dao.Collection;
import com.alterra.miniapp.domain.dao.Plant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CollectionRepository extends JpaRepository<Collection, Long> {
    @Query("SELECT c FROM Collection c WHERE c.user.id=?1"
            + " ORDER BY c.createdAt DESC")
    public List<Collection> findByUserId(Long id);

    @Query("SELECT c FROM Collection c WHERE c.user.id=?1 AND c.plant.id=?2")
    public List<Collection> findByUserIdPlantId(Long userId, Long plantId);
}
