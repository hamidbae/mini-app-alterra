package com.alterra.miniapp.repository;

import com.alterra.miniapp.domain.dao.Plant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlantRepository extends JpaRepository<Plant, Long> {
    @Query("SELECT p FROM Plant p WHERE p.name LIKE %?1%"
            + " OR p.content LIKE %?1%"
            + " ORDER BY p.createdAt DESC")
    public List<Plant> searchPlant(String keyword);
}
