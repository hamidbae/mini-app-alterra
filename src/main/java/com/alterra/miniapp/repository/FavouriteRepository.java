package com.alterra.miniapp.repository;

import com.alterra.miniapp.domain.dao.Comment;
import com.alterra.miniapp.domain.dao.Favourite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface FavouriteRepository extends JpaRepository<Favourite, Long> {
    @Query("SELECT f FROM Favourite f WHERE f.plant.id = ?1")
    public Set<Favourite> searchByPlantId(Long id);
}
